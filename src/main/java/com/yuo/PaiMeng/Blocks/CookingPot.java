package com.yuo.PaiMeng.Blocks;

import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Tiles.PotTile;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.AbstractFireballEntity;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Random;
import java.util.UUID;

public class CookingPot extends Block {
    public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
    public static final BooleanProperty FIRE = BlockStateProperties.LIT; //燃烧状态
    public static final IntegerProperty FUEL = IntegerProperty.create("fuel", 0, 4); //燃料数量
    private final int damage = 3; //火焰伤害值

    public CookingPot() {
        super(Properties.create(Material.IRON).hardnessAndResistance(10, 10).harvestLevel(0)
                .harvestTool(ToolType.PICKAXE).setRequiresTool().notSolid().setLightLevel(CookingBench.getLightValueLit(10)));
        this.setDefaultState(this.getDefaultState().with(FACING, Direction.NORTH).with(FIRE, Boolean.FALSE).with(FUEL, 0));
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        ItemStack heldItem = player.getHeldItem(handIn);
        if (!worldIn.isRemote){
            if (heldItem.getItem() instanceof ShovelItem){ //铲子扑灭火
                worldIn.setBlockState(pos, state.with(CookingPot.FIRE, Boolean.FALSE), 11);
                worldIn.playEvent(null, 1009, pos, 0);
                heldItem.damageItem(1, player, e -> e.sendBreakAnimation(handIn)); //消耗铲子耐久
                player.swingArm(handIn); //摆臂
                return ActionResultType.SUCCESS;
            }
            if (heldItem.getItem() instanceof FlintAndSteelItem){ //打火石点火
                if (state.get(CookingPot.FUEL) <= 0){ //没有燃料，无法点燃
                    player.sendMessage(new TranslationTextComponent("paimeng.message.pot_fuel"), UUID.randomUUID());
                    return ActionResultType.SUCCESS;
                }
                worldIn.setBlockState(pos, state.with(CookingPot.FIRE, Boolean.TRUE), 11);
                worldIn.playSound(player, pos, SoundEvents.ITEM_FLINTANDSTEEL_USE, SoundCategory.BLOCKS, 1.0F, new Random().nextFloat() * 0.4F + 0.8F);
                heldItem.onItemUsed(worldIn, player, 1); //使用打火石
                player.swingArm(handIn);
                return ActionResultType.SUCCESS;
            }
            if (heldItem.isEmpty()){ //空手右键打开gui
                if (!state.get(FIRE)) {
                    player.sendMessage(new TranslationTextComponent("paimeng.message.pot_open"), UUID.randomUUID());
                    return ActionResultType.SUCCESS;
                }
                TileEntity tileEntity = worldIn.getTileEntity(pos);
                if (tileEntity instanceof PotTile){ //打开gui
                    ((PotTile) tileEntity).setPlayer(player);
                    player.openContainer((INamedContainerProvider) tileEntity);
                    player.addStat(Stats.INTERACT_WITH_FURNACE);
//                    NetworkHooks.openGui((ServerPlayerEntity) player, (INamedContainerProvider) tileEntity, pos);
                }
                return ActionResultType.SUCCESS;
            }
            if (state.get(FUEL) < 4 && ForgeHooks.getBurnTime(heldItem) > 0 && !(heldItem.getItem() instanceof BucketItem)){ //手持木头添加燃料
                int i = damageWoodCount(heldItem);
                int count = heldItem.getCount();
                if (i > 0){
                    if (count >= i){
                        worldIn.setBlockState(pos, state.with(FUEL, state.get(FUEL) + 1), 2);
                        TileEntity tile = worldIn.getTileEntity(pos); //同时设置tile燃烧时间
                        if (tile instanceof PotTile) {
                            PotTile potTile = (PotTile) tile;
                            potTile.setBurnTime();
                        }
                        if (!player.isCreative()) heldItem.setCount(heldItem.getCount() - i);
                    }else player.sendMessage(new TranslationTextComponent("paimeng.message.pot_fuel_add1"), UUID.randomUUID());
                }else player.sendMessage(new TranslationTextComponent("paimeng.message.pot_fuel_add"), UUID.randomUUID());
                return ActionResultType.SUCCESS;
            }
        }
        else PaiMeng.PROXY.setRefrencedTE(worldIn.getTileEntity(pos)); //保存当前tile坐标
        return ActionResultType.SUCCESS;
    }

    //破坏后掉落
    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.matchesBlock(newState.getBlock())) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof PotTile) {
                InventoryHelper.dropInventoryItems(worldIn, pos, (PotTile)tileentity);
                worldIn.updateComparatorOutputLevel(pos, this);
            }

            super.onReplaced(state, worldIn, pos, newState, isMoving);
        }
    }

    //实体碰撞 给予实体火焰伤害
    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        if (!entityIn.isImmuneToFire() && state.get(FIRE) && entityIn instanceof LivingEntity && !EnchantmentHelper.hasFrostWalker((LivingEntity)entityIn)) {
            entityIn.attackEntityFrom(DamageSource.IN_FIRE, (float)this.damage);
        }
        super.onEntityCollision(state, worldIn, pos, entityIn);
    }

    //粒子效果
    @Override
    public void animateTick(BlockState stateIn, World worldIn, BlockPos pos, Random rand) {
        if (stateIn.get(FIRE)) {
            if (rand.nextInt(10) == 0) {
                worldIn.playSound((double)pos.getX() + 0.5D, (double)pos.getY() + 0.5D, (double)pos.getZ() + 0.5D, SoundEvents.BLOCK_CAMPFIRE_CRACKLE, SoundCategory.BLOCKS, 0.5F + rand.nextFloat(), rand.nextFloat() * 0.7F + 0.6F, false);
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
    public BlockState updatePostPlacement(BlockState stateIn, Direction facing, BlockState facingState, IWorld worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facingState.getMaterial() == Material.WATER && stateIn.get(FIRE)){ //周围有水，火会熄灭
            if (!worldIn.isRemote()) {
                worldIn.playEvent((PlayerEntity)null, 1009, currentPos, 0);
            }
            return stateIn.with(FIRE, Boolean.FALSE);
        }
        return stateIn;
    }

    //投掷物碰撞
    @Override
    public void onProjectileCollision(World worldIn, BlockState state, BlockRayTraceResult hit, ProjectileEntity projectile) {
        if (!state.get(FIRE) && state.get(FUEL) > 0){
            if (projectile instanceof AbstractFireballEntity || projectile instanceof FireworkRocketEntity){
                state.with(FIRE, Boolean.TRUE); //部分火系投掷物会点燃
            }
            if (projectile instanceof AbstractArrowEntity){ //火矢
                AbstractArrowEntity arrow = (AbstractArrowEntity) projectile;
                PlayerEntity player = worldIn.getPlayerByUuid(arrow.getUniqueID());
                if (player == null) return;
                int flame = EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, player.getHeldItemMainhand());
                if (flame > 0) state.with(FIRE, Boolean.TRUE);
            }
        }
    }


    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite()).with(FIRE, Boolean.FALSE).with(FUEL, 0);
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, FIRE, FUEL);
    }

    //方块实体渲染
    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new PotTile();
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        Direction direction = state.get(FACING);
        VoxelShape shape = VoxelShapes.empty();
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
     * @param stack
     * @return
     */
    private int damageWoodCount(ItemStack stack){
        Item item = stack.getItem();
        Block block = Block.getBlockFromItem(item);
        if (block.getDefaultState().getMaterial() == Material.WOOD){
            int burnTime = ForgeHooks.getBurnTime(stack);
            if (burnTime < 600) return  (int) Math.ceil(300.0 / burnTime);
            return 1;
        }
        return 0;
    }

    //烹饪锅碰撞箱
    private VoxelShape makeShapeWE(){
        VoxelShape shape = Block.makeCuboidShape(3, 0, 3, 13, 4, 13);
        VoxelShape shape1 = Block.makeCuboidShape(0, 0, 2, 16, 22, 2);
        VoxelShape shape2 = Block.makeCuboidShape(0, 0, 14, 16, 22, 14);
        VoxelShape shape3 = Block.makeCuboidShape(4, 9, 4, 12, 14, 12);
//        VoxelShape shape4 = Block.makeCuboidShape(4, 14, 7, 12, 22, 8);
        VoxelShape shape5 = Block.makeCuboidShape(7.5, 20.5, 0, 8.5, 21.5, 16);

        return VoxelShapes.or(shape, shape1, shape2, shape3, shape5);
    }
    private VoxelShape makeShapeNS(){
        VoxelShape shape = Block.makeCuboidShape(3, 0, 3, 13, 4, 13);
        VoxelShape shape1 = Block.makeCuboidShape(14, 0, 0, 16, 22, 16);
        VoxelShape shape2 = Block.makeCuboidShape(0, 0, 0, 2, 22, 16);
        VoxelShape shape3 = Block.makeCuboidShape(4, 9, 4, 12, 14, 12);
//        VoxelShape shape4 = Block.makeCuboidShape(7, 14, 4, 8, 22, 12);
        VoxelShape shape5 = Block.makeCuboidShape(0, 20.5, 7.5, 16, 21.5, 8.5);

        return VoxelShapes.or(shape, shape1, shape2, shape3, shape5);
    }
}
