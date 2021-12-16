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

public class OrdinaryItem extends Item {
    public OrdinaryItem() {
        super(new Properties().group(ModGroup.PaiMengMaterial));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        Item item = stack.getItem();
        if (item == ItemRegistry.danshoujian.get() || item == ItemRegistry.shuangshoujian.get() || item == ItemRegistry.changqiang.get()
                || item == ItemRegistry.gong.get() || item == ItemRegistry.faqi.get()){
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.star4"));
        }
    }
}
