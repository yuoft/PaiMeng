package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.gen.feature.ProbabilityConfig;

public class OceanProbabilityConfig extends ProbabilityConfig {
    public static final Codec<OceanProbabilityConfig> CODEC = RecordCodecBuilder.create(
            (builder) -> builder.group(Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter(
                    (config) -> config.probability)).apply(builder, OceanProbabilityConfig::new));

    private final BlockState state;

    public OceanProbabilityConfig(float p){
        super(p);
        this.state = Blocks.AIR.getDefaultState();
    }
    public OceanProbabilityConfig(float probability, BlockState state) {
        super(probability);
        this.state = state;
    }

    public BlockState getState() {
        return state;
    }
}
