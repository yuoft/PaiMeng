package com.yuo.paimeng.entity.AI;

import com.yuo.paimeng.entity.HypostasisEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

/**
 * 雷击：4~10 格内按概率触发多层环状落雷。
 * <p>雷击的覆盖范围是 0.5~11.5 格（3/6/9 三环 + 2.5 格判定半径），
 * 因此触发距离上限设为 10 格，保证目标一定在覆盖范围内。
 * <p>关键：不能直接每 tick 判定概率（1 秒内 40% × 20 ≈ 100% 必然触发），
 * 改为"内部 cooldown"，每 40~80 tick 才判定一次。
 */
public class HypostasisThunderGoal extends Goal {
    private final HypostasisEntity entity;
    /** 上一次雷击结束后，还需要等待多少 tick 才能再判定。 */
    private int cooldown;

    public HypostasisThunderGoal(HypostasisEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        // 🔴 冷却中，跳过判定
        if (cooldown > 0) {
            cooldown--;
            return false;
        }

        LivingEntity target = entity.getTarget();
        if (!HypostasisRangedGoal.canUse(entity, target)) return false;

        // 🔴 距离范围与雷击覆盖范围匹配：4~10 格
        double distance = entity.distanceTo(target);
        if (distance < 4.0D || distance > 10.0D) return false;

        // 🔴 每 tick 调用时会累积概率，所以用"低概率"补偿：实际触发概率约 1/40 tick
        double chance = entity.getPhase() >= 2 ? 0.03D : 0.02D;
        if (entity.getRandom().nextDouble() >= chance) return false;

        return true;
    }

    @Override
    public boolean canContinueToUse() {
        return !entity.isIdle();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        entity.startAttack(HypostasisEntity.AttackState.THUNDER);
    }

    @Override
    public void stop() {
        // 🔴 雷击结束后，进入 3~5 秒的 CD，让 Ranged/Melee 有机会抢占
        cooldown = 60 + entity.getRandom().nextInt(60);
    }
}