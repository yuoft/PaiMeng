package com.yuo.PaiMeng.Blocks;

import com.yuo.PaiMeng.Container.SynPlatContainer;
import com.yuo.PaiMeng.Tiles.SynPlatTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;

// 1.20.1：ContainerBlock 已删除，改用 BaseEntityBlock
public class SyntheticPlatform extends BaseEntityBlock {
    // GUI 标题（保留常量名；1.20.1 由方块实体作为 MenuProvider 提供标题）
    private static final Component CONTAINER_NAME = Component.translatable("gui.paimeng.synthetic_platform");

    public SyntheticPlatform() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(15, 15)
                .requiresCorrectToolForDrops().noOcclusion().sound(SoundType.ANVIL).lightLevel(e -> 5));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!worldIn.isClientSide){
            BlockEntity tileEntity = worldIn.getBlockEntity(pos);
            if (tileEntity instanceof SynPlatTile){ //打开gui
                MenuProvider provider = state.getMenuProvider(worldIn, pos);
                if (provider != null) {
                    player.openMenu(provider);
                    player.awardStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                }
                return InteractionResult.CONSUME;
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.SUCCESS;
    }

    //1.16 的 getContainer(...) → 1.20.1 的 getMenuProvider(...)
    @Nullable
    @Override
    public MenuProvider getMenuProvider(BlockState state, Level worldIn, BlockPos pos) {
        BlockEntity tileEntity = worldIn.getBlockEntity(pos);
        if (!(tileEntity instanceof SynPlatTile synPlatTile)) return null;
        return new SimpleMenuProvider((id, inventory, player) ->
                new SynPlatContainer(id, inventory, synPlatTile), CONTAINER_NAME);
    }

    //破坏后掉落
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof SynPlatTile synPlatTile) {
                Containers.dropContents(worldIn, pos, synPlatTile);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SynPlatTile(pos, state);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return makeShape();
    }

    //灶台碰撞箱
    private VoxelShape makeShape(){
        VoxelShape shape = Block.box(0, 0, 0, 16, 2, 16);
        VoxelShape shape1 = Block.box(2, 2, 2, 14, 16, 14);
        return Shapes.or(shape, shape1);
    }

    /**
     * 供其它代码读取的工作台 GUI 名称
     */
    public static Component getContainerName() {
        return CONTAINER_NAME;
    }
}
