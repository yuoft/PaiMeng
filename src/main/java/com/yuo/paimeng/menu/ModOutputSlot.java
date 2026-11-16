package com.yuo.paimeng.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ModOutputSlot extends Slot {
    public ModOutputSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    //能否放入物品
    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    //最大数量
    @Override
    public int getMaxStackSize() {
        return this.getItem().getMaxStackSize();
    }
}
