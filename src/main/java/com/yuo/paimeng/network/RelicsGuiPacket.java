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
    private final boolean flag; // 🔴 移除 static

    public RelicsGuiPacket(FriendlyByteBuf buffer) {
        this.flag = buffer.readBoolean();
    }

    public RelicsGuiPacket(boolean flag) {
        this.flag = flag;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBoolean(this.flag);
    }

    public static void handler(RelicsGuiPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player != null){
                if (msg.flag){ // 🔴 使用 msg.flag 获取数据
                    player.closeContainer(); //关闭gui
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
                    // 让服务端同步一下玩家的背包状态
                    player.inventoryMenu.broadcastChanges();
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}