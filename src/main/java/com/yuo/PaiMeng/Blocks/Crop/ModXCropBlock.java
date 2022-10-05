package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.Blocks.CommonFarmland;
import com.yuo.PaiMeng.Items.CropUseBlockEnum;
import com.yuo.PaiMeng.Items.ItemRegistry;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;
import java.util.function.ToIntFunction;

public class ModXCropBlock extends CropsBlock{
    private static final Block.Properties CROP = AbstractBlock.Properties.create(Material.PLANTS).setLightLevel(getLightValueLit()).doesNotBlockMovement().tickRandomly().
            zeroHardnessAndResistance().sound(SoundType.CROP); //作物
    private final CropUseBlockEnum useBlockEnum;

    public ModXCropBlock(CropUseBlockEnum blockEnum) {
        super(CROP);
        this.useBlockEnum = blockEnum;
    }

    public static ToIntFunction<BlockState> getLightValueLit() {
        return (state) -> {
            Block block = state.getBlock();
            if (block == BlockRegistry.lieyanhuaHuaruiCrop.get()) return 15;
            if (block == BlockRegistry.xiaodengcaoCrop.get()) return 8;
            if (block == BlockRegistry.youdengxunCrop.get()) return 5;
            return 0;
        };
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

    public CropUseBlockEnum getUseBlockEnum() {
        return useBlockEnum;
    }

    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        CropUseBlockEnum useBlockEnum = getUseBlockEnum();
        BlockState blockState = worldIn.getBlockState(pos.down());
        if (blockState.getBlock() instanceof CommonFarmland){
            switch (useBlockEnum){
                case COMMON: return blockState.matchesBlock(BlockRegistry.commonFarmland.get());
                case FERTILE: return blockState.matchesBlock(BlockRegistry.fertileFarmland.get());
                default: return false;
            }
        }
        return false;
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
        if (this == BlockRegistry.bailuoboCrop.get()){
            return ItemRegistry.bailuoboSeed.get();
        }else if (this == BlockRegistry.baocaiCrop.get()){
            return ItemRegistry.baocaiSeed.get();
        }else if (this == BlockRegistry.boheCrop.get()){
            return ItemRegistry.boheSeed.get();
        }else if (this == BlockRegistry.ceciliaHuaCrop.get()){
            return ItemRegistry.ceciliaHuaSeed.get();
        }else if (this == BlockRegistry.dudulianCrop.get()){
            return ItemRegistry.dudulianSeed.get();
        }else if (this == BlockRegistry.fanqieCrop.get()){
            return ItemRegistry.fanqieSeed.get();
        }else if (this == BlockRegistry.fengchejuCrop.get()){
            return ItemRegistry.fengchejuSeed.get();
        }else if (this == BlockRegistry.gougouguoCrop.get()){
            return ItemRegistry.gougouguoSeed.get();
        }else if (this == BlockRegistry.haicaoCrop.get()){
            return ItemRegistry.haicaoSeed.get();
        }else if (this == BlockRegistry.hailingzhiCrop.get()){
            return ItemRegistry.hailingzhiSeed.get();
        }else if (this == BlockRegistry.huluoboCrop.get()){
            return ItemRegistry.huluoboSeed.get();
        }else if (this == BlockRegistry.jinyucaoCrop.get()){
            return ItemRegistry.jinyucaoSeed.get();
        }else if (this == BlockRegistry.jueyunJiaojiaoCrop.get()){
            return ItemRegistry.jueyunJiaojiaoSeed.get();
        }else if (this == BlockRegistry.lianpengCrop.get()){
            return ItemRegistry.lianpengSeed.get();
        }else if (this == BlockRegistry.liuliBaiheCrop.get()){
            return ItemRegistry.liuliBaiheSeed.get();
        }else if (this == BlockRegistry.liuliDaiCrop.get()){
            return ItemRegistry.liuliDaiSeed.get();
        }else if (this == BlockRegistry.luoluomeiCrop.get()){
            return ItemRegistry.luoluomeiSeed.get();
        }else if (this == BlockRegistry.maweiCrop.get()){
            return ItemRegistry.maweiSeed.get();
        }else if (this == BlockRegistry.mingcaoCrop.get()){
            return ItemRegistry.mingcaoSeed.get();
        }else if (this == BlockRegistry.moguCrop.get()){
            return ItemRegistry.moguSeed.get();
        }else if (this == BlockRegistry.mufengMoguCrop.get()){
            return ItemRegistry.mufengMoguSeed.get();
        }else if (this == BlockRegistry.nichanghuaCrop.get()){
            return ItemRegistry.nichanghuaSeed.get();
        }else if (this == BlockRegistry.pugongyingCrop.get()){
            return ItemRegistry.pugongyingSeed.get();
        }else if (this == BlockRegistry.qingxinCrop.get()){
            return ItemRegistry.qingxinSeed.get();
        }else if (this == BlockRegistry.shumeiCrop.get()){
            return ItemRegistry.shumeiSeed.get();
        }else if (this == BlockRegistry.songrongCrop.get()){
            return ItemRegistry.songrongSeed.get();
        }else if (this == BlockRegistry.tiantianhuaCrop.get()){
            return ItemRegistry.tiantianhuaSeed.get();
        }else if (this == BlockRegistry.tianyunCaoshiCrop.get()){
            return ItemRegistry.tianyunCaoshiSeed.get();
        }else if (this == BlockRegistry.xiaodengcaoCrop.get()){
            return ItemRegistry.xiaodengcaoSeed.get();
        }else if (this == BlockRegistry.xiaomaiCrop.get()){
            return ItemRegistry.xiaomaiSeed.get();
        }else if (this == BlockRegistry.xuekuiCrop.get()){
            return ItemRegistry.xuekuiSeed.get();
        }else if (this == BlockRegistry.yangcongCrop.get()){
            return ItemRegistry.yangcongSeed.get();
        }else if (this == BlockRegistry.youdengxunCrop.get()){
            return ItemRegistry.youdengxunSeed.get();
        }else if (this == BlockRegistry.zhusunCrop.get()){
            return ItemRegistry.zhusunSeed.get();
        }
        return super.getSeedsItem();
    }
}
