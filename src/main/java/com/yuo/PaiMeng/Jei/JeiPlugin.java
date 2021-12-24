package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Recipes.BenchRecipe;
import com.yuo.PaiMeng.Recipes.ModRecipeType;
import com.yuo.PaiMeng.Recipes.PotRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.util.ResourceLocation;

import java.util.Objects;
import java.util.stream.Collectors;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(PaiMeng.MOD_ID, "jei_plugin");
    }

    //配方类别
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        registration.addRecipeCategories(new PotRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new BenchRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    //注册配方
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().world).getRecipeManager();
        registration.addRecipes(recipeManager.getRecipesForType(ModRecipeType.POT).stream()
                .filter(r -> r instanceof PotRecipe).collect(Collectors.toList()), PotRecipeCategory.UID);
        registration.addRecipes(recipeManager.getRecipesForType(ModRecipeType.BENCH).stream()
                .filter(r -> r instanceof BenchRecipe).collect(Collectors.toList()), BenchRecipeCategory.UID);
    }

    //注册+号添加
    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
//        registration.addRecipeTransferHandler(PotContainer.class, PotRecipeCategory.UID, 0, 3, 4, 36);
//        registration.addRecipeTransferHandler(BenchContainer.class, BenchRecipeCategory.UID, 0, 3, 4, 36);
        registration.addRecipeTransferHandler(new PotTransferInfo());
        registration.addRecipeTransferHandler(new BenchTransferInfo());
    }

    //注册机器合成
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ItemRegistry.cookingPot.get()), PotRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(ItemRegistry.cookingBench.get()), BenchRecipeCategory.UID);
    }
}
