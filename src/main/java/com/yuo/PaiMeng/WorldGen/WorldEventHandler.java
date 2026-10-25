package com.yuo.PaiMeng.WorldGen;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

/**
 * 世界生成总入口（1.20.1）。
 * <p>
 * 1.16.5 里通过 {@code BiomeLoadingEvent} 往每个群系塞 ConfiguredFeature；
 * 1.20.1 该事件已删除，改为：
 * <ol>
 *   <li>在 {@link RegisterEvent} 里注册 ConfiguredFeature / PlacedFeature；</li>
 *   <li>用 {@code data/paimeng/forge/biome_modifier/*.json} 把 PlacedFeature 注入生物群系。</li>
 * </ol>
 * 主控在 {@code PaiMeng.java} 里通过 {@code modEventBus.addListener(WorldEventHandler::registerFeatures)} 挂载。
 */
public class WorldEventHandler {

    //树（森林）
    public static final ResourceKey<PlacedFeature> TREE_APPLE_FOREST = treeKey("tree_apple_forest");
    public static final ResourceKey<PlacedFeature> TREE_PURPLE_APPLE_FOREST = treeKey("tree_purple_apple_forest");
    public static final ResourceKey<PlacedFeature> TREE_ZAOYE_FOREST = treeKey("tree_zaoye_forest");
    public static final ResourceKey<PlacedFeature> TREE_PAOPAOJU_FOREST = treeKey("tree_paopaoju_forest");
    public static final ResourceKey<PlacedFeature> TREE_WITHERED_FOREST = treeKey("tree_withered_forest");

    private static ResourceKey<PlacedFeature> treeKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(PaiMeng.MOD_ID, name));
    }

    private static ResourceLocation id(String name) {
        return new ResourceLocation(PaiMeng.MOD_ID, name);
    }

    /**
     * RegisterEvent 监听入口
     */
    public static void registerFeatures(RegisterEvent event) {
        if (event.getRegistryKey().equals(Registries.CONFIGURED_FEATURE)) {
            //树苗生长用的 ConfiguredFeature（TreeInit 的 key 必须与这里一致）
            configured(event, "apple_tree", FeatureInit.APPLE_TREE);
            configured(event, "sun_apple_tree", FeatureInit.SUN_APPLE_TREE);
            configured(event, "purple_apple_tree", FeatureInit.PURPLE_APPLE_TREE);
            configured(event, "zaoye_tree", FeatureInit.ZAOYE_TREE);
            configured(event, "paopaoju_tree", FeatureInit.PAOPAOJU_TREE);
            configured(event, "withered_tree", FeatureInit.WITHERED_TREE);
        } else if (event.getRegistryKey().equals(Registries.PLACED_FEATURE)) {
            WorldOreGen.register(event);
            WorldPlantGen.register(event);
            //树：森林里稀疏生成
            tree(event, TREE_APPLE_FOREST, FeatureInit.APPLE_TREE, 50);
            tree(event, TREE_PURPLE_APPLE_FOREST, FeatureInit.PURPLE_APPLE_TREE, 50);
            tree(event, TREE_ZAOYE_FOREST, FeatureInit.ZAOYE_TREE, 50);
            tree(event, TREE_PAOPAOJU_FOREST, FeatureInit.PAOPAOJU_TREE, 50);
            tree(event, TREE_WITHERED_FOREST, FeatureInit.WITHERED_TREE, 200);
        }
    }

    private static void configured(RegisterEvent event, String name, RegistryObject<? extends Feature<?>> feature) {
        event.register(Registries.CONFIGURED_FEATURE, id(name), () -> configuredOf(feature));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static ConfiguredFeature<?, ?> configuredOf(RegistryObject<? extends Feature<?>> feature) {
        return new ConfiguredFeature((Feature) feature.get(), NoneFeatureConfiguration.INSTANCE);
    }

    private static void tree(RegisterEvent event, ResourceKey<PlacedFeature> key,
                             RegistryObject<? extends Feature<?>> feature, int rarity) {
        event.register(Registries.PLACED_FEATURE, key.location(), () -> new PlacedFeature(
                Holder.direct(configuredOf(feature)),
                List.of(
                        CountPlacement.of(1),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        RarityFilter.onAverageOnceEvery(rarity),
                        BiomeFilter.biome()
                )));
    }
}
