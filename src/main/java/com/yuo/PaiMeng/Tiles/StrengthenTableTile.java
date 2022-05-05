package com.yuo.PaiMeng.Tiles;

import com.yuo.PaiMeng.Container.StrengthenIntArray;
import com.yuo.PaiMeng.Container.StrengthenTableContainer;
import com.yuo.PaiMeng.Items.RelicsHelper;
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

public class StrengthenTableTile extends LockableTileEntity implements ITickableTileEntity {
    public NonNullList<ItemStack> items = NonNullList.withSize(17, ItemStack.EMPTY); //物品栏
    public StrengthenIntArray data = new StrengthenIntArray();

    public StrengthenTableTile() {
        super(TileTypeRegistry.strengthenTableTile.get());
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        data.set(0, isEmptyAsInput() ? 0 : 1);
        ItemStack relics = items.get(16);
        data.set(1, relics.isEmpty() ? 0 : 1);
        if (data.get(0) == 0 && data.get(1) == 0){ //无输入无输出 清空经验数据
            data.set(2, 0);
            data.set(3, 0);
            data.set(4, 0);
            markDirty();
        }
        if (data.get(0) == 0){ //无输入 无强化经验
            data.set(2, 0);
            markDirty();
        }
        int star = RelicsHelper.getStarFormStack(relics);
        int level = RelicsHelper.getRelicsLevel(relics);
        int exp = RelicsHelper.getRelicsExp(relics);
        if (data.get(1) == 1){
            data.set(3, RelicsHelper.getUpExpForLevel(star, level) );
            data.set(2, exp);
            markDirty();
        }
        if (data.get(0) == 1 && data.get(1) == 1){ //有物品时 显示强化预览
            int relicsExp = RelicsHelper.getListRelicsExp(getRelicsExpItem());
            data.set(2, relicsExp + exp);
            int lv = RelicsHelper.getRelicsInfoInLevelUp(star, (int) Math.ceil(relicsExp * 0.8) + exp, level)[0];
            if (lv + level >= star * 4){
                data.set(4, -1);
            }else data.set(4, lv);
            markDirty();
        }
    }

    /**
     * 获取作为经验素材的物品列表
     * @return 物品列表
     */
    public NonNullList<ItemStack> getRelicsExpItem(){
        NonNullList<ItemStack> list = NonNullList.create();
        for (int i = 0; i < items.size() - 1; i++){
            if (!items.get(i).isEmpty()){
                list.add(items.get(i));
            }
        }
        return list;
    }

    /**
     * 判断输入是否是空
     * @return 空:true
     */
    private boolean isEmptyAsInput(){
        for (int i = 0; i < items.size() - 1; i++){
            if (!items.get(i).isEmpty()) return false;
        }
        return true;
    }

    /**
     * 消耗素材
     */
    public void shrinkItem(){
        for (int i = 0; i < items.size() - 1; i++){
            if (!items.get(i).isEmpty()){
                items.set(i,ItemStack.EMPTY);
            }
        }
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        readData(nbt);
    }

    private void readData(CompoundNBT nbt){
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(writeData(compound));
    }

    private CompoundNBT writeData(CompoundNBT nbt){
        ItemStackHelper.saveAllItems(nbt, this.items);
        return nbt;
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.pos = pkt.getPos();
        handleUpdateTag(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
    }

    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT compound = super.getUpdateTag();
        return writeData(compound);
    }

    @Override
    public void handleUpdateTag(BlockState state, CompoundNBT tag) {
        readData(tag);
    }

    //gui名称
    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("gui.paimeng.strengthen_table");
    }

    //打开gui
    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new StrengthenTableContainer(id, player, this);
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
        }  else {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        this.items.clear();
    }
}
