package com.yuo.PaiMeng.NetWork;

import com.yuo.PaiMeng.Jei.CookingTransferHandler;
import com.yuo.PaiMeng.Jei.RecipeTransferHandlerServer;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class TransferPacket {
    public Map<Integer, CookingTransferHandler.MatchingItem> recipeMap;
    public List<Integer> craftingSlots;
    public List<Integer> inventorySlots;
    private boolean maxTransfer;
    private boolean requireCompleteSets;

    public TransferPacket(Map<Integer, CookingTransferHandler.MatchingItem> recipeMap, List<Integer> craftingSlots, List<Integer> inventorySlots, boolean maxTransfer, boolean requireCompleteSets) {
        this.recipeMap = recipeMap;
        this.craftingSlots = craftingSlots;
        this.inventorySlots = inventorySlots;
        this.maxTransfer = maxTransfer;
        this.requireCompleteSets = requireCompleteSets;
    }

    public TransferPacket(PacketBuffer buf) {
        int recipeMapSize = buf.readVarInt();
        Map<Integer, CookingTransferHandler.MatchingItem> recipeMap = new HashMap<>();
        for (int i = 0; i < recipeMapSize; i++) {
            int slotIndex = buf.readVarInt();
            recipeMap.put(slotIndex, new CookingTransferHandler.MatchingItem(buf.readVarInt(), buf.readVarInt()));
        }

        int craftingSlotsSize = buf.readVarInt();
        List<Integer> craftingSlots = new ArrayList<>();
        for (int i = 0; i < craftingSlotsSize; i++) {
            int slotIndex = buf.readVarInt();
            craftingSlots.add(slotIndex);
        }

        int inventorySlotsSize = buf.readVarInt();
        List<Integer> inventorySlots = new ArrayList<>();
        for (int i = 0; i < inventorySlotsSize; i++) {
            int slotIndex = buf.readVarInt();
            inventorySlots.add(slotIndex);
        }
        boolean maxTransfer = buf.readBoolean();
        boolean requireCompleteSets = buf.readBoolean();
        this.recipeMap = recipeMap;
        this.craftingSlots = craftingSlots;
        this.inventorySlots = inventorySlots;
        this.maxTransfer = maxTransfer;
        this.requireCompleteSets = requireCompleteSets;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeVarInt(recipeMap.size());
        //修改处
        for (Map.Entry<Integer, CookingTransferHandler.MatchingItem> recipeMapEntry : recipeMap.entrySet()) {
            buf.writeVarInt(recipeMapEntry.getKey());
            buf.writeVarInt(recipeMapEntry.getValue().getSlotIndex());
            buf.writeVarInt(recipeMapEntry.getValue().getSlotCount());
//            for (Map.Entry<Integer, Integer> integerEntry : recipeMapEntry.getValue().entrySet()) {
//                buf.writeVarInt(integerEntry.getKey());
//                buf.writeVarInt(integerEntry.getValue());
//            }
        }

        buf.writeVarInt(craftingSlots.size());
        for (Integer craftingSlot : craftingSlots) {
            buf.writeVarInt(craftingSlot);
        }

        buf.writeVarInt(inventorySlots.size());
        for (Integer inventorySlot : inventorySlots) {
            buf.writeVarInt(inventorySlot);
        }

        buf.writeBoolean(maxTransfer);
        buf.writeBoolean(requireCompleteSets);
    }

    public static void handler(TransferPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            World world = player.world;
            if (!world.isRemote){
                RecipeTransferHandlerServer.setItems(player, msg.recipeMap, msg.craftingSlots, msg.inventorySlots, msg.maxTransfer, msg.requireCompleteSets);
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
