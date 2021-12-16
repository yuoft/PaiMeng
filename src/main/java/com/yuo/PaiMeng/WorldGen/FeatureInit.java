package com.yuo.PaiMeng.WorldGen;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class FeatureInit {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, PaiMeng.MOD_ID);

    public static final RegistryObject<AppleTreeFeature> APPLE_TREE = FEATURES.register("apple_tree",
            () -> new AppleTreeFeature(NoFeatureConfig.field_236558_a_));
    public static final RegistryObject<AppleTreeFeature> SUN_APPLE_TREE = FEATURES.register("sun_apple_tree",
            () -> new AppleTreeFeature(NoFeatureConfig.field_236558_a_));
    public static final RegistryObject<AppleTreeFeature> PURPLE_APPLE_TREE = FEATURES.register("purple_apple_tree",
            () -> new AppleTreeFeature(NoFeatureConfig.field_236558_a_));

}
