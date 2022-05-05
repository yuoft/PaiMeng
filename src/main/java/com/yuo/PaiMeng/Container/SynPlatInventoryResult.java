package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Tiles.SynPlatTile;
import net.minecraft.inventory.CraftResultInventory;
import net.minecraft.item.ItemStack;

public class SynPlatInventoryResult extends CraftResultInventory {

    private SynPlatTile craftTile;

    public SynPlatInventoryResult(SynPlatTile tile){
        this.craftTile = tile;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return craftTile.getStackInSlot(4);
    }

    @Override
    public ItemStack decrStackSize(int par1, int par2) {
        ItemStack stack = craftTile.getStackInSlot(4);
        if (!stack.isEmpty()) {
            craftTile.setInventorySlotContents(4, ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public void setInventorySlotContents(int par1, ItemStack par2ItemStack) {
        craftTile.setInventorySlotContents(4, par2ItemStack);
    }
}
