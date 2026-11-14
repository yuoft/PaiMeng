package com.yuo.paimeng.tiles;

import com.yuo.paimeng.blocks.crop.BingWuHuaCrop;
import com.yuo.paimeng.PMConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;

public class AbsBingWuHuaTile extends BlockEntity {
    private int tick = 0;

    public AbsBingWuHuaTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public static void tick(Level world, BlockPos pos, BlockState state, AbsBingWuHuaTile tile) {
        if (world.isClientSide) return;
        tile.tick++;

        if (tile.tick == PMConfig.SERVER.bingWUHuaTime.get()) {
            if (state.getBlock() instanceof BingWuHuaCrop){
                Integer age = state.getValue(CropBlock.AGE);
                if (age >= 7) tile.randomFrozen(world, pos, world.random);
            } else tile.randomFrozen(world, pos, world.random);
            tile.tick = 0;
        }

        if (tile.tick % PMConfig.SERVER.lieYanHuaAndBingWUHuaTime.get() == 0){
            tile.offLava(world, pos);
        }
    }

    /**
     * 随机冻结周围5*5*2范围的水源
     * @param world 世界
     * @param pos 坐标
     * @param rand 随机数
     */
    private void randomFrozen(Level world, BlockPos pos, RandomSource rand) {
        int i = rand.nextInt(3) + 2;
        for (int j = 0; j < i; j++){
            BlockPos blockpos = pos.offset(rand.nextInt(5) - 2, rand.nextInt(2) - 1, rand.nextInt(5) - 2);
            if (!world.isLoaded(blockpos)) return;
            BlockState state = world.getBlockState(blockpos);
            if (state.getBlock() == Blocks.WATER && state.getFluidState().isSource()){
                BlockState blockstate = Blocks.FROSTED_ICE.defaultBlockState();
                world.setBlockAndUpdate(blockpos, blockstate);
                world.scheduleTick(blockpos, Blocks.FROSTED_ICE, Mth.nextInt(rand, 120, 180));
            }
            if (state.getBlock() == Blocks.FROSTED_ICE){
                world.setBlockAndUpdate(blockpos, Blocks.ICE.defaultBlockState());
            }
        }
    }

    /**
     * 熄灭周围岩浆
     * @param world 世界
     * @param pos 坐标
     */
    private void offLava(Level world, BlockPos pos){
        for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 0, 1))) {
            BlockState state = world.getBlockState(blockPos);
            if (state.getBlock() == Blocks.LAVA){
                Block block = world.getFluidState(blockPos).isSource() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE;
                world.setBlockAndUpdate(pos, ForgeEventFactory.fireFluidPlaceBlockEvent(world, pos, pos, block.defaultBlockState()));
                triggerMixEffects(world, pos);
            }
        }

    }

    //播放岩浆熄灭声音
    private void triggerMixEffects(LevelAccessor worldIn, BlockPos pos) {
        worldIn.levelEvent(1501, pos, 0);
    }
}
