package com.yuo.paimeng.blocks;

import com.yuo.paimeng.PaiMeng;
import com.yuo.paimeng.tiles.PotTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.FlintAndSteelItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;

import javax.annotation.Nullable;

public class CookingPot extends BaseEntityBlock {
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty FIRE = BlockStateProperties.LIT; //燃烧状态
    public static final IntegerProperty FUEL = IntegerProperty.create("fuel", 0, 4); //燃料数量
    private final int damage = 3; //火焰伤害值

    public CookingPot() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.METAL).strength(10, 10)
                .requiresCorrectToolForDrops().noOcclusion().sound(SoundType.METAL)
                .lightLevel(CookingBench.getLightValueLit(10)));
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(FIRE, Boolean.FALSE).setValue(FUEL, 0));
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        ItemStack heldItem = player.getItemInHand(handIn);
        if (!worldIn.isClientSide){
            if (heldItem.getItem() instanceof ShovelItem){ //铲子扑灭火
                worldIn.setBlock(pos, state.setValue(CookingPot.FIRE, Boolean.FALSE), 11);
                worldIn.levelEvent(null, 1009, pos, 0);
                heldItem.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(handIn)); //消耗铲子耐久
                player.swing(handIn); //摆臂
                return InteractionResult.SUCCESS;
            }
            if (heldItem.getItem() instanceof FlintAndSteelItem){ //打火石点火
                if (state.getValue(CookingPot.FUEL) <= 0){ //没有燃料，无法点燃
                    player.sendSystemMessage(Component.translatable("paimeng.message.pot_fuel"));
                    return InteractionResult.SUCCESS;
                }
                worldIn.setBlock(pos, state.setValue(CookingPot.FIRE, Boolean.TRUE), 11);
                worldIn.playSound(player, pos, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, worldIn.getRandom().nextFloat() * 0.4F + 0.8F);
                heldItem.hurtAndBreak(1, player, e -> e.broadcastBreakEvent(handIn)); //使用打火石
                player.swing(handIn);
                return InteractionResult.SUCCESS;
            }
            if (heldItem.isEmpty()){ //空手右键打开gui
                if (!state.getValue(FIRE)) {
                    player.sendSystemMessage(Component.translatable("paimeng.message.pot_open"));
                    return InteractionResult.SUCCESS;
                }
                BlockEntity tileEntity = worldIn.getBlockEntity(pos);
                if (tileEntity instanceof PotTile potTile && tileEntity instanceof MenuProvider provider){ //打开gui
                    potTile.setPlayer(player);
                    player.openMenu(provider);
                    player.awardStat(Stats.INTERACT_WITH_FURNACE);
                }
                return InteractionResult.SUCCESS;
            }
            if (state.getValue(FUEL) < 4 && ForgeHooks.getBurnTime(heldItem, null) > 0 && !(heldItem.getItem() instanceof BucketItem)){ //手持木头添加燃料
                int i = damageWoodCount(heldItem);
                int count = heldItem.getCount();
                if (i > 0){
                    if (count >= i){
                        worldIn.setBlock(pos, state.setValue(FUEL, state.getValue(FUEL) + 1), 2);
                        BlockEntity tile = worldIn.getBlockEntity(pos); //同时设置tile燃烧时间
                        if (tile instanceof PotTile potTile) {
                            potTile.setBurnTime();
                        }
                        if (!player.isCreative()) heldItem.setCount(heldItem.getCount() - i);
                    }else player.sendSystemMessage(Component.translatable("paimeng.message.pot_fuel_add1"));
                }else player.sendSystemMessage(Component.translatable("paimeng.message.pot_fuel_add"));
                return InteractionResult.SUCCESS;
            }
        }
        else PaiMeng.PROXY.setRefrencedTE(worldIn.getBlockEntity(pos)); //保存当前tile坐标
        return InteractionResult.SUCCESS;
    }

    //破坏后掉落
    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.is(newState.getBlock())) {
            BlockEntity tileentity = worldIn.getBlockEntity(pos);
            if (tileentity instanceof PotTile potTile) {
                Containers.dropContents(worldIn, pos, potTile);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    //实体碰撞 给予实体火焰伤害
    @Override
    public void entityInside(BlockState state, Level worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.fireImmune() && state.getValue(FIRE) && entityIn instanceof LivingEntity living && !EnchantmentHelper.hasFrostWalker(living)) {
            entityIn.hurt(worldIn.damageSources().inFire(), (float)this.damage);
        }
        super.entityInside(state, worldIn, pos, entityIn);
    }

    //粒子效果
    @Override
    public void animateTick(BlockState stateIn, Level worldIn, BlockPos pos, RandomSource rand) {
        if (stateIn.getValue(FIRE)) {
            if (rand.nextInt(10) == 0) {
                worldIn.playLocalSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.CAMPFIRE_CRACKLE, SoundSource.BLOCKS, 0.5F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.6F, false);
            }
            if (rand.nextInt(5) == 0) {
                worldIn.addParticle(ParticleTypes.LAVA, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.25D, (double)pos.getZ() + 0.5D, rand.nextFloat() / 2.0F, 5.0E-5D, rand.nextFloat() / 2.0F);
            }
            if (rand.nextInt(5) == 0){
                worldIn.addParticle(ParticleTypes.LARGE_SMOKE, (double)pos.getX() + 0.5D, (double)pos.getY() + 0.875D, (double)pos.getZ() + 0.5D, rand.nextFloat() / 2.0F, 5.0E-5D, rand.nextFloat() / 2.0F);
            }
        }
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facingState.getFluidState().is(FluidTags.WATER) && stateIn.getValue(FIRE)){ //周围有水，火会熄灭
            if (!worldIn.isClientSide()) {
                worldIn.levelEvent(null, 1009, currentPos, 0);
            }
            return stateIn.setValue(FIRE, Boolean.FALSE);
        }
        return stateIn;
    }

    //投掷物碰撞
    @Override
    public void onProjectileHit(Level worldIn, BlockState state, BlockHitResult hit, Projectile projectile) {
        if (!state.getValue(FIRE) && state.getValue(FUEL) > 0){
            if (projectile instanceof AbstractHurtingProjectile || projectile instanceof FireworkRocketEntity){
                state.setValue(FIRE, Boolean.TRUE); //部分火系投掷物会点燃
            }
            if (projectile instanceof AbstractArrow arrow){ //火矢
                if (arrow.getOwner() instanceof Player player) {
                    int flame = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.FLAMING_ARROWS, player.getMainHandItem());
                    if (flame > 0) state.setValue(FIRE, Boolean.TRUE);
                }
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite()).setValue(FIRE, Boolean.FALSE).setValue(FUEL, 0);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, FIRE, FUEL);
    }

    //方块实体渲染
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PotTile(pos, state);
    }

    //1.20.1 没有 ITickableTileEntity，刻由方块侧 getTicker 提供
    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : (lvl, pos, st, blockEntity) -> {
            if (blockEntity instanceof PotTile potTile) potTile.tick(lvl, pos, st);
        };
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction direction = state.getValue(FACING);
        VoxelShape shape = Shapes.empty();
        switch (direction){
            case WEST:
            case EAST:
                shape = makeShapeWE();break;
            default: shape = makeShapeNS();
        }
        return shape;
    }

    /**
     * 计算消耗木材数量 0：不能使用；燃烧时间小于300tick则数量要求上升
     * 1.20.1 没有 Material，用方块标签判断是否为木质方块
     * @param stack
     * @return
     */
    private int damageWoodCount(ItemStack stack){
        Item item = stack.getItem();
        Block block = Block.byItem(item);
        if (block == Blocks.AIR) return 0;
        BlockState blockState = block.defaultBlockState();
        if (blockState.is(BlockTags.LOGS) || blockState.is(BlockTags.PLANKS)){
            int burnTime = ForgeHooks.getBurnTime(stack, null);
            if (burnTime <= 0) return 0;
            if (burnTime < 600) return  (int) Math.ceil(300.0 / burnTime);
            return 1;
        }
        return 0;
    }

    //烹饪锅碰撞箱
    private VoxelShape makeShapeWE(){
        VoxelShape shape = Block.box(3, 0, 3, 13, 4, 13);
        VoxelShape shape1 = Block.box(0, 0, 2, 16, 22, 2);
        VoxelShape shape2 = Block.box(0, 0, 14, 16, 22, 14);
        VoxelShape shape3 = Block.box(4, 9, 4, 12, 14, 12);
//        VoxelShape shape4 = Block.box(4, 14, 7, 12, 22, 8);
        VoxelShape shape5 = Block.box(7.5, 20.5, 0, 8.5, 21.5, 16);

        return Shapes.or(shape, shape1, shape2, shape3, shape5);
    }
    private VoxelShape makeShapeNS(){
        VoxelShape shape = Block.box(3, 0, 3, 13, 4, 13);
        VoxelShape shape1 = Block.box(14, 0, 0, 16, 22, 16);
        VoxelShape shape2 = Block.box(0, 0, 0, 2, 22, 16);
        VoxelShape shape3 = Block.box(4, 9, 4, 12, 14, 12);
//        VoxelShape shape4 = Block.box(7, 14, 4, 8, 22, 12);
        VoxelShape shape5 = Block.box(0, 20.5, 7.5, 16, 21.5, 8.5);

        return Shapes.or(shape, shape1, shape2, shape3, shape5);
    }
}
