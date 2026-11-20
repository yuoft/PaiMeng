package com.yuo.paimeng.entity.AI;

import com.yuo.paimeng.entity.HypostasisEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 雷击：5~16 格内按概率触发多层环状落雷（二阶段概率与环数都更高）。
 */
public class HypostasisThunderGoal extends Goal {
    private final HypostasisEntity entity;

    public HypostasisThunderGoal(HypostasisEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive() || !entity.canStartAttack()) return false;
        double distance = entity.distanceTo(target);
        if (distance < 5.0D || distance > 16.0D) return false;
        double chance = entity.getPhase() >= 2 ? 0.6D : 0.4D;
        return entity.getRandom().nextDouble() < chance;
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
}
