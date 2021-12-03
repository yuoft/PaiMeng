package com.yuo.PaiMeng.Tiles;

import com.yuo.PaiMeng.Blocks.CookingPot;
import com.yuo.PaiMeng.Gui.PotContainer;
import com.yuo.PaiMeng.Gui.PotIntArray;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

public class PotTile extends LockableTileEntity implements ITickableTileEntity {
    private NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY); //物品栏
    private final int FIRE_TIME[] = {0, 20 * 60 * 2, 20 * 60 * 4, 20 * 60 * 6, 20 * 60 * 8}; //总燃烧时间 每个燃料时间为两分钟
    private int TIME; //已燃烧时间
    private final PotIntArray data = new PotIntArray();

    public PotTile() {
        super(TileTypeRegistry.POT_TILE.get());
    }

    @Override
    public void tick() {
        if (world.isRemote || world == null) return;
        TIME--;
        if (TIME < 0) TIME = 0;
        this.data.set(0, TIME);
        BlockState state = world.getBlockState(pos); //当前方块
        Integer fuel = state.get(CookingPot.FUEL);
        if (TIME <= FIRE_TIME[fuel - 1 < 0 ? 0:fuel - 1]){ //到达燃烧时间
            world.setBlockState(pos, state.with(CookingPot.FUEL, fuel - 1 < 0 ? 0:fuel - 1)); //减少燃料
        }
        if (fuel == 0) world.setBlockState(pos, state.with(CookingPot.FIRE, false)); //燃料消耗完 火焰熄灭

        markDirty();
    }

    //设置燃烧时间
    public void setBurnTime(int fuel){
        TIME = FIRE_TIME[fuel];
        this.data.set(0, TIME);
        markDirty();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
        this.TIME = nbt.getInt("time");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("time", this.TIME);
        ItemStackHelper.saveAllItems(compound, this.items);
        return super.write(compound);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        handleUpdateTag(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compound = super.getUpdateTag();
        compound.putInt("time", this.TIME);
        ItemStackHelper.saveAllItems(compound, this.items);
        return compound;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tag, this.items);
        this.TIME = tag.getInt("time");
    }

    //gui名称
    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("gui.paimeng.pot");
    }

    //打开gui
    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new PotContainer(id, player, this, this.data);
    }

    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items.subList(0, 5)) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        return this.items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        ItemStack itemstack = this.items.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.items.set(index, stack);
        if (stack.getCount() > this.getInventoryStackLimit()) {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (!flag) {
            this.markDirty();
        }
    }

    //能否使用
    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return this.TIME > 0 && player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        this.items.clear();
    }

}
