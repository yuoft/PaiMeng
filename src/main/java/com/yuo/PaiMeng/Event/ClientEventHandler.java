package com.yuo.PaiMeng.Event;

import com.yuo.PaiMeng.ClientProxy;
import com.yuo.PaiMeng.Gui.PotScreen;
import com.yuo.PaiMeng.Gui.RelicsButton;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.NetWork.RelicsGuiPacket;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID, value = Dist.CLIENT)
public class ClientEventHandler {

    //在玩家物品栏界面上添加切换到圣遗物界面的按钮
    @SubscribeEvent
    public static void playerScreen(GuiScreenEvent.InitGuiEvent.Post event){
        Screen gui = event.getGui();
        if (gui instanceof InventoryScreen){
            InventoryScreen screen = (InventoryScreen) gui;
            if (event.getWidgetList() != null)
                event.addWidget(new RelicsButton(screen,77,44, 16, 16));
        }
        if (gui instanceof PotScreen){
            PotScreen potScreen = (PotScreen) gui;
            ClientPlayerEntity player = Minecraft.getInstance().player;
        }
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (ClientProxy.KEY_OPEN_RELICS.isPressed() && Minecraft.getInstance().isGameFocused()) {
                NetWorkHandler.INSTANCE.sendToServer(new RelicsGuiPacket(true));
            }
        }
    }
}
