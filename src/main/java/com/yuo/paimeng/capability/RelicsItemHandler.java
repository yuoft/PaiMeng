package com.yuo.paimeng.capability;

import net.minecraft.world.item.ItemStack;
import net.minecraft.core.NonNullList;
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
