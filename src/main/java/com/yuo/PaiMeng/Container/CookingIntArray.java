package com.yuo.PaiMeng.Container;

import net.minecraft.util.IIntArray;

public class CookingIntArray implements IIntArray {
    private int time; //燃烧时间
    private int flag; //合成标识
    private int level; //配方等级

    @Override
    public int get(int index) {
        switch(index) {
            case 0:
                return time;
            case 1:
                return flag;
            case 2:
                return level;
            default:
                return 0;
        }
    }

    @Override
    public void set(int index, int value) {
        switch(index) {
            case 0:
                time = value;
                break;
            case 1:
                flag = value;
                break;
            case 2:
                level = value;
                break;
        }
    }

    @Override
    public int size() {
        return 3;
    }
}
