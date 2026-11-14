package com.yuo.paimeng.tiles;

import com.yuo.paimeng.menu.SynPlatContainer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

//存储工作台数据
public class SynPlatTile extends BaseContainerBlockEntity {

    private NonNullList<ItemStack> items = NonNullList.withSize(4, ItemStack.EMPTY); //存储物品
    private final NonNullList<ItemStack> reslut = NonNullList.withSize(1, ItemStack.EMPTY); //存储合成物品

    public SynPlatTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.SYN_PLAT_TILE.get(), pos, state);
    }

    public SynPlatTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public int getContainerSize() {
        return 5;
    }


    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.items) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return this.reslut.get(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        if (index == 4) return reslut.get(0);
        else return this.items.get(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        if (index == 4) return ContainerHelper.removeItem(reslut, 0, count);
        else return ContainerHelper.removeItem(this.items, index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        if (index == 4) return ContainerHelper.takeItem(reslut, 0);
        return ContainerHelper.takeItem(this.items, index);
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

    //可由玩家使用吗
    @Override
    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.getBlockPos()) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.getBlockPos().getX() + 0.5D, (double)this.getBlockPos().getY() + 0.5D, (double)this.getBlockPos().getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clearContent() {
        this.items.clear();
        this.reslut.clear();
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
    protected Component getDefaultName() {
        return Component.translatable("gui.paimeng.synthetic_platform");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new SynPlatContainer(id, inventory, this);
    }
}
