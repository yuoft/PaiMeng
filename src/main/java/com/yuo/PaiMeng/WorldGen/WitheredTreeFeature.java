package com.yuo.PaiMeng.WorldGen;

import com.mojang.serialization.Codec;
import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Blocks.Crop.WallPlant;
import com.yuo.PaiMeng.Items.CropBlockItem;
import com.yuo.PaiMeng.Items.PMTags;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.IWorldGenerationBaseReader;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

//树结构生成规则
public class WitheredTreeFeature extends Feature<NoFeatureConfig> {
    private final BlockState LOG = PMBlocks.witheredLog.get().getDefaultState();
    private final BlockState[] PLANTS = new BlockState[]{
            PMBlocks.moguWallPlant.get().getDefaultState(),
            PMBlocks.mufengMoguWallPlant.get().getDefaultState(),
            PMBlocks.songrongWallPlant.get().getDefaultState(),
            PMBlocks.youdengxunWallPlant.get().getDefaultState()};
    private final BlockState[] UP_PLANTS = new BlockState[]{
            PMBlocks.moguPlant.get().getDefaultState(),
            PMBlocks.mufengMoguPlant.get().getDefaultState(),
            PMBlocks.songrongPlant.get().getDefaultState(),
            PMBlocks.youdengxunPlant.get().getDefaultState()};
    public WitheredTreeFeature(Codec<NoFeatureConfig> codec) {
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
        while (pos.getY() > 1 && isAirOrLeaves(reader, pos)) { //合适的生成位置
            pos = pos.down();
        }
        BlockPos down = pos.down();
        BlockState state = reader.getBlockState(down);
        ITag<Block> blockITag = BlockTags.getCollection().get(PMTags.APPLE_TREE_GROW);
        if (blockITag == null || !state.getBlock().isIn(blockITag) || !state.isSolid()){ //某些方块上生成 不能生成在非固体方块上
            return false;
        }
        // 树干
        int height = 3 + rand.nextInt(4);
        if (pos.getY() >= 10 && pos.getY() + 10 + 1 < reader.getHeight()) { //生成高度限制
            if (rand.nextDouble() > 0.7){
                spawnFallTree(reader, pos, rand);
            }else {
                for (int i = pos.getY() + 1; i < pos.getY() + height + 1; i++) {
                    BlockPos blockPos = new BlockPos(pos.getX(), i, pos.getZ());
                    reader.setBlockState(blockPos, LOG, 3);
                    spawnWallPlant(reader, blockPos, rand);
                }
            }
        } else return false;
        return true;
    }

    /**
     * 生成倒下的树干
     * @param world 世界
     * @param pos 坐标
     */
    private void spawnFallTree(ISeedReader world, BlockPos pos, Random random){
        int height = 4;
        double v = random.nextDouble();
        double rand = random.nextDouble();
        int posX = pos.getX();
        int posY = pos.getY();
        int posZ = pos.getZ();
        if (v > 0.5d){
            if (rand > 0.5d){
                for (int i = posX; i < posX + height; i++){
                    BlockPos blockPos = new BlockPos(i, posY, posZ);
                    setLOG(world, blockPos, random, Direction.Axis.X);
                }
            }else {
                for (int i = posX; i > posX - height; i--){
                    BlockPos blockPos = new BlockPos(i, posY, posZ);
                    setLOG(world, blockPos, random, Direction.Axis.X);
                }
            }
        }else {
            if (rand > 0.5d){
                for (int i = posZ; i < posZ + height; i++){
                    BlockPos blockPos = new BlockPos(posX, posY, i);
                    setLOG(world, blockPos, random, Direction.Axis.Z);
                }
            }else {
                for (int i = posZ; i > posZ - height; i--){
                    BlockPos blockPos = new BlockPos(posX, posY, i);
                    setLOG(world, blockPos, random, Direction.Axis.Z);
                }
            }
        }
    }

    private void setLOG(ISeedReader world, BlockPos blockPos, Random random, Direction.Axis axis){
        if (!world.isAirBlock(blockPos)){
            BlockPos up = blockPos.up();
            world.setBlockState(up, LOG.with(RotatedPillarBlock.AXIS, axis), 3);
            spawnWallPlant(world, up, random);
        }else if (world.isAirBlock(blockPos) && world.isAirBlock(blockPos.down()) && !world.isAirBlock(blockPos.down(2))){
            BlockPos down = blockPos.down(2);
            world.setBlockState(down, LOG.with(RotatedPillarBlock.AXIS, axis), 3);
            spawnWallPlant(world, down, random);
        } else {
            world.setBlockState(blockPos, LOG.with(RotatedPillarBlock.AXIS, axis), 3);
            spawnWallPlant(world, blockPos, random);
        }
    }

    /**
     * 随机生成蘑菇
     * @param world
     * @param blockPos
     * @param rand
     */
    private void spawnWallPlant(ISeedReader world, BlockPos blockPos, Random rand){
        //30%生成蘑菇
        if (rand.nextDouble() < 0.3d){
            for (Direction direction : Direction.values()){
                if (direction == Direction.DOWN || rand.nextDouble() > 0.6d) continue;
                BlockPos pos = blockPos.offset(direction);
                if (world.isAirBlock(pos)){
                    if (direction == Direction.UP){
                        world.setBlockState(pos, UP_PLANTS[rand.nextInt(4)], 3);
                    }else world.setBlockState(pos, PLANTS[rand.nextInt(4)].with(WallPlant.FACING, CropBlockItem.rotate(direction)), 3);
                }
            }
        }
    }
}
