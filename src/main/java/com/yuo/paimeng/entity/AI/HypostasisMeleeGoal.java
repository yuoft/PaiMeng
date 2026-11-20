package com.yuo.paimeng.entity.AI;

import com.yuo.paimeng.entity.HypostasisEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 近战形态选择：贴身时随机进入一字型 / 十字型 / X 型砸击。
 * 形态与命中判定都在 {@link HypostasisEntity} 里，这里只负责挑招式。
 */
public class HypostasisMeleeGoal extends Goal {
    private final HypostasisEntity entity;

    public HypostasisMeleeGoal(HypostasisEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = entity.getTarget();
        return target != null && target.isAlive()
                && entity.canStartAttack()
                && entity.distanceTo(target) <= 6.5D;
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
        int roll = entity.getRandom().nextInt(3);
        HypostasisEntity.AttackState state = roll == 0 ? HypostasisEntity.AttackState.MELEE_LINE
                : roll == 1 ? HypostasisEntity.AttackState.MELEE_CROSS
                : HypostasisEntity.AttackState.MELEE_X;
        entity.startAttack(state);
    }
}
