package com.yuo.PaiMeng.Blocks.Crop;

import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class ModXCropBlock extends CropsBlock{
    private static Block.Properties CROP = AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().
            zeroHardnessAndResistance().sound(SoundType.CROP); //作物

    public ModXCropBlock() {
        super(CROP);
    }

    //生长时间更长
    @Override
    public void grow(World worldIn, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + MathHelper.nextInt(RANDOM, 1, 4);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        worldIn.setBlockState(pos, this.withAge(i), 2);
    }

    //部分不能使用骨粉催熟
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return true;
    }
}
