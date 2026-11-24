package com.yuo.paimeng.blocks;

import net.minecraft.advancements.CriteriaTriggers;
import com.yuo.paimeng.tiles.SevenGodTile;
import com.yuo.paimeng.world.data.SevenGodSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.List;

public class SevenGodBlock extends BaseEntityBlock {
    private static final VoxelShape SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16, 14.0);
    private static final VoxelShape SHAPE0 = Block.box(2.0, 16.0, 2.0, 14.0, 32, 14.0);
    private static final VoxelShape SHAPE1 = Block.box(2.0, 32.0, 2.0, 14.0, 38, 14.0);

    public SevenGodBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(5, 9999)
				.requiresCorrectToolForDrops().lightLevel(e -> 15).noOcclusion());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (!level.isClientSide && placer instanceof Player player && level instanceof ServerLevel serverLevel) {
            BlockEntity te = level.getBlockEntity(pos);
            if (te instanceof SevenGodTile tile) {
                tile.setOwner(player.getUUID());
                // 注册到全局存档
                SevenGodSavedData.get(serverLevel).addShrine(player.getUUID(), new SevenGodSavedData.ShrineData(pos, level.dimension()));
            }
        }
    }

    // 🔴 放置前检查：每个玩家只能放置一个神像
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Player player = context.getPlayer();
        if (player instanceof ServerPlayer serverPlayer) {
            SevenGodSavedData data = SevenGodSavedData.get(serverPlayer.serverLevel());
            // 如果该玩家已经拥有了至少一个神像数据，则阻止放置
            if (!data.getPlayerShrines(player.getUUID()).isEmpty()) {
                player.sendSystemMessage(Component.translatable("paimeng.message.shrine.already_exists"));
                return null;
            }
        }
        return this.defaultBlockState();
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock()) && level instanceof ServerLevel serverLevel) {
            BlockEntity te = level.getBlockEntity(pos);
            if (te instanceof SevenGodTile tile && tile.getOwnerUUID() != null) {
                // 从全局存档注销
                SevenGodSavedData.get(serverLevel).removeShrine(tile.getOwnerUUID(), pos, level.dimension());
            }
        }
        super.onRemove(state, level, pos, newState, isMoving);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        if (!level.isClientSide && level instanceof ServerLevel serverLevel) {
            BlockEntity te = level.getBlockEntity(pos);
            if (te instanceof SevenGodTile tile && tile.getOwnerUUID() != null) {
                if (player.getUUID().equals(tile.getOwnerUUID())) {
                    ItemStack heldItem = player.getItemInHand(hand);
                    SevenGodSavedData data = SevenGodSavedData.get(serverLevel);

                    if (!heldItem.isEmpty() && data.donateItem(player.getUUID(), pos, level.dimension(), heldItem, player.isCrouching())) {
                        player.sendSystemMessage(Component.translatable("paimeng.message.shrine.donate_success"));
                        // 进度：只要不失去你的崇高，整个世界都为你敞开（原版 item_used_on_block）
                        if (player instanceof ServerPlayer serverPlayer) {
                            CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger(serverPlayer, pos, heldItem);
                        }
                        return InteractionResult.SUCCESS;
                    } else if (!heldItem.isEmpty()) {
                        player.sendSystemMessage(Component.translatable("paimeng.message.shrine.donate_fail"));
                        return InteractionResult.SUCCESS;
                    } else {
                        player.sendSystemMessage(Component.translatable("paimeng.message.shrine.status_title"));

                        SevenGodSavedData.MATERIAL_EFFECTS.forEach((k, v) -> {
                            int materialCount = data.getMaterialCount(player.getUUID(), pos, level.dimension(), k);
                            if (materialCount > 0) {
                                int maxLevel = SevenGodSavedData.getMaxLevel(v);
                                int currentLevel = Math.min(materialCount / 64, maxLevel); // 计算当前等级

                                // 传入参数：物品名, Buff名, 当前等级, 投入数量, 满级标识(如果是满级则显示 "[已满级]")
                                player.sendSystemMessage(Component.translatable("paimeng.message.shrine.status_effect",
                                        k.getDescription(), v.getDisplayName(), currentLevel, materialCount,
                                        currentLevel >= maxLevel ? Component.translatable("paimeng.message.shrine.max_level") : Component.empty()
                                ));
                            }
                        });

                        player.sendSystemMessage(Component.translatable("paimeng.message.shrine.status_footer"));
                        return InteractionResult.SUCCESS;
                    }
                } else {
                    player.sendSystemMessage(Component.translatable("paimeng.message.shrine.not_owner"));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.SUCCESS;
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