package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Items.Relics;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

public class StrengthenOutPutSlot extends Slot {
    public StrengthenOutPutSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof Relics;
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 1;
    }
}
