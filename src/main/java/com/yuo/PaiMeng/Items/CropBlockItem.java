package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.Blocks.Crop.WallXCrop;
import com.yuo.PaiMeng.tab.ModGroup;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import org.lwjgl.system.CallbackI;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

//种子
public class CropBlockItem extends BlockNamedItem {
    public static final Item.Properties GROUP = new Item.Properties().group(ModGroup.PaiMengCrop);
    private final CropUseBlockEnum useBlockEnum; //种子种植判断依据

    public CropBlockItem(Block blockIn, CropUseBlockEnum cropUseBlockEnum) {
        super(blockIn, GROUP);
        this.useBlockEnum = cropUseBlockEnum;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CropUseBlockEnum useBlockEnum = getUseBlockEnum();
        switch (useBlockEnum){
            case COMMON:
                tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.common_seed"));break;
            case FERTILE:
                tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.fertile_seed"));break;
            case AQUATIC:
                tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.aquatic_seed"));break;
            case LOGS:
                tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.logs_seed"));break;
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        if (world.isRemote || context.getPlayer() == null) return ActionResultType.FAIL;
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();
        BlockState blockGrow = world.getBlockState(pos);//可种植方块
        switch (useBlockEnum) {
            case COMMON:
                if (blockGrow.matchesBlock(BlockRegistry.commonFarmland.get()))
                    return resultType(context);
                else return funb(world, player);
            case FERTILE:
                if (blockGrow.matchesBlock(BlockRegistry.fertileFarmland.get()))
                    return resultType(context);
                else return funb(world, player);
            case AQUATIC:
                if (blockGrow.matchesBlock(BlockRegistry.aquaticFarmland.get()))
                    return resultType(context);
                else return funb(world, player);
            case LOGS:
                if (blockGrow.isIn(BlockTags.LOGS))
                    if (context.getFace() != Direction.UP)
                        return wallResultType(context);
                    else return resultType(context);
                else return funb(world, player);
            default:
                return funb(world, player);
        }
    }

    //原版种植
    private ActionResultType resultType(ItemUseContext context){
        ActionResultType actionresulttype = this.tryPlace(new BlockItemUseContext(context));
        return !actionresulttype.isSuccessOrConsume() && this.isFood() ? this.onItemRightClick(context.getWorld(), Objects.requireNonNull(context.getPlayer()), context.getHand()).getType() : actionresulttype;
    }

    /**
     * 侧面作物种植
     * @param context
     * @return
     */
    private ActionResultType wallResultType(ItemUseContext context){
        Direction face = context.getFace();
        if (face == Direction.DOWN) return ActionResultType.FAIL;
        World world = context.getWorld();
        ItemStack stack = context.getItem();
        Item seed = stack.getItem();
        if (seed instanceof CropBlockItem) {
            Block crop = getWallCrop(seed);
            if (crop == Blocks.AIR) return ActionResultType.FAIL;
            BlockState state = crop.getDefaultState().with(WallXCrop.FACING, rotate(face));
            BlockPos blockpos = context.getPos();
            PlayerEntity playerentity = context.getPlayer();
            world.setBlockState(blockpos.offset(face), state);

            state = this.func_219985_a(blockpos, world, stack, state);
            this.onBlockPlaced(blockpos, world, playerentity, stack, state);
            state.getBlock().onBlockPlacedBy(world, blockpos, state, playerentity, stack);
            if (playerentity instanceof ServerPlayerEntity) {
                CriteriaTriggers.PLACED_BLOCK.trigger((ServerPlayerEntity) playerentity, blockpos, stack);
            }

            SoundType soundtype = SoundType.CROP;
            if (context.getPlayer() != null)
                world.playSound(playerentity, blockpos, SoundEvents.ITEM_CROP_PLANT, SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
            if (playerentity == null || !playerentity.abilities.isCreativeMode) {
                stack.shrink(1);
            }

            return ActionResultType.func_233537_a_(world.isRemote);
        }
        return ActionResultType.FAIL;
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
        if (seed == ItemRegistry.moguSeed.get()){
            return BlockRegistry.moguWallCrop.get();
        }else if (seed == ItemRegistry.mufengMoguSeed.get()){
            return BlockRegistry.mufengMoguWallCrop.get();
        }else if (seed == ItemRegistry.songrongSeed.get()){
            return BlockRegistry.songrongWallCrop.get();
        }else if (seed == ItemRegistry.youdengxunSeed.get()){
            return BlockRegistry.youdengxunWallCrop.get();
        }
        return Blocks.AIR;
    }

    private BlockState func_219985_a(BlockPos blockPos, World world, ItemStack stack, BlockState blockState) {
        BlockState blockstate = blockState;
        CompoundNBT compoundnbt = stack.getTag();
        if (compoundnbt != null) {
            CompoundNBT compoundnbt1 = compoundnbt.getCompound("BlockStateTag");
            StateContainer<Block, BlockState> statecontainer = blockState.getBlock().getStateContainer();

            for(String s : compoundnbt1.keySet()) {
                Property<?> property = statecontainer.getProperty(s);
                if (property != null) {
                    CompoundNBT inbt = (CompoundNBT) compoundnbt1.get(s);
                    if (inbt == null) continue;
                    String s1 = inbt.getString();
                    blockstate = func_219988_a(blockstate, property, s1);
                }
            }
        }

        if (blockstate != blockState) {
            world.setBlockState(blockPos, blockstate, 2);
        }

        return blockstate;
    }

    private static <T extends Comparable<T>> BlockState func_219988_a(BlockState p_219988_0_, Property<T> p_219988_1_, String p_219988_2_) {
        return p_219988_1_.parseValue(p_219988_2_).map((p_219986_2_) -> p_219988_0_.with(p_219988_1_, p_219986_2_)).orElse(p_219988_0_);
    }


    //消息
    private ActionResultType funb(World world, PlayerEntity playerentity){
        if (!world.isRemote)
            playerentity.sendStatusMessage(new TranslationTextComponent("paimeng.message.un_plant"), true);
        return ActionResultType.FAIL;
    }

    public CropUseBlockEnum getUseBlockEnum() {
        return useBlockEnum;
    }
}
