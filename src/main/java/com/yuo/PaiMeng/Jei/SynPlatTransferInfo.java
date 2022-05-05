package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Container.SynPlatContainer;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;

public class SynPlatTransferInfo implements IRecipeTransferInfo<SynPlatContainer> {

    @Override
    public Class<SynPlatContainer> getContainerClass() {
        return SynPlatContainer.class;
    }

    @Override
    public ResourceLocation getRecipeCategoryUid() {
        return SynPlatRecipeCategory.UID;
    }

    @Override
    public boolean canHandle(SynPlatContainer container) {
        return true;
    }

    @Override
    public List<Slot> getRecipeSlots(SynPlatContainer container) {
        List<Slot> slots = new ArrayList<>();
        for (int i = 0; i < 4; i++){
            slots.add(container.getSlot(i));
        }
        return slots;
    }

    @Override
    public List<Slot> getInventorySlots(SynPlatContainer container) {
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
