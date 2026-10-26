package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.Blocks.Crop.WallXCrop;
import com.yuo.PaiMeng.Blocks.PMBlocks;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

//种子
public class CropBlockItem extends ItemNameBlockItem {
    public static final Item.Properties GROUP = new Item.Properties();
    private final CropUseBlockEnum useBlockEnum; //种子种植判断依据

    public CropBlockItem(Block blockIn, CropUseBlockEnum cropUseBlockEnum) {
        super(blockIn, GROUP);
        this.useBlockEnum = cropUseBlockEnum;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        CropUseBlockEnum useBlockEnum = getUseBlockEnum();
        switch (useBlockEnum){
            case COMMON:
                tooltip.add(Component.translatable("paimeng.text.itemInfo.common_seed"));break;
            case FERTILE:
                tooltip.add(Component.translatable("paimeng.text.itemInfo.fertile_seed"));break;
            case AQUATIC:
                tooltip.add(Component.translatable("paimeng.text.itemInfo.aquatic_seed"));break;
            case LOGS:
                tooltip.add(Component.translatable("paimeng.text.itemInfo.logs_seed"));break;
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level world = context.getLevel();
        if (world.isClientSide || context.getPlayer() == null) return InteractionResult.FAIL;
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        BlockState blockGrow = world.getBlockState(pos);//可种植方块
        switch (useBlockEnum) {
            case COMMON:
                if (blockGrow.is(PMBlocks.commonFarmland.get()))
                    return resultType(context);
                else return funb(world, player);
            case FERTILE:
                if (blockGrow.is(PMBlocks.fertileFarmland.get()))
                    return resultType(context);
                else return funb(world, player);
            case AQUATIC:
                if (blockGrow.is(PMBlocks.aquaticFarmland.get()) && world.getBlockState(pos.above()).is(Blocks.WATER))
                    return resultType(context);
                else return funb(world, player);
            case LOGS:
                if (blockGrow.is(BlockTags.LOGS))
                    if (context.getClickedFace() != Direction.UP)
                        return wallResultType(context);
                    else return resultType(context);
                else return funb(world, player);
            default:
                return funb(world, player);
        }
    }

    //原版种植
    private InteractionResult resultType(UseOnContext context){
        InteractionResult actionresulttype = this.place(new BlockPlaceContext(context));
        return !actionresulttype.consumesAction() && this.isEdible()
                ? this.use(context.getLevel(), Objects.requireNonNull(context.getPlayer()), context.getHand()).getResult()
                : actionresulttype;
    }

    /**
     * 侧面作物种植
     * @param context
     * @return
     */
    private InteractionResult wallResultType(UseOnContext context){
        Direction face = context.getClickedFace();
        if (face == Direction.DOWN) return InteractionResult.FAIL;
        Level world = context.getLevel();
        ItemStack stack = context.getItemInHand();
        Item seed = stack.getItem();
        if (seed instanceof CropBlockItem) {
            Block crop = getWallCrop(seed);
            if (crop == Blocks.AIR) return InteractionResult.FAIL;
            BlockState state = crop.defaultBlockState().setValue(WallXCrop.FACING, rotate(face));
            BlockPos blockpos = context.getClickedPos();
            Player playerentity = context.getPlayer();
            world.setBlock(blockpos.relative(face), state, 3);

            state = this.func_219985_a(blockpos, world, stack, state);
            state.getBlock().setPlacedBy(world, blockpos, state, playerentity, stack);
            if (playerentity instanceof ServerPlayer serverPlayer) {
                CriteriaTriggers.PLACED_BLOCK.trigger(serverPlayer, blockpos, stack);
            }

            SoundType soundtype = SoundType.CROP;
            if (context.getPlayer() != null)
                world.playSound(playerentity, blockpos, SoundEvents.CROP_PLANTED, SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            if (playerentity == null || !playerentity.getAbilities().instabuild) {
                stack.shrink(1);
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        return InteractionResult.FAIL;
    }

    /**
     * 反转朝向
     * @param direction 方向
     * @return 反转后方向
     */
    public static Direction rotate(Direction direction) {
        switch(direction) {
            case NORTH:
                return Direction.SOUTH;
            case SOUTH:
                return Direction.NORTH;
            case WEST:
                return Direction.EAST;
            case EAST:
                return Direction.WEST;
            default:
                throw new IllegalStateException("Unable to get rotated facing of " + direction);
        }
    }

    /**
     * 获取种子对应可侧面种植作物方块
     * @param seed 种子
     * @return 作物方块
     */
    private Block getWallCrop(Item seed) {
        if (seed == PMItems.moguSeed.get()){
            return PMBlocks.moguWallCrop.get();
        }else if (seed == PMItems.mufengMoguSeed.get()){
            return PMBlocks.mufengMoguWallCrop.get();
        }else if (seed == PMItems.songrongSeed.get()){
            return PMBlocks.songrongWallCrop.get();
        }else if (seed == PMItems.youdengxunSeed.get()){
            return PMBlocks.youdengxunWallCrop.get();
        }
        return Blocks.AIR;
    }

    private BlockState func_219985_a(BlockPos blockPos, Level world, ItemStack stack, BlockState blockState) {
        BlockState blockstate = blockState;
        CompoundTag compoundnbt = stack.getTag();
        if (compoundnbt != null) {
            CompoundTag compoundnbt1 = compoundnbt.getCompound("BlockStateTag");
            StateDefinition<Block, BlockState> statedefinition = blockState.getBlock().getStateDefinition();

            for(String s : compoundnbt1.getAllKeys()) {
                Property<?> property = statedefinition.getProperty(s);
                if (property != null) {
                    Tag inbt = compoundnbt1.get(s);
                    if (inbt == null) continue;
                    blockstate = func_219988_a(blockstate, property, inbt.getAsString());
                }
            }
        }

        if (blockstate != blockState) {
            world.setBlock(blockPos, blockstate, 2);
        }

        return blockstate;
    }

    private static <T extends Comparable<T>> BlockState func_219988_a(BlockState p_219988_0_, Property<T> p_219988_1_, String p_219988_2_) {
        return p_219988_1_.getValue(p_219988_2_).map((p_219986_2_) -> p_219988_0_.setValue(p_219988_1_, p_219986_2_)).orElse(p_219988_0_);
    }


    //消息
    private InteractionResult funb(Level world, Player playerentity){
        if (!world.isClientSide)
            playerentity.displayClientMessage(Component.translatable("paimeng.message.un_plant"), true);
        return InteractionResult.FAIL;
    }

    public CropUseBlockEnum getUseBlockEnum() {
        return useBlockEnum;
    }
}
