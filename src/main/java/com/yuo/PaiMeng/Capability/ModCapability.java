package com.yuo.PaiMeng.Capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;

//能力列表
//1.20.1 中 @CapabilityInject 已被移除，改为 CapabilityManager.get(new CapabilityToken<>() {})
public class ModCapability {
    public static final Capability<IBlowCapability> BLOW_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    public static final Capability<RelicsItemHandler> RELICS_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });
}
