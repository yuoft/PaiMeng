package com.yuo.paimeng.tiles;

import com.yuo.paimeng.blocks.CookingBench;
import com.yuo.paimeng.menu.BenchContainer;
import com.yuo.paimeng.recipe.BenchRecipe;
import com.yuo.paimeng.recipe.ModRecipeType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class BenchTile extends AbstractCookingTile {
    public final int MAX_TIME = 20 * 60 * 16; // 最大燃烧时间16分钟
    private int TIME; // 剩余燃烧时间
    private ItemStack fuelItem; // 燃料记录

    public BenchTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.BENCH_TILE.get(), pos, state);
    }

    public BenchTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state, 7); // 工作台总共 7 格 (0-4 输入, 5 输出, 6 假燃料)
    }

    @Override
    protected Component getDefaultName() {
        return Component.translatable("gui.paimeng.bench");
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

        // 🔴 记录方块当前的实际状态，而不是记录 Tile 的 TIME
        boolean stateBurning = state.getValue(CookingBench.FIRE);
        boolean changed = false;

        // 1. 燃烧时间减少
        if (this.isBurning()) {
            TIME--;
            this.data.set(0, TIME);
            changed = true;
        }

        // 2. 检查配方
        checkRecipe(world);

        // 3. 状态同步：如果 Tile 在燃烧但方块没亮，或者 Tile 不烧了但方块还亮着
        boolean isBurningNow = this.isBurning();
        if (stateBurning != isBurningNow) {
            state = state.setValue(CookingBench.FIRE, isBurningNow);
            world.setBlockAndUpdate(pos, state);
            if (!isBurningNow) {
                world.levelEvent(null, 1009, pos, 0); // 熄灭音效
            }
            changed = true;
        }

        if (changed) {
            setChanged(world, pos, state);
        }
    }

    public ItemStack getFuelItem() {
        return this.items.get(6);
    }

    public void setBurnTime(int time) {
        TIME = Math.min(TIME + time, MAX_TIME);
        this.data.set(0, TIME);
        setChanged();
    }

    public void setFuelItem(ItemStack stack) {
        ItemStack fuelStack = stack.copy();
        fuelStack.setCount(1); // 假物品只需显示 1 个
        this.fuelItem = fuelStack;
        this.items.set(6, fuelStack);
        setChanged();
    }

    public int getTIME() {
        return TIME;
    }

    private boolean isBurning() {
        return TIME > 0;
    }

    public static void dropContents(Level level, BlockPos pos, Container container) {
        for(int slot = 0; slot < container.getContainerSize() - 1; ++slot) {
            Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), container.getItem(slot));
        };
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
        return side == Direction.DOWN ? new int[]{5} : new int[]{0, 1, 2, 3, 4};
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new BenchContainer(id, player, this);
    }

    @Override
    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.getBlockPos()) != this) {
            return false;
        } else if (this.level.getBlockState(this.getBlockPos()).getValue(CookingBench.FIRE)) {
            return true;
        } else {
            return this.TIME > 0 && this.stillValid(player);
        }
    }
}