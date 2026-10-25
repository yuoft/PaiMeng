package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * 海洋植物生成配置（概率 + 方块）
 * 1.20.1：旧版 ProbabilityConfig 已删除，改为直接实现 FeatureConfiguration
 */
public class OceanProbabilityConfig implements FeatureConfiguration {
    public static final Codec<OceanProbabilityConfig> CODEC = RecordCodecBuilder.create(
            (builder) -> builder.group(
                    Codec.floatRange(0.0F, 1.0F).fieldOf("probability").forGetter((config) -> config.probability),
                    BlockState.CODEC.fieldOf("state").forGetter((config) -> config.state)
            ).apply(builder, OceanProbabilityConfig::new));

    public final float probability;
    private final BlockState state;

    public OceanProbabilityConfig(float probability) {
        //1.16 的单参数构造使用 AIR 作为默认方块，这里保持等价
        this(probability, Blocks.AIR.defaultBlockState());
    }

    public OceanProbabilityConfig(float probability, BlockState state) {
        this.probability = probability;
        this.state = state;
    }

    public BlockState getState() {
        return state;
    }

    public float getProbability() {
        return probability;
    }
}
