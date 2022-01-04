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
           return state.isIn(Blocks.STONE) || state.isIn(Blocks.SNOW_BLOCK);
        }
        if (this == BlockRegistry.dudulianPlant.get() || this == BlockRegistry.lianpengPlant.get() || this == BlockRegistry.maweiPlant.get()
            || this == BlockRegistry.jinyucaoPlant.get())   {
            return super.isValidGround(state, worldIn, pos) || state.isIn(Blocks.SAND);
        }
        if (this == BlockRegistry.mingcaoPlant.get() || this == BlockRegistry.tianyunCaoshiPlant.get()){
            return state.isIn(Blocks.END_STONE);
        }
        if (this == BlockRegistry.mufengMoguPlant.get()){
            return state.isIn(Blocks.MYCELIUM);
        }
        if (this == BlockRegistry.bingwuhuaHuaduoPlant.get()){
            return state.isIn(BlockTags.ICE) || state.isIn(Blocks.SNOW_BLOCK);
        }
        if (this == BlockRegistry.lieyanhuaHuaruiPlant.get()){
            return state.isIn(Blocks.CRIMSON_NYLIUM) || state.isIn(Blocks.STONE);
        }
        if (this == BlockRegistry.youdengxunPlant.get()){
            return state.isIn(Blocks.WARPED_NYLIUM);
        }
        if (this == BlockRegistry.jueyunJiaojiaoPlant.get()){
            return state.isIn(Blocks.SAND);
        }
        return super.isValidGround(state, worldIn, pos);
    }
}
