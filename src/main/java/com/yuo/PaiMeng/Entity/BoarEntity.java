package com.yuo.PaiMeng.Entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.IPacket;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.RangedInteger;
import net.minecraft.util.TickRangeConverter;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.UUID;

public class BoarEntity extends PigEntity implements IAngerable {
    private static final Ingredient TEMPTATION_ITEMS = Ingredient.fromItems(Items.CARROT, Items.POTATO, Items.BEETROOT);
    protected static final DataParameter<Boolean> TARGET = EntityDataManager.createKey(BoarEntity.class, DataSerializers.BOOLEAN);
    private static final RangedInteger angerRange = TickRangeConverter.convertRange(20, 39);
    private int angerTime;
    private UUID angerTarget;

    public BoarEntity(EntityType<? extends PigEntity> p_i50250_1_, World world) {
        super(p_i50250_1_, world);
    }

    //后代
    public PigEntity func_241840_a(ServerWorld serverWorld, AgeableEntity entity) {
        return EntityTypeRegister.BOAR.get().create(serverWorld);
    }

    @Override
    protected void registerData() {
        super.registerData();
        this.dataManager.register(TARGET, false);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    //AI
    @Override
    protected void registerGoals() {
//        super.registerGoals();
        this.goalSelector.addGoal(0, new SwimGoal(this));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.fromItems(Items.CARROT_ON_A_STICK), false));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, false, TEMPTATION_ITEMS));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomWalkingGoal(this, 1.0D));

        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.0D, 32.0F));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(7, new LookAtGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::func_233680_b_));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, MobEntity.class, 5, false, false, (p_234199_0_) -> p_234199_0_ instanceof IMob && !(p_234199_0_ instanceof CreeperEntity)));
        this.targetSelector.addGoal(4, new ResetAngerGoal<>(this, false));
    }

    //属性
    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.func_233666_p_()
                .createMutableAttribute(Attributes.MAX_HEALTH, 16.0D)
                .createMutableAttribute(Attributes.MOVEMENT_SPEED, 0.26D)
                .createMutableAttribute(Attributes.ATTACK_DAMAGE, 2.0D)
                .createMutableAttribute(Attributes.KNOCKBACK_RESISTANCE, 0.05D)
                .createMutableAttribute(Attributes.FOLLOW_RANGE, 16.0D)
                .createMutableAttribute(Attributes.ARMOR, 2.0d);
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn) {
        if (entityIn instanceof LivingEntity){
            entityIn.attackEntityFrom(DamageSource.GENERIC, (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
            //击飞
            entityIn.addVelocity(-MathHelper.sin(this.rotationYaw * (float) Math.PI / 180.0F) * 10 * 0.5F, 0.4D, MathHelper.cos(this.rotationYaw * (float) Math.PI / 180.0F) * 10 * 0.5F);

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
