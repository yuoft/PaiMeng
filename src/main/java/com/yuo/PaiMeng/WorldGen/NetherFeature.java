package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class NetherFeature extends Feature<NetherConfig> {
    public NetherFeature(Codec<NetherConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NetherConfig config) {
        BlockState blockState = reader.getBlockState(pos.down());
        if (blockState.matchesBlock(config.getStateDown().getBlock()) && reader.isAirBlock(pos.up())){ //下方是绯红菌岩 上方空气
            reader.setBlockState(pos, config.getState(), 3);
            return true;
        }
        return false;
    }
}
