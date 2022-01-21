package com.yuo.PaiMeng;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.Capability.BlowCapability;
import com.yuo.PaiMeng.Capability.IBlowCapability;
import com.yuo.PaiMeng.Capability.ModStorage;
import com.yuo.PaiMeng.Effects.EffectRegistry;
import com.yuo.PaiMeng.Gui.*;
import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.Recipes.RecipeSerializerRegistry;
import com.yuo.PaiMeng.Render.BenchTileTER;
import com.yuo.PaiMeng.Tiles.TileTypeRegistry;
import com.yuo.PaiMeng.WorldGen.FeatureInit;
import net.minecraft.block.Block;
import net.minecraft.block.BushBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
        EffectRegistry.EFFECTS.register(modEventBus);
        PROXY.init();
//        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, WorldOreGen::generateOres); //注册矿物生成
    }

    private  void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(BlockRegistry.cookingPot.get(), RenderType.getCutoutMipped());
            RenderTypeLookup.setRenderLayer(BlockRegistry.cookingBench.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.zhusunCrop.get(), RenderType.getCutout());
            ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.BENCH_TILE.get(), BenchTileTER::new);
        });
        //绑定Container和ContainerScreen
        event.enqueueWork(() -> {
            ScreenManager.registerFactory(ContainerTypeRegistry.potContainer.get(), PotScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.benchContainer.get(), BenchScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.synPlatContainer.get(), SynPlatScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.seedBoxContainer.get(), SeedBoxScreen::new);
        });
        //透明方块的渲染
        for (RegistryObject r : BlockRegistry.BLOCKS.getEntries()){
            if (r.get() instanceof BushBlock){
                RenderTypeLookup.setRenderLayer((Block) r.get(), RenderType.getCutout());
            }
        }
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
        event.enqueueWork( () -> {
            //能力实例 数据保存 默认创建
            CapabilityManager.INSTANCE.register(IBlowCapability.class, new ModStorage(), BlowCapability::new);
        });
    }
}
