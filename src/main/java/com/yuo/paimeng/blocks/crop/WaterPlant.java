package com.yuo.paimeng.blocks.crop;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

public class WaterPlant extends BushBlock implements LiquidBlockContainer {
    public WaterPlant(Properties properties) {
        super(properties);
    }

    //可存在什么方块上面？（1.16.5 的 isValidGround）
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.SAND) || state.is(Blocks.GRAVEL);
    }

    //可以装液体？
    @Override
    public boolean canPlaceLiquid(BlockGetter level, BlockPos pos, BlockState state, Fluid fluidIn) {
        return false;
    }

    //接收流体？
    @Override
    public boolean placeLiquid(LevelAccessor level, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return false;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.defaultFluidState();
    }
}
