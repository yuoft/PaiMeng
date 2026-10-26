package com.yuo.paimeng.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class LieYanHauCropTile extends AbsLieYanHuaTile{
    //1.20.1 BlockEntityType.Builder.of(LieYanHauCropTile::new, block) 使用的构造
    public LieYanHauCropTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.lieyanhuaCrop.get(), pos, state);
    }

    public LieYanHauCropTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //旧版无参构造
    public LieYanHauCropTile() {
        this(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
    }
}
