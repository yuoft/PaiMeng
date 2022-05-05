package com.yuo.PaiMeng.NetWork;

import com.yuo.PaiMeng.Tiles.BenchTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

//客户端控制灶台燃料显示
public class BenchClientPacket {
    public static void handlePacket(BenchPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() ->{
            ClientWorld world = Minecraft.getInstance().world;
            if (world != null){
                TileEntity tileEntity = world.getTileEntity(msg.getPos());
                if (tileEntity instanceof BenchTile){
                    ((BenchTile) tileEntity).items.set(5, msg.getStack());
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
