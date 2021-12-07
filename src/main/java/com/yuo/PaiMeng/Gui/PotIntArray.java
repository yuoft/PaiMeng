package com.yuo.PaiMeng.Gui;

import net.minecraft.util.IIntArray;

public class PotIntArray implements IIntArray {
    private int time; //燃烧时间
    private int posX; //坐标x
    private int posY;
    private int posZ;

    @Override
    public int get(int index) {
        switch(index) {
            case 0:
                return time;
            case 1:
                return posX;
            case 2:
                return posY;
            case 3:
                return posZ;
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
                posX = value;
                break;
            case 2:
                posY = value;
                break;
            case 3:
                posZ = value;
                break;
        }
    }

    @Override
    public int size() {
        return 1;
    }
}
