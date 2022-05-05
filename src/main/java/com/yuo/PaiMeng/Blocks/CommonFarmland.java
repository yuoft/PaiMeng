package com.yuo.PaiMeng.Blocks;

import net.minecraft.block.Blocks;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

public class CommonFarmland extends FarmlandBlock {

    public CommonFarmland() {
        super(Properties.from(Blocks.FARMLAND));
    }

    @Override
    public void onFallenUpon(World world, BlockPos pos, Entity entity, float i) {
        super.onFallenUpon(world, pos, entity, i);
    }
}
