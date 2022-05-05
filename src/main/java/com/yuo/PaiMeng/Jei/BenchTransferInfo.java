package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Container.BenchContainer;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class BenchTransferInfo implements IRecipeTransferInfo<BenchContainer> {
    @Override
    public Class<BenchContainer> getContainerClass() {
        return BenchContainer.class;
    }

    @Override
    public ResourceLocation getRecipeCategoryUid() {
        return BenchRecipeCategory.UID;
    }

    @Override
    public boolean canHandle(BenchContainer container) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(BenchContainer container) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(BenchContainer container) {
        List<Slot> slots = new ArrayList<>();
        int totalSize = container.inventorySlots.size();
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
