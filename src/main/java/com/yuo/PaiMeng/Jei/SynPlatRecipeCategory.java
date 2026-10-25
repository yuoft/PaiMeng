package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Recipes.SynPlatRecipe;
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

public class SynPlatRecipeCategory implements IRecipeCategory<SynPlatRecipe> {

    public static final ResourceLocation UID = new ResourceLocation(PaiMeng.MOD_ID, "syn_plat");
    //合成配方背景
    public static final ResourceLocation TEXTURE = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/synthetic_platform.png");
    //JEI 15 用 RecipeType 取代 1.16 的配方类别 UID 字符串
    public static final RecipeType<SynPlatRecipe> RECIPE_TYPE = RecipeType.create(PaiMeng.MOD_ID, "syn_plat", SynPlatRecipe.class);

    /** 三个原料槽位的 x 坐标（y 固定 26），与 1.16.5 的 setRecipe 一致 */
    private static final int[] INPUT_X = {3, 46, 89};
    private static final int INPUT_Y = 26;
    /** 催化剂槽位 */
    private static final int CATALYZER_X = 116;
    private static final int CATALYZER_Y = 2;
    /** 输出槽位 */
    private static final int OUTPUT_X = 147;
    private static final int OUTPUT_Y = 26;

    private final IDrawable background;
    private final IDrawable icon;

    public SynPlatRecipeCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(TEXTURE, 4,20,168,50); //绘制背景
        this.icon = helper.createDrawableItemStack(new ItemStack(PMItems.syntheticPlatform.get())); //绘制合成方块
    }

    @Override
    public RecipeType<SynPlatRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return PMItems.syntheticPlatform.get().getDescription();
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
    public void setRecipe(IRecipeLayoutBuilder builder, SynPlatRecipe recipe, IFocusGroup focuses) {
        //SynPlatRecipe.getIngredients() = 3 个原料（不足补空） + 1 个催化剂
        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < INPUT_X.length; i++) {
            IRecipeSlotBuilder slot = builder.addSlot(RecipeIngredientRole.INPUT, INPUT_X[i], INPUT_Y);
            PotRecipeCategory.addIngredient(slot, ingredients, i);
        }
        IRecipeSlotBuilder catalyzer = builder.addSlot(RecipeIngredientRole.INPUT, CATALYZER_X, CATALYZER_Y);
        PotRecipeCategory.addIngredient(catalyzer, ingredients, INPUT_X.length);
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStack(recipe.getResultItem(RegistryAccess.EMPTY));
    }
}
