package com.yuo.paimeng.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;

//矿物
public class PMOreBlock extends DropExperienceBlock {

    public PMOreBlock(float hardness, UniformInt xpRange) {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.STONE).strength(hardness, hardness + 5).requiresCorrectToolForDrops(), xpRange);
    }

    @Override
    public void stepOn(Level worldIn, BlockPos pos, BlockState state, Entity entityIn) {
        if (this == PMBlocks.dianqiShuijingOre.get()){
            entityIn.hurt(entityIn.damageSources().generic(), 0.5f);
        }
        super.stepOn(worldIn, pos, state, entityIn);
    }
}
