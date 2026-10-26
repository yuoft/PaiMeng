package com.yuo.paimeng.datagen;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * 生成 data/paimeng/worldgen/configured_feature/*.json
 */
public final class PMConfiguredFeatures {

    private PMConfiguredFeatures() {
    }

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        for (PMWorldGenData.CfgDef def : PMWorldGenData.configured()) {
            register(context, def);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static void register(BootstapContext<ConfiguredFeature<?, ?>> context, PMWorldGenData.CfgDef def) {
        FeatureUtils.register(context, (net.minecraft.resources.ResourceKey) def.key(),
                (net.minecraft.world.level.levelgen.feature.Feature) def.feature(), def.config());
    }
}
