package com.yuo.paimeng.worldgen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

/**
 * 下界植物生成配置
 * 1.20.1：旧版 NoFeatureConfig 子类改为直接实现 FeatureConfiguration（带 Codec）
 */
public class NetherConfig implements FeatureConfiguration {
    public static final Codec<NetherConfig> CODEC = RecordCodecBuilder.create(
            (builder) -> builder.group(
                    BlockState.CODEC.fieldOf("state").forGetter((config) -> config.state),
                    BlockState.CODEC.fieldOf("down").forGetter((config) -> config.stateDown)
            ).apply(builder, NetherConfig::new));

    private final BlockState state;
    private final BlockState stateDown;

    public NetherConfig(BlockState state, BlockState down) {
        this.state = state;
        this.stateDown = down;
    }

    public BlockState getState() {
        return state;
    }

    public BlockState getStateDown() {
        return stateDown;
    }
}
