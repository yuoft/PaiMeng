package com.yuo.paimeng.entity.AI;

import com.yuo.paimeng.entity.HypostasisEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 二阶段俯冲砸地。
 * <p>触发条件：二阶段、6~20 格内、空闲、且距离上次俯冲至少 {@link HypostasisEntity#SLAM_INTERVAL} tick。
 * 冷却独立于普通出招冷却，避免下落攻击过于频繁。
 */
public class HypostasisSlamGoal extends Goal {
    private final HypostasisEntity entity;

    public HypostasisSlamGoal(HypostasisEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        if (entity.getPhase() < 2) return false;
        if (!entity.canStartSlam()) return false; // 俯冲冷却未好
        LivingEntity target = entity.getTarget();
        if (!HypostasisRangedGoal.canUse(entity, target)) return false;
        double distance = entity.distanceTo(target);
        return distance >= 6.0D && distance <= 20.0D;
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
        entity.startAttack(HypostasisEntity.AttackState.MELEE_SLAM);
    }
}
