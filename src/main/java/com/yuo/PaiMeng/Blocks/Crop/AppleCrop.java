package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Items.PMItems;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BoneMealItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.Random;

//苹果果实
public class AppleCrop extends CropsBlock{
    private static Block.Properties CROP = AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().
            hardnessAndResistance(5.0f, 10.0f).sound(SoundType.CROP); //作物
    public static final IntegerProperty CROP_AGE = BlockStateProperties.NOTE_0_24; // 熟透值
    public static final IntegerProperty AGE = BlockStateProperties.AGE_0_7;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.makeCuboidShape(6.0D, 12.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            Block.makeCuboidShape(4.0D, 9.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.makeCuboidShape(3.0D, 6.0D, 3.0D, 13.0D, 16.0D, 13.0D),
            Block.makeCuboidShape(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D)};

    public AppleCrop() {
        super(CROP);
        this.setDefaultState(this.stateContainer.getBaseState().with(this.getAgeProperty(), 0).with(CROP_AGE, 0));
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        int age = this.getAge(state);
        if (age < 2) return SHAPE_BY_AGE[0];
        else if (age < 5) return SHAPE_BY_AGE[1];
        else if (age < 7) return SHAPE_BY_AGE[2];
        else return SHAPE_BY_AGE[3];
    }

    @Override
    public boolean ticksRandomly(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld worldIn, BlockPos pos, Random random) {
        if (!worldIn.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        int age = this.getAge(state);
        if (age < this.getMaxAge()) {
            float f = getGrowthChance(this, worldIn, pos);
            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(worldIn, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
                worldIn.setBlockState(pos, this.withAge(age + 1), 2);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(worldIn, pos, state);
            }
        }
        int cropAge = this.getCropAge(state);
        if (age == this.getMaxAge() && cropAge < this.getMaxCropAge() && random.nextInt(100) > 70) { //30%概率增加熟透值
            worldIn.setBlockState(pos, this.withCropAge( cropAge + random.nextInt(2), state));
        }
        if (cropAge == this.getMaxCropAge() && random.nextInt(100) > 90){ //10%概率熟透掉落
           spawnDrop(worldIn, pos, random, state, false);
        }
    }

    //设置熟透值
    private BlockState withCropAge(int age, BlockState state) {
        return state.with(CROP_AGE, Integer.valueOf(age));
    }

    private int getCropAge(BlockState state){
        return state.get(CROP_AGE);
    }
    private int getMaxCropAge(){ return 24;}

    @Override
    public void grow(World worldIn, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + this.getAgeIncrease(worldIn, state);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        worldIn.setBlockState(pos, this.withAge(i), 2);
    }

    //生长时年龄增长随机数
    protected int getAgeIncrease(World worldIn, BlockState state) {
        return state.getBlock() == PMBlocks.sunAppleCrop.get() ?
                MathHelper.nextInt(worldIn.rand, 0, 3) : MathHelper.nextInt(worldIn.rand, 1, 4);
    }

    //是否能放置
    @Override
    public boolean isValidPosition(BlockState state, IWorldReader worldIn, BlockPos pos) {
        if (state.getBlock() == PMBlocks.appleLeaf.get()){
            return true;
        }
        BlockState blockState = worldIn.getBlockState(pos.up());
        if (blockState.getBlock() == PMBlocks.appleLeaf.get()){
            return true;
        }
        return super.isValidPosition(state, worldIn, pos);
    }

    //苹果能使用骨粉催熟
    @Override
    public boolean canUseBonemeal(World worldIn, Random rand, BlockPos pos, BlockState state) {
        return state.getBlock() == PMBlocks.appleCrop.get();
    }

    @Override
    protected int getBonemealAgeIncrease(World worldIn) {
        return MathHelper.nextInt(worldIn.rand, 0, 4);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(AGE, CROP_AGE);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(handIn);
        if (!worldIn.isRemote){
            if (heldItem.getItem() == Items.DEBUG_STICK || heldItem.getItem() instanceof BoneMealItem)
                return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
            if (this.getAge(state) == this.getMaxAge()){
                spawnDrop(worldIn, pos, worldIn.rand, state, true);
            }
        }
        return ActionResultType.SUCCESS;
    }

    /**
     * 根据不同果实生成果实掉落物和经验球 并重置年龄和熟透值
     * @param world
     * @param pos
     * @param random
     * @param state
     * @param flag 是否有经验掉落 自然掉落不会有经验
     */
    private void spawnDrop(World world, BlockPos pos, Random random, BlockState state, boolean flag){
        ItemStack stack = ItemStack.EMPTY;
        int exp = 0;
        if (state.getBlock() == PMBlocks.appleCrop.get()){
            stack = new ItemStack(PMItems.pingguo.get());
            exp = random.nextInt(2) + 1;
        }else if (state.getBlock() == PMBlocks.sunAppleCrop.get()){
            stack = new ItemStack(PMItems.riluoguo.get());
            exp = random.nextInt(4) + 1;
        }else if (state.getBlock() == PMBlocks.purpleAppleCrop.get()){
            stack = new ItemStack(PMItems.jingua.get());
            exp = random.nextInt(3) + 1;
        }
        world.addEntity(new ItemEntity(world, pos.getX() + random.nextDouble(), pos.getY(), pos.getZ() + random.nextDouble(), stack));
        if (random.nextInt(100) > 90 && flag) { //概率产出经验
            world.addEntity(new ExperienceOrbEntity(world, pos.getX() + random.nextDouble(), pos.getY(), pos.getZ() + random.nextDouble(), exp));
        }
        world.setBlockState(pos, state.with(AGE, 0).with(CROP_AGE,0));
    }
}
