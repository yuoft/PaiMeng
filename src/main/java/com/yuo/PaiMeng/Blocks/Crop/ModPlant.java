package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Items.PMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ModPlant extends BushBlock {

    public ModPlant(Properties builder) {
        super(builder);
    }

    //可存在什么方块上面？（1.16.5 的 isValidGround）
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        if (this == PMBlocks.liuliDaiPlant.get() || this == PMBlocks.qingxinPlant.get() || this == PMBlocks.ceciliaHuaPlant.get()){
           return state.is(Blocks.STONE) || state.is(Blocks.SNOW_BLOCK);
        }
        if (this == PMBlocks.dudulianPlant.get() || this == PMBlocks.lianpengPlant.get() || this == PMBlocks.maweiPlant.get()
            || this == PMBlocks.jinyucaoPlant.get())   {
            return super.mayPlaceOn(state, level, pos) || state.is(Blocks.SAND);
        }
        if (this == PMBlocks.mingcaoPlant.get() || this == PMBlocks.tianyunCaoshiPlant.get()){
            return state.is(Blocks.END_STONE);
        }
        if (this == PMBlocks.mufengMoguPlant.get()){
            return state.is(Blocks.MYCELIUM);
        }
        if (this == PMBlocks.youdengxunPlant.get()){
            return state.is(Blocks.WARPED_NYLIUM);
        }
        if (this == PMBlocks.jueyunJiaojiaoPlant.get()){
            return state.is(Blocks.SAND);
        }
        return super.mayPlaceOn(state, level, pos);
    }

    //1.16.5 的 getItem -> 1.20.1 的 getCloneItemStack
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
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
        return super.getCloneItemStack(level, pos, state);
    }
}
