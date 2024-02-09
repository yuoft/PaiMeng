package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.Container.SeedBoxContainer;
import com.yuo.PaiMeng.tab.PMGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class SeedBox extends Item {
    private static final ITextComponent CONTAINER_NAME = new TranslationTextComponent("gui.paimeng.seed_box");

    public SeedBox() {
        super(new Properties().group(PMGroup.PaiMengOther).maxStackSize(1));
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.seed_box"));
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote){
            playerIn.openContainer(getContainer());
            playerIn.addStat(Stats.ITEM_USED.get(this));
        }
        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }

    public INamedContainerProvider getContainer() {
        return new SimpleNamedContainerProvider((id, inventory, player)
                -> new SeedBoxContainer(id, inventory), CONTAINER_NAME);
    }
}
