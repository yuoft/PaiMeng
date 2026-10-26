package com.yuo.paimeng.network;

import com.yuo.paimeng.menu.RelicsContainer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class RelicsGuiPacket {
    private static boolean flag; //是打开圣遗物gui还是关闭
    public RelicsGuiPacket(FriendlyByteBuf buffer) {
        flag = buffer.readBoolean();
    }

    public RelicsGuiPacket(boolean flag) {
        RelicsGuiPacket.flag = flag;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(RelicsGuiPacket.flag);
    }

    public static void handler(RelicsGuiPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null){
                player.closeContainer(); //关闭gui
                if (flag){
                    player.openMenu(new MenuProvider() {
                        @Override
                        public Component getDisplayName() {
                            return Component.translatable("gui.paimeng.relics");
                        }

                        @Nullable
                        @Override
                        public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player1) {
                            return new RelicsContainer(id, playerInventory);
                        }
                    });
                } else {
                    player.containerMenu = player.inventoryMenu;
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
