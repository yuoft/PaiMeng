package com.yuo.PaiMeng.Event;

import com.yuo.PaiMeng.Gui.PotScreen;
import com.yuo.PaiMeng.Gui.RelicsButton;
import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientEventHandler {

    //注册物品染色
    @SubscribeEvent
    public static void itemColors(ColorHandlerEvent.Item event) {
//        for (RegistryObject<Item> object : ItemRegistry.ITEMS.getEntries()) {
//            Item item = object.get();
//            if (item instanceof Drug){
//            }
//        }
        event.getItemColors().register((stack, color) -> color > 0 ? -1 : PotionUtils.getColor(stack), ItemRegistry.drug.get());
    }



}
