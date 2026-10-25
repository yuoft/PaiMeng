package com.yuo.PaiMeng.Container;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;

//1.20.1: CraftingInventory -> TransientCraftingContainer（CraftingContainer 接口的实现）
public class SeedBoxInventory extends TransientCraftingContainer {
    private final NonNullList<ItemStack> stackList;
    private final AbstractContainerMenu container;

    public SeedBoxInventory(AbstractContainerMenu containerIn) {
        super(containerIn, 1, 1);
        this.stackList = NonNullList.withSize(1, ItemStack.EMPTY);
        this.container = containerIn;
    }

    //1.20.1: decrStackSize -> removeItem
    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = stackList.get(index);
        if (!stack.isEmpty()) {
            ItemStack itemstack;
            if (stack.getCount() <= count) {
                itemstack = stack.copy();
                stackList.set(index, ItemStack.EMPTY);
                container.slotsChanged(this);
                return itemstack;
            } else {
                itemstack = stack.split(count);
                if (stack.getCount() == 0) {
                    stackList.set(index, ItemStack.EMPTY);
                }
                container.slotsChanged(this);
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

}
