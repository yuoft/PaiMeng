package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Items.CropUseBlockEnum;
import com.yuo.PaiMeng.Items.PMItems;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class XCropBlock extends CropsBlock{
    private final CropUseBlockEnum useBlockEnum; //作物是否可存在此类方块上
    public XCropBlock(CropUseBlockEnum blockEnum, Properties properties) {
        super(properties);
        this.useBlockEnum = blockEnum;
    }

//    @Override
//    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
//        return state.get(AGE) >= 7 ? super.getLightValue(state, world, pos) : 0;
//    }

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

    public CropUseBlockEnum getUseBlockEnum() {
        return useBlockEnum;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        CropUseBlockEnum useBlockEnum = getUseBlockEnum();
        BlockState blockState = worldIn.getBlockState(pos.down());
        switch (useBlockEnum){
            case COMMON: return blockState.matchesBlock(PMBlocks.commonFarmland.get());
            case FERTILE: return blockState.matchesBlock(PMBlocks.fertileFarmland.get());
            case LOGS: {

                return blockState.isIn(BlockTags.LOGS);
            }
            default: return false;
        }
    }

    //部分不能使用骨粉催熟
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return getUseBlockEnum() == CropUseBlockEnum.COMMON;
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (!worldIn.isRemote){
            if (state.get(AGE) == 7){ //收获作物
                Block.spawnDrops(state, worldIn, pos, null, player, player.getHeldItem(handIn)); //生成掉落物
                worldIn.setBlockState(pos, state.with(AGE, 0), 2);
                return ActionResultType.SUCCESS;
            }
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }

    @Override
    protected IItemProvider getSeedsItem() {
        if (this == PMBlocks.bailuoboCrop.get()){
            return PMItems.bailuoboSeed.get();
        }else if (this == PMBlocks.baocaiCrop.get()){
            return PMItems.baocaiSeed.get();
        }else if (this == PMBlocks.boheCrop.get()){
            return PMItems.boheSeed.get();
        }else if (this == PMBlocks.ceciliaHuaCrop.get()){
            return PMItems.ceciliaHuaSeed.get();
        }else if (this == PMBlocks.dudulianCrop.get()){
            return PMItems.dudulianSeed.get();
        }else if (this == PMBlocks.fanqieCrop.get()){
            return PMItems.fanqieSeed.get();
        }else if (this == PMBlocks.fengchejuCrop.get()){
            return PMItems.fengchejuSeed.get();
        }else if (this == PMBlocks.gougouguoCrop.get()){
            return PMItems.gougouguoSeed.get();
        }else if (this == PMBlocks.haicaoCrop.get()){
            return PMItems.haicaoSeed.get();
        }else if (this == PMBlocks.hailingzhiCrop.get()){
            return PMItems.hailingzhiSeed.get();
        }else if (this == PMBlocks.huluoboCrop.get()){
            return PMItems.huluoboSeed.get();
        }else if (this == PMBlocks.jinyucaoCrop.get()){
            return PMItems.jinyucaoSeed.get();
        }else if (this == PMBlocks.jueyunJiaojiaoCrop.get()){
            return PMItems.jueyunJiaojiaoSeed.get();
        }else if (this == PMBlocks.lianpengCrop.get()){
            return PMItems.lianpengSeed.get();
        }else if (this == PMBlocks.liuliBaiheCrop.get()){
            return PMItems.liuliBaiheSeed.get();
        }else if (this == PMBlocks.liuliDaiCrop.get()){
            return PMItems.liuliDaiSeed.get();
        }else if (this == PMBlocks.luoluomeiCrop.get()){
            return PMItems.luoluomeiSeed.get();
        }else if (this == PMBlocks.maweiCrop.get()){
            return PMItems.maweiSeed.get();
        }else if (this == PMBlocks.mingcaoCrop.get()){
            return PMItems.mingcaoSeed.get();
        }else if (this == PMBlocks.moguCrop.get() || this == PMBlocks.moguWallCrop.get()){
            return PMItems.moguSeed.get();
        }else if (this == PMBlocks.mufengMoguCrop.get() || this == PMBlocks.mufengMoguWallCrop.get()){
            return PMItems.mufengMoguSeed.get();
        }else if (this == PMBlocks.nichanghuaCrop.get()){
            return PMItems.nichanghuaSeed.get();
        }else if (this == PMBlocks.pugongyingCrop.get()){
            return PMItems.pugongyingSeed.get();
        }else if (this == PMBlocks.qingxinCrop.get()){
            return PMItems.qingxinSeed.get();
        }else if (this == PMBlocks.shumeiCrop.get()){
            return PMItems.shumeiSeed.get();
        }else if (this == PMBlocks.songrongCrop.get() || this == PMBlocks.songrongWallCrop.get()){
            return PMItems.songrongSeed.get();
        }else if (this == PMBlocks.tiantianhuaCrop.get()){
            return PMItems.tiantianhuaSeed.get();
        }else if (this == PMBlocks.tianyunCaoshiCrop.get()){
            return PMItems.tianyunCaoshiSeed.get();
        }else if (this == PMBlocks.xiaodengcaoCrop.get()){
            return PMItems.xiaodengcaoSeed.get();
        }else if (this == PMBlocks.xiaomaiCrop.get()){
            return PMItems.xiaomaiSeed.get();
        }else if (this == PMBlocks.xuekuiCrop.get()){
            return PMItems.xuekuiSeed.get();
        }else if (this == PMBlocks.yangcongCrop.get()){
            return PMItems.yangcongSeed.get();
        }else if (this == PMBlocks.youdengxunCrop.get() || this == PMBlocks.youdengxunWallCrop.get()){
            return PMItems.youdengxunSeed.get();
        }else if (this == PMBlocks.shuwangShengtiguCrop.get() || this == PMBlocks.shuwangShengtiguWallCrop.get()){
            return PMItems.shuwangShengtiguSeed.get();
        }else if (this == PMBlocks.zhusunCrop.get()){
            return PMItems.zhusunSeed.get();
        }else if (this == PMBlocks.dunduntaoCrop.get()){
            return PMItems.dunduntaoSeed.get();
        }else if (this == PMBlocks.padishalanCrop.get()){
            return PMItems.padishalanSeed.get();
        }else if (this == PMBlocks.piboyeCrop.get()){
            return PMItems.piboyeSeed.get();
        }else if (this == PMBlocks.xiangxinguoCrop.get()){
            return PMItems.xiangxinguoSeed.get();
        }else if (this == PMBlocks.xumiQiangweiCrop.get()){
            return PMItems.xumiQiangweiSeed.get();
        }else if (this == PMBlocks.mojiecaoCrop.get()){
            return PMItems.mojiecaoSeed.get();
        }
        return super.getSeedsItem();
    }
}
