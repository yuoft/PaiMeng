package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Container.ContainerTypeRegistry;
import com.yuo.PaiMeng.Container.SynPlatContainer;
import com.yuo.PaiMeng.Recipes.SynPlatRecipe;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SynPlatTransferInfo implements IRecipeTransferInfo<SynPlatContainer, SynPlatRecipe> {

    @Override
    public Class<? extends SynPlatContainer> getContainerClass() {
        return SynPlatContainer.class;
    }

    @Override
    public Optional<MenuType<SynPlatContainer>> getMenuType() {
        return Optional.of(ContainerTypeRegistry.synPlatContainer.get());
    }

    @Override
    public RecipeType<SynPlatRecipe> getRecipeType() {
        return SynPlatRecipeCategory.RECIPE_TYPE;
    }

    @Override
    public boolean canHandle(SynPlatContainer container, SynPlatRecipe recipe) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(SynPlatContainer container, SynPlatRecipe recipe) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(SynPlatContainer container, SynPlatRecipe recipe) {
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
