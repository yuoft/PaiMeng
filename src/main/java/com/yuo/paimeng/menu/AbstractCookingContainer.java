package com.yuo.paimeng.menu;

import com.yuo.paimeng.items.PMFoodItem;
import com.yuo.paimeng.network.CookingPacket;
import com.yuo.paimeng.tiles.AbstractCookingTile;
import com.yuo.paimeng.tiles.TileUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractCookingContainer<T extends AbstractCookingTile> extends AbstractContainerMenu {
    
    // 🔴 统一定义槽位边界，避免魔法数字
    public static final int INPUT_START = 0;
    public static final int INPUT_END = 5;
    public static final int OUTPUT_SLOT = 5;
    public static final int INV_START = 6;
    public static final int INV_END = 33;
    public static final int HOTBAR_START = 33;
    public static final int HOTBAR_END = 42;

    protected final T tile;
    protected final CookingIntArray data;
    protected final FoodRecipesIntArray fooData;
    protected final Level world;
    protected final BlockPos pos;

    protected AbstractCookingContainer(MenuType<?> type, int id, Inventory playerInventory, T tile) {
        super(type, id);
        this.tile = tile;
        this.pos = tile.getBlockPos();
        this.fooData = tile.foodData;
        this.world = playerInventory.player.level();
        this.data = tile.data;
        
        addDataSlots(data); //同步数据
        addDataSlots(fooData);
        
        // 食材槽 (0-4)
        for (int m = 0; m < 2; m++){
            for (int n = 0; n < 2; n++){
                this.addSlot(new CookingSlot(tile, n + m * 2, 8 + n * 30, 22 + m * 27));
            }
        }
        this.addSlot(new CookingSlot(tile, 4, 91, 16));
        
        // 食品槽 (5)
        this.addSlot(new ModOutputSlot(tile, 5, 132, 33));
        
        // 玩家物品栏 (6-32)
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        // 玩家快捷栏 (33-41)
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }
    }

    // 🔴 子类必须实现的方法
    protected abstract RecipeType<?> getRecipeType();
    protected abstract double getMaxBurnTime();

    public BlockPos getPos() {
        return pos;
    }

    public int getBurnTime() {
        return (int) Math.ceil((this.data.get(0) / getMaxBurnTime()) * 14.0);
    }

    public int getTime() {
        return (int) Math.ceil(this.data.get(0) / 20.0);
    }

    public int getItemMaxCount() {
        NonNullList<ItemStack> inputs = TileUtils.getRecipeInputs(world, getRecipeType(), this.tile);
        List<Integer> list = new ArrayList<>();
        if (canRecipe()){
            for (ItemStack input : inputs) {
                for (int i = 0; i < this.tile.getContainerSize(); i++) {
                    ItemStack item = this.tile.getItem(i);
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

    public int getFoodRecipesLevel() {
        return this.fooData.get(0);
    }

    public int getExp() {
        return  (int) Math.floor(this.fooData.get(1) / (double) CookingPacket.getUpExp(getFoodRecipesLevel()) * 49);
    }

    public boolean canRecipe() {
        return this.data.get(1) != 0;
    }

    public int getFoodLevel() {
        return this.data.get(2);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.tile.stillValid(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            
            if (index >= OUTPUT_SLOT) { // 如果点击的是输出槽或玩家背包
                if (itemStack1.getItem() instanceof PMFoodItem)
                    if (!this.moveItemStackTo(itemStack1, INPUT_START, INPUT_END, false)) return ItemStack.EMPTY;
                if (itemStack1.is(Items.TOTEM_OF_UNDYING))
                    if (!this.moveItemStackTo(itemStack1, INPUT_END - 1, INPUT_END, false)) return ItemStack.EMPTY;
                
                // 🔴 修复：原版正确的槽位边界
                if (index < HOTBAR_START) { // 从物品栏到快捷栏
                    if (!this.moveItemStackTo(itemStack1, HOTBAR_START, HOTBAR_END, false)) return ItemStack.EMPTY;
                } else if (index < HOTBAR_END) { // 从快捷栏到物品栏
                    if (!this.moveItemStackTo(itemStack1, INV_START, INV_END, false)) return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemStack1, INV_START, HOTBAR_END, false)) {
                return ItemStack.EMPTY; // 取出来
            }

            if (itemStack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }
        return itemstack;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
        boolean flag = false;
        int i = startIndex;
        if (reverseDirection) {
            i = endIndex - 1;
        }

        if (stack.isStackable() || stack.is(Items.TOTEM_OF_UNDYING)) {
            while (!stack.isEmpty()) {
                if (reverseDirection) {
                    if (i < startIndex) break;
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot1 = this.slots.get(i);
                ItemStack itemstack = slot1.getItem();
                if (!itemstack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemstack)) {
                    int j = itemstack.getCount() + stack.getCount();

                    int maxSize = stack.is(Items.TOTEM_OF_UNDYING)
                            ? slot1.getMaxStackSize()
                            : Math.min(slot1.getMaxStackSize(), stack.getMaxStackSize());

                    if (j <= maxSize) {
                        stack.setCount(0);
                        itemstack.setCount(j);
                        slot1.setChanged();
                        flag = true;
                    } else if (itemstack.getCount() < maxSize) {
                        stack.shrink(maxSize - itemstack.getCount());
                        itemstack.setCount(maxSize);
                        slot1.setChanged();
                        flag = true;
                    }
                }

                if (reverseDirection) --i;
                else ++i;
            }
        }

        if (!stack.isEmpty()) {
            if (reverseDirection) {
                i = endIndex - 1;
            } else {
                i = startIndex;
            }

            while (true) {
                if (reverseDirection) {
                    if (i < startIndex) break;
                } else if (i >= endIndex) {
                    break;
                }

                Slot slot1 = this.slots.get(i);
                ItemStack itemstack = slot1.getItem();

                if (itemstack.isEmpty() && slot1.mayPlace(stack)) {
                    if (stack.getCount() > slot1.getMaxStackSize()) {
                        slot1.setByPlayer(stack.split(slot1.getMaxStackSize()));
                    } else {
                        slot1.setByPlayer(stack.split(stack.getCount()));
                    }
                    slot1.setChanged();
                    flag = true;
                    break;
                }

                if (reverseDirection) --i;
                else ++i;
            }
        }
        return flag;
    }
}