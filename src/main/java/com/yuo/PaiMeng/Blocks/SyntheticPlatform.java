package com.yuo.PaiMeng.Blocks;

import com.yuo.PaiMeng.Container.SynPlatContainer;
import com.yuo.PaiMeng.Tiles.SynPlatTile;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.ContainerBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;

public class SyntheticPlatform extends ContainerBlock {
    private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent("gui.paimeng.synthetic_platform");

    public SyntheticPlatform() {
        super(Properties.create(Material.ANVIL).hardnessAndResistance(15, 15).harvestLevel(1)
                .harvestTool(ToolType.PICKAXE).setRequiresTool().notSolid().setLightLevel(e -> 5));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote){
            TileEntity tileEntity = worldIn.getTileEntity(pos);
            if (tileEntity instanceof SynPlatTile){ //打开gui
                player.openContainer(state.getContainer(worldIn, pos));
                player.addStat(Stats.INTERACT_WITH_CRAFTING_TABLE);
                return ActionResultType.CONSUME;
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public INamedContainerProvider getContainer(BlockState state, World worldIn, BlockPos pos) {
        return new SimpleNamedContainerProvider((id, inventory, player) -> {
            return new SynPlatContainer(id, inventory, (SynPlatTile) worldIn.getTileEntity(pos));
        }, CONTAINER_NAME);
    }

    //破坏后掉落
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.matchesBlock(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof SynPlatTile) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (SynPlatTile)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SynPlatTile();
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return makeShape();
    }

    //灶台碰撞箱
    private VoxelShape makeShape(){
        VoxelShape shape = Block.makeCuboidShape(0, 0, 0, 16, 2, 16);
        VoxelShape shape1 = Block.makeCuboidShape(2, 2, 2, 14, 16, 14);
        return VoxelShapes.or(shape, shape1);
    }

    @Nullable
    @Override
    public TileEntity createNewTileEntity(IBlockReader worldIn) {
        return new SynPlatTile();
    }
}
