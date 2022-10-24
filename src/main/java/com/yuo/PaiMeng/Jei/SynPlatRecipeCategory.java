package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Recipes.SynPlatRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SynPlatRecipeCategory implements IRecipeCategory<SynPlatRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(PaiMeng.MOD_ID, "syn_plat");
    //合成配方背景
    public static final ResourceLocation TEXTURE = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/synthetic_platform.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SynPlatRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4,20,168,50); //绘制背景
        this.icon = helper.createDrawableIngredient(new ItemStack(PMItems.syntheticPlatform.get())); //绘制合成方块
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends SynPlatRecipe> getRecipeClass() {
        return SynPlatRecipe.class;
    }

    @Override
    public String getTitle() {
        return PMItems.syntheticPlatform.get().getName().getString();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    //填充输入输出
    @Override
    public void setIngredients(SynPlatRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    //绘制输入输出的物品图标
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SynPlatRecipe recipe, IIngredients ingredients) {
        for (int i = 0; i < recipe.getInputs().size(); i++){
            recipeLayout.getItemStacks().init(i, true, 3 + i * 43, 26);
        }
//        recipeLayout.getItemStacks().init(1, true, 46, 42);
//        recipeLayout.getItemStacks().init(2, true, 89, 42);
        recipeLayout.getItemStacks().init(3, true, 116, 2);
        recipeLayout.getItemStacks().init(4, false, 147, 26);
        recipeLayout.getItemStacks().set(ingredients);
    }
}
