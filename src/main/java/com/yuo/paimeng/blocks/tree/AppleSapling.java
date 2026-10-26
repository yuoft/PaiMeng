package com.yuo.paimeng.blocks.tree;

import com.yuo.paimeng.blocks.PMBlocks;
import com.yuo.paimeng.worldgen.TreeSpawner;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class AppleSapling extends SaplingBlock {

    private TreeSpawner tree;

    public AppleSapling(TreeSpawner treeIn, BlockBehaviour.Properties properties) {
        //1.20.1：SaplingBlock 需要 AbstractTreeGrower，这里传 null，实际生长逻辑由下面的 advanceTree/placeTree 接管
        super((AbstractTreeGrower) null, properties);
        this.tree = treeIn;
    }

    //1.20.1 的 IGrowable → BonemealableBlock：canUseBonemeal → isBonemealSuccess
    @Override
    public boolean isBonemealSuccess(Level worldIn, RandomSource rand, BlockPos pos, BlockState state) {
        return this.canUseBonemeal(worldIn, rand, pos, state);
    }

    //保留旧方法名
    public boolean canUseBonemeal(Level worldIn, RandomSource rand, BlockPos pos, BlockState state) {
        return state.getBlock() != PMBlocks.sunAppleSapling.get();
    }

    //1.20.1：isValidGround → mayPlaceOn
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter worldIn, BlockPos pos) {
        return this.isValidGround(state, worldIn, pos);
    }

    //保留旧方法名
    protected boolean isValidGround(BlockState state, BlockGetter worldIn, BlockPos pos) {
        if (this == PMBlocks.zaoyeSapling.get())
            return state.is(Blocks.SAND);
        return super.mayPlaceOn(state, worldIn, pos);
    }

    //1.20.1 的 SaplingBlock 用 advanceTree 取代了 1.16 的 placeTree
    @Override
    public void advanceTree(ServerLevel world, BlockPos pos, BlockState state, RandomSource rand) {
        this.placeTree(world, pos, state, rand);
    }

    public void placeTree(ServerLevel world, BlockPos pos, BlockState state, RandomSource rand) {
        if (state.getValue(STAGE) == 0) {
            world.setBlock(pos, state.cycle(STAGE), 4);
        } else {
            // TODO(port): TreeSpawner.spawn 目前还是 1.16 的 java.util.Random 参数（WorldGen 侧未移植），
            //  等那边把签名改成 RandomSource 后，这里直接传 rand 即可，去掉临时转换。
            tree.spawn(world, world.getChunkSource().getGenerator(), pos, state, new java.util.Random(rand.nextLong()));
        }
    }
}
