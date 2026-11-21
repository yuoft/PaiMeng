package com.yuo.paimeng.entity.AI;

import com.yuo.paimeng.entity.HypostasisEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

/**
 * 近战形态选择：贴身时随机进入一字型 / 十字型 / X 型砸击。
 */
public class HypostasisMeleeGoal extends Goal {
    private final HypostasisEntity entity;

    public HypostasisMeleeGoal(HypostasisEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive() || !entity.canStartAttack()) return false;

        // 🔴 排除创造/旁观模式玩家，避免"对着玩家放技能但没伤害"的假象
        if (target instanceof Player player && (player.isCreative() || player.isSpectator())) {
            return false;
        }

        // 水平距离判定
        double dx = target.getX() - entity.getX();
        double dz = target.getZ() - entity.getZ();
        if (dx * dx + dz * dz > 6.0D * 6.0D) return false;

        // Y 差不超过 5 格
        return Math.abs(target.getY() - entity.getY()) <= 5.0D;
    }

    @Override
    public boolean canContinueToUse() {
        if (entity.isIdle()) return false;

        // 🔴 目标死亡或消失：中止攻击，避免砸地判定对空气生效
        LivingEntity target = entity.getTarget();
        if (target == null || !target.isAlive()) {
            entity.abortAttack();
            return false;
        }

        // 🔴 目标在蓄力期间跑出 9 格：提前收招
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
        int roll = entity.getRandom().nextInt(3);
        HypostasisEntity.AttackState state = roll == 0 ? HypostasisEntity.AttackState.MELEE_LINE
                : roll == 1 ? HypostasisEntity.AttackState.MELEE_CROSS
                : HypostasisEntity.AttackState.MELEE_X;
        entity.startAttack(state);
    }

    @Override
    public void stop() {
        // 如果中途被其他目标打断，也要确保攻击状态被清理
        if (!entity.isIdle()) {
            entity.abortAttack();
        }
    }
}