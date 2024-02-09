package com.yuo.PaiMeng.Blocks;

import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Random;

//矿物
public class OrdinaryOre extends OreBlock {

    public OrdinaryOre(int harvestLevel, float hardness) {
        super(Properties.create(Material.ROCK).harvestLevel(harvestLevel).harvestTool(ToolType.PICKAXE)
                .hardnessAndResistance(hardness, hardness + 5).setRequiresTool());
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        if (this == PMBlocks.dianqiShuijingOre.get()){
            entityIn.attackEntityFrom(new DamageSource("Electrical"), 0.5f);
        }
    }

    @Override
    protected int getExperience(Random rand) {
        if (this == PMBlocks.jinghuaGusuiOre.get()){
            return MathHelper.nextInt(rand, 3, 7);
        }else if (this == PMBlocks.mojingOre.get()){
            return MathHelper.nextInt(rand, 3, 6);
        }else if (this == PMBlocks.shipoOre.get()){
            return MathHelper.nextInt(rand, 2, 5);
        }else if (this == PMBlocks.shuijingOre.get()){
            return MathHelper.nextInt(rand, 5, 10);
        }else if (this == PMBlocks.yeboshiOre.get()){
            return MathHelper.nextInt(rand, 3, 6);
        }else if (this == PMBlocks.zijingOre.get()){
            return MathHelper.nextInt(rand, 2, 6);
        }
        return 0;
    }
}
