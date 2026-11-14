package com.yuo.paimeng.tiles;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.UUID;

public class SevenGodTile extends BlockEntity {

    @Nullable
    private UUID ownerUUID;

    public SevenGodTile(BlockPos pos, BlockState state) {
        this(TileTypeRegistry.SEVEN_GOD.get(), pos, state);
    }

    public SevenGodTile(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public SevenGodTile() {
        this(BlockPos.ZERO, Blocks.AIR.defaultBlockState());
    }

    @Nullable
    public UUID getOwnerUUID() {
        return ownerUUID;
    }

    public void setOwner(UUID uuid) {
        this.ownerUUID = uuid;
        this.setChanged();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        if (nbt.hasUUID("Owner")) {
            this.ownerUUID = nbt.getUUID("Owner");
        }
    }

    @Override
    protected void saveAdditional(CompoundTag compound) {
        super.saveAdditional(compound);
        if (this.ownerUUID != null) {
            compound.putUUID("Owner", this.ownerUUID);
        }
    }

    @Nullable
    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}