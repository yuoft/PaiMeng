package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Recipes.PotRecipe;
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

import java.util.Arrays;

public class PotRecipeCategory implements IRecipeCategory<PotRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(PaiMeng.MOD_ID, "pot");
    //合成配方背景
    public static final ResourceLocation TEXTURE = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/pot.png");
    //JEI 15 用 RecipeType 取代 1.16 的配方类别 UID 字符串
    public static final RecipeType<PotRecipe> RECIPE_TYPE = RecipeType.create(PaiMeng.MOD_ID, "pot", PotRecipe.class);

    //输入槽位坐标（与 1.16.5 的 setRecipe 完全一致）
    private static final int[][] INPUT_POSITIONS = {{3, 17}, {33, 17}, {3, 44}, {33, 44}, {86, 11}};
    private static final int OUTPUT_X = 127;
    private static final int OUTPUT_Y = 28;

    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawableStatic fire;

    public PotRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4,4,168,76); //绘制背景
        this.icon = helper.createDrawableItemStack(new ItemStack(PMItems.cookingPot.get())); //绘制合成方块
        this.fire = helper.createDrawable(TEXTURE, 176, 0, 14,15);
    }

    @Override
    public RecipeType<PotRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return PMItems.cookingPot.get().getDescription();
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
    public void setRecipe(IRecipeLayoutBuilder builder, PotRecipe recipe, IFocusGroup focuses) {
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < INPUT_POSITIONS.length; i++) {
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, INPUT_POSITIONS[i][0], INPUT_POSITIONS[i][1]);
            addIngredient(slot, ingredients, i);
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
    }

    @Override
    public void draw(PotRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.fire.draw(guiGraphics, 61, 33);
    }

    /** 把第 index 个原料放进槽位；原料列表不足时留空槽（与 1.16.5 行为一致） */
    static void addIngredient(IRecipeSlotBuilder slot, NonNullList<Ingredient> ingredients, int index) {
        if (index < 0 || index >= ingredients.size()) return;
        Ingredient ingredient = ingredients.get(index);
        if (ingredient == null) return;
        ItemStack[] items = ingredient.getItems();
        if (items.length > 0) slot.addItemStacks(Arrays.asList(items));
    }
}
