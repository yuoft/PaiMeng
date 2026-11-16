package com.yuo.paimeng.client.screen;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.network.NetWorkHandler;
import com.yuo.paimeng.network.StrengthenPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
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
        // 直接从 Menu 获取坐标
        BlockPos pos = screen.getMenu().getBlockPos();
        if (pos != null) {
            NetWorkHandler.INSTANCE.sendToServer(new StrengthenPacket(pos));
        }
    }

    //绘制按钮提示文字
    public void renderToolTip(GuiGraphics graphics, int mouseX, int mouseY) {
        Font fontRenderer = Minecraft.getInstance().font;
        graphics.drawString(fontRenderer, Component.translatable("gui.strengthen.button").getString(), this.getX() + 2, this.getY() - 10, 0x696969, false);
    }
}
