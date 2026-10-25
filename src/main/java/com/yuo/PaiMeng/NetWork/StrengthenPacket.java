package com.yuo.PaiMeng.NetWork;

import com.yuo.PaiMeng.Items.RelicsHelper;
import com.yuo.PaiMeng.Tiles.StrengthenTableTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

//强化
public class StrengthenPacket {
    private static BlockPos pos; //坐标
    public StrengthenPacket(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
    }

    public StrengthenPacket(BlockPos pos) {
        StrengthenPacket.pos = pos;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public static void handler(StrengthenPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            Level world = player.level();
            if (!world.isClientSide && world.isLoaded(pos)){ //检查此坐标区块是否加载
                BlockEntity tileEntity = world.getBlockEntity(pos);
                if (tileEntity instanceof StrengthenTableTile tile){
                    NonNullList<ItemStack> relicsExpItem = tile.getRelicsExpItem();
                    ItemStack relics = tile.getItem(16);
                    RelicsHelper.relicsLevelUp(relicsExpItem, relics);
                    tile.shrinkItem();
                    world.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 3.0f, 1.0f);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static BlockPos getPos() {
        return pos;
    }
}
