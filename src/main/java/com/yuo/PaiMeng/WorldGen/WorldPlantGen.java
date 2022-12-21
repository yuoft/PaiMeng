package com.yuo.PaiMeng.WorldGen;

import com.yuo.PaiMeng.Blocks.PMBlocks;
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
            addFeaturePlainFlower(generation, PMBlocks.zhusunPlant.get().getDefaultState(), 6, 2);
        }
        if (key.equals(Biomes.CRIMSON_FOREST)) { //绯红森林
            addFeatureNether(generation, PMBlocks.lieyanhuaHuaruiPlant.get().getDefaultState(), Blocks.CRIMSON_NYLIUM.getDefaultState(), 1);
        }
        if (key.equals(Biomes.WARPED_FOREST)){ //诡异森林
            addFeatureNether(generation, PMBlocks.youdengxunPlant.get().getDefaultState(), Blocks.WARPED_NYLIUM.getDefaultState(), 1);
        }
        if (key.equals(Biomes.DARK_FOREST) || key.equals(Biomes.DARK_FOREST_HILLS)){ //黑森林
            addFeaturePlainFlower(generation, PMBlocks.xiaodengcaoPlant.get().getDefaultState(), 16, 3);
        }
        if (key.equals(Biomes.BIRCH_FOREST) || key.equals(Biomes.BIRCH_FOREST_HILLS) || key.equals(Biomes.TALL_BIRCH_FOREST)
                || key.equals(Biomes.TALL_BIRCH_HILLS)){ //白桦林
            addFeaturePlainFlower(generation, PMBlocks.gougouguoPlant.get().getDefaultState(), 3, 1);
        }
        if (key.equals(Biomes.FOREST)){ //森林
            addFeaturePlainFlower(generation, PMBlocks.luoluomeiPlant.get().getDefaultState(), 3, 1);
            addFeaturePlainFlower(generation, PMBlocks.dunduntaoPlant.get().getDefaultState(), 3, 1);
            addFeaturePlainFlower(generation, PMBlocks.xiangxinguoPlant.get().getDefaultState(), 3, 1);
        }
        if (key.equals(Biomes.END_BARRENS) || key.equals(Biomes.END_MIDLANDS)){ //末地荒岛 中型末地岛屿
            addFeaturePlainFlower(generation, PMBlocks.tianyunCaoshiPlant.get().getDefaultState(), 1, 1);
        }
        if (key.equals(Biomes.SMALL_END_ISLANDS)){ //小型末地岛屿
            addFeaturePlainFlower(generation, PMBlocks.mingcaoPlant.get().getDefaultState(), 1, 3);
        }
        if (key.equals(Biomes.FLOWER_FOREST)){ //繁花森林
            addFeaturePlainFlower(generation, PMBlocks.liuliBaihePlant.get().getDefaultState(), 3, 1);
            addFeaturePlainFlower(generation, PMBlocks.nichanghuaPlant.get().getDefaultState(), 3, 1);
            addFeaturePlainFlower(generation, PMBlocks.piboyePlant.get().getDefaultState(), 3, 1);
            addFeaturePlainFlower(generation, PMBlocks.padishalanPlant.get().getDefaultState(), 3, 1);
        }
        if (category.equals(Biome.Category.JUNGLE)) { //丛林
            addFeaturePlainFlower(generation, PMBlocks.moguPlant.get().getDefaultState(), 3, 1);
            addFeaturePlainFlower(generation, PMBlocks.shuwangShengtiguPlant.get().getDefaultState(), 3, 1);
        }
        if (category.equals(Biome.Category.TAIGA)) { //针叶林
            addFeaturePlainFlower(generation, PMBlocks.songrongPlant.get().getDefaultState(), 3, 1);
        }
        if (category.equals(Biome.Category.ICY)) { //冰原
            addFeaturePlainFlower(generation, PMBlocks.bingwuhuaHuaduoPlant.get().getDefaultState(), 1, 1);
        }
        if (category.equals(Biome.Category.DESERT)) { //沙漠
            addFeaturePlainFlower(generation, PMBlocks.jueyunJiaojiaoPlant.get().getDefaultState(), 1, 1);
        }
        if (category.equals(Biome.Category.OCEAN)) { //海洋
            addFeatureOcean(generation, PMBlocks.haicaoPlant.get().getDefaultState(), 0.4f, 16);
            addFeatureOcean(generation, PMBlocks.hailingzhiPlant.get().getDefaultState(), 0.05f, 8);
        }
        if (category.equals(Biome.Category.MESA)) { //平顶山
            addFeaturePlainFlower(generation, PMBlocks.xuekuiPlant.get().getDefaultState(), 3, 1);
        }
        if (category.equals(Biome.Category.SWAMP)) { //沼泽
            addFeatureRiver(generation, PMBlocks.maweiPlant.get().getDefaultState(),  1);
        }
        if (category.equals(Biome.Category.MUSHROOM)) { //蘑菇岛
            addFeaturePlainFlower(generation, PMBlocks.mufengMoguPlant.get().getDefaultState(), 3, 1);
        }
        if (category.equals(Biome.Category.PLAINS)) { //平原
            addFeaturePlainFlower(generation, PMBlocks.fengchejuPlant.get().getDefaultState(), 1, 1);
            addFeaturePlainFlower(generation, PMBlocks.pugongyingPlant.get().getDefaultState(), 1, 1);
            addFeaturePlainFlower(generation, PMBlocks.xumiQiangweiPlant.get().getDefaultState(), 1, 1);
        }
        if (category.equals(Biome.Category.BEACH)) { //海滩
            addFeatureRiver(generation, PMBlocks.dudulianPlant.get().getDefaultState(),  4);
            addFeatureOcean(generation, PMBlocks.haicaoPlant.get().getDefaultState(), 0.4f, 8);
        }
        //岩浆湖
        addFeatureLava(generation, PMBlocks.lieyanhuaHuaruiPlant.get().getDefaultState(), 8);
        //河流
        addFeatureRiver(generation, PMBlocks.lianpengPlant.get().getDefaultState(),  4);
        addFeatureRiver(generation, PMBlocks.maweiPlant.get().getDefaultState(),  4);
        addFeatureRiver(generation, PMBlocks.jinyucaoPlant.get().getDefaultState(), 4);
        //高山
        addFeatureMountain(generation, PMBlocks.ceciliaHuaPlant.get().getDefaultState(), 1, 1);
        addFeatureMountain(generation, PMBlocks.liuliDaiPlant.get().getDefaultState(), 1, 1);
        addFeatureMountain(generation, PMBlocks.qingxinPlant.get().getDefaultState(), 1, 1);

        addFeaturePlainFlower(generation, PMBlocks.bohePlant.get().getDefaultState(), 1, 1);
        addFeaturePlainFlower(generation, PMBlocks.tiantianhuaPlant.get().getDefaultState(), 1, 1);
        addFeaturePlainFlower(generation, PMBlocks.shumeiPlant.get().getDefaultState(), 1, 1);
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
     * @param builder bu
     * @param state 要生成的方块
     * @param maxSize 最大生成数量
     * @param genCount 次数
     */
    private static void addFeatureMountain(BiomeGenerationSettingsBuilder builder, BlockState state, int maxSize, int genCount) {
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                FeatureInit.MOUNTAIN_PLANT.get().withConfiguration((new BlockClusterFeatureConfig.Builder(
                        new SimpleBlockStateProvider(state), SimpleBlockPlacer.PLACER)).tries(maxSize). //最大数量
                        build()).withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(genCount)); //生成次数

    }

    /**
     * 海洋生成
     * @param builder b
     * @param state s
     * @param probability 概率
     * @param genCount c
     */
    private static void addFeatureOcean(BiomeGenerationSettingsBuilder builder, BlockState state, float probability, int genCount) {
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                FeatureInit.OCEAN_PLANT.get().withConfiguration(new OceanProbabilityConfig(probability, state))
                        .withPlacement(Features.Placements.SEAGRASS_DISK_PLACEMENT).count(genCount)); //生成次数
    }

    /**
     * 下界生成
     * @param builder b
     * @param state s
     * @param down d
     * @param genCount c
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
     * @param builder b
     * @param state s
     * @param genCount c
     */
    private static void addFeatureLava(BiomeGenerationSettingsBuilder builder, BlockState state, int genCount) {
        builder.withFeature(GenerationStage.Decoration.VEGETAL_DECORATION,
                FeatureInit.LAVA_PLANT.get().withConfiguration(new PlantConfig(state))
                        .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).count(genCount)); //生成次数
    }
}
