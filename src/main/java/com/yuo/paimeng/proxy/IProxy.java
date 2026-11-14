package com.yuo.paimeng.proxy;

import net.minecraftforge.eventbus.api.IEventBus;

public interface IProxy {
    default void registerHandlers(IEventBus modBus) {}
}
