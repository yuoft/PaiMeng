package com.yuo.paimeng;

import net.minecraft.ChatFormatting;
import net.minecraft.ResourceLocationException;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

@SuppressWarnings("removal")
public class ModUtils {
    public static ResourceLocation fa(String path) {
        return fa(PaiMeng.MOD_ID, path);
    }

    public static ResourceLocation fa(String id, String path) {
        return new ResourceLocation(id, path);
    }

    public static ResourceLocation def(String path) {
        return new ResourceLocation(path);
    }

    public static ResourceLocation parse(String s){
        return new ResourceLocation(s);
    }

    public static ResourceLocation tayParse(String path) {
        try {
            return new ResourceLocation(path);
        } catch (ResourceLocationException var2) {
            return null;
        }
    }

    public static Component getTierName(int tier, MutableComponent component){
        return switch (tier) {
            case 1 -> component.withStyle(ChatFormatting.WHITE);
            case 2 -> component.withStyle(ChatFormatting.GREEN);
            case 3 -> component.withStyle(ChatFormatting.BLUE);
            case 4 -> component.withStyle(ChatFormatting.DARK_PURPLE);
            case 5 -> component.withStyle(ChatFormatting.GOLD);
            default -> component;
        };
    }
}
