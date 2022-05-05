package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsExp;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class StrengthenInputSlot extends Slot {
    public StrengthenInputSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof Relics || stack.getItem() instanceof RelicsExp;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return stack.getMaxStackSize();
    }
}
