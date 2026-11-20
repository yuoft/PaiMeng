package com.yuo.paimeng.menu;

import com.yuo.paimeng.recipe.ModRecipeType;
import com.yuo.paimeng.tiles.BenchTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeType;

public class BenchContainer extends AbstractCookingContainer<BenchTile> {
    
    public BenchContainer(int id, Inventory playerInventory, FriendlyByteBuf buf) {
        this(id, playerInventory, (Container) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public BenchContainer(int id, Inventory playerInventory, Container tile) {
        super(PMMenuTypes.benchContainer.get(), id, playerInventory, (BenchTile) tile);
    }

    @Override
    protected RecipeType<?> getRecipeType() {
        return ModRecipeType.BENCH;
    }

    @Override
    protected double getMaxBurnTime() {
        return 20 * 60 * 16.0; // 工作台最大燃烧 16 分钟
    }
}