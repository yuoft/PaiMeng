package com.yuo.paimeng.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * 水边植物：周围一格内有水时，在泥土/草方块/沙子上生成
 */
public class RiverFeature extends Feature<PlantConfig> {
    public RiverFeature(Codec<PlantConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlantConfig> ctx) {
        WorldGenLevel reader = ctx.level();
        BlockPos pos = ctx.origin();
        BlockState blockstate = ctx.config().getState(); //植物

        if (reader.getFluidState(pos).is(FluidTags.WATER)) return false; //当前位置不能是水
        boolean water = isWater(pos, reader);
        if (water && blockstate.canSurvive(reader, pos)) {
            reader.setBlock(pos, blockstate, 3);
            return true;
        }
        return false;
    }

    /**
     * 周围一格内有没有水
     */
    private boolean isWater(BlockPos pos, WorldGenLevel reader) {
        Iterable<BlockPos> allInBox = BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 0, 1));
        for (BlockPos inBox : allInBox) {
            if (reader.getFluidState(inBox).is(FluidTags.WATER)) return true;
        }
        return false;
    }
}
