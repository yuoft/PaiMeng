package com.yuo.paimeng.blocks.crop;

import com.yuo.paimeng.items.CropUseBlockEnum;
import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.tiles.AbsBingWuHuaTile;
import com.yuo.paimeng.tiles.BingWuHuaCropTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class BingWuHuaCrop extends XCropBlock implements EntityBlock {

    public BingWuHuaCrop(CropUseBlockEnum blockEnum, Properties properties) {
        super(blockEnum, properties);
    }

    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        BlockPos blockpos = pos.above();
        if (getAge(stateIn) < 7) return;
        randomParticle(level, pos, rand, blockpos);
    }

    public static void randomParticle(Level level, BlockPos pos, RandomSource rand, BlockPos blockpos) {
        if (level.getBlockState(blockpos).isAir() && !level.getBlockState(blockpos).isSolidRender(level, blockpos)) {
            if (rand.nextInt(10) == 0) {
                double d0 = (double)pos.getX() + rand.nextDouble();
                double d1 = (double)pos.getY() + 0.8D;
                double d2 = (double)pos.getZ() + rand.nextDouble();
                for (int i = 0; i < 5; i++)
                    level.addParticle(ParticleTypes.SOUL_FIRE_FLAME, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            }
        }
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) {
        if (!level.isClientSide && entityIn instanceof LivingEntity living){
            if (getAge(state) < 7) return;
            MobEffectInstance instance = living.getEffect(MobEffects.MOVEMENT_SLOWDOWN);
            if (instance == null)
                living.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20 * 3, 0));
        }
    }

    @Override
    protected ItemLike getSeedsItem() {
        return PMItems.bingwuhuaHuaduoSeed.get();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BingWuHuaCropTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof AbsBingWuHuaTile tile) tile.tick(lvl, pos, st);
        };
    }
}
