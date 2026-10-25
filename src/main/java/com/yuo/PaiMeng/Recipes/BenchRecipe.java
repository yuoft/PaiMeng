package com.yuo.PaiMeng.Recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.ShapedRecipe;

import javax.annotation.Nullable;

public class BenchRecipe extends CookingRecipe {

    public BenchRecipe(ResourceLocation idIn, NonNullList<ItemStack> inputIn, ItemStack resultIn){
        super(idIn, inputIn, resultIn);
    }

    public static class Serializer implements RecipeSerializer<BenchRecipe> {

        @Override
        public BenchRecipe fromJson(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
            NonNullList<ItemStack> inputs = NonNullList.create();
            for (JsonElement inputJson : GsonHelper.getAsJsonArray(json, "inputs")) {
                //1.20.1: ShapedRecipe.deserializeItem -> ShapedRecipe.itemStackFromJson（同样支持 count，缺省为 1）
                ItemStack stack = ShapedRecipe.itemStackFromJson(GsonHelper.convertToJsonObject(inputJson, "input"));
                if (!stack.isEmpty()) inputs.add(stack);
            }
            ItemStack result = ShapedRecipe.itemStackFromJson(GsonHelper.getAsJsonObject(json, "result"));
            if (inputs.isEmpty() || inputs.size() > 5){
                //1.20.1 RecipeManager 只捕获 JsonParseException / IllegalArgumentException，这里沿用可被记录的异常类型
                throw new JsonSyntaxException("Recipe is not Error");
            }
            return new BenchRecipe(recipeId, inputs, result);
        }

        @Nullable
        @Override
        public BenchRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int i = buffer.readVarInt();
            NonNullList<ItemStack> list = NonNullList.create();
            for(int j = 0; j < i; ++j) {
                list.add(j, buffer.readItem()); //1.20.1: readItemStack -> readItem
            }
            ItemStack result = buffer.readItem();
            return new BenchRecipe(recipeId, list, result);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, BenchRecipe recipe) {
            buffer.writeVarInt(recipe.inputs.size());
            for(ItemStack stack : recipe.inputs) {
                buffer.writeItem(stack); //1.20.1: writeItemStack -> writeItem
            }
            buffer.writeItem(recipe.result);
        }
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.BENCH.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipeType.BENCH;
    }
}
