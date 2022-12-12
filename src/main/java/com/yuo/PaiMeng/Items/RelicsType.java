package com.yuo.PaiMeng.Items;

//圣遗物部位枚举
public enum RelicsType {
    EMPTY(-1, "empty"), //空
    HEAD(0, "head"),
    CUP(1,"cup"),
    CLOCK(2,"clock"),
    FEATHER(3, "feather"),
    FLOWER(4,"flower"),
    EYE(5, "eye"); //神之眼或特殊道具

    public static RelicsType[] ALL = new RelicsType[]{HEAD, CUP, CLOCK, FEATHER, FLOWER, EYE};

    private final int id; //部位id
    private final String name; //名称
    RelicsType(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public static RelicsType getTypeForId(int id){
        for (RelicsType relicsType : ALL) {
            if (relicsType.id == id) return relicsType;
        }
        return EMPTY;
    }
}
