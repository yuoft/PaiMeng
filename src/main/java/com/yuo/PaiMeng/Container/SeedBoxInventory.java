package com.yuo.PaiMeng.Container;

import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SeedBoxInventory extends CraftingInventory {
    private final NonNullList<ItemStack> stackList;
    private Container container;

    public SeedBoxInventory(Container containerIn) {
        super(containerIn, 1, 1);
        this.stackList = NonNullList.withSize(1, ItemStack.EMPTY);
        this.container = containerIn;
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = stackList.get(index);
        if (!stack.isEmpty()) {
            ItemStack itemstack;
            if (stack.getCount() <= count) {
                itemstack = stack.copy();
                stackList.set(index, ItemStack.EMPTY);
                container.onCraftMatrixChanged(this);
                return itemstack;
            } else {
                itemstack = stack.split(count);
                if (stack.getCount() == 0) {
                    stackList.set(index, ItemStack.EMPTY);
                }
                container.onCraftMatrixChanged(this);
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

}
