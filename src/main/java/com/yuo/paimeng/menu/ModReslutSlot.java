package com.yuo.paimeng.menu;

import com.yuo.paimeng.items.Drug;
import com.yuo.paimeng.tiles.TileUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;

public class ModReslutSlot extends ResultSlot {
    private final CraftingContainer craftMatrix;
    private final Player player;
    private final RecipeType<?> recipeType;

    public ModReslutSlot(RecipeType<?> recipeType, Player player, CraftingContainer craftingInventory, Container inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
        this.craftMatrix = craftingInventory;
        this.recipeType = recipeType;
    }

    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
        this.checkTakeAchievements(stack);
        ForgeHooks.setCraftingPlayer(thePlayer);
        NonNullList<ItemStack> recipeInputs;
        
        // 🔴 判断是否是特殊药水产物
        if (stack.getItem() instanceof Drug){
            recipeInputs = getDrugItems();
        }else {
            recipeInputs = TileUtils.getRecipeInputs(thePlayer.level(), recipeType, this.craftMatrix);
        }
        
        ForgeHooks.setCraftingPlayer(null);
        
        for(int i = 0; i < recipeInputs.size(); ++i) {
            ItemStack itemstack = this.craftMatrix.getItem(i);
            ItemStack itemstack1 = recipeInputs.get(i);
            
            // 如果输入槽有物品，且该槽位被列在扣除列表中
            if (!itemstack.isEmpty() && !itemstack1.isEmpty()) {
                this.craftMatrix.removeItem(i, itemstack1.getCount());
            }
        }
    }

    /**
     * 获取药水消耗物品列表
     * @return 列表
     */
    private NonNullList<ItemStack> getDrugItems(){
        NonNullList<ItemStack> list = NonNullList.create();
        for (int i = 0; i < 4; i++){
            ItemStack stack = this.craftMatrix.getItem(i);
            if (!stack.isEmpty()) {
                ItemStack copy = stack.copy();
                copy.setCount(1); // 🔴 关键修复：无论槽位里有多少个，强制只扣除 1 个
                list.add(copy);
            } else {
                list.add(ItemStack.EMPTY); // 保持索引对齐
            }
        }
        return list;
    }
}