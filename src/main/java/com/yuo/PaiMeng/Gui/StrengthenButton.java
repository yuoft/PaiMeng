package com.yuo.PaiMeng.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.NetWork.StrengthenPacket;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Tiles.StrengthenTableTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TranslationTextComponent;

public class StrengthenButton extends ImageButton {
    public static final ResourceLocation TEXTURE = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/strengthen_table.png");
    private final StrengthenTableScreen screen;
    private static final int ZERO = 0;
    private static final int textureSize = 256;

    public StrengthenButton(StrengthenTableScreen screen, int x, int y, int width, int height) {
        super(screen.getGuiLeft() + x, screen.getGuiTop() + y, width, height, ZERO, ZERO, ZERO
                , TEXTURE, Button::onPress);
        this.screen = screen;
    }
    @Override
    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        if (this.visible){
            if (this.isHovered && screen.getContainer().hasItem()){
                minecraft.getTextureManager().bindTexture(TEXTURE);
                RenderSystem.enableDepthTest();
                blit(matrixStack, this.x, this.y, 198, 0, this.width, this.height, textureSize, textureSize);
                this.renderToolTip(matrixStack, mouseX, mouseY);
            }else {
                minecraft.getTextureManager().bindTexture(TEXTURE);
                RenderSystem.enableDepthTest();
                blit(matrixStack, this.x, this.y, 176, 0, this.width, this.height, textureSize, textureSize);
            }
        }
    }

    //按下按钮后
    @Override
    public void onPress() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        TileEntity te = PaiMeng.PROXY.getRefrencedTE();
        if (te instanceof StrengthenTableTile){
            NetWorkHandler.INSTANCE.sendToServer(new StrengthenPacket(te.getPos()));
        }
    }

    @Override
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        fontRenderer.drawString(matrixStack, new TranslationTextComponent("gui.button.strengthen").getString(), x + 2, this.y - 10, 0x696969);
    }
}
