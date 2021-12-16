package com.yuo.PaiMeng.Tiles;

import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.NetWork.PotPacket;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Recipes.PotRecipe;
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
        Set<IRecipe<?>> recipes = world.getRecipeManager().getRecipes().stream().filter(recipe ->
                recipe.getType() == recipeType).collect(Collectors.toSet());
        for (IRecipe recipe : recipes) {
            if (recipe.matches(inventory, world)){
                return recipe.getCraftingResult(inventory);
            }
        }
        return ItemStack.EMPTY;
    }

    /**
     * 合成消耗物品
     * @param tile
     * @param inputs 配方输入
     */
    public static void shirkItem(TileEntity tile, NonNullList<ItemStack> inputs){
        if (inputs.size() <= 0) return;
        NonNullList<ItemStack> stacks = NonNullList.create();
        if (tile instanceof PotTile) stacks = ((PotTile) tile).items;
//        if (tile instanceof PotTile) stacks = ((PotTile) tile).items;
        if (stacks.size() <= 0) return;
        for (ItemStack stack : stacks){
            if (!stack.isEmpty()){
                for (ItemStack input : inputs) {
                    if (input.getItem() == stack.getItem()){ //相同物品
                        stack.shrink(input.getCount()); //消耗物品
                    }
                }
            }
        }
    }

    /**
     * 获取配方输入列表
     * @param world
     * @param recipeType
     * @param inventory
     * @return
     */
    public static NonNullList<ItemStack> getRecipeInputs(World world, IRecipeType recipeType, IInventory inventory){
        Set<IRecipe<?>> recipes = world.getRecipeManager().getRecipes().stream().filter(recipe ->
                recipe.getType() == recipeType).collect(Collectors.toSet());
        for (IRecipe recipe : recipes) {
            if (recipe.matches(inventory, world)){
                if (recipe instanceof PotRecipe)
                return ((PotRecipe) recipe).getInputs();
            }
        }
        return NonNullList.create();
    }

    /**
     * 获取变更后的物品栈 （在原基础上增加的）
     * @param world
     * @param recipeType
     * @param inventory
     * @return
     */
    public static ItemStack getTileStack(World world, IRecipeType recipeType,IInventory inventory){
        ItemStack output = getRecipeOutput(world, recipeType, inventory);
        if (output.isEmpty()) return ItemStack.EMPTY;
        ItemStack stack = inventory.getStackInSlot(4);
        if (stack.isEmpty()) return output;
        stack.grow(output.getCount());
        return stack;
    }

    /**
     * 发送数据包给服务端
     */
    public static void  sendPotPacket() {
        TileEntity te = PaiMeng.PROXY.getRefrencedTE();
        if (te instanceof PotTile)
            NetWorkHandler.INSTANCE.sendToServer(new PotPacket(te.getPos()));
    }
}
