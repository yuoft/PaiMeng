package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Tiles.SynPlatTile;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class SynPlatInventory extends CraftingInventory {
    private final NonNullList<ItemStack> stackList;
    private SynPlatTile craftTile;
    private Container container;

    public SynPlatInventory(Container containerIn, SynPlatTile tile) {
        super(containerIn, 4, 1);
        this.stackList = NonNullList.withSize(4, ItemStack.EMPTY);
        this.craftTile = tile;
        this.container = containerIn;
    }

    @Override
    public boolean isEmpty() {
        return craftTile.isEmpty();
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return index >= this.getSizeInventory() ? ItemStack.EMPTY : craftTile.getStackInSlot(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        ItemStack stack = craftTile.getStackInSlot(index);
        if (!stack.isEmpty()) {
            ItemStack itemstack;
            if (stack.getCount() <= count) {
                itemstack = stack.copy();
                if (stack.hasContainerItem()){
                    craftTile.setInventorySlotContents(index, stack.getContainerItem());
                }else craftTile.setInventorySlotContents(index, ItemStack.EMPTY);
                container.onCraftMatrixChanged(this);
                return itemstack;
            } else {
                itemstack = stack.split(count);
                if (stack.getCount() == 0) {
                    if (stack.hasContainerItem()){
                        craftTile.setInventorySlotContents(index, stack.getContainerItem());
                    }else craftTile.setInventorySlotContents(index, ItemStack.EMPTY);
                }
                container.onCraftMatrixChanged(this);
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int slot, ItemStack itemstack) {
        craftTile.setInventorySlotContents(slot, itemstack);
        container.onCraftMatrixChanged(this);
    }
}
