package com.yuo.paimeng.jei;

import com.yuo.paimeng.menu.BenchContainer;
import com.yuo.paimeng.menu.PotContainer;
import com.yuo.paimeng.menu.SeedBoxContainer;
import com.yuo.paimeng.menu.SynPlatContainer;
import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.recipe.BenchRecipe;
import com.yuo.paimeng.recipe.CookingRecipe;
import com.yuo.paimeng.recipe.PotRecipe;
import com.yuo.paimeng.recipe.SeedBoxRecipe;
import com.yuo.paimeng.recipe.SynPlatRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.subtypes.IIngredientSubtypeInterpreter;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import mezz.jei.api.registration.ISubtypeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;

@mezz.jei.api.JeiPlugin
public class JeiPlugin implements IModPlugin {

    @Override
    public ResourceLocation getPluginUid() {
        return ModUtils.fa("jei_plugin");
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
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        RecipeManager recipeManager = level.getRecipeManager();

        registration.addRecipes(PotRecipeCategory.RECIPE_TYPE, getRecipes(recipeManager, PotRecipe.class));
        registration.addRecipes(BenchRecipeCategory.RECIPE_TYPE, getRecipes(recipeManager, BenchRecipe.class));
        //1.16.5 原实现里 cooking 类别注册的是 pot 配方，这里保持同样行为
        List<CookingRecipe> cookingRecipes = new ArrayList<>(getRecipes(recipeManager, PotRecipe.class));
        registration.addRecipes(CookingRecipeCategory.RECIPE_TYPE, cookingRecipes);
        registration.addRecipes(SynPlatRecipeCategory.RECIPE_TYPE, getRecipes(recipeManager, SynPlatRecipe.class));
        registration.addRecipes(SeedBoxRecipeCategory.RECIPE_TYPE, getRecipes(recipeManager, SeedBoxRecipe.class));
    }

    /**
     * 1.20.1 的 RecipeManager 只有 getAllRecipesFor(RecipeType)，
     * 这里直接按配方实现类过滤，避免依赖其它包里的配方类型字段。
     */
    private static <T> List<T> getRecipes(RecipeManager recipeManager, Class<? extends T> recipeClass) {
        List<T> recipes = new ArrayList<>();
        for (Recipe<?> recipe : recipeManager.getRecipes()) {
            if (recipeClass.isInstance(recipe)) {
                recipes.add(recipeClass.cast(recipe));
            }
        }
        return recipes;
    }

    private List<CookingRecipe> getBenchRecipes(RecipeManager recipeManager){
        List<CookingRecipe> recipes = new ArrayList<>();
        recipes.addAll(getRecipes(recipeManager, PotRecipe.class));
        recipes.addAll(getRecipes(recipeManager, BenchRecipe.class));
        return recipes;
    }

    //注册+号添加
    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        registration.addRecipeTransferHandler(
                new CookingTransferHandler<PotContainer, PotRecipe>(jeiHelpers.getStackHelper(), registration.getTransferHelper(), new PotTransferInfo()),
                PotRecipeCategory.RECIPE_TYPE);
        registration.addRecipeTransferHandler(
                new CookingTransferHandler<BenchContainer, BenchRecipe>(jeiHelpers.getStackHelper(), registration.getTransferHelper(), new BenchTransferInfo()),
                BenchRecipeCategory.RECIPE_TYPE);
        registration.addRecipeTransferHandler(
                new CookingTransferHandler<BenchContainer, CookingRecipe>(jeiHelpers.getStackHelper(), registration.getTransferHelper(), new CookingTransferInfo()),
                CookingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeTransferHandler(
                new CookingTransferHandler<SynPlatContainer, SynPlatRecipe>(jeiHelpers.getStackHelper(), registration.getTransferHelper(), new SynPlatTransferInfo()),
                SynPlatRecipeCategory.RECIPE_TYPE);
        registration.addRecipeTransferHandler(
                new CookingTransferHandler<SeedBoxContainer, SeedBoxRecipe>(jeiHelpers.getStackHelper(), registration.getTransferHelper(), new SeedBoxTransferInfo()),
                SeedBoxRecipeCategory.RECIPE_TYPE);
    }

    //注册机器合成
    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(PMItems.cookingPot.get()), PotRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(PMItems.cookingBench.get()), BenchRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(PMItems.cookingBench.get()), CookingRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(PMItems.syntheticPlatform.get()), SynPlatRecipeCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(PMItems.huazhongxia.get()), SeedBoxRecipeCategory.RECIPE_TYPE);
    }

    //注册物品不同nbt 使用nbt来在jei中显示
    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        //药剂
        registration.registerSubtypeInterpreter(PMItems.drug.get(), (stack, context) -> {
            List<MobEffectInstance> list = PotionUtils.getMobEffects(stack);
            if (list.size() > 0) {
                StringBuilder s = new StringBuilder();
                for (MobEffectInstance instance : list) {
                    s.append(instance.getDescriptionId());
                    s.append(instance.getAmplifier());
                    s.append(instance.getDuration());
                }
                return s.toString();
            }
            return IIngredientSubtypeInterpreter.NONE;
        });
//        //圣遗物
//        registration.registerSubtypeInterpreter(PMItems.xingyunerRelics.get(), (e, u) -> {
//            return RelicsHelper.getTypeForStack(e).getName() + RelicsHelper.getMaxStar(e);
//        });
    }
}
