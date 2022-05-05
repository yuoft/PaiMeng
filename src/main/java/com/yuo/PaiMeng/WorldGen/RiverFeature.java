package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;

import java.util.Random;

public class RiverFeature extends Feature<PlantConfig> {
    public RiverFeature(Codec<PlantConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, PlantConfig config) {
        BlockState blockstate = config.getState(); //植物
        if(reader.getFluidState(pos).isTagged(FluidTags.WATER)) return false; //当前位置不能是水
        BlockState state = reader.getBlockState(pos.down()); //下方是沙子 泥土
        boolean water = isWater(pos, reader);
        if (water && (state.matchesBlock(Blocks.DIRT) || state.matchesBlock(Blocks.GRASS_BLOCK) || state.matchesBlock(Blocks.SAND)) ){
            reader.setBlockState(pos, blockstate, 3);
            return true;
        }
        return false;
    }

    /**
     * 周围一格内有没有水
     * @param pos
     * @param reader
     * @return
     */
    private boolean isWater(BlockPos pos, ISeedReader reader){
        Iterable<BlockPos> allInBox = BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 0, 1));
        for (BlockPos inBox : allInBox) {
            if (reader.getFluidState(inBox).isTagged(FluidTags.WATER)) return true;
        }
        return false;
    }
}
