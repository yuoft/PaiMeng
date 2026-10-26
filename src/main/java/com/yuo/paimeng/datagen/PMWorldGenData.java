package com.yuo.paimeng.datagen;

import com.yuo.paimeng.blocks.PMBlocks;
import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.worldgen.FeatureInit;
import com.yuo.paimeng.worldgen.NetherConfig;
import com.yuo.paimeng.worldgen.OceanProbabilityConfig;
import com.yuo.paimeng.worldgen.PlantConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.BlockPredicateFilter;
import net.minecraft.world.level.levelgen.placement.CountOnEveryLayerPlacement;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * 世界生成数据表：ConfiguredFeature / PlacedFeature / BiomeModifier 三份 datagen 都由这里驱动，
 * 保证「同一个 id」在三处一致。
 */
public final class PMWorldGenData {

    private PMWorldGenData() {
    }

    // ---------------------------------------------------------------- keys

    public static ResourceKey<ConfiguredFeature<?, ?>> cfgKey(String id) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ModUtils.fa(id));
    }

    public static ResourceKey<PlacedFeature> placedKey(String id) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ModUtils.fa(id));
    }

    public static ResourceKey<Biome> biomeKey(String id) {
        return ResourceKey.create(Registries.BIOME, ModUtils.def(id));
    }

    // ---------------------------------------------------------------- records

    /** ConfiguredFeature 定义 */
    public record CfgDef(String id, Feature<?> feature, FeatureConfiguration config) {
        public ResourceKey<ConfiguredFeature<?, ?>> key() {
            return cfgKey(id);
        }
    }

    /** PlacedFeature + 该 feature 注入哪些生物群系、哪个 generation step */
    public record PfDef(String id, String configuredId, List<PlacementModifier> modifiers,
                        @Nullable TagKey<Biome> biomeTag, List<ResourceKey<Biome>> biomes,
                        GenerationStep.Decoration step) {
        public ResourceKey<PlacedFeature> key() {
            return placedKey(id);
        }
    }

    // ---------------------------------------------------------------- helpers

    private static List<PlacementModifier> ore(int count, int minY, int maxY) {
        return List.of(
                CountPlacement.of(count),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY)),
                BiomeFilter.biome());
    }

    private static List<PlacementModifier> patch(int count) {
        return List.of(
                CountPlacement.of(count),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                BiomeFilter.biome());
    }

    private static List<PlacementModifier> ocean(int count) {
        return List.of(
                CountPlacement.of(count),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
                BiomeFilter.biome());
    }

    private static List<PlacementModifier> netherLayer(int count) {
        return List.of(
                CountOnEveryLayerPlacement.of(count),
                BiomeFilter.biome());
    }

    private static List<PlacementModifier> underground(int count, int minY, int maxY) {
        return List.of(
                CountPlacement.of(count),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY)),
                BiomeFilter.biome());
    }

    private static List<PlacementModifier> tree(int rarity) {
        return List.of(
                CountPlacement.of(1),
                InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
                RarityFilter.onAverageOnceEvery(rarity),
                BiomeFilter.biome());
    }

    private static Block block(RegistryObject<? extends Block> block) {
        return block.get();
    }

    private static CfgDef none(String id, RegistryObject<? extends Feature<?>> feature) {
        return new CfgDef(id, feature.get(), NoneFeatureConfiguration.INSTANCE);
    }

    /** 原版随机斑块：只在空气位置生成，不会替换已有方块 */
    private static CfgDef patchFeature(String id, Block block, int tries) {
        Holder<PlacedFeature> nested = Holder.direct(new PlacedFeature(
                Holder.direct(new ConfiguredFeature<>(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(block.defaultBlockState())))),
                List.of(BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE))));
        return new CfgDef(id, Feature.RANDOM_PATCH, new RandomPatchConfiguration(tries, 7, 3, nested));
    }

    private static CfgDef oreFeature(String id, Block ore, int size, RuleTest rule) {
        return new CfgDef(id, Feature.ORE,
                new OreConfiguration(List.of(OreConfiguration.target(rule, ore.defaultBlockState())), size));
    }

    private static CfgDef netherPlant(String id, Block plant, Block ground) {
        return new CfgDef(id, FeatureInit.NETHER_PLANT.get(),
                new NetherConfig(plant.defaultBlockState(), ground.defaultBlockState()));
    }

    private static CfgDef oceanPlant(String id, Block plant, float probability) {
        return new CfgDef(id, FeatureInit.OCEAN_PLANT.get(),
                new OceanProbabilityConfig(probability, plant.defaultBlockState()));
    }

    private static CfgDef lavaPlant(String id, Block plant) {
        return new CfgDef(id, FeatureInit.LAVA_PLANT.get(), new PlantConfig(plant.defaultBlockState()));
    }

    @SafeVarargs
    private static PfDef pf(String id, String configuredId, List<PlacementModifier> modifiers,
                            GenerationStep.Decoration step, @Nullable TagKey<Biome> tag, ResourceKey<Biome>... biomes) {
        return new PfDef(id, configuredId, modifiers, tag, List.of(biomes), step);
    }

    // ---------------------------------------------------------------- 群系集合

    private static final ResourceKey<Biome>[] ICY = new ResourceKey[]{
            biomeKey("minecraft:snowy_plains"), biomeKey("minecraft:ice_spikes"),
            biomeKey("minecraft:snowy_taiga"), biomeKey("minecraft:snowy_beach"),
            biomeKey("minecraft:frozen_ocean"), biomeKey("minecraft:frozen_river"),
            biomeKey("minecraft:snowy_slopes"), biomeKey("minecraft:grove"),
            biomeKey("minecraft:frozen_peaks")};

    private static final ResourceKey<Biome>[] DESERT = new ResourceKey[]{biomeKey("minecraft:desert")};
    private static final ResourceKey<Biome>[] BAMBOO = new ResourceKey[]{biomeKey("minecraft:bamboo_jungle")};
    private static final ResourceKey<Biome>[] CRIMSON = new ResourceKey[]{biomeKey("minecraft:crimson_forest")};
    private static final ResourceKey<Biome>[] WARPED = new ResourceKey[]{biomeKey("minecraft:warped_forest")};
    private static final ResourceKey<Biome>[] DARK_FOREST = new ResourceKey[]{biomeKey("minecraft:dark_forest")};
    private static final ResourceKey<Biome>[] BIRCH = new ResourceKey[]{
            biomeKey("minecraft:birch_forest"), biomeKey("minecraft:old_growth_birch_forest")};
    private static final ResourceKey<Biome>[] FOREST = new ResourceKey[]{biomeKey("minecraft:forest")};
    private static final ResourceKey<Biome>[] END_MID = new ResourceKey[]{
            biomeKey("minecraft:end_barrens"), biomeKey("minecraft:end_midlands")};
    private static final ResourceKey<Biome>[] SMALL_END = new ResourceKey[]{biomeKey("minecraft:small_end_islands")};
    private static final ResourceKey<Biome>[] FLOWER_FOREST = new ResourceKey[]{biomeKey("minecraft:flower_forest")};
    private static final ResourceKey<Biome>[] PLAINS = new ResourceKey[]{biomeKey("minecraft:plains")};
    private static final ResourceKey<Biome>[] SWAMP = new ResourceKey[]{biomeKey("minecraft:swamp")};
    private static final ResourceKey<Biome>[] MUSHROOM = new ResourceKey[]{biomeKey("minecraft:mushroom_fields")};

    // ---------------------------------------------------------------- 数据表

    /**
     * 生成配置
     */
    public static List<CfgDef> configured() {
        RuleTest stone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest netherrack = new TagMatchTest(BlockTags.BASE_STONE_NETHER);
        RuleTest endStone = new BlockMatchTest(Blocks.END_STONE);

        List<CfgDef> list = new ArrayList<>();

        // 树（树苗生长 + 世界生成共用）
        list.add(none("apple_tree", FeatureInit.APPLE_TREE));
        list.add(none("sun_apple_tree", FeatureInit.SUN_APPLE_TREE));
        list.add(none("purple_apple_tree", FeatureInit.PURPLE_APPLE_TREE));
        list.add(none("zaoye_tree", FeatureInit.ZAOYE_TREE));
        list.add(none("paopaoju_tree", FeatureInit.PAOPAOJU_TREE));
        list.add(none("withered_tree", FeatureInit.WITHERED_TREE));

        // 矿物
        list.add(oreFeature("ore_xingyin_icy", block(PMBlocks.xingyinOre), 8, stone));
        list.add(oreFeature("ore_shipo_desert", block(PMBlocks.shipoOre), 4, stone));
        list.add(oreFeature("ore_dianqi_ocean", block(PMBlocks.dianqiShuijingOre), 4, stone));
        list.add(oreFeature("ore_jinghua_badlands", block(PMBlocks.jinghuaGusuiOre), 4, stone));
        list.add(oreFeature("ore_shuijing_badlands", block(PMBlocks.shuijingOre), 4, stone));
        list.add(oreFeature("ore_shuijing", block(PMBlocks.shuijingOre), 4, stone));
        list.add(oreFeature("ore_baitie", block(PMBlocks.baitieOre), 8, stone));
        list.add(oreFeature("ore_heitie", block(PMBlocks.heitieOre), 10, stone));
        list.add(oreFeature("ore_mojing_nether", block(PMBlocks.mojingOre), 5, netherrack));
        list.add(oreFeature("ore_zijing_nether", block(PMBlocks.zijingOre), 5, netherrack));
        list.add(oreFeature("ore_yeboshi_end", block(PMBlocks.yeboshiOre), 4, endStone));

        // 花/普通植物（原版随机斑块，只在空气格生成）
        list.add(patchFeature("plant_zhusun_bamboo_jungle", block(PMBlocks.zhusunPlant), 6));
        list.add(patchFeature("plant_xiaodengcao_dark_forest", block(PMBlocks.xiaodengcaoPlant), 16));
        list.add(patchFeature("plant_gougouguo_birch_forest", block(PMBlocks.gougouguoPlant), 3));
        list.add(patchFeature("plant_luoluomei_forest", block(PMBlocks.luoluomeiPlant), 3));
        list.add(patchFeature("plant_dunduntao_forest", block(PMBlocks.dunduntaoPlant), 3));
        list.add(patchFeature("plant_xiangxinguo_forest", block(PMBlocks.xiangxinguoPlant), 3));
        list.add(patchFeature("plant_tianyun_end_barrens", block(PMBlocks.tianyunCaoshiPlant), 2));
        list.add(patchFeature("plant_mingcao_small_end_islands", block(PMBlocks.mingcaoPlant), 2));
        list.add(patchFeature("plant_liuli_baihe_flower_forest", block(PMBlocks.liuliBaihePlant), 3));
        list.add(patchFeature("plant_nichanghua_flower_forest", block(PMBlocks.nichanghuaPlant), 3));
        list.add(patchFeature("plant_piboye_flower_forest", block(PMBlocks.piboyePlant), 3));
        list.add(patchFeature("plant_padishalan_flower_forest", block(PMBlocks.padishalanPlant), 3));
        list.add(patchFeature("plant_mogu_jungle", block(PMBlocks.moguPlant), 3));
        list.add(patchFeature("plant_shuwang_jungle", block(PMBlocks.shuwangShengtiguPlant), 3));
        list.add(patchFeature("plant_songrong_taiga", block(PMBlocks.songrongPlant), 3));
        list.add(patchFeature("plant_bingwuhua_icy", block(PMBlocks.bingwuhuaHuaduoPlant), 2));
        list.add(patchFeature("plant_jueyun_desert", block(PMBlocks.jueyunJiaojiaoPlant), 2));
        list.add(patchFeature("plant_xuekui_badlands", block(PMBlocks.xuekuiPlant), 3));
        list.add(patchFeature("plant_mufeng_mushroom_fields", block(PMBlocks.mufengMoguPlant), 3));
        list.add(patchFeature("plant_fengcheju_plains", block(PMBlocks.fengchejuPlant), 2));
        list.add(patchFeature("plant_pugongying_plains", block(PMBlocks.pugongyingPlant), 2));
        list.add(patchFeature("plant_xumi_qiangwei_plains", block(PMBlocks.xumiQiangweiPlant), 2));
        list.add(patchFeature("plant_mojiecao_plains", block(PMBlocks.mojiecaoPlant), 2));
        list.add(patchFeature("plant_bohe", block(PMBlocks.bohePlant), 2));
        list.add(patchFeature("plant_tiantianhua", block(PMBlocks.tiantianhuaPlant), 2));
        list.add(patchFeature("plant_shumei", block(PMBlocks.shumeiPlant), 2));
        list.add(patchFeature("plant_lianpeng_river", block(PMBlocks.lianpengPlant), 4));
        list.add(patchFeature("plant_mawei_river", block(PMBlocks.maweiPlant), 4));
        list.add(patchFeature("plant_jinyucao_river", block(PMBlocks.jinyucaoPlant), 4));
        list.add(patchFeature("plant_mawei_swamp", block(PMBlocks.maweiPlant), 4));
        list.add(patchFeature("plant_dudulian_beach", block(PMBlocks.dudulianPlant), 4));
        list.add(patchFeature("plant_cecilia_mountain", block(PMBlocks.ceciliaHuaPlant), 2));
        list.add(patchFeature("plant_liuli_dai_mountain", block(PMBlocks.liuliDaiPlant), 2));
        list.add(patchFeature("plant_qingxin_mountain", block(PMBlocks.qingxinPlant), 2));

        // 海洋植物（自定义 Feature：只在水中、按深度与概率生成）
        list.add(oceanPlant("plant_haicao_ocean", block(PMBlocks.haicaoPlant), 0.4F));
        list.add(oceanPlant("plant_hailingzhi_ocean", block(PMBlocks.hailingzhiPlant), 0.05F));
        list.add(oceanPlant("plant_haicao_beach", block(PMBlocks.haicaoPlant), 0.4F));
        list.add(oceanPlant("plant_hailingzhi_river", block(PMBlocks.hailingzhiPlant), 0.05F));
        list.add(oceanPlant("plant_haicao_river", block(PMBlocks.haicaoPlant), 0.4F));

        // 下界植物（自定义 Feature：菌岩上生成）
        list.add(netherPlant("plant_lieyanhua_crimson_forest", block(PMBlocks.lieyanhuaHuaruiPlant), Blocks.CRIMSON_NYLIUM));
        list.add(netherPlant("plant_youdengxun_warped_forest", block(PMBlocks.youdengxunPlant), Blocks.WARPED_NYLIUM));

        // 岩浆边植物（自定义 Feature：附近有岩浆 + 下方石头）
        list.add(lavaPlant("plant_lieyanhua_huarui_lava", block(PMBlocks.lieyanhuaHuaruiPlant)));

        return List.copyOf(list);
    }

    /**
     * 放置规则
     */
    public static List<PfDef> placed() {
        List<PfDef> list = new ArrayList<>();

        // 树（森林稀疏生成）
        list.add(pf("tree_apple_forest", "apple_tree", tree(50), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_FOREST));
        list.add(pf("tree_purple_apple_forest", "purple_apple_tree", tree(50), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_FOREST));
        list.add(pf("tree_zaoye_forest", "zaoye_tree", tree(50), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_FOREST, Biomes.WINDSWEPT_FOREST));
        list.add(pf("tree_paopaoju_forest", "paopaoju_tree", tree(50), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_FOREST));
        list.add(pf("tree_withered_forest", "withered_tree", tree(200), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_FOREST));

        // 树苗生长用的 PlacedFeature（不注入群系，仅保持与 ConfiguredFeature 同 id 的可用性）
        for (String tree : List.of("apple_tree", "sun_apple_tree", "purple_apple_tree", "zaoye_tree", "paopaoju_tree", "withered_tree")) {
            list.add(pf(tree, tree, patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null));
        }

        // 矿物
        list.add(pf("ore_xingyin_icy", "ore_xingyin_icy", ore(6, 8, 32), GenerationStep.Decoration.UNDERGROUND_ORES, null, ICY));
        list.add(pf("ore_shipo_desert", "ore_shipo_desert", ore(4, 16, 64), GenerationStep.Decoration.UNDERGROUND_ORES, null, DESERT));
        list.add(pf("ore_dianqi_ocean", "ore_dianqi_ocean", ore(4, 8, 48), GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OCEAN));
        list.add(pf("ore_jinghua_badlands", "ore_jinghua_badlands", ore(4, 16, 48), GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_BADLANDS));
        list.add(pf("ore_shuijing_badlands", "ore_shuijing_badlands", ore(6, 0, 16), GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_BADLANDS));
        list.add(pf("ore_shuijing", "ore_shuijing", ore(4, 8, 32), GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD));
        list.add(pf("ore_baitie", "ore_baitie", ore(6, 0, 32), GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD));
        list.add(pf("ore_heitie", "ore_heitie", ore(8, 0, 96), GenerationStep.Decoration.UNDERGROUND_ORES, BiomeTags.IS_OVERWORLD));
        list.add(pf("ore_mojing_nether", "ore_mojing_nether", ore(16, 8, 32), GenerationStep.Decoration.UNDERGROUND_DECORATION, BiomeTags.IS_NETHER));
        list.add(pf("ore_zijing_nether", "ore_zijing_nether", ore(16, 96, 128), GenerationStep.Decoration.UNDERGROUND_DECORATION, BiomeTags.IS_NETHER));
        list.add(pf("ore_yeboshi_end", "ore_yeboshi_end", ore(4, 32, 48), GenerationStep.Decoration.UNDERGROUND_DECORATION, BiomeTags.IS_END));

        // 花 / 普通植物
        list.add(pf("plant_zhusun_bamboo_jungle", "plant_zhusun_bamboo_jungle", patch(2), GenerationStep.Decoration.VEGETAL_DECORATION, null, BAMBOO));
        list.add(pf("plant_xiaodengcao_dark_forest", "plant_xiaodengcao_dark_forest", patch(3), GenerationStep.Decoration.VEGETAL_DECORATION, null, DARK_FOREST));
        list.add(pf("plant_gougouguo_birch_forest", "plant_gougouguo_birch_forest", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, BIRCH));
        list.add(pf("plant_luoluomei_forest", "plant_luoluomei_forest", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, FOREST));
        list.add(pf("plant_dunduntao_forest", "plant_dunduntao_forest", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, FOREST));
        list.add(pf("plant_xiangxinguo_forest", "plant_xiangxinguo_forest", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, FOREST));
        list.add(pf("plant_tianyun_end_barrens", "plant_tianyun_end_barrens", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, END_MID));
        list.add(pf("plant_mingcao_small_end_islands", "plant_mingcao_small_end_islands", patch(3), GenerationStep.Decoration.VEGETAL_DECORATION, null, SMALL_END));
        list.add(pf("plant_liuli_baihe_flower_forest", "plant_liuli_baihe_flower_forest", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, FLOWER_FOREST));
        list.add(pf("plant_nichanghua_flower_forest", "plant_nichanghua_flower_forest", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, FLOWER_FOREST));
        list.add(pf("plant_piboye_flower_forest", "plant_piboye_flower_forest", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, FLOWER_FOREST));
        list.add(pf("plant_padishalan_flower_forest", "plant_padishalan_flower_forest", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, FLOWER_FOREST));
        list.add(pf("plant_mogu_jungle", "plant_mogu_jungle", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_JUNGLE));
        list.add(pf("plant_shuwang_jungle", "plant_shuwang_jungle", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_JUNGLE));
        list.add(pf("plant_songrong_taiga", "plant_songrong_taiga", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_TAIGA));
        list.add(pf("plant_bingwuhua_icy", "plant_bingwuhua_icy", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, ICY));
        list.add(pf("plant_jueyun_desert", "plant_jueyun_desert", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, DESERT));
        list.add(pf("plant_xuekui_badlands", "plant_xuekui_badlands", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_BADLANDS));
        list.add(pf("plant_mufeng_mushroom_fields", "plant_mufeng_mushroom_fields", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, MUSHROOM));
        list.add(pf("plant_fengcheju_plains", "plant_fengcheju_plains", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, PLAINS));
        list.add(pf("plant_pugongying_plains", "plant_pugongying_plains", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, PLAINS));
        list.add(pf("plant_xumi_qiangwei_plains", "plant_xumi_qiangwei_plains", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, PLAINS));
        list.add(pf("plant_mojiecao_plains", "plant_mojiecao_plains", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, PLAINS));
        list.add(pf("plant_bohe", "plant_bohe", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD));
        list.add(pf("plant_tiantianhua", "plant_tiantianhua", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD));
        list.add(pf("plant_shumei", "plant_shumei", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD));

        list.add(pf("plant_lianpeng_river", "plant_lianpeng_river", patch(4), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_RIVER));
        list.add(pf("plant_mawei_river", "plant_mawei_river", patch(4), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_RIVER));
        list.add(pf("plant_jinyucao_river", "plant_jinyucao_river", patch(4), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_RIVER));
        list.add(pf("plant_mawei_swamp", "plant_mawei_swamp", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, null, SWAMP));
        list.add(pf("plant_dudulian_beach", "plant_dudulian_beach", patch(4), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_BEACH));
        list.add(pf("plant_cecilia_mountain", "plant_cecilia_mountain", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_MOUNTAIN));
        list.add(pf("plant_liuli_dai_mountain", "plant_liuli_dai_mountain", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_MOUNTAIN));
        list.add(pf("plant_qingxin_mountain", "plant_qingxin_mountain", patch(1), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_MOUNTAIN));

        // 海洋
        list.add(pf("plant_haicao_ocean", "plant_haicao_ocean", ocean(16), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OCEAN));
        list.add(pf("plant_hailingzhi_ocean", "plant_hailingzhi_ocean", ocean(8), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OCEAN));
        list.add(pf("plant_haicao_beach", "plant_haicao_beach", ocean(8), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_BEACH));
        list.add(pf("plant_haicao_river", "plant_haicao_river", ocean(16), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_RIVER));
        list.add(pf("plant_hailingzhi_river", "plant_hailingzhi_river", ocean(8), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_RIVER));

        // 下界（必须 count_on_every_layer，任何 heightmap 都会指向基岩天花板）
        list.add(pf("plant_lieyanhua_crimson_forest", "plant_lieyanhua_crimson_forest", netherLayer(8), GenerationStep.Decoration.VEGETAL_DECORATION, null, CRIMSON));
        list.add(pf("plant_youdengxun_warped_forest", "plant_youdengxun_warped_forest", netherLayer(8), GenerationStep.Decoration.VEGETAL_DECORATION, null, WARPED));

        // 岩浆边（地下随机高度，自定义 Feature 自己判断岩浆）
        list.add(pf("plant_lieyanhua_huarui_lava", "plant_lieyanhua_huarui_lava", underground(40, 40, 80), GenerationStep.Decoration.VEGETAL_DECORATION, BiomeTags.IS_OVERWORLD));

        return List.copyOf(list);
    }
}
