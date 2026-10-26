package com.yuo.paimeng.menu;

import com.yuo.paimeng.items.Relics;
import com.yuo.paimeng.tiles.StrengthenTableTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

public class StrengthenTableContainer extends AbstractContainerMenu {
    private final StrengthenTableTile sTTile; //存储食材
    private final StrengthenIntArray data;
    private final Level world;

    public StrengthenTableContainer(int id, Inventory playerInventory){
        //1.20.1 的方块实体没有无参构造，这里创建一个只用于客户端的空容器
        this(id, playerInventory , new StrengthenTableTile(BlockPos.ZERO, Blocks.AIR.defaultBlockState()));
    }

    public StrengthenTableContainer(int id, Inventory playerInventory, StrengthenTableTile tile) {
        super(ContainerTypeRegistry.strengthenTableContainer.get(), id);
        this.sTTile = tile;
        this.data = tile.data;
        this.world = playerInventory.player.level();
        this.addDataSlots(data); //1.20.1: trackIntArray -> addDataSlots
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

    //1.20.1: canInteractWith -> stillValid
    @Override
    public boolean stillValid(Player playerIn) {
        return this.sTTile.stillValid(playerIn);
    }

    //玩家shift行为 1.20.1: transferStackInSlot -> quickMoveStack
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (index > 16){
                if (itemStack1.getItem() instanceof Relics)
                    if (!this.moveItemStackTo(itemStack1, 0, 16, false)) return ItemStack.EMPTY;
                if (index < 44) { //从物品栏到快捷栏
                    if (!this.moveItemStackTo(itemStack1, 45, 53, false)) return ItemStack.EMPTY;
                } else if (index < 53) {
                    if (!this.moveItemStackTo(itemStack1, 17, 44, false)) return ItemStack.EMPTY;
                }
            }else if (!this.moveItemStackTo(itemStack1, 17, 53, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }
}
