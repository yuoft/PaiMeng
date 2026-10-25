package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Tiles.SynPlatTile;
import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

//1.20.1: CraftingInventory -> TransientCraftingContainer（CraftingContainer 接口的实现）
public class SynPlatInventory extends TransientCraftingContainer {
    private final NonNullList<ItemStack> stackList;
    private final SynPlatTile craftTile;
    private final AbstractContainerMenu container;

    public SynPlatInventory(AbstractContainerMenu containerIn, SynPlatTile tile) {
        super(containerIn, 4, 1);
        this.stackList = NonNullList.withSize(4, ItemStack.EMPTY);
        this.craftTile = tile;
        this.container = containerIn;
    }

    @Override
    public boolean isEmpty() {
        return craftTile.isEmpty();
    }

    //1.20.1: getStackInSlot -> getItem
    @Override
    public ItemStack getItem(int index) {
        return index >= this.getContainerSize() ? ItemStack.EMPTY : craftTile.getItem(index);
    }

    //1.20.1: decrStackSize -> removeItem；hasContainerItem/getContainerItem -> getCraftingRemainingItem
    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack stack = craftTile.getItem(index);
        if (!stack.isEmpty()) {
            ItemStack itemstack;
            if (stack.getCount() <= count) {
                itemstack = stack.copy();
                craftTile.setItem(index, stack.getCraftingRemainingItem());
                container.slotsChanged(this);
                return itemstack;
            } else {
                itemstack = stack.split(count);
                if (stack.getCount() == 0) {
                    craftTile.setItem(index, stack.getCraftingRemainingItem());
                }
                container.slotsChanged(this);
                return itemstack;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    //1.20.1: setInventorySlotContents -> setItem
    @Override
    public void setItem(int slot, ItemStack itemstack) {
        craftTile.setItem(slot, itemstack);
        container.slotsChanged(this);
    }

    //清空方块实体内的合成输入
    @Override
    public void clearContent() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            craftTile.setItem(i, ItemStack.EMPTY);
        }
    }

    //1.20.1: CraftingContainer 需要 getItems()，直接读取方块实体
    @Override
    public List<ItemStack> getItems() {
        List<ItemStack> list = new ArrayList<>();
        for (int i = 0; i < this.getContainerSize(); i++) {
            list.add(craftTile.getItem(i));
        }
        return list;
    }
}
