package com.yuo.PaiMeng.WorldGen;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * 各树苗对应的 ConfiguredFeature。
 * key 与 WorldEventHandler#registerFeatures 里注册的 ConfiguredFeature 一一对应。
 */
public class TreeInit {

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(PaiMeng.MOD_ID, name));
    }

    public static final TreeSpawner APPLE_TREE = new TreeSpawner(key("apple_tree"));
    public static final TreeSpawner SUN_APPLE_TREE = new TreeSpawner(key("sun_apple_tree"));
    public static final TreeSpawner PURPLE_APPLE_TREE = new TreeSpawner(key("purple_apple_tree"));
    public static final TreeSpawner ZAOYE_TREE = new TreeSpawner(key("zaoye_tree"));
    public static final TreeSpawner PAOPAOJU_TREE = new TreeSpawner(key("paopaoju_tree"));
}
