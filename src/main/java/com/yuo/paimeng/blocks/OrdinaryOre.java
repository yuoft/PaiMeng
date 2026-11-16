package com.yuo.paimeng.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

//矿物
public class OrdinaryOre extends DropExperienceBlock {

    public OrdinaryOre(float hardness) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(hardness, hardness + 5).requiresCorrectToolForDrops());
    }

    @Override
    public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn) {
        if (this == PMBlocks.dianqiShuijingOre.get()){
            entityIn.hurt(entityIn.damageSources().generic(), 0.5f);
        }
        super.stepOn(worldIn, pos, state, entityIn);
    }

    @Override
    public int getExpDrop(BlockState state, LevelReader level, RandomSource random, BlockPos pos, int fortuneLevel, int silkTouchLevel) {
        return silkTouchLevel == 0 ? this.getExperience(random) : 0;
    }

    protected int getExperience(RandomSource rand) {
        if (this == PMBlocks.jinghuaGusuiOre.get()){
            return Mth.nextInt(rand, 3, 7);
        }else if (this == PMBlocks.mojingOre.get()){
            return Mth.nextInt(rand, 3, 6);
        }else if (this == PMBlocks.shipoOre.get()){
            return Mth.nextInt(rand, 2, 5);
        }else if (this == PMBlocks.shuijingOre.get()){
            return Mth.nextInt(rand, 5, 10);
        }else if (this == PMBlocks.yeboshiOre.get()){
            return Mth.nextInt(rand, 3, 6);
        }else if (this == PMBlocks.zijingOre.get()){
            return Mth.nextInt(rand, 2, 6);
        }
        return 0;
    }
}
