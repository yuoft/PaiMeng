package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsHelper;
import com.yuo.PaiMeng.Items.RelicsType;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Recipes.BenchRecipe;
import com.yuo.PaiMeng.Recipes.CookingRecipe;
import com.yuo.PaiMeng.Recipes.ModRecipeType;
import com.yuo.PaiMeng.Recipes.PotRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.registration.*;
import net.minecraft.client.Minecraft;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;

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

    //注册物品不同nbt 使用nbt来在jei中显示
    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        IModPlugin.super.registerItemSubtypes(registration);
        //药剂
        registration.registerSubtypeInterpreter(PMItems.drug.get(), (e, u) -> {
            List<EffectInstance> list = PotionUtils.getEffectsFromStack(e);
            if (list.size() > 0) {
                StringBuilder s = new StringBuilder();
                for (EffectInstance instance : list) {
                    s.append(instance.getEffectName());
                    s.append(instance.getAmplifier());
                    s.append(instance.getDuration());
                }
                return s.toString();
            }
            return "";
        });
//        //圣遗物
//        registration.registerSubtypeInterpreter(PMItems.xingyunerRelics.get(), (e, u) -> {
//            return RelicsHelper.getTypeForStack(e).getName() + RelicsHelper.getMaxStar(e);
//        });
    }
}
