package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class PlantConfig extends NoFeatureConfig {
    public static final Codec<PlantConfig> CODEC = RecordCodecBuilder.create(
            (builder) -> builder.group(BlockState.CODEC.fieldOf("state").forGetter(
                    (config) -> config.state)).apply(builder, PlantConfig::new));

    private final BlockState state;

    public PlantConfig(BlockState state) {
        this.state = state;
    }

    public BlockState getState() {
        return state;
    }
}
