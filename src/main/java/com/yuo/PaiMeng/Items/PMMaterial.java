package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.tab.PMGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class PMMaterial extends Item{

	public PMMaterial() {
		super(new Properties().group(PMGroup.PaiMengMaterial));
	}
}
