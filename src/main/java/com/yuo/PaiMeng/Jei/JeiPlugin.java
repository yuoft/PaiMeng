package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Recipes.BenchRecipe;
import com.yuo.PaiMeng.Recipes.CookingRecipe;
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

import java.util.ArrayList;
import java.util.List;
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
        registration.addRecipeCategories(
                new PotRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new BenchRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new CookingRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new SynPlatRecipeCategory(registration.getJeiHelpers().getGuiHelper()),
                new SeedBoxRecipeCategory(registration.getJeiHelpers().getGuiHelper()));
    }

    //注册配方
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        RecipeManager recipeManager = Objects.requireNonNull(Minecraft.getInstance().world).getRecipeManager();
        registration.addRecipes(recipeManager.getRecipesForType(ModRecipeType.POT).stream()
                .filter(Objects::nonNull).collect(Collectors.toList()), PotRecipeCategory.UID);
        registration.addRecipes(recipeManager.getRecipesForType(ModRecipeType.BENCH).stream()
                .filter(Objects::nonNull).collect(Collectors.toList()), BenchRecipeCategory.UID);
        registration.addRecipes(recipeManager.getRecipesForType(ModRecipeType.POT).stream()
                .filter(Objects::nonNull).collect(Collectors.toList()), CookingRecipeCategory.UID);
        registration.addRecipes(recipeManager.getRecipesForType(ModRecipeType.SYN_PLAT).stream()
                .filter(Objects::nonNull).collect(Collectors.toList()), SynPlatRecipeCategory.UID);
        registration.addRecipes(recipeManager.getRecipesForType(ModRecipeType.SEED_BOX).stream()
                .filter(Objects::nonNull).collect(Collectors.toList()), SeedBoxRecipeCategory.UID);
    }

    private List<CookingRecipe> getBenchRecipes(RecipeManager recipeManager){
        List<PotRecipe> pot = recipeManager.getRecipesForType(ModRecipeType.POT).stream()
                .filter(Objects::nonNull).collect(Collectors.toList());
        List<BenchRecipe> bench = recipeManager.getRecipesForType(ModRecipeType.BENCH).stream()
                .filter(Objects::nonNull).collect(Collectors.toList());
        List<CookingRecipe> recipes = new ArrayList<>();
        recipes.addAll(pot);
        recipes.addAll(bench);
        return recipes;
    }

    //注册+号添加
    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(new CookingTransferHandler(
                registration.getJeiHelpers().getStackHelper(), registration.getTransferHelper(), new PotTransferInfo()), PotRecipeCategory.UID);
        registration.addRecipeTransferHandler(new CookingTransferHandler(
                registration.getJeiHelpers().getStackHelper(), registration.getTransferHelper(), new BenchTransferInfo()), BenchRecipeCategory.UID);
        registration.addRecipeTransferHandler(new CookingTransferHandler(
                registration.getJeiHelpers().getStackHelper(), registration.getTransferHelper(), new CookingTransferInfo()), CookingRecipeCategory.UID);
        registration.addRecipeTransferHandler(new CookingTransferHandler(
                registration.getJeiHelpers().getStackHelper(), registration.getTransferHelper(), new SynPlatTransferInfo()), SynPlatRecipeCategory.UID);
        registration.addRecipeTransferHandler(new CookingTransferHandler(
                registration.getJeiHelpers().getStackHelper(), registration.getTransferHelper(), new SeedBoxTransferInfo()), SeedBoxRecipeCategory.UID);
    }

    //注册机器合成
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(PMItems.cookingPot.get()), PotRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(PMItems.cookingBench.get()), BenchRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(PMItems.cookingBench.get()), CookingRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(PMItems.syntheticPlatform.get()), SynPlatRecipeCategory.UID);
        registration.addRecipeCatalyst(new ItemStack(PMItems.huazhongxia.get()), SeedBoxRecipeCategory.UID);
    }
}
