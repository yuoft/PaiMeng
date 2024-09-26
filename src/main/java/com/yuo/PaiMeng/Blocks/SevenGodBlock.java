package com.yuo.PaiMeng.Blocks;

import com.yuo.PaiMeng.Tiles.SevenGodTile;
import net.minecraft.block.*;
import net.minecraft.block.AbstractBlock.Properties;
import net.minecraft.block.material.Material;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.List;

public class SevenGodBlock extends Block{
	private static final VoxelShape SHAPE = Block.makeCuboidShape(2.0, 0.0, 2.0,
			14.0, 16, 14.0);
	private static final VoxelShape SHAPE0 = Block.makeCuboidShape(2.0, 16.0, 2.0,
			14.0, 32, 14.0);
	private static final VoxelShape SHAPE1 = Block.makeCuboidShape(2.0, 32.0, 2.0,
			14.0, 38, 14.0);

	public SevenGodBlock() {
		super(Properties.create(Material.ROCK).harvestLevel(1).harvestTool(ToolType.PICKAXE)
				.hardnessAndResistance(5, 9999).setRequiresTool().setLightLevel(e -> 15).notSolid());
	}


	@Override
	public void addInformation(ItemStack stack, @Nullable IBlockReader blockReader, List<ITextComponent> list, ITooltipFlag flag) {
		list.add(new TranslationTextComponent("paimeng.text.itemInfo.seven_god"));
	}

	@Override
	public boolean hasTileEntity(BlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world) {
		return new SevenGodTile();
	}

	@Override
	public float getAmbientOcclusionLightValue(BlockState state, IBlockReader blockReader, BlockPos pos) {
		return 1.0f;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader blockReader, BlockPos pos, ISelectionContext context) {
		return VoxelShapes.or(SHAPE, SHAPE0, SHAPE1);
	}

	@Override
	public boolean allowsMovement(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
		return false;
	}
}
