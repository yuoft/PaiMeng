package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Capability.ModCapability;
import com.yuo.PaiMeng.Capability.RelicsItemHandler;
import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class RelicsContainer extends AbstractContainerMenu {
    private final RelicsItemHandler itemHandler; //存储食材
    private final Level world;

    public RelicsContainer(int id, Inventory playerInventory) {
        super(ContainerTypeRegistry.relicsContainer.get(), id);
        Player player = playerInventory.player;
        this.itemHandler = player.getCapability(ModCapability.RELICS_CAPABILITY).orElseThrow(NullPointerException::new);
        this.world = player.level();
        //圣遗物槽
        for (int m = 0; m < 2; ++m){
            for (int n = 0; n < 3; ++n){
                this.addSlot(new RelicsSlot(this.itemHandler, n + m * 3, 8 + m * 28, 8 + n * 23));
            }
        }
        //添加玩家物品栏
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        //添加玩家快捷栏
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    //1.20.1: canInteractWith -> stillValid
    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    //玩家shift行为 1.20.1: transferStackInSlot -> quickMoveStack
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (index > 6){
                if (itemStack1.getItem() instanceof Relics){
                    switch (RelicsHelper.getTypeForStack(itemStack1)){
                        case HEAD:
                            if (!this.moveItemStackTo(itemStack1, 0, 1, false)) return ItemStack.EMPTY;
                            break;
                        case CUP:
                            if (!this.moveItemStackTo(itemStack1, 1, 2, false)) return ItemStack.EMPTY;
                            break;
                        case CLOCK:
                            if (!this.moveItemStackTo(itemStack1, 2, 3, false)) return ItemStack.EMPTY;
                            break;
                        case FEATHER:
                            if (!this.moveItemStackTo(itemStack1, 3, 4, false)) return ItemStack.EMPTY;
                            break;
                        case FLOWER:
                            if (!this.moveItemStackTo(itemStack1, 4, 5, false)) return ItemStack.EMPTY;
                            break;
                    }
                }
                if (index < 34) { //从物品栏到快捷栏
                    if (!this.moveItemStackTo(itemStack1, 35, 43, false)) return ItemStack.EMPTY;
                } else if (index < 43) {
                    if (!this.moveItemStackTo(itemStack1, 7, 34, false)) return ItemStack.EMPTY;
                }
            }else if (!this.moveItemStackTo(itemStack1, 7, 43, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }
}
