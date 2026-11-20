package com.yuo.paimeng.tiles;

import com.yuo.paimeng.items.Food.PaiMengFood;
import com.yuo.paimeng.items.PMFoodItem;
import com.yuo.paimeng.menu.CookingIntArray;
import com.yuo.paimeng.menu.FoodRecipesIntArray;
import com.yuo.paimeng.world.data.FoodRecipeSaveData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public abstract class AbstractCookingTile extends BaseContainerBlockEntity implements WorldlyContainer {
    public NonNullList<ItemStack> items;
    public final CookingIntArray data = new CookingIntArray(); //燃烧数据
    public final FoodRecipesIntArray foodData = new FoodRecipesIntArray(); //烹饪数据
    public Player player;
    
    protected boolean FLAG; // 是否可以合成
    protected int LEVEL; // 配方等级

    protected AbstractCookingTile(BlockEntityType<?> type, BlockPos pos, BlockState state, int size) {
        super(type, pos, state);
        this.items = NonNullList.withSize(size, ItemStack.EMPTY);
    }

    /**
     * 获取有效输入数量
     */
    public int getInputSize(){
        int i = 0;
        for (ItemStack item : items.subList(0, 5)) {
            if (!item.isEmpty()) i++;
        }
        return i;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * 同步玩家烹饪经验数据
     */
    protected void syncPlayerData(Level world) {
        if (world.isClientSide) return;
        FoodRecipeSaveData saveData = FoodRecipeSaveData.get(world);
        if (player != null) {
            FoodRecipeSaveData.PlayerFoodRecipesInfo info = saveData.getInfo(player.getUUID());
            if (!info.isEmpty()) {
                this.foodData.set(0, info.getLevel());
                this.foodData.set(1, info.getExp());
            }
        }
    }

    /**
     * 检查配方并设置 FLAG
     */
    protected void checkRecipe(Level world) {
        ItemStack output = TileUtils.getRecipeOutput(world, getRecipeType(), this);
        if (!output.isEmpty() && output.getItem() instanceof PaiMengFood food) {
            LEVEL = food.getFOOD_LEVEL();
            this.data.set(2, LEVEL);
        }
        
        // 有输出 产物栏无物品或物品相同 则能合成
        if (!output.isEmpty() && LEVEL <= getMaxRecipeLevel()
                && (this.items.get(getOutputSlot()).getItem() == output.getItem() || this.items.get(getOutputSlot()).isEmpty())) {
            FLAG = true;
            this.data.set(1, 1);
        } else {
            FLAG = false;
            this.data.set(1, 0);
        }
    }

    /**
     * 获取配方类型
     */
    protected abstract RecipeType<?> getRecipeType();

    /**
     * 获取允许的最高配方等级(比如锅是3，工作台不限)
     */
    protected abstract int getMaxRecipeLevel();

    /**
     * 获取输出槽位索引
     */
    protected abstract int getOutputSlot();

    /**
     * 具体的 Tick 逻辑
     */
    public abstract void tickInternal(Level world, BlockPos pos, BlockState state);

    // --- NBT 读写 ---
    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items);
        this.FLAG = nbt.getBoolean("flag");
        this.LEVEL = nbt.getInt("level");
        readAdditional(nbt);
    }

    // 子类用来读写特有数据（如 TIME）
    protected abstract void readAdditional(CompoundTag nbt);

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        compound.putBoolean("flag", this.FLAG);
        compound.putInt("level", this.LEVEL);
        ContainerHelper.saveAllItems(compound, this.items, true);
        writeAdditional(compound);
    }

    // 子类用来读写特有数据（如 TIME）
    protected abstract void writeAdditional(CompoundTag compound);

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compound = super.getUpdateTag();
        compound.putBoolean("flag", this.FLAG);
        compound.putInt("level", this.LEVEL);
        ContainerHelper.saveAllItems(compound, this.items, true);
        writeAdditional(compound);
        return compound;
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        this.FLAG = tag.getBoolean("flag");
        this.LEVEL = tag.getInt("level");
        ContainerHelper.loadAllItems(tag, this.items);
        readAdditional(tag);
    }

    // --- 容器交互 ---

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getOutputSlot(); i++) {
            if (!this.items.get(i).isEmpty()) return false;
        }
        return this.items.get(getOutputSlot()).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    @Override
    public void setItem(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
        if (!flag) {
            this.setChanged();
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
    }

    // --- 物品插入/提取限制 ---
    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return direction != Direction.DOWN && index < getOutputSlot() && itemStackIn.getItem() instanceof PMFoodItem;
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && index == getOutputSlot();
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.isRemoved() && side != null && cap == ForgeCapabilities.ITEM_HANDLER) {
            if (side == Direction.DOWN)
                return SidedInvWrapper.create(this, Direction.DOWN)[0].cast();
            else
                return SidedInvWrapper.create(this, Direction.UP, Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH)[0].cast();
        }
        return super.getCapability(cap, side);
    }
}