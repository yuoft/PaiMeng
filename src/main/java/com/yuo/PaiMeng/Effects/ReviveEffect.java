package com.yuo.PaiMeng.Effects;

import com.yuo.PaiMeng.Items.Food.ModFoodEffects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;

public class ReviveEffect extends Effect {
    protected ReviveEffect() {
        super(EffectType.BENEFICIAL, 112233);
    }

    public static void revive(PlayerEntity player, int revive, boolean flag){
        if (player instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
            serverplayerentity.addStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
            CriteriaTriggers.USED_TOTEM.trigger(serverplayerentity, new ItemStack(Items.TOTEM_OF_UNDYING));
        }
        player.setHealth(Math.min(2 * revive, player.getMaxHealth())); //血量
        EffectInstance revive0 = player.getActivePotionEffect(EffectRegistry.REVIVE.get());
        player.clearActivePotions(); //清除buff
        if (revive > 1 && revive0 != null && flag){
            player.addPotionEffect(new EffectInstance(EffectRegistry.REVIVE.get(), revive0.getDuration(), revive - 2)); //添加降一级的复活buff
        }
        if (flag)
            player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, (40 + 10 * revive) * 20, 0)); //防火
        player.addPotionEffect(new EffectInstance(Effects.REGENERATION, (45 + 10 * revive) * 20, revive)); //生命恢复
        player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, (5 * revive) * 20, revive)); //伤害吸收
        player.world.setEntityState(player, (byte) 35);
    }
}
