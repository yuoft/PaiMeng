package com.yuo.paimeng.menu;

import com.yuo.paimeng.recipe.ModRecipeType;
import com.yuo.paimeng.tiles.SuperBenchTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeType;

public class SuperBenchContainer extends AbstractCookingContainer<SuperBenchTile> {

    public SuperBenchContainer(int id, Inventory playerInventory, FriendlyByteBuf buf) {
        this(id, playerInventory, (Container) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public SuperBenchContainer(int id, Inventory playerInventory, Container tile) {
        super(PMMenuTypes.superBenchContainer.get(), id, playerInventory, (SuperBenchTile) tile);
    }

    @Override
    protected RecipeType<?> getRecipeType() {
        return ModRecipeType.BENCH;
    }

    @Override
    protected double getMaxBurnTime() {
        return 0;
    }

    @Override
    public int getBurnTime() {
        return 14;
    }
}