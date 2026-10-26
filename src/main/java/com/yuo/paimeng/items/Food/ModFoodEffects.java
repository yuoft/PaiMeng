package com.yuo.paimeng.items.Food;

import com.yuo.paimeng.effects.EffectRegistry;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

public class ModFoodEffects {
    //食物属性构建  补充饥饿值，饱腹度  药水效果 获取药水效果概率（1=100%） 总是可以食用 肉
    public static final FoodProperties TIANTIANHUA_NIANGJI = (new FoodProperties.Builder()).nutrition(2).saturationMod(1).effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION,  30 * 20, 0), 1).effect(
            () -> new MobEffectInstance(MobEffects.SLOW_FALLING, 20 * 20, 1), 1).alwaysEat().build();

    //回复类 瞬间恢复多少血量 + 持续恢复效果
    public static final FoodProperties RECOVER_ONE = (new FoodProperties.Builder()).nutrition(2).saturationMod(1).effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION,  30 * 20, 0), 1).alwaysEat().build();
    public static final FoodProperties RECOVER_TWO = (new FoodProperties.Builder()).nutrition(4).saturationMod(2).effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION,  50 * 20, 1), 1).alwaysEat().build();
    public static final FoodProperties RECOVER_THREE = (new FoodProperties.Builder()).nutrition(6).saturationMod(3).effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION,  70 * 20, 2), 1).alwaysEat().build();
    public static final FoodProperties RECOVER_FOUR = (new FoodProperties.Builder()).nutrition(8).saturationMod(4).effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION,  90 * 20, 3), 1).alwaysEat().build();
    public static final FoodProperties RECOVER_FIVE = (new FoodProperties.Builder()).nutrition(10).saturationMod(5).effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION,  120 * 20, 4), 1).alwaysEat().build();

    //复活类 不死图腾效果 + 恢复多少血量
    /**
     * 复活
     */
    public static final FoodProperties REVIVE_ONE = (new FoodProperties.Builder()).nutrition(2).saturationMod(1).effect(
            () -> new MobEffectInstance(EffectRegistry.REVIVE.get(),  60 * 20, 0), 1).alwaysEat().build();
    public static final FoodProperties REVIVE_TWO = (new FoodProperties.Builder()).nutrition(4).saturationMod(2).effect(
            () -> new MobEffectInstance(EffectRegistry.REVIVE.get(),  120 * 20, 1), 1).alwaysEat().build();
    public static final FoodProperties REVIVE_THREE = (new FoodProperties.Builder()).nutrition(6).saturationMod(3).effect(
            () -> new MobEffectInstance(EffectRegistry.REVIVE.get(),  180 * 20, 2), 1).alwaysEat().build();
    public static final FoodProperties REVIVE_FOUR = (new FoodProperties.Builder()).nutrition(8).saturationMod(4).effect(
            () -> new MobEffectInstance(EffectRegistry.REVIVE.get(),  240 * 20, 3), 1).alwaysEat().build();
    public static final FoodProperties REVIVE_FIVE = (new FoodProperties.Builder()).nutrition(10).saturationMod(5).effect(
            () -> new MobEffectInstance(EffectRegistry.REVIVE.get(),  300 * 20, 4), 1).alwaysEat().build();

    //攻击类
    // 增加攻击力
    public static final FoodProperties ATTACK_TWO = (new FoodProperties.Builder()).nutrition(3).saturationMod(2).effect(
            () -> new MobEffectInstance(EffectRegistry.ATTACK.get(),  60 * 20, 0), 1).alwaysEat().build();
    public static final FoodProperties ATTACK_TWO_RATE = (new FoodProperties.Builder()).nutrition(3).saturationMod(2).effect(
            () -> new MobEffectInstance(EffectRegistry.ATTACK.get(),  60 * 20, 0), 1).effect(
            () -> new MobEffectInstance(EffectRegistry.CRITICAL_RATE.get(), 60 * 20, 0),1).alwaysEat().build();

    public static final FoodProperties ATTACK_THREE = (new FoodProperties.Builder()).nutrition(5).saturationMod(3).effect(
            () -> new MobEffectInstance(EffectRegistry.ATTACK.get(),  90 * 20, 1), 1).alwaysEat().build();
    public static final FoodProperties ATTACK_THREE_RATE = (new FoodProperties.Builder()).nutrition(5).saturationMod(3).effect(
            () -> new MobEffectInstance(EffectRegistry.ATTACK.get(),  90 * 20, 1), 1).effect(
            () -> new MobEffectInstance(EffectRegistry.CRITICAL_RATE.get(), 90 * 20, 2),1).alwaysEat().build();
    public static final FoodProperties ATTACK_PHYSICS = (new FoodProperties.Builder()).nutrition(5).saturationMod(3).effect(
            () -> new MobEffectInstance(EffectRegistry.ATTACK_PHYSICS.get(),  90 * 20, 0), 1).alwaysEat().build();

    public static final FoodProperties ATTACK_FOUR = (new FoodProperties.Builder()).nutrition(7).saturationMod(4).effect(
            () -> new MobEffectInstance(EffectRegistry.ATTACK.get(),  120 * 20, 2), 1).alwaysEat().build();
    public static final FoodProperties ATTACK_FOUR_RATE = (new FoodProperties.Builder()).nutrition(7).saturationMod(4).effect( //加暴击
            () -> new MobEffectInstance(EffectRegistry.ATTACK.get(),  120 * 20, 2), 1).effect(
            () -> new MobEffectInstance(EffectRegistry.CRITICAL_RATE.get(), 120 * 20, 0),1).alwaysEat().build();
    public static final FoodProperties ATTACK_PHYSICS_RATE = (new FoodProperties.Builder()).nutrition(7).saturationMod(4).effect( //加暴击 物理
            () -> new MobEffectInstance(EffectRegistry.ATTACK_PHYSICS.get(),  120 * 20, 1), 1).effect(
            () -> new MobEffectInstance(EffectRegistry.CRITICAL_RATE.get(), 120 * 20, 0),1).alwaysEat().build();

    public static final FoodProperties ATTACK_FIVE = (new FoodProperties.Builder()).nutrition(9).saturationMod(5).effect(
            () -> new MobEffectInstance(EffectRegistry.ATTACK.get(),  180 * 20, 3), 1).alwaysEat().build();
    public static final FoodProperties ATTACK_FIVE_RATE_DAMAGE = (new FoodProperties.Builder()).nutrition(9).saturationMod(5).effect(
            () -> new MobEffectInstance(EffectRegistry.ATTACK.get(),  180 * 20, 3), 1).effect(
            () -> new MobEffectInstance(EffectRegistry.CRITICAL_RATE.get(), 180 * 20, 1), 1).effect(
            () -> new MobEffectInstance(EffectRegistry.CRITICAL_DAMAGE.get(), 180 * 20, 1), 1).alwaysEat().build();

    //防御类 增加防御力
    public static final FoodProperties DEFENSE_TWO = (new FoodProperties.Builder()).nutrition(3).saturationMod(2).effect(
            () -> new MobEffectInstance(EffectRegistry.DEFENSE.get(),  60 * 20, 0), 1).alwaysEat().build();

    public static final FoodProperties DEFENSE_THREE = (new FoodProperties.Builder()).nutrition(5).saturationMod(3).effect(
            () -> new MobEffectInstance(EffectRegistry.DEFENSE.get(),  90 * 20, 1), 1).alwaysEat().build();
    public static final FoodProperties DEFENSE_SHIELD = (new FoodProperties.Builder()).nutrition(5).saturationMod(3).effect( //护盾强效
            () -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 30 * 20, 0),1).alwaysEat().build();

    public static final FoodProperties DEFENSE_FOUR = (new FoodProperties.Builder()).nutrition(7).saturationMod(4).effect(
            () -> new MobEffectInstance(EffectRegistry.DEFENSE.get(),  120 * 20, 2), 1).alwaysEat().build();
    public static final FoodProperties DEFENSE_FOUR_SHIELD = (new FoodProperties.Builder()).nutrition(7).saturationMod(4).effect(
            () -> new MobEffectInstance(EffectRegistry.DEFENSE.get(),  120 * 20, 2), 1).effect(
            () -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 60 * 20, 1), 1).alwaysEat().build();
    public static final FoodProperties DEFENSE_FOUR_RECOVER = (new FoodProperties.Builder()).nutrition(7).saturationMod(4).effect(
            () -> new MobEffectInstance(EffectRegistry.DEFENSE.get(),  120 * 20, 2), 1).effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION, 60 * 20, 0), 1).alwaysEat().build();

    public static final FoodProperties DEFENSE_FIVE = (new FoodProperties.Builder()).nutrition(9).saturationMod(5).effect(
            () -> new MobEffectInstance(EffectRegistry.DEFENSE.get(),  180 * 20, 3), 1).alwaysEat().build();

    //暴击类
    public static final FoodProperties RATE_TWO = (new FoodProperties.Builder()).nutrition(4).saturationMod(1).effect(
            () -> new MobEffectInstance(EffectRegistry.CRITICAL_RATE.get(), 60 * 20, 0),1).alwaysEat().build();
    public static final FoodProperties RATE_THREE = (new FoodProperties.Builder()).nutrition(5).saturationMod(2).effect(
            () -> new MobEffectInstance(EffectRegistry.CRITICAL_RATE.get(), 120 * 20, 1),1).alwaysEat().build();
    public static final FoodProperties RATE_FOUR = (new FoodProperties.Builder()).nutrition(6).saturationMod(3).effect(
            () -> new MobEffectInstance(EffectRegistry.CRITICAL_RATE.get(), 180 * 20, 2),1).alwaysEat().build();


    //体力类 额外恢复饱食度 + 饱腹效果
    public static final FoodProperties FOOD_ONE = (new FoodProperties.Builder()).nutrition(3).saturationMod(2).effect(
            () -> new MobEffectInstance(MobEffects.SATURATION,  1 * 20, 0), 1).build();

    public static final FoodProperties FOOD_TWO = (new FoodProperties.Builder()).nutrition(6).saturationMod(5).effect(
            () -> new MobEffectInstance(MobEffects.SATURATION,  2 * 20, 0), 1).build();
    public static final FoodProperties FOOD_TWO_AND_FIRE = (new FoodProperties.Builder()).nutrition(6).saturationMod(5).effect( //稠汁蔬菜炖肉
            () -> new MobEffectInstance(MobEffects.SATURATION,  2 * 20, 0), 1).effect(
            () -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE,  10 * 20, 0), 1).build();

    public static final FoodProperties FOOD_THREE = (new FoodProperties.Builder()).nutrition(10).saturationMod(8).effect(
            () -> new MobEffectInstance(MobEffects.SATURATION,  4 * 20, 0), 1).build();
    public static final FoodProperties FOOD_FOUR = (new FoodProperties.Builder()).nutrition(15).saturationMod(13).effect(
            () -> new MobEffectInstance(MobEffects.SATURATION,  6 * 20, 0), 1).build();
    public static final FoodProperties FOOD_FIVE = (new FoodProperties.Builder()).nutrition(20).saturationMod(17).effect(
            () -> new MobEffectInstance(MobEffects.SATURATION,  10 * 20, 1), 1).build();

    //生命上限
    public static final FoodProperties HEALTH_BOOST = (new FoodProperties.Builder()).nutrition(6).saturationMod(3).effect(
            () -> new MobEffectInstance(MobEffects.HEALTH_BOOST,  70 * 20, 1), 1).build();

    //雷神特殊料理 裁决之时
    public static final FoodProperties BUG_FOOD = (new FoodProperties.Builder()).nutrition(1).saturationMod(0).effect(
            () -> new MobEffectInstance(MobEffects.WITHER,  10 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.DIG_SLOWDOWN,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.CONFUSION,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.BLINDNESS,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.HUNGER,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.POISON,  20 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.UNLUCK,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.BAD_OMEN,  120 * 20, 1), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.WEAKNESS, 20 * 20, 4), 0.1f).alwaysEat().build();

    public static final FoodProperties PAIMENG_FOOD = (new FoodProperties.Builder()).nutrition(20).saturationMod(30).effect(
            () -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.DIG_SPEED,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.DAMAGE_BOOST,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.HEAL,  2 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.JUMP,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.REGENERATION,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.FIRE_RESISTANCE,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.WATER_BREATHING,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.NIGHT_VISION,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.HEALTH_BOOST,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.ABSORPTION,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.SATURATION,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.LUCK,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.SLOW_FALLING,  60 * 20, 4), 0.1f).effect(
            () -> new MobEffectInstance(MobEffects.HERO_OF_THE_VILLAGE,  60 * 20, 1), 0.1f).alwaysEat().build();

}
