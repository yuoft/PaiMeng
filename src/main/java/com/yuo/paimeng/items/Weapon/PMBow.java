package com.yuo.paimeng.items.Weapon;

import com.yuo.paimeng.ModUtils;
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

    @Override
    public Component getName(ItemStack stack) {
        MutableComponent component = Component.translatable(this.getDescriptionId(stack));
        return ModUtils.getTierName(this.tier, component);
    }

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
