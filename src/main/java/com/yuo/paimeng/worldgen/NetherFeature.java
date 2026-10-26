package com.yuo.paimeng.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * 下界植物：下方是指定方块、上方是空气时生成
 */
public class NetherFeature extends Feature<NetherConfig> {
    public NetherFeature(Codec<NetherConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NetherConfig> ctx) {
        WorldGenLevel reader = ctx.level();
        BlockPos pos = ctx.origin();
        NetherConfig config = ctx.config();

        BlockState blockState = reader.getBlockState(pos.below());
        if (blockState.is(config.getStateDown().getBlock()) && reader.isEmptyBlock(pos.above())) { //下方是指定方块 上方空气
            reader.setBlock(pos, config.getState(), 3);
            return true;
        }
        return false;
    }
}
