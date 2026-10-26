package com.yuo.paimeng.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

/**
 * 生成 data/paimeng/worldgen/placed_feature/*.json
 */
public final class PMPlacedFeatures {

    private PMPlacedFeatures() {
    }

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        for (PMWorldGenData.PfDef def : PMWorldGenData.placed()) {
            Holder<ConfiguredFeature<?, ?>> configured =
                    context.lookup(Registries.CONFIGURED_FEATURE).getOrThrow(PMWorldGenData.cfgKey(def.configuredId()));
            PlacementUtils.register(context, def.key(), configured, def.modifiers());
        }
    }
}
