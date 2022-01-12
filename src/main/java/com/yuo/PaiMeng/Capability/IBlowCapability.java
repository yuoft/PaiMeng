package com.yuo.PaiMeng.Capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;

//双爆能力接口
public interface IBlowCapability extends INBTSerializable<CompoundNBT> {
    /**
     * 设置暴击率
     * @param criticalRate 0 ~ 1.0f
     */
    void setCriticalRate(float criticalRate);

    /**
     * 设置暴击伤害
     * @param criticalDamage 0 ~ infinity
     */
    void setCriticalDamage(float criticalDamage);

    /**
     * 重置双爆属性为默认值
     */
    void resetCritical();

    /**
     * 获取当前暴击率
     * @return 暴击率值
     */
    float getCriticalRate();

    /**
     * 获取当前暴击伤害
     * @return 暴击伤害值
     */
    float getCriticalDamage();
}
