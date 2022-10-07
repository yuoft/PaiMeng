package com.yuo.PaiMeng.WorldGen;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.Biomes;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.blockplacer.SimpleBlockPlacer;
import net.minecraft.world.gen.blockstateprovider.SimpleBlockStateProvider;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureSpreadConfig;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;

/**
 * 植物生成
 */
public class WorldPlantGen {

    public static void generatePlants(final BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        Biome.Category category = event.getCategory();

        RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());
        //获取生物群系列表
        if (key.equals(Biomes.BAMBOO_JUNGLE) || key.equals(Biomes.BAMBOO_JUNGLE_HILLS)) { //竹林
            addFeaturePlainFlower(generation, BlockRegistry.zhusunPlant.get().getDefaultState(), 6, 2);
        }
        if (key.equals(Biomes.CRIMSON_FOREST)) { //绯红森林
            addFeatureNether(generation, BlockRegistry.lieyanhuaHuaruiPlant.get().getDefaultState(), Blocks.CRIMSON_NYLIUM.getDefaultState(), 1);
        }
        if (key.equals(Biomes.FOREST)) { //森林
            addFeatureWallPlant(generation, BlockRegistry.moguWallPlant.get().getDefaultState(), 5, 5);
            addFeatureWallPlant(generation, BlockRegistry.mufengMoguWallPlant.get().getDefaultState(), 5, 5);
        }
        if (key.equals(Biomes.WARPED_FOREST)){ //诡异森林
            addFeatureNether(generation, BlockRegistry.youdengxunPlant.get().getDefaultState(), Blocks.WARPED_NYLIUM.getDefaultState(), 1);
            addFeatureWallPlant(generation, BlockRegistry.youdengxunWallPlant.get().getDefaultState(), 5, 5);
        }
        if (key.equals(Biomes.DARK_FOREST) || key.equals(Biomes.DARK_FOREST_HILLS)){ //黑森林
            addFeaturePlainFlower(generation, BlockRegistry.xiaodengcaoPlant.get().getDefaultState(), 16, 3);
        }
        if (key.equals(Biomes.BIRCH_FOREST) || key.equals(Biomes.BIRCH_FOREST_HILLS) || key.equals(Biomes.TALL_BIRCH_FOREST)
                || key.equals(Biomes.TALL_BIRCH_HILLS)){ //白桦林
            addFeaturePlainFlower(generation, BlockRegistry.gougouguoPlant.get().getDefaultState(), 3, 1);
        }
        if (key.equals(Biomes.FOREST)){ //森林
            addFeaturePlainFlower(generation, BlockRegistry.luoluomeiPlant.get().getDefaultState(), 3, 1);
        }
        if (key.equals(Biomes.END_BARRENS) || key.equals(Biomes.END_MIDLANDS)){ //末地荒岛 中型末地岛屿
            addFeaturePlainFlower(generation, BlockRegistry.tianyunCaoshiPlant.get().getDefaultState(), 1, 1);
        }
        if (key.equals(Biomes.SMALL_END_ISLANDS)){ //小型末地岛屿
            addFeaturePlainFlower(generation, BlockRegistry.mingcaoPlant.get().getDefaultState(), 1, 3);
        }
        if (key.equals(Biomes.FLOWER_FOREST)){ //繁花森林
            addFeaturePlainFlower(generation, BlockRegistry.liuliBaihePlant.get().getDefaultState(), 3, 1);
            addFeaturePlainFlower(generation, BlockRegistry.nichanghuaPlant.get().getDefaultState(), 3, 1);
        }
        if (category.equals(Biome.Category.JUNGLE)) { //丛林
            addFeaturePlainFlower(generation, BlockRegistry.moguPlant.get().getDefaultState(), 3, 1);
        }
        if (category.equals(Biome.Category.TAIGA)) { //针叶林
            addFeaturePlainFlower(generation, BlockRegistry.songrongPlant.get().getDefaultState(), 3, 1);
            addFeatureWallPlant(generation, BlockRegistry.songrongWallPlant.get().getDefaultState(), 6, 5);
        }
        if (category.equals(Biome.Category.ICY)) { //冰原
            addFeaturePlainFlower(generation, BlockRegistry.bingwuhuaHuaduoPlant.get().getDefaultState(), 1, 1);
        }
        if (category.equals(Biome.Category.DESERT)) { //沙漠
            addFeaturePlainFlower(generation, BlockRegistry.jueyunJiaojiaoPlant.get().getDefaultState(), 1, 1);
        }
        if (category.equals(Biome.Category.OCEAN)) { //海洋
            addFeatureOcean(generation, BlockRegistry.haicaoPlant.get().getDefaultState(), 0.4f, 16);
            addFeatureOcean(generation, BlockRegistry.hailingzhiPlant.get().getDefaultState(), 0.05f, 8);
        }
        if (category.equals(Biome.Category.MESA)) { //平顶山
            addFeaturePlainFlower(generation, BlockRegistry.xuekuiPlant.get().getDefaultState(), 3, 1);
        }
        if (category.equals(Biome.Category.SWAMP)) { //沼泽
            addFeatureRiver(generation, BlockRegistry.maweiPlant.get().getDefaultState(),  1);
        }
        if (category.equals(Biome.Category.MUSHROOM)) { //蘑菇岛
            addFeaturePlainFlower(generation, BlockRegistry.mufengMoguPlant.get().getDefaultState(), 3, 1);
        }
        if (category.equals(Biome.Category.PLAINS)) { //平原
            addFeaturePlainFlower(generation, BlockRegistry.fengchejuPlant.get().getDefaultState(), 1, 1);
            addFeaturePlainFlower(generation, BlockRegistry.pugongyingPlant.get().getDefaultState(), 1, 1);
        }
        if (category.equals(Biome.Category.BEACH)) { //海滩
            addFeatureRiver(generation, BlockRegistry.dudulianPlant.get().getDefaultState(),  4);
            addFeatureOcean(generation, BlockRegistry.haicaoPlant.get().getDefaultState(), 0.4f, 8);
        }
        //岩浆湖
        addFeatureLava(generation, BlockRegistry.lieyanhuaHuaruiPlant.get().getDefaultState(), 8);
        //河流
        addFeatureRiver(generation, BlockRegistry.lianpengPlant.get().getDefaultState(),  4);
        addFeatureRiver(generation, BlockRegistry.maweiPlant.get().getDefaultState(),  4);
        addFeatureRiver(generation, BlockRegistry.jinyucaoPlant.get().getDefaultState(), 4);
        //高山
        addFeatureMountain(generation, BlockRegistry.ceciliaHuaPlant.get().getDefaultState(), 1, 1);
        addFeatureMountain(generation, BlockRegistry.liuliDaiPlant.get().getDefaultState(), 1, 1);
        addFeatureMountain(generation, BlockRegistry.qingxinPlant.get().getDefaultState(), 1, 1);

        addFeaturePlainFlower(generation, BlockRegistry.bohePlant.get().getDefaultState(), 1, 1);
        addFeaturePlainFlower(generation, BlockRegistry.tiantianhuaPlant.get().getDefaultState(), 1, 1);
        addFeaturePlainFlower(generation, BlockRegistry.shumeiPlant.get().getDefaultState(), 1, 1);
    }

    /**
     * 花 平原 生成规则
     */
    private static void addFeaturePlainFlower(BiomeGenerationSettingsBuilder builder, BlockState state, int maxSize, int genCount) {
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                Feature.FLOWER.withConfiguration((new BlockClusterFeatureConfig.Builder(
                        new SimpleBlockStateProvider(state), SimpleBlockPlacer.PLACER)).tries(maxSize). //最大数量
                        build()).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(genCount)); //生成次数

    }

    /**
     * 高山生成
     * @param builder
     * @param state
     * @param maxSize
     * @param genCount
     */
    private static void addFeatureMountain(BiomeGenerationSettingsBuilder builder, BlockState state, int maxSize, int genCount) {
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                FeatureInit.MOUNTAIN_PLANT.get().withConfiguration((new BlockClusterFeatureConfig.Builder(
                        new SimpleBlockStateProvider(state), SimpleBlockPlacer.PLACER)).tries(maxSize). //最大数量
                        build()).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(genCount)); //生成次数

    }

    //墙上植物生成
    private static void addFeatureWallPlant(BiomeGenerationSettingsBuilder builder, BlockState state, int maxSize, int genCount) {
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                FeatureInit.WALL_PLANT.get().withConfiguration(new PlantConfig(state))
                        .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(genCount)); //生成次数

    }

    /**
     * 海洋生成
     * @param builder
     * @param state
     * @param probability 概率
     * @param genCount
     */
    private static void addFeatureOcean(BiomeGenerationSettingsBuilder builder, BlockState state, float probability, int genCount) {
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                FeatureInit.OCEAN_PLANT.get().withConfiguration(new OceanProbabilityConfig(probability, state))
                        .withPlacement(Features.Placements.SEAGRASS_DISK_PLACEMENT).count(genCount)); //生成次数
    }

    /**
     * 下界生成
     * @param builder
     * @param state
     * @param down
     * @param genCount
     */
    private static void addFeatureNether(BiomeGenerationSettingsBuilder builder, BlockState state, BlockState down, int genCount) {
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                FeatureInit.NETHER_PLANT.get().withConfiguration(new NetherConfig(state, down)).count(genCount)
                        .withPlacement(Placement.COUNT_MULTILAYER.configure(new FeatureSpreadConfig(6)))); //生成次数
    }

    /**
     * 水边生成规则
     */
    private static void addFeatureRiver(BiomeGenerationSettingsBuilder builder, BlockState state, int genCount) {
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                FeatureInit.RIVER_PLANT.get().withConfiguration(new PlantConfig(state))
                        .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(genCount)); //生成次数
    }

    /**
     * 岩浆边生成
     * @param builder
     * @param state
     * @param genCount
     */
    private static void addFeatureLava(BiomeGenerationSettingsBuilder builder, BlockState state, int genCount) {
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                FeatureInit.LAVA_PLANT.get().withConfiguration(new PlantConfig(state))
                        .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(genCount)); //生成次数
    }
}
