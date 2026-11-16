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

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        if (stack.getItem() instanceof Relics){ //只能放入对应部位圣遗物
            RelicsType relicsType = RelicsHelper.getTypeForStack(stack);
            return switch (relicsType) {
                case HEAD -> this.getSlotIndex() == 0;
                case CUP -> this.getSlotIndex() == 1;
                case CLOCK -> this.getSlotIndex() == 2;
                case FEATHER -> this.getSlotIndex() == 3;
                case FLOWER -> this.getSlotIndex() == 4;
                default -> throw new IllegalStateException("Unexpected value: " + relicsType);
            };
        }
        return false;
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return 1;
    }
}
