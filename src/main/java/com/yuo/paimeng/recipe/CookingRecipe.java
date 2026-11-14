package com.yuo.paimeng.recipe;

import com.yuo.paimeng.tiles.BenchTile;
import com.yuo.paimeng.tiles.PotTile;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

/**
 * 锅 / 工作台 配方基类
 */
public abstract class CookingRecipe implements Recipe<Container> {
    public final NonNullList<ItemStack> inputs; //输入
    public final ItemStack result; //输出
    public final ResourceLocation id;

    public CookingRecipe(ResourceLocation idIn, NonNullList<ItemStack> inputIn, ItemStack resultIn){
        this.id = idIn;
        this.inputs = inputIn;
        this.result = resultIn;
    }

    @Override
    public boolean matches(Container inv, Level worldIn) {
        if (inv instanceof BenchTile){
            //工作台：0~4 为食材，5 为产出，6 为燃料
            int size = RecipeUtils.match(inv.getContainerSize() - 2, inv, inputs);
            return size == this.inputs.size() && size == ((BenchTile) inv).getInputSize();
        }
        if (inv instanceof PotTile){
            //锅：0~4 为食材，5 为产出
            int size = RecipeUtils.match(inv.getContainerSize() - 1, inv, inputs);
            return size == this.inputs.size() && size == ((PotTile) inv).getInputSize();
        }
        return false;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        for (ItemStack input : inputs) {
            list.add(Ingredient.of(input));
        }

        return list;
    }

    public NonNullList<ItemStack> getInputs(){
        return this.inputs;
    }

    /**
     * 1.20.1 标准产出方法
     */
    @Override
    public ItemStack assemble(Container inv, RegistryAccess registryAccess) {
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

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    /**
     * 不在配方书显示，不遵循标准的“JSON定义输入输出”模式时
     */
    @Override
    public boolean isSpecial() {
        return true;
    }
}
