package com.yuo.PaiMeng.tab;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

//创造模式物品栏 实例化
public class ModGroup extends ItemGroup{
	public static ItemGroup PaiMeng = new ModGroup();

	public ModGroup() {
		super(ItemGroup.GROUPS.length, "PaiMeng"); //页码11开始，名称
	}
	//图标
	@Override
	public ItemStack createIcon() {
		return new ItemStack(Items.ENCHANTED_GOLDEN_APPLE);
	}
}
