package com.yuo.paimeng.menu;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;

//1.20.1: CraftResultInventory -> ResultContainer
public class SeedBoxInventoryResult extends ResultContainer {

    private final NonNullList<ItemStack> stackResult = NonNullList.withSize(1, ItemStack.EMPTY);

    public SeedBoxInventoryResult(){

    }

    //1.20.1: getStackInSlot -> getItem
    @Override
    public ItemStack getItem(int index) {
        return stackResult.get(0);
    }

    //1.20.1: decrStackSize -> removeItem
    @Override
    public ItemStack removeItem(int par1, int par2) {
        ItemStack stack = stackResult.get(0);
        if (!stack.isEmpty()) {
            stackResult.set(0, ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    //1.20.1: setInventorySlotContents -> setItem
    @Override
    public void setItem(int par1, ItemStack par2ItemStack) {
        stackResult.set(0, par2ItemStack);
    }
}
