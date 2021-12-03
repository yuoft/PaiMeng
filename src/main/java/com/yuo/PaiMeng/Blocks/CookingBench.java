package com.yuo.PaiMeng.Blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class CookingBench extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING; //朝向
    public static final BooleanProperty FIRE = BlockStateProperties.LIT; //燃烧状态

    public CookingBench() {
        super(Properties.create(Material.IRON).hardnessAndResistance(10, 20).harvestLevel(1)
                .harvestTool(ToolType.PICKAXE).setRequiresTool());
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(FIRE, Boolean.FALSE));
    }

    //设置放下时的状态
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(FIRE, Boolean.FALSE);
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(FIRE)) {
            if (rand.nextInt(10) == 0) {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.6F, false);
            }
            if (rand.nextInt(5) == 0) {
                worldIn.addParticle(ParticleTypes.LAVA, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.3125D, (double)pos.getZ() + 0.5D, (double)(rand.nextFloat() / 2.0F), 5.0E-5D, (double)(rand.nextFloat() / 2.0F));
            }
            if (rand.nextInt(5) == 0){
                worldIn.addParticle(ParticleTypes.CAMPFIRE_COSY_SMOKE, (double)pos.getX() + 0.5D, (double)pos.getY() + 1.0D, (double)pos.getZ() + 0.5D, 0, 0.1d, 0);
            }
        }
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, FIRE);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(FACING);
        VoxelShape shape = VoxelShapes.empty();
        switch (direction){
            case DOWN:
            case UP:
            case NORTH:
                shape = makeShapeNorth();break;
            case SOUTH:
                shape = makeShapeSouth();break;
            case WEST:
                shape = makeShapeWest();break;
            case EAST:
                shape = makeShapeEast();break;
        }
        return shape;
    }

    //灶台碰撞箱
    private VoxelShape makeShapeNorth(){
        VoxelShape shape = Block.makeCuboidShape(1, 0, 0, 5, 14, 15);
        VoxelShape shape1 = Block.makeCuboidShape(11, 0, 0, 15, 14, 15);
        VoxelShape shape2 = Block.makeCuboidShape(5, 0, 0, 11, 1, 13);
        VoxelShape shape3 = Block.makeCuboidShape(5, 1, 2, 11, 2, 13);
        VoxelShape shape4 = Block.makeCuboidShape(5, 0, 13, 11, 14, 15);
        VoxelShape shape5 = Block.makeCuboidShape(5, 4, 0, 11, 5, 15);
        VoxelShape shape6 = Block.makeCuboidShape(5, 11, 0, 11, 14, 4);
        VoxelShape shape7 = Block.makeCuboidShape(4, 10, 4, 12, 16, 12);
        return VoxelShapes.or(shape, shape1, shape2, shape3, shape4, shape5, shape6, shape7);
    }

    private VoxelShape makeShapeSouth(){
        VoxelShape shape = Block.makeCuboidShape(11, 0, 1, 15, 14, 16);
        VoxelShape shape1 = Block.makeCuboidShape(1, 0, 1, 5, 14, 16);
        VoxelShape shape2 = Block.makeCuboidShape(3, 0, 3, 11, 1, 16);
        VoxelShape shape3 = Block.makeCuboidShape(3, 1, 3, 11, 2, 14);
        VoxelShape shape4 = Block.makeCuboidShape(5, 0, 1, 11, 14, 3);
        VoxelShape shape5 = Block.makeCuboidShape(3, 4, 3, 11, 5, 16);
        VoxelShape shape6 = Block.makeCuboidShape(5, 11, 12, 11, 14, 16);
        VoxelShape shape7 = Block.makeCuboidShape(4, 10, 4, 12, 16, 12);
        return VoxelShapes.or(shape, shape1, shape2, shape3, shape4, shape5, shape6, shape7);
    }
    private VoxelShape makeShapeWest(){
        VoxelShape shape = Block.makeCuboidShape(0, 0, 11, 15, 14, 15);
        VoxelShape shape1 = Block.makeCuboidShape(0, 0, 1, 15, 14, 5);
        VoxelShape shape2 = Block.makeCuboidShape(0, 0, 5, 13, 1, 11);
        VoxelShape shape3 = Block.makeCuboidShape(2, 1, 5, 13, 2, 11);
        VoxelShape shape4 = Block.makeCuboidShape(13, 0, 5, 15, 14, 11);
        VoxelShape shape5 = Block.makeCuboidShape(0, 4, 5, 15, 5, 11);
        VoxelShape shape6 = Block.makeCuboidShape(0, 11, 5, 4, 14, 11);
        VoxelShape shape7 = Block.makeCuboidShape(4, 10, 4, 12, 16, 12);
        return VoxelShapes.or(shape, shape1, shape2, shape3, shape4, shape5, shape6, shape7);
    }
    private VoxelShape makeShapeEast(){
        VoxelShape shape = Block.makeCuboidShape(1, 0, 1, 16, 14, 5);
        VoxelShape shape1 = Block.makeCuboidShape(1, 0, 11, 16, 14, 15);
        VoxelShape shape2 = Block.makeCuboidShape(3, 0, 5, 16, 1, 11);
        VoxelShape shape3 = Block.makeCuboidShape(3, 1, 5, 14, 2, 11);
        VoxelShape shape4 = Block.makeCuboidShape(1, 0, 5, 3, 14, 11);
        VoxelShape shape5 = Block.makeCuboidShape(3, 4, 5, 16, 5, 11);
        VoxelShape shape6 = Block.makeCuboidShape(12, 11, 5, 16, 14, 11);
        VoxelShape shape7 = Block.makeCuboidShape(4, 10, 4, 12, 16, 12);
        return VoxelShapes.or(shape, shape1, shape2, shape3, shape4, shape5, shape6, shape7);
    }

}
