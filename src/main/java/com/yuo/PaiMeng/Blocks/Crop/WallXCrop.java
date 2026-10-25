package com.yuo.PaiMeng.Blocks.Crop;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.yuo.PaiMeng.Items.CropUseBlockEnum;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;
import java.util.Map;

public class WallXCrop extends XCropBlock{
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final Map<Direction, VoxelShape[]> SHAPES = Maps.newEnumMap(ImmutableMap.of(
            Direction.NORTH, new VoxelShape[]{
                    Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 2.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 4.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 6.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 8.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 10.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 12.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 14.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            },
            Direction.SOUTH, new VoxelShape[]{
                    Block.box( 0.0D, 0.0D, 14.0D,16.0D, 16.0D, 16.0D),
                    Block.box( 0.0D, 0.0D, 12.0D,16.0D, 16.0D, 16.0D),
                    Block.box( 0.0D, 0.0D, 10.0D,16.0D, 16.0D, 16.0D),
                    Block.box( 0.0D, 0.0D, 8.0D ,16.0D, 16.0D, 16.0D),
                    Block.box( 0.0D, 0.0D, 6.0D ,16.0D, 16.0D, 16.0D),
                    Block.box( 0.0D, 0.0D, 4.0D ,16.0D, 16.0D, 16.0D),
                    Block.box( 0.0D, 0.0D, 2.0D ,16.0D, 16.0D, 16.0D),
                    Block.box( 0.0D, 0.0D, 0.0D ,16.0D, 16.0D, 16.0D),
            },
            Direction.WEST, new VoxelShape[]{
                    Block.box(0.0D, 0.0D, 0.0D, 2.0D, 16.0D, 16.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 4.0D, 16.0D, 16.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 6.0D, 16.0D, 16.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 8.0D, 16.0D, 16.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 12.0D, 16.0D, 16.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 14.0D, 16.0D, 16.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            },
            Direction.EAST, new VoxelShape[]{
                    Block.box(14.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                    Block.box(12.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                    Block.box(10.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                    Block.box(8.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                    Block.box(6.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                    Block.box(4.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                    Block.box(2.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
                    Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
            }));

    public WallXCrop(CropUseBlockEnum blockEnum, Properties properties) {
        super(blockEnum, properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(AGE, 0));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES.get(state.getValue(FACING))[getAge(state)];
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return state.setValue(FACING, rot.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.getRotation(state.getValue(FACING)));
    }

    private Direction getFacing(BlockState state){
        return state.getValue(FACING);
    }

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + Mth.nextInt(level.random, 1, 4);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        level.setBlock(pos, this.withAge(i, getFacing(state)), 2);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        if (level.getRawBrightness(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                float f = getGrowthSpeed(this, level, pos);
                if (ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
                    level.setBlock(pos, this.withAge(i + 1, getFacing(state)), 2);
                    ForgeHooks.onCropsGrowPost(level, pos, state);
                }
            }
        }

    }

    public BlockState withAge(int age, Direction direction) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), age).setValue(FACING, direction);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE,FACING);
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor levelIn, BlockPos currentPos, BlockPos facingPos) {
        return facing.getOpposite() == stateIn.getValue(FACING) && !stateIn.canSurvive(levelIn, currentPos) ? Blocks.AIR.defaultBlockState() : stateIn;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction direction = state.getValue(FACING);
        BlockPos blockpos = pos.relative(direction.getOpposite());
        BlockState blockstate = level.getBlockState(blockpos);
        Block block = blockstate.getBlock();
        if (!blockstate.is(BlockTags.LOGS)) return false;
        return blockstate.isFaceSturdy(level, blockpos, direction);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = super.getStateForPlacement(context);
        LevelReader levelReader = context.getLevel();
        BlockPos blockpos = context.getClickedPos();
        Direction[] adirection = context.getNearestLookingDirections();

        for(Direction direction : adirection) {
            if (direction.getAxis().isHorizontal()) {
                blockstate = blockstate.setValue(FACING, direction.getOpposite());
                if (blockstate.canSurvive(levelReader, blockpos)) {
                    return blockstate;
                }
            }
        }

        return null;
    }
}
