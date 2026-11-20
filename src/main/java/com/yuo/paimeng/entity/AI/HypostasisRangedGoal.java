package com.yuo.paimeng.entity.AI;

import com.yuo.paimeng.entity.HypostasisEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 远程：对 6~28 格的目标连续抛掷随机矿物方块。
 */
public class HypostasisRangedGoal extends Goal {
    private final HypostasisEntity entity;

    public HypostasisRangedGoal(HypostasisEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive() || !entity.canStartAttack()) return false;
        double distance = entity.distanceTo(target);
        return distance > 6.0D && distance <= 28.0D;
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
        entity.startAttack(HypostasisEntity.AttackState.RANGED);
    }
}
