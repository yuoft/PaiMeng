package com.yuo.paimeng.worldgen;

import com.mojang.serialization.Codec;
import com.yuo.paimeng.blocks.PMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * 海洋植物：水中、下面是沙子/沙砾时按概率生成；海草靠海面，海灵芝在深水
 */
public class OceanFeature extends Feature<OceanProbabilityConfig> {
    public OceanFeature(Codec<OceanProbabilityConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<OceanProbabilityConfig> ctx) {
        WorldGenLevel reader = ctx.level();
        BlockPos pos = ctx.origin();
        OceanProbabilityConfig config = ctx.config();

        int k = reader.getHeight(Heightmap.Types.OCEAN_FLOOR, pos.getX(), pos.getZ()); //水下第一个非液体方块高度
        if (reader.getBlockState(pos).is(Blocks.WATER)) { //当前位置有水
            boolean flag = ctx.random().nextDouble() < (double) config.getProbability();
            BlockState blockstate = config.getState();
            BlockState state = reader.getBlockState(pos.below());
            if ((state.is(Blocks.SAND) || state.is(Blocks.GRAVEL)) && flag) { //下面是沙子或沙砾
                if (blockstate.is(PMBlocks.haicaoPlant.get()) && k + 6 >= 63) { //海草 距海平面小于6格
                    reader.setBlock(pos, blockstate, 3);
                    return true;
                }
                if (blockstate.is(PMBlocks.hailingzhiPlant.get()) && k + 15 <= 63) { //海灵芝 大于15格
                    reader.setBlock(pos, blockstate, 3);
                    return true;
                }
            }
        }
        return false;
    }
}
