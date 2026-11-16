package com.yuo.paimeng;

import com.yuo.paimeng.blocks.PMBlocks;
import com.yuo.paimeng.effects.EffectRegistry;
import com.yuo.paimeng.entity.EntityTypeRegister;
import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.menu.PMMenuTypes;
import com.yuo.paimeng.proxy.ClientProxy;
import com.yuo.paimeng.proxy.CommonProxy;
import com.yuo.paimeng.recipe.PMRecipeSerializers;
import com.yuo.paimeng.tiles.TileTypeRegistry;
import com.yuo.paimeng.world.gen.EntityGen;
import com.yuo.paimeng.world.gen.FeatureInit;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("paimeng")
public class PaiMeng {
	public static final String MOD_ID = "paimeng";
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new); //用于客户端获取tile坐标

	@SuppressWarnings("removal")
	public PaiMeng() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PMConfig.SERVER_CONFIG); //配置文件
        final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		//注册物品至mod总线
        PMItems.ITEMS.register(modEventBus);
        PMBlocks.BLOCKS.register(modEventBus);
        PMGroupTab.TABS.register(modEventBus);
        TileTypeRegistry.TILE_ENTITIES.register(modEventBus);
        PMMenuTypes.CONTAINERS.register(modEventBus);
        PMRecipeSerializers.RECIPE_TYPES.register(modEventBus);
        FeatureInit.FEATURES.register(modEventBus);
        EffectRegistry.EFFECTS.register(modEventBus);
        EntityTypeRegister.ENTITY_TYPE.register(modEventBus);
        MinecraftForge.EVENT_BUS.addListener(EntityGen::registerSpawnPlacements);
        PROXY.registerHandlers(modEventBus);

        /*
         * 圣遗物界面添加总属性显示（修改gui），烹饪界面添加开始烹饪按钮
         * 设计无相BOSS，2*2方块加中间倾斜方块核心，掉落神之心
         * 第6个圣遗物栏位放置神之心。
         */
    }

}
