package com.yuo.paimeng;


import com.yuo.paimeng.blocks.PMBlocks;
import com.yuo.paimeng.effects.EffectRegistry;
import com.yuo.paimeng.entity.PMEntityTypes;
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
    public static CommonProxy PROXY = DistExecutor.safeRunForDist(() -> ClientProxy::new, () -> CommonProxy::new);

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
        PMEntityTypes.ENTITY_TYPE.register(modEventBus);
        MinecraftForge.EVENT_BUS.addListener(EntityGen::registerSpawnPlacements);
        PROXY.registerHandlers(modEventBus);

        /*
         * 参考《原神》里的无相系列boss，设计一个无相BOSS;外形:2*2*2方块外壳加中间倾斜方块核心，外壳用原版击中颜色相差较大的方块纹理，核心用基岩；
         * 攻击：近战（用外壳方块组成一字型，十字形，x形等对5格内生物拍下），
         * 远程（发射随机方块攻击距离较远生物，概率雷击5-16内生物（多层环状）），击杀后掉落神之心。
         * 添加高级烹饪台（无需燃料，不用合成进度条）
         * 第6个圣遗物栏位放置神之心。
         */
    }

}
