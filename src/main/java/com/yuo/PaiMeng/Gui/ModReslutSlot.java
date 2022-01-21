package com.yuo.PaiMeng.Gui;

import com.yuo.PaiMeng.Tiles.TileUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.CraftingResultSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.util.NonNullList;

public class ModReslutSlot extends CraftingResultSlot {
    private final CraftingInventory craftMatrix;
    private final PlayerEntity player;
    private final IRecipeType recipeType;

    public ModReslutSlot(IRecipeType recipeType, PlayerEntity player, CraftingInventory craftingInventory, IInventory inventoryIn, int slotIndex, int xPosition, int yPosition) {
        super(player, craftingInventory, inventoryIn, slotIndex, xPosition, yPosition);
        this.player = player;
        this.craftMatrix = craftingInventory;
        this.recipeType = recipeType;
    }

    @Override
    public ItemStack onTake(PlayerEntity thePlayer, ItemStack stack) {
        this.onCrafting(stack);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(thePlayer);
        NonNullList<ItemStack> nonnulllist = thePlayer.world.getRecipeManager().getRecipeNonNull(recipeType, this.craftMatrix, thePlayer.world);
        NonNullList<ItemStack> recipeInputs = TileUtils.getRecipeInputs(thePlayer.world, recipeType, this.craftMatrix);
        net.minecraftforge.common.ForgeHooks.setCraftingPlayer(null);
        for(int i = 0; i < recipeInputs.size(); ++i) {
            ItemStack itemstack = this.craftMatrix.getStackInSlot(i);
            ItemStack itemstack1 = recipeInputs.get(i);
            if (!itemstack.isEmpty()) {
                this.craftMatrix.decrStackSize(i, itemstack1.getCount());
//                itemstack = this.craftMatrix.getStackInSlot(i);
            }

//            if (!itemstack1.isEmpty()) {
//                if (itemstack.isEmpty()) {
//                    this.craftMatrix.setInventorySlotContents(i, itemstack1);
//                } else if (ItemStack.areItemsEqual(itemstack, itemstack1) && ItemStack.areItemStackTagsEqual(itemstack, itemstack1)) {
//                    itemstack1.grow(itemstack.getCount());
//                    this.craftMatrix.setInventorySlotContents(i, itemstack1);
//                } else if (!this.player.inventory.addItemStackToInventory(itemstack1)) {
//                    this.player.dropItem(itemstack1, false);
//                }
//            }
        }

        return stack;
    }
}
