package com.yuo.PaiMeng;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.Blocks.Tree.AppleSapling;
import com.yuo.PaiMeng.Gui.ContainerTypeRegistry;
import com.yuo.PaiMeng.Gui.PotScreen;
import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.Recipes.RecipeSerializerRegistry;
import com.yuo.PaiMeng.Tiles.TileTypeRegistry;
import com.yuo.PaiMeng.WorldGen.FeatureInit;
import net.minecraft.block.Block;
import net.minecraft.block.CropsBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("paimeng")
@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PaiMeng {
	public static final String MOD_ID = "paimeng";
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new); //用于客户端获取tile坐标
	public PaiMeng() {
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::clientSetup);
        modEventBus.addListener(this::commonSetup);
		//注册物品至mod总线
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        TileTypeRegistry.TILE_ENTITIES.register(modEventBus);
        ContainerTypeRegistry.CONTAINERS.register(modEventBus);
        RecipeSerializerRegistry.RECIPE_TYPES.register(modEventBus);
        FeatureInit.FEATURES.register(modEventBus);
        PROXY.init();
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
        //透明方块的渲染
        for (RegistryObject r : BlockRegistry.BLOCKS.getEntries()){
            if (r.get() instanceof CropsBlock || r.get() instanceof AppleSapling){
                RenderTypeLookup.setRenderLayer((Block) r.get(), RenderType.getCutout());
            }
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
    }
}
