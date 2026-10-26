package com.yuo.paimeng.jei;

import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.PaiMeng;
import com.yuo.paimeng.recipe.SeedBoxRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class SeedBoxRecipeCategory implements IRecipeCategory<SeedBoxRecipe> {

    public static final ResourceLocation UID = ModUtils.fa( "seed_box");
    //合成配方背景
    public static final ResourceLocation TEXTURE = ModUtils.fa( "textures/gui/seed_box.png");
    //JEI 15 用 RecipeType 取代 1.16 的配方类别 UID 字符串
    public static final RecipeType<SeedBoxRecipe> RECIPE_TYPE = RecipeType.create(PaiMeng.MOD_ID, "seed_box", SeedBoxRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;

    public SeedBoxRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 38,24,100,38); //绘制背景
        this.icon = helper.createDrawableItemStack(new ItemStack(PMItems.huazhongxia.get())); //绘制合成方块
    }

    @Override
    public RecipeType<SeedBoxRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return PMItems.huazhongxia.get().getDescription();
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    //绘制输入输出的物品图标（JEI 15：输入输出在 setRecipe 里一次性填充）
    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, SeedBoxRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        IRecipeSlotBuilder input = builder.addSlot(RecipeIngredientRole.INPUT, 12, 10);
        PotRecipeCategory.addIngredient(input, ingredients, 0);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 70, 10)
                .addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
    }
}
