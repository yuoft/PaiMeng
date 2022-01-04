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
        for (BlockPos blockPos : BlockPos.getAllInBoxMutable(pos.add(-4, -4, -4), pos.add(4, 4, 4))) {
            if(reader.getFluidState(blockPos).isTagged(FluidTags.WATER)) return false; //当前位置不能是水
            BlockState state = reader.getBlockState(blockPos.down()); //下方是沙子 泥土 上方是空气
            boolean water = isWater(blockPos, reader);
            if ( water && reader.isAirBlock(blockPos.up()) && (state.isIn(Blocks.DIRT) || state.isIn(Blocks.GRASS_BLOCK) || state.isIn(Blocks.SAND)) ){
                reader.setBlockState(pos, blockstate, 3);
                return true;
            }
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
        Iterable<BlockPos> allInBox = BlockPos.getAllInBoxMutable(pos.add(-1, 0, -1), pos.add(1, 0, 1));
        for (BlockPos inBox : allInBox) {
            if (reader.getFluidState(inBox).isTagged(FluidTags.WATER)) return true;
        }
        return false;
    }
}
