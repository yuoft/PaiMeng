package com.yuo.PaiMeng.Container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class ModOutputSlot extends Slot {
    public ModOutputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    //能否放入物品
    @Override
    public boolean isItemValid(ItemStack stack) {
        return false;
    }

    //最大数量
    @Override
    public int getSlotStackLimit() {
        return this.getStack().getMaxStackSize();
    }
}
