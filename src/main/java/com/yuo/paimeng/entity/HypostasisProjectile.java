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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

/**
 * 无相之岩抛出的“外壳方块”投射物：命中生物造成伤害并击退，命中方块破碎消失。
 */
public class HypostasisProjectile extends ThrowableProjectile {

    /** 与外壳一致的 8 种矿物方块。 */
    public static final Block[] SHELL_BLOCKS = {
            Blocks.IRON_BLOCK,
            Blocks.GOLD_BLOCK,
            Blocks.DIAMOND_BLOCK,
            Blocks.EMERALD_BLOCK,
            Blocks.LAPIS_BLOCK,
            Blocks.REDSTONE_BLOCK,
            Blocks.NETHERITE_BLOCK,
            Blocks.COPPER_BLOCK
    };

    private static final EntityDataAccessor<Integer> DATA_BLOCK = SynchedEntityData.defineId(HypostasisProjectile.class, EntityDataSerializers.INT);

    private float damage = 8.0F;

    public HypostasisProjectile(EntityType<? extends HypostasisProjectile> type, Level level) {
        super(type, level);
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_BLOCK, 0);
    }

    public void setBlockIndex(int index) {
        this.entityData.set(DATA_BLOCK, Math.abs(index) % SHELL_BLOCKS.length);
    }

    public BlockState getBlockState() {
        return SHELL_BLOCKS[this.entityData.get(DATA_BLOCK)].defaultBlockState();
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide && this.tickCount > 160) {
            this.discard();
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
            // 🔴 击中方块时产生小范围爆炸
            this.level().explode(this, this.getX(), this.getY(), this.getZ(), 1.2F, Level.ExplosionInteraction.BLOCK);
            this.discard();
        }
    }

    @Override
    protected float getGravity() {
        return 0.015F;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }
}
