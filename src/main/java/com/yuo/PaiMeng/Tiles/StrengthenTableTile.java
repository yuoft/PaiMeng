package com.yuo.PaiMeng.Tiles;

import com.yuo.PaiMeng.Container.StrengthenIntArray;
import com.yuo.PaiMeng.Container.StrengthenTableContainer;
import com.yuo.PaiMeng.Items.RelicsHelper;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class StrengthenTableTile extends BaseContainerBlockEntity {
    public NonNullList<ItemStack> items = NonNullList.withSize(17, ItemStack.EMPTY); //物品栏
    public StrengthenIntArray data = new StrengthenIntArray();

    //1.20.1 BlockEntityType.Builder.of(StrengthenTableTile::new, block) 使用的构造
    public StrengthenTableTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.strengthenTableTile.get(), pos, state);
    }

    //1.20.1 标准构造
    public StrengthenTableTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //旧版无参构造（Container / 客户端预览仍在用）
    public StrengthenTableTile() {
        this(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
    }

    /**
     * 1.20.1 方块侧的 getTicker 调用入口
     */
    public void tick(Level level, BlockPos pos, BlockState state) {
        tickInternal(level, pos);
    }

    /**
     * 兼容旧调用
     */
    public void tick() {
        if (this.level == null) return;
        tickInternal(this.level, this.getBlockPos());
    }

    private void tickInternal(Level world, BlockPos pos) {
        if (world.isClientSide) return;

        data.set(0, isEmptyAsInput() ? 0 : 1);
        ItemStack relics = items.get(16);
        data.set(1, relics.isEmpty() ? 0 : 1);
        if (data.get(0) == 0 && data.get(1) == 0){ //无输入无输出 清空经验数据
            data.set(2, 0);
            data.set(3, 0);
            data.set(4, 0);
            setChanged();
        }
        if (data.get(0) == 0){ //无输入 无强化经验
            data.set(2, 0);
            setChanged();
        }
        int star = RelicsHelper.getStarFormStack(relics);
        int level = RelicsHelper.getRelicsLevel(relics);
        int exp = RelicsHelper.getRelicsExp(relics);
        if (data.get(1) == 1){
            data.set(3, RelicsHelper.getUpExpForLevel(star, level) );
            data.set(2, exp);
            setChanged();
        }
        if (data.get(0) == 1 && data.get(1) == 1){ //有物品时 显示强化预览
            int relicsExp = RelicsHelper.getListRelicsExp(getRelicsExpItem());
            data.set(2, relicsExp + exp);
            int lv = RelicsHelper.getRelicsInfoInLevelUp(star, (int) Math.ceil(relicsExp * 0.8) + exp, level)[0];
            if (lv + level >= star * 4){
                data.set(4, -1);
            }else data.set(4, lv);
            setChanged();
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
    public void load(CompoundTag nbt) {
        super.load(nbt);
        readData(nbt);
    }

    private void readData(CompoundTag nbt){
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        writeData(compound);
    }

    private CompoundTag writeData(CompoundTag nbt){
        ContainerHelper.saveAllItems(nbt, this.items, true);
        return nbt;
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag compound = super.getUpdateTag();
        return writeData(compound);
    }

    @Override
    public void handleUpdateTag(CompoundTag tag) {
        readData(tag);
    }

    //gui名称
    @Override
    protected Component getDefaultName() {
        return Component.translatable("gui.paimeng.strengthen_table");
    }

    //打开gui
    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new StrengthenTableContainer(id, player, this);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    //1.16.5 旧名（其它类仍在调用）
    public int getSizeInventory() {
        return this.getContainerSize();
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items.subList(0, 5)) {
            if (!stack.isEmpty()) return false;
        }
        return true;
    }

    @Override
    public ItemStack getItem(int index) {
        return this.items.get(index);
    }

    //1.16.5 旧名
    public ItemStack getStackInSlot(int index) {
        return this.getItem(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        return ContainerHelper.removeItem(this.items, index, count);
    }

    //1.16.5 旧名
    public ItemStack decrStackSize(int index, int count) {
        return this.removeItem(index, count);
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return ContainerHelper.takeItem(this.items, index);
    }

    //1.16.5 旧名
    public ItemStack removeStackFromSlot(int index) {
        return this.removeItemNoUpdate(index);
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

    //1.16.5 旧名
    public void setInventorySlotContents(int index, ItemStack stack) {
        this.setItem(index, stack);
    }

    //1.16.5 旧名
    public int getInventoryStackLimit() {
        return this.getMaxStackSize();
    }

    //能否使用
    @Override
    public boolean stillValid(Player player) {
        if (this.level == null || this.level.getBlockEntity(this.getBlockPos()) != this) {
            return false;
        }  else {
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
    }

    //1.16.5 旧名
    public void clear() {
        this.clearContent();
    }
}
