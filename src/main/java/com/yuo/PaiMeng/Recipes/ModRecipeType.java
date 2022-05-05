package com.yuo.PaiMeng.Recipes;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface ModRecipeType<T extends IRecipe> extends IRecipeType {

    IRecipeType<PotRecipe> POT = IRecipeType.register(PaiMeng.MOD_ID + ":pot");
    IRecipeType<BenchRecipe> BENCH = IRecipeType.register(PaiMeng.MOD_ID + ":bench");
    IRecipeType<SynPlatRecipe> SYN_PLAT = IRecipeType.register(PaiMeng.MOD_ID + ":syn_plat");
    IRecipeType<SeedBoxRecipe> SEED_BOX = IRecipeType.register(PaiMeng.MOD_ID + ":seed_box");


}


