package com.yuo.PaiMeng.Blocks;

import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CommonFarmland extends FarmlandBlock {

    public CommonFarmland() {
        super(Properties.from(Blocks.FARMLAND));
    }

    @Override
    public void onFallenUpon(World worldIn, BlockPos pos, Entity entityIn, float fallDistance) {
        //无法被踩坏
    }
}
