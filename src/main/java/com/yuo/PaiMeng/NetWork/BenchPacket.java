package com.yuo.PaiMeng.NetWork;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class BenchPacket {
    private static BlockPos pos;
    private static ItemStack stack;
    public BenchPacket(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        stack = buffer.readItem();
    }

    public BenchPacket(BlockPos pos, ItemStack stack) {
        BenchPacket.pos = pos;
        BenchPacket.stack = stack;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeItem(stack);
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
