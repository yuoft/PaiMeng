package com.yuo.paimeng.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LieYanHauCropTile extends AbsLieYanHuaTile{
    public LieYanHauCropTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.lieyanhuaCrop.get(), pos, state);
    }

    public LieYanHauCropTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

}
