package com.yuo.paimeng.tiles;

import com.yuo.paimeng.network.CookingPacket;
import com.yuo.paimeng.network.NetWorkHandler;
import com.yuo.paimeng.PaiMeng;
import com.yuo.paimeng.recipe.*;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Collection;

//机器方块公用方法
public class TileUtils {
    /**
     * 获取当前容器内物品的输出
     * @param world 世界
     * @param recipeType 配方类型
     * @param inventory 容器
     * @return 配方输出
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static ItemStack getRecipeOutput(Level world, RecipeType<?> recipeType, Container inventory){
        Collection<Recipe<?>> recipes = world.getRecipeManager().getRecipes();
        for (Recipe<?> recipe : recipes) {
            if (!matchType(recipe, recipeType)) continue;
            Recipe rawRecipe = recipe;
            if (rawRecipe.matches(inventory, world)){
                return rawRecipe.assemble(inventory, world.registryAccess());
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * 合成消耗物品
     * @param tile 方块实体
     * @param count 批量数量
     * @param inputs 配方输入
     */
    public static void shirkItem(BlockEntity tile, NonNullList<ItemStack> inputs, int count){
        if (inputs.size() <= 0) return;
        count = Math.max(count, 1);
        NonNullList<ItemStack> stacks = NonNullList.create();
        if (tile instanceof PotTile) stacks = ((PotTile) tile).items;
        if (tile instanceof BenchTile) stacks = ((BenchTile) tile).items;

        if (stacks.size() <= 0) return;
        for (ItemStack stack : stacks){
            if (!stack.isEmpty()){
                for (ItemStack input : inputs) {
                    if (input.getItem() == stack.getItem()){ //相同物品
                        stack.shrink(input.getCount() * count); //消耗物品
                    }
                }
            }
        }
    }

    /**
     * 获取配方输入列表
     * @param world 世界
     * @param recipeType 合成配方类别
     * @param inventory 物品栏
     * @return 输入列表
     */
    public static NonNullList<ItemStack> getRecipeInputs(Level world, RecipeType<?> recipeType, Container inventory){
        Collection<Recipe<?>> recipes = world.getRecipeManager().getRecipes();
        for (Recipe<?> recipe : recipes) {
            if (!matchType(recipe, recipeType)) continue;
            if (matches(recipe, inventory, world)) {
                if (recipe instanceof PotRecipe)
                    return ((PotRecipe) recipe).getInputs();
                if (recipe instanceof BenchRecipe)
                    return ((BenchRecipe) recipe).getInputs();
                if (recipe instanceof SynPlatRecipe)
                    return ((SynPlatRecipe) recipe).getInputs();
                if (recipe instanceof SeedBoxRecipe)
                    return ((SeedBoxRecipe) recipe).getInputs();
            }
        }
        return NonNullList.create();
    }

    /**
     * 获取变更后的物品栈 （在原基础上增加的）
     * @param world 世界
     * @param recipeType 配方类型
     * @param inventory 物品栏
     * @param count 批量数量
     * @return  产物
     */
    public static ItemStack getTileStack(Level world, RecipeType<?> recipeType, Container inventory, int count, int star){
        ItemStack output = getRecipeOutput(world, recipeType, inventory);
        if (output.isEmpty()) return ItemStack.EMPTY;
        ItemStack stack = inventory.getItem(5);
        output.setCount(count);
        if (stack.isEmpty()) return output;
        stack.grow(output.getCount());
        return stack;
    }

    /**
     * 发送数据包给服务端
     */
    public static void  sendPotPacket(int count, int exp, int satr) {
        exp = count > 1 ? 0 : exp;
        if (count == 0) exp = 0;
        BlockEntity te = PaiMeng.PROXY.getRefrencedTE();
        if (te instanceof PotTile)
            NetWorkHandler.INSTANCE.sendToServer(new CookingPacket(te.getBlockPos(), count, exp, satr));
        if (te instanceof BenchTile){
            NetWorkHandler.INSTANCE.sendToServer(new CookingPacket(te.getBlockPos(), count, exp, satr));
        }
    }

    //配方类别是否匹配（工作台配方同时接受锅配方）
    private static boolean matchType(Recipe<?> recipe, RecipeType<?> recipeType) {
        if (recipeType == ModRecipeType.BENCH)
            return recipe.getType() == ModRecipeType.BENCH || recipe.getType() == ModRecipeType.POT;
        return recipe.getType() == recipeType;
    }

    //配方的输入是否匹配
    @SuppressWarnings({"rawtypes", "unchecked"})
    private static boolean matches(Recipe<?> recipe, Container inventory, Level world) {
        Recipe rawRecipe = recipe;
        return rawRecipe.matches(inventory, world);
    }
}
