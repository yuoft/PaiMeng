package com.yuo.paimeng.entity;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.entity.AI.HypostasisHoverGoal;
import com.yuo.paimeng.entity.AI.HypostasisMeleeGoal;
import com.yuo.paimeng.entity.AI.HypostasisRangedGoal;
import com.yuo.paimeng.entity.AI.HypostasisThunderGoal;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.FlyingMob;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 无相之岩：2×2×2 矿物方块外壳 + 倾斜基岩核心的浮空 BOSS。
 *
 * <p>战斗循环（服务端驱动，状态经同步数据传给客户端渲染器）：
 * <ul>
 *   <li>近战三种形态：一字型扫击、十字型镇压、X 型砸击，命中判定覆盖身前三到六格、横向四格内的生物，
 *       并把落点硬度小于 10 的方块击碎（正常掉落）。</li>
 *   <li>远程：向远处目标抛掷随机矿物方块（真正的实体投射物）。</li>
 *   <li>雷击：以 BOSS 为中心的 3/6/9 格三个环，分别释放 8/16/24 道雷击（共 48 道），
 *       命中同一生物只造成一次伤害。</li>
 * </ul>
 * 半血进入二阶段：外壳散开、移动与出招更快、雷击伤害提升。
 */
public class HypostasisEntity extends FlyingMob {

    /** 外壳方块数量（2×2×2）。 */
    public static final int SHELL_COUNT = 8;

    /** 战利品表路径常量，避免每次创建新对象。 */
    private static final ResourceLocation LOOT_TABLE = ModUtils.fa( "entities/hypostasis");

    /** 雷击环参数：每个环的 {半径, 落雷点数}。 */
    private static final double[] THUNDER_RING_RADII = {3.0D, 6.0D, 9.0D};
    private static final int[] THUNDER_RING_POINTS = {8, 16, 24};

    /** 出招阶段枚举，客户端渲染器据此播放不同形态动画。 */
    public enum AttackState {
        IDLE,
        MELEE_LINE,
        MELEE_CROSS,
        MELEE_X,
        RANGED,
        THUNDER
    }

    /** 各形态总时长（tick），渲染器与 AI 共用。 */
    public static final int DUR_MELEE = 34;
    public static final int DUR_RANGED = 46;
    public static final int DUR_THUNDER = 58;
    /** 近战砸击命中帧。 */
    public static final int MELEE_IMPACT_TICK = 23;
    /** 远程每次抛掷方块的时间点。 */
    public static final int[] RANGED_FIRE_TICKS = {8, 18, 28};
    /** 雷击落下的帧。 */
    public static final int THUNDER_IMPACT_TICK = 34;
    /** 飞行清路：硬度低于该值的方块会被撞碎（树叶 0.2、花草 0、雪片 0.1、玻璃 0.3…；泥土/沙子 0.5 不会被破坏）。 */
    private static final float PATH_CLEAR_MAX_HARDNESS = 0.6F;
    /** 每 tick 最多破坏的方块数，防止穿林时瞬间刷爆区块更新。 */
    private static final int PATH_CLEAR_LIMIT = 6;

    private static final EntityDataAccessor<Integer> DATA_STATE = SynchedEntityData.defineId(HypostasisEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_PHASE = SynchedEntityData.defineId(HypostasisEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> DATA_ANIM = SynchedEntityData.defineId(HypostasisEntity.class, EntityDataSerializers.INT);

    private final ServerBossEvent bossEvent = (ServerBossEvent) (new ServerBossEvent(
            this.getDisplayName(), BossEvent.BossBarColor.PURPLE, BossEvent.BossBarOverlay.PROGRESS)).setDarkenScreen(true);

    private boolean attacking;
    private int attackTicks;
    private int attackDuration;
    private int attackCooldown;
    private int ambientSoundCooldown;

    public HypostasisEntity(EntityType<? extends FlyingMob> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        this.noPhysics = false;
        this.setPersistenceRequired();
        this.xpReward = 150;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Monster.createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 450.0D)
                .add(Attributes.ATTACK_DAMAGE, 13.0D)
                .add(Attributes.ARMOR, 10.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 4.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FLYING_SPEED, 0.45D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.FOLLOW_RANGE, 48.0D);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_STATE, AttackState.IDLE.ordinal());
        this.entityData.define(DATA_PHASE, 1);
        this.entityData.define(DATA_ANIM, 0);
    }

    @Override
    protected void registerGoals() {
        // 🔴 重新排定优先级：近战 > 远程 > 雷击 > 悬浮
        this.goalSelector.addGoal(0, new HypostasisMeleeGoal(this));     // 水平 6 格内
        this.goalSelector.addGoal(1, new HypostasisRangedGoal(this));    // 8~28 格
        this.goalSelector.addGoal(2, new HypostasisThunderGoal(this));   // 4~10 格 + 概率 CD
        this.goalSelector.addGoal(3, new HypostasisHoverGoal(this));     // 悬浮跟随
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    // ---------------------------------------------------------------- 同步数据

    public AttackState getAttackState() {
        return AttackState.values()[Mth.clamp(this.entityData.get(DATA_STATE), 0, AttackState.values().length - 1)];
    }

    private void setAttackState(AttackState state) {
        this.entityData.set(DATA_STATE, state.ordinal());
    }

    public int getPhase() {
        return this.entityData.get(DATA_PHASE);
    }

    private void setPhase(int phase) {
        this.entityData.set(DATA_PHASE, phase);
    }

    /** 当前出招已进行的 tick 数，渲染器用它算动画进度。 */
    public int getAnimTick() {
        return this.entityData.get(DATA_ANIM);
    }

    private void setAnimTick(int tick) {
        this.entityData.set(DATA_ANIM, tick);
    }

    // ---------------------------------------------------------------- 出招调度

    public boolean isIdle() {
        return !this.attacking && getAttackState() == AttackState.IDLE;
    }

    public boolean canStartAttack() {
        return isIdle() && this.attackCooldown <= 0 && this.getTarget() != null;
    }

    /** 由 AI 目标调用：开始一次出招。 */
    public boolean startAttack(AttackState state) {
        if (!canStartAttack()) return false;
        LivingEntity target = this.getTarget();
        if (target == null) return false; // 防御性检查，防止其他代码直接调用引发 NPE
        this.attacking = true;
        this.attackTicks = 0;
        this.attackDuration = state == AttackState.RANGED ? DUR_RANGED : state == AttackState.THUNDER ? DUR_THUNDER : DUR_MELEE;
        this.setAttackState(state);
        this.setAnimTick(0);
        this.getLookControl().setLookAt(target, 60.0F, 40.0F);
        this.level().playSound(null, this.blockPosition(), SoundEvents.STONE_PLACE, SoundSource.HOSTILE, 1.2F, 0.6F);
        return true;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            spawnClientParticles();
            return;
        }

        // 🔴 兜底：万一被指令传送 / 地形变化卡进方块，强制顶回地表之上
        this.ensureAboveGround();
        // 🔴 撞碎移动路径上的软方块（树叶、花草、雪片等），避免被卡住
        this.clearMovementPath();

        if (this.attacking) {
            // 🔴 近战攻击时，强制停止 BOSS 的移动，使其僵在空中
            AttackState state = getAttackState();
            if (state == AttackState.MELEE_LINE || state == AttackState.MELEE_CROSS || state == AttackState.MELEE_X
                    || state == AttackState.THUNDER) {
                this.setDeltaMovement(Vec3.ZERO);
            }

            this.attackTicks++;
            this.setAnimTick(this.attackTicks);
            this.onAttackTick(this.attackTicks);
            if (this.attackTicks >= this.attackDuration) {
                finishAttack();
            }
        } else if (this.attackCooldown > 0) {
            this.attackCooldown--;
        }

        if (this.ambientSoundCooldown > 0) this.ambientSoundCooldown--;

        if (this.getPhase() == 1 && this.getHealth() <= this.getMaxHealth() * 0.5F) {
            enterPhaseTwo();
        }
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());
    }

    private void onAttackTick(int tick) {
        AttackState state = getAttackState();
        switch (state) {
            case MELEE_LINE, MELEE_CROSS, MELEE_X -> {
                if (tick == MELEE_IMPACT_TICK) {
                    performMeleeImpact(state);
                }
            }
            case RANGED -> {
                for (int fireTick : RANGED_FIRE_TICKS) {
                    if (tick == fireTick) {
                        fireBlockProjectile();
                    }
                }
            }
            case THUNDER -> {
                if (tick == THUNDER_IMPACT_TICK) {
                    performThunderRings();
                }
            }
            default -> {
            }
        }
    }

    private void finishAttack() {
        AttackState finished = getAttackState();
        this.attacking = false;
        this.setAttackState(AttackState.IDLE);
        this.setAnimTick(0);
        boolean fast = getPhase() >= 2;
        this.attackCooldown = switch (finished) {
            case MELEE_LINE, MELEE_CROSS, MELEE_X -> fast ? 22 : 36;
            case RANGED -> fast ? 34 : 52;
            case THUNDER -> fast ? 46 : 72;
            default -> 20;
        };
    }

    // ---------------------------------------------------------------- 近战：一字 / 十字 / X
    private void performMeleeImpact(AttackState state) {
        Vec3 forward = horizontalForward();
        Vec3 right = new Vec3(-forward.z, 0.0D, forward.x);
        Vec3 origin = new Vec3(this.getX(), this.getY() + 1.0D, this.getZ());

        float multiplier = state == AttackState.MELEE_X ? 1.2F
                : state == AttackState.MELEE_CROSS ? 0.85F : 1.0F;
        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * multiplier;

        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(8.0D), t -> t != this && t.isAlive() && !(t instanceof HypostasisEntity));

        for (LivingEntity target : targets) {
            Vec3 delta = target.position().subtract(origin);
            double f = delta.dot(forward);
            double r = delta.dot(right);
            if (!isInMeleeArea(state, f, r, delta.y)) continue;
            if (target instanceof Player player && (player.isCreative() || player.isSpectator())) continue;

            target.hurt(this.damageSources().mobAttack(this), damage);
            double strength = getPhase() >= 2 ? 1.2D : 0.85D;
            target.knockback(strength, target.getX() - this.getX(), target.getZ() - this.getZ());
            target.setDeltaMovement(target.getDeltaMovement().add(0.0D, 0.45D, 0.0D));
            target.hurtMarked = true;
        }

        breakTerrain(state, forward, right, origin);
        playImpactEffects(origin.add(forward.scale(3.0D)));
    }

    /** 命中判定：以自身为原点、朝向为 f 轴、右侧为 r 轴的局部坐标系。 */
    private boolean isInMeleeArea(AttackState state, double f, double r, double dy) {
        if (f < 0.5D || f > 6.0D) return false;
        if (Math.abs(dy) > 5.0D) return false;
        return switch (state) {
            case MELEE_LINE -> Math.abs(r) <= 3.9D;
            case MELEE_CROSS -> Math.abs(r) <= 3.2D && f >= 1.2D;
            default -> Math.abs(r) <= 4.4D;
        };
    }

    /** 砸击落点：把硬度 0~10 的方块击碎并正常掉落（基岩、带方块实体的方块、流体不受影响）。 */
    private void breakTerrain(AttackState state, Vec3 forward, Vec3 right, Vec3 origin) {
        int broken = 0;
        int maxBroken = getPhase() >= 2 ? 14 : 10;
        for (double f = 1.0D; f <= 6.0D && broken < maxBroken; f += 1.0D) {
            double halfWidth = state == AttackState.MELEE_LINE ? 3.5D : 3.0D;
            for (double r = -halfWidth; r <= halfWidth && broken < maxBroken; r += 1.0D) {
                if (state == AttackState.MELEE_X && Math.abs(r) > f) continue;
                if (state == AttackState.MELEE_CROSS && f > 2.5D && Math.abs(r) > 1.5D) continue;

                double x = origin.x + forward.x * f + right.x * r;
                double z = origin.z + forward.z * f + right.z * r;
                int yTop = Mth.floor(origin.y) - 1;

                for (int dy = 1; dy >= -3; dy--) {
                    BlockPos pos = new BlockPos(Mth.floor(x), yTop + dy, Mth.floor(z));
                    BlockState blockState = this.level().getBlockState(pos);
                    if (blockState.isAir() || !blockState.getFluidState().isEmpty()) continue;

                    float hardness = blockState.getDestroySpeed(this.level(), pos);
                    // 硬度 >= 0 已经排除基岩（-1），无需额外判断 Blocks.BEDROCK
                    if (hardness >= 0.0F && hardness < 10.0F && this.level().getBlockEntity(pos) == null) {
                        this.level().destroyBlock(pos, true, this);
                        broken++;
                    }
                    break;
                }
            }
        }
    }

    private void playImpactEffects(Vec3 center) {
        if (this.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.EXPLOSION, center.x, center.y - 0.6D, center.z,
                    5, 1.4D, 0.2D, 1.4D, 0.0D);
            server.sendParticles(ParticleTypes.CLOUD, center.x, center.y - 0.5D, center.z,
                    30, 2.2D, 0.3D, 2.2D, 0.08D);
            server.sendParticles(ParticleTypes.CRIT, center.x, center.y, center.z,
                    16, 1.8D, 0.6D, 1.8D, 0.4D);
        }
        this.level().playSound(null, this.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.HOSTILE, 2.0F, 0.55F);
        this.level().playSound(null, this.blockPosition(), SoundEvents.STONE_BREAK, SoundSource.HOSTILE, 1.6F, 0.5F);
    }

    // ---------------------------------------------------------------- 远程：抛掷随机方块

    private void fireBlockProjectile() {
        LivingEntity target = this.getTarget();
        if (target == null) return;

        HypostasisProjectile projectile = new HypostasisProjectile(PMEntityTypes.HYPOSTASIS_PROJECTILE.get(), this.level());
        Vec3 eye = new Vec3(this.getX(), this.getY() + 1.1D, this.getZ());
        projectile.moveTo(eye.x, eye.y, eye.z, this.getYRot(), this.getXRot());
        projectile.setBlockIndex(this.random.nextInt(HypostasisProjectile.SHELL_BLOCKS.length));
        projectile.setOwner(this);
        projectile.setDamage(getPhase() >= 2 ? 10.0F : 8.0F);

        // 简单预判：按目标当前速度外推 10 tick，让命中更精准
        Vec3 aimPos = target.getEyePosition().add(target.getDeltaMovement().scale(10.0D));
        Vec3 direction = aimPos.subtract(eye).normalize();
        projectile.shoot(direction.x, direction.y, direction.z, 1.35F, 3.0F);
        this.level().addFreshEntity(projectile);

        this.level().playSound(null, this.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.HOSTILE, 1.2F, 0.6F);
    }

    /**
     * 环状落雷：以 BOSS 为中心的 3 / 6 / 9 格处固定点位落雷。
     * 不检查生物，而是直接对雷击点周围 2 格内的生物造成伤害。
     */
    private void performThunderRings() {
        double centerX = this.getX();
        double centerZ = this.getZ();
        Set<LivingEntity> victimsToStrike = new HashSet<>();

        for (int ringIdx = 0; ringIdx < THUNDER_RING_RADII.length; ringIdx++) {
            double radius = THUNDER_RING_RADII[ringIdx];
            int points = THUNDER_RING_POINTS[ringIdx];
            double angleOffset = ringIdx * (Math.PI / points);

            for (int i = 0; i < points; i++) {
                double angle = (Math.PI * 2.0D * i) / points + angleOffset;
                double x = centerX + Math.cos(angle) * radius;
                double z = centerZ + Math.sin(angle) * radius;

                // 🔴 获取固定点位的地表高度
                int groundY = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, (int)Math.floor(x), (int)Math.floor(z));
                double strikeY = groundY + 0.1D;

                // 生成视觉闪电实体
                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.level());
                if (bolt != null) {
                    bolt.moveTo(x, strikeY, z);
                    bolt.setVisualOnly(true); // 仅作视觉，伤害由下方逻辑判定
                    this.level().addFreshEntity(bolt);
                }

                // 视觉粒子：落雷点火花
                if (this.level() instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, strikeY + 0.2D, z, 6, 0.3D, 0.8D, 0.3D, 0.08D);
                }

                // 判定该点位周围 2 格的生物
                List<LivingEntity> victims = this.level().getEntitiesOfClass(LivingEntity.class,
                        new AABB(x - 2.0D, strikeY - 8.0D, z - 2.0D, x + 2.0D, strikeY + 10.0D, z + 2.0D),
                        t -> t != this && t.isAlive() && !(t instanceof HypostasisEntity));

                for (LivingEntity victim : victims) {
                    if (victim instanceof Player player && (player.isCreative() || player.isSpectator())) continue;
                    // 用 Set 去重，确保同一实体在一次雷击中只受 1 次伤害
                    if (victimsToStrike.add(victim)) {
                        victim.hurt(this.damageSources().mobAttack(this), getPhase() >= 2 ? 10.0F : 8.0F);
                        victim.setSecondsOnFire(2);
                    }
                }
            }
        }

        this.level().playSound(null, this.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 2.2F, 1.0F);
    }

    /**
     * 强制中止当前出招：立即回到待机并给一个短冷却。
     * 用于目标死亡 / 逃出范围时取消砸击，避免对空气造成伤害和地形破坏。
     */
    public void abortAttack() {
        if (!this.attacking) return;
        this.attacking = false;
        this.setAttackState(AttackState.IDLE);
        this.setAnimTick(0);
        this.attackTicks = 0;
        this.attackCooldown = 20;
    }

    /**
     * 🔴 防止遁地兜底：若 BOSS 被卡进方块内部（被指令传送、周围地形被修改等），
     * 直接把它抬到该列地表上方，避免它在地底穿模或徘徊。
     */
    private void ensureAboveGround() {
        if (this.level().isClientSide) return;

        double centerX = this.getX();
        double centerZ = this.getZ();
        BlockPos feetPos = BlockPos.containing(centerX, this.getY() + 0.1D, centerZ);
        BlockPos bodyPos = BlockPos.containing(centerX, this.getY() + this.getBbHeight() * 0.5D, centerZ);

        boolean embedded = hasBlockCollision(feetPos) || hasBlockCollision(bodyPos);
        if (!embedded) return;

        int surfaceY = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Mth.floor(centerX), Mth.floor(centerZ));
        double safeY = Math.max(surfaceY + 0.05D, this.level().getMinBuildHeight() + 3.0D);
        if (this.getY() < safeY) {
            this.setPos(centerX, safeY, centerZ);
            this.setDeltaMovement(this.getDeltaMovement().x, 0.0D, this.getDeltaMovement().z);
        }
    }

    /** 该坐标是否被方块碰撞箱占据（流体、空气不算）。 */
    private boolean hasBlockCollision(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        if (state.isAir()) return false;
        return !state.getCollisionShape(this.level(), pos).isEmpty();
    }

    /**
     * 🔴 飞行清路：撞碎包围盒范围内硬度低于 {@link #PATH_CLEAR_MAX_HARDNESS} 的方块，
     * 让 BOSS 能穿过树叶、花丛、雪层等软阻挡，同时不会啃掉泥土、沙子、石头和地形。
     * <p>不掉落方块（避免走过森林时刷一地树叶）；想让它掉材料可把 destroyBlock 的第二个参数改成 true。
     */
    private void clearMovementPath() {
        if (this.level().isClientSide) return;

        // 略微外扩 0.05：贴住软方块时让判定"咬"进对方一点点，逐层吃掉阻挡
        AABB box = this.getBoundingBox().inflate(0.05D);
        int minX = Mth.floor(box.minX);
        int maxX = Mth.floor(box.maxX);
        int minY = Mth.floor(box.minY);
        int maxY = Mth.floor(box.maxY);
        int minZ = Mth.floor(box.minZ);
        int maxZ = Mth.floor(box.maxZ);

        int broken = 0;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    cursor.set(x, y, z);
                    BlockState state = this.level().getBlockState(cursor);
                    if (state.isAir() || !state.getFluidState().isEmpty()) continue;

                    float hardness = state.getDestroySpeed(this.level(), cursor);
                    // 基岩(-1) 等负硬度跳过；泥土/沙子(0.5) 及以上不破坏
                    if (hardness < 0.0F || hardness >= PATH_CLEAR_MAX_HARDNESS) continue;
                    // 带方块实体的方块（箱子、熔炉、刷怪笼等）不破坏
                    if (this.level().getBlockEntity(cursor) != null) continue;

                    this.level().destroyBlock(cursor, false, this);
                    if (++broken >= PATH_CLEAR_LIMIT) return;
                }
            }
        }
    }

    // ---------------------------------------------------------------- 二阶段

    private void enterPhaseTwo() {
        setPhase(2);
        this.attacking = false;
        this.attackCooldown = 20;
        this.setAttackState(AttackState.IDLE);
        this.setAnimTick(0);

        if (this.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY() + 1.0D, this.getZ(),
                    1, 0.0D, 0.0D, 0.0D, 0.0D);
            server.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY() + 1.0D, this.getZ(),
                    60, 1.6D, 1.6D, 1.6D, 0.12D);
        }
        this.level().playSound(null, this.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 1.8F, 0.6F);

        // 崩解冲击：把贴身的生物推开，给玩家一个退后的窗口
        for (LivingEntity nearby : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(7.0D),
                t -> t != this && t.isAlive() && !(t instanceof HypostasisEntity))) {
            if (nearby instanceof Player player && (player.isCreative() || player.isSpectator())) continue;
            nearby.hurt(this.damageSources().mobAttack(this), 4.0F);
            nearby.knockback(1.6D, nearby.getX() - this.getX(), nearby.getZ() - this.getZ());
        }
    }

    // ---------------------------------------------------------------- 移动与外观

    /** 朝目标点平滑飞行（AI 每 tick 调用）。 */
    public void hoverTowards(Vec3 desired, double speed) {
        Vec3 delta = desired.subtract(this.position());
        if (delta.lengthSqr() > 1.0E-4D) {
            double scale = Math.min(speed, delta.length());
            this.setDeltaMovement(delta.normalize().scale(scale));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        // 自由飞行：不吃重力、不吃地面摩擦;与方块的挤压交给 move 处理（已开启碰撞）
        this.move(MoverType.SELF, this.getDeltaMovement());

        // 🔴 防止 noPhysics 状态下钻进地底：低于世界最低可建造高度 +5 时强制上浮
        int minY = this.level().getMinBuildHeight() + 5;
        if (this.getY() < minY) {
            this.setPos(this.getX(), minY, this.getZ());
            this.setDeltaMovement(this.getDeltaMovement().x, Math.max(0.1D, this.getDeltaMovement().y), this.getDeltaMovement().z);
        }
    }

    private Vec3 horizontalForward() {
        float yaw = this.getYRot() * ((float) Math.PI / 180.0F);
        return new Vec3(-Mth.sin(yaw), 0.0D, Mth.cos(yaw));
    }

    private void spawnClientParticles() {
        int interval = getPhase() >= 2 ? 3 : 7;
        if (this.random.nextInt(interval) != 0) return;
        double spread = getPhase() >= 2 ? 1.1D : 0.8D;
        this.level().addParticle(ParticleTypes.END_ROD,
                this.getX() + (this.random.nextDouble() - 0.5D) * spread,
                this.getY() + 1.0D + (this.random.nextDouble() - 0.5D) * spread,
                this.getZ() + (this.random.nextDouble() - 0.5D) * spread,
                0.0D, 0.01D, 0.0D);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        this.bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        this.bossEvent.removePlayer(player);
    }

    // ---------------------------------------------------------------- 杂项

    /**
     * 判断核心是否处于暴露状态。
     * 在近战攻击的前摇和砸击瞬间，外壳防护减弱，核心会停顿暴露。
     */
    public boolean isCoreExposed() {
        AttackState state = getAttackState();
        if (state == AttackState.MELEE_LINE || state == AttackState.MELEE_CROSS || state == AttackState.MELEE_X) {
            int tick = getAnimTick();
            // 在命中帧(23)的前后 10 tick 内是暴露期
            return tick >= MELEE_IMPACT_TICK - 10 && tick <= MELEE_IMPACT_TICK + 8;
        }
        return false;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        // 🔴 判断是否为远程伤害（箭矢、投掷物、自定义的 HypostasisProjectile）
        boolean isRanged = source.is(DamageTypes.ARROW)
                || source.is(DamageTypes.THROWN)
                || source.is(DamageTypes.MOB_PROJECTILE)
                || source.getDirectEntity() instanceof AbstractArrow
                || source.getDirectEntity() instanceof Projectile;

        if (isRanged) {
            if (!isCoreExposed()) {
                // 🔴 未暴露时，远程伤害减免 90%
                amount *= 0.1F;
                // 播放金属格挡音效和火花
                this.level().playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1.2F, 0.6F);
                if (this.level() instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.ENCHANTED_HIT, this.getX(), this.getY() + 1.0D, this.getZ(),
                            10, 1.0D, 1.0D, 1.0D, 0.1D);
                }
            } else {
                // 🔴 核心暴露时，受到全额伤害，附带金属受击音效
                this.level().playSound(null, this.blockPosition(), SoundEvents.ANVIL_PLACE, SoundSource.HOSTILE, 1.0F, 1.2F);
                if (this.level() instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY() + 1.0D, this.getZ(),
                            15, 1.0D, 1.0D, 1.0D, 0.2F);
                }
            }
        }

        boolean hurt = super.hurt(source, amount);
        if (hurt && source.getEntity() instanceof LivingEntity attacker && !(attacker instanceof HypostasisEntity)) {
            this.setTarget(attacker);
        }
        return hurt;
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int loot, boolean isPlayer) {
        super.dropCustomDeathLoot(source, loot, isPlayer);
//        this.spawnAtLocation(new ItemStack(Items.DIAMOND_BLOCK));
    }

    @Override
    protected ResourceLocation getDefaultLootTable() {
        return LOOT_TABLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.IRON_GOLEM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.IRON_GOLEM_DEATH;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.STONE_BREAK;
    }

    @Override
    public int getAmbientSoundInterval() {
        return 160;
    }

    @Override
    protected float getSoundVolume() {
        return 1.4F;
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Phase", getPhase());
        // 便于 /data get entity 排查当前出招形态
        tag.putString("AttackState", getAttackState().name());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Phase")) {
            setPhase(Math.max(1, tag.getInt("Phase")));
        }
    }
}