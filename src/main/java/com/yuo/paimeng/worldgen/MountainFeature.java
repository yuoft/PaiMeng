package com.yuo.paimeng.worldgen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * 高山植物：只在 y>=96 生成，寻找真实地表放置，避免悬空或卡在山体内部
 */
public class MountainFeature extends Feature<PlantConfig> {
    public MountainFeature(Codec<PlantConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlantConfig> ctx) {
        WorldGenLevel reader = ctx.level();
        RandomSource rand = ctx.random();
        BlockPos origin = ctx.origin();
        PlantConfig config = ctx.config();

        // 如果原点高度本身就不够，直接退出
        if (origin.getY() < 96) return false;

        BlockState blockstate = config.getState();
        int placed = 0;

        for (int j = 0; j < config.getTries(); ++j) {
            // 1. 只在水平方向随机偏移（±7格）
            int x = origin.getX() + rand.nextInt(15) - 7;
            int z = origin.getZ() + rand.nextInt(15) - 7;

            // 2. 获取该坐标的真实地表高度 (WORLD_SURFACE)
            int y = reader.getHeight(Heightmap.Types.WORLD_SURFACE, x, z);

            // 限制高山高度，如果地表太低就不生成
            if (y < 96 || y >= reader.getHeight()) continue;

            BlockPos targetPos = new BlockPos(x, y, z);

            // 3. 检查当前是否是空气（虽然地表上方通常是空气，但为了防止长进树里，还是检查一下）
            // 并且检查方块是否可以在这里存活（即下方是否是石头/雪块等）
            if (reader.isEmptyBlock(targetPos) && blockstate.canSurvive(reader, targetPos)) {
                reader.setBlock(targetPos, blockstate, 3);
                ++placed;
            }
        }
        return placed > 0;
    }
}