package com.yuo.PaiMeng.NetWork;

import com.yuo.PaiMeng.Tiles.BenchTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

//客户端控制灶台燃料显示
public class BenchClientPacket {
    public static void handlePacket(BenchPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->{
            ClientLevel world = Minecraft.getInstance().level;
            if (world != null){
                BlockEntity tileEntity = world.getBlockEntity(msg.getPos());
                if (tileEntity instanceof BenchTile benchTile){
                    benchTile.items.set(6, msg.getStack());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
