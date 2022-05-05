package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsHelper;
import com.yuo.PaiMeng.Items.RelicsType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class RelicsSlot extends SlotItemHandler {

    public RelicsSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean isItemValid(@Nonnull ItemStack stack) {
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

    @Override
    public int getItemStackLimit(@Nonnull ItemStack stack) {
        return 1;
    }
}
