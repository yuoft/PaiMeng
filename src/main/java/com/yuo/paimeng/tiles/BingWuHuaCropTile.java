package com.yuo.paimeng.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BingWuHuaCropTile extends AbsBingWuHuaTile{
    //1.20.1 BlockEntityType.Builder.of(BingWuHuaCropTile::new, block) 使用的构造
    public BingWuHuaCropTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.bingwuhuaCrop.get(), pos, state);
    }

    public BingWuHuaCropTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //旧版无参构造
    public BingWuHuaCropTile() {
        this(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
    }
}
