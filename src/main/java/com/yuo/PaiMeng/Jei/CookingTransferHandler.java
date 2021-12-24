package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Gui.BenchContainer;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.Map;

public class CookingTransferHandler implements IRecipeTransferHandler {

    //配方转移逻辑
    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(Container container, Object recipe, IRecipeLayout recipeLayout, PlayerEntity player, boolean maxTransfer, boolean doTransfer) {
        IGuiItemStackGroup stacks = recipeLayout.getItemStacks();
        Map<Integer, ? extends IGuiIngredient<ItemStack>> guiIngredients = stacks.getGuiIngredients();
        for (int i = 0; i < guiIngredients.size(); i++){
            IGuiIngredient<ItemStack> ingredient = guiIngredients.get(i);
            if (ingredient.isInput()){
                container.setAll(ingredient.getAllIngredients());
            }
        }
        return IRecipeTransferHandler.super.transferRecipe(container, recipe, recipeLayout, player, maxTransfer, doTransfer);
    }

    @Override
    public Class getContainerClass() {
        return BenchContainer.class;
    }
}
