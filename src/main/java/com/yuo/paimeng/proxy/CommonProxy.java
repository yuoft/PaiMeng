package com.yuo.paimeng.proxy;

import com.yuo.paimeng.capability.IBlowCapability;
import com.yuo.paimeng.capability.RelicsItemHandler;
import com.yuo.paimeng.network.NetWorkHandler;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

public class CommonProxy implements IProxy {
    @Override
    public void registerHandlers(IEventBus modBus) {
        IProxy.super.registerHandlers(modBus);
        modBus.addListener(this::commonSetup);
        modBus.addListener(this::registerCapabilities);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
    }

    //能力注册
    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IBlowCapability.class);
        event.register(RelicsItemHandler.class);
    }
}
