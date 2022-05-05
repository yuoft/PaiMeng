package com.yuo.PaiMeng.NetWork;

import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class BenchPacket {
    private static BlockPos pos;
    private static ItemStack stack;
    public BenchPacket(PacketBuffer buffer) {
        pos = buffer.readBlockPos();
        stack = buffer.readItemStack();
    }

    public BenchPacket(BlockPos pos, ItemStack stack) {
        BenchPacket.pos = pos;
        BenchPacket.stack = stack;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeItemStack(stack);
    }

    public static void handler(BenchPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> BenchClientPacket.handlePacket(msg, ctx)); //处理服务端发送给客户端的消息
        });
        ctx.get().setPacketHandled(true);
    }

    public BlockPos getPos() {
        return pos;
    }

    public ItemStack getStack() {
        return stack;
    }
}
