package com.yuo.PaiMeng.Items;

import net.minecraft.world.item.Item;

public class DrugBottle extends Item {

    public DrugBottle() {
        super(new Properties().stacksTo(64));
    }

//    @Override
//    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
//        if (!worldIn.isClientSide){
//            ItemStack stack = PotionUtils.setCustomEffects(new ItemStack(PMItems.drug.get()), Arrays.asList(
//                    new MobEffectInstance(MobEffects.LUCK, 1000, 1),
//                    new MobEffectInstance(MobEffects.REGENERATION, 1000, 4),
//                    new MobEffectInstance(MobEffects.WATER_BREATHING, 1000, 2),
//                    new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1000, 3),
//                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1000, 1),
//                    new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1000, 2)));
//            ItemStack stack1 = PotionUtils.setCustomEffects(new ItemStack(Items.POTION), Arrays.asList(
//                    new MobEffectInstance(MobEffects.LUCK, 600, 1),
//                    new MobEffectInstance(MobEffects.REGENERATION, 1500, 4),
//                    new MobEffectInstance(MobEffects.WATER_BREATHING, 1000, 2),
//                    new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1000, 3),
//                    new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 1000, 1),
//                    new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 1000, 2)));
//            playerIn.getInventory().add(stack1);
//        }
//        return super.use(worldIn, playerIn, handIn);
//    }
}
