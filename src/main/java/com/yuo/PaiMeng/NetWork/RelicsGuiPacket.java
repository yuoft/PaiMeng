package com.yuo.PaiMeng.NetWork;

import com.yuo.PaiMeng.Container.RelicsContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class RelicsGuiPacket {
    private static boolean flag; //是打开圣遗物gui还是关闭
    public RelicsGuiPacket(PacketBuffer buffer) {
        flag = buffer.readBoolean();
    }

    public RelicsGuiPacket(boolean flag) {
        RelicsGuiPacket.flag = flag;
    }

    public void toBytes(PacketBuffer buf) {
        buf.writeBoolean(RelicsGuiPacket.flag);
    }

    public static void handler(RelicsGuiPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player != null){
                World world = player.world;
                if (!world.isRemote){
//                player.closeContainer();
                    player.openContainer.onContainerClosed(player); //关闭gui
                    if (flag){
                        player.openContainer(new INamedContainerProvider() {
                            @Override
                            public ITextComponent getDisplayName() {
                                return new TranslationTextComponent("gui.paimeng.relics");
                            }

                            @Nullable
                            @Override
                            public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player1) {
                                return new RelicsContainer(id, playerInventory);
                            }
                        });
                    }else {
                        player.openContainer = player.container;
                    }
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

}
