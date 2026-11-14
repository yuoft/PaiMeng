package com.yuo.paimeng.tiles;

import com.yuo.paimeng.blocks.CookingPot;
import com.yuo.paimeng.menu.PotContainer;
import com.yuo.paimeng.recipe.ModRecipeType;
import com.yuo.paimeng.recipe.PotRecipe;
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

public class PotTile extends AbstractCookingTile {
    private final int[] FIRE_TIME = {0, 20 * 90, 20 * 90 * 2, 20 * 90 * 3, 20 * 90 * 4}; // 燃料时间表
    private int TIME; // 当前已燃烧时间

    public PotTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.POT_TILE.get(), pos, state);
    }

    public PotTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 6); // 锅总共 6 格
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("gui.paimeng.pot");
    }

    @Override
    protected RecipeType<PotRecipe> getRecipeType() {
        return ModRecipeType.POT;
    }

    @Override
    protected int getMaxRecipeLevel() {
        return 3; // 锅最大能做出 3 级食物
    }

    @Override
    protected int getOutputSlot() {
        return 5;
    }

    @Override
    public void tickInternal(Level world, BlockPos pos, BlockState state) {
        if (world.isClientSide) return;

        syncPlayerData(world);

        // 1. 下雨熄灭逻辑
        if (world.isRainingAt(pos.above()) && state.getValue(CookingPot.FIRE)) {
            world.levelEvent(null, 1009, pos, 0);
            state = state.setValue(CookingPot.FIRE, Boolean.FALSE);
            world.setBlockAndUpdate(pos, state);
        }

        // 2. 燃烧时间减少
        if (state.getValue(CookingPot.FIRE)) {
            TIME--;
            if (TIME < 0) TIME = 0;
            this.data.set(0, TIME);
        }

        // 3. 燃料消耗
        int fuel = state.getValue(CookingPot.FUEL);
        if (TIME <= FIRE_TIME[Math.max(fuel - 1, 0)]) {
            state = state.setValue(CookingPot.FUEL, Math.max(fuel - 1, 0));
            world.setBlockAndUpdate(pos, state);
            fuel = Math.max(fuel - 1, 0);
        }

        if (fuel == 0 && state.getValue(CookingPot.FIRE)) {
            state = state.setValue(CookingPot.FIRE, false);
            world.setBlockAndUpdate(pos, state);
        }

        // 4. 检查配方
        checkRecipe(world);
    }

    public void setBurnTime() {
        TIME = Math.min(TIME + FIRE_TIME[1], FIRE_TIME[4]);
        this.data.set(0, TIME);
        setChanged();
    }

    @Override
    protected void readAdditional(CompoundTag nbt) {
        this.TIME = nbt.getInt("time");
    }

    @Override
    protected void writeAdditional(CompoundTag compound) {
        compound.putInt("time", this.TIME);
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        // 原逻辑保持不变：DOWN 返回 {4}
        return side == Direction.DOWN ? new int[]{4} : new int[]{0, 1, 2, 3};
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new PotContainer(id, player, this);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.getBlockPos()) != this) {
            return false;
        } else if (this.level.getBlockState(this.getBlockPos()).getValue(CookingPot.FIRE)) {
            return true; // 燃烧中，不管距离
        } else {
            return this.TIME > 0 && this.stillValid(player); // 熄灭时需满足 TIME 和距离
        }
    }
}