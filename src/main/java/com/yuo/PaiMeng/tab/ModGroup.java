package com.yuo.PaiMeng.tab;

import com.yuo.PaiMeng.Items.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

//创造模式物品栏 实例化
public class ModGroup{
	public static ItemGroup PaiMengFood = new GroupFood();
	public static ItemGroup PaiMengMaterial = new GroupMaterial();
	public static ItemGroup PaiMengCrop = new GroupCrop();
	public static ItemGroup PaiMengOther = new GroupOther();

	private static class GroupFood extends ItemGroup{

		public GroupFood() {
			super(ItemGroup.GROUPS.length, "PaiMengFood"); //页码11开始，名称
		}
		//图标
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ItemRegistry.sunAppleSapling.get());
		}

	}
	private static class GroupMaterial extends ItemGroup{

		public GroupMaterial() {
			super(ItemGroup.GROUPS.length, "PaiMengMaterial");
		}
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ItemRegistry.shourou.get());
		}

	}
	private static class GroupCrop extends ItemGroup{

		public GroupCrop() {
			super(ItemGroup.GROUPS.length, "PaiMengCrop");
		}
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ItemRegistry.baocai.get());
		}

	}
	private static class GroupOther extends ItemGroup{

		public GroupOther() {
			super(ItemGroup.GROUPS.length, "PaiMengOther");
		}

		@Override
		public ItemStack createIcon() {
			return new ItemStack(ItemRegistry.cookingBench.get());
		}
	}
}
