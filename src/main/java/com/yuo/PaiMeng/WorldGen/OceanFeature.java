package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import com.yuo.PaiMeng.Blocks.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class OceanFeature extends Feature<OceanProbabilityConfig> {
    public OceanFeature(Codec<OceanProbabilityConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, OceanProbabilityConfig config) {
        int k = reader.getHeight(Heightmap.Type.OCEAN_FLOOR, pos.getX() , pos.getZ()); //水下第一个非液体方块高度
        if (reader.getBlockState(pos).matchesBlock(Blocks.WATER)) { //当前位置有水
            boolean flag = rand.nextDouble() < (double)config.probability;
            BlockState blockstate = config.getState();
            BlockState state = reader.getBlockState(pos.down());
            if ((state.matchesBlock(Blocks.SAND) || state.matchesBlock(Blocks.GRAVEL)) && flag){ //下面是沙子或沙砾
                if (blockstate.matchesBlock(BlockRegistry.haicaoPlant.get()) && k + 6 >= 63){ //海草 距海平面小于6格
                    reader.setBlockState(pos, blockstate, 3);
                    return true;
                }
                if (blockstate.matchesBlock(BlockRegistry.hailingzhiPlant.get()) && k + 15 <= 63){ //海灵芝 大于15格
                    reader.setBlockState(pos, blockstate, 3);
                    return true;
                }
            }
        }
        return false;
    }
}
