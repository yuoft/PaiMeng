package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Items.PMFoodItem;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class CookingSlot extends Slot {
    public CookingSlot(IInventory inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(ItemStack stack) {
        return stack.getItem() instanceof PMFoodItem || stack.getItem() == Items.TOTEM_OF_UNDYING; //食材
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return 64;
    }
}
