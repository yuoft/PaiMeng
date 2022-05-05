package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Recipes.SeedBoxRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class SeedBoxRecipeCategory implements IRecipeCategory<SeedBoxRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(PaiMeng.MOD_ID, "seed_box");
    //合成配方背景
    public static final ResourceLocation TEXTURE = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/seed_box.png");

    private final IDrawable background;
    private final IDrawable icon;

    public SeedBoxRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 38,24,100,38); //绘制背景
        this.icon = helper.createDrawableIngredient(new ItemStack(ItemRegistry.huazhongxia.get())); //绘制合成方块
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public Class<? extends SeedBoxRecipe> getRecipeClass() {
        return SeedBoxRecipe.class;
    }

    @Override
    public String getTitle() {
        return ItemRegistry.huazhongxia.get().getName().getString();
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
    public void setIngredients(SeedBoxRecipe recipe, IIngredients ingredients) {
        ingredients.setInputIngredients(recipe.getIngredients());
        ingredients.setOutput(VanillaTypes.ITEM, recipe.getRecipeOutput());
    }

    //绘制输入输出的物品图标
    @Override
    public void setRecipe(IRecipeLayout recipeLayout, SeedBoxRecipe recipe, IIngredients ingredients) {
        recipeLayout.getItemStacks().init(0, true, 12, 10);
        recipeLayout.getItemStacks().init(1, false, 70, 10);
        recipeLayout.getItemStacks().set(ingredients);
    }
}
