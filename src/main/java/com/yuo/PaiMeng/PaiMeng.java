package com.yuo.PaiMeng;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.Capability.BlowCapability;
import com.yuo.PaiMeng.Capability.IBlowCapability;
import com.yuo.PaiMeng.Capability.ModStorage;
import com.yuo.PaiMeng.Container.ContainerTypeRegistry;
import com.yuo.PaiMeng.Effects.EffectRegistry;
import com.yuo.PaiMeng.Entity.BoarEntity;
import com.yuo.PaiMeng.Entity.CraneEntity;
import com.yuo.PaiMeng.Entity.EntityTypeRegister;
import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.Recipes.RecipeSerializerRegistry;
import com.yuo.PaiMeng.Tiles.TileTypeRegistry;
import com.yuo.PaiMeng.WorldGen.FeatureInit;
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("paimeng")
@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class PaiMeng {
	public static final String MOD_ID = "paimeng";
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new); //用于客户端获取tile坐标
	public PaiMeng() {
//        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, PMConfig.CLIENT_CONFIG);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PMConfig.SERVER_CONFIG); //配置文件
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
		//注册物品至mod总线
        ItemRegistry.ITEMS.register(modEventBus);
        BlockRegistry.BLOCKS.register(modEventBus);
        TileTypeRegistry.TILE_ENTITIES.register(modEventBus);
        ContainerTypeRegistry.CONTAINERS.register(modEventBus);
        RecipeSerializerRegistry.RECIPE_TYPES.register(modEventBus);
        FeatureInit.FEATURES.register(modEventBus);
        EffectRegistry.EFFECTS.register(modEventBus);
        EntityTypeRegister.ENTITY_TYPE.register(modEventBus);
        ClientRegistry.registerKeyBinding(ClientProxy.KEY_OPEN_RELICS); //快捷键注册
        PROXY.init();
//        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, WorldOreGen::generateOres); //注册矿物生成
    }


    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
        event.enqueueWork( () -> {
            //能力实例 数据保存 默认创建
            CapabilityManager.INSTANCE.register(IBlowCapability.class, new ModStorage<>(), BlowCapability::new);
        });


    }

}
