package com.yuo.paimeng.menu;

import com.yuo.paimeng.recipe.ModRecipeType;
import com.yuo.paimeng.tiles.PotTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.crafting.RecipeType;

public class PotContainer extends AbstractCookingContainer<PotTile> {
    
    public PotContainer(int id, Inventory playerInventory, FriendlyByteBuf buf) {
        this(id, playerInventory, (Container) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public PotContainer(int id, Inventory playerInventory, Container tile) {
        super(PMMenuTypes.potContainer.get(), id, playerInventory, (PotTile) tile);
    }

    @Override
    protected RecipeType<?> getRecipeType() {
        return ModRecipeType.POT;
    }

    @Override
    protected double getMaxBurnTime() {
        return 20 * 60 * 8.0; // 锅最大燃烧 8 分钟
    }
}