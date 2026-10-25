package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsExp;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class StrengthenInputSlot extends Slot {
    public StrengthenInputSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    //1.20.1: isItemValid -> mayPlace
    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof Relics || stack.getItem() instanceof RelicsExp;
    }

    //1.20.1: getItemStackLimit(stack) -> getMaxStackSize(stack)
    @Override
    public int getMaxStackSize(ItemStack stack) {
        return stack.getMaxStackSize();
    }
}
