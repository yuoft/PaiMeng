package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class MountainFeature extends Feature<BlockClusterFeatureConfig> {
    public MountainFeature(Codec<BlockClusterFeatureConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, BlockClusterFeatureConfig config) {
        BlockState blockstate = this.getFlowerToPlace(rand, pos, config);
        int i = 0;
        if (pos.getY() < 96) return false; //高度不足

        for(int j = 0; j < this.getFlowerCount(config); ++j) {
            BlockPos blockpos = this.getNearbyPos(rand, pos, config);
            if (reader.isAirBlock(blockpos) && blockpos.getY() < 255 && blockstate.isValidPosition(reader, blockpos) && this.isValidPosition(reader, blockpos, config)) {
                reader.setBlockState(blockpos, blockstate, 3);
                ++i;
            }
        }

        return i > 0;
    }

    public boolean isValidPosition(IWorld world, BlockPos pos, BlockClusterFeatureConfig config) {
        return !config.blacklist.contains(world.getBlockState(pos));
    }

    public int getFlowerCount(BlockClusterFeatureConfig config) {
        return config.tryCount;
    }

    public BlockPos getNearbyPos(Random rand, BlockPos pos, BlockClusterFeatureConfig config) {
        return pos.add(rand.nextInt(config.xSpread) - rand.nextInt(config.xSpread), rand.nextInt(config.ySpread) - rand.nextInt(config.ySpread), rand.nextInt(config.zSpread) - rand.nextInt(config.zSpread));
    }

    public BlockState getFlowerToPlace(Random rand, BlockPos pos, BlockClusterFeatureConfig confgi) {
        return confgi.stateProvider.getBlockState(rand, pos);
    }
}
