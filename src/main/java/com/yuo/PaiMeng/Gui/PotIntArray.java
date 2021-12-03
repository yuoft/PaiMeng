package com.yuo.PaiMeng.Gui;

import net.minecraft.util.IIntArray;

public class PotIntArray implements IIntArray {
    private int time; //燃烧时间

    @Override
    public int get(int index) {
        switch(index) {
            case 0:
                return time;
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
        }
    }

    @Override
    public int size() {
        return 1;
    }
}
