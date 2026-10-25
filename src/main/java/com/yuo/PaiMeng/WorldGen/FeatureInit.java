package com.yuo.PaiMeng.WorldGen;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Feature 注册
 * 1.16: NoFeatureConfig -> 1.20.1: NoneFeatureConfiguration
 * BlockClusterFeatureConfig / ProbabilityConfig 在 1.20.1 已删除，相关 Feature 改用 WorldGen 包内的自定义 Config
 */
public class FeatureInit {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, PaiMeng.MOD_ID);

    public static final RegistryObject<AppleTreeFeature> APPLE_TREE = FEATURES.register("apple_tree",
            () -> new AppleTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<AppleTreeFeature> SUN_APPLE_TREE = FEATURES.register("sun_apple_tree",
            () -> new AppleTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<AppleTreeFeature> PURPLE_APPLE_TREE = FEATURES.register("purple_apple_tree",
            () -> new AppleTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<AppleTreeFeature> ZAOYE_TREE = FEATURES.register("zaoye_tree",
            () -> new AppleTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<AppleTreeFeature> PAOPAOJU_TREE = FEATURES.register("paopaoju_tree",
            () -> new AppleTreeFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<WitheredTreeFeature> WITHERED_TREE = FEATURES.register("withered_tree",
            () -> new WitheredTreeFeature(NoneFeatureConfiguration.CODEC));

    public static final RegistryObject<RiverFeature> RIVER_PLANT = FEATURES.register("river_plant",
            () -> new RiverFeature(PlantConfig.CODEC));
    //1.20.1: BlockClusterFeatureConfig 已删除，MountainFeature 改用 PlantConfig(state + tries)
    public static final RegistryObject<MountainFeature> MOUNTAIN_PLANT = FEATURES.register("mountain_plant",
            () -> new MountainFeature(PlantConfig.CODEC));
    public static final RegistryObject<OceanFeature> OCEAN_PLANT = FEATURES.register("ocean_plant",
            () -> new OceanFeature(OceanProbabilityConfig.CODEC));
    public static final RegistryObject<NetherFeature> NETHER_PLANT = FEATURES.register("nether_plant",
            () -> new NetherFeature(NetherConfig.CODEC));
    public static final RegistryObject<LavaFeature> LAVA_PLANT = FEATURES.register("lava_plant",
            () -> new LavaFeature(PlantConfig.CODEC));

}
