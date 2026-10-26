package com.yuo.paimeng.capability;

import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.core.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;

//圣遗物数据保存
public class RelicsItemCapability implements INBTSerializable<CompoundTag> {
    private final int SLOT_SIZE = 6;
    private NonNullList<ItemStack> itemStacks = NonNullList.withSize(SLOT_SIZE, ItemStack.EMPTY);

    public RelicsItemCapability(NonNullList<ItemStack> list) {
        this.itemStacks = list;
    }

    public RelicsItemCapability(){
    }

    public void setItem(int slot, ItemStack stack){
        this.itemStacks.set(slot, stack);
    }

    public void getItem(int slot){
        this.itemStacks.get(slot);
    }

    public NonNullList<ItemStack> getItemStacks(){
        return this.itemStacks;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        ContainerHelper.saveAllItems(nbt, this.itemStacks, true);
        nbt.put("relics", nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ContainerHelper.loadAllItems(nbt, this.itemStacks);
    }
}
