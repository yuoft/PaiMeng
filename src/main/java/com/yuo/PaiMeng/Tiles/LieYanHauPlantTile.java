package com.yuo.PaiMeng.Tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LieYanHauPlantTile extends AbsLieYanHuaTile{
    //1.20.1 BlockEntityType.Builder.of(LieYanHauPlantTile::new, block) 使用的构造
    public LieYanHauPlantTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.lieyanhuaPlant.get(), pos, state);
    }

    public LieYanHauPlantTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //旧版无参构造
    public LieYanHauPlantTile() {
        this(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
    }
}
