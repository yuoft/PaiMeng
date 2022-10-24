package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Items.PMFoods;
import com.yuo.PaiMeng.Recipes.ModRecipeType;
import com.yuo.PaiMeng.Recipes.SeedBoxRecipe;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.*;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;

import java.util.Optional;

public class SeedBoxContainer extends RecipeBookContainer<CraftingInventory> {
    private final CraftingInventory inputInventory;
    private final CraftResultInventory outputInventory;
    private final World world;
    private final PlayerEntity player;

    public SeedBoxContainer(int id, PlayerInventory playerInventory) {
        super(ContainerTypeRegistry.seedBoxContainer.get(), id);
        this.inputInventory = new CraftingInventory(this,1,1);
        this.outputInventory = new CraftResultInventory();
        this.player = playerInventory.player;
        this.world = playerInventory.player.world;
        //输入槽
        this.addSlot(new CookingSlot(inputInventory, 0, 51 , 35));
        //输出槽
        this.addSlot(new ModReslutSlot(ModRecipeType.SEED_BOX, player, inputInventory, outputInventory, 1, 109, 35));
        //添加玩家物品栏
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        //添加玩家快捷栏
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        onCraftMatrixChanged(inputInventory);
    }

    //输入改变时设置输出
    @Override
    public void onCraftMatrixChanged(IInventory matrix) {
        World world = player.world;
        if (world.isRemote) return;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
        ItemStack itemstack = ItemStack.EMPTY;
        //获取配方
        Optional<SeedBoxRecipe> optional = world.getRecipeManager().getRecipe(ModRecipeType.SEED_BOX, matrix, world);
        if (optional.isPresent()) {
            SeedBoxRecipe recipe = optional.get();
            if (outputInventory.canUseRecipe(world, serverPlayer, recipe)) {
                itemstack = recipe.getCraftingResult(matrix); //获取配方输出
            }
        }
        outputInventory.setInventorySlotContents(1, itemstack); //设置产出
        serverPlayer.connection.sendPacket(new SSetSlotPacket(windowId, 1, itemstack)); //发包同步数据
    }

    //在打开gui时不能选中容器物品
    @Override
    public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, PlayerEntity player) {
        if(slotId == (inventorySlots.size() - 1) - (8 - player.inventory.currentItem)) {
            return ItemStack.EMPTY;
        }
        return super.slotClick(slotId, dragType, clickTypeIn, player);
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.inputInventory.isUsableByPlayer(playerIn);
    }

    //玩家shift行为
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();
            if (index == 1){
                if (!this.mergeItemStack(itemStack1, 2, 38, true)) return ItemStack.EMPTY;
                slot.onSlotChange(itemStack1, itemstack);
            }
            else if (index > 1){
                if (itemStack1.getItem() instanceof PMFoods) //包含在配方中
                    if (!this.mergeItemStack(itemStack1, 0, 1, false)) return ItemStack.EMPTY;
                if (index < 29) { //从物品栏到快捷栏
                    if (!this.mergeItemStack(itemStack1, 30, 38, false)) return ItemStack.EMPTY;
                } else if (index < 38) {
                    if (!this.mergeItemStack(itemStack1, 2, 29, false)) return ItemStack.EMPTY;
                }
            }else if (!this.mergeItemStack(itemStack1, 2 ,38, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }

    @Override
    public void fillStackedContents(RecipeItemHelper itemHelperIn) {
        if (this.inputInventory instanceof IRecipeHelperPopulator) {
            ((IRecipeHelperPopulator)this.inputInventory).fillStackedContents(itemHelperIn);
        }
    }

    @Override
    public void clear() {
        this.inputInventory.clear();
        this.outputInventory.clear();
    }

    @Override
    public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
        return recipeIn.matches(this.inputInventory, this.player.world);
    }

    @Override
    public int getOutputSlot() {
        return 1;
    }

    @Override
    public int getWidth() {
        return 1;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 2;
    }

    @Override
    public RecipeBookCategory func_241850_m() {
        return null;
    }

    @Override
    public void onContainerClosed(PlayerEntity playerIn) {
        ItemStack stack = inputInventory.getStackInSlot(0);
        if (!stack.isEmpty()){
            clearContainer(playerIn, world, inputInventory);
        }
        super.onContainerClosed(playerIn);
    }

    protected void clearContainer(PlayerEntity playerIn, World worldIn, IInventory inventoryIn) {
        if (!playerIn.isAlive() || playerIn instanceof ServerPlayerEntity && ((ServerPlayerEntity)playerIn).hasDisconnected()) {
            for(int j = 0; j < inventoryIn.getSizeInventory(); ++j) {
                playerIn.dropItem(inventoryIn.removeStackFromSlot(j), false);
            }

        } else {
            for(int i = 0; i < inventoryIn.getSizeInventory(); ++i) {
                playerIn.inventory.placeItemBackInInventory(worldIn, inventoryIn.removeStackFromSlot(i));
            }

        }
    }
}
