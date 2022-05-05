package com.yuo.PaiMeng.Blocks;

import com.yuo.PaiMeng.NetWork.BenchPacket;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Tiles.BenchTile;
import com.yuo.PaiMeng.Tiles.PotTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.BucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;
import java.util.function.ToIntFunction;

public class CookingBench extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING; //朝向
    public static final BooleanProperty FIRE = BlockStateProperties.LIT; //燃烧状态

    public CookingBench() {
        super(Properties.create(Material.IRON).hardnessAndResistance(10, 20).harvestLevel(1)
                .harvestTool(ToolType.PICKAXE).setRequiresTool().setLightLevel(getLightValueLit(13)));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(FIRE, Boolean.FALSE));
    }

    public static ToIntFunction<BlockState> getLightValueLit(int lightValue) {
        return (state) -> state.get(BlockStateProperties.LIT) ? lightValue : 0;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(handIn);
        if (!worldIn.isRemote){
            if (heldItem.isEmpty()){ //空手右键打开gui
                if (!state.get(FIRE)) {
                    player.sendMessage(new TranslationTextComponent("paimeng.message.bench_open"), UUID.randomUUID());
                    return ActionResultType.SUCCESS;
                }
                TileEntity tileEntity = worldIn.getTileEntity(pos);
                if (tileEntity instanceof BenchTile){ //打开gui
                    ((BenchTile) tileEntity).setPlayer(player);
                    player.openContainer((INamedContainerProvider) tileEntity);
                    player.addStat(Stats.INTERACT_WITH_FURNACE);
                }
                return ActionResultType.SUCCESS;
            }
            int burnTime = ForgeHooks.getBurnTime(heldItem);
            if (burnTime > 0 && !(heldItem.getItem() instanceof BucketItem)){ //手持燃料
                TileEntity tileEntity = worldIn.getTileEntity(pos);
                if (!(tileEntity instanceof BenchTile)) return ActionResultType.FAIL;
                BenchTile benchTile = (BenchTile) tileEntity;
                int i = (int) (Math.ceil(burnTime / 1600d * 60d)); //增加时间
                i = Math.max(i * 20, 1);
                int fuleCount = getFuleCount(benchTile, i);
                if (fuleCount > heldItem.getCount()){ //燃料不足
                    benchTile.setBurnTime(heldItem.getCount() * i);
                    benchTile.setFuelItem(heldItem.getItem());
                    NetWorkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                            new BenchPacket(pos, heldItem));
                    if (!player.isCreative()) heldItem.setCount(0);
                }else {
                    benchTile.setBurnTime(fuleCount * i);
                    benchTile.setFuelItem(heldItem.getItem());
                    NetWorkHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player),
                            new BenchPacket(pos, heldItem));
                    if (!player.isCreative()) heldItem.shrink(fuleCount);
                }
                return ActionResultType.SUCCESS;
            }
        }
        else PaiMeng.PROXY.setRefrencedTE(worldIn.getTileEntity(pos)); //保存当前tile坐标
        return ActionResultType.SUCCESS;
    }

    //消耗多个燃料时 所需具体数量
    private int getFuleCount(BenchTile benchTile, int time){
        int tileTIME = benchTile.getTIME(); //当前时间
        return (int) Math.floor((benchTile.MAX_TIME - tileTIME) / (time * 1d));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BenchTile();
    }

    //破坏后掉落
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.matchesBlock(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof PotTile) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (PotTile)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    //设置放下时的状态
    @Override
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
