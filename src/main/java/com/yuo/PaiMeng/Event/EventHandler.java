package com.yuo.PaiMeng.Event;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Random;

/**
 * 事件处理类
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PaiMeng.MOD_ID)
public class EventHandler {
    private final static Random random = new Random();

    //烹饪锅（灶台）被点燃或熄灭
    @SubscribeEvent
    public static void potFire(PlayerInteractEvent.RightClickBlock event) {
    }

}

