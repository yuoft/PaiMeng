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

public class SynPlatRecipe implements IRecipe<IInventory> {
    private final NonNullList<ItemStack> inputs; //输入
    private final ItemStack catalyzer; //输入
    private final ItemStack result; //输出
    private final ResourceLocation id;

    public SynPlatRecipe(ResourceLocation idIn, NonNullList<ItemStack> inputIn, ItemStack catalyzerIn, ItemStack resultIn){
        this.id = idIn;
        this.inputs = inputIn;
        this.catalyzer = catalyzerIn;
        this.result = resultIn;
    }

    public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<SynPlatRecipe>{

        @Override
        public SynPlatRecipe read(ResourceLocation recipeId, JsonObject json) { //从json中获取信息
            NonNullList<ItemStack> inputs = NonNullList.create();
            for (JsonElement inputJson : JSONUtils.getJsonArray(json, "inputs")) {
                ItemStack stack = ShapedRecipe.deserializeItem((JsonObject) inputJson);
                if (!stack.isEmpty()) inputs.add(stack);
            }
            ItemStack catalyzer = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "catalyzer"));
            ItemStack result = ShapedRecipe.deserializeItem(JSONUtils.getJsonObject(json, "result"));
            if (inputs.isEmpty() || inputs.size() > 4){
                throw new IllegalStateException("Recipe is not Error");
            }
            return new SynPlatRecipe(recipeId, inputs, catalyzer, result);
        }

        @Nullable
        @Override
        public SynPlatRecipe read(ResourceLocation recipeId, PacketBuffer buffer) {
            int i = buffer.readVarInt();
            NonNullList<ItemStack> list = NonNullList.create();
            for(int j = 0; j < i; ++j) {
                list.add(j, buffer.readItemStack());
            }
            ItemStack catalyzer = buffer.readItemStack();
            ItemStack result = buffer.readItemStack();
            return new SynPlatRecipe(recipeId, list, catalyzer, result);
        }

        @Override
        public void write(PacketBuffer buffer, SynPlatRecipe recipe) {
            buffer.writeVarInt(recipe.inputs.size());
            for(ItemStack stack : recipe.inputs) {
                buffer.writeItemStack(stack);
            }
            buffer.writeItemStack(recipe.catalyzer);
            buffer.writeItemStack(recipe.result);
        }
    }

    @Override
    public boolean matches(IInventory inv, World worldIn) {
        int size = RecipeUtils.match(inv.getSizeInventory() - 1, inv, inputs);

        boolean flag = false;
        ItemStack stack = inv.getStackInSlot(inv.getSizeInventory() - 1);
        if (!stack.isEmpty()){
            if (catalyzer.getItem() == stack.getItem() && stack.getCount() >= catalyzer.getCount()){
                flag = true;
            }
        }

        return size == this.inputs.size() && flag;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        for (int i = 0; i < 3; i++){
            if (inputs.size() >= i + 1){
                list.add(Ingredient.fromStacks(inputs.get(i)));
            }else list.add(Ingredient.fromStacks(ItemStack.EMPTY));
        }
//        for (ItemStack input : inputs) {
//            list.add(Ingredient.fromStacks(input));
//        }
        list.add(Ingredient.fromStacks(catalyzer));
        return list;
    }

    public NonNullList<ItemStack> getInputs(){
        NonNullList<ItemStack> list = NonNullList.create();
        for (int i = 0; i < 3; i++){
            if (inputs.size() >= i + 1){
                list.add(inputs.get(i));
            }else list.add(ItemStack.EMPTY);
        }
        list.add(catalyzer);
        return list;
    }

    public ItemStack getCatalyzer(){
        return this.catalyzer;
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
        return RecipeSerializerRegistry.SYN_PLAT.get();
    }

    @Override
    public IRecipeType<?> getType() {
        return ModRecipeType.SYN_PLAT;
    }

    @Override
    public boolean isDynamic() {
        return true;
    }
}
