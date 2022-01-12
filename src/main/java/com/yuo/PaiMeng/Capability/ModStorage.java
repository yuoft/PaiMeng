package com.yuo.PaiMeng.Capability;

import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

//能力数据保存
public class ModStorage implements Capability.IStorage<IBlowCapability> {
    @Nullable
    @Override
    public INBT writeNBT(Capability<IBlowCapability> capability, IBlowCapability instance, Direction side) {
        return null;
    }

    @Override
    public void readNBT(Capability<IBlowCapability> capability, IBlowCapability instance, Direction side, INBT nbt) {

    }
}
