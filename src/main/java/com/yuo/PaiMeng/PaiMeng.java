package com.yuo.PaiMeng;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Capability.IBlowCapability;
import com.yuo.PaiMeng.Capability.RelicsItemHandler;
import com.yuo.PaiMeng.Container.ContainerTypeRegistry;
import com.yuo.PaiMeng.Effects.EffectRegistry;
import com.yuo.PaiMeng.Entity.EntityTypeRegister;
import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.Recipes.RecipeSerializerRegistry;
import com.yuo.PaiMeng.Tiles.TileTypeRegistry;
import com.yuo.PaiMeng.WorldGen.EntityGen;
import com.yuo.PaiMeng.WorldGen.FeatureInit;
import com.yuo.PaiMeng.WorldGen.WorldEventHandler;
import com.yuo.PaiMeng.tab.PMGroup;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("paimeng")
public class PaiMeng {
	public static final String MOD_ID = "paimeng";
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new); //用于客户端获取tile坐标

	@SuppressWarnings("removal")
	public PaiMeng() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PMConfig.SERVER_CONFIG); //配置文件
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::registerCapabilities);
		//注册物品至mod总线
        PMItems.ITEMS.register(modEventBus);
        PMBlocks.BLOCKS.register(modEventBus);
        PMGroup.TABS.register(modEventBus);
        TileTypeRegistry.TILE_ENTITIES.register(modEventBus);
        ContainerTypeRegistry.CONTAINERS.register(modEventBus);
        RecipeSerializerRegistry.RECIPE_TYPES.register(modEventBus);
        FeatureInit.FEATURES.register(modEventBus);
        EffectRegistry.EFFECTS.register(modEventBus);
        EntityTypeRegister.ENTITY_TYPE.register(modEventBus);
        //1.20.1 世界生成改为数据包 JSON（src/main/resources/data/paimeng/worldgen/**）。
        //代码注册 ConfiguredFeature/PlacedFeature 会被 Forge 判定为 "Unbound values"，导致 biome_modifier 引用失败，
        //所以这里不再注册（WorldEventHandler.registerFeatures 保留，便于以后需要时切回代码方式）。
        // modEventBus.addListener(WorldEventHandler::registerFeatures);
        //刷怪位置规则（属性在 Event\MobEvent 里注册）
        MinecraftForge.EVENT_BUS.addListener(EntityGen::registerSpawnPlacements);
        PROXY.init();
    }


    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
    }

    // 1.20.1 的能力注册（IStorage / CapabilityManager.INSTANCE.register 已被移除）
    private void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(IBlowCapability.class);
        event.register(RelicsItemHandler.class);
    }

}
