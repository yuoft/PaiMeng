package com.yuo.PaiMeng.Items.Food;

import net.minecraft.item.Food;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;

public class ModFoods {
    //食物属性构建  补充饥饿值，饱腹度  药水效果 获取药水效果概率（1=100%） 总是可以食用 肉
    //甜甜花酿鸡
    public static final Food TIANTIANHUA_NIANGJI = (new Food.Builder()).hunger(5).saturation(3).effect(
            () -> new EffectInstance(Effects.INSTANT_HEALTH,  30 * 20, 1), 1).setAlwaysEdible().meat().build();
}
