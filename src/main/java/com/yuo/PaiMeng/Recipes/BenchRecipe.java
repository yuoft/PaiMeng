package com.yuo.PaiMeng.Recipes;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.*;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;

public class BenchRecipe implements IRecipe<IInventory> {
    private final NonNullList<ItemStack> inputs; //输入
    private final ItemStack result; //输出
    private final ResourceLocation id;

    public BenchRecipe(ResourceLocation idIn, NonNullList<ItemStack> inputIn, ItemStack resultIn){
        this.id = idIn;
        this.inputs = inputIn;
        this.result = resultIn;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<BenchRecipe>{

        @Override
        public BenchRecipe read(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
            NonNullList<ItemStack> inputs = NonNullList.create();
            for (JsonElement inputJson : JSONUtils.getJsonArray(json, "inputs")) {
                ItemStack stack = ShapedRecipe.deserializeItem((JsonObject) inputJson);
                if (!stack.isEmpty()) inputs.add(stack);
            }
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            String type = JSONUtils.getString(json, "type");
            if (!type.equals("paimeng:bench")){
                throw new IllegalStateException("Type is not found");
            }
            if (inputs.isEmpty() || inputs.size() > 4){
                throw new IllegalStateException("Recipe is not Error");
            }
            return new BenchRecipe(recipeId, inputs, result);
        }

        @Nullable
        @Override
        public BenchRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            int i = buffer.readVarInt();
            NonNullList<ItemStack> list = NonNullList.create();
            for(int j = 0; j < i; ++j) {
                list.add(j, buffer.readItemStack());
            }
            ItemStack result = buffer.readItemStack();
            String type = buffer.readString();
            if (!type.equals("paimeng:bench")){
                throw new IllegalStateException("Type is not found");
            }
            return new BenchRecipe(recipeId, list, result);
        }

        @Override
        public void write(PacketBuffer buffer, BenchRecipe recipe) {
            buffer.writeVarInt(recipe.inputs.size());
            for(ItemStack stack : recipe.inputs) {
                buffer.writeItemStack(stack);
            }
            buffer.writeItemStack(recipe.result);
        }
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        int size = RecipeUtils.match(inv.getSizeInventory() - 2, inv, inputs);
        return size == this.inputs.size();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        for (ItemStack input : inputs) {
            list.add(Ingredient.fromStacks(input));
        }

        return list;
    }

    public NonNullList<ItemStack> getInputs(){
        return this.inputs;
    }

    @Override
    public ItemStack getCraftingResult(IInventory inv) {
        return this.result.copy();
    }

    @Override
    public boolean canFit(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.result;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return RecipeSerializerRegistry.BENCH.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeType.BENCH;
    }

}
