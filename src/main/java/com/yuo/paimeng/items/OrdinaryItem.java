package com.yuo.paimeng.items;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 通用普通物品
 */
public class OrdinaryItem extends Item{

	public OrdinaryItem() {
		super(new Properties());
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
		return InteractionResultHolder.pass(playerIn.getItemInHand(handIn));
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
		Item item = stack.getItem();
		if (item.equals(PMItems.yuanshi.get())){
			tooltip.add(Component.translatable("paimeng.text.itemInfo.yuanshi"));
		}
		if (item.equals(PMItems.jiejing.get())){
			tooltip.add(Component.translatable("paimeng.text.itemInfo.jiejing"));
		}
	}
}
