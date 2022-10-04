package com.yuo.PaiMeng.Tiles;

import com.yuo.PaiMeng.Blocks.Crop.LieYanHuaCrop;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FlintAndSteelItem;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class AbsLieYanHuaTile extends TileEntity implements ITickableTileEntity {
    private int tick = 0;

    public AbsLieYanHuaTile(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        tick++;

        if (tick == 40) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof LieYanHuaCrop){
                Integer age = state.get(CropsBlock.AGE);
                if (age >= 7) randomFire(world, pos, world.rand);
            } else randomFire(world, pos, world.rand);
            tick = 0;
        }

        if (tick % 5 == 0){
            meltIce(world, pos);
        }
    }

    /**
     * 融化周围冰块
     * @param world
     * @param pos
     */
    private void meltIce(World world, BlockPos pos){
        for (BlockPos blockPos : BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 0, 1))) {
            BlockState state = world.getBlockState(blockPos);
            if (state.getBlock() == Blocks.ICE){
               turnIntoWater(state, world, blockPos);
            }
        }
    }

    //冰化为水
    protected void turnIntoWater(BlockState state, World world, BlockPos pos) {
        if (world.getDimensionType().isUltrawarm()) {
            world.removeBlock(pos, false);
        } else {
            world.setBlockState(pos, Blocks.WATER.getDefaultState());
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
    public void randomFire(World world, BlockPos pos, Random random) {
        int i = random.nextInt(3);
        if (i > 0) {
            BlockPos blockpos = pos;

            for(int j = 0; j < i; ++j) {
                blockpos = blockpos.add(random.nextInt(5) - 2, 1, random.nextInt(5) - 2);
                if (!world.isBlockPresent(blockpos)) {
                    return;
                }

                BlockState blockstate = world.getBlockState(blockpos);
                if (blockstate.isAir()) {
                    if (this.isSurroundingBlockFlammable(world, blockpos)) {
                        world.setBlockState(blockpos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(world, blockpos, pos, Blocks.FIRE.getDefaultState()));
                        return;
                    }
                } else if (blockstate.getMaterial().blocksMovement()) {
                    return;
                }
            }
        } else {
            for(int k = 0; k < 3; ++k) {
                BlockPos blockpos1 = pos.add(random.nextInt(5) - 2, 0, random.nextInt(5) - 2);
                if (!world.isBlockPresent(blockpos1)) {
                    return;
                }

                if (world.isAirBlock(blockpos1.up()) && this.isFlammable(world, blockpos1, Direction.UP)) {
                    world.setBlockState(blockpos1.up(), net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(world, blockpos1.up(), pos, Blocks.FIRE.getDefaultState()));
                }
            }
        }


        //随机点燃周围4格内1-3个生物 20%概率
        if (random.nextDouble() < 0.2) {
            AxisAlignedBB aabb = new AxisAlignedBB(pos.add(-3, 0, -3), pos.add(3, 1, 3));
            int num = i;
            for (LivingEntity living : world.getEntitiesWithinAABB(LivingEntity.class, aabb)) {
                if (num <= 0) break;
                living.setFire(3);
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
    private boolean isSurroundingBlockFlammable(IWorldReader worldIn, BlockPos pos) {
        for (Direction direction : Direction.values()) {
            if (this.isFlammable(worldIn, pos.offset(direction), direction.getOpposite())) {
                return true;
            }
        }

        return false;
    }

    //是否会着火
    private boolean isFlammable(IWorldReader world, BlockPos pos, Direction face) {
        return (pos.getY() < 0 || pos.getY() >= 256 || world.isBlockLoaded(pos))
                && world.getBlockState(pos).isFlammable(world, pos, face);
    }
}
