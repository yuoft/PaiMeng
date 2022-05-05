package com.yuo.PaiMeng.Container;

import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SeedBoxInventoryResult extends CraftResultInventory {

    private final NonNullList<ItemStack> stackResult = NonNullList.withSize(1, ItemStack.EMPTY);

    public SeedBoxInventoryResult(){

    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return stackResult.get(0);
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        ItemStack stack = stackResult.get(0);
        if (!stack.isEmpty()) {
            stackResult.set(0, ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        stackResult.set(0, par2ItemStack);
    }
}
