package com.yuo.PaiMeng.NetWork;

import com.yuo.PaiMeng.Items.RelicsHelper;
import com.yuo.PaiMeng.Tiles.StrengthenTableTile;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

//强化
public class StrengthenPacket {
    private static BlockPos pos; //坐标
    public StrengthenPacket(PacketBuffer buffer) {
        pos = buffer.readBlockPos();
    }

    public StrengthenPacket(BlockPos pos) {
        StrengthenPacket.pos = pos;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
    }

    public static void handler(StrengthenPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            World world = player.world;
            if (!world.isRemote && world.isBlockLoaded(pos)){ //检查此坐标区块是否加载
                TileEntity tileEntity = world.getTileEntity(pos);
                if (tileEntity instanceof StrengthenTableTile){
                    StrengthenTableTile tile = (StrengthenTableTile) tileEntity;
                    NonNullList<ItemStack> relicsExpItem = tile.getRelicsExpItem();
                    ItemStack relics = tile.getStackInSlot(16);
                    RelicsHelper.relicsLevelUp(relicsExpItem, relics);
                    tile.shrinkItem();
                    world.playSound(null, pos, SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.BLOCKS, 3.0f, 1.0f);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static BlockPos getPos() {
        return pos;
    }
}
