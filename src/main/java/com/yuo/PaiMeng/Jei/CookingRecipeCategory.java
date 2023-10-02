package com.yuo.PaiMeng.Jei;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Recipes.CookingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CookingRecipeCategory implements IRecipeCategory<CookingRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(PaiMeng.MOD_ID, "cooking");
    //合成配方背景
    public static final ResourceLocation TEXTURE = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/bench.png");

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic fire;

    public CookingRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4,4,168,76); //绘制背景
        this.icon = helper.createDrawableIngredient(new ItemStack(PMItems.cookingBench.get())); //绘制合成方块
        this.fire = helper.createDrawable(TEXTURE, 176, 0, 14,15);
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends CookingRecipe> getRecipeClass() {
        return CookingRecipe.class;
    }

    @Override
    public String getTitle() {
        return PMItems.cookingBench.get().getName().getString();
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
    public void setIngredients(CookingRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    //绘制输入输出的物品图标
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, CookingRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 3, 17);
        recipeLayout.getItemStacks().init(1, true, 33, 17);
        recipeLayout.getItemStacks().init(2, true, 3, 44);
        recipeLayout.getItemStacks().init(3, true, 33, 44);
        recipeLayout.getItemStacks().init(4, true, 86, 11);
        recipeLayout.getItemStacks().init(5, false, 127, 28);
        recipeLayout.getItemStacks().set(ingredients);
    }

    @Override
    public void draw(CookingRecipe recipe, MatrixStack matrixStack, double mouseX, double mouseY) {
        this.fire.draw(matrixStack, 61, 33);
    }
}
