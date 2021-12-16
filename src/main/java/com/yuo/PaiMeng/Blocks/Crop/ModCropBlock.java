package com.yuo.PaiMeng.Blocks.Crop;

import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Random;

public class ModCropBlock extends CropsBlock{
    private static Properties CROP = Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().
            zeroHardnessAndResistance().sound(SoundType.CROP); //作物

    public ModCropBlock() {
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
