package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.tab.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class RelicsExp extends Item {
    private final int exp;

    public RelicsExp(int exp) {
        super(new Properties().group(ModGroup.PaiMengRelics).maxStackSize(16));
        this.exp = exp;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Item item = stack.getItem();
        if (item == ItemRegistry.relicsExpBig.get()){
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_exp_big0"));
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_exp_big1"));
        }
        if (item == ItemRegistry.relicsExpSmall.get()){
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_exp_small0"));
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_exp_small1"));
        }
    }

    public int getExp() {
        return exp;
    }
}
