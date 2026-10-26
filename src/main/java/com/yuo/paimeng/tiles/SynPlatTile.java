package com.yuo.paimeng.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

//存储工作台数据
public class SynPlatTile extends BlockEntity implements Container, Nameable {

    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY); //存储物品
    private NonNullList<ItemStack> reslut = NonNullList.withSize(1, ItemStack.EMPTY); //存储合成物品

    //1.20.1 BlockEntityType.Builder.of(SynPlatTile::new, block) 使用的构造
    public SynPlatTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.SYN_PLAT_TILE.get(), pos, state);
    }

    //1.20.1 标准构造
    public SynPlatTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //旧版无参构造（Container / 客户端预览仍在用）
    public SynPlatTile() {
        this(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
    }

    @Override
    public int getContainerSize() {
        return 5;
    }

    //1.16.5 旧名（其它类仍在调用）
    public int getSizeInventory() {
        return this.getContainerSize();
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
    public ItemStack getItem(int index) {
        if (index == 4) return reslut.get(0);
        else return this.items.get(index);
    }

    //1.16.5 旧名
    public ItemStack getStackInSlot(int index) {
        return this.getItem(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (index == 4) return ContainerHelper.removeItem(reslut, 0, count);
        else return ContainerHelper.removeItem(this.items, index, count);
    }

    //1.16.5 旧名
    public ItemStack decrStackSize(int index, int count) {
        return this.removeItem(index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index == 4) return ContainerHelper.takeItem(reslut, 0);
        return ContainerHelper.takeItem(this.items, index);
    }

    //1.16.5 旧名
    public ItemStack removeStackFromSlot(int index) {
        return this.removeItemNoUpdate(index);
    }

    //将给定项目设置为容器中的制定槽位
    @Override
    public void setItem(int index, ItemStack stack) {
        if (index == 4) reslut.set(0, stack);
        else {
            ItemStack itemstack = this.items.get(index);
            boolean flag = !stack.isEmpty() && ItemStack.isSameItemSameTags(stack, itemstack); //相同物品
            this.items.set(index, stack);
            if (stack.getCount() > this.getMaxStackSize()) {
                stack.setCount(this.getMaxStackSize());
            }

            if (!flag) {
                this.setChanged();
            }
        }
    }

    //1.16.5 旧名
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.setItem(index, stack);
    }

    //1.16.5 旧名
    public int getInventoryStackLimit() {
        return this.getMaxStackSize();
    }

    //可由玩家使用吗
    @Override
    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.getBlockPos()) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.getBlockPos().getX() + 0.5D, (double)this.getBlockPos().getY() + 0.5D, (double)this.getBlockPos().getZ() + 0.5D) <= 64.0D;
        }
    }

    //1.16.5 旧名
    public boolean isUsableByPlayer(Player player) {
        return this.stillValid(player);
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.reslut.clear();
    }

    //1.16.5 旧名
    public void clear() {
        this.clearContent();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        this.items = NonNullList.withSize(this.getContainerSize() - 1, ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items);
        this.reslut.set(0, ItemStack.of(nbt.getCompound("Result")));
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        ContainerHelper.saveAllItems(compound, this.items, true);
        CompoundTag nbt = new CompoundTag();
        this.reslut.get(0).save(nbt);
        compound.put("Result", nbt);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compound = super.getUpdateTag();
        ContainerHelper.saveAllItems(compound, this.items, true);
        CompoundTag nbt = new CompoundTag();
        this.reslut.get(0).save(nbt);
        compound.put("Result", nbt);
        return compound;
    }

    @Override
    public Component getName() {
        return Component.translatable("gui.paimeng.synthetic_platform");
    }
}
