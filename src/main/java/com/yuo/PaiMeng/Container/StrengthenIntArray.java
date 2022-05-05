package com.yuo.PaiMeng.Container;

import net.minecraft.util.IIntArray;

public class StrengthenIntArray implements IIntArray {
    private int left; //左边是否有物品 0：false
    private int right; //右边
    private int LeftExp; //提供经验值
    private int rightExp; //所需经验值
    private int levelUp; //提升的等级

    @Override
    public int get(int index) {
        switch(index) {
            case 0:
                return left;
            case 1:
                return right;
            case 2:
                return LeftExp;
            case 3:
                return rightExp;
            case 4:
                return levelUp;
            default:
                return 0;
        }
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
    public int size() {
        return 5;
    }
}
