package com.yuo.paimeng.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class Drug extends Item {

    public Drug() {
        super(new Properties().stacksTo(1).durability(6));
    }

    // TODO(port): 1.20.1 删除了 Item#fillItemGroup / fillItemCategory，创造栏内容改由 CreativeModeTab#displayItems 决定。
    // 药剂的多药水变体现在由 PMGroup 的 DRUG 分类（displayItems）负责，见 Report 中的资源/协调项。

    //获取默认实例
    @Override
    public ItemStack getDefaultInstance() {
        return PotionUtils.setPotion(super.getDefaultInstance(), Potions.WATER);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
        Player playerentity = entityLiving instanceof Player ? (Player)entityLiving : null;
        if (playerentity instanceof ServerPlayer serverPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverPlayer, stack);
        }

        if (!worldIn.isClientSide) {
            for(MobEffectInstance effectinstance : PotionUtils.getMobEffects(stack)) {
                if (effectinstance.getEffect().isInstantenous()) {
                    effectinstance.getEffect().applyInstantenousEffect(playerentity, playerentity, entityLiving, effectinstance.getAmplifier(), 1.0D);
                } else {
                    entityLiving.addEffect(new MobEffectInstance(effectinstance));
                }
            }
        }

        if (playerentity != null) {
            playerentity.awardStat(Stats.ITEM_USED.get(this));
            if (!playerentity.getAbilities().instabuild) {
                if (stack.getDamageValue() == stack.getMaxDamage() - 1)
                    stack.shrink(1); //耐久高于1 消耗耐久
                else stack.hurtAndBreak(1, playerentity, e -> e.broadcastBreakEvent(playerentity.getUsedItemHand()));
            }
        }

        if (playerentity == null || !playerentity.getAbilities().instabuild) {
            if (stack.isEmpty()) {
                return new ItemStack(PMItems.drugBottle.get());
            }

            if (playerentity != null && stack.getDamageValue() == stack.getMaxDamage()) { //给予空药剂瓶
                playerentity.getInventory().add(new ItemStack(PMItems.drugBottle.get()));
            }
        }

        return stack;
    }

    //使用时间
    @Override
    public int getUseDuration(ItemStack stack) {
        return 32;
    }

    //使用动作
    @Override
    public UseAnim getUseAnimation(ItemStack stack) {
        return UseAnim.DRINK;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        return ItemUtils.startUsingInstantly(worldIn, playerIn, handIn);
    }

    //名称随药水类型变化（1.16.5 的 getDisplayName 在 1.20.1 中叫 getName）
    @Override
    public Component getName(ItemStack stack) {
        return Component.translatable(this.getDescriptionId(),
                Component.translatable(PotionUtils.getPotion(stack).getName("drug.paimeng.")));
    }

    //添加药水描述和染色
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        PotionUtils.addPotionTooltip(stack, tooltip, 1.0F);
    }

    //是否显示附魔光效
    @Override
    public boolean isFoil(ItemStack stack) {
        return super.isFoil(stack) || !PotionUtils.getMobEffects(stack).isEmpty();
    }
}
