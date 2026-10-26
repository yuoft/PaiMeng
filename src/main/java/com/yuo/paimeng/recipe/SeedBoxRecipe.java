package com.yuo.paimeng.recipe;

import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class SeedBoxRecipe implements Recipe<Container> {
    private final ItemStack input; //输入
    private final ItemStack result; //输出
    private final ResourceLocation id;

    public SeedBoxRecipe(ResourceLocation idIn, ItemStack inputIn, ItemStack resultIn){
        this.id = idIn;
        this.input = inputIn;
        this.result = resultIn;
    }

    public static class Serializer implements RecipeSerializer<SeedBoxRecipe> {

        @Override
        public SeedBoxRecipe fromJson(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
            //1.20.1: ShapedRecipe.deserializeItem -> ShapedRecipe.itemStackFromJson（同样支持 count，缺省为 1）
            ItemStack input = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "input"));
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            return new SeedBoxRecipe(recipeId, input, result);
        }

        @Nullable
        @Override
        public SeedBoxRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            ItemStack input = buffer.readItem(); //1.20.1: readItemStack -> readItem
            ItemStack result = buffer.readItem();
            return new SeedBoxRecipe(recipeId, input, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, SeedBoxRecipe recipe) {
            buffer.writeItem(recipe.input); //1.20.1: writeItemStack -> writeItem
            buffer.writeItem(recipe.result);
        }
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        boolean flag = false;
        ItemStack stack = inv.getItem(0); //1.20.1: getStackInSlot -> getItem
        if (!stack.isEmpty()){
            if (input.getItem() == stack.getItem() && stack.getCount() >= input.getCount()){
                flag = true;
            }
        }

        return flag;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(Ingredient.of(input)); //1.20.1: Ingredient.fromStacks -> Ingredient.of
        return list;
    }

    public NonNullList<ItemStack> getInputs(){
        NonNullList<ItemStack> list = NonNullList.create();
        list.add(input);
        return list;
    }

    /**
     * 1.20.1 标准产出方法
     */
    @Override
    public ItemStack assemble(Container inv, RegistryAccess registryAccess) {
        return this.result.copy();
    }

    /**
     * 1.20.1 便捷别名（与 assemble 等价），供 Container / JEI 使用
     */
    public ItemStack craft(Container inv, RegistryAccess registryAccess) {
        return this.assemble(inv, registryAccess);
    }

    /**
     * 1.16.5 旧名，保留给 Container 调用
     */
    public ItemStack getCraftingResult(Container inv) {
        return this.result.copy();
    }

    /**
     * 配方产物（副本，避免外部修改共享栈）
     */
    public ItemStack getResult() {
        return this.result.copy();
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result.copy();
    }

    /**
     * 1.16.5 旧名，保留兼容
     */
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.SEED_BOX.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeType.SEED_BOX;
    }

    //1.16.5 的 isDynamic 在 1.20.1 叫 isSpecial
    @Override
    public boolean isSpecial() {
        return true;
    }
}
