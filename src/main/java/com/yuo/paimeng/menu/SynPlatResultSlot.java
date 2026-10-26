package com.yuo.paimeng.menu;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class SynPlatResultSlot extends Slot {
    public SynPlatResultSlot(Container inventoryIn, int index, int xPosition, int yPosition) {
        super(inventoryIn, index, xPosition, yPosition);
    }

    //1.20.1: onTake 返回 void（1.16.5 返回 ItemStack）
    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
        super.onTake(thePlayer, stack);
    }
}
