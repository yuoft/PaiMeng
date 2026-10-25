package com.yuo.PaiMeng.Event;

import com.yuo.PaiMeng.Client.Gui.RelicsButton;
import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventHandler {

    //注册物品染色
    //1.20.1: ColorHandlerEvent.Item -> RegisterColorHandlersEvent.Item（仍然是 mod 总线事件）
    @SubscribeEvent
    public static void itemColors(RegisterColorHandlersEvent.Item event) {
        event.getItemColors().register((stack, color) -> color > 0 ? -1 : PotionUtils.getColor(stack), PMItems.drug.get());
    }

    /**
     * 游戏总线（Forge bus）的客户端事件。
     * 1.20.1: GuiScreenEvent.InitGuiEvent.Post -> ScreenEvent.Init.Post，
     * 且这类事件只在客户端存在，不能放在通用包（服务端会加载不到这些类）。
     */
    @Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID, value = Dist.CLIENT)
    public static class ForgeBus {

        //在玩家物品栏界面上添加切换到圣遗物界面的按钮
        @SubscribeEvent
        public static void playerScreen(ScreenEvent.Init.Post event) {
            Screen gui = event.getScreen();
            if (gui instanceof InventoryScreen screen) {
                if (event.getListenersList() != null)
                    event.addListener(new RelicsButton(screen, 77, 44, 16, 16));
            }
        }
    }

}
