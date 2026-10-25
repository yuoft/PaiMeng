package com.yuo.PaiMeng.tab;

import com.yuo.PaiMeng.Blocks.CommonFarmland;
import com.yuo.PaiMeng.Blocks.Tree.AppleSapling;
import com.yuo.PaiMeng.Items.CropBlockItem;
import com.yuo.PaiMeng.Items.Drug;
import com.yuo.PaiMeng.Items.DrugBottle;
import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.Items.PMMaterial;
import com.yuo.PaiMeng.Items.PMWeaponMaterial;
import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsBox;
import com.yuo.PaiMeng.Items.RelicsExp;
import com.yuo.PaiMeng.Items.Food.PaiMengFood;
import com.yuo.PaiMeng.Items.PMFoodItem;
import com.yuo.PaiMeng.Items.Weapon.OneSword;
import com.yuo.PaiMeng.Items.Weapon.PMBow;
import com.yuo.PaiMeng.Items.Weapon.PMBook;
import com.yuo.PaiMeng.Items.Weapon.Spear;
import com.yuo.PaiMeng.Items.Weapon.TwoSword;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * 创造模式物品栏。
 * 1.20.1 中物品不再自带物品栏（Item.Properties#group 已移除），
 * 物品 -> 物品栏 的归属由 CreativeModeTab#displayItems 决定，这里用类型分类实现，
 * 分类规则与 1.16.5 各物品的 .group(...) 保持一致。
 */
public class PMGroup {

    public static final DeferredRegister<CreativeModeTab> TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PaiMeng.MOD_ID);

    // 分类编号
    private static final int FOOD = 0;      // 食物（成品）
    private static final int FOODS = 1;     // 食材
    private static final int MATERIAL = 2;  // 素材
    private static final int CROP = 3;      // 种植
    private static final int RELICS = 4;    // 圣遗物
    private static final int DRUG = 5;      // 药剂
    private static final int WEAPON = 6;    // 武器
    private static final int OTHER = 7;     // 其它

    public static final RegistryObject<CreativeModeTab> PaiMengFood = register("paimeng_food", "itemGroup.PaiMengFood",
            () -> PMItems.xiantiaoqiang, FOOD);
    public static final RegistryObject<CreativeModeTab> PaiMengFoods = register("paimeng_foods", "itemGroup.PaiMengFoods",
            () -> PMItems.shourou, FOODS);
    public static final RegistryObject<CreativeModeTab> PaiMengMaterial = register("paimeng_material", "itemGroup.PaiMengMaterial",
            () -> PMItems.longya1, MATERIAL);
    public static final RegistryObject<CreativeModeTab> PaiMengCrop = register("paimeng_crop", "itemGroup.PaiMengCrop",
            () -> PMItems.sunAppleSapling, CROP);
    public static final RegistryObject<CreativeModeTab> PaiMengRelics = register("paimeng_relics", "itemGroup.PaiMengRelics",
            () -> PMItems.juedoushiRelics, RELICS);
    public static final RegistryObject<CreativeModeTab> PaiMengDrug = register("paimeng_drug", "itemGroup.PaiMengDrug",
            () -> PMItems.drugBottle, DRUG);
    public static final RegistryObject<CreativeModeTab> PaiMengWeapon = register("paimeng_weapon", "itemGroup.PaiMengWeapon",
            () -> PMItems.danshoujian, WEAPON);
    public static final RegistryObject<CreativeModeTab> PaiMengOther = register("paimeng_other", "itemGroup.PaiMengOther",
            () -> PMItems.cookingBench, OTHER);

    private static RegistryObject<CreativeModeTab> register(String name, String titleKey,
                                                             Supplier<RegistryObject<? extends Item>> icon, int category) {
        return TABS.register(name, () -> CreativeModeTab.builder()
                .title(Component.translatable(titleKey))
                .icon(() -> new ItemStack(icon.get().get()))
                .displayItems((parameters, output) -> {
                    for (RegistryObject<Item> entry : PMItems.ITEMS.getEntries()) {
                        Item item = entry.get();
                        if (item instanceof Relics) continue; // 圣遗物按 星级×部位 生成变体，见下
                        if (item instanceof Drug) continue; // 药剂按 药水种类 生成变体，见下
                        if (classify(item) == category) {
                            output.accept(item);
                        }
                    }
                    if (category == RELICS) {
                        Relics.fillCreativeItems(output); // 圣遗物的星级/部位变体
                    }
                    if (category == DRUG) {
                        // 1.16.5 里由 Drug#fillItemGroup 生成，1.20.1 无该回调，改为在此生成
                        Item drug = PMItems.drug.get();
                        for (Potion potion : BuiltInRegistries.POTION) {
                            output.accept(PotionUtils.setPotion(new ItemStack(drug), potion));
                        }
                    }
                })
                .build());
    }

    // 物品所属分类，先匹配先得，OTHER 为兜底
    private static int classify(Item item) {
        if (item instanceof PaiMengFood) return FOOD;
        if (item instanceof PMFoodItem) return FOODS;
        if (item instanceof PMMaterial) return MATERIAL;
        if (item instanceof CropBlockItem) return CROP;
        if (item instanceof Relics || item instanceof RelicsExp || item instanceof RelicsBox) return RELICS;
        if (item instanceof Drug || item instanceof DrugBottle) return DRUG;
        if (item instanceof PMWeaponMaterial || item instanceof OneSword || item instanceof TwoSword
                || item instanceof Spear || item instanceof PMBow || item instanceof PMBook) return WEAPON;
        if (item instanceof BlockItem blockItem) {
            if (blockItem.getBlock() instanceof AppleSapling || blockItem.getBlock() instanceof CommonFarmland) return CROP;
        }
        return OTHER;
    }

    public static CreativeModeTab[] all() {
        return new CreativeModeTab[]{PaiMengFood.get(), PaiMengFoods.get(), PaiMengMaterial.get(), PaiMengCrop.get(),
                PaiMengRelics.get(), PaiMengDrug.get(), PaiMengWeapon.get(), PaiMengOther.get()};
    }
}
