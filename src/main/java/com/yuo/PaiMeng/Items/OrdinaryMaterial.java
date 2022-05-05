package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.tab.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class OrdinaryMaterial extends Item{

	public OrdinaryMaterial() {
		super(new Properties().group(ModGroup.PaiMengMaterial));
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
	}
}
