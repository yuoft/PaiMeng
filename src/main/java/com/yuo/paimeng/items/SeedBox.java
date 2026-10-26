package com.yuo.paimeng.items;

import com.yuo.paimeng.menu.SeedBoxContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class SeedBox extends Item {
    private static final Component CONTAINER_NAME = Component.translatable("gui.paimeng.seed_box");

    public SeedBox() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("paimeng.text.itemInfo.seed_box"));
    }

    // TODO(port): 1.20.1 删除了 Item#hasContainerItem(ItemStack)，改为 hasCraftingRemainingItem()/craftRemainder(Item)。
    // 旧实现只返回 true 却从未提供剩余物，属于无效覆写，故这里不再覆写（默认 false，行为与旧版实际效果一致）。

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (!worldIn.isClientSide){
            playerIn.openMenu(getContainer());
            playerIn.awardStat(Stats.ITEM_USED.get(this));
        }
        return InteractionResultHolder.success(playerIn.getItemInHand(handIn));
    }

    //1.16.5 的 INamedContainerProvider 在 1.20.1 中叫 MenuProvider
    public MenuProvider getContainer() {
        return new SimpleMenuProvider((id, inventory, player) -> {
            // TODO(port): SeedBoxContainer 尚未移植完（继承链 RecipeBookContainer -> AbstractContainerMenu 断在 Container 组那边），
            // 这里先用 (Object) 中转保证本文件可编译；等 Container 组修好后可直接写成 new SeedBoxContainer(id, inventory)。
            return (AbstractContainerMenu) (Object) new SeedBoxContainer(id, inventory);
        }, CONTAINER_NAME);
    }
}
