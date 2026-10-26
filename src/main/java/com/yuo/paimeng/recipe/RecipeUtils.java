package com.yuo.paimeng.recipe;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

public class RecipeUtils {

    /**
     * 配方匹配输入是否相同
     * @param num 匹配槽位个数（0 ~ num-1）
     * @param inv 待匹配物品容器
     * @param inputs 配方输入
     * @return 已匹配数量
     */
    public static int match(int num, Container inv, NonNullList<ItemStack> inputs){
        int size = 0; //以匹配数量
        NonNullList<ItemStack> stacks = NonNullList.create();
        stacks.addAll(inputs);

        //1.20.1: getSizeInventory -> getContainerSize, getStackInSlot -> getItem
        //槽位数取两者较小值，防止容器实际槽位少于配方需要的槽位时越界
        int slots = Math.min(num, inv == null ? 0 : inv.getContainerSize());
        for(int j = 0; j < slots; ++j) {
            ItemStack itemstack = inv.getItem(j);
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
