package com.yuo.PaiMeng.WorldGen;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

/**
 * 植物生成（1.20.1：PlacedFeature + data/paimeng/forge/biome_modifier/*.json）
 * 1.16.5 里用 BiomeLoadingEvent 往每个群系里塞 ConfiguredFeature，
 * 现在改为「注册 PlacedFeature + 用 biome_modifier JSON 注入群系」。
 */
public class WorldPlantGen {

    //竹林
    public static final ResourceKey<PlacedFeature> PLANT_ZHUSUN = key("plant_zhusun_bamboo_jungle");
    //下界
    public static final ResourceKey<PlacedFeature> PLANT_LIEYANHUA_CRIMSON = key("plant_lieyanhua_crimson_forest");
    public static final ResourceKey<PlacedFeature> PLANT_YOUDENGXUN_WARPED = key("plant_youdengxun_warped_forest");
    //黑森林 / 白桦林 / 森林
    public static final ResourceKey<PlacedFeature> PLANT_XIAODENGCAO_DARK = key("plant_xiaodengcao_dark_forest");
    public static final ResourceKey<PlacedFeature> PLANT_GOUGOUGUO_BIRCH = key("plant_gougouguo_birch_forest");
    public static final ResourceKey<PlacedFeature> PLANT_LUOLUOMEI_FOREST = key("plant_luoluomei_forest");
    public static final ResourceKey<PlacedFeature> PLANT_DUNDUNTAO_FOREST = key("plant_dunduntao_forest");
    public static final ResourceKey<PlacedFeature> PLANT_XIANGXINGUO_FOREST = key("plant_xiangxinguo_forest");
    //末地
    public static final ResourceKey<PlacedFeature> PLANT_TIANYUN_END = key("plant_tianyun_end_barrens");
    public static final ResourceKey<PlacedFeature> PLANT_MINGCAO_END = key("plant_mingcao_small_end_islands");
    //繁花森林
    public static final ResourceKey<PlacedFeature> PLANT_LIULI_BAIHE_FLOWER = key("plant_liuli_baihe_flower_forest");
    public static final ResourceKey<PlacedFeature> PLANT_NICHANGHUA_FLOWER = key("plant_nichanghua_flower_forest");
    public static final ResourceKey<PlacedFeature> PLANT_PIBOYE_FLOWER = key("plant_piboye_flower_forest");
    public static final ResourceKey<PlacedFeature> PLANT_PADISHALAN_FLOWER = key("plant_padishalan_flower_forest");
    //丛林
    public static final ResourceKey<PlacedFeature> PLANT_MOGU_JUNGLE = key("plant_mogu_jungle");
    public static final ResourceKey<PlacedFeature> PLANT_SHUWANG_JUNGLE = key("plant_shuwang_jungle");
    //针叶林
    public static final ResourceKey<PlacedFeature> PLANT_SONGRONG_TAIGA = key("plant_songrong_taiga");
    //冰原
    public static final ResourceKey<PlacedFeature> PLANT_BINGWUHUA_ICY = key("plant_bingwuhua_icy");
    //沙漠
    public static final ResourceKey<PlacedFeature> PLANT_JUEYUN_DESERT = key("plant_jueyun_desert");
    //海洋
    public static final ResourceKey<PlacedFeature> PLANT_HAICAO_OCEAN = key("plant_haicao_ocean");
    public static final ResourceKey<PlacedFeature> PLANT_HAILINGZHI_OCEAN = key("plant_hailingzhi_ocean");
    public static final ResourceKey<PlacedFeature> PLANT_HAICAO_BEACH = key("plant_haicao_beach");
    //平顶山 / 沼泽 / 蘑菇岛
    public static final ResourceKey<PlacedFeature> PLANT_XUEKUI_BADLANDS = key("plant_xuekui_badlands");
    public static final ResourceKey<PlacedFeature> PLANT_MAWEI_SWAMP = key("plant_mawei_swamp");
    public static final ResourceKey<PlacedFeature> PLANT_MUFENG_MUSHROOM = key("plant_mufeng_mushroom_fields");
    //平原
    public static final ResourceKey<PlacedFeature> PLANT_FENGCHEJU_PLAINS = key("plant_fengcheju_plains");
    public static final ResourceKey<PlacedFeature> PLANT_PUGONGYING_PLAINS = key("plant_pugongying_plains");
    public static final ResourceKey<PlacedFeature> PLANT_XUMI_PLAINS = key("plant_xumi_qiangwei_plains");
    public static final ResourceKey<PlacedFeature> PLANT_MOJIE_PLAINS = key("plant_mojiecao_plains");
    //河滩
    public static final ResourceKey<PlacedFeature> PLANT_DUDULIAN_BEACH = key("plant_dudulian_beach");
    //岩浆湖
    public static final ResourceKey<PlacedFeature> PLANT_LIEYANHUA_LAVA = key("plant_lieyanhua_huarui_lava");
    //河流
    public static final ResourceKey<PlacedFeature> PLANT_LIANPENG_RIVER = key("plant_lianpeng_river");
    public static final ResourceKey<PlacedFeature> PLANT_MAWEI_RIVER = key("plant_mawei_river");
    public static final ResourceKey<PlacedFeature> PLANT_JINYUCAO_RIVER = key("plant_jinyucao_river");
    //高山
    public static final ResourceKey<PlacedFeature> PLANT_CECILIA_MOUNTAIN = key("plant_cecilia_mountain");
    public static final ResourceKey<PlacedFeature> PLANT_LIULIDAI_MOUNTAIN = key("plant_liuli_dai_mountain");
    public static final ResourceKey<PlacedFeature> PLANT_QINGXIN_MOUNTAIN = key("plant_qingxin_mountain");
    //所有群系
    public static final ResourceKey<PlacedFeature> PLANT_BOHE = key("plant_bohe");
    public static final ResourceKey<PlacedFeature> PLANT_TIANTIANHUA = key("plant_tiantianhua");
    public static final ResourceKey<PlacedFeature> PLANT_SHUMEI = key("plant_shumei");

    private static ResourceKey<PlacedFeature> key(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(PaiMeng.MOD_ID, name));
    }

    public static void register(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.PLACED_FEATURE)) return;

        //花：原版随机斑块（RANDOM_PATCH + SIMPLE_BLOCK）
        flower(event, PLANT_ZHUSUN, state(PMBlocks.zhusunPlant), 6, 2);
        flower(event, PLANT_XIAODENGCAO_DARK, state(PMBlocks.xiaodengcaoPlant), 16, 3);
        flower(event, PLANT_GOUGOUGUO_BIRCH, state(PMBlocks.gougouguoPlant), 3, 1);
        flower(event, PLANT_LUOLUOMEI_FOREST, state(PMBlocks.luoluomeiPlant), 3, 1);
        flower(event, PLANT_DUNDUNTAO_FOREST, state(PMBlocks.dunduntaoPlant), 3, 1);
        flower(event, PLANT_XIANGXINGUO_FOREST, state(PMBlocks.xiangxinguoPlant), 3, 1);
        flower(event, PLANT_TIANYUN_END, state(PMBlocks.tianyunCaoshiPlant), 1, 1);
        flower(event, PLANT_MINGCAO_END, state(PMBlocks.mingcaoPlant), 1, 3);
        flower(event, PLANT_LIULI_BAIHE_FLOWER, state(PMBlocks.liuliBaihePlant), 3, 1);
        flower(event, PLANT_NICHANGHUA_FLOWER, state(PMBlocks.nichanghuaPlant), 3, 1);
        flower(event, PLANT_PIBOYE_FLOWER, state(PMBlocks.piboyePlant), 3, 1);
        flower(event, PLANT_PADISHALAN_FLOWER, state(PMBlocks.padishalanPlant), 3, 1);
        flower(event, PLANT_MOGU_JUNGLE, state(PMBlocks.moguPlant), 3, 1);
        flower(event, PLANT_SHUWANG_JUNGLE, state(PMBlocks.shuwangShengtiguPlant), 3, 1);
        flower(event, PLANT_SONGRONG_TAIGA, state(PMBlocks.songrongPlant), 3, 1);
        flower(event, PLANT_BINGWUHUA_ICY, state(PMBlocks.bingwuhuaHuaduoPlant), 1, 1);
        flower(event, PLANT_JUEYUN_DESERT, state(PMBlocks.jueyunJiaojiaoPlant), 1, 1);
        flower(event, PLANT_XUEKUI_BADLANDS, state(PMBlocks.xuekuiPlant), 3, 1);
        flower(event, PLANT_MUFENG_MUSHROOM, state(PMBlocks.mufengMoguPlant), 3, 1);
        flower(event, PLANT_FENGCHEJU_PLAINS, state(PMBlocks.fengchejuPlant), 1, 1);
        flower(event, PLANT_PUGONGYING_PLAINS, state(PMBlocks.pugongyingPlant), 1, 1);
        flower(event, PLANT_XUMI_PLAINS, state(PMBlocks.xumiQiangweiPlant), 1, 1);
        flower(event, PLANT_MOJIE_PLAINS, state(PMBlocks.mojiecaoPlant), 1, 1);
        flower(event, PLANT_BOHE, state(PMBlocks.bohePlant), 1, 1);
        flower(event, PLANT_TIANTIANHUA, state(PMBlocks.tiantianhuaPlant), 1, 1);
        flower(event, PLANT_SHUMEI, state(PMBlocks.shumeiPlant), 1, 1);

        //河流 / 水边
        plant(event, PLANT_LIANPENG_RIVER, FeatureInit.RIVER_PLANT, new PlantConfig(state(PMBlocks.lianpengPlant)), 4);
        plant(event, PLANT_MAWEI_RIVER, FeatureInit.RIVER_PLANT, new PlantConfig(state(PMBlocks.maweiPlant)), 4);
        plant(event, PLANT_JINYUCAO_RIVER, FeatureInit.RIVER_PLANT, new PlantConfig(state(PMBlocks.jinyucaoPlant)), 4);
        plant(event, PLANT_MAWEI_SWAMP, FeatureInit.RIVER_PLANT, new PlantConfig(state(PMBlocks.maweiPlant)), 1);
        plant(event, PLANT_DUDULIAN_BEACH, FeatureInit.RIVER_PLANT, new PlantConfig(state(PMBlocks.dudulianPlant)), 4);
        //高山
        plant(event, PLANT_CECILIA_MOUNTAIN, FeatureInit.MOUNTAIN_PLANT, new PlantConfig(state(PMBlocks.ceciliaHuaPlant)), 1);
        plant(event, PLANT_LIULIDAI_MOUNTAIN, FeatureInit.MOUNTAIN_PLANT, new PlantConfig(state(PMBlocks.liuliDaiPlant)), 1);
        plant(event, PLANT_QINGXIN_MOUNTAIN, FeatureInit.MOUNTAIN_PLANT, new PlantConfig(state(PMBlocks.qingxinPlant)), 1);
        //岩浆湖
        plant(event, PLANT_LIEYANHUA_LAVA, FeatureInit.LAVA_PLANT, new PlantConfig(state(PMBlocks.lieyanhuaHuaruiPlant)), 8);
        //海洋
        ocean(event, PLANT_HAICAO_OCEAN, state(PMBlocks.haicaoPlant), 0.4f, 16);
        ocean(event, PLANT_HAILINGZHI_OCEAN, state(PMBlocks.hailingzhiPlant), 0.05f, 8);
        ocean(event, PLANT_HAICAO_BEACH, state(PMBlocks.haicaoPlant), 0.4f, 8);
        //下界
        nether(event, PLANT_LIEYANHUA_CRIMSON, state(PMBlocks.lieyanhuaHuaruiPlant), Blocks.CRIMSON_NYLIUM.defaultBlockState(), 1);
        nether(event, PLANT_YOUDENGXUN_WARPED, state(PMBlocks.youdengxunPlant), Blocks.WARPED_NYLIUM.defaultBlockState(), 1);
    }

    private static BlockState state(RegistryObject<? extends Block> block) {
        return block.get().defaultBlockState();
    }

    //原版花：RANDOM_PATCH + SIMPLE_BLOCK
    private static void flower(RegisterEvent event, ResourceKey<PlacedFeature> key, BlockState state, int tries, int count) {
        SimpleBlockConfiguration simpleConfig = new SimpleBlockConfiguration(BlockStateProvider.simple(state));

        // 🔴 核心修改 1：使用 BlockPredicateFilter 代替 onlyWhenEmpty
        // 要求：目标位置是空气，且下方是草方块或泥土（可自行添加沙子、雪等）
        Holder<PlacedFeature> nested = Holder.direct(new PlacedFeature(
                Holder.direct(new ConfiguredFeature<>(Feature.SIMPLE_BLOCK, simpleConfig)),
                List.of(
                        BlockPredicateFilter.forPredicate(BlockPredicate.allOf(
                                BlockPredicate.ONLY_IN_AIR_PREDICATE, // 目标位置必须是空气
                                BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(),
                                        Blocks.GRASS_BLOCK, Blocks.DIRT, Blocks.PODZOL, Blocks.COARSE_DIRT, Blocks.SAND, Blocks.SNOW_BLOCK) // 下方必须是这些方块
                        ))
                )
        ));

        event.register(Registries.PLACED_FEATURE, key.location(), () -> new PlacedFeature(
                Holder.direct(new ConfiguredFeature<>(Feature.RANDOM_PATCH, new RandomPatchConfiguration(tries, 7, 3, nested))),
                List.of(
                        CountPlacement.of(count),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                )));
    }

    //自定义植物 Feature
    private static void plant(RegisterEvent event, ResourceKey<PlacedFeature> key,
                              RegistryObject<? extends Feature<?>> feature, Object config, int count) {
        event.register(Registries.PLACED_FEATURE, key.location(), () -> new PlacedFeature(
                Holder.direct(configured(feature, config)),
                List.of(
                        CountPlacement.of(count),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                )));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private static ConfiguredFeature<?, ?> configured(RegistryObject<? extends Feature<?>> feature, Object config) {
        return new ConfiguredFeature((Feature) feature.get(), (FeatureConfiguration) config);
    }

    //海洋植物
    private static void ocean(RegisterEvent event, ResourceKey<PlacedFeature> key, BlockState state, float probability, int count) {
        event.register(Registries.PLACED_FEATURE, key.location(), () -> new PlacedFeature(
                Holder.direct(configured(FeatureInit.OCEAN_PLANT, new OceanProbabilityConfig(probability, state))),
                List.of(
                        CountPlacement.of(count),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                        BiomeFilter.biome()
                )));
    }

    //下界植物
    private static void nether(RegisterEvent event, ResourceKey<PlacedFeature> key, BlockState state, BlockState down, int count) {
        event.register(Registries.PLACED_FEATURE, key.location(), () -> new PlacedFeature(
                Holder.direct(configured(FeatureInit.NETHER_PLANT, new NetherConfig(state, down))),
                List.of(
                        CountPlacement.of(count),
                        InSquarePlacement.spread(),
                        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                        BiomeFilter.biome()
                )));
    }
}
