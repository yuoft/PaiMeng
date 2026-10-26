package com.yuo.paimeng.menu;

import net.minecraft.world.inventory.ContainerData;

//1.20.1: IIntArray -> ContainerData（size() -> getCount()）
public class CookingIntArray implements ContainerData {
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
    public int getCount() {
        return 3;
    }

    /**
     * 1.16.5 旧名称，保留以便其它代码继续使用
     * @return 数据长度
     */
    public int size() {
        return this.getCount();
    }
}
