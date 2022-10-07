package com.yuo.PaiMeng.WorldGen;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FeatureInit {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, PaiMeng.MOD_ID);

    public static final RegistryObject<AppleTreeFeature> APPLE_TREE = FEATURES.register("apple_tree",
            () -> new AppleTreeFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<AppleTreeFeature> SUN_APPLE_TREE = FEATURES.register("sun_apple_tree",
            () -> new AppleTreeFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<AppleTreeFeature> PURPLE_APPLE_TREE = FEATURES.register("purple_apple_tree",
            () -> new AppleTreeFeature(NoFeatureConfig.CODEC));
    public static final RegistryObject<WitheredTreeFeature> WITHERED_TREE = FEATURES.register("withered_tree",
            () -> new WitheredTreeFeature(NoFeatureConfig.CODEC));

    public static final RegistryObject<RiverFeature> RIVER_PLANT = FEATURES.register("river_plant",
            () -> new RiverFeature(PlantConfig.CODEC));
    public static final RegistryObject<MountainFeature> MOUNTAIN_PLANT = FEATURES.register("mountain_plant",
            () -> new MountainFeature(BlockClusterFeatureConfig.CODEC));
    public static final RegistryObject<OceanFeature> OCEAN_PLANT = FEATURES.register("ocean_plant",
            () -> new OceanFeature(OceanProbabilityConfig.CODEC));
    public static final RegistryObject<NetherFeature> NETHER_PLANT = FEATURES.register("nether_plant",
            () -> new NetherFeature(NetherConfig.CODEC));
    public static final RegistryObject<LavaFeature> LAVA_PLANT = FEATURES.register("lava_plant",
            () -> new LavaFeature(PlantConfig.CODEC));

}
