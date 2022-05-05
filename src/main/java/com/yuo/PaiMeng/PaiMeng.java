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
import com.yuo.PaiMeng.Entity.Render.BoarRender;
import com.yuo.PaiMeng.Entity.Render.CraneRender;
import com.yuo.PaiMeng.Gui.*;
import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsHelper;
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
import net.minecraft.entity.ai.attributes.GlobalEntityTypeAttributes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
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
        EntityTypeRegister.ENTITY_TYPE.register(modEventBus);
        PROXY.init();
//        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, WorldOreGen::generateOres); //注册矿物生成
    }

    private  void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(BlockRegistry.cookingPot.get(), RenderType.getCutoutMipped()); //不完整方块渲染
            RenderTypeLookup.setRenderLayer(BlockRegistry.cookingBench.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.zhusunCrop.get(), RenderType.getCutout());
            ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.BENCH_TILE.get(), BenchTileTER::new); //特殊ter渲染
        });
        //实体渲染
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegister.BOAR.get(), BoarRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegister.CRANE.get(), CraneRender::new);
        //绑定Container和ContainerScreen
        event.enqueueWork(() -> {
            ScreenManager.registerFactory(ContainerTypeRegistry.potContainer.get(), PotScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.benchContainer.get(), BenchScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.synPlatContainer.get(), SynPlatScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.seedBoxContainer.get(), SeedBoxScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.relicsContainer.get(), RelicsScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.strengthenTableContainer.get(), StrengthenTableScreen::new);
        });
        //透明方块的渲染
        for (RegistryObject<Block> r : BlockRegistry.BLOCKS.getEntries()){
            if (r.get() instanceof BushBlock){
                RenderTypeLookup.setRenderLayer(r.get(), RenderType.getCutout());
            }
        }
        //圣遗物
        event.enqueueWork( () ->{
            for (RegistryObject<Item> entry : ItemRegistry.ITEMS.getEntries()) {
                if (entry.get() instanceof Relics)
                    setRelicsProperty(entry.get());
            }
        });
    }

    //设置圣遗物的动态属性 根据不同部位更改贴图
    private void setRelicsProperty(Item item){
        ItemModelsProperties.registerProperty(item, new ResourceLocation(PaiMeng.MOD_ID,
                "type"), (itemStack, clientWorld, livingEntity) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return RelicsHelper.getTypeForStack(itemStack).getId();
            }
        });
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(NetWorkHandler::registerMessage); //创建数据包
        event.enqueueWork( () -> {
            //能力实例 数据保存 默认创建
            CapabilityManager.INSTANCE.register(IBlowCapability.class, new ModStorage<>(), BlowCapability::new);

            //实体属性
            GlobalEntityTypeAttributes.put(EntityTypeRegister.BOAR.get(), BoarEntity.setCustomAttributes().create());
            GlobalEntityTypeAttributes.put(EntityTypeRegister.CRANE.get(), CraneEntity.setCustomAttributes().create());
        });

    }

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
