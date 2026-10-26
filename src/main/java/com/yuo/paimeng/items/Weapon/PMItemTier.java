package com.yuo.paimeng.items.Weapon;

import com.yuo.paimeng.items.PMItems;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public enum PMItemTier implements Tier {
    FIVE(6, 6480, 25.0F, 30.0F, 30, () -> {
        return Ingredient.of(PMItems.zijing.get());
    }),
    FOUR(5, 4182, 18.0F, 20.0F, 23, () -> {
        return Ingredient.of(PMItems.zijing.get());
    }),
    THREE(4, 2946, 13.0F, 15.0F, 18, () -> {
        return Ingredient.of(PMItems.zijing.get());
    }),
    TWO(3, 1531, 10.0F, 11.0F, 14, () -> {
        return Ingredient.of(PMItems.baitie.get());
    }),
    ONE(2, 648, 8.0F, 7.0F, 12, () -> {
        return Ingredient.of(PMItems.baitie.get());
    });

    private final int maxUses;//耐久
    private final float efficiency;//使用效率
    private final float attackDamage;//工具伤害
    private final int harvestLevel;//工具等级
    private final int enchantability;//附魔等级
    private final Lazy<Ingredient> repairMaterial;//修复材料

    PMItemTier(int harvestLevelIn, int maxUsesIn, float efficiencyIn, float attackDamageIn, int enchantabilityIn, Supplier<Ingredient> repairMaterialIn) {
        this.harvestLevel = harvestLevelIn;
        this.maxUses = maxUsesIn;
        this.efficiency = efficiencyIn;
        this.attackDamage = attackDamageIn;
        this.enchantability = enchantabilityIn;
        this.repairMaterial = Lazy.of(repairMaterialIn);
    }

    //1.20.1 的 Tier（原 IItemTier）方法名
    @Override
    public int getUses() {
        return this.maxUses;
    }

    @Override
    public float getSpeed() {
        return this.efficiency;
    }

    @Override
    public float getAttackDamageBonus() {
        return this.attackDamage;
    }

    @Override
    public int getLevel() {
        return this.harvestLevel;
    }

    @Override
    public int getEnchantmentValue() {
        return this.enchantability;
    }

    @Override
    public Ingredient getRepairIngredient() {
        return this.repairMaterial.get();
    }

    //保留 1.16.5 的旧方法名，避免其它代码/外部调用失效
    public int getMaxUses() {
        return getUses();
    }

    public float getEfficiency() {
        return getSpeed();
    }

    public float getAttackDamage() {
        return getAttackDamageBonus();
    }

    public int getHarvestLevel() {
        return getLevel();
    }

    public int getEnchantability() {
        return getEnchantmentValue();
    }

    public Ingredient getRepairMaterial() {
        return getRepairIngredient();
    }
}
