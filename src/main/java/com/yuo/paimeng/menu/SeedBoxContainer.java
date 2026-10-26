package com.yuo.paimeng.menu;

import com.yuo.paimeng.items.PMFoodItem;
import com.yuo.paimeng.recipe.ModRecipeType;
import com.yuo.paimeng.recipe.SeedBoxRecipe;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.Optional;

//1.20.1: RecipeBookContainer<CraftingInventory> -> RecipeBookMenu<CraftingContainer>
public class SeedBoxContainer extends RecipeBookMenu<CraftingContainer> {
    private final CraftingContainer inputInventory;
    private final ResultContainer outputInventory;
    private final Level world;
    private final Player player;

    public SeedBoxContainer(int id, Inventory playerInventory) {
        super(ContainerTypeRegistry.seedBoxContainer.get(), id);
        this.inputInventory = new TransientCraftingContainer(this,1,1);
        this.outputInventory = new ResultContainer();
        this.player = playerInventory.player;
        this.world = playerInventory.player.level();
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

        slotsChanged(inputInventory);
    }

    //输入改变时设置输出 1.20.1: onCraftMatrixChanged -> slotsChanged
    @Override
    public void slotsChanged(Container matrix) {
        if (this.world.isClientSide) return;
        ServerPlayer serverPlayer = (ServerPlayer)player;
        ItemStack itemstack = ItemStack.EMPTY;
        //获取配方
        Optional<SeedBoxRecipe> optional = this.world.getRecipeManager().getRecipeFor(ModRecipeType.SEED_BOX, matrix, this.world);
        if (optional.isPresent()) {
            SeedBoxRecipe recipe = optional.get();
            //1.20.1: canUseRecipe -> RecipeHolder#setRecipeUsed(Level, ServerPlayer, Recipe)
            if (this.outputInventory.setRecipeUsed(this.world, serverPlayer, recipe)) {
                itemstack = recipe.getCraftingResult(matrix); //获取配方输出
            }
        }
        this.outputInventory.setItem(1, itemstack); //设置产出
        this.setRemoteSlot(1, itemstack);
        //发包同步数据
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 1, itemstack));
    }

    //在打开gui时不能选中容器物品 1.20.1: slotClick -> clicked（返回 void）
    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if(slotId == (this.slots.size() - 1) - (8 - player.getInventory().selected)) {
            return;
        }
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    //1.20.1: canInteractWith -> stillValid
    @Override
    public boolean stillValid(Player playerIn) {
        return this.inputInventory.stillValid(playerIn);
    }

    //玩家shift行为 1.20.1: transferStackInSlot -> quickMoveStack
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (index == 1){
                if (!this.moveItemStackTo(itemStack1, 2, 38, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(itemStack1, itemstack); //1.20.1: onSlotChange -> onQuickCraft
            }
            else if (index > 1){
                if (itemStack1.getItem() instanceof PMFoodItem) //包含在配方中
                    if (!this.moveItemStackTo(itemStack1, 0, 1, false)) return ItemStack.EMPTY;
                if (index < 29) { //从物品栏到快捷栏
                    if (!this.moveItemStackTo(itemStack1, 30, 38, false)) return ItemStack.EMPTY;
                } else if (index < 38) {
                    if (!this.moveItemStackTo(itemStack1, 2, 29, false)) return ItemStack.EMPTY;
                }
            }else if (!this.moveItemStackTo(itemStack1, 2 ,38, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }

    //1.20.1: fillStackedContents -> fillCraftSlotsStackedContents
    @Override
    public void fillCraftSlotsStackedContents(StackedContents itemHelperIn) {
        this.inputInventory.fillStackedContents(itemHelperIn);
    }

    //1.20.1: clear -> clearCraftingContent
    @Override
    public void clearCraftingContent() {
        this.inputInventory.clearContent();
        this.outputInventory.clearContent();
    }

    //1.20.1: matches -> recipeMatches
    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipeIn) {
        return recipeIn.matches(this.inputInventory, this.player.level());
    }

    //1.20.1: getOutputSlot -> getResultSlotIndex
    @Override
    public int getResultSlotIndex() {
        return 1;
    }

    //1.20.1: getWidth -> getGridWidth
    @Override
    public int getGridWidth() {
        return 1;
    }

    //1.20.1: getHeight -> getGridHeight
    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 2;
    }

    //1.20.1: func_241850_m -> getRecipeBookType
    @Override
    public RecipeBookType getRecipeBookType() {
        return null;
    }

    @Override
    public boolean shouldMoveToInventory(int index) {
        return index != this.getResultSlotIndex();
    }

    //1.20.1: onContainerClosed -> removed
    @Override
    public void removed(Player playerIn) {
        ItemStack stack = this.inputInventory.getItem(0);
        if (!stack.isEmpty()){
            this.clearContainer(playerIn, this.inputInventory);
        }
        super.removed(playerIn);
    }
}
