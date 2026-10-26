package com.yuo.paimeng.worldgen;

import com.yuo.paimeng.ModUtils;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

/**
 * 各树苗对应的 ConfiguredFeature。
 * key 与 datagen 产出的 data/paimeng/worldgen/configured_feature/&lt;name&gt;.json 一一对应
 * （见 {@code com.yuo.PaiMeng.datagen.PMWorldGenData#configured()}）。
 */
public class TreeInit {

    private static ResourceKey<ConfiguredFeature<?, ?>> key(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ModUtils.fa( name));
    }

    public static final TreeSpawner APPLE_TREE = new TreeSpawner(key("apple_tree"));
    public static final TreeSpawner SUN_APPLE_TREE = new TreeSpawner(key("sun_apple_tree"));
    public static final TreeSpawner PURPLE_APPLE_TREE = new TreeSpawner(key("purple_apple_tree"));
    public static final TreeSpawner ZAOYE_TREE = new TreeSpawner(key("zaoye_tree"));
    public static final TreeSpawner PAOPAOJU_TREE = new TreeSpawner(key("paopaoju_tree"));
}
