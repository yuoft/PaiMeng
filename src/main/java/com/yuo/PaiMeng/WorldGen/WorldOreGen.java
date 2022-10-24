package com.yuo.PaiMeng.WorldGen;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.template.BlockMatchRuleTest;
import net.minecraft.world.gen.placement.Placement;
import net.minecraft.world.gen.placement.TopSolidRangeConfig;
import net.minecraftforge.common.world.BiomeGenerationSettingsBuilder;
import net.minecraftforge.event.world.BiomeLoadingEvent;

/**
 * 矿物生成
 */
public class WorldOreGen {
    private static int topOffset = 0;

    public static void generateOres(final BiomeLoadingEvent event) {
        BiomeGenerationSettingsBuilder generation = event.getGeneration();
        //主世界
        if (!(event.getCategory().equals(Biome.Category.THEEND) || event.getCategory().equals(Biome.Category.NETHER))){
            if (event.getCategory().equals(Biome.Category.ICY)){ //冰原
                addFeatureOverWorld(generation, PMBlocks.xingyinOre.get().getDefaultState(),
                        8, 8, 32, 6);
            }
            if (event.getCategory().equals(Biome.Category.DESERT)){ //沙漠
                addFeatureOverWorld(generation, PMBlocks.shipoOre.get().getDefaultState(),
                        4, 16, 64, 4);
            }
            if (event.getCategory().equals(Biome.Category.OCEAN)){ //海洋
                addFeatureOverWorld(generation, PMBlocks.dianqiShuijingOre.get().getDefaultState(),
                        4, 8, 48, 4);
            }
            if (event.getCategory().equals(Biome.Category.MESA)){ //平顶山
                addFeatureOverWorld(generation, PMBlocks.jinghuaGusuiOre.get().getDefaultState(),
                        4, 16, 48, 4);
                addFeatureOverWorld(generation, PMBlocks.shuijingOre.get().getDefaultState(),
                        4, 0, 16, 6);
            }
            addFeatureOverWorld(generation, PMBlocks.shuijingOre.get().getDefaultState(),
                    4, 8, 32, 4);
            addFeatureOverWorld(generation, PMBlocks.baitieOre.get().getDefaultState(),
                    8, 0, 32, 6);
            addFeatureOverWorld(generation, PMBlocks.heitieOre.get().getDefaultState(),
                    10, 0, 96, 8);
        }
        //下届
        if (event.getCategory().equals(Biome.Category.NETHER)){
            addFeatureNether(generation, PMBlocks.mojingOre.get().getDefaultState(),
                    5, 8, 32, 16);
            addFeatureNether(generation, PMBlocks.zijingOre.get().getDefaultState(),
                    5, 96, 128, 16);
        }
        //末地
        if (event.getCategory().equals(Biome.Category.THEEND)){
            addFeatureTheend(generation, PMBlocks.yeboshiOre.get().getDefaultState(),
                    4, 32, 48, 4);
        }
    }

    /**
     * 主世界矿物生成规则
     */
    private static void addFeatureOverWorld(BiomeGenerationSettingsBuilder builder, BlockState state, int maxSize,
                                            int minHeight, int maxHeight, int genCount){
        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_ORES,
                Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.BASE_STONE_OVERWORLD, state, maxSize)) // 替换方块, 生成矿物， 最大生成数量
                        .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, topOffset , maxHeight))) //最低高度, 0,最高高度
                        .square().count(genCount)); //生成次数
    }
    /**
     * 下届矿物生成规则
     */
    private static void addFeatureNether(BiomeGenerationSettingsBuilder builder, BlockState state, int maxSize,
                                         int minHeight, int maxHeight, int genCount){
        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_DECORATION,
                Feature.ORE.withConfiguration(new OreFeatureConfig(OreFeatureConfig.FillerBlockType.NETHERRACK, state, maxSize))
                        .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, topOffset , maxHeight)))
                        .square().count(genCount));
    }
    /**
     * 末地矿物生成规则
     */
    private static void addFeatureTheend(BiomeGenerationSettingsBuilder builder, BlockState state, int maxSize,
                                         int minHeight, int maxHeight, int genCount){
        builder.withFeature(GenerationStage.Decoration.UNDERGROUND_STRUCTURES,
                Feature.ORE.withConfiguration(new OreFeatureConfig(new BlockMatchRuleTest(Blocks.END_STONE), state, maxSize))
                        .withPlacement(Placement.RANGE.configure(new TopSolidRangeConfig(minHeight, topOffset , maxHeight)))
                        .square().count(genCount));
    }
}
