package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Tiles.StrengthenTableTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class StrengthenTableContainer extends Container {
    private final StrengthenTableTile sTTile; //存储食材
    private final StrengthenIntArray data;
    private World world;

    public StrengthenTableContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory , new StrengthenTableTile());
    }

    public StrengthenTableContainer(int id, PlayerInventory playerInventory, StrengthenTableTile tile) {
        super(ContainerTypeRegistry.strengthenTableContainer.get(), id);
        this.sTTile = tile;
        this.data = tile.data;
        this.world = playerInventory.player.world;
        this.trackIntArray(data);
        //经验槽
        for (int m = 0; m < 4; m++){
            for (int n = 0; n < 4; n++){
                this.addSlot(new StrengthenInputSlot(sTTile, n + m * 4, 8 + n * 18, 7 + m * 18));
            }
        }
        //待强化槽
        this.addSlot(new StrengthenOutPutSlot(sTTile, 16, 121, 35));
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

    /**
     * 按钮是否可用
     * @return 两边都有物品：true
     */
    public boolean hasItem(){
        return data.get(0) == 1 && data.get(1) == 1;
    }

    /**
     * 右边是否有物品
     * @return bool
     */
    public boolean hasRightItem(){
        return data.get(1) == 1;
    }

    public int getLeftExp(){
        return data.get(2);
    }

    public int getRightExp(){
        return data.get(3);
    }

    //提升的等级
    public int getLevelUp(){
        return data.get(4);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.sTTile.isUsableByPlayer(playerIn);
    }

    //玩家shift行为
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();
            if (index > 16){
                if (itemStack1.getItem() instanceof Relics)
                    if (!this.mergeItemStack(itemStack1, 0, 16, false)) return ItemStack.EMPTY;
                if (index < 44) { //从物品栏到快捷栏
                    if (!this.mergeItemStack(itemStack1, 45, 53, false)) return ItemStack.EMPTY;
                } else if (index < 53) {
                    if (!this.mergeItemStack(itemStack1, 17, 44, false)) return ItemStack.EMPTY;
                }
            }else if (!this.mergeItemStack(itemStack1, 17, 53, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }
}
