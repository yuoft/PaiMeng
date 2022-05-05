package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.tab.ModGroup;
import net.minecraft.item.Item;

public class DrugBottle extends Item {

    public DrugBottle() {
        super(new Properties().maxStackSize(64).group(ModGroup.PaiMengDrug));
    }

//    @Override
//    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
//        if (!worldIn.isRemote){
//            ItemStack stack = PotionUtils.appendEffects(new ItemStack(ItemRegistry.drug.get()), Arrays.asList(
//                    new EffectInstance(Effects.LUCK, 1000, 1),
//                    new EffectInstance(Effects.REGENERATION, 1000, 4),
//                    new EffectInstance(Effects.WATER_BREATHING, 1000, 2),
//                    new EffectInstance(Effects.FIRE_RESISTANCE, 1000, 3),
//                    new EffectInstance(Effects.RESISTANCE, 1000, 1),
//                    new EffectInstance(Effects.MINING_FATIGUE, 1000, 2)));
//            ItemStack stack1 = PotionUtils.appendEffects(new ItemStack(Items.POTION), Arrays.asList(
//                    new EffectInstance(Effects.LUCK, 600, 1),
//                    new EffectInstance(Effects.REGENERATION, 1500, 4),
//                    new EffectInstance(Effects.WATER_BREATHING, 1000, 2),
//                    new EffectInstance(Effects.FIRE_RESISTANCE, 1000, 3),
//                    new EffectInstance(Effects.RESISTANCE, 1000, 1),
//                    new EffectInstance(Effects.MINING_FATIGUE, 1000, 2)));
//            playerIn.inventory.addItemStackToInventory(stack1);
//        }
//        return super.onItemRightClick(worldIn, playerIn, handIn);
//    }
}
