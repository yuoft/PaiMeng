package com.yuo.PaiMeng.Recipes;

import com.yuo.PaiMeng.Tiles.BenchTile;
import com.yuo.PaiMeng.Tiles.PotTile;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public abstract class CookingRecipe implements IRecipe<IInventory> {
    public final NonNullList<ItemStack> inputs; //输入
    public final ItemStack result; //输出
    public final ResourceLocation id;

    public CookingRecipe(ResourceLocation idIn, NonNullList<ItemStack> inputIn, ItemStack resultIn){
        this.id = idIn;
        this.inputs = inputIn;
        this.result = resultIn;
    }

//    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ICookingRecipe>{
//
//        @Override
//        public ICookingRecipe read(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
//            NonNullList<ItemStack> inputs = NonNullList.create();
//            for (JsonElement inputJson : JSONUtils.getJsonArray(json, "inputs")) {
//                ItemStack stack = ShapedRecipe.deserializeItem((JsonObject) inputJson);
//                if (!stack.isEmpty()) inputs.add(stack);
//            }
//            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
//            if (inputs.isEmpty() || inputs.size() > 4){
//                throw new IllegalStateException("Recipe is not Error");
//            }
//            return new ICookingRecipe(recipeId, inputs, result);
//        }
//
//        @Nullable
//        @Override
//        public ICookingRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
//            int i = buffer.readVarInt();
//            NonNullList<ItemStack> list = NonNullList.create();
//            for(int j = 0; j < i; ++j) {
//                list.add(j, buffer.readItemStack());
//            }
//            ItemStack result = buffer.readItemStack();
//            return new ICookingRecipe(recipeId, list, result);
//        }
//
//        @Override
//        public void write(PacketBuffer buffer, ICookingRecipe recipe) {
//            buffer.writeVarInt(recipe.inputs.size());
//            for(ItemStack stack : recipe.inputs) {
//                buffer.writeItemStack(stack);
//            }
//            buffer.writeItemStack(recipe.result);
//        }
//    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        if (inv instanceof BenchTile){
            int size = RecipeUtils.match(inv.getSizeInventory() - 2, inv, inputs);
            return size == this.inputs.size() && size == ((BenchTile) inv).getInputSize();
        }
        if (inv instanceof PotTile){
            int size = RecipeUtils.match(inv.getSizeInventory() - 1, inv, inputs);
            return size == this.inputs.size() && size == ((PotTile) inv).getInputSize();
        }
        return false;
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

//    @Override
//    public IRecipeSerializer<?> getSerializer() {
//        return RecipeSerializerRegistry.POT.get();
//    }
//
//    @Override
//    public IRecipeType<?> getType() {
//        return ModRecipeType.POT;
//    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}
