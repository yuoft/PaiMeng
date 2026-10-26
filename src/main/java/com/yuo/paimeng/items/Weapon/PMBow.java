package com.yuo.paimeng.items.Weapon;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PMBow extends BowItem {
    private final int tier;
    public PMBow(int tierIn) {
        super(new Item.Properties());
        this.tier = tierIn;
    }

    //1.16.5 的 getDisplayName 在 1.20.1 中叫 getName
    @Override
    public Component getName(ItemStack stack) {
        MutableComponent component = Component.translatable(this.getDescriptionId(stack));
        switch (tier) {
            case 1: return component.withStyle(ChatFormatting.WHITE);
            case 2: return component.withStyle(ChatFormatting.GREEN);
            case 3: return component.withStyle(ChatFormatting.BLUE);
            case 4: return component.withStyle(ChatFormatting.DARK_PURPLE);
            case 5: return component.withStyle(ChatFormatting.GOLD);
            default: return component;
        }
    }

    //1.20.1 的 BowItem 仍然保留 customArrow(AbstractArrow)；AbstractArrowEntity#setDamage 改名为 setBaseDamage
    @Override
    public AbstractArrow customArrow(AbstractArrow arrow) {
        switch (tier){
            case 1: arrow.setBaseDamage(2.5d);break;
            case 2: arrow.setBaseDamage(3.5d);break;
            case 3: arrow.setBaseDamage(5.5d);break;
            case 4: arrow.setBaseDamage(8d);break;
            case 5: arrow.setBaseDamage(12d);break;
            default: break;
        }
        return super.customArrow(arrow);
    }
}
