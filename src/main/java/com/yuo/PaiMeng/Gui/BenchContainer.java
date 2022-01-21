package com.yuo.PaiMeng.Gui;

import com.yuo.PaiMeng.Recipes.ModRecipeType;
import com.yuo.PaiMeng.Tiles.BenchTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BenchContainer extends Container {
    private final BenchTile potTile; //存储食材
    private final CookingIntArray data;
    private World world;

    public BenchContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory , new BenchTile());
    }

    public BenchContainer(int id, PlayerInventory playerInventory, BenchTile tile) {
        super(ContainerTypeRegistry.benchContainer.get(), id);
        this.potTile = tile;
        this.world = playerInventory.player.world;
        this.data = tile.data;
        trackIntArray(data); //同步数据
        //食材槽
        for (int m = 0; m < 2; m++){
            for (int n = 0; n < 2; n++){
                this.addSlot(new Slot(potTile, n + m * 2, 8 + n * 30, 22 + m * 27));
            }
        }
        //食品槽
        this.addSlot(new ModOutputSlot(potTile, 4, 132, 33));
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

    //燃烧时间
    public int getTime(){
        return (int) Math.ceil(this.data.get(0) / 20d);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.potTile.isUsableByPlayer(playerIn);
    }

    //玩家shift行为
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();
            if (index > 4){
                if (hasRecipe(itemStack1))
                    if (!this.mergeItemStack(itemStack1, 0, 4, false)) return ItemStack.EMPTY;
                if (index >= 5 && index < 32) { //从物品栏到快捷栏
                    if (!this.mergeItemStack(itemStack1, 33, 41, false)) return ItemStack.EMPTY;
                } else if (index >= 32 && index < 41 ) {
                    if (!this.mergeItemStack(itemStack1, 5, 32, false)) return ItemStack.EMPTY;
                }
            }else if (!this.mergeItemStack(itemStack1, 5, 41, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }
    protected boolean hasRecipe(ItemStack stack) {
        return this.world.getRecipeManager().getRecipe(ModRecipeType.SYN_PLAT, new Inventory(stack), this.world).isPresent();
    }

    public boolean canRecipe(){
        return this.data.get(1) != 0;
    }

    public int getFoodLevel(){
        return this.data.get(2);
    }
}
