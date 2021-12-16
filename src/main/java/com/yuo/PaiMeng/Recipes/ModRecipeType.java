package com.yuo.PaiMeng.Recipes;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;

public interface ModRecipeType<T extends IRecipe> extends IRecipeType {

    IRecipeType<PotRecipe> POT = IRecipeType.register(PaiMeng.MOD_ID + ":pot");

//    static <T extends IRecipe<?>> IRecipeType<T> register(final String key) {
//        return Registry.register(Registry.RECIPE_TYPE, new ResourceLocation(key), new IRecipeType<T>() {
//            public String toString() {
//                return key;
//            }
//        });
//    }
}
