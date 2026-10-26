package com.yuo.PaiMeng.Client.Gui;

import com.yuo.PaiMeng.ModUtils;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.NetWork.StrengthenPacket;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Tiles.StrengthenTableTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StrengthenButton extends ImageButton {
    public static final ResourceLocation TEXTURE = ModUtils.fa("textures/gui/strengthen_table.png");
    private final StrengthenTableScreen screen;
    private static final int ZERO = 0;
    private static final int textureSize = 256;

    public StrengthenButton(StrengthenTableScreen screen, int x, int y, int width, int height) {
        super(screen.getGuiLeft() + x, screen.getGuiTop() + y, width, height, ZERO, ZERO, ZERO
                , TEXTURE, Button::onPress);
        this.screen = screen;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            if (this.isHovered && screen.getMenu().hasItem()) {
                graphics.blit(TEXTURE, this.getX(), this.getY(), 198, 0, this.width, this.height, textureSize, textureSize);
                this.renderToolTip(graphics, mouseX, mouseY);
            } else {
                graphics.blit(TEXTURE, this.getX(), this.getY(), 176, 0, this.width, this.height, textureSize, textureSize);
            }
        }
    }

    //按下按钮后
    @Override
    public void onPress() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        BlockEntity te = PaiMeng.PROXY.getRefrencedTE();
        if (te instanceof StrengthenTableTile) {
            NetWorkHandler.INSTANCE.sendToServer(new StrengthenPacket(te.getBlockPos()));
        }
    }

    //绘制按钮提示文字
    public void renderToolTip(GuiGraphics graphics, int mouseX, int mouseY) {
        Font fontRenderer = Minecraft.getInstance().font;
        graphics.drawString(fontRenderer, Component.translatable("gui.button.strengthen").getString(), this.getX() + 2, this.getY() - 10, 0x696969);
    }
}
