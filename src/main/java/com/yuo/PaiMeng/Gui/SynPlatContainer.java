package com.yuo.PaiMeng.Gui;

import com.yuo.PaiMeng.Items.EvolutionDust;
import com.yuo.PaiMeng.Recipes.ModRecipeType;
import com.yuo.PaiMeng.Recipes.SynPlatRecipe;
import com.yuo.PaiMeng.Tiles.SynPlatTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;

import java.util.Optional;

public class SynPlatContainer extends RecipeBookContainer<CraftingInventory> {
    private final SynPlatInventory inputInventory;
    private final SynPlatInventoryResult outputInventory;
    private final SynPlatTile synPlatTile; //存储食材
    private final World world;
    private final PlayerEntity player;

    public SynPlatContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory , new SynPlatTile());
    }

    public SynPlatContainer(int id, PlayerInventory playerInventory, SynPlatTile tile) {
        super(ContainerTypeRegistry.synPlatContainer.get(), id);
        this.synPlatTile = tile;
        this.inputInventory = new SynPlatInventory(this, tile);
        this.outputInventory = new SynPlatInventoryResult(tile);
        this.player = playerInventory.player;
        this.world = playerInventory.player.world;
        //物品槽
        for (int m = 0; m < 3; m++){
            this.addSlot(new Slot(inputInventory, m, 8 + m * 43, 47));
        }
        //转变物质
        this.addSlot(new Slot(inputInventory, 3, 121,23));
        //输出槽
        this.addSlot(new ModReslutSlot(ModRecipeType.SYN_PLAT, player, inputInventory, outputInventory, 4, 152, 47));
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
        if (world.isRemote) return;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
        ItemStack itemstack = ItemStack.EMPTY;
        //获取配方
        Optional<SynPlatRecipe> optional = world.getRecipeManager().getRecipe(ModRecipeType.SYN_PLAT, matrix, world);
        if (optional.isPresent()) {
            SynPlatRecipe recipe = optional.get();
            if (outputInventory.canUseRecipe(world, serverPlayer, recipe)) {
                itemstack = recipe.getCraftingResult(matrix); //获取配方输出
            }
        }
        outputInventory.setInventorySlotContents(4, itemstack); //设置产出
        serverPlayer.connection.sendPacket(new SSetSlotPacket(windowId, 4, itemstack)); //发包同步数据
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.synPlatTile.isUsableByPlayer(playerIn);
    }

    //玩家shift行为
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();
            if (index == 4){
                if (!this.mergeItemStack(itemStack1, 5, 41, true)) return ItemStack.EMPTY;
                slot.onSlotChange(itemStack1, itemstack);
            } else if (index > 4){
                if (hasRecipe(itemStack1)) //包含在配方中
                    if (!this.mergeItemStack(itemStack1, 0, 3, false)) return ItemStack.EMPTY;
                if (itemstack.getItem() instanceof EvolutionDust) //嬗变之尘
                    if (!this.mergeItemStack(itemStack1, 3, 4, false)) return ItemStack.EMPTY;
                if (index < 32) { //从物品栏到快捷栏
                    if (!this.mergeItemStack(itemStack1, 33, 41, false)) return ItemStack.EMPTY;
                } else if (index < 41) {
                    if (!this.mergeItemStack(itemStack1, 5, 32, false)) return ItemStack.EMPTY;
                }
            }else if (!this.mergeItemStack(itemStack1, 5, 41, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }

    protected boolean hasRecipe(ItemStack stack) {
        return this.world.getRecipeManager().getRecipe(ModRecipeType.SYN_PLAT, new Inventory(stack), this.world).isPresent();
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
        return 4;
    }

    @Override
    public int getWidth() {
        return 4;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public RecipeBookCategory func_241850_m() {
        return null;
    }
}
