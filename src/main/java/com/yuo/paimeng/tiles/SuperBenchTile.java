package com.yuo.paimeng.tiles;

import com.yuo.paimeng.menu.SuperBenchContainer;
import com.yuo.paimeng.recipe.BenchRecipe;
import com.yuo.paimeng.recipe.ModRecipeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SuperBenchTile extends AbstractCookingTile {

    public SuperBenchTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.SUPER_BENCH_TILE.get(), pos, state);
    }

    public SuperBenchTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 6); // 工作台总共 7 格 (0-4 输入, 5 输出)
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("gui.paimeng.super_bench");
    }

    @Override
    protected RecipeType<BenchRecipe> getRecipeType() {
        return ModRecipeType.BENCH;
    }

    @Override
    protected int getMaxRecipeLevel() {
        return Integer.MAX_VALUE; // 工作台不限制食物等级
    }

    @Override
    protected int getOutputSlot() {
        return 5;
    }

    @Override
    public void tickInternal(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide) return;

        syncPlayerData(world);
        // 2. 检查配方
        checkRecipe(world);
    }

    @Override
    protected void readAdditional(CompoundTag nbt) {

    }

    @Override
    protected void writeAdditional(CompoundTag compound) {

    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? new int[]{5} : new int[]{0, 1, 2, 3, 4};
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new SuperBenchContainer(id, player, this);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.getBlockPos()) != this) {
            return false;
        }else return player.distanceToSqr(this.getBlockPos().getX(), this.getBlockPos().getY(), this.getBlockPos().getZ()) < 64.0D;
    }
}