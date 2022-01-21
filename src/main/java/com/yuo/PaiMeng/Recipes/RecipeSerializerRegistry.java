package com.yuo.PaiMeng.Recipes;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerRegistry {
    public static final DeferredRegister RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PaiMeng.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<?>> POT = RECIPE_TYPES.register("pot", () -> new PotRecipe.Serializer());
    public static final RegistryObject<IRecipeSerializer<?>> BENCH = RECIPE_TYPES.register("bench", () -> new BenchRecipe.Serializer());
    public static final RegistryObject<IRecipeSerializer<?>> SYN_PLAT = RECIPE_TYPES.register("syn_plat", () -> new SynPlatRecipe.Serializer());
    public static final RegistryObject<IRecipeSerializer<?>> SEED_BOX = RECIPE_TYPES.register("seed_box", () -> new SeedBoxRecipe.Serializer());

}