package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Items.PMTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

//树结构生成规则
public class AppleTreeFeature extends Feature<NoFeatureConfig> {
    private final BlockState LOG = PMBlocks.appleLog.get().getDefaultState();
    private final BlockState LEAVES = PMBlocks.appleLeaf.get().getDefaultState();
    private BlockState CROP;

    public AppleTreeFeature(Codec<NoFeatureConfig> codec) {
        super(codec);
    }

    public boolean isAirOrLeaves(IWorldGenerationBaseReader reader, BlockPos pos) {
        if (!(reader instanceof IWorldReader)) {
            return reader.hasBlockState(pos, state -> state.isAir() || state.isIn(BlockTags.LEAVES));
        } else {
            return reader.hasBlockState(pos, state -> state.canBeReplacedByLeaves((IWorldReader) reader, pos));
        }
    }

    //生成规则
    @Override
    public boolean generate(ISeedReader reader, ChunkGenerator generator, Random rand, BlockPos pos, NoFeatureConfig config) {
        if (this == FeatureInit.APPLE_TREE.get()){
            this.CROP = PMBlocks.appleCrop.get().getDefaultState();
        }else if (this == FeatureInit.SUN_APPLE_TREE.get()){
            this.CROP = PMBlocks.sunAppleCrop.get().getDefaultState();
        }else if (this == FeatureInit.PURPLE_APPLE_TREE.get()){
            this.CROP = PMBlocks.purpleAppleCrop.get().getDefaultState();
        }else if (this == FeatureInit.ZAOYE_TREE.get()){
            this.CROP = PMBlocks.zaoyeCrop.get().getDefaultState();
        }
        if (CROP == null) return false;
        while (pos.getY() > 1 && isAirOrLeaves(reader, pos)) { //合适的生成位置
            pos = pos.down();
        }
        BlockPos down = pos.down();
        BlockState state = reader.getBlockState(down);
        ITag<Block> blockITag = BlockTags.getCollection().get(PMTags.APPLE_TREE_GROW);
        if (blockITag == null || !state.getBlock().isIn(blockITag) || !state.isSolid()){ //某些方块上生成
            return false;
        }
        // 树干
        int height = 4 + rand.nextInt(4);
        if (pos.getY() >= 1 && pos.getY() + 6 + 1 < reader.getHeight()) { //生成高度限制
            for (int i = pos.getY() + 1; i < pos.getY() + height + 1; i++) {
                reader.setBlockState(new BlockPos(pos.getX(), i, pos.getZ()), LOG, 3);
            }
        }
        else {
            return false;
        }
        // 树叶
        for (int y = pos.getY() + 2 + rand.nextInt(2); y <= pos.getY() + height + 1; ++y) {
            int restHeight = y - (pos.getY() + height + 1); //与树干顶端距离
            int xzSize = 1 - restHeight / 2; //树叶范围
            for (int x = pos.getX() - xzSize; x <= pos.getX() + xzSize; ++x) {
                int xOffset = x - pos.getX();
                for (int z = pos.getZ() - xzSize; z <= pos.getZ() + xzSize; ++z) {
                    int zOffset = z - pos.getZ();
                    if (   Math.abs(xOffset) != xzSize || Math.abs(zOffset) != xzSize // 不在边缘4个点
                            || rand.nextInt(2) != 0 && restHeight != 0) {
                        BlockPos blockpos = new BlockPos(x, y, z);

                        if (reader.isAirBlock(blockpos)) {
                            reader.setBlockState(blockpos, LEAVES.with(LeavesBlock.DISTANCE, Math.max(Math.min(Math.abs(xOffset + zOffset - 1), 7), 1)), 3);
                            //果实 20%概率生成
                            if (reader.isAirBlock(blockpos.down()) && rand.nextInt(100) > 80){
                                reader.setBlockState(new BlockPos(x, y - 1, z), CROP, 3);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
