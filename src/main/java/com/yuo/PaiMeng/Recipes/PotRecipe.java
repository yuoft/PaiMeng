package com.yuo.PaiMeng.Recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class PotRecipe extends CookingRecipe {

    public PotRecipe(ResourceLocation idIn, NonNullList<ItemStack> inputIn, ItemStack resultIn){
        super(idIn, inputIn, resultIn);
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<PotRecipe>{

        @Override
        public PotRecipe read(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
            NonNullList<ItemStack> inputs = NonNullList.create();
            for (JsonElement inputJson : JSONUtils.getJsonArray(json, "inputs")) {
                ItemStack stack = ShapedRecipe.deserializeItem((JsonObject) inputJson);
                if (!stack.isEmpty()) inputs.add(stack);
            }
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            if (inputs.isEmpty() || inputs.size() > 4){
                throw new IllegalStateException("Recipe is not Error");
            }
//            Item item = result.getItem();
//            if (item instanceof PaiMengFood){
//                PaiMengFood food = (PaiMengFood) item;
//                if (food.getLEVEL() > 3) return new BenchRecipe(recipeId, inputs, result);
//                else
                    return new PotRecipe(recipeId, inputs, result);
//            }else throw new IllegalStateException("Recipe Error");
        }

        @Nullable
        @Override
        public PotRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            int i = buffer.readVarInt();
            NonNullList<ItemStack> list = NonNullList.create();
            for(int j = 0; j < i; ++j) {
                list.add(j, buffer.readItemStack());
            }
            ItemStack result = buffer.readItemStack();
            return new PotRecipe(recipeId, list, result);
        }

        @Override
        public void write(PacketBuffer buffer, PotRecipe recipe) {
            buffer.writeVarInt(recipe.inputs.size());
            for(ItemStack stack : recipe.inputs) {
                buffer.writeItemStack(stack);
            }
            buffer.writeItemStack(recipe.result);
        }
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.POT.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeType.POT;
    }

}
