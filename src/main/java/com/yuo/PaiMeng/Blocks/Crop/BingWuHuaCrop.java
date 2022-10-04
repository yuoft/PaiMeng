package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Items.CropUseBlockEnum;
import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.Tiles.BingWuHuaCropTile;
import com.yuo.PaiMeng.Tiles.LieYanHauCropTile;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class BingWuHuaCrop extends ModXCropBlock{

    public BingWuHuaCrop(CropUseBlockEnum blockEnum) {
        super(blockEnum);
    }

    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        BlockPos blockpos = pos.up();
        if (getAge(stateIn) < 7) return;
        randomParticle(worldIn, pos, rand, blockpos);
    }

    public static void randomParticle(World worldIn, BlockPos pos, Random rand, BlockPos blockpos) {
        if (worldIn.getBlockState(blockpos).isAir() && !worldIn.getBlockState(blockpos).isOpaqueCube(worldIn, blockpos)) {
            if (rand.nextInt(10) == 0) {
                double d0 = (double)pos.getX() + rand.nextDouble();
                double d1 = (double)pos.getY() + 0.8D;
                double d2 = (double)pos.getZ() + rand.nextDouble();
                for (int i = 0; i < 5; i++)
                    worldIn.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!worldIn.isRemote && entityIn instanceof LivingEntity){
            LivingEntity living = (LivingEntity) entityIn;
            if (getAge(state) < 7) return;
            EffectInstance instance = living.getActivePotionEffect(Effects.SLOWNESS);
            if (instance == null)
                living.addPotionEffect(new EffectInstance(Effects.SLOWNESS, 20 * 3, 0));
        }
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return ItemRegistry.bingwuhuaHuaduoSeed.get();
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new BingWuHuaCropTile();
    }
}
