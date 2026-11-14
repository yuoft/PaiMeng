package com.yuo.paimeng.blocks;

import com.yuo.paimeng.tiles.BenchTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.network.NetworkHooks;

import javax.annotation.Nullable;
import java.util.function.ToIntFunction;

public class CookingBench extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING; //朝向
    public static final BooleanProperty FIRE = BlockStateProperties.LIT; //燃烧状态

    public CookingBench() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(10, 20)
                .requiresCorrectToolForDrops().sound(SoundType.METAL).lightLevel(getLightValueLit(13)));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FIRE, Boolean.FALSE));
    }

    public static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return  (state) -> state.hasProperty(BlockStateProperties.LIT) && state.getValue(BlockStateProperties.LIT) ? lightValue : 0;
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(handIn);

        if (worldIn.isClientSide) {
            return InteractionResult.SUCCESS;
        }

        // 以下全部是服务端逻辑
        if (heldItem.isEmpty()){ // 空手右键
            if (!state.getValue(FIRE)) {
                player.sendSystemMessage(Component.translatable("paimeng.message.bench_open"));
                return InteractionResult.CONSUME;
            }
            BlockEntity tileEntity = worldIn.getBlockEntity(pos);
            if (tileEntity instanceof BenchTile benchTile) {
                benchTile.setPlayer(player);
                NetworkHooks.openScreen((ServerPlayer) player, benchTile, pos);
                player.awardStat(Stats.INTERACT_WITH_FURNACE);
            }
            return InteractionResult.SUCCESS;
        }else {
            int burnTime = ForgeHooks.getBurnTime(heldItem, null);
            if (burnTime > 0 && !(heldItem.getItem() instanceof BucketItem)){ // 手持燃料
                BlockEntity tileEntity = worldIn.getBlockEntity(pos);
                if (!(tileEntity instanceof BenchTile benchTile)) return InteractionResult.FAIL;

                int i = (int) (Math.ceil(burnTime / 1600d * 60d)); //增加时间
                i = Math.max(i * 20, 1);
                int fuleCount = getFuleCount(benchTile, i);

                if (fuleCount > heldItem.getCount()){ //燃料不足
                    benchTile.setBurnTime(heldItem.getCount() * i);
                    benchTile.setFuelItem(heldItem);
                    if (!player.isCreative()) heldItem.setCount(0);
                }else {
                    benchTile.setBurnTime(fuleCount * i);
                    benchTile.setFuelItem(heldItem);
                    if (!player.isCreative()) heldItem.shrink(fuleCount);
                }

                // 播放一点声音反馈
                worldIn.playSound(null, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, worldIn.getRandom().nextFloat() * 0.4F + 0.8F);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }

    //消耗多个燃料时 所需具体数量
    private int getFuleCount(BenchTile benchTile, int time){
        int tileTIME = benchTile.getTIME(); //当前时间
        return (int) Math.floor((benchTile.MAX_TIME - tileTIME) / (time * 1d));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BenchTile(pos, state);
    }

    //1.20.1 没有 ITickableTileEntity，刻由方块侧 getTicker 提供
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof BenchTile benchTile) benchTile.tickInternal(lvl, pos, st);
        };
    }

    //破坏后掉落（1.16 原代码这里误写成 PotTile 导致灶台内容物不掉落，此处按意图改为 BenchTile）
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof BenchTile benchTile) {
                BenchTile.dropContents(worldIn, pos, benchTile);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    //设置放下时的状态
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(FIRE, Boolean.FALSE);
    }

    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if (stateIn.getValue(FIRE)) {
            if (rand.nextInt(10) == 0) {
                worldIn.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.6F, false);
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
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FIRE);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        return switch (direction) {
            case DOWN, UP, NORTH -> makeShapeNorth();
            case SOUTH -> makeShapeSouth();
            case WEST -> makeShapeWest();
            case EAST -> makeShapeEast();
        };
    }

    //灶台碰撞箱
    private VoxelShape makeShapeNorth(){
        VoxelShape shape = Block.box(1, 0, 0, 5, 14, 15);
        VoxelShape shape1 = Block.box(11, 0, 0, 15, 14, 15);
        VoxelShape shape2 = Block.box(5, 0, 0, 11, 1, 13);
        VoxelShape shape3 = Block.box(5, 1, 2, 11, 2, 13);
        VoxelShape shape4 = Block.box(5, 0, 13, 11, 14, 15);
        VoxelShape shape5 = Block.box(5, 4, 0, 11, 5, 15);
        VoxelShape shape6 = Block.box(5, 11, 0, 11, 14, 4);
        VoxelShape shape7 = Block.box(4, 10, 4, 12, 16, 12);
        return Shapes.or(shape, shape1, shape2, shape3, shape4, shape5, shape6, shape7);
    }

    private VoxelShape makeShapeSouth(){
        VoxelShape shape = Block.box(11, 0, 1, 15, 14, 16);
        VoxelShape shape1 = Block.box(1, 0, 1, 5, 14, 16);
        VoxelShape shape2 = Block.box(3, 0, 3, 11, 1, 16);
        VoxelShape shape3 = Block.box(3, 1, 3, 11, 2, 14);
        VoxelShape shape4 = Block.box(5, 0, 1, 11, 14, 3);
        VoxelShape shape5 = Block.box(3, 4, 3, 11, 5, 16);
        VoxelShape shape6 = Block.box(5, 11, 12, 11, 14, 16);
        VoxelShape shape7 = Block.box(4, 10, 4, 12, 16, 12);
        return Shapes.or(shape, shape1, shape2, shape3, shape4, shape5, shape6, shape7);
    }
    private VoxelShape makeShapeWest(){
        VoxelShape shape = Block.box(0, 0, 11, 15, 14, 15);
        VoxelShape shape1 = Block.box(0, 0, 1, 15, 14, 5);
        VoxelShape shape2 = Block.box(0, 0, 5, 13, 1, 11);
        VoxelShape shape3 = Block.box(2, 1, 5, 13, 2, 11);
        VoxelShape shape4 = Block.box(13, 0, 5, 15, 14, 11);
        VoxelShape shape5 = Block.box(0, 4, 5, 15, 5, 11);
        VoxelShape shape6 = Block.box(0, 11, 5, 4, 14, 11);
        VoxelShape shape7 = Block.box(4, 10, 4, 12, 16, 12);
        return Shapes.or(shape, shape1, shape2, shape3, shape4, shape5, shape6, shape7);
    }
    private VoxelShape makeShapeEast(){
        VoxelShape shape = Block.box(1, 0, 1, 16, 14, 5);
        VoxelShape shape1 = Block.box(1, 0, 11, 16, 14, 15);
        VoxelShape shape2 = Block.box(3, 0, 5, 16, 1, 11);
        VoxelShape shape3 = Block.box(3, 1, 5, 14, 2, 11);
        VoxelShape shape4 = Block.box(1, 0, 5, 3, 14, 11);
        VoxelShape shape5 = Block.box(3, 4, 5, 16, 5, 11);
        VoxelShape shape6 = Block.box(12, 11, 5, 16, 14, 11);
        VoxelShape shape7 = Block.box(4, 10, 4, 12, 16, 12);
        return Shapes.or(shape, shape1, shape2, shape3, shape4, shape5, shape6, shape7);
    }

}
