package com.yuo.PaiMeng.Blocks.Tree;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.WorldGen.TreeSpawner;
import net.minecraft.block.BlockState;
import net.minecraft.block.IGrowable;
import net.minecraft.block.SaplingBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

public class AppleSapling extends SaplingBlock implements IGrowable {

    private TreeSpawner tree;

    public AppleSapling(TreeSpawner treeIn, Properties properties) {
        super(null, properties);
        this.tree = treeIn;
    }
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return state.getBlock() != BlockRegistry.sunAppleSapling.get();
    }

    public void placeTree(ServerWorld world, BlockPos pos, BlockState state, Random rand) {
        if (state.get(STAGE) == 0) {
            world.setBlockState(pos, state.cycleValue(STAGE), 4);
        } else {
            tree.spawn(world, world.getChunkProvider().getChunkGenerator(), pos, state, rand);
        }
    }
}
