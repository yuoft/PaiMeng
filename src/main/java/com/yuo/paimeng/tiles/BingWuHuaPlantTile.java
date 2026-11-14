package com.yuo.paimeng.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BingWuHuaPlantTile extends AbsBingWuHuaTile{
    public BingWuHuaPlantTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.bingwuhuaPlant.get(), pos, state);
    }

    public BingWuHuaPlantTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

}
