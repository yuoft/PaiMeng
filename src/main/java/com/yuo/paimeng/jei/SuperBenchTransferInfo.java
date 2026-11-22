package com.yuo.paimeng.jei;

import com.yuo.paimeng.menu.PMMenuTypes;
import com.yuo.paimeng.menu.SuperBenchContainer;
import com.yuo.paimeng.recipe.BenchRecipe;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SuperBenchTransferInfo implements IRecipeTransferInfo<SuperBenchContainer, BenchRecipe> {

    @Override
    public Class<? extends SuperBenchContainer> getContainerClass() {
        return SuperBenchContainer.class;
    }

    @Override
    public Optional<MenuType<SuperBenchContainer>> getMenuType() {
        return Optional.of(PMMenuTypes.superBenchContainer.get());
    }

    @Override
    public RecipeType<BenchRecipe> getRecipeType() {
        return SuperBenchRecipeCategory.RECIPE_TYPE;
    }

    @Override
    public boolean canHandle(SuperBenchContainer container, BenchRecipe recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(SuperBenchContainer container, BenchRecipe recipe) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(SuperBenchContainer container, BenchRecipe recipe) {
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
