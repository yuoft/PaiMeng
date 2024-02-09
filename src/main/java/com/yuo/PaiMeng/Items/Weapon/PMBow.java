package com.yuo.PaiMeng.Items.Weapon;

import com.yuo.PaiMeng.tab.PMGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.item.BowItem;
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

public class PMBow extends BowItem {
    private final int tier;
    public PMBow(int tierIn) {
        super(new Properties().group(PMGroup.PaiMengWeapon));
        this.tier = tierIn;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        TranslationTextComponent component = new TranslationTextComponent(getTranslationKey(stack));
        switch (tier) {
            case 1: return component.mergeStyle(TextFormatting.WHITE);
            case 2: return component.mergeStyle(TextFormatting.GREEN);
            case 3: return component.mergeStyle(TextFormatting.BLUE);
            case 4: return component.mergeStyle(TextFormatting.DARK_PURPLE);
            case 5: return component.mergeStyle(TextFormatting.GOLD);
            default: return component;
        }
    }

    @Override
    public AbstractArrowEntity customArrow(AbstractArrowEntity arrow) {
        switch (tier){
            case 1: arrow.setDamage(2.5d);break;
            case 2: arrow.setDamage(3.5d);break;
            case 3: arrow.setDamage(5.5d);break;
            case 4: arrow.setDamage(8d);break;
            case 5: arrow.setDamage(12d);break;
            default: break;
        }
        return super.customArrow(arrow);
    }
}
