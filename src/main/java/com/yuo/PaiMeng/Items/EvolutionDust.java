package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.tab.PMGroup;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

//嬗变之尘
public class EvolutionDust extends Item {

    public EvolutionDust() {
        super(new Properties().group(PMGroup.PaiMengOther).maxStackSize(16));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (stack.getItem() == PMItems.shanbianzhichen.get()){
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.shanbianzhichen1"));
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.shanbianzhichen2"));
        }
        if (stack.getItem() == PMItems.yimengrongmei.get()){
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.yimengrongemi"));
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        BlockPos pos = context.getPos();
        World world = context.getWorld();
        BlockState state = world.getBlockState(pos);
        ItemStack stack = context.getItem();
        if (state.getBlock() == Blocks.FARMLAND && world.isAirBlock(pos.up())){ //是耕地 上方是空气
            world.setBlockState(pos, PMBlocks.commonFarmland.get().getDefaultState(), 2);
            stack.shrink(1);
            return ActionResultType.SUCCESS;
        }
        if (state.getBlock() == Blocks.FARMLAND && world.hasWater(pos.up())){ //上方是水
            world.setBlockState(pos, PMBlocks.aquaticFarmland.get().getDefaultState(), 2);
            stack.shrink(2);
            return ActionResultType.SUCCESS;
        }
        if (state.getBlock() == PMBlocks.commonFarmland.get()){ //是玄此玉田
            world.setBlockState(pos, PMBlocks.fertileFarmland.get().getDefaultState(), 2);
            stack.shrink(2);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
