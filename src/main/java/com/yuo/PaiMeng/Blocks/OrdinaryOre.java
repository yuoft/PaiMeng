package com.yuo.PaiMeng.Blocks;

import net.minecraft.block.OreBlock;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.ToolType;

import java.util.Random;

//矿物
public class OrdinaryOre extends OreBlock {

    public OrdinaryOre(int harvestLevel, float hardness) {
        super(Properties.create(Material.ROCK).harvestLevel(harvestLevel).harvestTool(ToolType.PICKAXE)
                .hardnessAndResistance(hardness, hardness + 5).setRequiresTool());
    }

    @Override
    protected int getExperience(Random rand) {
        if (this == BlockRegistry.jinghuaGusuiOre.get()){
            return MathHelper.nextInt(rand, 3, 7);
        }else if (this == BlockRegistry.mojingOre.get()){
            return MathHelper.nextInt(rand, 3, 6);
        }else if (this == BlockRegistry.shipoOre.get()){
            return MathHelper.nextInt(rand, 2, 5);
        }else if (this == BlockRegistry.shuijingOre.get()){
            return MathHelper.nextInt(rand, 5, 10);
        }else if (this == BlockRegistry.yeboshiOre.get()){
            return MathHelper.nextInt(rand, 3, 6);
        }else if (this == BlockRegistry.zijingOre.get()){
            return MathHelper.nextInt(rand, 2, 6);
        }
        return 0;
    }
}
