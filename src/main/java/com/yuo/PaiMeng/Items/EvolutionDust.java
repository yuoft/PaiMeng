package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;
import java.util.List;

//嬗变之尘
public class EvolutionDust extends Item {

    public EvolutionDust() {
        super(new Properties().stacksTo(16));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (stack.getItem() == PMItems.shanbianzhichen.get()){
            tooltip.add(Component.translatable("paimeng.text.itemInfo.shanbianzhichen1"));
            tooltip.add(Component.translatable("paimeng.text.itemInfo.shanbianzhichen2"));
        }
        if (stack.getItem() == PMItems.yimengrongmei.get()){
            tooltip.add(Component.translatable("paimeng.text.itemInfo.yimengrongemi"));
        }
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        BlockPos pos = context.getClickedPos();
        Level world = context.getLevel();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = context.getItemInHand();
        if (state.is(Blocks.FARMLAND) && world.isEmptyBlock(pos.above())){ //是耕地 上方是空气
            world.setBlock(pos, PMBlocks.commonFarmland.get().defaultBlockState(), 2);
            stack.shrink(1);
            return InteractionResult.SUCCESS;
        }
        if (state.is(Blocks.FARMLAND) && hasWater(world, pos.above())){ //上方是水
            world.setBlock(pos, PMBlocks.aquaticFarmland.get().defaultBlockState(), 2);
            stack.shrink(2);
            return InteractionResult.SUCCESS;
        }
        if (state.is(PMBlocks.commonFarmland.get())){ //是玄此玉田
            world.setBlock(pos, PMBlocks.fertileFarmland.get().defaultBlockState(), 2);
            stack.shrink(2);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    //1.16.5 的 IWorld#hasWater 在 1.20.1 已被移除，这里直接用流体状态判断
    private static boolean hasWater(Level world, BlockPos pos) {
        FluidState fluidState = world.getFluidState(pos);
        return fluidState.getType().isSame(Fluids.WATER) || fluidState.getType().isSame(Fluids.FLOWING_WATER);
    }
}
