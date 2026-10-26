package com.yuo.PaiMeng.Recipes;

import com.yuo.PaiMeng.ModUtils;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

//配方类型（1.20.1 中 RecipeType 仍是接口，用 RecipeType.simple 创建实例）
public interface ModRecipeType {

    RecipeType<PotRecipe> POT = RecipeType.simple(ModUtils.fa( "pot"));
    RecipeType<BenchRecipe> BENCH = RecipeType.simple(ModUtils.fa( "bench"));
    RecipeType<SynPlatRecipe> SYN_PLAT = RecipeType.simple(ModUtils.fa( "syn_plat"));
    RecipeType<SeedBoxRecipe> SEED_BOX = RecipeType.simple(ModUtils.fa("seed_box"));

}
