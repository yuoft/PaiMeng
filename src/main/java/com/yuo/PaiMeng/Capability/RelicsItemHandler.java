package com.yuo.PaiMeng.Capability;

import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

public class RelicsItemHandler extends ItemStackHandler implements IItemHandlerModifiable {
    private final int SLOT_SIZE = 6;

    public RelicsItemHandler(){
        this.stacks = NonNullList.withSize(SLOT_SIZE, ItemStack.EMPTY);
    }

    public NonNullList<ItemStack> getStacks(){
        return this.stacks;
    }


}
