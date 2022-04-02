package com.yuo.PaiMeng.Capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

//能力提供器
public class RelicsItemCapabilityProvider implements ICapabilityProvider, INBTSerializable<CompoundNBT> {
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
    public CompoundNBT serializeNBT() {
        return getOrCreateCap().serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        getOrCreateCap().deserializeNBT(nbt);
    }
}
