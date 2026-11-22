package com.yuo.paimeng.advancement;

import net.minecraft.advancements.CriteriaTriggers;

/**
 * 模组自定义的进度判据。
 *
 * <p>1.20.1 的判据没有独立注册表，统一通过 {@link CriteriaTriggers#register} 注册，
 * 需要在数据包加载前（模组构造阶段）调用 {@link #register()}。
 */
public final class PMTriggers {

    /** 吃遍模组全部料理（排除裁决之时、派蒙料理）。 */
    public static final AllFoodEatenTrigger ALL_FOOD_EATEN = new AllFoodEatenTrigger();

    public static void register() {
        CriteriaTriggers.register(ALL_FOOD_EATEN);
    }

    private PMTriggers() {
    }
}
