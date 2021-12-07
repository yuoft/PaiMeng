package com.yuo.PaiMeng.Items.Food;

import com.yuo.PaiMeng.tab.ModGroup;
import net.minecraft.item.Food;
import net.minecraft.item.Item;

//食物物品
public class OrdinaryFood extends Item {
    public OrdinaryFood(Food food) {
        super(new Properties().food(food).group(ModGroup.PaiMeng));
    }
}