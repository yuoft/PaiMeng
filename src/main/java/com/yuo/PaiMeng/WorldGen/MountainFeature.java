package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * 高山植物：只在 y>=96 生成，在附近随机位置尝试放置
 */
public class MountainFeature extends Feature<PlantConfig> {
    public MountainFeature(Codec<PlantConfig> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<PlantConfig> ctx) {
        WorldGenLevel reader = ctx.level();
        RandomSource rand = ctx.random();
        BlockPos pos = ctx.origin();
        PlantConfig config = ctx.config();

        if (pos.getY() < 96) return false; //高度不足

        BlockState blockstate = config.getState();
        int i = 0;
        for (int j = 0; j < config.getTries(); ++j) {
            BlockPos blockpos = this.getNearbyPos(rand, pos, 7, 3, 7);
            if (reader.isEmptyBlock(blockpos) && blockpos.getY() < reader.getHeight()
                    && blockstate.canSurvive(reader, blockpos)) {
                reader.setBlock(blockpos, blockstate, 3);
                ++i;
            }
        }
        return i > 0;
    }

    public BlockPos getNearbyPos(RandomSource rand, BlockPos pos, int xSpread, int ySpread, int zSpread) {
        return pos.offset(rand.nextInt(xSpread) - rand.nextInt(xSpread),
                rand.nextInt(ySpread) - rand.nextInt(ySpread),
                rand.nextInt(zSpread) - rand.nextInt(zSpread));
    }
}
