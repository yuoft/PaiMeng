package com.yuo.PaiMeng.Effects;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class ModPlainEffect extends Effect {
    protected ModPlainEffect(int colorIn) {
        super(EffectType.BENEFICIAL, colorIn);
    }
}
