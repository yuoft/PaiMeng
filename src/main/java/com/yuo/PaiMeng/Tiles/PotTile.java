package com.yuo.PaiMeng.Tiles;

import com.yuo.PaiMeng.Blocks.CookingPot;
import com.yuo.PaiMeng.Container.FoodRecipesIntArray;
import com.yuo.PaiMeng.Container.PotContainer;
import com.yuo.PaiMeng.Container.CookingIntArray;
import com.yuo.PaiMeng.Items.Food.PaiMengFood;
import com.yuo.PaiMeng.Items.PMFoodItem;
import com.yuo.PaiMeng.Recipes.ModRecipeType;
import com.yuo.PaiMeng.WorldData.FoodLevelWorldSaveData;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

public class PotTile extends LockableTileEntity implements ITickableTileEntity, ISidedInventory {
    public NonNullList<ItemStack> items = NonNullList.withSize(5, ItemStack.EMPTY); //物品栏
    private final int[] FIRE_TIME = {0, 20 * 90, 20 * 90 * 2, 20 * 90 * 3, 20 * 90 * 4}; //总燃烧时间 每个燃料时间为一分半钟
    private int TIME; //已燃烧时间
    private boolean FLAG; //是否可以合成
    private int LEVEL; //配方等级
    public final CookingIntArray data = new CookingIntArray();
    public final FoodRecipesIntArray foodData = new FoodRecipesIntArray();
    public PlayerEntity player;

    public PotTile() {
        super(TileTypeRegistry.POT_TILE.get());
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        //同步玩家烹饪经验数据
        FoodLevelWorldSaveData saveData = FoodLevelWorldSaveData.get(world);
        if (player != null){
            FoodLevelWorldSaveData.PlayerFoodRecipesInfo info = saveData.getInfo(player.getName().getString());
            if (!info.isEmpty()){
                this.foodData.set(0, info.getLevel() );
                this.foodData.set(1, info.getExp());
            }
        }

        BlockState state = world.getBlockState(pos); //当前方块
        if (world.isRainingAt(pos.up()) && state.get(CookingPot.FIRE)){ //下雨熄灭
            world.playEvent(null, 1009, pos, 0);
            world.setBlockState(pos, state.with(CookingPot.FIRE, Boolean.FALSE));
        }
        if (state.get(CookingPot.FIRE)) { //燃烧时时间减少
            TIME--;
            if (TIME < 0) TIME = 0;
            this.data.set(0, TIME);
        }
        int fuel = state.get(CookingPot.FUEL);
        if (TIME <= FIRE_TIME[Math.max(fuel - 1, 0)]){ //到达燃烧时间
            world.setBlockState(pos, state.with(CookingPot.FUEL, Math.max(fuel - 1, 0))); //减少燃料
        }
        if (fuel == 0) world.setBlockState(pos, state.with(CookingPot.FIRE, false)); //燃料消耗完 火焰熄灭
        ItemStack output = TileUtils.getRecipeOutput(world, ModRecipeType.POT, this);
        if (!output.isEmpty() && output.getItem() instanceof PaiMengFood){
            LEVEL = ((PaiMengFood) output.getItem()).getLEVEL();
            this.data.set(2, LEVEL);
        }
        if (!output.isEmpty() && LEVEL <= 3 && (this.items.get(4).getItem() == output.getItem() || this.items.get(4).isEmpty())){
            //有输出 食物等级<=3 产物栏无物品或物品相同 则能合成
            FLAG = true;
            this.data.set(1, 1);
            markDirty();
        }else {
            FLAG = false;
            this.data.set(1, 0);
        }
    }
    //设置玩家
    public void setPlayer(PlayerEntity player){
        this.player = player;
    }
    //设置燃烧时间 每次时间增加一分钟
    public void setBurnTime(){
        TIME = Math.min(TIME + FIRE_TIME[1], FIRE_TIME[4]);
        this.data.set(0, TIME);
        markDirty();
    }

    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
        readData(state, nbt);
    }

    private void readData(BlockState state, CompoundNBT nbt){
        this.items = NonNullList.withSize(this.getSizeInventory(), ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(nbt, this.items);
        this.TIME = nbt.getInt("time");
        this.FLAG = nbt.getBoolean("flag");
        this.LEVEL = nbt.getInt("level");
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(writeData(compound));
    }

    private CompoundNBT writeData(CompoundNBT nbt){
        nbt.putInt("time", this.TIME);
        nbt.putBoolean("flag", this.FLAG);
        nbt.putInt("level", this.LEVEL);
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
        readData(state, tag);
    }

    //gui名称
    @Override
    protected ITextComponent getDefaultName() {
        return new TranslationTextComponent("gui.paimeng.pot");
    }

    //打开gui
    @Override
    protected Container createMenu(int id, PlayerInventory player) {
        return new PotContainer(id, player, this);
    }

    @Override
    public int getSizeInventory() {
        return this.items.size();
    }

    //获取有效输入数量
    public int getInputSize(){
        int i = 0;
        for (ItemStack item : items.subList(0, 4)) {
            if (!item.isEmpty()) i++;
        }
        return i;
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
        }else if (this.world.getBlockState(this.pos).get(CookingPot.FIRE)){
            return true;
        } else {
            return this.TIME > 0 && player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
    }

    @Override
    public void clear() {
        this.items.clear();
    }

    @Override
    public int[] getSlotsForFace(Direction side) {
        return side == Direction.DOWN ? new int[]{4} : new int[]{0,1,2,3};
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStackIn, @Nullable Direction direction) {
        return direction != Direction.DOWN && index < 4 && itemStackIn.getItem() instanceof PMFoodItem;
    }

    @Override
    public boolean canExtractItem(int index, ItemStack stack, Direction direction) {
        return direction == Direction.DOWN && index == 4;
    }

    @Nullable
    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
        if (!this.removed && side != null && cap == net.minecraftforge.items.CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            if (side == Direction.DOWN)
                return SidedInvWrapper.create(this, Direction.DOWN)[0].cast();
            else
                return SidedInvWrapper.create(this, Direction.UP, Direction.WEST, Direction.EAST, Direction.SOUTH, Direction.NORTH)[0].cast();
        }
        return super.getCapability(cap, side);
    }
}
