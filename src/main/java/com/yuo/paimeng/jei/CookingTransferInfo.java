package com.yuo.paimeng.jei;

import com.yuo.paimeng.menu.BenchContainer;
import com.yuo.paimeng.menu.ContainerTypeRegistry;
import com.yuo.paimeng.recipe.CookingRecipe;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CookingTransferInfo implements IRecipeTransferInfo<BenchContainer, CookingRecipe> {

    @Override
    public Class<? extends BenchContainer> getContainerClass() {
        return BenchContainer.class;
    }

    @Override
    public Optional<MenuType<BenchContainer>> getMenuType() {
        return Optional.of(ContainerTypeRegistry.benchContainer.get());
    }

    @Override
    public RecipeType<CookingRecipe> getRecipeType() {
        return CookingRecipeCategory.RECIPE_TYPE;
    }

    @Override
    public boolean canHandle(BenchContainer container, CookingRecipe recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(BenchContainer container, CookingRecipe recipe) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(BenchContainer container, CookingRecipe recipe) {
        List<Slot> slots = new ArrayList<>();
        int totalSize = container.slots.size();
        int sideInventoryEnd = totalSize - 36; //初始物品槽位id
        for (int i = sideInventoryEnd; i < totalSize; i++) {
            slots.add(container.getSlot(i));
        }
        for (int i = 10; i < sideInventoryEnd; i++) {
            slots.add(container.getSlot(i));
        }
        return slots;
    }

}
