package com.yuo.PaiMeng.Capability;

import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.util.INBTSerializable;

//圣遗物数据保存
public class RelicsItemCapability implements INBTSerializable<CompoundNBT> {
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
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = new CompoundNBT();
        ItemStackHelper.saveAllItems(nbt, this.itemStacks, true);
        nbt.put("relics", nbt);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ItemStackHelper.loadAllItems(nbt, this.itemStacks);
    }
}
