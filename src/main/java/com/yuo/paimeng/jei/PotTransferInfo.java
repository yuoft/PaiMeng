package com.yuo.paimeng.jei;

import com.yuo.paimeng.menu.ContainerTypeRegistry;
import com.yuo.paimeng.menu.PotContainer;
import com.yuo.paimeng.recipe.PotRecipe;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PotTransferInfo implements IRecipeTransferInfo<PotContainer, PotRecipe> {

    @Override
    public Class<? extends PotContainer> getContainerClass() {
        return PotContainer.class;
    }

    @Override
    public Optional<MenuType<PotContainer>> getMenuType() {
        return Optional.of(ContainerTypeRegistry.potContainer.get());
    }

    @Override
    public RecipeType<PotRecipe> getRecipeType() {
        return PotRecipeCategory.RECIPE_TYPE;
    }

    @Override
    public boolean canHandle(PotContainer container, PotRecipe recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(PotContainer container, PotRecipe recipe) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(PotContainer container, PotRecipe recipe) {
        List<Slot> slots = new ArrayList<>();
        // 36格玩家物品栏
        int totalSize = container.slots.size();
        int sideInventoryEnd = totalSize - 36; //初始物品槽位id
        // 27 添加背包
        for (int i = sideInventoryEnd; i < totalSize; i++) {
            slots.add(container.getSlot(i));
        }
        // 9 快捷栏
        for (int i = 10; i < sideInventoryEnd; i++) {
            slots.add(container.getSlot(i));
        }
        return slots;
    }

}
