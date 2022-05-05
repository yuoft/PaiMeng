package com.yuo.PaiMeng.Entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.ParrotEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.UUID;

public class CraneEntity extends ParrotEntity implements IAngerable {
    private static final RangedInteger angerRange = TickRangeConverter.convertRange(20, 39);
    private int angerTime;
    private UUID angerTarget;

    protected CraneEntity(EntityType<? extends ParrotEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    protected void registerGoals() {
//        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(1, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(2, new SitGoal(this));
        this.goalSelector.addGoal(2, new FollowOwnerGoal(this, 1.0D, 5.0F, 1.0F, true));
        this.goalSelector.addGoal(2, new WaterAvoidingRandomFlyingGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new LandOnOwnersShoulderGoal(this));
        this.goalSelector.addGoal(3, new FollowMobGoal(this, 1.0D, 3.0F, 7.0F));

        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.0D, 32.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::func_233680_b_));
        this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));
    }


    @Nullable
    @Override
    public AgeableEntity createChild(ServerWorld serverWorld, AgeableEntity entity) {
        return EntityTypeRegister.CRANE.get().create(world);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 20.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.25D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 3.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.05D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 20.0D)
                .createMutableAttribute(Attributes.ARMOR, 3.0d)
                .createMutableAttribute(Attributes.FLYING_SPEED, 0.45D);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (entityIn instanceof LivingEntity){
            entityIn.attackEntityFrom(DamageSource.GENERIC, (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
            //击飞
//            Vector3d lookVec = entityIn.getLookVec();
//            entityIn.setMotion(entityIn.getMotion().add(0, 0.4F, 0));
//            entityIn.addVelocity(0,0.4f,0);
//            entityIn.addVelocity(-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * 10 * 0.5F, 0.4D, MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * 10 * 0.5F);

        }
        return true;
    }
    //
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount) {
        if (source.getTrueSource() instanceof LivingEntity){
            this.setAngerTime(20);
            this.setAngerTarget(source.getTrueSource().getUniqueID());
            this.setAttackTarget((LivingEntity) source.getTrueSource());
            return super.attackEntityFrom(source, amount);
        }
        return false;
    }

    //与实体碰撞
    protected void collideWithEntity(Entity entityIn) {
        if (entityIn instanceof IMob && !(entityIn instanceof CreeperEntity) && this.getRNG().nextInt(20) == 0) {
            this.setAttackTarget((LivingEntity)entityIn);
        }

        super.collideWithEntity(entityIn);
    }

    @Override
    public int getAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setAngerTime(int time) {
        this.angerTime = time;
    }

    @Nullable
    @Override
    public UUID getAngerTarget() {
        return this.angerTarget;
    }

    @Override
    public void setAngerTarget(@Nullable UUID target) {
        this.angerTarget = target;
    }
    @Override
    public void func_230258_H__() {
        this.setAngerTime(angerRange.getRandomWithinRange(this.rand));
    }
}
