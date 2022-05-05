package com.yuo.PaiMeng.Container;

import net.minecraft.util.IIntArray;

public class FoodRecipesIntArray implements IIntArray {
    private int level;
    private int exp;

    @Override
    public int get(int index) {
        switch(index) {
            case 0:
                return level;
            case 1:
                return exp;
            default:
                return 0;
        }
    }

    @Override
    public void set(int index, int value) {
        switch(index) {
            case 0:
                level = value;
                break;
            case 1:
                exp = value;
                break;
        }
    }

    @Override
    public int size() {
        return 2;
    }
}
