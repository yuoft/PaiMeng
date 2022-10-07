package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.Items.ItemRegistry;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import java.util.function.ToIntFunction;

public class ModPlant extends BushBlock {

    public ModPlant(Properties builder) {
        super(builder);
    }

    //可存在什么方块上面？
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
        if (this == BlockRegistry.youdengxunPlant.get()){
            return state.matchesBlock(Blocks.WARPED_NYLIUM);
        }
        if (this == BlockRegistry.jueyunJiaojiaoPlant.get()){
            return state.matchesBlock(Blocks.SAND);
        }
        return super.isValidGround(state, worldIn, pos);
    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        Item item = Items.AIR;
        if (this == BlockRegistry.bohePlant.get()){
            item = ItemRegistry.bohe.get();
        }else if (this == BlockRegistry.ceciliaHuaPlant.get()){
            item = ItemRegistry.ceciliaHua.get();
        }else if (this == BlockRegistry.dudulianPlant.get()){
            item = ItemRegistry.dudulian.get();
        }else if (this == BlockRegistry.fengchejuPlant.get()){
            item = ItemRegistry.fengcheju.get();
        }else if (this == BlockRegistry.gougouguoPlant.get()){
            item = ItemRegistry.gougouguo.get();
        }else if (this == BlockRegistry.haicaoPlant.get()){
            item = ItemRegistry.haicao.get();
        }else if (this == BlockRegistry.hailingzhiPlant.get()){
            item = ItemRegistry.hailingzhi.get();
        }else if (this == BlockRegistry.jinyucaoPlant.get()){
            item = ItemRegistry.jinyucao.get();
        }else if (this == BlockRegistry.jueyunJiaojiaoPlant.get()){
            item = ItemRegistry.jueyunJiaojiao.get();
        }else if (this == BlockRegistry.lianpengPlant.get()){
            item = ItemRegistry.lianpeng.get();
        }else if (this == BlockRegistry.liuliDaiPlant.get()){
            item = ItemRegistry.liuliDai.get();
        }else if (this == BlockRegistry.liuliBaihePlant.get()){
            item = ItemRegistry.liuliBaihe.get();
        }else if (this == BlockRegistry.luoluomeiPlant.get()){
            item = ItemRegistry.luoluomei.get();
        }else if (this == BlockRegistry.maweiPlant.get()){
            item = ItemRegistry.mawei.get();
        }else if (this == BlockRegistry.mingcaoPlant.get()){
            item = ItemRegistry.mingcao.get();
        }else if (this == BlockRegistry.moguPlant.get() || this == BlockRegistry.moguWallPlant.get()){
            item = ItemRegistry.mogu.get();
        }else if (this == BlockRegistry.mufengMoguPlant.get() || this == BlockRegistry.mufengMoguWallPlant.get()){
            item = ItemRegistry.mufeng_mogu.get();
        }else if (this == BlockRegistry.nichanghuaPlant.get()){
            item = ItemRegistry.nichanghua.get();
        }else if (this == BlockRegistry.pugongyingPlant.get()){
            item = ItemRegistry.pugongying_zhongzi.get();
        }else if (this == BlockRegistry.qingxinPlant.get()){
            item = ItemRegistry.qingxin.get();
        }else if (this == BlockRegistry.shumeiPlant.get()){
            item = ItemRegistry.shumei.get();
        }else if (this == BlockRegistry.songrongPlant.get() || this == BlockRegistry.songrongWallPlant.get()){
            item = ItemRegistry.songrong.get();
        }else if (this == BlockRegistry.tiantianhuaPlant.get()){
            item = ItemRegistry.tiantianhua.get();
        }else if (this == BlockRegistry.tianyunCaoshiPlant.get()){
            item = ItemRegistry.tianyun_caoshi.get();
        }else if (this == BlockRegistry.xiaodengcaoPlant.get()){
            item = ItemRegistry.xiaodengcao.get();
        }else if (this == BlockRegistry.xuekuiPlant.get()){
            item = ItemRegistry.xuekui.get();
        }else if (this == BlockRegistry.youdengxunPlant.get() || this == BlockRegistry.youdengxunWallPlant.get()){
            item = ItemRegistry.youdengxun.get();
        }else if (this == BlockRegistry.zhusunPlant.get()){
            item = ItemRegistry.zhusun.get();
        }
        if (item != Items.AIR){
            return new ItemStack(item);
        }
        return super.getItem(worldIn, pos, state);
    }
}
