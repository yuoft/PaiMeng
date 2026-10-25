package com.yuo.PaiMeng.Effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModPlainEffect extends MobEffect {
    protected ModPlainEffect(int colorIn) {
        super(MobEffectCategory.BENEFICIAL, colorIn);
    }
}
