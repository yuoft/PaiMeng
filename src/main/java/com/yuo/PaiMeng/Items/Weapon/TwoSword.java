package com.yuo.PaiMeng.Items.Weapon;

import com.yuo.PaiMeng.tab.PMGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class TwoSword extends SwordItem {
    public TwoSword(IItemTier tier) {
        super(tier,3,-2.8f, new Properties().group(PMGroup.PaiMengWeapon));
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        TranslationTextComponent component = new TranslationTextComponent(getTranslationKey(stack));
        PMItemTier tier = (PMItemTier) getTier();
        switch (tier) {
            case ONE: return component.mergeStyle(TextFormatting.WHITE);
            case TWO: return component.mergeStyle(TextFormatting.GREEN);
            case THREE: return component.mergeStyle(TextFormatting.BLUE);
            case FOUR: return component.mergeStyle(TextFormatting.DARK_PURPLE);
            case FIVE: return component.mergeStyle(TextFormatting.GOLD);
            default: return component;
        }
    }
}
