package com.yuo.PaiMeng.tab;

import com.yuo.PaiMeng.Items.PMItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

//创造模式物品栏 实例化
public class PMGroup {
	public static ItemGroup PaiMengFood = new GroupFood();
	public static ItemGroup PaiMengFoods = new GroupFoods();
	public static ItemGroup PaiMengMaterial = new GroupMaterial();
	public static ItemGroup PaiMengCrop = new GroupCrop();
	public static ItemGroup PaiMengRelics = new GroupRelics();
	public static ItemGroup PaiMengDrug = new GroupDrug();
	public static ItemGroup PaiMengWeapon = new GroupWeapon();
	public static ItemGroup PaiMengOther = new GroupOther();

	private static class GroupFood extends ItemGroup{

		public GroupFood() {
			super(ItemGroup.GROUPS.length, "PaiMengFood"); //页码11开始，名称
		}
		//图标
		@Override
		public ItemStack createIcon() {
			return new ItemStack(PMItems.xiantiaoqiang.get());
		}

	}
	private static class GroupMaterial extends ItemGroup{

		public GroupMaterial() {
			super(ItemGroup.GROUPS.length, "PaiMengMaterial");
		}
		@Override
		public ItemStack createIcon() {
			return new ItemStack(PMItems.longya1.get());
		}

	}
	private static class GroupFoods extends ItemGroup{

		public GroupFoods() {
			super(ItemGroup.GROUPS.length, "PaiMengFoods");
		}
		@Override
		public ItemStack createIcon() {
			return new ItemStack(PMItems.shourou.get());
		}

	}
	private static class GroupCrop extends ItemGroup{

		public GroupCrop() {
			super(ItemGroup.GROUPS.length, "PaiMengCrop");
		}
		@Override
		public ItemStack createIcon() {
			return new ItemStack(PMItems.sunAppleSapling.get());
		}

	}
	private static class GroupRelics extends ItemGroup{

		public GroupRelics() {
			super(ItemGroup.GROUPS.length, "PaiMengRelics");
		}
		@Override
		public ItemStack createIcon() {
			return new ItemStack(PMItems.juedoushiRelics.get());
		}

	}

	private static class GroupDrug extends ItemGroup{

		public GroupDrug() {
			super(ItemGroup.GROUPS.length, "PaiMengDrug");
		}
		@Override
		public ItemStack createIcon() {
			return new ItemStack(PMItems.drugBottle.get());
		}

	}
	private static class GroupOther extends ItemGroup{

		public GroupOther() {
			super(ItemGroup.GROUPS.length, "PaiMengOther");
		}

		@Override
		public ItemStack createIcon() {
			return new ItemStack(PMItems.cookingBench.get());
		}
	}

	private static class GroupWeapon extends ItemGroup {
		public GroupWeapon() {
			super(ItemGroup.GROUPS.length, "PaiMengWeapon");
		}

		@Override
		public ItemStack createIcon() {
			return new ItemStack(PMItems.danshoujian.get());
		}
	}
}
