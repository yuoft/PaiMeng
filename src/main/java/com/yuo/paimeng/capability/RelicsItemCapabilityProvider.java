package com.yuo.paimeng.capability;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

//能力提供器
public class RelicsItemCapabilityProvider implements ICapabilitySerializable<CompoundTag> {
    private RelicsItemHandler relicsItemHandler;

    @Nonnull
    RelicsItemHandler getOrCreateCap(){
        if (this.relicsItemHandler == null){
            this.relicsItemHandler = new RelicsItemHandler();
        }
        return this.relicsItemHandler;
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == ModCapability.RELICS_CAPABILITY ? LazyOptional.of(this::getOrCreateCap).cast() : LazyOptional.empty();
    }

    @Override
    public CompoundTag serializeNBT() {
        return getOrCreateCap().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        getOrCreateCap().deserializeNBT(nbt);
    }
}
