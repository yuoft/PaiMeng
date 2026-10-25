package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Items.PMFoodItem;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class CookingSlot extends Slot {
    public CookingSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    //1.20.1: isItemValid -> mayPlace
    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof PMFoodItem || stack.getItem() == Items.TOTEM_OF_UNDYING; //食材
    }

    //1.20.1: getItemStackLimit(stack) -> getMaxStackSize()
    @Override
    public int getMaxStackSize() {
        return 64;
    }
}
