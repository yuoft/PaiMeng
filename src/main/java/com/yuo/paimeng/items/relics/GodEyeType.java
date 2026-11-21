package com.yuo.paimeng.items.relics;

import net.minecraft.ChatFormatting;

public enum GodEyeType {

    /**
     * 七种元素 风，岩，雷，草，水，火，冰
     */
    EMPTY("empty", ChatFormatting.BLACK),
    WIND("wind", ChatFormatting.AQUA),
    STONE("stone", ChatFormatting.GOLD),
    THUNDER("thunder", ChatFormatting.LIGHT_PURPLE),
    GRASS("grass", ChatFormatting.GREEN),
    WATER("water", ChatFormatting.BLUE),
    FIRE("fire", ChatFormatting.RED),
    ICE("ice", ChatFormatting.WHITE);

    private final String id;
    private final ChatFormatting color;

    GodEyeType(String id, ChatFormatting color) {
        this.id = id;
        this.color = color;
    }

    public String getId() {
        return id;
    }

    public ChatFormatting getColor() {
        return color;
    }

}
