package com.yuo.paimeng.menu;

import net.minecraft.world.inventory.ContainerData;

//1.20.1: IIntArray -> ContainerData（size() -> getCount()）
public class FoodRecipesIntArray implements ContainerData {
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
    public int getCount() {
        return 2;
    }

    /**
     * 1.16.5 旧名称，保留以便其它代码继续使用
     * @return 数据长度
     */
    public int size() {
        return this.getCount();
    }
}
