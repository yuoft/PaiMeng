package com.yuo.paimeng.worldgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * 树苗生成器：持有一个 ConfiguredFeature 的 key，在世界中放置对应的树。
 * 1.20.1 中树的生成走 ConfiguredFeature#place(WorldGenLevel, ChunkGenerator, RandomSource, BlockPos)。
 */
public class TreeSpawner {
    @Nullable
    private final ResourceKey<ConfiguredFeature<?, ?>> feature;

    public TreeSpawner(@Nullable ResourceKey<ConfiguredFeature<?, ?>> feature) {
        this.feature = feature;
    }

    @Nullable
    public ResourceKey<ConfiguredFeature<?, ?>> key() {
        return this.feature;
    }

    /**
     * 1.20.1 主入口
     */
    public boolean spawn(WorldGenLevel level, ChunkGenerator generator, BlockPos pos, BlockState blockUnder, RandomSource random) {
        if (this.feature == null) return false;
        Optional<Holder.Reference<ConfiguredFeature<?, ?>>> holder =
                level.registryAccess().registryOrThrow(Registries.CONFIGURED_FEATURE).getHolder(this.feature);
        if (holder.isEmpty()) return false;

        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 4);
        if (holder.get().value().place(level, generator, random, pos)) {
            return true;
        }

        level.setBlock(pos, blockUnder, 4);
        return false;
    }

    /**
     * 兼容 1.16.5 的 Random 调用（AppleSapling 仍在用）
     */
    public boolean spawn(WorldGenLevel level, ChunkGenerator generator, BlockPos pos, BlockState blockUnder, java.util.Random random) {
        return this.spawn(level, generator, pos, blockUnder, RandomSource.create(random.nextLong()));
    }
}
