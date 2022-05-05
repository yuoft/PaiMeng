package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Container.SeedBoxContainer;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SeedBoxTransferInfo implements IRecipeTransferInfo<SeedBoxContainer> {

    @Override
    public Class<SeedBoxContainer> getContainerClass() {
        return SeedBoxContainer.class;
    }

    @Override
    public ResourceLocation getRecipeCategoryUid() {
        return SeedBoxRecipeCategory.UID;
    }

    @Override
    public boolean canHandle(SeedBoxContainer container) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(SeedBoxContainer container) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(SeedBoxContainer container) {
        List<Slot> slots = new ArrayList<>();
        // 36格玩家物品栏
        int totalSize = container.inventorySlots.size();
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
