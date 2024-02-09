package com.yuo.PaiMeng.Items.Weapon;

import com.yuo.PaiMeng.Items.PMItems;
import net.minecraft.item.IItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyValue;

import java.util.function.Supplier;

public enum PMItemTier implements IItemTier {
    FIVE(6, 6480, 25.0F, 30.0F, 30, () -> {
        return Ingredient.fromItems(PMItems.zijing.get());
    }),
    FOUR(5, 4182, 18.0F, 20.0F, 23, () -> {
        return Ingredient.fromItems(PMItems.zijing.get());
    }),
    THREE(4, 2946, 13.0F, 15.0F, 18, () -> {
        return Ingredient.fromItems(PMItems.zijing.get());
    }),
    TWO(3, 1531, 10.0F, 11.0F, 14, () -> {
        return Ingredient.fromItems(PMItems.baitie.get());
    }),
    ONE(2, 648, 8.0F, 7.0F, 12, () -> {
        return Ingredient.fromItems(PMItems.baitie.get());
    });

    private final int maxUses;//耐久
    private final float efficiency;//使用效率
    private final float attackDamage;//工具伤害
    private final int harvestLevel;//工具等级
    private final int enchantability;//附魔等级
    private final LazyValue<Ingredient> repairMaterial;//修复材料

    PMItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = new LazyValue<>(repairMaterialIn);
    }

    public int getMaxUses() {
        return this.maxUses;
    }

    public float getEfficiency() {
        return this.efficiency;
    }

    public float getAttackDamage() {
        return this.attackDamage;
    }

    public int getHarvestLevel() {
        return this.harvestLevel;
    }

    public int getEnchantability() {
        return this.enchantability;
    }

    public Ingredient getRepairMaterial() {
        return this.repairMaterial.getValue();
    }
}
