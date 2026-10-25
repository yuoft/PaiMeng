package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Items.CropUseBlockEnum;
import com.yuo.PaiMeng.Items.PMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class XCropBlock extends CropBlock {
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
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + Mth.nextInt(level.random, 1, 4);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        level.setBlock(pos, this.getStateForAge(i), 2);
    }

    public CropUseBlockEnum getUseBlockEnum() {
        return useBlockEnum;
    }

    //可存在什么方块上面？（1.16.5 的 isValidPosition）
    //原版 CropBlock#canSurvive 附带亮度要求，这里保持 1.16.5 行为：只看下方方块
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos.below());
        switch (useBlockEnum){
            case COMMON: return blockState.is(PMBlocks.commonFarmland.get());
            case FERTILE: return blockState.is(PMBlocks.fertileFarmland.get());
            case LOGS: {

                return blockState.is(BlockTags.LOGS);
            }
            default: return false;
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return this.mayPlaceOn(level.getBlockState(pos.below()), level, pos.below());
    }

    //部分不能使用骨粉催熟
    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return getUseBlockEnum() == CropUseBlockEnum.COMMON && !this.isMaxAge(state);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!level.isClientSide){
            if (state.getValue(AGE) == 7){ //收获作物
                Block.dropResources(state, level, pos, null, player, player.getItemInHand(handIn)); //生成掉落物
                level.setBlock(pos, state.setValue(AGE, 0), 2);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, level, pos, player, handIn, hit);
    }

    //作物种子（1.16.5 的 getSeedsItem）
    protected ItemLike getSeedsItem() {
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
        return super.getBaseSeedId();
    }

    //1.20.1：getBaseSeedId 已不再由外部调用，掉落种子改由 getCloneItemStack 提供
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(this.getSeedsItem());
    }
}
