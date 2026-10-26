package com.yuo.paimeng.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

// 1.20.1：FarmlandBlock → FarmBlock；Properties.from → Properties.copy
public class CommonFarmland extends FarmBlock {

    public CommonFarmland() {
        super(BlockBehaviour.Properties.copy(Blocks.FARMLAND));
    }

    @Override
    public void fallOn(Level world, BlockState state, BlockPos pos, Entity entity, float i) {
        super.fallOn(world, state, pos, entity, i);
    }
}
