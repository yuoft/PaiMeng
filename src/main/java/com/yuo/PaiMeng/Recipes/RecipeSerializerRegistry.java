package com.yuo.PaiMeng.Recipes;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerRegistry {
    public static final DeferredRegister<IRecipeSerializer<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PaiMeng.MOD_ID);

    public static final RegistryObject<IRecipeSerializer<PotRecipe>> POT = RECIPE_TYPES.register("pot", PotRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<BenchRecipe>> BENCH = RECIPE_TYPES.register("bench", BenchRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<SynPlatRecipe>> SYN_PLAT = RECIPE_TYPES.register("syn_plat", SynPlatRecipe.Serializer::new);
    public static final RegistryObject<IRecipeSerializer<SeedBoxRecipe>> SEED_BOX = RECIPE_TYPES.register("seed_box", SeedBoxRecipe.Serializer::new);

}