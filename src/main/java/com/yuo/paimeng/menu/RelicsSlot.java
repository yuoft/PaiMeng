package com.yuo.paimeng.menu;

import com.yuo.paimeng.items.Relics;
import com.yuo.paimeng.items.RelicsHelper;
import com.yuo.paimeng.items.RelicsType;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class RelicsSlot extends SlotItemHandler {

    public RelicsSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    //1.20.1: isItemValid -> mayPlace
    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        if (stack.getItem() instanceof Relics){ //只能放入对应部位圣遗物
            RelicsType relicsType = RelicsHelper.getTypeForStack(stack);
            switch (relicsType){
                case HEAD:
                    return this.getSlotIndex() == 0;
                case CUP:
                    return this.getSlotIndex() == 1;
                case CLOCK:
                    return this.getSlotIndex() == 2;
                case FEATHER:
                    return this.getSlotIndex() == 3;
                case FLOWER:
                    return this.getSlotIndex() == 4;
                default:
                    throw new IllegalStateException("Unexpected value: " + relicsType);
            }
        }
        return false;
    }

    //1.20.1: getItemStackLimit(stack) -> getMaxStackSize(stack)
    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return 1;
    }
}
