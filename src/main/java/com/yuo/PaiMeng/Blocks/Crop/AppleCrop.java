package com.yuo.PaiMeng.Blocks.Crop;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Items.PMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

//苹果果实
public class AppleCrop extends CropBlock{
    private static final BlockBehaviour.Properties CROP = BlockBehaviour.Properties.of().noCollission().randomTicks().
            strength(5.0f, 10.0f).sound(SoundType.CROP); //作物
    public static final IntegerProperty CROP_AGE = BlockStateProperties.NOTE; // 熟透值 (0..24)
    public static final IntegerProperty AGE = BlockStateProperties.AGE_7;
    private static final VoxelShape[] SHAPE_BY_AGE = new VoxelShape[]{
            Block.box(6.0D, 12.0D, 6.0D, 10.0D, 16.0D, 10.0D),
            Block.box(4.0D, 9.0D, 4.0D, 12.0D, 16.0D, 12.0D),
            Block.box(3.0D, 6.0D, 3.0D, 13.0D, 16.0D, 13.0D),
            Block.box(2.0D, 2.0D, 2.0D, 14.0D, 16.0D, 14.0D)};

    public AppleCrop() {
        super(CROP);
        this.registerDefaultState(this.stateDefinition.any().setValue(this.getAgeProperty(), 0).setValue(CROP_AGE, 0));
    }

    @Override
    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        int age = this.getAge(state);
        if (age < 2) return SHAPE_BY_AGE[0];
        else if (age < 5) return SHAPE_BY_AGE[1];
        else if (age < 7) return SHAPE_BY_AGE[2];
        else return SHAPE_BY_AGE[3];
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return true;
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!level.isAreaLoaded(pos, 1)) return; // Forge: prevent loading unloaded chunks when checking neighbor's light
        int age = this.getAge(state);
        if (age < this.getMaxAge()) {
            float f = getGrowthSpeed(this, level, pos);
            if (net.minecraftforge.common.ForgeHooks.onCropsGrowPre(level, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
                level.setBlock(pos, this.getStateForAge(age + 1), 2);
                net.minecraftforge.common.ForgeHooks.onCropsGrowPost(level, pos, state);
            }
        }
        int cropAge = this.getCropAge(state);
        if (age == this.getMaxAge() && cropAge < this.getMaxCropAge() && random.nextInt(100) > 70) { //30%概率增加熟透值
            level.setBlock(pos, this.withCropAge( cropAge + random.nextInt(2), state), 2);
        }
        if (cropAge == this.getMaxCropAge() && random.nextInt(100) > 90){ //10%概率熟透掉落
           spawnDrop(level, pos, random, state, false);
        }
    }

    //设置熟透值
    private BlockState withCropAge(int age, BlockState state) {
        return state.setValue(CROP_AGE, Integer.valueOf(age));
    }

    private int getCropAge(BlockState state){
        return state.getValue(CROP_AGE);
    }
    private int getMaxCropAge(){ return 24;}

    @Override
    public void growCrops(Level level, BlockPos pos, BlockState state) {
        int i = this.getAge(state) + this.getAgeIncrease(level, state);
        int j = this.getMaxAge();
        if (i > j) {
            i = j;
        }
        level.setBlock(pos, this.getStateForAge(i), 2);
    }

    //生长时年龄增长随机数
    protected int getAgeIncrease(Level level, BlockState state) {
        return state.getBlock() == PMBlocks.sunAppleCrop.get() ?
                Mth.nextInt(level.random, 0, 3) : Mth.nextInt(level.random, 1, 4);
    }

    //是否能放置（1.16.5 的 isValidPosition）
    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getBlock() == PMBlocks.appleLeaf.get()){
            return true;
        }
        BlockState blockState = level.getBlockState(pos.above());
        if (blockState.getBlock() == PMBlocks.appleLeaf.get()){
            return true;
        }
        return super.canSurvive(state, level, pos);
    }

    //苹果能使用骨粉催熟
    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos pos, BlockState state, boolean isClient) {
        return (state.getBlock() == PMBlocks.appleCrop.get() || state.getBlock() == PMBlocks.paopaojuCrop.get())
                && !this.isMaxAge(state);
    }

    @Override
    protected int getBonemealAgeIncrease(Level level) {
        return Mth.nextInt(level.random, 0, 4);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE, CROP_AGE);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(handIn);
        if (!level.isClientSide){
            if (heldItem.is(Items.DEBUG_STICK) || heldItem.getItem() instanceof BoneMealItem)
                return super.use(state, level, pos, player, handIn, hit);
            if (this.getAge(state) == this.getMaxAge()){
                spawnDrop(level, pos, level.random, state, true);
            }
        }
        return InteractionResult.SUCCESS;
    }

    /**
     * 根据不同果实生成果实掉落物和经验球 并重置年龄和熟透值
     * @param level
     * @param pos
     * @param random
     * @param state
     * @param flag 是否有经验掉落 自然掉落不会有经验
     */
    private void spawnDrop(Level level, BlockPos pos, RandomSource random, BlockState state, boolean flag){
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
        }else if (state.getBlock() == PMBlocks.zaoyeCrop.get()){
            stack = new ItemStack(PMItems.zaoye.get());
            exp = random.nextInt(2) + 1;
        }else if (state.getBlock() == PMBlocks.paopaojuCrop.get()){
            stack = new ItemStack(PMItems.paopaoju.get());
            exp = random.nextInt(3) + 1;
        }
        level.addFreshEntity(new ItemEntity(level, pos.getX() + random.nextDouble(), pos.getY(), pos.getZ() + random.nextDouble(), stack));
        if (random.nextInt(100) > 90 && flag) { //概率产出经验
            level.addFreshEntity(new ExperienceOrb(level, pos.getX() + random.nextDouble(), pos.getY(), pos.getZ() + random.nextDouble(), exp));
        }
        level.setBlock(pos, state.setValue(AGE, 0).setValue(CROP_AGE,0), 3);
    }
}
