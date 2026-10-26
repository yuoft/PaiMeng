package com.yuo.paimeng.tiles;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class SevenGodTile extends BlockEntity {
    private List<BeaconBlockEntity.BeaconBeamSection> beamSegments = Lists.newArrayList();
    private List<BeaconBlockEntity.BeaconBeamSection> beamColorSegments = Lists.newArrayList();

    //1.20.1 BlockEntityType.Builder.of(SevenGodTile::new, block) 使用的构造
    public SevenGodTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.SEVEN_GOD.get(), pos, state);
    }

    //1.20.1 标准构造
    public SevenGodTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //旧版无参构造（客户端预览仍在用）
    public SevenGodTile() {
        this(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
    }

    /**
     * 1.20.1 方块侧的 getTicker 调用入口
     */
    public void tick(Level level, BlockPos pos, BlockState state) {
        tickInternal(level, pos);
    }

    /**
     * 兼容旧调用
     */
    public void tick() {
        if (this.level == null) return;
        tickInternal(this.level, this.getBlockPos());
    }

    private void tickInternal(Level world, BlockPos pos) {
        if (world.isClientSide) return;

        AABB bb = new AABB(Vec3.atLowerCornerOf(pos.offset(-16, -8, -16)), Vec3.atLowerCornerOf(pos.offset(16, 8, 16)));
        for (Player player : world.getEntitiesOfClass(Player.class, bb)) {
            if (player.isAlive()){
                float maxHealth = player.getMaxHealth();
                float health = player.getHealth();
                if (health < maxHealth){
                    player.setHealth(maxHealth);
                }
                player.clearFire();
                player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 100, 0));
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 100, 0));
                player.addEffect(new MobEffectInstance(MobEffects.JUMP, 100, 0));
            }
        }
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
