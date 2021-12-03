package com.yuo.PaiMeng;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.Gui.ContainerTypeRegistry;
import com.yuo.PaiMeng.Gui.PotScreen;
import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.Tiles.TileTypeRegistry;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("paimeng")
@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PaiMeng {
	public static final String MOD_ID = "paimeng";
	public PaiMeng() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::clientSetup);
		//注册物品至mod总线
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        TileTypeRegistry.TILE_ENTITIES.register(modEventBus);
        ContainerTypeRegistry.CONTAINERS.register(modEventBus);
    }

    private  void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(BlockRegistry.cookingPot.get(), RenderType.getCutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.cookingBench.get(), RenderType.getCutout());
        });
        //绑定Container和ContainerScreen
        event.enqueueWork(() -> {
            ScreenManager.registerFactory(ContainerTypeRegistry.potContainer.get(), PotScreen::new);
        });
    }
}
