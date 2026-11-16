package com.yuo.paimeng.menu;

import net.minecraft.world.inventory.ContainerData;

/** 强化桌数据*/
public class StrengthenIntArray implements ContainerData {
    private int left; //左边是否有物品 0：false
    private int right; //右边
    private int LeftExp; //提供经验值
    private int rightExp; //所需经验值
    private int levelUp; //提升的等级

    @Override
    public int get(int index) {
        return switch (index) {
            case 0 -> left;
            case 1 -> right;
            case 2 -> LeftExp;
            case 3 -> rightExp;
            case 4 -> levelUp;
            default -> 0;
        };
    }

    @Override
    public void set(int index, int value) {
        switch(index) {
            case 0:
                left = value;
                break;
            case 1:
                right = value;
                break;
            case 2:
                LeftExp = value;
                break;
            case 3:
                rightExp = value;
                break;
            case 4:
                levelUp = value;
                break;
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
