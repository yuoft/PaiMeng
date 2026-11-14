package com.yuo.paimeng.network;

import com.yuo.paimeng.items.RelicsHelper;
import com.yuo.paimeng.tiles.StrengthenTableTile;
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

public class StrengthenPacket {
    private final BlockPos pos; // 🔴 移除 static

    public StrengthenPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
    }

    public StrengthenPacket(BlockPos pos) {
        this.pos = pos;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    public static void handler(StrengthenPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            Level world = player.level();
            
            // 🔴 安全校验：玩家必须距离方块 8 格以内
            if (player.distanceToSqr(msg.pos.getX() + 0.5, msg.pos.getY() + 0.5, msg.pos.getZ() + 0.5) > 64.0) {
                return;
            }

            if (!world.isClientSide && world.isLoaded(msg.pos)){ //检查此坐标区块是否加载
                BlockEntity tileEntity = world.getBlockEntity(msg.pos);
                if (tileEntity instanceof StrengthenTableTile tile){
                    NonNullList<ItemStack> relicsExpItem = tile.getRelicsExpItem();
                    ItemStack relics = tile.getItem(16);
                    
                    // 这里建议再增加一个判断，确认确实有物品可以强化，防止空包刷声音或报错
                    if (!relics.isEmpty() && !relicsExpItem.isEmpty()) {
                        RelicsHelper.relicsLevelUp(relicsExpItem, relics);
                        tile.shrinkItem();
                        world.playSound(null, msg.pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 3.0f, 1.0f);
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}