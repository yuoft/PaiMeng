package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.Tiles.AbsLieYanHuaTile;
import com.yuo.PaiMeng.Tiles.LieYanHauPlantTile;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class LieYanHuaPlant extends BushBlock implements EntityBlock {

    public LieYanHuaPlant(Properties builder) {
        super(builder);
    }

    //粒子
    @Override
    public void animateTick(BlockState stateIn, Level level, BlockPos pos, RandomSource rand) {
        BlockPos blockpos = pos.above();
        LieYanHuaCrop.randomParticle(level, pos, rand, blockpos);
    }

    //实体碰撞
    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entityIn) {
        if (!level.isClientSide && entityIn instanceof LivingEntity living){
            if (living.getRemainingFireTicks() <= 0) living.setSecondsOnFire(3); //点燃
        }
    }

    //可存在什么方块上面？ 可以生成在什么方块上面（1.16.5 的 isValidGround）
    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(Blocks.CRIMSON_NYLIUM) || state.is(Blocks.STONE);
    }

    //1.16.5 的 getItem -> 1.20.1 的 getCloneItemStack
    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return new ItemStack(PMItems.lieyanhua_huarui.get());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new LieYanHauPlantTile(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, be) -> {
            if (be instanceof AbsLieYanHuaTile tile) tile.tick(lvl, pos, st);
        };
    }
}
