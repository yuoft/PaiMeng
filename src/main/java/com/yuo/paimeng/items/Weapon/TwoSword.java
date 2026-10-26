package com.yuo.paimeng.items.Weapon;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;

public class TwoSword extends SwordItem {
    public TwoSword(Tier tier) {
        super(tier, 3, -2.8f, new Item.Properties());
    }

    @Override
    public Component getName(ItemStack stack) {
        MutableComponent component = Component.translatable(this.getDescriptionId(stack));
        PMItemTier tier = (PMItemTier) getTier();
        switch (tier) {
            case ONE: return component.withStyle(ChatFormatting.WHITE);
            case TWO: return component.withStyle(ChatFormatting.GREEN);
            case THREE: return component.withStyle(ChatFormatting.BLUE);
            case FOUR: return component.withStyle(ChatFormatting.DARK_PURPLE);
            case FIVE: return component.withStyle(ChatFormatting.GOLD);
            default: return component;
        }
    }
}
