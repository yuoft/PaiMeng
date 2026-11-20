package com.yuo.paimeng.entity.AI;

import com.yuo.paimeng.entity.HypostasisEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

/**
 * 悬浮跟随：绕在目标身前上方 4 格、高度 +3.4 的位置，左右横向游走。
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

        entity.getLookControl().setLookAt(target, 25.0F, 25.0F);

        Vec3 self = entity.position();
        Vec3 targetPos = target.position();
        Vec3 horizontal = new Vec3(targetPos.x - self.x, 0.0D, targetPos.z - self.z);
        double distance = horizontal.length();
        Vec3 direction = distance > 0.001D ? horizontal.scale(1.0D / distance) : new Vec3(0.0D, 0.0D, 1.0D);

        if (--strafeTimer <= 0) {
            strafeTimer = 40 + entity.getRandom().nextInt(50);
            strafeDirection = entity.getRandom().nextBoolean() ? 1.0F : -1.0F;
        }

        // 二阶段贴得更近，但始终与玩家保持一段距离，避免外壳直接糊在镜头上
        double keepDistance = entity.getPhase() >= 2 ? 4.5D : 5.5D;
        double sideOffset = 1.6D * strafeDirection * (entity.getPhase() >= 2 ? 1.6D : 1.0D);

        Vec3 desired = new Vec3(
                targetPos.x - direction.x * keepDistance - direction.z * sideOffset,
                targetPos.y + 3.4D,
                targetPos.z - direction.z * keepDistance + direction.x * sideOffset);

        double speed = entity.getPhase() >= 2 ? 0.24D : 0.16D;
        entity.hoverTowards(desired, speed);

        // 偶尔抬头/俯冲的小幅摆动，避免看起来像静止贴图
        entity.setXRot((float) (Math.sin(entity.tickCount * 0.06D) * 8.0D));
    }

    @Override
    public void stop() {
        entity.setDeltaMovement(entity.getDeltaMovement().scale(0.4D));
    }
}
