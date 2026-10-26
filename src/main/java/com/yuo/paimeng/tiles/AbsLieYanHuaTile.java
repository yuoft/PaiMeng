package com.yuo.paimeng.tiles;

import com.yuo.paimeng.blocks.crop.LieYanHuaCrop;
import com.yuo.paimeng.PMConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.ForgeEventFactory;

public class AbsLieYanHuaTile extends BlockEntity {
    private int tick = 0;

    public AbsLieYanHuaTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    /**
     * 1.20.1 方块侧的 getTicker 调用入口
     */
    public void tick(Level level, BlockPos pos, BlockState state) {
        tickInternal(level, pos);
    }

    /**
     * 兼容旧调用
     */
    public void tick() {
        if (this.level == null) return;
        tickInternal(this.level, this.getBlockPos());
    }

    private void tickInternal(Level world, BlockPos pos) {
        if (world.isClientSide) return;
        tick++;

        if (tick == PMConfig.SERVER.lieYanHuaTime.get()) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof LieYanHuaCrop){
                Integer age = state.getValue(CropBlock.AGE);
                if (age >= 7) randomFire(world, pos, world.random);
            } else randomFire(world, pos, world.random);
            tick = 0;
        }

        if (tick % PMConfig.SERVER.lieYanHuaAndBingWUHuaTime.get() == 0){
            meltIce(world, pos);
        }
    }

    /**
     * 融化周围冰块
     * @param world 世界
     * @param pos 坐标
     */
    private void meltIce(Level world, BlockPos pos){
        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 0, 1))) {
            BlockState state = world.getBlockState(blockPos);
            if (state.getBlock() == Blocks.ICE){
               turnIntoWater(state, world, blockPos.immutable());
            }
        }
    }

    //冰化为水
    protected void turnIntoWater(BlockState state, Level world, BlockPos pos) {
        if (world.dimensionType().ultraWarm()) {
            world.removeBlock(pos, false);
        } else {
            world.setBlockAndUpdate(pos, Blocks.WATER.defaultBlockState());
            world.neighborChanged(pos, Blocks.WATER, pos);
        }
    }

    /**
     * 随机点燃周围方块和生物
     *
     * @param world  世界
     * @param pos    作物坐标
     * @param random 随机数生成器
     */
    public void randomFire(Level world, BlockPos pos, RandomSource random) {
        int i = random.nextInt(3);
        if (i > 0) {
            BlockPos blockpos = pos;

            for(int j = 0; j < i; ++j) {
                blockpos = blockpos.offset(random.nextInt(5) - 2, 1, random.nextInt(5) - 2);
                if (!world.isLoaded(blockpos)) {
                    return;
                }

                BlockState blockstate = world.getBlockState(blockpos);
                if (blockstate.isAir()) {
                    if (this.isSurroundingBlockFlammable(world, blockpos)) {
                        world.setBlockAndUpdate(blockpos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, blockpos, pos, Blocks.FIRE.defaultBlockState()));
                        return;
                    }
                } else if (blockstate.blocksMotion()) {
                    return;
                }
            }
        } else {
            for(int k = 0; k < 3; ++k) {
                BlockPos blockpos1 = pos.offset(random.nextInt(5) - 2, 0, random.nextInt(5) - 2);
                if (!world.isLoaded(blockpos1)) {
                    return;
                }

                if (world.isEmptyBlock(blockpos1.above()) && this.isFlammable(world, blockpos1, Direction.UP)) {
                    world.setBlockAndUpdate(blockpos1.above(), ForgeEventFactory.fireFluidPlaceBlockEvent(world, blockpos1.above(), pos, Blocks.FIRE.defaultBlockState()));
                }
            }
        }


        //随机点燃周围4格内1-3个生物 20%概率
        if (random.nextDouble() < 0.2) {
            AABB aabb = new AABB(Vec3.atLowerCornerOf(pos.offset(-3, 0, -3)), Vec3.atLowerCornerOf(pos.offset(3, 1, 3)));
            int num = i;
            for (LivingEntity living : world.getEntitiesOfClass(LivingEntity.class, aabb)) {
                if (num <= 0) break;
                living.setSecondsOnFire(3);
                num--;
            }

        }
    }

    /**
     * 寻找四周可点燃方块
     * @param worldIn 世界
     * @param pos 当前空气坐标
     * @return 是否存在
     */
    private boolean isSurroundingBlockFlammable(Level worldIn, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (this.isFlammable(worldIn, pos.relative(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    //是否会着火
    private boolean isFlammable(Level world, BlockPos pos, Direction face) {
        return world.isLoaded(pos) && world.getBlockState(pos).isFlammable(world, pos, face);
    }
}
