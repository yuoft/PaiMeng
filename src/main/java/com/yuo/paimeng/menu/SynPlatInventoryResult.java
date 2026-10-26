package com.yuo.paimeng.menu;

import com.yuo.paimeng.tiles.SynPlatTile;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;

//1.20.1: CraftResultInventory -> ResultContainer
public class SynPlatInventoryResult extends ResultContainer {

    private final SynPlatTile craftTile;

    public SynPlatInventoryResult(SynPlatTile tile){
        this.craftTile = tile;
    }

    //1.20.1: getStackInSlot -> getItem
    @Override
    public ItemStack getItem(int index) {
        return craftTile.getItem(4);
    }

    //1.20.1: decrStackSize -> removeItem
    @Override
    public ItemStack removeItem(int par1, int par2) {
        ItemStack stack = craftTile.getItem(4);
        if (!stack.isEmpty()) {
            craftTile.setItem(4, ItemStack.EMPTY);
            return stack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    //1.20.1: setInventorySlotContents -> setItem
    @Override
    public void setItem(int par1, ItemStack par2ItemStack) {
        craftTile.setItem(4, par2ItemStack);
    }
}
