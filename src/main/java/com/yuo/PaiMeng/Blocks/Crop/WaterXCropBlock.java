package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Items.CropUseBlockEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class WaterXCropBlock extends CropBlock implements LiquidBlockContainer{
    private static final BlockBehaviour.Properties CROP = BlockBehaviour.Properties.of().noCollission().randomTicks().
            instabreak().sound(SoundType.CROP); //作物
    private final CropUseBlockEnum useBlockEnum;

    public WaterXCropBlock(CropUseBlockEnum blockEnum) {
        super(CROP);
        this.useBlockEnum = blockEnum;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!level.isClientSide){
            if (state.getValue(AGE) == 7){ //收获作物
                Block.dropResources(state, level, pos, null, player, player.getItemInHand(handIn)); //生成掉落物
                level.setBlock(pos, state.setValue(AGE, 0), 2);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, level, pos, player, handIn, hit);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return Fluids.WATER.defaultFluidState();
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

    //生长时间更长
    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + Mth.nextInt(level.random, 1, 4);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        level.setBlock(pos, this.getStateForAge(i), 2);
    }

    public CropUseBlockEnum getUseBlockEnum() {
        return useBlockEnum;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos.below());
        return blockState.is(PMBlocks.aquaticFarmland.get());
    }

    //部分不能使用骨粉催熟
    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return getUseBlockEnum() == CropUseBlockEnum.COMMON && !this.isMaxAge(state);
    }

}
