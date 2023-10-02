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
