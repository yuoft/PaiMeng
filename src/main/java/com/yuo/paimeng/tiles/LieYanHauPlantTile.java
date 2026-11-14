package com.yuo.paimeng.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LieYanHauPlantTile extends AbsLieYanHuaTile{
    public LieYanHauPlantTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.lieyanhuaPlant.get(), pos, state);
    }

    public LieYanHauPlantTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }
}
