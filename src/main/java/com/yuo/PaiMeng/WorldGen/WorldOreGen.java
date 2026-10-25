package com.yuo.PaiMeng.WorldGen;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import net.minecraftforge.registries.RegisterEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

/**
 * 矿物生成（1.20.1：PlacedFeature + data/paimeng/forge/biome_modifier/*.json）
 * 每个 key 对应一条 biome_modifier 注入规则。
 */
public class WorldOreGen {

    public static final ResourceKey<PlacedFeature> ORE_XINGYIN_ICY = key("ore_xingyin_icy");            //冰原
    public static final ResourceKey<PlacedFeature> ORE_SHIPO_DESERT = key("ore_shipo_desert");          //沙漠
    public static final ResourceKey<PlacedFeature> ORE_DIANQI_OCEAN = key("ore_dianqi_ocean");          //海洋
    public static final ResourceKey<PlacedFeature> ORE_JINGHUA_BADLANDS = key("ore_jinghua_badlands");  //平顶山
    public static final ResourceKey<PlacedFeature> ORE_SHUIJING_BADLANDS = key("ore_shuijing_badlands");//平顶山
    public static final ResourceKey<PlacedFeature> ORE_SHUIJING = key("ore_shuijing");                  //主世界
    public static final ResourceKey<PlacedFeature> ORE_BAITIE = key("ore_baitie");                      //主世界
    public static final ResourceKey<PlacedFeature> ORE_HEITIE = key("ore_heitie");                      //主世界
    public static final ResourceKey<PlacedFeature> ORE_MOJING_NETHER = key("ore_mojing_nether");        //下界
    public static final ResourceKey<PlacedFeature> ORE_ZIJING_NETHER = key("ore_zijing_nether");        //下界
    public static final ResourceKey<PlacedFeature> ORE_YEBOSHI_END = key("ore_yeboshi_end");            //末地

    private static ResourceKey<PlacedFeature> key(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, new ResourceLocation(PaiMeng.MOD_ID, name));
    }

    /**
     * 在 RegisterEvent 的 PLACED_FEATURE 阶段被调用
     */
    public static void register(RegisterEvent event) {
        if (!event.getRegistryKey().equals(Registries.PLACED_FEATURE)) return;

        RuleTest stone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest nether = new TagMatchTest(BlockTags.BASE_STONE_NETHER);
        RuleTest end = new BlockMatchTest(Blocks.END_STONE);

        //主世界
        register(event, ORE_XINGYIN_ICY, state(PMBlocks.xingyinOre), stone, 8, 6, 8, 32);
        register(event, ORE_SHIPO_DESERT, state(PMBlocks.shipoOre), stone, 4, 4, 16, 64);
        register(event, ORE_DIANQI_OCEAN, state(PMBlocks.dianqiShuijingOre), stone, 4, 4, 8, 48);
        register(event, ORE_JINGHUA_BADLANDS, state(PMBlocks.jinghuaGusuiOre), stone, 4, 4, 16, 48);
        register(event, ORE_SHUIJING_BADLANDS, state(PMBlocks.shuijingOre), stone, 4, 6, 0, 16);
        register(event, ORE_SHUIJING, state(PMBlocks.shuijingOre), stone, 4, 4, 8, 32);
        register(event, ORE_BAITIE, state(PMBlocks.baitieOre), stone, 8, 6, 0, 32);
        register(event, ORE_HEITIE, state(PMBlocks.heitieOre), stone, 10, 8, 0, 96);
        //下界
        register(event, ORE_MOJING_NETHER, state(PMBlocks.mojingOre), nether, 5, 16, 8, 32);
        register(event, ORE_ZIJING_NETHER, state(PMBlocks.zijingOre), nether, 5, 16, 96, 128);
        //末地
        register(event, ORE_YEBOSHI_END, state(PMBlocks.yeboshiOre), end, 4, 4, 32, 48);
    }

    private static BlockState state(RegistryObject<? extends Block> block) {
        return block.get().defaultBlockState();
    }

    private static void register(RegisterEvent event, ResourceKey<PlacedFeature> key, BlockState ore, RuleTest rule,
                                 int size, int count, int minY, int maxY) {
        event.register(Registries.PLACED_FEATURE, key.location(), () -> new PlacedFeature(
                Holder.direct(new ConfiguredFeature<>(Feature.ORE,
                        new OreConfiguration(List.of(OreConfiguration.target(rule, ore)), size))),
                List.of(
                        CountPlacement.of(count),
                        InSquarePlacement.spread(),
                        HeightRangePlacement.uniform(VerticalAnchor.absolute(minY), VerticalAnchor.absolute(maxY)),
                        BiomeFilter.biome()
                )));
    }
}
