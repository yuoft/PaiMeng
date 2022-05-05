package com.yuo.PaiMeng.tab;

import com.yuo.PaiMeng.Items.ItemRegistry;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

//创造模式物品栏 实例化
public class ModGroup{
	public static ItemGroup PaiMengFood = new GroupFood();
	public static ItemGroup PaiMengFoods = new GroupFoods();
	public static ItemGroup PaiMengMaterial = new GroupMaterial();
	public static ItemGroup PaiMengCrop = new GroupCrop();
	public static ItemGroup PaiMengRelics = new GroupRelics();
	public static ItemGroup PaiMengDrug = new GroupDrug();
	public static ItemGroup PaiMengOther = new GroupOther();

	private static class GroupFood extends ItemGroup{

		public GroupFood() {
			super(ItemGroup.GROUPS.length, "PaiMengFood"); //页码11开始，名称
		}
		//图标
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ItemRegistry.xiantiaoqiang.get());
		}

	}
	private static class GroupMaterial extends ItemGroup{

		public GroupMaterial() {
			super(ItemGroup.GROUPS.length, "PaiMengMaterial");
		}
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ItemRegistry.longya1.get());
		}

	}
	private static class GroupFoods extends ItemGroup{

		public GroupFoods() {
			super(ItemGroup.GROUPS.length, "PaiMengFoods");
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
			return new ItemStack(ItemRegistry.sunAppleSapling.get());
		}

	}
	private static class GroupRelics extends ItemGroup{

		public GroupRelics() {
			super(ItemGroup.GROUPS.length, "PaiMengRelics");
		}
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ItemRegistry.juedoushiRelics.get());
		}

	}

	private static class GroupDrug extends ItemGroup{

		public GroupDrug() {
			super(ItemGroup.GROUPS.length, "PaiMengDrug");
		}
		@Override
		public ItemStack createIcon() {
			return new ItemStack(ItemRegistry.drugBottle.get());
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
