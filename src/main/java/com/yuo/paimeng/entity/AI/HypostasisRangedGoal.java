package com.yuo.paimeng.entity.AI;

import com.yuo.paimeng.entity.HypostasisEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

/**
 * 远程：对 8~28 格的目标连续抛掷随机矿物方块。
 * <p>距离阈值从 6 格上调到 8 格，避免和近战触发范围重叠。
 */
public class HypostasisRangedGoal extends Goal {
    private final HypostasisEntity entity;

    public HypostasisRangedGoal(HypostasisEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = entity.getTarget();
        if (!canUse(entity, target)) return false;
        double distance = entity.distanceTo(target);
        return distance > 8.0D && distance <= 28.0D;
    }

    protected static boolean canUse(HypostasisEntity entity, LivingEntity target){
        if (target == null || !target.isAlive() || !entity.canStartAttack()) return false;
        return !(target instanceof Player player) || (!player.isCreative() && !player.isSpectator());
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