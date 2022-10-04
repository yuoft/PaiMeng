package com.yuo.PaiMeng.Tiles;

import com.yuo.PaiMeng.Blocks.Crop.BingWuHuaCrop;
import com.yuo.PaiMeng.Blocks.Crop.LieYanHuaCrop;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.LavaFluid;
import net.minecraft.fluid.WaterFluid;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;

import java.util.Random;

public class AbsBingWuHuaTile extends TileEntity implements ITickableTileEntity {
    private int tick = 0;

    public AbsBingWuHuaTile(TileEntityType<?> type) {
        super(type);
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;
        tick++;

        if (tick == 50) {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof BingWuHuaCrop){
                Integer age = state.get(CropsBlock.AGE);
                if (age >= 7) randomFrozen(world, pos, world.rand);
            } else randomFrozen(world, pos, world.rand);
            tick = 0;
        }

        if (tick % 5 == 0){
            offLava(world, pos);
        }
    }

    /**
     * 随机冻结周围5*5*2范围的水源
     * @param world
     * @param pos
     * @param rand
     */
    private void randomFrozen(World world, BlockPos pos, Random rand) {
        int i = rand.nextInt(3) + 2;
        for (int j = 0; j < i; j++){
            BlockPos blockpos = pos.add(rand.nextInt(5) - 2, rand.nextInt(2) - 1, rand.nextInt(5) - 2);
            if (!world.isBlockPresent(blockpos)) return;
            BlockState state = world.getBlockState(blockpos);
            Material material = state.getMaterial();
            if (material == Material.WATER && state.getBlock() == Blocks.WATER && state.get(FlowingFluidBlock.LEVEL) == 0){
                BlockState blockstate = Blocks.FROSTED_ICE.getDefaultState();
                world.setBlockState(blockpos, blockstate);
                world.getPendingBlockTicks().scheduleTick(blockpos, Blocks.FROSTED_ICE, MathHelper.nextInt(rand, 120, 180));
            }
            if (state.getBlock() == Blocks.FROSTED_ICE){
                world.setBlockState(blockpos, Blocks.ICE.getDefaultState());
            }
        }
    }

    /**
     * 熄灭周围岩浆
     * @param world
     * @param pos
     */
    private void offLava(World world, BlockPos pos){
        for (BlockPos blockPos : BlockPos.getAllInBoxMutable(pos.add(-1, -1, -1), pos.add(1, 0, 1))) {
            BlockState state = world.getBlockState(blockPos);
            if (state.getBlock() == Blocks.LAVA){
                Block block = world.getFluidState(blockPos).isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE;
                world.setBlockState(pos, net.minecraftforge.event.ForgeEventFactory.fireFluidPlaceBlockEvent(world, pos, pos, block.getDefaultState()));
                triggerMixEffects(world, pos);
            }
        }

    }

    //播放岩浆熄灭声音
    private void triggerMixEffects(IWorld worldIn, BlockPos pos) {
        worldIn.playEvent(1501, pos, 0);
    }
}
