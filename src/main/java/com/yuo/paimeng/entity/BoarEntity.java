package com.yuo.paimeng.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.FollowParentGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsTargetGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.UUID;

public class BoarEntity extends Animal implements NeutralMob {
    private static final Ingredient TEMPTATION_ITEMS = Ingredient.of(Items.CARROT, Items.POTATO, Items.BEETROOT);
    protected static final EntityDataAccessor<Boolean> TARGET = SynchedEntityData.defineId(BoarEntity.class, EntityDataSerializers.BOOLEAN);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int angerTime;
    @Nullable
    private UUID angerTarget;

    public BoarEntity(EntityType<? extends Animal> type, Level world) {
        super(type, world);
    }

    //后代
    @Nullable
    @Override
    public BoarEntity getBreedOffspring(ServerLevel serverWorld, AgeableMob entity) {
        return EntityTypeRegister.BOAR.get().create(serverWorld);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TARGET, false);
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.is(Items.CARROT) || stack.is(Items.POTATO) || stack.is(Items.BEETROOT);
    }

    //AI
    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(2, new MoveTowardsTargetGoal(this, 1.0D, 32.0F));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(Items.CARROT_ON_A_STICK), false));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, TEMPTATION_ITEMS, false));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAtPlayer));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false,
                target -> target instanceof Enemy && !(target instanceof Creeper)));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    //愤怒判定（原 1.16 的 this::func_233680_b_）
    private boolean isAngryAtPlayer(LivingEntity target) {
        UUID uuid = this.getPersistentAngerTarget();
        return uuid != null && uuid.equals(target.getUUID());
    }

    //属性
    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 16.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.26D)
                .add(Attributes.ATTACK_DAMAGE, 2.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.05D)
                .add(Attributes.FOLLOW_RANGE, 16.0D)
                .add(Attributes.ARMOR, 2.0d);
    }

    //旧名保留（1.16.5 的 setCustomAttributes）
    public static AttributeSupplier.Builder setCustomAttributes() {
        return createAttributes();
    }

    //刷怪位置规则（EntityGen 用）
    public static boolean checkBoarSpawnRules(EntityType<BoarEntity> type, net.minecraft.world.level.ServerLevelAccessor level,
                                              net.minecraft.world.entity.MobSpawnType spawnType, net.minecraft.core.BlockPos pos,
                                              net.minecraft.util.RandomSource random) {
        return Animal.checkAnimalSpawnRules(type, level, spawnType, pos, random)
                && level.getBlockState(pos.below()).is(net.minecraft.tags.BlockTags.DIRT);
    }

    @Override
    public boolean doHurtTarget(Entity entityIn) {
        if (entityIn instanceof LivingEntity) {
            entityIn.hurt(damageSources().generic(), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
            //击飞
            float yaw = this.getYRot() * (float) Math.PI / 180.0F;
            entityIn.push(-Mth.sin(yaw) * 5.0F, 0.4D, Mth.cos(yaw) * 5.0F);
        }
        return true;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        if (source.getEntity() instanceof LivingEntity living) {
            this.startPersistentAngerTimer();
            this.setPersistentAngerTarget(living.getUUID());
            this.setTarget(living);
            return super.hurt(source, amount);
        }
        return false;
    }

    //与实体碰撞
    @Override
    public void push(Entity entityIn) {
        if (entityIn instanceof Enemy && !(entityIn instanceof Creeper) && this.getRandom().nextInt(20) == 0) {
            this.setTarget((LivingEntity) entityIn);
        }
        super.push(entityIn);
    }

    //吃草奖励（BoarEatGoal 调用）
    public void eatGrassBonus() {
        this.heal(4.0F);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.addPersistentAngerSaveData(compound);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.readPersistentAngerSaveData(this.level(), compound);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.angerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int time) {
        this.angerTime = time;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.angerTarget;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID target) {
        this.angerTarget = target;
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.getRandom()));
    }

    //旧名兼容（1.16.5 的 getAngerTime / setAngerTime / getAngerTarget / setAngerTarget / func_230258_H__）
    public int getAngerTime() {
        return getRemainingPersistentAngerTime();
    }

    public void setAngerTime(int time) {
        setRemainingPersistentAngerTime(time);
    }

    @Nullable
    public UUID getAngerTarget() {
        return getPersistentAngerTarget();
    }

    public void setAngerTarget(@Nullable UUID target) {
        setPersistentAngerTarget(target);
    }
}
