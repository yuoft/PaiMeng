package com.yuo.paimeng.capability;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;

//能力
public class BlowCapability implements IBlowCapability{
    private double criticalRate; //暴击率
    private double criticalDamage; //暴击伤害

    public BlowCapability(float criticalRate, float criticalDamage) {
        this.criticalRate = criticalRate;
        this.criticalDamage = criticalDamage;
    }

    public BlowCapability(){
        this.criticalRate = 0.1f;
        this.criticalDamage = 0.5f;
    }

    @Override
    public void resetCritical(){
        this.criticalRate = 0.1f;
        this.criticalDamage = 0.5f;
    }

    //增加多少暴击率 0-100
    @Override
    public void setCriticalRate(double criticalRate) {
        this.criticalRate += criticalRate;
        this.criticalRate = Math.min(this.criticalRate, 1);
        this.criticalRate = Math.max(this.criticalRate, 0);
    }

    // 0 - 无上限
    @Override
    public void setCriticalDamage(double criticalDamage) {
        this.criticalDamage += criticalDamage;
        this.criticalDamage = Math.max(this.criticalDamage, 0);
    }

    @Override
    public double getCriticalRate() {
        return this.criticalRate;
    }

    @Override
    public double getCriticalDamage() {
        return this.criticalDamage;
    }

    @Override
    public CompoundTag serializeNBT() {
        ListTag listNBT = new ListTag();
        CompoundTag criticalRate = new CompoundTag();
        CompoundTag criticalDamage = new CompoundTag();
        criticalRate.putDouble("critical_rate", this.criticalRate);
        criticalDamage.putDouble("critical_damage", this.criticalDamage);
        listNBT.add(0,criticalRate);
        listNBT.add(1,criticalDamage);
        CompoundTag nbt = new CompoundTag();
        nbt.put("critical", listNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag listNBT = (ListTag) nbt.get("critical");
        CompoundTag criticalRateNbt = (CompoundTag) listNBT.get(0);
        CompoundTag criticalDamageNbt = (CompoundTag) listNBT.get(1);
        double playerHit = criticalRateNbt.getDouble("critical_rate");
        double playerDamage = criticalDamageNbt.getDouble("critical_damage");
        this.criticalRate = playerHit;
        this.criticalDamage = playerDamage;
    }
}
