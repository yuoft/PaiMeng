package com.yuo.paimeng.entity.AI;

import com.yuo.paimeng.entity.HypostasisEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/**
 * 悬浮跟随：绕在目标身前上方 2 格、左右横向游走。
 */
public class HypostasisHoverGoal extends Goal {
    private final HypostasisEntity entity;
    private int strafeTimer;
    private float strafeDirection = 1.0F;

    public HypostasisHoverGoal(HypostasisEntity entity) {
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = entity.getTarget();
        return target != null && target.isAlive();
    }

    @Override
    public boolean canContinueToUse() {
        return canUse();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity target = entity.getTarget();
        if (target == null) return;

        // 🔴 如果是攻击状态，不执行悬停（由实体本身的 tick 确保速度归零）
        if (!entity.isIdle()) {
            return;
        }

        // 🔴 快速转向目标（原来 LookControl 只有 25°/tick，跟不上绕圈的玩家）
        float turnSpeed = entity.getPhase() >= 2 ? HypostasisEntity.TURN_SPEED_PHASE2 : HypostasisEntity.TURN_SPEED;
        entity.turnTo(target, turnSpeed);
        entity.getLookControl().setLookAt(target, turnSpeed, 60.0F);

        Vec3 self = entity.position();
        Vec3 targetPos = target.position();
        Vec3 horizontal = new Vec3(targetPos.x - self.x, 0.0D, targetPos.z - self.z);
        double distance = horizontal.length();
        Vec3 direction = distance > 0.001D ? horizontal.scale(1.0D / distance) : new Vec3(0.0D, 0.0D, 1.0D);

        if (--strafeTimer <= 0) {
            strafeTimer = 40 + entity.getRandom().nextInt(50);
            strafeDirection = entity.getRandom().nextBoolean() ? 1.0F : -1.0F;
        }

        double keepDistance = entity.getPhase() >= 2 ? 4.5D : 5.5D;
        double sideOffset = 1.6D * strafeDirection * (entity.getPhase() >= 2 ? 1.6D : 1.0D);

        // 🔴 离地高度下调到 2 格（原为 3.4D）
        Vec3 desired = new Vec3(
                targetPos.x - direction.x * keepDistance - direction.z * sideOffset,
                targetPos.y + 2.0D,
                targetPos.z - direction.z * keepDistance + direction.x * sideOffset);

        double speed = entity.getPhase() >= 2 ? 0.24D : 0.16D;
        entity.hoverTowards(desired, speed);
    }

    @Override
    public void stop() {
        entity.setDeltaMovement(Vec3.ZERO);
        strafeTimer = 0;
    }
}