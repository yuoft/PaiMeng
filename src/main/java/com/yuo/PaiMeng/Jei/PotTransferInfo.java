package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Container.PotContainer;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class PotTransferInfo implements IRecipeTransferInfo<PotContainer> {

    @Override
    public Class<PotContainer> getContainerClass() {
        return PotContainer.class;
    }

    @Override
    public ResourceLocation getRecipeCategoryUid() {
        return PotRecipeCategory.UID;
    }

    @Override
    public boolean canHandle(PotContainer container) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(PotContainer container) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < 5; i++){
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(PotContainer container) {
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
