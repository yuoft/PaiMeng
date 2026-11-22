package com.yuo.paimeng.entity.AI;

import com.yuo.paimeng.entity.HypostasisEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

/**
 * 近战形态选择：贴身时从「能真正打到目标」的一字型 / 十字型 / X 型里随机挑一个。
 * 目标不在身前命中区、或三种形态都覆盖不到时不启动，避免对着空气挥击。
 */
public class HypostasisMeleeGoal extends Goal {
    private final HypostasisEntity entity;

    public HypostasisMeleeGoal(HypostasisEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = entity.getTarget();
        if (!HypostasisRangedGoal.canUse(entity, target)) return false;

        // 水平距离判定
        double dx = target.getX() - entity.getX();
        double dz = target.getZ() - entity.getZ();
        if (dx * dx + dz * dz > 6.0D * 6.0D) return false;

        // Y 差不超过 5 格
        if (Math.abs(target.getY() - entity.getY()) > 5.0D) return false;

        // 必须在身前，且至少有一种形态能覆盖到目标
        return entity.isMeleeTargetInFront() && entity.pickMeleeShape() != null;
    }

    @Override
    public boolean canContinueToUse() {
        if (entity.isIdle()) return false;

        // 目标死亡或消失：中止攻击
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) {
            entity.abortAttack();
            return false;
        }

        // 目标在蓄力期间跑出 9 格：提前收招
        double dx = target.getX() - entity.getX();
        double dz = target.getZ() - entity.getZ();
        if (dx * dx + dz * dz > 9.0D * 9.0D) {
            entity.abortAttack();
            return false;
        }

        return true;
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void start() {
        HypostasisEntity.AttackState shape = entity.pickMeleeShape();
        if (shape == null || !entity.startAttack(shape)) {
            entity.abortAttack();
        }
    }

    @Override
    public void stop() {
        // 如果中途被其他目标打断，也要确保攻击状态被清理
        if (!entity.isIdle()) {
            entity.abortAttack();
        }
    }
}
