package com.yuo.PaiMeng.Tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BingWuHuaPlantTile extends AbsBingWuHuaTile{
    //1.20.1 BlockEntityType.Builder.of(BingWuHuaPlantTile::new, block) 使用的构造
    public BingWuHuaPlantTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.bingwuhuaPlant.get(), pos, state);
    }

    public BingWuHuaPlantTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //旧版无参构造
    public BingWuHuaPlantTile() {
        this(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
    }
}
