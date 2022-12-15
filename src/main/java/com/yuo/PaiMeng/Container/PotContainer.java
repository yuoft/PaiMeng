package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Items.PMFoodItem;
import com.yuo.PaiMeng.NetWork.CookingPacket;
import com.yuo.PaiMeng.Recipes.ModRecipeType;
import com.yuo.PaiMeng.Tiles.PotTile;
import com.yuo.PaiMeng.Tiles.TileUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class PotContainer extends Container {
    private final PotTile potTile; //存储食材
    private final CookingIntArray data;
    private final FoodRecipesIntArray fooData;
    private World world;

    public PotContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory , new PotTile());
    }

    public PotContainer(int id, PlayerInventory playerInventory, PotTile tile) {
        super(ContainerTypeRegistry.potContainer.get(), id);
        this.potTile = tile;
        this.fooData = tile.foodData;
        this.world = playerInventory.player.world;
        this.data = tile.data;
        trackIntArray(data); //同步数据
        trackIntArray(fooData);
        //食材槽
        for (int m = 0; m < 2; m++){
            for (int n = 0; n < 2; n++){
                this.addSlot(new CookingSlot(potTile, n + m * 2, 8 + n * 30, 22 + m * 27));
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

    public int getBurnTime(){
        return (int) Math.ceil((this.data.get(0) / (20 * 60 * 8.0)) * 14.0);
    }

    public int getTime(){ return (int) Math.ceil(this.data.get(0) / 20.0);}

    /**
     * 获取批量制作数量
     * @return 数量
     */
    public int getItemMaxCount(){
        NonNullList<ItemStack> inputs = TileUtils.getRecipeInputs(world, ModRecipeType.POT, this.potTile);
        NonNullList<ItemStack> items = this.potTile.items;
        List<Integer> list = new ArrayList<>();
        if (canRecipe()){
            for (ItemStack input : inputs) {
                for (ItemStack item : items) {
                    if (input.getItem() == item.getItem()){
                       list.add(item.getCount() / input.getCount());
                    }
                }
            }
        }

        int min = 65;
        for (Integer i : list) {
            if (i < min) min = i;
        }

        return min;
    }

    //获取玩家食品熟练度
    public int getFoodRecipesLevel(){
        return this.fooData.get(0);
    }

    public int getExp(){
        return  (int) Math.floor(this.fooData.get(1) / (double)CookingPacket.getUpExp(getFoodRecipesLevel()) * 49);
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
                if (itemStack1.getItem() instanceof PMFoodItem)
                    if (!this.mergeItemStack(itemStack1, 0, 4, false)) return ItemStack.EMPTY;
                if (index < 32) { //从物品栏到快捷栏
                    if (!this.mergeItemStack(itemStack1, 33, 41, false)) return ItemStack.EMPTY;
                } else if (index < 41) {
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
        return this.world.getRecipeManager().getRecipe(ModRecipeType.POT, new Inventory(stack), this.world).isPresent();
    }

    public boolean canRecipe(){
        return this.data.get(1) != 0;
    }

    public int getFoodLevel(){
        return this.data.get(2);
    }
}
