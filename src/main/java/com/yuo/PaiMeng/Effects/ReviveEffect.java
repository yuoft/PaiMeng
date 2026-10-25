package com.yuo.PaiMeng.Effects;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ReviveEffect extends MobEffect {
    protected ReviveEffect() {
        super(MobEffectCategory.BENEFICIAL, 112233);
    }

    public static void revive(Player player, int revive, boolean flag){
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.awardStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
            CriteriaTriggers.USED_TOTEM.trigger(serverPlayer, new ItemStack(Items.TOTEM_OF_UNDYING));
        }
        player.setHealth(Math.min(2 * revive, player.getMaxHealth())); //血量
        MobEffectInstance revive0 = player.getEffect(EffectRegistry.REVIVE.get());
        player.removeAllEffects(); //清除buff
        if (revive > 1 && revive0 != null && flag){
            player.addEffect(new MobEffectInstance(EffectRegistry.REVIVE.get(), revive0.getDuration(), revive - 2)); //添加降一级的复活buff
        }
        if (flag)
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, (40 + 10 * revive) * 20, 0)); //防火
        player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, (45 + 10 * revive) * 20, revive)); //生命恢复
        player.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, (5 * revive) * 20, revive)); //伤害吸收
        player.level().broadcastEntityEvent(player, (byte) 35);
    }
}
