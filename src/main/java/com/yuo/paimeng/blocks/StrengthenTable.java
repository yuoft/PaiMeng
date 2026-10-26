package com.yuo.paimeng.blocks;

import com.yuo.paimeng.PaiMeng;
import com.yuo.paimeng.tiles.StrengthenTableTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

public class StrengthenTable extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final VoxelShape BASE_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D);
    public static final VoxelShape POST_SHAPE = Block.box(4.0D, 2.0D, 4.0D, 12.0D, 14.0D, 12.0D);
    public static final VoxelShape COMMON_SHAPE = Shapes.or(BASE_SHAPE, POST_SHAPE);
    public static final VoxelShape TOP_PLATE_SHAPE = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 15.0D, 16.0D);
    public static final VoxelShape COLLISION_SHAPE = Shapes.or(COMMON_SHAPE, TOP_PLATE_SHAPE);
    // 1.20.1 的 Block.box 会对 min>max 抛异常（1.16 的 makeCuboidShape 不会），这里按 W/N 镜像修正了 E/S 的坐标
    public static final VoxelShape WEST_SHAPE = Shapes.or(Block.box(1.0D, 10.0D, 0.0D, 5.333333D, 14.0D, 16.0D), Block.box(5.333333D, 12.0D, 0.0D, 9.666667D, 16.0D, 16.0D), Block.box(9.666667D, 14.0D, 0.0D, 14.0D, 18.0D, 16.0D), COMMON_SHAPE);
    public static final VoxelShape NORTH_SHAPE = Shapes.or(Block.box(0.0D, 10.0D, 1.0D, 16.0D, 14.0D, 5.333333D), Block.box(0.0D, 12.0D, 5.333333D, 16.0D, 16.0D, 9.666667D), Block.box(0.0D, 14.0D, 9.666667D, 16.0D, 18.0D, 14.0D), COMMON_SHAPE);
    public static final VoxelShape EAST_SHAPE = Shapes.or(Block.box(10.666667D, 10.0D, 0.0D, 15.0D, 14.0D, 16.0D), Block.box(6.333333D, 12.0D, 0.0D, 10.666667D, 16.0D, 16.0D), Block.box(2.0D, 14.0D, 0.0D, 6.333333D, 18.0D, 16.0D), COMMON_SHAPE);
    public static final VoxelShape SOUTH_SHAPE = Shapes.or(Block.box(0.0D, 10.0D, 10.666667D, 16.0D, 14.0D, 15.0D), Block.box(0.0D, 12.0D, 6.333333D, 16.0D, 16.0D, 10.666667D), Block.box(0.0D, 14.0D, 2.0D, 16.0D, 18.0D, 6.333333D), COMMON_SHAPE);

    public StrengthenTable() {
        super(BlockBehaviour.Properties.copy(Blocks.LECTERN));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));

    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new StrengthenTableTile(pos, state);
    }

    //1.20.1 没有 ITickableTileEntity，刻由方块侧 getTicker 提供
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof StrengthenTableTile tableTile) tableTile.tick(lvl, pos, st);
        };
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide){
            BlockEntity tileEntity = worldIn.getBlockEntity(pos);
            if (tileEntity instanceof StrengthenTableTile){
                MenuProvider provider = state.getMenuProvider(worldIn, pos);
                if (provider != null) {
                    player.openMenu(provider);
                    player.awardStat(Stats.INTERACT_WITH_FURNACE);
                }
            }
        }else PaiMeng.PROXY.setRefrencedTE(worldIn.getBlockEntity(pos));
        return InteractionResult.SUCCESS;
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return COLLISION_SHAPE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        switch(state.getValue(FACING)) {
            case NORTH:
                return NORTH_SHAPE;
            case SOUTH:
                return SOUTH_SHAPE;
            case EAST:
                return EAST_SHAPE;
            case WEST:
                return WEST_SHAPE;
            default:
                return COMMON_SHAPE;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
