package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Capability.ModCapability;
import com.yuo.PaiMeng.Capability.RelicsItemHandler;
import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class RelicsContainer extends Container {
    private final RelicsItemHandler itemHandler; //存储食材
    private final World world;

    public RelicsContainer(int id, PlayerInventory playerInventory) {
        super(ContainerTypeRegistry.relicsContainer.get(), id);
        PlayerEntity player = playerInventory.player;
        this.itemHandler = player.getCapability(ModCapability.RELICS_CAPABILITY).orElseThrow(NullPointerException::new);
        this.world = player.world;
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

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return true;
    }

    //放入物品
    @Override
    public void putStackInSlot(int slotID, ItemStack stack) {
        super.putStackInSlot(slotID, stack);
    }



    //玩家shift行为
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();
            if (index > 6){
                if (itemStack1.getItem() instanceof Relics){
                    switch (RelicsHelper.getTypeForStack(itemStack1)){
                        case HEAD:
                            if (!this.mergeItemStack(itemStack1, 0, 1, false)) return ItemStack.EMPTY;
                            break;
                        case CUP:
                            if (!this.mergeItemStack(itemStack1, 1, 2, false)) return ItemStack.EMPTY;
                            break;
                        case CLOCK:
                            if (!this.mergeItemStack(itemStack1, 2, 3, false)) return ItemStack.EMPTY;
                            break;
                        case FEATHER:
                            if (!this.mergeItemStack(itemStack1, 3, 4, false)) return ItemStack.EMPTY;
                            break;
                        case FLOWER:
                            if (!this.mergeItemStack(itemStack1, 4, 5, false)) return ItemStack.EMPTY;
                            break;
                    }
                }
                if (index < 34) { //从物品栏到快捷栏
                    if (!this.mergeItemStack(itemStack1, 35, 43, false)) return ItemStack.EMPTY;
                } else if (index < 43) {
                    if (!this.mergeItemStack(itemStack1, 7, 34, false)) return ItemStack.EMPTY;
                }
            }else if (!this.mergeItemStack(itemStack1, 7, 43, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }
}
