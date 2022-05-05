package com.yuo.PaiMeng.Entity.AI;

import com.yuo.PaiMeng.Entity.BoarEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockStateMatcher;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.EnumSet;
import java.util.function.Predicate;

public class BoarEatGoal extends Goal {
    private static final Predicate<BlockState> IS_GRASS = BlockStateMatcher.forBlock(Blocks.SWEET_BERRY_BUSH);
    /** The world the grass eater entity is eating from */
    private final World entityWorld;
    /** Number of ticks since the entity started to eat grass */
    private int eatingGrassTimer;
    private BoarEntity boarEntity;

    public BoarEatGoal(BoarEntity boar) {
        this.entityWorld = boar.world;
        this.setMutexFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        this.boarEntity = boar;
    }

    //实现功能 返回是否进行
    public boolean shouldExecute() {
        if (this.boarEntity.getRNG().nextInt(this.boarEntity.isChild() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPos blockpos = this.boarEntity.getPosition();
            if (IS_GRASS.test(this.entityWorld.getBlockState(blockpos))) {
                return true;
            } else {
                return this.entityWorld.getBlockState(blockpos.down()).matchesBlock(Blocks.SWEET_BERRY_BUSH);
            }
        }
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting() {
        this.eatingGrassTimer = 40;
        this.entityWorld.setEntityState(this.boarEntity, (byte)10);
        this.boarEntity.getNavigator().clearPath();
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask() {
        this.eatingGrassTimer = 0;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting() {
        return this.eatingGrassTimer > 0;
    }

    /**
     * Number of ticks since the entity started to eat grass
     */
    public int getEatingGrassTimer() {
        return this.eatingGrassTimer;
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void tick() {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
        if (this.eatingGrassTimer == 4) {
            BlockPos blockpos = this.boarEntity.getPosition();
            if (IS_GRASS.test(this.entityWorld.getBlockState(blockpos))) {
                if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entityWorld, this.boarEntity)) {
                    this.entityWorld.destroyBlock(blockpos, false);
                }

                this.boarEntity.eatGrassBonus();
            } else {
                BlockPos blockpos1 = blockpos.down();
                if (this.entityWorld.getBlockState(blockpos1).matchesBlock(Blocks.SWEET_BERRY_BUSH)) {
                    if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entityWorld, this.boarEntity)) {
                        this.entityWorld.playEvent(2001, blockpos1, Block.getStateId(Blocks.SWEET_BERRY_BUSH.getDefaultState()));
                        this.entityWorld.setBlockState(blockpos1, Blocks.SWEET_BERRY_BUSH.getDefaultState(), 2);
                    }

                    this.boarEntity.eatGrassBonus();
                }
            }

        }
    }
}
