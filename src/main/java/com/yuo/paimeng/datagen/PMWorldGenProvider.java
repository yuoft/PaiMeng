package com.yuo.paimeng.datagen;

import com.yuo.paimeng.PaiMeng;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 世界生成数据包条目（configured_feature / placed_feature / biome_modifier）的 datagen Provider。
 */
public class PMWorldGenProvider extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.CONFIGURED_FEATURE, PMConfiguredFeatures::bootstrap)
            .add(Registries.PLACED_FEATURE, PMPlacedFeatures::bootstrap)
            .add(ForgeRegistries.Keys.BIOME_MODIFIERS, PMBiomeModifiers::bootstrap);

    public PMWorldGenProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(PaiMeng.MOD_ID));
    }

    @Override
    public String getName() {
        return "PaiMeng Worldgen (" + DataProvider.class.getSimpleName() + ")";
    }
}
