package com.yuo.paimeng.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class RelicsExp extends Item {
    private final int exp;

    public RelicsExp(int exp) {
        super(new Properties().stacksTo(16));
        this.exp = exp;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        Item item = stack.getItem();
        if (item == PMItems.relicsExpBig.get()){
            tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_exp_big0"));
            tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_exp_big1"));
        }
        if (item == PMItems.relicsExpSmall.get()){
            tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_exp_small0"));
            tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_exp_small1"));
        }
    }

    public int getExp() {
        return exp;
    }
}
