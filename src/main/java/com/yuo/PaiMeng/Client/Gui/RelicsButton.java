package com.yuo.PaiMeng.Client.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.NetWork.RelicsGuiPacket;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.button.ImageButton;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class RelicsButton extends ImageButton {
    public static final ResourceLocation RESOURCE_OPEN = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/open.png");
    public static final ResourceLocation RESOURCE_CLOSE = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/close.png");
    private final ContainerScreen playerScreen;
    private static final int ZERO = 0;
    private static final int textureX = 16;
    private static final int textureY = 16;
    private final int guiX;
    private final int guiY;

    public RelicsButton(InventoryScreen screen, int x, int y, int width, int height) {
        super(screen.getGuiLeft() + x, screen.getGuiTop() + y, width, height, ZERO, ZERO, ZERO
                , RESOURCE_OPEN, textureX,textureY, Button::onPress,Button::renderToolTip, StringTextComponent.EMPTY);
        this.playerScreen = screen;
        this.guiX = x;
        this.guiY = y;
    }

    public RelicsButton(RelicsScreen screen, int x, int y, int width, int height) {
        super(x, y, width, height, ZERO, ZERO, ZERO, RESOURCE_OPEN, textureX,textureY, Button::onPress,Button::renderToolTip, StringTextComponent.EMPTY);
        this.playerScreen = screen;
        this.guiX = x;
        this.guiY = y;
    }

    @Override
    public void renderWidget(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        Minecraft minecraft = Minecraft.getInstance();
        if (!minecraft.player.isCreative() && this.visible){
            if (playerScreen instanceof InventoryScreen){
                InventoryScreen screen = (InventoryScreen) playerScreen;
                boolean visible = screen.getRecipeGui().isVisible();
                if (visible){ //如果玩家打开配方书 重设按钮位置
                    this.x = screen.getGuiLeft() + guiX;
                    this.y = screen.getGuiTop() + guiY;
                }else {
                    this.x = screen.getGuiLeft() + guiX;
                    this.y = screen.getGuiTop() + guiY;
                }
            }
            if (this.isHovered){
                minecraft.getTextureManager().bindTexture(RESOURCE_CLOSE);
                RenderSystem.enableDepthTest();
                blit(matrixStack, this.x, this.y, 0, 0, this.width, this.height, 16, 16);
                this.renderToolTip(matrixStack, mouseX, mouseY);
            }else {
                minecraft.getTextureManager().bindTexture(RESOURCE_OPEN);
                RenderSystem.enableDepthTest();
                blit(matrixStack, this.x, this.y, 0, 0, this.width, this.height, 16, 16);
            }
        }
    }

    //按下按钮后
    @Override
    public void onPress() {
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;
        if (playerScreen instanceof  InventoryScreen) {
            NetWorkHandler.INSTANCE.sendToServer(new RelicsGuiPacket(true));
        }else {
            NetWorkHandler.INSTANCE.sendToServer(new RelicsGuiPacket(false));
            this.displayNormalInventory(mc.player);
        }
    }

    @Override
    public void renderToolTip(MatrixStack matrixStack, int mouseX, int mouseY) {
//        super.renderToolTip(matrixStack, mouseX, mouseY);
        FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;
        if (playerScreen instanceof InventoryScreen){
            fontRenderer.drawString(matrixStack, new TranslationTextComponent("gui.button.relics_open").getString(), x, this.y - 8, 0xffffff);
        }else
        fontRenderer.drawString(matrixStack, new TranslationTextComponent("gui.button.relics_close").getString(), x, this.y - 8, 0x696969);
    }

    //切换回玩家背包
    public void displayNormalInventory(PlayerEntity player) {
        InventoryScreen gui = new InventoryScreen(player);
        Minecraft.getInstance().displayGuiScreen(gui);
    }
}
