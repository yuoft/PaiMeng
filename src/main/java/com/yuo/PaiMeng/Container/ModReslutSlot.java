package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Items.Drug;
import com.yuo.PaiMeng.Tiles.TileUtils;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;

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

    //1.20.1: onTake 返回 void（1.16.5 返回 ItemStack）
    @Override
    public void onTake(Player thePlayer, ItemStack stack) {
        this.checkTakeAchievements(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
//        NonNullList<ItemStack> nonnulllist = thePlayer.level().getRecipeManager().getRemainingItemsFor(recipeType, this.craftMatrix, thePlayer.level());
        NonNullList<ItemStack> recipeInputs;
        if (stack.getItem() instanceof Drug){
            recipeInputs = getDrugItems();
        }else recipeInputs = TileUtils.getRecipeInputs(thePlayer.level(), recipeType, this.craftMatrix);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
        for(int i = 0; i < recipeInputs.size(); ++i) {
            ItemStack itemstack = this.craftMatrix.getItem(i);
            ItemStack itemstack1 = recipeInputs.get(i);
            if (!itemstack.isEmpty()) {
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
            if (i > 1)
                list.add(new ItemStack(this.craftMatrix.getItem(i).getItem(), 1));
            else list.add(this.craftMatrix.getItem(i));
        }
        return list;
    }
}
