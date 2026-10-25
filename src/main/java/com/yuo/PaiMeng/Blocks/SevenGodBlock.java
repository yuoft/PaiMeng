package com.yuo.PaiMeng.Blocks;

import com.yuo.PaiMeng.Tiles.SevenGodTile;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class SevenGodBlock extends BaseEntityBlock{
	private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0,
			14.0, 16, 14.0);
	private static final VoxelShape SHAPE0 = Block.box(2.0, 16.0, 2.0,
			14.0, 32, 14.0);
	private static final VoxelShape SHAPE1 = Block.box(2.0, 32.0, 2.0,
			14.0, 38, 14.0);

	public SevenGodBlock() {
		super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(5, 9999)
				.requiresCorrectToolForDrops().lightLevel(e -> 15).noOcclusion());
	}


	@Override
	public void appendHoverText(ItemStack stack, @Nullable BlockGetter blockReader, List<Component> list, TooltipFlag flag) {
		list.add(Component.translatable("paimeng.text.itemInfo.seven_god"));
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new SevenGodTile(pos, state);
	}

	//1.20.1 没有 ITickableTileEntity，刻由方块侧 getTicker 提供
	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
		return level.isClientSide ? null : (lvl, pos, st, blockEntity) -> {
			if (blockEntity instanceof SevenGodTile godTile) godTile.tick(lvl, pos, st);
		};
	}

	@Override
	public float getShadeBrightness(BlockState state, BlockGetter blockReader, BlockPos pos) {
		return 1.0f;
	}

	@Override
	public RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockReader, BlockPos pos, CollisionContext context) {
		return Shapes.or(SHAPE, SHAPE0, SHAPE1);
	}

	@Override
	public boolean isPathfindable(BlockState p_196266_1_, BlockGetter p_196266_2_, BlockPos p_196266_3_, PathComputationType p_196266_4_) {
		return false;
	}
}
