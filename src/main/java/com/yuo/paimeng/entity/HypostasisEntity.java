package com.yuo.paimeng.entity;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.items.relics.RelicsHelper;
import com.yuo.paimeng.world.data.HypostasisBorderSavedData;
import com.yuo.paimeng.entity.AI.HypostasisHoverGoal;
import com.yuo.paimeng.entity.AI.HypostasisMeleeGoal;
import com.yuo.paimeng.entity.AI.HypostasisRangedGoal;
import com.yuo.paimeng.entity.AI.HypostasisSlamGoal;
import com.yuo.paimeng.entity.AI.HypostasisThunderGoal;
import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.items.relics.GodEyeType;
import com.yuo.paimeng.items.relics.RelicsType;
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
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.WeakHashMap;

/**
 * 无相之岩。
 * <p>关键特性：
 * <ul>
 *   <li>半血触发 PHASE_TRANSITION：核心落地无敌，外壳飞到 6 格外八顶点，3 波递增雷击 + <b>世界边界</b>封锁</li>
 *   <li>二阶段新增 MELEE_SLAM：飞到目标上方俯冲落地，爆炸 + 冲击波 + 击飞重力方块</li>
 * </ul>
 */
public class HypostasisEntity extends FlyingMob {

    public static final int SHELL_COUNT = 8;
    private static final ResourceLocation LOOT_TABLE = ModUtils.fa("entities/hypostasis");

    private static final double[] THUNDER_RING_RADII = {3.0D, 6.0D, 9.0D};
    private static final int[] THUNDER_RING_POINTS = {8, 16, 24};

    /** 世界边界半径（以核心为中心，边长 = 2 × 半径）。 */
    private static final double BARRIER_RADIUS = 6.0D;
    /** 8 个顶点在 6 格球面上的坐标偏移（6 / √3）。 */
    public static final float VERTEX_OFFSET = 3.4641F;

    public enum AttackState {
        IDLE, MELEE_LINE, MELEE_CROSS, MELEE_X, RANGED, THUNDER, PHASE_TRANSITION, MELEE_SLAM
    }

    public static final int DUR_MELEE = 34;
    public static final int DUR_RANGED = 46;
    public static final int DUR_THUNDER = 58;
    public static final int DUR_PHASE_TRANSITION = 160;
    public static final int DUR_MELEE_SLAM = 50;
    /** 两次俯冲砸地的最小间隔（tick），避免下落攻击刷屏。 */
    public static final int SLAM_INTERVAL = 240;

    public static final int MELEE_IMPACT_TICK = 23;
    public static final int[] RANGED_FIRE_TICKS = {8, 18, 28};
    public static final int THUNDER_IMPACT_TICK = 34;

    public static final int[] TRANSITION_THUNDER_TICKS = {40, 80, 120};
    public static final float[] TRANSITION_THUNDER_DAMAGES = {10.0F, 15.0F, 20.0F};
    /** 世界边界生效时机。 */
    public static final int BARRIER_SPAWN_TICK = 20;

    public static final int SLAM_RISE_START = 1;
    public static final int SLAM_RISE_END = 15;
    public static final int SLAM_HOVER_END = 20;
    public static final int SLAM_FALL_END = 30;
    public static final int SLAM_IMPACT_TICK = 31;

    private static final float PATH_CLEAR_MAX_HARDNESS = 0.6F;
    private static final int PATH_CLEAR_LIMIT = 6;

    /** 悬浮/移动时的转向速度（度/tick），二阶段更快。 */
    public static final float TURN_SPEED = 90.0F;
    public static final float TURN_SPEED_PHASE2 = 130.0F;

    /** 砸碎矿石/矿物块的回血表：下标为品质 0~5（点/块）。 */
    private static final float[] ORE_HEAL_BY_QUALITY = {0.0F, 2.0F, 4.0F, 6.0F, 10.0F, 16.0F};
    /** 单次砸击的吸血上限，避免在矿区里无限续航。 */
    private static final float MAX_ORE_HEAL_PER_SLAM = 30.0F;

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
    /** 俯冲砸地冷却（独立于普通出招冷却）。 */
    private int slamCooldown;

    private double slamStartY;

    // ============================================================
    //  🔴 世界边界保存与恢复
    // ============================================================
    /**
     * 边界锁：世界边界是整维度共享的，同一维度只允许一只 BOSS 持有，
     * 否则第二只 BOSS 会把「已改小的边界」当成原值保存，导致打完边界回不去。
     */
    private static final Map<ServerLevel, UUID> BORDER_OWNER = new WeakHashMap<>();

    private boolean borderSaved = false;
    private double originalBorderSize;
    private double originalBorderCenterX;
    private double originalBorderCenterZ;
    private double originalBorderDamagePerBlock;
    private double originalBorderDamageSafeZone;
    private int originalBorderWarningBlocks;
    private int originalBorderWarningTime;

    /** 读档时世界边界可能仍停在封锁尺寸，延迟一 tick 再恢复。 */
    private boolean borderRestorePending = false;

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
        this.goalSelector.addGoal(0, new HypostasisMeleeGoal(this));
        this.goalSelector.addGoal(1, new HypostasisSlamGoal(this));
        this.goalSelector.addGoal(2, new HypostasisRangedGoal(this));
        this.goalSelector.addGoal(3, new HypostasisThunderGoal(this));
        this.goalSelector.addGoal(4, new HypostasisHoverGoal(this));
        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, true));
    }

    // ------------------------- 同步数据
    public AttackState getAttackState() { return AttackState.values()[Mth.clamp(this.entityData.get(DATA_STATE), 0, AttackState.values().length - 1)]; }
    private void setAttackState(AttackState state) { this.entityData.set(DATA_STATE, state.ordinal()); }
    public int getPhase() { return this.entityData.get(DATA_PHASE); }
    private void setPhase(int phase) { this.entityData.set(DATA_PHASE, phase); }
    public int getAnimTick() { return this.entityData.get(DATA_ANIM); }
    private void setAnimTick(int tick) { this.entityData.set(DATA_ANIM, tick); }

    // ------------------------- 出招调度
    public boolean isIdle() { return !this.attacking && getAttackState() == AttackState.IDLE; }
    public boolean canStartAttack() { return isIdle() && this.attackCooldown <= 0 && this.getTarget() != null; }

    /** 俯冲砸地是否已冷却完毕（AI 用来限制下落攻击频率）。 */
    public boolean canStartSlam() { return this.slamCooldown <= 0; }

    public boolean startAttack(AttackState state) {
        if (!canStartAttack()) return false;
        LivingEntity target = this.getTarget();
        if (target == null) return false;
        // 近战必须真的够得着，否则不浪费一次出招（避免对空气挥击）
        if (isMeleeState(state) && !isMeleeTargetInFront()) return false;
        this.attacking = true;
        this.attackTicks = 0;
        this.attackDuration = switch (state) {
            case RANGED -> DUR_RANGED;
            case THUNDER -> DUR_THUNDER;
            case MELEE_SLAM -> DUR_MELEE_SLAM;
            case PHASE_TRANSITION -> DUR_PHASE_TRANSITION;
            default -> DUR_MELEE;
        };
        this.setAttackState(state);
        this.setAnimTick(0);
        this.slamStartY = this.getY();
        if (state == AttackState.MELEE_SLAM) {
            this.slamCooldown = SLAM_INTERVAL; // 起手就进入冷却，防止连续俯冲
        }
        this.getLookControl().setLookAt(target, TURN_SPEED_PHASE2, 90.0F);
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

        // 读档后若存档里残留封锁边界，先恢复
        if (this.borderRestorePending) {
            this.borderRestorePending = false;
            restoreStaleBorder();
        }

        this.ensureAboveGround();
        this.clearMovementPath();

        if (this.attacking) {
            AttackState state = getAttackState();
            if (isMeleeState(state)) {
                faceTarget(); // 蓄力期间持续对准目标，避免砸向空地
            }
            if (state == AttackState.MELEE_LINE || state == AttackState.MELEE_CROSS
                    || state == AttackState.MELEE_X || state == AttackState.THUNDER
                    || state == AttackState.PHASE_TRANSITION) {
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
        if (this.slamCooldown > 0) this.slamCooldown--;

        if (this.ambientSoundCooldown > 0) this.ambientSoundCooldown--;

        if (this.getPhase() == 1 && this.getHealth() <= this.getMaxHealth() * 0.5F) {
            enterPhaseTransition();
        }
        this.bossEvent.setProgress(this.getHealth() / this.getMaxHealth());

        // 封锁期间不许自己飞出边界
        clampInsideBarrier();
    }

    private void onAttackTick(int tick) {
        AttackState state = getAttackState();
        switch (state) {
            case MELEE_LINE, MELEE_CROSS, MELEE_X -> {
                if (tick == MELEE_IMPACT_TICK && !performMeleeImpact(state)) {
                    // 落点没有生物：直接收招，不再对空气砸地/破方块
                    abortAttack();
                }
            }
            case RANGED -> { for (int ft : RANGED_FIRE_TICKS) if (tick == ft) fireBlockProjectile(); }
            case THUNDER -> { if (tick == THUNDER_IMPACT_TICK) performThunderRings(getPhase() >= 2 ? 10.0F : 8.0F); }
            case PHASE_TRANSITION -> onTransitionTick(tick);
            case MELEE_SLAM -> onSlamTick(tick);
            default -> { }
        }
    }

    // ============================= 二阶段转变 =============================
    private void enterPhaseTransition() {
        setPhase(2);
        this.attacking = true;
        this.attackTicks = 0;
        this.attackDuration = DUR_PHASE_TRANSITION;
        this.setAttackState(AttackState.PHASE_TRANSITION);
        this.setAnimTick(0);
        this.setDeltaMovement(Vec3.ZERO);
        this.slamStartY = this.getY();

        this.level().playSound(null, this.blockPosition(), SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 2.5F, 0.5F);
        if (this.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY() + 1.0D, this.getZ(), 2, 0, 0, 0, 0);
        }
    }

    private void onTransitionTick(int tick) {
        for (int i = 0; i < TRANSITION_THUNDER_TICKS.length; i++) {
            if (tick == TRANSITION_THUNDER_TICKS[i]) {
                performThunderRings(TRANSITION_THUNDER_DAMAGES[i]);
                this.level().playSound(null, this.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 3.0F, 0.8F - i * 0.1F);
            }
        }

        // 🔴 使用世界边界代替屏障方块
        if (tick == BARRIER_SPAWN_TICK) {
            applyPhaseBorder();
            if (this.level() instanceof ServerLevel server) {
                server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, this.getX(), this.getY() + 1.0D, this.getZ(), 2, BARRIER_RADIUS, 1, BARRIER_RADIUS, 0);
            }
            this.level().playSound(null, this.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.HOSTILE, 3.0F, 0.6F);
        }

        if (tick == DUR_PHASE_TRANSITION - 1 && this.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.END_ROD, this.getX(), this.getY() + 1.0D, this.getZ(), 80, 1.5D, 1.5D, 1.5D, 0.15D);
            this.level().playSound(null, this.blockPosition(), SoundEvents.BEACON_ACTIVATE, SoundSource.HOSTILE, 2.0F, 1.2F);
        }
    }

    // ============================================================
    //  🔴 世界边界封锁：保存原边界 → 设置新边界 → 结束后恢复
    // ============================================================

    /** 封锁时的边界边长（方形边界，半径 × 2，多留 0.5 给玩家容错）。 */
    public static double barrierSize() {
        return BARRIER_RADIUS * 2.0D + 0.5D;
    }

    private void applyPhaseBorder() {
        if (!(this.level() instanceof ServerLevel server) || this.borderSaved) return;
        WorldBorder border = server.getWorldBorder();

        // 同维度已有其它实体持锁：不抢，避免把改小后的尺寸当成原值保存
        UUID owner = BORDER_OWNER.get(server);
        if (owner != null && !owner.equals(this.getUUID())) return;
        // 世界边界本来就比封锁范围小：既不放宽也不紧缩，直接跳过
        if (border.getSize() <= barrierSize()) return;

        this.originalBorderSize = border.getSize();
        this.originalBorderCenterX = border.getCenterX();
        this.originalBorderCenterZ = border.getCenterZ();
        this.originalBorderDamagePerBlock = border.getDamagePerBlock();
        this.originalBorderDamageSafeZone = border.getDamageSafeZone();
        this.originalBorderWarningBlocks = border.getWarningBlocks();
        this.originalBorderWarningTime = border.getWarningTime();
        this.borderSaved = true;
        BORDER_OWNER.put(server, this.getUUID());
        // 先把原值写进存档快照，再动边界：崩溃/关服后重启也能改回来
        HypostasisBorderSavedData.get(server).snapshot(border);

        border.setCenter(this.getX(), this.getZ());
        border.setSize(barrierSize());
        border.setWarningBlocks(3);
        border.setWarningTime(0);
        border.setDamagePerBlock(0.0D); // 不伤害，只靠推力限制活动范围
        border.setDamageSafeZone(0.0D);
    }

    /** 恢复原世界边界参数；只恢复自己持有的那份，不覆盖别人的改动。 */
    private void restorePhaseBorder() {
        if (!this.borderSaved) return;
        this.borderSaved = false;
        if (!(this.level() instanceof ServerLevel server)) return;

        UUID owner = BORDER_OWNER.get(server);
        if (owner != null && !owner.equals(this.getUUID())) {
            // 已被别的实体接管：原值不再是我们的，只丢弃本地记录
            return;
        }
        BORDER_OWNER.remove(server);

        WorldBorder border = server.getWorldBorder();
        border.setCenter(this.originalBorderCenterX, this.originalBorderCenterZ);
        border.setSize(this.originalBorderSize);
        border.setWarningBlocks(this.originalBorderWarningBlocks);
        border.setWarningTime(this.originalBorderWarningTime);
        border.setDamagePerBlock(this.originalBorderDamagePerBlock);
        border.setDamageSafeZone(this.originalBorderDamageSafeZone);
        HypostasisBorderSavedData.get(server).clear();
    }

    /**
     * 读档补救：存档里可能残留「被我们改小的边界」（服务器崩溃/关闭时正在封锁）。
     * 只有当当前边界确实等于封锁尺寸时才恢复，避免覆盖玩家/其它模组后来调过的值。
     */
    private void restoreStaleBorder() {
        if (!(this.level() instanceof ServerLevel server)) return;
        WorldBorder border = server.getWorldBorder();
        if (Math.abs(border.getSize() - barrierSize()) > 0.05D) return;
        UUID owner = BORDER_OWNER.get(server);
        if (owner != null && !owner.equals(this.getUUID())) return;

        BORDER_OWNER.remove(server);
        border.setCenter(this.originalBorderCenterX, this.originalBorderCenterZ);
        border.setSize(this.originalBorderSize);
        border.setWarningBlocks(this.originalBorderWarningBlocks);
        border.setWarningTime(this.originalBorderWarningTime);
        border.setDamagePerBlock(this.originalBorderDamagePerBlock);
        border.setDamageSafeZone(this.originalBorderDamageSafeZone);
        HypostasisBorderSavedData.get(server).clear();
    }

    /** 把 BOSS 夹在当前世界边界内（封锁阶段边界就是竞技场，平时则是世界边界本身）。 */
    private void clampInsideBarrier() {
        WorldBorder border = this.level().getWorldBorder();
        double margin = this.getBbWidth() * 0.5D + 0.5D;
        double minX = border.getMinX() + margin, maxX = border.getMaxX() - margin;
        double minZ = border.getMinZ() + margin, maxZ = border.getMaxZ() - margin;
        if (minX > maxX || minZ > maxZ) return;
        double x = Mth.clamp(this.getX(), minX, maxX);
        double z = Mth.clamp(this.getZ(), minZ, maxZ);
        if (x != this.getX() || z != this.getZ()) {
            this.setPos(x, this.getY(), z);
            this.setDeltaMovement(0.0D, this.getDeltaMovement().y, 0.0D);
        }
    }

    private double clampBarrierX(double x) {
        WorldBorder border = this.level().getWorldBorder();
        double margin = this.getBbWidth() * 0.5D + 0.5D;
        double min = border.getMinX() + margin, max = border.getMaxX() - margin;
        return min <= max ? Mth.clamp(x, min, max) : x;
    }

    private double clampBarrierZ(double z) {
        WorldBorder border = this.level().getWorldBorder();
        double margin = this.getBbWidth() * 0.5D + 0.5D;
        double min = border.getMinZ() + margin, max = border.getMaxZ() - margin;
        return min <= max ? Mth.clamp(z, min, max) : z;
    }

    // ============================= MELEE_SLAM 俯冲 =============================
    private void onSlamTick(int tick) {
        LivingEntity target = this.getTarget();
        if (target == null) {
            if (tick >= SLAM_RISE_START) this.setPos(this.getX(), this.slamStartY, this.getZ());
            return;
        }

        if (tick >= SLAM_RISE_START && tick <= SLAM_RISE_END) {
            double targetY = target.getY() + 12.0D;
            double newY = Mth.lerp((tick - SLAM_RISE_START) / (double) (SLAM_RISE_END - SLAM_RISE_START), this.slamStartY, targetY);
            this.setPos(this.getX(), newY, this.getZ());
            this.setDeltaMovement(Vec3.ZERO);
        } else if (tick > SLAM_RISE_END && tick <= SLAM_HOVER_END) {
            double k = (tick - SLAM_RISE_END) / (double) (SLAM_HOVER_END - SLAM_RISE_END);
            this.setPos(Mth.lerp(k, this.getX(), target.getX()), this.getY(), Mth.lerp(k, this.getZ(), target.getZ()));
            this.getLookControl().setLookAt(target, 90.0F, 90.0F);
            if (this.level() instanceof ServerLevel server) {
                server.sendParticles(ParticleTypes.SMOKE, this.getX(), this.getY() + 0.5D, this.getZ(), 4, 0.5D, 0.5D, 0.5D, 0.02D);
            }
        } else if (tick > SLAM_HOVER_END && tick <= SLAM_FALL_END) {
            double k = (tick - SLAM_HOVER_END) / (double) (SLAM_FALL_END - SLAM_HOVER_END);
            this.setPos(this.getX(), Mth.lerp(k, this.getY(), target.getY() + 0.5D), this.getZ());
        } else if (tick == SLAM_IMPACT_TICK) {
            performSlamImpact();
        }
    }

    private void performSlamImpact() {
        Vec3 center = this.position();
        float damage = getPhase() >= 2 ? 25.0F : 15.0F;

        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(6.0D), t -> t != this && t.isAlive() && !(t instanceof HypostasisEntity));
        for (LivingEntity target : targets) {
            if (target instanceof Player p && (p.isCreative() || p.isSpectator())) continue;
            target.hurt(this.damageSources().mobAttack(this), damage);
            target.knockback(2.8D, target.getX() - center.x, target.getZ() - center.z);
            target.setDeltaMovement(target.getDeltaMovement().add(0, 1.5D, 0));
            target.hurtMarked = true;
        }
        knockGravityBlocks(center, 6.0D);

        if (this.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, center.x, center.y + 0.5D, center.z, 2, 1.5D, 0.3D, 1.5D, 0);
            server.sendParticles(ParticleTypes.CLOUD, center.x, center.y + 0.5D, center.z, 100, 5.0D, 0.4D, 5.0D, 0.15D);
            server.sendParticles(ParticleTypes.CRIT, center.x, center.y + 0.5D, center.z, 60, 4.0D, 0.5D, 4.0D, 0.6D);
            server.sendParticles(ParticleTypes.EXPLOSION, center.x, center.y + 0.5D, center.z, 8, 3.0D, 0.5D, 3.0D, 0);
        }
        this.level().playSound(null, this.blockPosition(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 3.0F, 0.5F);
        this.level().playSound(null, this.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.HOSTILE, 2.0F, 0.4F);
        this.level().playSound(null, this.blockPosition(), SoundEvents.STONE_BREAK, SoundSource.HOSTILE, 2.5F, 0.4F);
    }

    private void knockGravityBlocks(Vec3 center, double radius) {
        if (!(this.level() instanceof ServerLevel server)) return;
        int minX = Mth.floor(center.x - radius), maxX = Mth.floor(center.x + radius);
        int minY = Mth.floor(center.y - radius), maxY = Mth.floor(center.y + radius);
        int minZ = Mth.floor(center.z - radius), maxZ = Mth.floor(center.z + radius);
        for (int x = minX; x <= maxX; x++) for (int y = minY; y <= maxY; y++) for (int z = minZ; z <= maxZ; z++) {
            double distSqr = center.distanceToSqr(x + 0.5, y + 0.5, z + 0.5);
            if (distSqr > radius * radius) continue;
            BlockPos pos = new BlockPos(x, y, z);
            BlockState state = this.level().getBlockState(pos);
            if (!isGravityBlock(state)) continue;
            FallingBlockEntity falling = FallingBlockEntity.fall(server, pos, state);
            if (falling != null) {
                double dx = x + 0.5 - center.x, dz = z + 0.5 - center.z;
                double len = Math.max(Math.sqrt(dx * dx + dz * dz), 0.1D);
                falling.setDeltaMovement(dx / len * 0.6D, 1.5D + random.nextDouble() * 0.5D, dz / len * 0.6D);
            }
        }
    }

    private boolean isGravityBlock(BlockState state) {
        Block b = state.getBlock();
        return b == Blocks.SAND || b == Blocks.RED_SAND || b == Blocks.GRAVEL
                || b == Blocks.ANVIL || b == Blocks.CHIPPED_ANVIL || b == Blocks.DAMAGED_ANVIL
                || b == Blocks.DRAGON_EGG
                || b == Blocks.WHITE_CONCRETE_POWDER || b == Blocks.ORANGE_CONCRETE_POWDER
                || b == Blocks.MAGENTA_CONCRETE_POWDER || b == Blocks.LIGHT_BLUE_CONCRETE_POWDER
                || b == Blocks.YELLOW_CONCRETE_POWDER || b == Blocks.LIME_CONCRETE_POWDER
                || b == Blocks.PINK_CONCRETE_POWDER || b == Blocks.GRAY_CONCRETE_POWDER
                || b == Blocks.LIGHT_GRAY_CONCRETE_POWDER || b == Blocks.CYAN_CONCRETE_POWDER
                || b == Blocks.PURPLE_CONCRETE_POWDER || b == Blocks.BLUE_CONCRETE_POWDER
                || b == Blocks.BROWN_CONCRETE_POWDER || b == Blocks.GREEN_CONCRETE_POWDER
                || b == Blocks.RED_CONCRETE_POWDER || b == Blocks.BLACK_CONCRETE_POWDER;
    }

    // ============================= 出招结束 =============================
    private void finishAttack() {
        AttackState finished = getAttackState();
        this.attacking = false;
        this.setAttackState(AttackState.IDLE);
        this.setAnimTick(0);
        // 🔴 无论是正常结束还是中断，都恢复世界边界
        if (finished == AttackState.PHASE_TRANSITION) {
            restorePhaseBorder();
        }
        boolean fast = getPhase() >= 2;
        this.attackCooldown = switch (finished) {
            case PHASE_TRANSITION -> 30;
            case MELEE_SLAM -> fast ? 30 : 50;
            case MELEE_LINE, MELEE_CROSS, MELEE_X -> fast ? 22 : 36;
            case RANGED -> fast ? 34 : 52;
            case THUNDER -> fast ? 46 : 72;
            default -> 20;
        };
    }

    public void abortAttack() {
        if (!this.attacking) return;
        // 🔴 中断 PHASE_TRANSITION 也要恢复世界边界
        if (getAttackState() == AttackState.PHASE_TRANSITION) {
            restorePhaseBorder();
        }
        this.attacking = false;
        this.setAttackState(AttackState.IDLE);
        this.setAnimTick(0);
        this.attackTicks = 0;
        this.attackCooldown = 20;
    }

    // ============================= 近战 / 远程 / 雷击 =============================
    /** 近战形态：一字 / 十字 / X */
    public static boolean isMeleeState(AttackState state) {
        return state == AttackState.MELEE_LINE || state == AttackState.MELEE_CROSS || state == AttackState.MELEE_X;
    }

    /** 当前目标是否站在近战可及范围内（水平 6 格内、身前、Y 差 5 内）。 */
    public boolean isMeleeTargetInFront() {
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) return false;
        Vec3 d = target.position().subtract(this.position());
        double horizontal = Math.sqrt(d.x * d.x + d.z * d.z);
        if (horizontal < 0.4D || horizontal > 6.0D) return false;
        if (Math.abs(d.y) > 5.0D) return false;
        Vec3 forward = horizontalForward();
        return (d.x * forward.x + d.z * forward.z) / horizontal > 0.1D;
    }

    /** 某个形态的命中区是否能覆盖到当前目标。 */
    private boolean canShapeHitTarget(AttackState shape) {
        LivingEntity target = this.getTarget();
        if (target == null || !target.isAlive()) return false;
        Vec3 forward = horizontalForward();
        Vec3 right = new Vec3(-forward.z, 0.0D, forward.x);
        Vec3 d = target.position().subtract(new Vec3(this.getX(), this.getY() + 1.0D, this.getZ()));
        return isInMeleeArea(shape, d.dot(forward), d.dot(right), d.y);
    }

    /**
     * 随机挑一个「确实能打到目标」的近战形态；一个都打不到就返回 null（此时不要出招，避免打空气）。
     */
    public AttackState pickMeleeShape() {
        AttackState[] shapes = {AttackState.MELEE_LINE, AttackState.MELEE_CROSS, AttackState.MELEE_X};
        int start = this.random.nextInt(shapes.length);
        for (int i = 0; i < shapes.length; i++) {
            AttackState shape = shapes[(start + i) % shapes.length];
            if (canShapeHitTarget(shape)) return shape;
        }
        return null;
    }

    /** 近战蓄力时把身体正对目标，保证落点判定跟着目标走。 */
    private void faceTarget() {
        LivingEntity target = this.getTarget();
        if (target == null) return;
        turnTo(target, 180.0F); // 蓄力锁定时几乎瞬间对准
    }

    /**
     * 朝目标快速转向（水平），每 tick 最多转 {@code maxStep} 度。
     * 直接改 yRot / yBodyRot / yHeadRot，避免 LookControl 默认 25°/tick 的慢转。
     */
    public void turnTo(LivingEntity target, float maxStep) {
        if (target == null) return;
        double dx = target.getX() - this.getX();
        double dz = target.getZ() - this.getZ();
        if (dx * dx + dz * dz < 1.0E-4D) return;
        float wanted = (float) (Mth.atan2(dz, dx) * (180.0D / Math.PI)) - 90.0F;
        float delta = Mth.wrapDegrees(wanted - this.getYRot());
        float step = Mth.clamp(delta, -maxStep, maxStep);
        float yaw = this.getYRot() + step;
        this.yBodyRotO = this.yBodyRot;
        this.yHeadRotO = this.getYHeadRot();
        this.setYRot(yaw);
        this.setYHeadRot(yaw);
        this.yBodyRot = yaw;
    }

    private boolean performMeleeImpact(AttackState state) {
        Vec3 forward = horizontalForward();
        Vec3 right = new Vec3(-forward.z, 0.0D, forward.x);
        Vec3 origin = new Vec3(this.getX(), this.getY() + 1.0D, this.getZ());
        float multiplier = state == AttackState.MELEE_X ? 1.2F : state == AttackState.MELEE_CROSS ? 0.85F : 1.0F;
        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE) * multiplier;
        boolean hitAny = false;

        List<LivingEntity> targets = this.level().getEntitiesOfClass(LivingEntity.class,
                this.getBoundingBox().inflate(8.0D), t -> t != this && t.isAlive() && !(t instanceof HypostasisEntity));
        for (LivingEntity target : targets) {
            Vec3 delta = target.position().subtract(origin);
            double f = delta.dot(forward), r = delta.dot(right);
            if (!isInMeleeArea(state, f, r, delta.y)) continue;
            hitAny = true; // 命中区里有生物（创造玩家也算，只是不受伤）
            if (target instanceof Player p && (p.isCreative() || p.isSpectator())) continue;
            target.hurt(this.damageSources().mobAttack(this), damage);
            double strength = getPhase() >= 2 ? 1.2D : 0.85D;
            target.knockback(strength, target.getX() - this.getX(), target.getZ() - this.getZ());
            target.setDeltaMovement(target.getDeltaMovement().add(0.0D, 0.45D, 0.0D));
            target.hurtMarked = true;
        }
        if (hitAny) {
            breakTerrain(state, forward, right, origin);
            playImpactEffects(origin.add(forward.scale(3.0D)));
        }
        return hitAny;
    }

    private boolean isInMeleeArea(AttackState state, double f, double r, double dy) {
        if (f < 0.5D || f > 6.0D || Math.abs(dy) > 5.0D) return false;
        return switch (state) {
            case MELEE_LINE -> Math.abs(r) <= 3.9D;
            case MELEE_CROSS -> Math.abs(r) <= 3.2D && f >= 1.2D;
            default -> Math.abs(r) <= 4.4D;
        };
    }

    private void breakTerrain(AttackState state, Vec3 forward, Vec3 right, Vec3 origin) {
        int broken = 0, maxBroken = getPhase() >= 2 ? 14 : 10;
        float heal = 0.0F;
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
                    if (hardness >= 0.0F && hardness < 10.0F && this.level().getBlockEntity(pos) == null) {
                        // 砸碎矿石/矿物块时按品质积攒回血
                        heal += ORE_HEAL_BY_QUALITY[oreQuality(blockState)];
                        this.level().destroyBlock(pos, true, this);
                        broken++;
                    }
                    break;
                }
            }
        }
        if (heal > 0.0F) {
            this.absorbOre(heal);
        }
    }

    /**
     * 矿石 / 原矿块 / 矿物块的品质分级（1~5，0 表示不回血）。
     * 1 煤铜 · 2 铁 · 3 金/红石/青金石/石英 · 4 钻石/绿宝石/紫水晶 · 5 远古残骸/下界合金
     */
    private static int oreQuality(BlockState state) {
        Block block = state.getBlock();
        // 1 级
        if (block == Blocks.COAL_ORE || block == Blocks.DEEPSLATE_COAL_ORE
                || block == Blocks.COPPER_ORE || block == Blocks.DEEPSLATE_COPPER_ORE
                || block == Blocks.COAL_BLOCK || block == Blocks.COPPER_BLOCK
                || block == Blocks.RAW_COPPER_BLOCK) {
            return 1;
        }
        // 2 级
        if (block == Blocks.IRON_ORE || block == Blocks.DEEPSLATE_IRON_ORE
                || block == Blocks.IRON_BLOCK || block == Blocks.RAW_IRON_BLOCK) {
            return 2;
        }
        // 3 级
        if (block == Blocks.GOLD_ORE || block == Blocks.DEEPSLATE_GOLD_ORE || block == Blocks.NETHER_GOLD_ORE
                || block == Blocks.REDSTONE_ORE || block == Blocks.DEEPSLATE_REDSTONE_ORE
                || block == Blocks.LAPIS_ORE || block == Blocks.DEEPSLATE_LAPIS_ORE
                || block == Blocks.NETHER_QUARTZ_ORE
                || block == Blocks.GOLD_BLOCK || block == Blocks.RAW_GOLD_BLOCK
                || block == Blocks.REDSTONE_BLOCK || block == Blocks.LAPIS_BLOCK || block == Blocks.QUARTZ_BLOCK) {
            return 3;
        }
        // 4 级
        if (block == Blocks.DIAMOND_ORE || block == Blocks.DEEPSLATE_DIAMOND_ORE
                || block == Blocks.EMERALD_ORE || block == Blocks.DEEPSLATE_EMERALD_ORE
                || block == Blocks.DIAMOND_BLOCK || block == Blocks.EMERALD_BLOCK
                || block == Blocks.AMETHYST_BLOCK) {
            return 4;
        }
        // 5 级
        if (block == Blocks.ANCIENT_DEBRIS || block == Blocks.NETHERITE_BLOCK) {
            return 5;
        }
        return 0;
    }

    /** 吸收矿石回血：封顶后治疗，并给出粒子/音效反馈。 */
    private void absorbOre(float amount) {
        float healValue = Math.min(amount, MAX_ORE_HEAL_PER_SLAM);
        float before = this.getHealth();
        this.heal(healValue);
        if (this.getHealth() <= before) return; // 已满血就不放特效

        if (this.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.HEART, this.getX(), this.getY() + 1.2D, this.getZ(),
                    6, 0.6D, 0.4D, 0.6D, 0.02D);
            server.sendParticles(ParticleTypes.HAPPY_VILLAGER, this.getX(), this.getY() + 1.0D, this.getZ(),
                    8, 0.8D, 0.4D, 0.8D, 0.02D);
        }
        this.level().playSound(null, this.blockPosition(), SoundEvents.EXPERIENCE_ORB_PICKUP,
                SoundSource.HOSTILE, 1.0F, 0.7F);
    }

    private void playImpactEffects(Vec3 center) {
        if (this.level() instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.EXPLOSION, center.x, center.y - 0.6D, center.z, 5, 1.4D, 0.2D, 1.4D, 0.0D);
            server.sendParticles(ParticleTypes.CLOUD, center.x, center.y - 0.5D, center.z, 30, 2.2D, 0.3D, 2.2D, 0.08D);
            server.sendParticles(ParticleTypes.CRIT, center.x, center.y, center.z, 16, 1.8D, 0.6D, 1.8D, 0.4D);
        }
        this.level().playSound(null, this.blockPosition(), SoundEvents.ANVIL_LAND, SoundSource.HOSTILE, 2.0F, 0.55F);
        this.level().playSound(null, this.blockPosition(), SoundEvents.STONE_BREAK, SoundSource.HOSTILE, 1.6F, 0.5F);
    }

    private void fireBlockProjectile() {
        LivingEntity target = this.getTarget();
        if (target == null) return;

        HypostasisProjectile projectile = new HypostasisProjectile(PMEntityTypes.HYPOSTASIS_PROJECTILE.get(), this.level());
        Vec3 eye = new Vec3(this.getX(), this.getY() + 1.1D, this.getZ());
        projectile.moveTo(eye.x, eye.y, eye.z, this.getYRot(), this.getXRot());
        projectile.setBlockIndex(this.random.nextInt(HypostasisProjectile.SHELL_BLOCKS.length));
        projectile.setOwner(this);
        projectile.setDamage(getPhase() >= 2 ? 10.0F : 8.0F);
        projectile.setHomingTarget(target);

        Vec3 aimPos = target.getEyePosition().add(target.getDeltaMovement().scale(10.0D));
        Vec3 direction = aimPos.subtract(eye).normalize();
        projectile.shoot(direction.x, direction.y, direction.z, 1.35F, 3.0F);
        this.level().addFreshEntity(projectile);
        this.level().playSound(null, this.blockPosition(), SoundEvents.SNOWBALL_THROW, SoundSource.HOSTILE, 1.2F, 0.6F);
    }

    private void performThunderRings(float damage) {
        double centerX = this.getX(), centerZ = this.getZ();
        Set<LivingEntity> victimsToStrike = new HashSet();
        for (int ringIdx = 0; ringIdx < THUNDER_RING_RADII.length; ringIdx++) {
            double radius = THUNDER_RING_RADII[ringIdx];
            int points = THUNDER_RING_POINTS[ringIdx];
            double angleOffset = ringIdx * (Math.PI / points);
            for (int i = 0; i < points; i++) {
                double angle = (Math.PI * 2.0D * i) / points + angleOffset;
                double x = centerX + Math.cos(angle) * radius;
                double z = centerZ + Math.sin(angle) * radius;
                int groundY = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor(x), Mth.floor(z));
                double strikeY = groundY + 0.1D;

                LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(this.level());
                if (bolt != null) { bolt.moveTo(x, strikeY, z); bolt.setVisualOnly(true); this.level().addFreshEntity(bolt); }

                if (this.level() instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, strikeY + 0.2D, z, 6, 0.3D, 0.8D, 0.3D, 0.08D);
                }

                List<LivingEntity> victims = this.level().getEntitiesOfClass(LivingEntity.class,
                        new AABB(x - 2.0D, strikeY - 8.0D, z - 2.0D, x + 2.0D, strikeY + 10.0D, z + 2.0D),
                        t -> t != this && t.isAlive() && !(t instanceof HypostasisEntity));
                for (LivingEntity victim : victims) {
                    if (victim instanceof Player p && (p.isCreative() || p.isSpectator())) continue;
                    if (victimsToStrike.add(victim)) {
                        victim.hurt(this.damageSources().mobAttack(this), damage);
                        victim.setSecondsOnFire(2);
                    }
                }
            }
        }
        this.level().playSound(null, this.blockPosition(), SoundEvents.LIGHTNING_BOLT_THUNDER, SoundSource.HOSTILE, 2.2F, 1.0F);
    }

    // ============================= 移动与兜底 =============================
    public void hoverTowards(Vec3 desired, double speed) {
        // 封锁期间目标点也夹在边界内，避免 BOSS 贴着边界往外顶
        Vec3 clamped = new Vec3(clampBarrierX(desired.x), desired.y, clampBarrierZ(desired.z));
        Vec3 delta = clamped.subtract(this.position());
        if (delta.lengthSqr() > 1.0E-4D) {
            double scale = Math.min(speed, delta.length());
            this.setDeltaMovement(delta.normalize().scale(scale));
        } else {
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
        }
    }

    @Override
    public void travel(Vec3 travelVector) {
        this.move(MoverType.SELF, this.getDeltaMovement());
        int minY = this.level().getMinBuildHeight() + 5;
        if (this.getY() < minY) {
            this.setPos(this.getX(), minY, this.getZ());
            this.setDeltaMovement(this.getDeltaMovement().x, Math.max(0.1D, this.getDeltaMovement().y), this.getDeltaMovement().z);
        }
        clampInsideBarrier();
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
                this.getZ() + (this.random.nextDouble() - 0.5D) * spread, 0.0D, 0.01D, 0.0D);
    }

    private void ensureAboveGround() {
        if (this.level().isClientSide) return;
        double cx = this.getX(), cz = this.getZ();
        BlockPos feetPos = BlockPos.containing(cx, this.getY() + 0.1D, cz);
        BlockPos bodyPos = BlockPos.containing(cx, this.getY() + this.getBbHeight() * 0.5D, cz);
        if (!hasBlockCollision(feetPos) && !hasBlockCollision(bodyPos)) return;
        int surfaceY = this.level().getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mth.floor(cx), Mth.floor(cz));
        double safeY = Math.max(surfaceY + 0.05D, this.level().getMinBuildHeight() + 3.0D);
        if (this.getY() < safeY) {
            this.setPos(cx, safeY, cz);
            this.setDeltaMovement(this.getDeltaMovement().x, 0.0D, this.getDeltaMovement().z);
        }
    }

    private boolean hasBlockCollision(BlockPos pos) {
        BlockState state = this.level().getBlockState(pos);
        return !state.isAir() && !state.getCollisionShape(this.level(), pos).isEmpty();
    }

    private void clearMovementPath() {
        if (this.level().isClientSide) return;
        AABB box = this.getBoundingBox().inflate(0.05D);
        int minX = Mth.floor(box.minX), maxX = Mth.floor(box.maxX);
        int minY = Mth.floor(box.minY), maxY = Mth.floor(box.maxY);
        int minZ = Mth.floor(box.minZ), maxZ = Mth.floor(box.maxZ);
        int broken = 0;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
        for (int x = minX; x <= maxX; x++) for (int y = minY; y <= maxY; y++) for (int z = minZ; z <= maxZ; z++) {
            cursor.set(x, y, z);
            BlockState state = this.level().getBlockState(cursor);
            if (state.isAir() || !state.getFluidState().isEmpty()) continue;
            float hardness = state.getDestroySpeed(this.level(), cursor);
            if (hardness < 0.0F || hardness >= PATH_CLEAR_MAX_HARDNESS) continue;
            if (this.level().getBlockEntity(cursor) != null) continue;
            this.level().destroyBlock(cursor, false, this);
            if (++broken >= PATH_CLEAR_LIMIT) return;
        }
    }

    // ============================= 杂项 =============================
    public boolean isCoreExposed() {
        AttackState state = getAttackState();
        if (state == AttackState.PHASE_TRANSITION) return false;
        if (state == AttackState.MELEE_LINE || state == AttackState.MELEE_CROSS || state == AttackState.MELEE_X) {
            int tick = getAnimTick();
            return tick >= MELEE_IMPACT_TICK - 10 && tick <= MELEE_IMPACT_TICK + 8;
        }
        return false;
    }

    public boolean isInvulnerable() { return getAttackState() == AttackState.PHASE_TRANSITION; }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (isInvulnerable()) {
            this.level().playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1.2F, 1.6F);
            if (this.level() instanceof ServerLevel server) {
                server.sendParticles(ParticleTypes.ENCHANTED_HIT, this.getX(), this.getY() + 1.0D, this.getZ(), 10, 1.0D, 1.0D, 1.0D, 0.1D);
            }
            return false;
        }

        boolean isRanged = source.is(DamageTypes.ARROW) || source.is(DamageTypes.THROWN) || source.is(DamageTypes.MOB_PROJECTILE)
                || source.getDirectEntity() instanceof AbstractArrow || source.getDirectEntity() instanceof Projectile;

        if (isRanged) {
            if (!isCoreExposed()) {
                amount *= 0.1F;
                this.level().playSound(null, this.blockPosition(), SoundEvents.SHIELD_BLOCK, SoundSource.HOSTILE, 1.2F, 0.6F);
                if (this.level() instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.ENCHANTED_HIT, this.getX(), this.getY() + 1.0D, this.getZ(), 10, 1.0D, 1.0D, 1.0D, 0.1D);
                }
            } else {
                this.level().playSound(null, this.blockPosition(), SoundEvents.ANVIL_PLACE, SoundSource.HOSTILE, 1.0F, 1.2F);
                if (this.level() instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.CRIT, this.getX(), this.getY() + 1.0D, this.getZ(), 15, 1.0D, 1.0D, 1.0D, 0.2F);
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
    public boolean isInvulnerableTo(DamageSource source) {
        if (isInvulnerable()) return true;
        return super.isInvulnerableTo(source);
    }

    /** 🔴 强制移除实体时（/kill、维度切换等），也确保世界边界被恢复。 */
    @Override
    public void remove(RemovalReason reason) {
        if (!this.level().isClientSide) {
            restorePhaseBorder();
        }
        super.remove(reason);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) { super.startSeenByPlayer(player); this.bossEvent.addPlayer(player); }
    @Override
    public void stopSeenByPlayer(ServerPlayer player) { super.stopSeenByPlayer(player); this.bossEvent.removePlayer(player); }

    @Override
    protected void dropCustomDeathLoot(DamageSource source, int loot, boolean isPlayer) {
        super.dropCustomDeathLoot(source, loot, isPlayer);
        ItemStack godEye = getRandomGodEye();
        if (!godEye.isEmpty()) this.spawnAtLocation(godEye);
    }

    /** 掉落池：7 种元素，不含 {@link GodEyeType#EMPTY}。 */
    private static final GodEyeType[] GOD_EYE_DROPS = {
            GodEyeType.WIND, GodEyeType.STONE, GodEyeType.THUNDER,
            GodEyeType.GRASS, GodEyeType.WATER, GodEyeType.FIRE, GodEyeType.ICE
    };

    private ItemStack getRandomGodEye() {
        GodEyeType type = GOD_EYE_DROPS[random.nextInt(GOD_EYE_DROPS.length)];
        Item item = switch (type) {
            case WIND -> PMItems.godEyeWind.get();
            case STONE -> PMItems.godEyeStone.get();
            case THUNDER -> PMItems.godEyeThunder.get();
            case GRASS -> PMItems.godEyeGrass.get();
            case WATER -> PMItems.godEyeWater.get();
            case FIRE -> PMItems.godEyeFire.get();
            case ICE -> PMItems.godEyeIce.get();
            default -> Items.AIR;
        };
        if (item == Items.AIR) return ItemStack.EMPTY;

        ItemStack godEye = new ItemStack(item);
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(RelicsHelper.NBT_STAR, 5); // 神之眼固定五星
        nbt.putInt(RelicsHelper.NBT_TYPE, RelicsType.EYE.getId());
        godEye.getOrCreateTag().put(RelicsHelper.NBT_RELICS, nbt);
        return godEye;
    }

    @Override protected ResourceLocation getDefaultLootTable() { return LOOT_TABLE; }
    @Override protected SoundEvent getHurtSound(DamageSource s) { return SoundEvents.IRON_GOLEM_HURT; }
    @Override protected SoundEvent getDeathSound() { return SoundEvents.IRON_GOLEM_DEATH; }
    @Override protected SoundEvent getAmbientSound() { return SoundEvents.STONE_BREAK; }
    @Override public int getAmbientSoundInterval() { return 160; }
    @Override protected float getSoundVolume() { return 1.4F; }
    @Override public boolean causeFallDamage(float d, float m, DamageSource s) { return false; }
    @Override protected void checkFallDamage(double y, boolean onGround, BlockState s, BlockPos p) { }
    @Override public boolean isPushable() { return false; }
    @Override protected void doPush(Entity e) { }
    @Override public boolean removeWhenFarAway(double d) { return false; }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putInt("Phase", getPhase());
        tag.putString("AttackState", getAttackState().name());
        // 封锁期间把原边界写进存档：即使服务器直接关闭/崩溃，读档后也能把世界边界改回来
        if (this.borderSaved) {
            tag.putBoolean("BorderSaved", true);
            tag.putDouble("BorderSize", this.originalBorderSize);
            tag.putDouble("BorderCenterX", this.originalBorderCenterX);
            tag.putDouble("BorderCenterZ", this.originalBorderCenterZ);
            tag.putDouble("BorderDamage", this.originalBorderDamagePerBlock);
            tag.putDouble("BorderSafeZone", this.originalBorderDamageSafeZone);
            tag.putInt("BorderWarnBlocks", this.originalBorderWarningBlocks);
            tag.putInt("BorderWarnTime", this.originalBorderWarningTime);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if (tag.contains("Phase")) setPhase(Math.max(1, tag.getInt("Phase")));
        if (tag.getBoolean("BorderSaved")) {
            this.originalBorderSize = tag.getDouble("BorderSize");
            this.originalBorderCenterX = tag.getDouble("BorderCenterX");
            this.originalBorderCenterZ = tag.getDouble("BorderCenterZ");
            this.originalBorderDamagePerBlock = tag.getDouble("BorderDamage");
            this.originalBorderDamageSafeZone = tag.getDouble("BorderSafeZone");
            this.originalBorderWarningBlocks = tag.getInt("BorderWarnBlocks");
            this.originalBorderWarningTime = tag.getInt("BorderWarnTime");
            this.borderRestorePending = true;
        }
    }
}