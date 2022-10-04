package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.Items.CropUseBlockEnum;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class ModWaterCropBlock extends CropsBlock implements ILiquidContainer{
    private static final Block.Properties CROP = AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().
            zeroHardnessAndResistance().sound(SoundType.CROP); //作物
    private final CropUseBlockEnum useBlockEnum;

    public ModWaterCropBlock(CropUseBlockEnum blockEnum) {
        super(CROP);
        this.useBlockEnum = blockEnum;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote){
            if (state.get(AGE) == 7){ //收获作物
                Block.spawnDrops(state, worldIn, pos, null, player, player.getHeldItem(handIn)); //生成掉落物
                worldIn.setBlockState(pos, state.with(AGE, 0), 2);
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.getStillFluidState(false);
    }

    //可以装液体？
    public boolean canContainFluid(IBlockReader worldIn, BlockPos pos, BlockState state, Fluid fluidIn) {
        return false;
    }
    //接收流体？
    public boolean receiveFluid(IWorld worldIn, BlockPos pos, BlockState state, FluidState fluidStateIn) {
        return false;
    }

    //生长时间更长
    @Override
    public void grow(World worldIn, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + MathHelper.nextInt(RANDOM, 1, 4);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        worldIn.setBlockState(pos, this.withAge(i), 2);
    }

    public CropUseBlockEnum getUseBlockEnum() {
        return useBlockEnum;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        BlockState blockState = worldIn.getBlockState(pos.down());
        return blockState.matchesBlock(BlockRegistry.aquaticFarmland.get());
    }

    //部分不能使用骨粉催熟
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return getUseBlockEnum() == CropUseBlockEnum.COMMON;
    }

}
