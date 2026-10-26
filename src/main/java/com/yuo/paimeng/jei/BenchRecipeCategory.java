package com.yuo.paimeng.jei;

import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.PaiMeng;
import com.yuo.paimeng.recipe.BenchRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.builder.IRecipeSlotBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class BenchRecipeCategory implements IRecipeCategory<BenchRecipe> {

    public static final ResourceLocation UID = ModUtils.fa( "bench");
    //合成配方背景
    public static final ResourceLocation TEXTURE = ModUtils.fa( "textures/gui/bench.png");
    //JEI 15 用 RecipeType 取代 1.16 的配方类别 UID 字符串
    public static final RecipeType<BenchRecipe> RECIPE_TYPE = RecipeType.create(PaiMeng.MOD_ID, "bench", BenchRecipe.class);

    //输入槽位坐标（与 1.16.5 的 setRecipe 完全一致）
    private static final int[][] INPUT_POSITIONS = {{3, 17}, {33, 17}, {3, 44}, {33, 44}, {86, 11}};
    private static final int OUTPUT_X = 127;
    private static final int OUTPUT_Y = 28;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic fire;

    public BenchRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4,4,168,76); //绘制背景
        this.icon = helper.createDrawableItemStack(new ItemStack(PMItems.cookingBench.get())); //绘制合成方块
        this.fire = helper.createDrawable(TEXTURE, 176, 0, 14,15);
    }

    @Override
    public RecipeType<BenchRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return PMItems.cookingBench.get().getDescription();
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
    public void setRecipe(IRecipeLayoutBuilder builder, BenchRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < INPUT_POSITIONS.length; i++) {
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, INPUT_POSITIONS[i][0], INPUT_POSITIONS[i][1]);
            PotRecipeCategory.addIngredient(slot, ingredients, i);
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
    }

    @Override
    public void draw(BenchRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.fire.draw(guiGraphics, 61, 33);
    }
}
