package com.yuo.paimeng.event;

import com.yuo.paimeng.items.relics.GodEyeType;
import com.yuo.paimeng.items.relics.RelicsHelper;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * 神之眼（圣遗物栏第 6 格）被动效果。
 *
 * <p>每个元素的效果：
 * <ul>
 *   <li>风 WIND：移动速度 +0.1（约等于步行速度翻倍）、攻击速度 +1、免疫缓慢</li>
 *   <li>岩 STONE：最大生命 +10、护甲 +20</li>
 *   <li>雷 THUNDER：免疫负面药水效果，免疫普通生物的魔法伤害（末影龙/凋灵/监守者除外）</li>
 *   <li>火 FIRE：免疫火焰/岩浆伤害并持续灭火，近战命中点燃目标 5 秒</li>
 *   <li>冰 ICE：免疫冻伤（细雪）并清零冰冻值，近战命中使目标缓慢 II 5 秒</li>
 *   <li>水 WATER：游泳速度 +1，持续水下呼吸</li>
 *   <li>草 GRASS：持续生命恢复 I，免疫中毒</li>
 * </ul>
 */
public final class GodEyeEffects {

    private GodEyeEffects() {
    }

    /** 每 tick 由 {@link EventHandler#updatePlayer} 调用：按当前神之眼维持属性加成。 */
    public static void tick(Player player) {
        GodEyeType type = RelicsHelper.getGodEyeType(player);
        if (type == GodEyeType.EMPTY) return;

        switch (type) {
            case WIND -> {
                if (player.hasEffect(MobEffects.MOVEMENT_SLOWDOWN))
                    player.removeEffect(MobEffects.MOVEMENT_SLOWDOWN); // 免疫缓慢
            }
            case THUNDER -> removeHarmfulEffects(player);
            case FIRE -> {
                if (player.isOnFire() || player.getRemainingFireTicks() > 0) player.clearFire();
            }
            case ICE -> {
                if (player.getTicksFrozen() > 0) player.setTicksFrozen(0);
            }
            case WATER -> keepEffect(player, MobEffects.WATER_BREATHING, 0);
            case GRASS -> {
                keepEffect(player, MobEffects.REGENERATION, 1);
                player.removeEffect(MobEffects.POISON);
            }
            default -> {
            }
        }
    }

    /** 受伤时：元素免疫 + 攻击附加效果。 */
    public static void onHurt(LivingHurtEvent event) {
        DamageSource source = event.getSource();

        if (event.getEntity() instanceof Player player) {
            GodEyeType type = RelicsHelper.getGodEyeType(player);
            switch (type) {
                case FIRE -> {
                    if (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE)
                            || source.is(DamageTypes.LAVA) || source.is(DamageTypes.HOT_FLOOR)) {
                        event.setCanceled(true);
                    }
                }
                case ICE -> {
                    if (source.is(DamageTypes.FREEZE)) event.setCanceled(true);
                }
                case WATER -> {
                    if (source.is(DamageTypes.DROWN)) event.setCanceled(true);
                }
                case THUNDER -> {
                    if (source.is(DamageTypes.MAGIC) || source.is(DamageTypes.INDIRECT_MAGIC)) {
                        if (!isBossAttack(source)) event.setCanceled(true);
                    }
                }
                default -> {
                }
            }
        }

        // 玩家近战命中：点燃 / 减速
        if (source.getEntity() instanceof Player attacker && source.getDirectEntity() == attacker) {
            LivingEntity target = event.getEntity();
            GodEyeType attackerType = RelicsHelper.getGodEyeType(attacker);
            if (attackerType == GodEyeType.FIRE) {
                target.setSecondsOnFire(5);
            } else if (attackerType == GodEyeType.ICE) {
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
            }
        }
    }

    /** 末影龙、凋灵、监守者不算“小怪”，其魔法伤害仍会生效。 */
    private static boolean isBossAttack(DamageSource source) {
        return source.getEntity() instanceof EnderDragon
                || source.getEntity() instanceof WitherBoss
                || source.getEntity() instanceof Warden;
    }

    private static void removeHarmfulEffects(Player player) {
        List<MobEffectInstance> active = new ArrayList<>(player.getActiveEffects());
        for (MobEffectInstance instance : active) {
            if (!instance.getEffect().isBeneficial()) {
                player.removeEffect(instance.getEffect());
            }
        }
    }

    /** 维持一个常驻效果：快到期时才续，避免每 tick 发包。 */
    private static void keepEffect(Player player, MobEffect effect, int amplifier) {
        MobEffectInstance current = player.getEffect(effect);
        if (current == null || current.getDuration() < 20 || current.getAmplifier() != 0) {
            player.addEffect(new MobEffectInstance(effect, 100, amplifier, true, false, false));
        }
    }

}
