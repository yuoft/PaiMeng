package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Items.PMTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * 苹果树结构生成（树干 + 树叶 + 果实）
 * 1.20.1：generate(ISeedReader, ...) → place(FeaturePlaceContext)
 */
public class AppleTreeFeature extends Feature<NoneFeatureConfiguration> {
    private static final TagKey<Block> APPLE_TREE_GROW =
            TagKey.create(Registries.BLOCK, PMTags.APPLE_TREE_GROW);

    private BlockState CROP;

    public AppleTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    //延迟解析方块（FeatureInit 可能在 Block 注册前创建 Feature 实例）
    private BlockState log() {
        return PMBlocks.appleLog.get().defaultBlockState();
    }

    private BlockState leaves() {
        return PMBlocks.appleLeaf.get().defaultBlockState();
    }

    public boolean isAirOrLeaves(WorldGenLevel reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos);
        return state.isAir() || state.is(BlockTags.LEAVES) || state.canBeReplaced();
    }

    //生成规则
    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel reader = ctx.level();
        RandomSource rand = ctx.random();
        BlockPos pos = ctx.origin();

        if (this == FeatureInit.APPLE_TREE.get()) {
            this.CROP = PMBlocks.appleCrop.get().defaultBlockState();
        } else if (this == FeatureInit.SUN_APPLE_TREE.get()) {
            this.CROP = PMBlocks.sunAppleCrop.get().defaultBlockState();
        } else if (this == FeatureInit.PURPLE_APPLE_TREE.get()) {
            this.CROP = PMBlocks.purpleAppleCrop.get().defaultBlockState();
        } else if (this == FeatureInit.ZAOYE_TREE.get()) {
            this.CROP = PMBlocks.zaoyeCrop.get().defaultBlockState();
        } else if (this == FeatureInit.PAOPAOJU_TREE.get()) {
            this.CROP = PMBlocks.paopaojuCrop.get().defaultBlockState();
        }
        if (CROP == null) return false;

        while (pos.getY() > 1 && isAirOrLeaves(reader, pos)) { //合适的生成位置
            pos = pos.below();
        }
        BlockPos down = pos.below();
        BlockState state = reader.getBlockState(down);
        if (!state.is(APPLE_TREE_GROW) || !state.isSolid()) { //某些方块上生成
            return false;
        }
        // 树干
        int height = 4 + rand.nextInt(4);
        if (pos.getY() >= 1 && pos.getY() + 6 + 1 < reader.getHeight()) { //生成高度限制
            for (int i = pos.getY() + 1; i < pos.getY() + height + 1; i++) {
                reader.setBlock(new BlockPos(pos.getX(), i, pos.getZ()), log(), 3);
            }
        } else {
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
                    if (Math.abs(xOffset) != xzSize || Math.abs(zOffset) != xzSize // 不在边缘4个点
                            || rand.nextInt(2) != 0 && restHeight != 0) {
                        BlockPos blockpos = new BlockPos(x, y, z);

                        if (reader.isEmptyBlock(blockpos)) {
                            reader.setBlock(blockpos, leaves().setValue(LeavesBlock.DISTANCE,
                                    Math.max(Math.min(Math.abs(xOffset + zOffset - 1), 7), 1)), 3);
                            //果实 20%概率生成
                            if (reader.isEmptyBlock(blockpos.below()) && rand.nextInt(100) > 80) {
                                reader.setBlock(new BlockPos(x, y - 1, z), CROP, 3);
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
