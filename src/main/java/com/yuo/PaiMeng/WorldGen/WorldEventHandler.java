package com.yuo.PaiMeng.WorldGen;


import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStage;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Features;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.placement.AtSurfaceWithExtraConfig;
import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
//世界生成
@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID)
public class WorldEventHandler {

    @SubscribeEvent
    public static void biomeLoading(final BiomeLoadingEvent event){
        WorldOreGen.generateOres(event); //矿物
        WorldPlantGen.generatePlants(event); //植物
        genTree(event); //树
        EntityGen.onEntitySpawn(event); //实体
    }

    //树生成
    public static void genTree(final BiomeLoadingEvent event){
        //获取生物群系列表
        if (event.getName() == null) return;
        RegistryKey<Biome> key = RegistryKey.getOrCreateKey(Registry.BIOME_KEY, event.getName());
        Set<BiomeDictionary.Type> types = BiomeDictionary.getTypes(key);

        if (types.contains(BiomeDictionary.Type.FOREST)){ //森林
            List<Supplier<ConfiguredFeature<?, ?>>> features = event.getGeneration().getFeatures(GenerationStage.Decoration.VEGETAL_DECORATION);
            //添加自定义树世界生成规则
            features.add(() -> FeatureInit.APPLE_TREE.get().withConfiguration(NoFeatureConfig.INSTANCE)
                    .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(
                            new AtSurfaceWithExtraConfig(0, 0.01f, 1) //生成次数 额外生成机会 额外生成次数
                    )));
            features.add(() -> FeatureInit.WITHERED_TREE.get().withConfiguration(NoFeatureConfig.INSTANCE)
                    .withPlacement(Features.Placements.HEIGHTMAP_PLACEMENT).withPlacement(Placement.COUNT_EXTRA.configure(
                            new AtSurfaceWithExtraConfig(1, 0.005f, 1) //生成次数 额外生成机会 额外生成次数
                    )));
        }
    }
}
