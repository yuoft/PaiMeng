package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class PlantBlock extends BushBlock {

    public PlantBlock(Properties builder) {
        super(builder);
    }

    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        if (this == BlockRegistry.liuliDaiPlant.get() || this == BlockRegistry.qingxinPlant.get() || this == BlockRegistry.ceciliaHuaPlant.get()){
           return state.matchesBlock(Blocks.STONE) || state.matchesBlock(Blocks.SNOW_BLOCK);
        }
        if (this == BlockRegistry.dudulianPlant.get() || this == BlockRegistry.lianpengPlant.get() || this == BlockRegistry.maweiPlant.get()
            || this == BlockRegistry.jinyucaoPlant.get())   {
            return super.isValidGround(state, worldIn, pos) || state.matchesBlock(Blocks.SAND);
        }
        if (this == BlockRegistry.mingcaoPlant.get() || this == BlockRegistry.tianyunCaoshiPlant.get()){
            return state.matchesBlock(Blocks.END_STONE);
        }
        if (this == BlockRegistry.mufengMoguPlant.get()){
            return state.matchesBlock(Blocks.MYCELIUM);
        }
        if (this == BlockRegistry.bingwuhuaHuaduoPlant.get()){
            return state.isIn(BlockTags.ICE) || state.matchesBlock(Blocks.SNOW_BLOCK);
        }
        if (this == BlockRegistry.lieyanhuaHuaruiPlant.get()){
            return state.matchesBlock(Blocks.CRIMSON_NYLIUM) || state.matchesBlock(Blocks.STONE);
        }
        if (this == BlockRegistry.youdengxunPlant.get()){
            return state.matchesBlock(Blocks.WARPED_NYLIUM);
        }
        if (this == BlockRegistry.jueyunJiaojiaoPlant.get()){
            return state.matchesBlock(Blocks.SAND);
        }
        return super.isValidGround(state, worldIn, pos);
    }
}
