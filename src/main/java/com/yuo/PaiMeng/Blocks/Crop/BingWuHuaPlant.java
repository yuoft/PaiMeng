package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.Tiles.BingWuHuaPlantTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.BushBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.BlockTags;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BingWuHuaPlant extends BushBlock {

    public BingWuHuaPlant(Properties builder) {
        super(builder);
    }

    //粒子
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        BlockPos blockpos = pos.up();
        BingWuHuaCrop.randomParticle(worldIn, pos, rand, blockpos);
    }

    //实体碰撞
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof LivingEntity){
            LivingEntity living = (LivingEntity) entityIn; //缓慢
            EffectInstance instance = living.getActivePotionEffect(Effects.SLOWNESS);
            if (instance == null)
                living.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 20 * 3, 0));
        }
    }

    //可存在什么方块上面？
    @Override
    protected boolean isValidGround(BlockState state, IBlockReader worldIn, BlockPos pos) {
        return state.isIn(BlockTags.ICE) || state.matchesBlock(Blocks.SNOW_BLOCK);
    }

    @Override
    public ItemStack getItem(IBlockReader worldIn, BlockPos pos, BlockState state) {
        return new ItemStack(ItemRegistry.bingwuhua_huaduo.get());
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BingWuHuaPlantTile();
    }
}
