package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Items.PMItems;
import net.minecraft.block.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

public class ModPlant extends BushBlock {

    public ModPlant(Properties builder) {
        super(builder);
    }

    //可存在什么方块上面？
    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        if (this == PMBlocks.liuliDaiPlant.get() || this == PMBlocks.qingxinPlant.get() || this == PMBlocks.ceciliaHuaPlant.get()){
           return state.matchesBlock(Blocks.STONE) || state.matchesBlock(Blocks.SNOW_BLOCK);
        }
        if (this == PMBlocks.dudulianPlant.get() || this == PMBlocks.lianpengPlant.get() || this == PMBlocks.maweiPlant.get()
            || this == PMBlocks.jinyucaoPlant.get())   {
            return super.isValidGround(state, worldIn, pos) || state.matchesBlock(Blocks.SAND);
        }
        if (this == PMBlocks.mingcaoPlant.get() || this == PMBlocks.tianyunCaoshiPlant.get()){
            return state.matchesBlock(Blocks.END_STONE);
        }
        if (this == PMBlocks.mufengMoguPlant.get()){
            return state.matchesBlock(Blocks.MYCELIUM);
        }
        if (this == PMBlocks.youdengxunPlant.get()){
            return state.matchesBlock(Blocks.WARPED_NYLIUM);
        }
        if (this == PMBlocks.jueyunJiaojiaoPlant.get()){
            return state.matchesBlock(Blocks.SAND);
        }
        return super.isValidGround(state, worldIn, pos);
    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        Item item = Items.AIR;
        if (this == PMBlocks.bohePlant.get()){
            item = PMItems.bohe.get();
        }else if (this == PMBlocks.ceciliaHuaPlant.get()){
            item = PMItems.ceciliaHua.get();
        }else if (this == PMBlocks.dudulianPlant.get()){
            item = PMItems.dudulian.get();
        }else if (this == PMBlocks.fengchejuPlant.get()){
            item = PMItems.fengcheju.get();
        }else if (this == PMBlocks.gougouguoPlant.get()){
            item = PMItems.gougouguo.get();
        }else if (this == PMBlocks.haicaoPlant.get()){
            item = PMItems.haicao.get();
        }else if (this == PMBlocks.hailingzhiPlant.get()){
            item = PMItems.hailingzhi.get();
        }else if (this == PMBlocks.jinyucaoPlant.get()){
            item = PMItems.jinyucao.get();
        }else if (this == PMBlocks.jueyunJiaojiaoPlant.get()){
            item = PMItems.jueyunJiaojiao.get();
        }else if (this == PMBlocks.lianpengPlant.get()){
            item = PMItems.lianpeng.get();
        }else if (this == PMBlocks.liuliDaiPlant.get()){
            item = PMItems.liuliDai.get();
        }else if (this == PMBlocks.liuliBaihePlant.get()){
            item = PMItems.liuliBaihe.get();
        }else if (this == PMBlocks.luoluomeiPlant.get()){
            item = PMItems.luoluomei.get();
        }else if (this == PMBlocks.maweiPlant.get()){
            item = PMItems.mawei.get();
        }else if (this == PMBlocks.mingcaoPlant.get()){
            item = PMItems.mingcao.get();
        }else if (this == PMBlocks.moguPlant.get() || this == PMBlocks.moguWallPlant.get()){
            item = PMItems.mogu.get();
        }else if (this == PMBlocks.mufengMoguPlant.get() || this == PMBlocks.mufengMoguWallPlant.get()){
            item = PMItems.mufeng_mogu.get();
        }else if (this == PMBlocks.nichanghuaPlant.get()){
            item = PMItems.nichanghua.get();
        }else if (this == PMBlocks.pugongyingPlant.get()){
            item = PMItems.pugongying_zhongzi.get();
        }else if (this == PMBlocks.qingxinPlant.get()){
            item = PMItems.qingxin.get();
        }else if (this == PMBlocks.shumeiPlant.get()){
            item = PMItems.shumei.get();
        }else if (this == PMBlocks.songrongPlant.get() || this == PMBlocks.songrongWallPlant.get()){
            item = PMItems.songrong.get();
        }else if (this == PMBlocks.tiantianhuaPlant.get()){
            item = PMItems.tiantianhua.get();
        }else if (this == PMBlocks.tianyunCaoshiPlant.get()){
            item = PMItems.tianyun_caoshi.get();
        }else if (this == PMBlocks.xiaodengcaoPlant.get()){
            item = PMItems.xiaodengcao.get();
        }else if (this == PMBlocks.xuekuiPlant.get()){
            item = PMItems.xuekui.get();
        }else if (this == PMBlocks.youdengxunPlant.get() || this == PMBlocks.youdengxunWallPlant.get()){
            item = PMItems.youdengxun.get();
        }else if (this == PMBlocks.shuwangShengtiguPlant.get() || this == PMBlocks.shuwangShengtiguWallPlant.get()){
            item = PMItems.shuwangShengtigu.get();
        }else if (this == PMBlocks.zhusunPlant.get()){
            item = PMItems.zhusun.get();
        }else if (this == PMBlocks.dunduntaoPlant.get()){
            item = PMItems.dunduntao.get();
        }else if (this == PMBlocks.padishalanPlant.get()){
            item = PMItems.padishalan.get();
        }else if (this == PMBlocks.piboyePlant.get()){
            item = PMItems.piboye.get();
        }else if (this == PMBlocks.xiangxinguoPlant.get()){
            item = PMItems.xiangxinguo.get();
        }else if (this == PMBlocks.xumiQiangweiPlant.get()){
            item = PMItems.xumiQiangwei.get();
        }else if (this == PMBlocks.mojiecaoPlant.get()){
            item = PMItems.mojiecao.get();
        }
        if (item != Items.AIR){
            return new ItemStack(item);
        }
        return super.getItem(worldIn, pos, state);
    }
}
