package com.yuo.PaiMeng.datagen;

import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;

/**
 * 生成 data/paimeng/forge/biome_modifier/*.json
 * 每个 PlacedFeature 一条注入规则（等价于原来手写的 JSON）。
 */
public final class PMBiomeModifiers {

    private PMBiomeModifiers() {
    }

    public static void bootstrap(BootstapContext<BiomeModifier> context) {
        var biomeGetter = context.lookup(Registries.BIOME);
        var placedGetter = context.lookup(Registries.PLACED_FEATURE);

        for (PMWorldGenData.PfDef def : PMWorldGenData.placed()) {
            if (def.biomeTag() == null && def.biomes().isEmpty()) {
                continue; // 该 placed feature 不注入群系（例如树苗生长用的那几条）
            }
            HolderSet<Biome> biomes;
            if (def.biomeTag() != null) {
                biomes = biomeGetter.getOrThrow(def.biomeTag());
            } else {
                List<Holder<Biome>> holders = new ArrayList<>();
                for (ResourceKey<Biome> biomeKey : def.biomes()) {
                    holders.add(biomeGetter.getOrThrow(biomeKey));
                }
                biomes = HolderSet.direct(holders);
            }
            Holder<PlacedFeature> feature = placedGetter.getOrThrow(PMWorldGenData.placedKey(def.id()));
            context.register(ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS,
                            new ResourceLocation(com.yuo.PaiMeng.PaiMeng.MOD_ID, def.id())),
                    new ForgeBiomeModifiers.AddFeaturesBiomeModifier(biomes, HolderSet.direct(feature), def.step()));
        }
    }
}
