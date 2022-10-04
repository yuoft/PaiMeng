package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Tiles.AbsLieYanHuaTile;
import com.yuo.PaiMeng.Tiles.LieYanHauPlantTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class LieYanHuaPlant extends BushBlock {

    public LieYanHuaPlant(Properties builder) {
        super(builder);
    }

    //粒子
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        BlockPos blockpos = pos.up();
        LieYanHuaCrop.randomParticle(worldIn, pos, rand, blockpos);
    }

    //实体碰撞
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof LivingEntity){
            LivingEntity living = (LivingEntity) entityIn;
            if (living.getFireTimer() <= 0) living.setFire(3); //点燃
        }
    }

    //可存在什么方块上面？ 可以生成在什么方块上面
    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.matchesBlock(Blocks.CRIMSON_NYLIUM) || state.matchesBlock(Blocks.STONE);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new LieYanHauPlantTile();
    }
}
