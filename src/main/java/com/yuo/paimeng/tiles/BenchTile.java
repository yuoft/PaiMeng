package com.yuo.paimeng.tiles;

import com.yuo.paimeng.blocks.CookingBench;
import com.yuo.paimeng.menu.BenchContainer;
import com.yuo.paimeng.menu.CookingIntArray;
import com.yuo.paimeng.menu.FoodRecipesIntArray;
import com.yuo.paimeng.items.Food.PaiMengFood;
import com.yuo.paimeng.items.PMFoodItem;
import com.yuo.paimeng.recipe.ModRecipeType;
import com.yuo.paimeng.savedata.FoodLevelWorldSaveData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public class BenchTile extends BaseContainerBlockEntity implements WorldlyContainer {
    public NonNullList<ItemStack> items = NonNullList.withSize(7, ItemStack.EMPTY); //物品栏
    public final int MAX_TIME = 20 * 60 * 16; //最大燃烧时间16分钟
    private int TIME; //剩余燃烧时间
    private boolean FLAG; //是否可以合成
    private int LEVEL; //配方等级
    private Item fuelItem; //燃料
    public final CookingIntArray data = new CookingIntArray();
    public final FoodRecipesIntArray foodData = new FoodRecipesIntArray(); //玩家烹饪数据
    public Player player;

    //1.20.1 BlockEntityType.Builder.of(BenchTile::new, block) 使用的构造
    public BenchTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.BENCH_TILE.get(), pos, state);
    }

    //1.20.1 标准构造
    public BenchTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //旧版无参构造（Container / 客户端预览仍在用）
    public BenchTile() {
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

        //同步玩家烹饪经验数据
        FoodLevelWorldSaveData saveData = FoodLevelWorldSaveData.get(world);
        if (player != null){
            FoodLevelWorldSaveData.PlayerFoodRecipesInfo info = saveData.getInfo(player.getName().getString());
            if (!info.isEmpty()){
                this.foodData.set(0, info.getLevel() );
                this.foodData.set(1, info.getExp());
            }
        }

        boolean burning = this.isBurning();
        BlockState state = world.getBlockState(pos); //当前方块
        if (!state.getValue(CookingBench.FIRE) && burning){ //点燃
            world.setBlockAndUpdate(pos, state.setValue(CookingBench.FIRE, true));
        }
        if(!state.getValue(CookingBench.FIRE)) return;
        TIME--;//燃烧时时间减少
        this.data.set(0, TIME);
        if (TIME < 0) {
            TIME = 0;
            this.data.set(0, TIME); //燃烧时间为0时，熄灭
            world.levelEvent(null, 1009, pos, 0);
            world.setBlockAndUpdate(pos, state.setValue(CookingBench.FIRE, false));
        }

        ItemStack output = TileUtils.getRecipeOutput(world, ModRecipeType.BENCH, this);
        if (!output.isEmpty() && output.getItem() instanceof PaiMengFood){
            LEVEL = ((PaiMengFood) output.getItem()).getLEVEL();
            this.data.set(2, LEVEL);
        }
        if (!output.isEmpty() && (this.items.get(5).getItem() == output.getItem() || this.items.get(5).isEmpty())){
            //有输出 产物栏无物品或物品相同 则能合成
            FLAG = true;
            this.data.set(1, 1);
            setChanged();
        }else {
            FLAG = false;
            this.data.set(1, 0);
        }

        if (burning != this.isBurning()){
            world.setBlockAndUpdate(pos, state.setValue(CookingBench.FIRE, this.isBurning()));
        }
    }

    //设置玩家
    public void setPlayer(Player player){
        this.player = player;
    }

    //增加燃烧时间
    public void setBurnTime(int time){
        TIME = Math.min(TIME + time, MAX_TIME);
//        level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(CookingBench.FIRE, true));
        this.data.set(0, TIME);
        setChanged();
    }

    public void setFuelItem(Item item){
        this.fuelItem = item;
        this.items.set(6, new ItemStack(item));
        setChanged();
    }

    public Item getFuelItem() {
        return fuelItem;
    }

    public int getTIME() {
        return TIME;
    }

    private boolean isBurning(){
        return TIME > 0;
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        readData(nbt);
    }

    private void readData(CompoundTag nbt){
        this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.items);
        this.TIME = nbt.getInt("time");
        this.FLAG = nbt.getBoolean("flag");
        this.LEVEL = nbt.getInt("level");
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        writeData(compound);
    }

    private CompoundTag writeData(CompoundTag nbt){
        nbt.putInt("time", this.TIME);
        nbt.putBoolean("flag", this.FLAG);
        nbt.putInt("level", this.LEVEL);
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
        return Component.translatable("gui.paimeng.bench");
    }

    //打开gui
    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory player) {
        return new BenchContainer(id, player, this);
    }

    @Override
    public int getContainerSize() {
        return this.items.size();
    }

    //1.16.5 旧名（其它类仍在调用）
    public int getSizeInventory() {
        return this.getContainerSize();
    }

    public int getInputSize(){
        int i = 0;
        for (ItemStack item : items.subList(0, 5)) {
            if (!item.isEmpty()) i++;
        }
        return i;
    }

    @Override
    public boolean isEmpty() {
        for (ItemStack stack : this.items.subList(0, 6)) {
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
        }else if (this.level.getBlockState(this.getBlockPos()).getValue(CookingBench.FIRE)){
            return true;
        }  else {
            return this.TIME > 0 && player.distanceToSqr((double)this.getBlockPos().getX() + 0.5D, (double)this.getBlockPos().getY() + 0.5D, (double)this.getBlockPos().getZ() + 0.5D) <= 64.0D;
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

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? new int[]{5} : new int[]{0,1,2,3,4};
    }

    @Override
    public boolean canPlaceItemThroughFace(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return direction != Direction.DOWN && index < 5 && itemStackIn.getItem() instanceof PMFoodItem;
    }

    //1.16.5 旧名
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return this.canPlaceItemThroughFace(index, itemStackIn, direction);
    }

    @Override
    public boolean canTakeItemThroughFace(int index, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && index == 5;
    }

    //1.16.5 旧名
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return this.canTakeItemThroughFace(index, stack, direction);
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
