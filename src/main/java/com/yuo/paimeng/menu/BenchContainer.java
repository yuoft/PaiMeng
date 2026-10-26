package com.yuo.paimeng.menu;

import com.yuo.paimeng.items.PMFoodItem;
import com.yuo.paimeng.network.CookingPacket;
import com.yuo.paimeng.recipe.ModRecipeType;
import com.yuo.paimeng.tiles.BenchTile;
import com.yuo.paimeng.tiles.TileUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.List;

public class BenchContainer extends AbstractContainerMenu {
    private final BenchTile benchTile; //存储食材
    private final CookingIntArray data;
    private final FoodRecipesIntArray fooData;
    private final Level world;

    public BenchContainer(int id, Inventory playerInventory){
        //1.20.1 的方块实体没有无参构造，这里创建一个只用于客户端的空容器
        this(id, playerInventory , new BenchTile(BlockPos.ZERO, Blocks.AIR.defaultBlockState()));
    }

    public BenchContainer(int id, Inventory playerInventory, BenchTile tile) {
        super(ContainerTypeRegistry.benchContainer.get(), id);
        this.benchTile = tile;
        this.fooData = tile.foodData;
        this.world = playerInventory.player.level();
        this.data = tile.data;
        addDataSlots(data); //同步数据
        addDataSlots(fooData);
        //食材槽
        for (int m = 0; m < 2; m++){
            for (int n = 0; n < 2; n++){
                this.addSlot(new CookingSlot(benchTile, n + m * 2, 8 + n * 30, 22 + m * 27));
            }
        }
        this.addSlot(new CookingSlot(benchTile,4, 91, 16));
        //食品槽
        this.addSlot(new ModOutputSlot(benchTile, 5, 132, 33));
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

    /**
     * 获取当前容器内食材 能够烹饪的最大食物数量
     * @return 数量
     */
    public int getItemMaxCount(){
        NonNullList<ItemStack> inputs = TileUtils.getRecipeInputs(world, ModRecipeType.BENCH, this.benchTile);
        List<Integer> list = new ArrayList<>();
        if (canRecipe()){
            for (ItemStack input : inputs) {
                for (int i = 0; i < this.benchTile.getContainerSize(); i++) {
                    ItemStack item = this.benchTile.getItem(i);
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

    public int getBurnTime(){
        return (int) Math.ceil((this.data.get(0) / (20 * 60 * 16.0)) * 14.0);
    }

    public int getExp(){
        return  (int) Math.floor(this.fooData.get(1) / (double) CookingPacket.getUpExp(getFoodRecipesLevel()) * 49);
    }

    //1.20.1: canInteractWith -> stillValid
    @Override
    public boolean stillValid(Player playerIn) {
        return this.benchTile.stillValid(playerIn);
    }

    //玩家shift行为 1.20.1: transferStackInSlot -> quickMoveStack
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (index > 5){
                if (itemStack1.getItem() instanceof PMFoodItem)
                    if (!this.moveItemStackTo(itemStack1, 0, 5, false)) return ItemStack.EMPTY;
                if (index < 32) { //从物品栏到快捷栏
                    if (!this.moveItemStackTo(itemStack1, 34, 42, false)) return ItemStack.EMPTY;
                } else if (index < 41) {
                    if (!this.moveItemStackTo(itemStack1, 6, 33, false)) return ItemStack.EMPTY;
                }
            }else if (!this.moveItemStackTo(itemStack1, 6, 42, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }

    public boolean canRecipe(){
        return this.data.get(1) != 0;
    }

    public int getFoodLevel(){
        return this.data.get(2);
    }
}
