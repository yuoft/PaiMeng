package com.yuo.paimeng.menu;

import net.minecraft.world.inventory.ContainerData;

public class FoodRecipesIntArray implements ContainerData {
    private int level; //食物等级
    private int exp; //烹饪经验

    @Override
    public int get(int index) {
        return switch (index) {
            case 0 -> level;
            case 1 -> exp;
            default -> 0;
        };
    }

    @Override
    public void set(int index, int value) {
        switch(index) {
            case 0: level = value;break;
            case 1: exp = value;break;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}
