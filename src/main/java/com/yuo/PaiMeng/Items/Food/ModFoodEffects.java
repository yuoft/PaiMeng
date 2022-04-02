package com.yuo.PaiMeng.Items.Food;

import com.yuo.PaiMeng.Effects.EffectRegistry;
import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModFoodEffects {
    //食物属性构建  补充饥饿值，饱腹度  药水效果 获取药水效果概率（1=100%） 总是可以食用 肉
    public static final Food TIANTIANHUA_NIANGJI = (new Food.Builder()).hunger(2).saturation(1).effect(
            () -> new EffectInstance(Effects.REGENERATION,  30 * 20, 0), 1).effect(
            () -> new EffectInstance(Effects.SLOW_FALLING, 20 * 20, 1), 1).setAlwaysEdible().build();

    //回复类 瞬间恢复多少血量 + 持续恢复效果
    public static final Food RECOVER_ONE = (new Food.Builder()).hunger(2).saturation(1).effect(
            () -> new EffectInstance(Effects.REGENERATION,  30 * 20, 0), 1).setAlwaysEdible().build();
    public static final Food RECOVER_TWO = (new Food.Builder()).hunger(4).saturation(2).effect(
            () -> new EffectInstance(Effects.REGENERATION,  50 * 20, 1), 1).setAlwaysEdible().build();
    public static final Food RECOVER_THREE = (new Food.Builder()).hunger(6).saturation(3).effect(
            () -> new EffectInstance(Effects.REGENERATION,  70 * 20, 2), 1).setAlwaysEdible().build();
    public static final Food RECOVER_FOUR = (new Food.Builder()).hunger(8).saturation(4).effect(
            () -> new EffectInstance(Effects.REGENERATION,  90 * 20, 3), 1).setAlwaysEdible().build();
    public static final Food RECOVER_FIVE = (new Food.Builder()).hunger(10).saturation(5).effect(
            () -> new EffectInstance(Effects.REGENERATION,  120 * 20, 4), 1).setAlwaysEdible().build();

    //复活类 不死图腾效果 + 恢复多少血量
    /**
     * 复活
     */
    public static final Food REVIVE_ONE = (new Food.Builder()).hunger(2).saturation(1).effect(
            () -> new EffectInstance(EffectRegistry.REVIVE.get(),  60 * 20, 0), 1).setAlwaysEdible().build();
    public static final Food REVIVE_TWO = (new Food.Builder()).hunger(4).saturation(2).effect(
            () -> new EffectInstance(EffectRegistry.REVIVE.get(),  120 * 20, 1), 1).setAlwaysEdible().build();
    public static final Food REVIVE_THREE = (new Food.Builder()).hunger(6).saturation(3).effect(
            () -> new EffectInstance(EffectRegistry.REVIVE.get(),  180 * 20, 2), 1).setAlwaysEdible().build();
    public static final Food REVIVE_FOUR = (new Food.Builder()).hunger(8).saturation(4).effect(
            () -> new EffectInstance(EffectRegistry.REVIVE.get(),  240 * 20, 3), 1).setAlwaysEdible().build();
    public static final Food REVIVE_FIVE = (new Food.Builder()).hunger(10).saturation(5).effect(
            () -> new EffectInstance(EffectRegistry.REVIVE.get(),  300 * 20, 4), 1).setAlwaysEdible().build();

    //攻击类
    // 增加攻击力
    public static final Food ATTACK_TWO = (new Food.Builder()).hunger(3).saturation(2).effect(
            () -> new EffectInstance(EffectRegistry.ATTACK.get(),  60 * 20, 0), 1).setAlwaysEdible().build();
    public static final Food ATTACK_TWO_RATE = (new Food.Builder()).hunger(3).saturation(2).effect(
            () -> new EffectInstance(EffectRegistry.ATTACK.get(),  60 * 20, 0), 1).effect(
            () -> new EffectInstance(EffectRegistry.CRITICAL_RATE.get(), 60 * 20, 0),1).setAlwaysEdible().build();

    public static final Food ATTACK_THREE = (new Food.Builder()).hunger(5).saturation(3).effect(
            () -> new EffectInstance(EffectRegistry.ATTACK.get(),  90 * 20, 1), 1).setAlwaysEdible().build();
    public static final Food ATTACK_THREE_RATE = (new Food.Builder()).hunger(5).saturation(3).effect(
            () -> new EffectInstance(EffectRegistry.ATTACK.get(),  90 * 20, 1), 1).effect(
            () -> new EffectInstance(EffectRegistry.CRITICAL_RATE.get(), 90 * 20, 2),1).setAlwaysEdible().build();
    public static final Food ATTACK_PHYSICS = (new Food.Builder()).hunger(5).saturation(3).effect(
            () -> new EffectInstance(EffectRegistry.ATTACK_PHYSICS.get(),  90 * 20, 0), 1).setAlwaysEdible().build();

    public static final Food ATTACK_FOUR = (new Food.Builder()).hunger(7).saturation(4).effect(
            () -> new EffectInstance(EffectRegistry.ATTACK.get(),  120 * 20, 2), 1).setAlwaysEdible().build();
    public static final Food ATTACK_FOUR_RATE = (new Food.Builder()).hunger(7).saturation(4).effect( //加暴击
            () -> new EffectInstance(EffectRegistry.ATTACK.get(),  120 * 20, 2), 1).effect(
            () -> new EffectInstance(EffectRegistry.CRITICAL_RATE.get(), 120 * 20, 0),1).setAlwaysEdible().build();
    public static final Food ATTACK_PHYSICS_RATE = (new Food.Builder()).hunger(7).saturation(4).effect( //加暴击 物理
            () -> new EffectInstance(EffectRegistry.ATTACK_PHYSICS.get(),  120 * 20, 1), 1).effect(
            () -> new EffectInstance(EffectRegistry.CRITICAL_RATE.get(), 120 * 20, 0),1).setAlwaysEdible().build();

    public static final Food ATTACK_FIVE = (new Food.Builder()).hunger(9).saturation(5).effect(
            () -> new EffectInstance(EffectRegistry.ATTACK.get(),  180 * 20, 3), 1).setAlwaysEdible().build();
    public static final Food ATTACK_FIVE_RATE_DAMAGE = (new Food.Builder()).hunger(9).saturation(5).effect(
            () -> new EffectInstance(EffectRegistry.ATTACK.get(),  180 * 20, 3), 1).effect(
            () -> new EffectInstance(EffectRegistry.CRITICAL_RATE.get(), 180 * 20, 1), 1).effect(
            () -> new EffectInstance(EffectRegistry.CRITICAL_DAMAGE.get(), 180 * 20, 1), 1).setAlwaysEdible().build();

    //防御类 增加防御力
    public static final Food DEFENSE_TWO = (new Food.Builder()).hunger(3).saturation(2).effect(
            () -> new EffectInstance(EffectRegistry.DEFENSE.get(),  60 * 20, 0), 1).setAlwaysEdible().build();

    public static final Food DEFENSE_THREE = (new Food.Builder()).hunger(5).saturation(3).effect(
            () -> new EffectInstance(EffectRegistry.DEFENSE.get(),  90 * 20, 1), 1).setAlwaysEdible().build();
    public static final Food DEFENSE_SHIELD = (new Food.Builder()).hunger(5).saturation(3).effect( //护盾强效
            () -> new EffectInstance(Effects.RESISTANCE, 30 * 20, 0),1).setAlwaysEdible().build();

    public static final Food DEFENSE_FOUR = (new Food.Builder()).hunger(7).saturation(4).effect(
            () -> new EffectInstance(EffectRegistry.DEFENSE.get(),  120 * 20, 2), 1).setAlwaysEdible().build();
    public static final Food DEFENSE_FOUR_SHIELD = (new Food.Builder()).hunger(7).saturation(4).effect(
            () -> new EffectInstance(EffectRegistry.DEFENSE.get(),  120 * 20, 2), 1).effect(
            () -> new EffectInstance(Effects.RESISTANCE, 60 * 20, 1), 1).setAlwaysEdible().build();
    public static final Food DEFENSE_FOUR_RECOVER = (new Food.Builder()).hunger(7).saturation(4).effect(
            () -> new EffectInstance(EffectRegistry.DEFENSE.get(),  120 * 20, 2), 1).effect(
            () -> new EffectInstance(Effects.REGENERATION, 60 * 20, 0), 1).setAlwaysEdible().build();

    public static final Food DEFENSE_FIVE = (new Food.Builder()).hunger(9).saturation(5).effect(
            () -> new EffectInstance(EffectRegistry.DEFENSE.get(),  180 * 20, 3), 1).setAlwaysEdible().build();

    //体力类 额外恢复饱食度 + 饱腹效果
    public static final Food FOOD_ONE = (new Food.Builder()).hunger(3).saturation(2).effect(
            () -> new EffectInstance(Effects.SATURATION,  1 * 20, 0), 1).build();

    public static final Food FOOD_TWO = (new Food.Builder()).hunger(6).saturation(5).effect(
            () -> new EffectInstance(Effects.SATURATION,  2 * 20, 0), 1).build();
    public static final Food FOOD_TWO_AND_FIRE = (new Food.Builder()).hunger(6).saturation(5).effect( //稠汁蔬菜炖肉
            () -> new EffectInstance(Effects.SATURATION,  2 * 20, 0), 1).effect(
            () -> new EffectInstance(Effects.FIRE_RESISTANCE,  10 * 20, 0), 1).build();

    public static final Food FOOD_THREE = (new Food.Builder()).hunger(10).saturation(8).effect(
            () -> new EffectInstance(Effects.SATURATION,  4 * 20, 0), 1).build();
    public static final Food FOOD_FOUR = (new Food.Builder()).hunger(15).saturation(13).effect(
            () -> new EffectInstance(Effects.SATURATION,  6 * 20, 0), 1).build();
    public static final Food FOOD_FIVE = (new Food.Builder()).hunger(20).saturation(17).effect(
            () -> new EffectInstance(Effects.SATURATION,  10 * 20, 1), 1).build();

    //生命上限
    public static final Food HEALTH_BOOST = (new Food.Builder()).hunger(6).saturation(3).effect(
            () -> new EffectInstance(Effects.HEALTH_BOOST,  70 * 20, 1), 1).build();

    //雷神特殊料理 裁决之时
    public static final Food BUG_FOOD = (new Food.Builder()).hunger(1).saturation(0).effect(
            () -> new EffectInstance(Effects.WITHER,  10 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.SLOWNESS,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.MINING_FATIGUE,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.NAUSEA,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.BLINDNESS,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.HUNGER,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.POISON,  20 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.UNLUCK,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.BAD_OMEN,  120 * 20, 1), 0.1f).effect(
            () -> new EffectInstance(Effects.WEAKNESS, 20 * 20, 4), 0.1f).setAlwaysEdible().build();

    public static final Food PAIMENG_FOOD = (new Food.Builder()).hunger(20).saturation(30).effect(
            () -> new EffectInstance(Effects.SPEED,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.HASTE,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.STRENGTH,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.INSTANT_HEALTH,  2 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.JUMP_BOOST,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.REGENERATION,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.RESISTANCE,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.FIRE_RESISTANCE,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.WATER_BREATHING,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.NIGHT_VISION,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.HEALTH_BOOST,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.ABSORPTION,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.SATURATION,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.LUCK,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.SLOW_FALLING,  60 * 20, 4), 0.1f).effect(
            () -> new EffectInstance(Effects.HERO_OF_THE_VILLAGE,  60 * 20, 1), 0.1f).setAlwaysEdible().build();

}
