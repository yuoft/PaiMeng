package com.yuo.PaiMeng.Recipes;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

public class RecipeUtils {

    /**
     * 配方匹配输入是否相同
     * @param num 匹配槽位个数（0 ~ num-1）
     * @param inv 待匹配物品容器
     * @param inputs 配方输入
     * @return 已匹配数量
     */
    public static int match(int num, IInventory inv, NonNullList<ItemStack> inputs){
        int size = 0; //以匹配数量
        NonNullList<ItemStack> stacks = NonNullList.create();
        stacks.addAll(inputs);

        for(int j = 0; j < num; ++j) {
            ItemStack itemstack = inv.getStackInSlot(j);
            if (!itemstack.isEmpty()) {
                for (int i =0; i < stacks.size(); i++){
                    if (stacks.get(i).getItem() == itemstack.getItem() && itemstack.getCount() >= stacks.get(i).getCount()){
                        ++size;
                        stacks.remove(i); //删除以匹配项目
                        break;
                    }
                }
            }
        }

        return size;
    }
}
