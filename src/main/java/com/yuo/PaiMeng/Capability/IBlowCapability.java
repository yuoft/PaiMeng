package com.yuo.PaiMeng.Capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

//双爆能力接口
public interface IBlowCapability extends INBTSerializable<CompoundNBT> {
    /**
     * 设置暴击率
     * @param criticalRate 0 ~ 1.0d
     */
    void setCriticalRate(double criticalRate);

    /**
     * 设置暴击伤害
     * @param criticalDamage 0 ~ infinity
     */
    void setCriticalDamage(double criticalDamage);

    /**
     * 重置双爆属性为默认值
     */
    void resetCritical();

    /**
     * 获取当前暴击率
     * @return 暴击率值
     */
    double getCriticalRate();

    /**
     * 获取当前暴击伤害
     * @return 暴击伤害值
     */
    double getCriticalDamage();
}
