package com.yuo.paimeng.recipe;

import com.yuo.paimeng.PaiMeng;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RecipeSerializerRegistry {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_TYPES = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, PaiMeng.MOD_ID);

    public static final RegistryObject<RecipeSerializer<PotRecipe>> POT = RECIPE_TYPES.register("pot", PotRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<BenchRecipe>> BENCH = RECIPE_TYPES.register("bench", BenchRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<SynPlatRecipe>> SYN_PLAT = RECIPE_TYPES.register("syn_plat", SynPlatRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<SeedBoxRecipe>> SEED_BOX = RECIPE_TYPES.register("seed_box", SeedBoxRecipe.Serializer::new);

}