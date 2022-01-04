package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.world.gen.feature.NoFeatureConfig;

public class NetherConfig extends NoFeatureConfig {
    public static final Codec<NetherConfig> CODEC = RecordCodecBuilder.create((builder) -> {
        return builder.group(BlockState.CODEC.fieldOf("state").forGetter((config) -> {
            return config.state;
        }), BlockState.CODEC.fieldOf("down").forGetter( e -> e.stateDown)).apply(builder, NetherConfig::new);
    });

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
