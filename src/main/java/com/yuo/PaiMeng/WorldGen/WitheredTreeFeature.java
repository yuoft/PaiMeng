package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import com.yuo.PaiMeng.Blocks.Crop.WallPlant;
import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Items.CropBlockItem;
import com.yuo.PaiMeng.Items.PMTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

/**
 * 枯木结构生成（树干 + 倒木 + 侧面蘑菇）
 * 1.20.1：generate(ISeedReader, ...) → place(FeaturePlaceContext)
 */
public class WitheredTreeFeature extends Feature<NoneFeatureConfiguration> {
    private static final TagKey<Block> APPLE_TREE_GROW =
            TagKey.create(Registries.BLOCK, PMTags.APPLE_TREE_GROW);

    // 延迟解析方块（FeatureInit 可能在 Block 注册前创建 Feature 实例）
    private BlockState log() {
        return PMBlocks.witheredLog.get().defaultBlockState();
    }

    private BlockState[] plants() {
        return new BlockState[]{
                PMBlocks.moguWallPlant.get().defaultBlockState(),
                PMBlocks.mufengMoguWallPlant.get().defaultBlockState(),
                PMBlocks.songrongWallPlant.get().defaultBlockState(),
                PMBlocks.shuwangShengtiguWallPlant.get().defaultBlockState(),
                PMBlocks.youdengxunWallPlant.get().defaultBlockState()};
    }

    private BlockState[] upPlants() {
        return new BlockState[]{
                PMBlocks.moguPlant.get().defaultBlockState(),
                PMBlocks.mufengMoguPlant.get().defaultBlockState(),
                PMBlocks.songrongPlant.get().defaultBlockState(),
                PMBlocks.shuwangShengtiguPlant.get().defaultBlockState(),
                PMBlocks.youdengxunPlant.get().defaultBlockState()};
    }

    public WitheredTreeFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    public boolean isAirOrLeaves(WorldGenLevel reader, BlockPos pos) {
        BlockState state = reader.getBlockState(pos);
        return state.isAir() || state.is(BlockTags.LEAVES) || state.canBeReplaced();
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel reader = ctx.level();
        RandomSource rand = ctx.random();
        BlockPos pos = ctx.origin();

        while (pos.getY() > 1 && isAirOrLeaves(reader, pos)) { //合适的生成位置
            pos = pos.below();
        }
        BlockPos down = pos.below();
        BlockState state = reader.getBlockState(down);
        if (!state.is(APPLE_TREE_GROW) || !state.isSolid() || !state.getFluidState().isEmpty()) { //某些方块上生成，不能生成在非固体方块上
            return false;
        }
        // 树干
        int height = 3 + rand.nextInt(4);
        if (pos.getY() >= 10 && pos.getY() + 10 + 1 < reader.getHeight()) { //生成高度限制
            if (rand.nextDouble() > 0.7) {
                spawnFallTree(reader, pos, rand);
            } else {
                for (int i = pos.getY() + 1; i < pos.getY() + height + 1; i++) {
                    BlockPos blockPos = new BlockPos(pos.getX(), i, pos.getZ());
                    reader.setBlock(blockPos, log(), 3);
                    spawnWallPlant(reader, blockPos, rand);
                }
            }
        } else return false;
        return true;
    }

    /**
     * 生成倒下的树干
     */
    private void spawnFallTree(WorldGenLevel world, BlockPos pos, RandomSource random) {
        int height = 4;
        double v = random.nextDouble();
        double rand = random.nextDouble();
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        if (v > 0.5d) {
            if (rand > 0.5d) {
                for (int i = posX; i < posX + height; i++) {
                    setLOG(world, new BlockPos(i, posY, posZ), random, Direction.Axis.X);
                }
            } else {
                for (int i = posX; i > posX - height; i--) {
                    setLOG(world, new BlockPos(i, posY, posZ), random, Direction.Axis.X);
                }
            }
        } else {
            if (rand > 0.5d) {
                for (int i = posZ; i < posZ + height; i++) {
                    setLOG(world, new BlockPos(posX, posY, i), random, Direction.Axis.Z);
                }
            } else {
                for (int i = posZ; i > posZ - height; i--) {
                    setLOG(world, new BlockPos(posX, posY, i), random, Direction.Axis.Z);
                }
            }
        }
    }

    private void setLOG(WorldGenLevel world, BlockPos blockPos, RandomSource random, Direction.Axis axis) {
        if (!world.isEmptyBlock(blockPos)) {
            BlockPos up = blockPos.above();
            world.setBlock(up, log().setValue(RotatedPillarBlock.AXIS, axis), 3);
            spawnWallPlant(world, up, random);
        } else if (world.isEmptyBlock(blockPos) && world.isEmptyBlock(blockPos.below()) && !world.isEmptyBlock(blockPos.below(2))) {
            BlockPos down = blockPos.below(2);
            world.setBlock(down, log().setValue(RotatedPillarBlock.AXIS, axis), 3);
            spawnWallPlant(world, down, random);
        } else {
            world.setBlock(blockPos, log().setValue(RotatedPillarBlock.AXIS, axis), 3);
            spawnWallPlant(world, blockPos, random);
        }
    }

    /**
     * 随机生成蘑菇
     */
    private void spawnWallPlant(WorldGenLevel world, BlockPos blockPos, RandomSource rand) {
        //30%生成蘑菇
        if (rand.nextDouble() < 0.3d) {
            for (Direction direction : Direction.values()) {
                if (direction == Direction.DOWN || rand.nextDouble() > 0.6d) continue;
                BlockPos pos = blockPos.relative(direction);
                if (world.isEmptyBlock(pos)) {
                    if (direction == Direction.UP) {
                        world.setBlock(pos, upPlants()[rand.nextInt(4)], 3);
                    } else {
                        world.setBlock(pos, plants()[rand.nextInt(4)].setValue(WallPlant.FACING,
                                CropBlockItem.rotate(direction)), 3);
                    }
                }
            }
        }
    }
}
