package com.yuo.paimeng.menu;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ModOutputSlot extends Slot {
    public ModOutputSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    //能否放入物品 1.20.1: isItemValid -> mayPlace
    @Override
    public boolean mayPlace(ItemStack stack) {
        return false;
    }

    //最大数量 1.20.1: getSlotStackLimit -> getMaxStackSize
    @Override
    public int getMaxStackSize() {
        return this.getItem().getMaxStackSize();
    }
}
