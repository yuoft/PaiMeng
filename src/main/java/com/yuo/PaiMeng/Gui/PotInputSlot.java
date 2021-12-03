package com.yuo.PaiMeng.Gui;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class PotInputSlot extends Slot {
    public PotInputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return true;
    }

    @Override
    public int getSlotStackLimit() {
        return this.getStack().getMaxStackSize();
    }
}
