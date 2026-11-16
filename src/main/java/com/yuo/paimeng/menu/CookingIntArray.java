package com.yuo.paimeng.menu;

import net.minecraft.world.inventory.ContainerData;

public class CookingIntArray implements ContainerData {
    private int time; //燃烧时间
    private int flag; //合成标识
    private int level; //配方等级

    @Override
    public int get(int index) {
        return switch (index) {
            case 0 -> time;
            case 1 -> flag;
            case 2 -> level;
            default -> 0;
        };
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
    public int getCount() {
        return 3;
    }
}
