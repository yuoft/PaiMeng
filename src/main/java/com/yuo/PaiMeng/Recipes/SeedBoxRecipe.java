package com.yuo.PaiMeng.Recipes;

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

public class SeedBoxRecipe implements IRecipe<IInventory> {
    private final ItemStack input; //输入
    private final ItemStack result; //输出
    private final ResourceLocation id;

    public SeedBoxRecipe(ResourceLocation idIn, ItemStack inputIn, ItemStack resultIn){
        this.id = idIn;
        this.input = inputIn;
        this.result = resultIn;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SeedBoxRecipe>{

        @Override
        public SeedBoxRecipe read(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
            ItemStack input = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "input"));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            return new SeedBoxRecipe(recipeId, input, result);
        }

        @Nullable
        @Override
        public SeedBoxRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            ItemStack input = buffer.readItemStack();
            ItemStack result = buffer.readItemStack();
            return new SeedBoxRecipe(recipeId, input, result);
        }

        @Override
        public void write(PacketBuffer buffer, SeedBoxRecipe recipe) {
            buffer.writeItemStack(recipe.input);
            buffer.writeItemStack(recipe.result);
        }
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        boolean flag = false;
        ItemStack stack = inv.getStackInSlot(0);
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
        list.add(Ingredient.fromStacks(input));
        return list;
    }

    public NonNullList<ItemStack> getInputs(){
        NonNullList<ItemStack> list = NonNullList.create();
        list.add(input);
        return list;
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
        return RecipeSerializerRegistry.SEED_BOX.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeType.SEED_BOX;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}
