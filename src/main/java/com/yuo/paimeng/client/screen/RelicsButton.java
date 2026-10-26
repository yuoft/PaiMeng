package com.yuo.paimeng.client.screen;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.network.NetWorkHandler;
import com.yuo.paimeng.network.RelicsGuiPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RelicsButton extends ImageButton {
    public static final ResourceLocation RESOURCE_OPEN = ModUtils.fa( "textures/gui/open.png");
    public static final ResourceLocation RESOURCE_CLOSE = ModUtils.fa( "textures/gui/close.png");
    private final AbstractContainerScreen<?> playerScreen;
    private static final int ZERO = 0;
    private static final int textureX = 16;
    private static final int textureY = 16;
    private final int guiX;
    private final int guiY;

    public RelicsButton(InventoryScreen screen, int x, int y, int width, int height) {
        super(screen.getGuiLeft() + x, screen.getGuiTop() + y, width, height, ZERO, ZERO, ZERO
                , RESOURCE_OPEN, textureX, textureY, Button::onPress, Component.empty());
        this.playerScreen = screen;
        this.guiX = x;
        this.guiY = y;
    }

    public RelicsButton(RelicsScreen screen, int x, int y, int width, int height) {
        super(x, y, width, height, ZERO, ZERO, ZERO, RESOURCE_OPEN, textureX, textureY, Button::onPress, Component.empty());
        this.playerScreen = screen;
        this.guiX = x;
        this.guiY = y;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && !minecraft.player.isCreative() && this.visible) {
            if (playerScreen instanceof InventoryScreen screen) {
                //如果玩家打开配方书 重设按钮位置
                this.setX(screen.getGuiLeft() + guiX);
                this.setY(screen.getGuiTop() + guiY);
            }
            if (this.isHovered) {
                graphics.blit(RESOURCE_CLOSE, this.getX(), this.getY(), 0, 0, this.width, this.height, 16, 16);
                this.renderToolTip(graphics, mouseX, mouseY);
            } else {
                graphics.blit(RESOURCE_OPEN, this.getX(), this.getY(), 0, 0, this.width, this.height, 16, 16);
            }
        }
    }

    //按下按钮后
    @Override
    public void onPress() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (playerScreen instanceof InventoryScreen) {
            NetWorkHandler.INSTANCE.sendToServer(new RelicsGuiPacket(true));
        } else {
            NetWorkHandler.INSTANCE.sendToServer(new RelicsGuiPacket(false));
            this.displayNormalInventory(mc.player);
        }
    }

    //绘制按钮提示文字
    public void renderToolTip(GuiGraphics graphics, int mouseX, int mouseY) {
        Font fontRenderer = Minecraft.getInstance().font;
        if (playerScreen instanceof InventoryScreen) {
            graphics.drawString(fontRenderer, Component.translatable("gui.button.relics_open").getString(), this.getX(), this.getY() - 8, 0xffffff, false);
        } else
            graphics.drawString(fontRenderer, Component.translatable("gui.button.relics_close").getString(), this.getX(), this.getY() - 8, 0x696969, false);
    }

    //切换回玩家背包
    public void displayNormalInventory(Player player) {
        InventoryScreen gui = new InventoryScreen(player);
        Minecraft.getInstance().setScreen(gui);
    }
}
