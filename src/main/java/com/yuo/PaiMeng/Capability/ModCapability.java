package com.yuo.PaiMeng.Capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

//能力列表
public class ModCapability {
    @CapabilityInject(IBlowCapability.class)
    public static Capability<IBlowCapability> BLOW_CAPABILITY;

    @CapabilityInject(RelicsItemHandler.class)
    public static Capability<RelicsItemHandler> RELICS_CAPABILITY;
}
