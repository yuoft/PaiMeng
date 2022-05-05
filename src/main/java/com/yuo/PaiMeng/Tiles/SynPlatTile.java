package com.yuo.PaiMeng.Tiles;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nullable;

//存储工作台数据
public class SynPlatTile extends TileEntity implements IInventory, INameable {

    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY); //存储物品
    private NonNullList<ItemStack> reslut = NonNullList.withSize(1, ItemStack.EMPTY); //存储合成物品

    public SynPlatTile() {
        super(TileTypeRegistry.SYN_PLAT_TILE.get());
    }

    @Override
    public int getSizeInventory() {
        return 5;
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        if (!this.reslut.get(0).isEmpty()) return false;
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int index) {
        if (index == 4) return reslut.get(0);
        else return this.items.get(index);
    }

    @Override
    public ItemStack decrStackSize(int index, int count) {
        if (index == 4) return ItemStackHelper.getAndSplit(reslut, index, count);
        else return ItemStackHelper.getAndSplit(this.items, index, count);
    }

    @Override
    public ItemStack removeStackFromSlot(int index) {
        if (index == 4) return ItemStackHelper.getAndRemove(reslut, index);
        return ItemStackHelper.getAndRemove(this.items, index);
    }

    //将给定项目设置为容器中的制定槽位
    @Override
    public void setInventorySlotContents(int index, ItemStack stack) {
        if (index == 4) reslut.set(0, stack);
        else {
            ItemStack itemstack = this.items.get(index);
            boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack); //相同物品
            this.items.set(index, stack);
            if (stack.getCount() > this.getInventoryStackLimit()) {
                stack.setCount(this.getInventoryStackLimit());
            }

            if (!flag) {
                this.markDirty();
            }
        }
    }

    //可由玩家使用吗
    @Override
    public boolean isUsableByPlayer(PlayerEntity player) {
        if (this.world.getTileEntity(this.pos) != this) {
            return false;
        } else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        this.items.clear();
        this.reslut.clear();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        this.items = NonNullList.withSize(this.getSizeInventory() - 1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
        CompoundNBT resultNbt = (CompoundNBT) nbt.get("Result");
        this.reslut.set(0, ItemStack.read(resultNbt));
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ItemStackHelper.saveAllItems(compound, this.items);
        CompoundNBT nbt = new CompoundNBT();
        this.reslut.get(0).write(nbt);
        compound.put("Result", nbt);
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
        ItemStackHelper.saveAllItems(compound, this.items);
        CompoundNBT nbt = new CompoundNBT();
        this.reslut.get(0).write(nbt);
        compound.put("Result", nbt);
        return compound;
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        this.items = NonNullList.withSize(this.getSizeInventory() - 1, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(tag, this.items);
        CompoundNBT resultNbt = (CompoundNBT) tag.get("Result");
        this.reslut.set(0, ItemStack.read(resultNbt));
    }

    @Override
    public ITextComponent getName() {
        return new TranslationTextComponent("gui.paimeng.synthetic_platform");
    }
}