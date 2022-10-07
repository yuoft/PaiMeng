package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import com.yuo.PaiMeng.Blocks.Crop.WallPlant;
import com.yuo.PaiMeng.Items.CropBlockItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.treedecorator.CocoaTreeDecorator;

import java.util.Random;

public class WallFeature extends Feature<PlantConfig> {
    public WallFeature(Codec<PlantConfig> codec) {
        super(codec);
    }

    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, PlantConfig config) {
        BlockState blockstate = config.getState(); //植物
        if(!reader.isAirBlock(pos)) return false; //当前位置是空气
        for (Direction direction : Direction.values()){
            BlockPos offset = pos.offset(direction);
            BlockState state = reader.getBlockState(offset);
            if (state.isIn(BlockTags.LOGS)){
                reader.setBlockState(pos, blockstate.with(WallPlant.FACING, CropBlockItem.rotate(direction)), 3);
                return true;
            }
        }
        return false;
    }
}
