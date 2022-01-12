package com.yuo.PaiMeng.Capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;

//能力
public class BlowCapability implements IBlowCapability{
    private float criticalRate;
    private float criticalDamage;

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
    public void setCriticalRate(float criticalRate) {
        this.criticalRate += criticalRate;
        this.criticalRate = this.criticalRate > 1.0f ? 1.0f : this.criticalRate;
        this.criticalRate = this.criticalRate < 0 ? 0 : this.criticalRate;
    }

    // 0 - 无上限
    @Override
    public void setCriticalDamage(float criticalDamage) {
        this.criticalDamage += criticalDamage;
        this.criticalDamage = this.criticalDamage < 0 ? 0 : this.criticalDamage;
    }

    @Override
    public float getCriticalRate() {
        return this.criticalRate;
    }

    @Override
    public float getCriticalDamage() {
        return this.criticalDamage;
    }

    @Override
    public CompoundNBT serializeNBT() {
        ListNBT listNBT = new ListNBT();
        CompoundNBT criticalRate = new CompoundNBT();
        CompoundNBT criticalDamage = new CompoundNBT();
        criticalRate.putFloat("critical_rate", this.criticalRate);
        criticalDamage.putFloat("critical_damage", this.criticalDamage);
        listNBT.add(0,criticalRate);
        listNBT.add(1,criticalDamage);
        CompoundNBT nbt = new CompoundNBT();
        nbt.put("critical", listNBT);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        ListNBT listNBT = (ListNBT) nbt.get("critical");
        CompoundNBT criticalRateNbt = (CompoundNBT) listNBT.get(0);
        CompoundNBT criticalDamageNbt = (CompoundNBT) listNBT.get(1);
        float playerHit = criticalRateNbt.getFloat("critical_rate");
        float playerDamage = criticalDamageNbt.getFloat("critical_damage");
        this.criticalRate = playerHit;
        this.criticalDamage = playerDamage;
    }
}
