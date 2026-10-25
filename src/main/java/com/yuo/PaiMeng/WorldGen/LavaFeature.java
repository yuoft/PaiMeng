package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * 岩浆边植物：周围一格内有岩浆时，在石头上生成
 */
public class LavaFeature extends Feature<PlantConfig> {
    public LavaFeature(Codec<PlantConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlantConfig> ctx) {
        WorldGenLevel reader = ctx.level();
        BlockPos pos = ctx.origin();
        BlockState blockstate = ctx.config().getState(); //植物

        if (!reader.isEmptyBlock(pos)) return false; //当前位置是空气
        BlockState state = reader.getBlockState(pos.below()); //下方是石头
        boolean lava = isLava(pos, reader);
        if (lava && state.is(Blocks.STONE)) {
            reader.setBlock(pos, blockstate, 3);
            return true;
        }
        return false;
    }

    /**
     * 周围一格内有没有岩浆
     */
    private boolean isLava(BlockPos pos, WorldGenLevel reader) {
        Iterable<BlockPos> allInBox = BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 0, 1));
        for (BlockPos inBox : allInBox) {
            if (reader.getFluidState(inBox).is(FluidTags.LAVA)) return true;
        }
        return false;
    }
}
