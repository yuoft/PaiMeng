package com.yuo.PaiMeng.Tiles;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.BeaconTileEntity.BeamSegment;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SevenGodTile extends TileEntity implements ITickableTileEntity {
    private List<BeamSegment> beamSegments = Lists.newArrayList();
    private List<BeamSegment> beamColorSegments = Lists.newArrayList();

    public SevenGodTile() {
        super(TileTypeRegistry.SEVEN_GOD.get());
    }

    @Override
    public void tick() {
        if (world == null || world.isRemote) return;

        AxisAlignedBB bb = new AxisAlignedBB(pos.add(-16, -8, -16), pos.add(16, 8, 16));
        for (PlayerEntity player : world.getEntitiesWithinAABB(PlayerEntity.class, bb)) {
            if (player.isAlive()){
                float maxHealth = player.getMaxHealth();
                float health = player.getHealth();
                if (health < maxHealth){
                    player.setHealth(maxHealth);
                }
                player.forceFireTicks(0);
                player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, 100, 0));
                player.addPotionEffect(new EffectInstance(Effects.SPEED, 100, 0));
                player.addPotionEffect(new EffectInstance(Effects.JUMP_BOOST, 100, 0));
            }
        }
    }



    @Override
    public void read(BlockState state, CompoundNBT nbt) {
        super.read(state, nbt);
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        return super.write(compound);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(pos, 1, getUpdateTag());
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        this.pos = pkt.getPos();
        handleUpdateTag(world.getBlockState(pkt.getPos()), pkt.getNbtCompound());
    }
}
