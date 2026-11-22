package com.yuo.paimeng.entity;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * 无相之岩抛出的“外壳方块”投射物。
 * <ul>
 *   <li>命中生物造成伤害并击退</li>
 *   <li>击中方块产生小范围爆炸</li>
 *   <li>飞行中轻微追踪目标（每 tick 偏转 6%）</li>
 * </ul>
 */
public class HypostasisProjectile extends ThrowableProjectile {

    public static final Block[] SHELL_BLOCKS = {
            Blocks.IRON_BLOCK, Blocks.GOLD_BLOCK, Blocks.DIAMOND_BLOCK, Blocks.EMERALD_BLOCK,
            Blocks.LAPIS_BLOCK, Blocks.REDSTONE_BLOCK, Blocks.NETHERITE_BLOCK, Blocks.COPPER_BLOCK
    };

    private static final EntityDataAccessor<Integer> DATA_BLOCK = SynchedEntityData.defineId(HypostasisProjectile.class, EntityDataSerializers.INT);

    /** 每 tick 向目标方向偏转的比例。0.06 约等于“轻微追踪”。 */
    private static final double HOMING_STRENGTH = 0.06D;

    private float damage = 8.0F;

    @Nullable
    private LivingEntity homingTarget;

    public HypostasisProjectile(EntityType<? extends HypostasisProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_BLOCK, 0);
    }

    public void setBlockIndex(int index) { this.entityData.set(DATA_BLOCK, Math.abs(index) % SHELL_BLOCKS.length); }
    public BlockState getBlockState() { return SHELL_BLOCKS[this.entityData.get(DATA_BLOCK)].defaultBlockState(); }
    public void setDamage(float damage) { this.damage = damage; }

    /** 🔴 设置追踪目标。目标死亡 / 切换维度 / 消失后自动失效。 */
    public void setHomingTarget(@Nullable LivingEntity target) { this.homingTarget = target; }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide) return;

        if (this.tickCount > 160) {
            this.discard();
            return;
        }

        // 🔴 轻微追踪：每 tick 让当前速度方向向目标中心轻微偏转，保持速度大小不变
        if (this.homingTarget != null) {
            if (!this.homingTarget.isAlive() || this.homingTarget.level() != this.level()) {
                this.homingTarget = null;
                return;
            }
            Vec3 currentVel = this.getDeltaMovement();
            double speed = currentVel.length();
            if (speed > 0.05D) {
                Vec3 targetCenter = new Vec3(
                        this.homingTarget.getX(),
                        this.homingTarget.getY() + this.homingTarget.getBbHeight() * 0.5D,
                        this.homingTarget.getZ());
                Vec3 toTarget = targetCenter.subtract(this.position()).normalize();
                Vec3 newDir = currentVel.normalize().scale(1.0D - HOMING_STRENGTH)
                        .add(toTarget.scale(HOMING_STRENGTH))
                        .normalize();
                this.setDeltaMovement(newDir.scale(speed));
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (this.level().isClientSide) return;
        Entity hit = result.getEntity();
        if (hit instanceof LivingEntity living && !(living instanceof HypostasisEntity)) {
            living.hurt(this.damageSources().thrown(this, this.getOwner()), this.damage);
            living.knockback(0.7D, this.getX() - living.getX(), this.getZ() - living.getZ());
        }
        this.discard();
    }

    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.2F, Level.ExplosionInteraction.BLOCK);
            this.discard();
        }
    }

    @Override
    protected float getGravity() { return 0.015F; }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() { return new ClientboundAddEntityPacket(this); }
}