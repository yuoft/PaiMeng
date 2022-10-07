package com.yuo.PaiMeng.Blocks.Crop;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.yuo.PaiMeng.Items.CropUseBlockEnum;
import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Random;

public class WallXCrop extends XCropBlock{
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    private static final Map<Direction, VoxelShape[]> SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, new VoxelShape[]{
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 4.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 6.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 10.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 12.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 14.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            },
            Direction.SOUTH, new VoxelShape[]{
                    Block.makeCuboidShape( 0.0D, 0.0D, 14.0D,16.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape( 0.0D, 0.0D, 12.0D,16.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape( 0.0D, 0.0D, 10.0D,16.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape( 0.0D, 0.0D, 8.0D ,16.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape( 0.0D, 0.0D, 6.0D ,16.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape( 0.0D, 0.0D, 4.0D ,16.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape( 0.0D, 0.0D, 2.0D ,16.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape( 0.0D, 0.0D, 0.0D ,16.0D, 16.0D, 16.0D),
            },
            Direction.WEST, new VoxelShape[]{
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 4.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 6.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 14.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            },
            Direction.EAST, new VoxelShape[]{
                    Block.makeCuboidShape(16.0D, 0.0D, 0.0D, 14.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(16.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(16.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(16.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(16.0D, 0.0D, 0.0D, 6.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(16.0D, 0.0D, 0.0D, 4.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(16.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D),
                    Block.makeCuboidShape(16.0D, 0.0D, 0.0D, 0.0D, 16.0D, 16.0D),
            }));

    public WallXCrop(CropUseBlockEnum blockEnum, Properties properties) {
        super(blockEnum, properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(AGE, 0));
    }

    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPES.get(state.get(FACING))[getAge(state)];
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    private Direction getFacing(BlockState state){
        return state.get(FACING);
    }

    @Override
    public void grow(World worldIn, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + MathHelper.nextInt(RANDOM, 1, 4);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        worldIn.setBlockState(pos, this.withAge(i, getFacing(state)), 2);
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (worldIn.getLightSubtracted(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getGrowthChance(this, worldIn, pos);
                if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
                    worldIn.setBlockState(pos, this.withAge(i + 1, getFacing(state)), 2);
                    net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
                }
            }
        }

    }

    public BlockState withAge(int age, Direction direction) {
        return this.getDefaultState().with(this.getAgeProperty(), age).with(FACING, direction);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE,FACING);
    }

    @Override
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        return facing.getOpposite() == stateIn.get(FACING) && !stateIn.isValidPosition(worldIn, currentPos) ? Blocks.AIR.getDefaultState() : stateIn;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        Direction direction = state.get(FACING);
        BlockPos blockpos = pos.offset(direction.getOpposite());
        BlockState blockstate = worldIn.getBlockState(blockpos);
        Block block = blockstate.getBlock();
        if (!block.isIn(BlockTags.LOGS)) return false;
        return blockstate.isSolidSide(worldIn, blockpos, direction);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState blockstate = super.getStateForPlacement(context);
        IWorldReader iworldreader = context.getWorld();
        BlockPos blockpos = context.getPos();
        Direction[] adirection = context.getNearestLookingDirections();

        for(Direction direction : adirection) {
            if (direction.getAxis().isHorizontal()) {
                blockstate = blockstate.with(FACING, direction.getOpposite());
                if (blockstate.isValidPosition(iworldreader, blockpos)) {
                    return blockstate;
                }
            }
        }

        return null;
    }
}
