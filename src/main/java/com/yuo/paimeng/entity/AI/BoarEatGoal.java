package com.yuo.paimeng.entity.AI;

import com.yuo.paimeng.entity.BoarEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.predicate.BlockStatePredicate;

import java.util.EnumSet;
import java.util.function.Predicate;

public class BoarEatGoal extends Goal {
    private static final Predicate<BlockState> IS_GRASS = BlockStatePredicate.forBlock(Blocks.SWEET_BERRY_BUSH);
    /** The world the grass eater entity is eating from */
    private final Level entityWorld;
    /** Number of ticks since the entity started to eat grass */
    private int eatingGrassTimer;
    private final BoarEntity boarEntity;

    public BoarEatGoal(BoarEntity boar) {
        this.entityWorld = boar.level();
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
        this.boarEntity = boar;
    }

    //实现功能 返回是否进行
    @Override
    public boolean canUse() {
        if (this.boarEntity.getRandom().nextInt(this.boarEntity.isBaby() ? 50 : 1000) != 0) {
            return false;
        } else {
            BlockPos blockpos = this.boarEntity.blockPosition();
            if (IS_GRASS.test(this.entityWorld.getBlockState(blockpos))) {
                return true;
            } else {
                return this.entityWorld.getBlockState(blockpos.below()).is(Blocks.SWEET_BERRY_BUSH);
            }
        }
    }

    @Override
    public void start() {
        this.eatingGrassTimer = 40;
        this.entityWorld.broadcastEntityEvent(this.boarEntity, (byte)10);
        this.boarEntity.getNavigation().stop();
    }

    @Override
    public void stop() {
        this.eatingGrassTimer = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return this.eatingGrassTimer > 0;
    }

    /**
     * Number of ticks since the entity started to eat grass
     */
    public int getEatingGrassTimer() {
        return this.eatingGrassTimer;
    }

    @Override
    public void tick() {
        this.eatingGrassTimer = Math.max(0, this.eatingGrassTimer - 1);
        if (this.eatingGrassTimer == 4) {
            BlockPos blockpos = this.boarEntity.blockPosition();
            if (IS_GRASS.test(this.entityWorld.getBlockState(blockpos))) {
                if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entityWorld, this.boarEntity)) {
                    this.entityWorld.destroyBlock(blockpos, false);
                }
                this.boarEntity.eatGrassBonus();
            } else {
                BlockPos blockpos1 = blockpos.below();
                if (this.entityWorld.getBlockState(blockpos1).is(Blocks.SWEET_BERRY_BUSH)) {
                    if (net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(this.entityWorld, this.boarEntity)) {
                        this.entityWorld.levelEvent(2001, blockpos1, Block.getId(Blocks.SWEET_BERRY_BUSH.defaultBlockState()));
                        this.entityWorld.setBlock(blockpos1, Blocks.SWEET_BERRY_BUSH.defaultBlockState(), 2);
                    }
                    this.boarEntity.eatGrassBonus();
                }
            }
        }
    }
}
