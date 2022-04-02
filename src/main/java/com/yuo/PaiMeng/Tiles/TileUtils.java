package com.yuo.PaiMeng.Tiles;

import com.yuo.PaiMeng.NetWork.CookingPacket;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Recipes.*;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.Set;
import java.util.stream.Collectors;

//机器方块公用方法
public class TileUtils {
    /**
     * 获取当前容器内物品的输出
     * @param world 世界
     * @param recipeType 配方类型
     * @param inventory 容器
     * @return 配方输出
     */
    public static ItemStack getRecipeOutput(World world, IRecipeType recipeType, IInventory inventory){
        Set<IRecipe<?>> recipes = world.getRecipeManager().getRecipes().stream().filter(recipe ->{
                   if (recipeType == ModRecipeType.BENCH)
                       return recipe.getType() ==recipeType || recipe.getType() == ModRecipeType.POT;
                   else return recipe.getType() == recipeType;
                }).collect(Collectors.toSet());
        for (IRecipe recipe : recipes) {
            if (recipe.matches(inventory, world)){
                return recipe.getCraftingResult(inventory);
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
    public static void shirkItem(TileEntity tile, NonNullList<ItemStack> inputs, int count){
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
    public static NonNullList<ItemStack> getRecipeInputs(World world, IRecipeType recipeType, IInventory inventory){
        Set<IRecipe<?>> recipes = world.getRecipeManager().getRecipes().stream().filter(recipe ->{
            if (recipeType == ModRecipeType.BENCH)
                return recipe.getType() ==recipeType || recipe.getType() == ModRecipeType.POT;
            else return recipe.getType() == recipeType;
        }).collect(Collectors.toSet());
        for (IRecipe recipe : recipes) {
            if (recipe.matches(inventory, world)){
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
    public static ItemStack getTileStack(World world, IRecipeType recipeType,IInventory inventory, int count, int star){
        ItemStack output = getRecipeOutput(world, recipeType, inventory);
        if (output.isEmpty()) return ItemStack.EMPTY;
        ItemStack stack = inventory.getStackInSlot(4);
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
        TileEntity te = PaiMeng.PROXY.getRefrencedTE();
        if (te instanceof PotTile)
            NetWorkHandler.INSTANCE.sendToServer(new CookingPacket(te.getPos(), count, exp, satr));
        if (te instanceof BenchTile){
            NetWorkHandler.INSTANCE.sendToServer(new CookingPacket(te.getPos(), count, exp, satr));
        }
    }
}
