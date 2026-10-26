package com.yuo.paimeng.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * 岩浆边植物：周围一格内有岩浆时，在石头/深板岩上生成
 */
public class LavaFeature extends Feature<PlantConfig> {
    public LavaFeature(Codec<PlantConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlantConfig> ctx) {
        WorldGenLevel reader = ctx.level();
        BlockPos pos = ctx.origin();
        BlockState plantState = ctx.config().getState(); // 植物

        // 🔴 修复 1：交给植物自己的 mayPlaceOn 判断（需确保植物能长在深板岩上）
        if (!plantState.canSurvive(reader, pos)) return false;

        BlockState stateBelow = reader.getBlockState(pos.below());

        // 🔴 修复 2：使用 Tag 判断，包含石头、深板岩等，适应地下深处的地形
        if (!stateBelow.is(BlockTags.BASE_STONE_OVERWORLD)) return false;

        if (isLava(pos, reader)) {
            reader.setBlock(pos, plantState, 3);
            return true;
        }
        return false;
    }

    /**
     * 检查周围是否有岩浆
     */
    private boolean isLava(BlockPos pos, WorldGenLevel reader) {
        Iterable<BlockPos> allInBox = BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 0, 1));
        for (BlockPos inBox : allInBox) {
            if (reader.getFluidState(inBox).is(FluidTags.LAVA)) return true;
        }
        return false;
    }
}