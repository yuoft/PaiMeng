package com.yuo.PaiMeng.NetWork;

import com.yuo.PaiMeng.Recipes.ModRecipeType;
import com.yuo.PaiMeng.Tiles.BenchTile;
import com.yuo.PaiMeng.Tiles.PotTile;
import com.yuo.PaiMeng.Tiles.TileUtils;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class CookingPacket {
    private static BlockPos pos;
    public CookingPacket(PacketBuffer buffer) {
        pos = buffer.readBlockPos();
    }

    public CookingPacket(BlockPos pos) {
        this.pos = pos;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(this.pos);
    }

    public static void handler(CookingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            World world = player.world;
            if (!world.isRemote && world.isBlockLoaded(pos)){ //检查此坐标区块是否加载
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof PotTile){
                    PotTile potTile = (PotTile) tile;
                    potTile.setInventorySlotContents(4, TileUtils.getTileStack(world, ModRecipeType.POT, potTile));
                    TileUtils.shirkItem(potTile, TileUtils.getRecipeInputs(world, ModRecipeType.POT, potTile));
                }
                if (tile instanceof BenchTile){
                    BenchTile benchTile = (BenchTile) tile;
                    benchTile.setInventorySlotContents(4, TileUtils.getTileStack(world, ModRecipeType.BENCH, benchTile));
                    TileUtils.shirkItem(benchTile, TileUtils.getRecipeInputs(world, ModRecipeType.BENCH, benchTile));
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static BlockPos getPos() {
        return pos;
    }
}
