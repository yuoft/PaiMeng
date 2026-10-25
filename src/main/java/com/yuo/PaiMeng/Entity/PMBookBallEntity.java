package com.yuo.PaiMeng.Entity;

import com.yuo.PaiMeng.Items.PMItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class PMBookBallEntity extends ThrowableItemProjectile {
    private float damage;
    public PMBookBallEntity(EntityType<? extends ThrowableItemProjectile> type, Level world) {
        super(type, world);
    }

    public PMBookBallEntity(EntityType<? extends ThrowableItemProjectile> type, LivingEntity shooter, Level worldIn, float damageIn) {
        super(type, shooter, worldIn);
        this.damage = damageIn;
    }

    public PMBookBallEntity(EntityType<? extends ThrowableItemProjectile> type, double x, double y, double z, Level worldIn, float damageIn) {
        super(type, x, y, z, worldIn);
        this.damage = damageIn;
    }

    public void setDamage(float damage){
        this.damage = damage;
    }

    public float getBallDamage(){
        return this.damage;
    }

    private ParticleOptions makeParticle() {
        ItemStack itemstack = this.getItem();
        return itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleOption(ParticleTypes.ITEM, itemstack);
    }

    //客户端粒子（原 handleStatusUpdate）
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particle = this.makeParticle();
            for (int i = 0; i < 8; ++i) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0D, 0.0D, 0.0D);
            }
        }
    }

    //命中实体
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        if (!this.level().isClientSide) {
            entity.hurt(this.damageSources().thrown(this, this.getOwner()), this.damage);
            this.discard();
        }
    }

    //命中方块
    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide) {
            this.discard();
        }
    }

    //重力
    @Override
    protected float getGravity() {
        return 0.001f;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("Damage", this.damage);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setDamage(compound.getFloat("Damage"));
    }

    @Override
    protected Item getDefaultItem() {
        return PMItems.qiuqiu_baoyu.get().asItem();
    }
}
