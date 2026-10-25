package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * 植物生成配置
 * 1.20.1：旧版 NoFeatureConfig 子类改为直接实现 FeatureConfiguration（带 Codec）
 */
public class PlantConfig implements FeatureConfiguration {
    public static final Codec<PlantConfig> CODEC = RecordCodecBuilder.create(
            (builder) -> builder.group(
                    BlockState.CODEC.fieldOf("state").forGetter((config) -> config.state),
                    Codec.INT.fieldOf("tries").orElse(1).forGetter((config) -> config.tries)
            ).apply(builder, PlantConfig::new));

    private final BlockState state;
    private final int tries;

    public PlantConfig(BlockState state) {
        this(state, 1);
    }

    public PlantConfig(BlockState state, int tries) {
        this.state = state;
        this.tries = tries;
    }

    public BlockState getState() {
        return state;
    }

    /**
     * 每个放置点的尝试次数（1.16 BlockClusterFeatureConfig#tryCount 的等价物）
     */
    public int getTries() {
        return tries;
    }
}
