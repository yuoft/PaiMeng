package com.yuo.PaiMeng.Entity;

import com.yuo.PaiMeng.Items.PMItems;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.entity.projectile.SnowballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ItemParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.EntityDamageSource;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkHooks;

public class PMBookBallEntity extends ProjectileItemEntity {
    private float damage;
    public PMBookBallEntity(EntityType<? extends ProjectileItemEntity> type, World world) {
        super(type, world);
    }

    public PMBookBallEntity(EntityType<? extends ProjectileItemEntity> type, LivingEntity shooter, World worldIn, float damageIn) {
        super(type, shooter, worldIn);
        this.damage = damageIn;
    }

    public PMBookBallEntity(EntityType<? extends ProjectileItemEntity> type, double x, double y, double z, World worldIn, float damageIn) {
        super(type, x, y, z, worldIn);
        this.damage = damageIn;
    }

    public void setDamage(float damage){
        this.damage = damage;
    }

    @OnlyIn(Dist.CLIENT)
    private IParticleData makeParticle() {
        ItemStack itemstack = this.func_213882_k();
        return itemstack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemParticleData(ParticleTypes.ITEM, itemstack);
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void handleStatusUpdate(byte id) {
        if (id == 3) {
            IParticleData iparticledata = this.makeParticle();

            for(int i = 0; i < 8; ++i) {
                this.world.addParticle(iparticledata, this.getPosX(), this.getPosY(), this.getPosZ(), 0.0D, 0.0D, 0.0D);
            }
        }

    }

    //落地或碰撞后触发事件
    @Override
    protected void onImpact(RayTraceResult result) {
        RayTraceResult.Type raytraceresult$type = result.getType();
        if (raytraceresult$type == RayTraceResult.Type.ENTITY) {
            this.onEntityHit((EntityRayTraceResult)result);
        } else if (raytraceresult$type == RayTraceResult.Type.BLOCK) {
            this.func_230299_a_((BlockRayTraceResult)result);
        }

    }

    @Override
    protected void onEntityHit(EntityRayTraceResult result) {
        super.onEntityHit(result);
        Entity entity = result.getEntity();
        hitByEntity(entity);
    }

    @Override
    public boolean hitByEntity(Entity entityIn) {
        World world = entityIn.world;
        if (!world.isRemote){
            entityIn.attackEntityFrom(new EntityDamageSource("PM Ball", getShooter()), damage);
            this.remove();
        }
        return super.hitByEntity(entityIn);
    }

    //重力
    @Override
    protected float getGravityVelocity() {
        return 0.001f;
    }

    public void writeAdditional(CompoundNBT compound) {
        if (!func_213882_k().isEmpty()) {
            compound.putFloat("Damage", this.damage);
        }

    }

    public void readAdditional(CompoundNBT compound) {
        float damage = compound.getFloat("Damage");
        this.setDamage(damage);
    }

    @Override
    protected Item getDefaultItem() {
        return PMItems.qiuqiu_baoyu.get().asItem();
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}
