package com.yuo.PaiMeng.NetWork;

import com.yuo.PaiMeng.Tiles.PotTile;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PotPacket {
    private static ItemStack stack;
    private static BlockPos pos;
    public PotPacket(PacketBuffer buffer) {
        stack = buffer.readItemStack();
        pos = buffer.readBlockPos();
    }

    public PotPacket(ItemStack message, BlockPos pos) {
        this.stack = message;
        this.pos = pos;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeItemStack(this.stack);
        buf.writeBlockPos(this.pos);
    }

    public static void handler(PotPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            World world = player.world;
            if (!world.isRemote && world.isBlockLoaded(pos)){ //检查此坐标区块是否加载
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof PotTile){
                    ((PotTile) tile).setInventorySlotContents(4, stack);
                }
            }
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> PotClientPacker.handlePacket(msg, ctx)); //处理服务端发送给客户端的消息
        });
        ctx.get().setPacketHandled(true);
    }
    public static ItemStack getStack() {
        return stack;
    }

    public static BlockPos getPos() {
        return pos;
    }
}
