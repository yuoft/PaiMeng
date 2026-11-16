package com.yuo.paimeng.menu;

import com.yuo.paimeng.items.Relics;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class StrengthenOutPutSlot extends Slot {
    public StrengthenOutPutSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof Relics;
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return 1;
    }
}
