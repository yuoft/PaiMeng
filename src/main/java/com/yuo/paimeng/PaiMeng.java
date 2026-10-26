package com.yuo.paimeng;

import com.yuo.paimeng.blocks.PMBlocks;
import com.yuo.paimeng.capability.IBlowCapability;
import com.yuo.paimeng.capability.RelicsItemHandler;
import com.yuo.paimeng.menu.ContainerTypeRegistry;
import com.yuo.paimeng.effects.EffectRegistry;
import com.yuo.paimeng.entity.EntityTypeRegister;
import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.network.NetWorkHandler;
import com.yuo.paimeng.recipe.RecipeSerializerRegistry;
import com.yuo.paimeng.tiles.TileTypeRegistry;
import com.yuo.paimeng.worldgen.EntityGen;
import com.yuo.paimeng.worldgen.FeatureInit;
import com.yuo.paimeng.tab.PMGroup;
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
        //世界生成完全由 datagen 产出（data/paimeng/worldgen/** 与 data/paimeng/forge/biome_modifier/**，
        //在 src/generated/resources 里，由 runData 生成），运行时不需要再注册 ConfiguredFeature/PlacedFeature。
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
