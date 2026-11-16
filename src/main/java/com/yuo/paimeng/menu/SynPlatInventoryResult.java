package com.yuo.paimeng.menu;

import com.yuo.paimeng.tiles.SynPlatTile;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;

public class SynPlatInventoryResult extends ResultContainer {

    private final SynPlatTile craftTile;

    public SynPlatInventoryResult(SynPlatTile tile){
        this.craftTile = tile;
    }

    @Override
    public ItemStack getItem(int index) {
        return craftTile.getItem(4);
    }

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

    @Override
    public void setItem(int par1, ItemStack par2ItemStack) {
        craftTile.setItem(4, par2ItemStack);
    }
}
