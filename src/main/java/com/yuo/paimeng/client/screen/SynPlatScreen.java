package com.yuo.paimeng.client.screen;

import com.yuo.paimeng.menu.SynPlatContainer;
import com.yuo.paimeng.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SynPlatScreen extends AbstractContainerScreen<SynPlatContainer> {
    private final ResourceLocation RESOURCE = ModUtils.fa( "textures/gui/synthetic_platform.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;

    public SynPlatScreen(SynPlatContainer potContainer, Inventory inv, Component titleIn) {
        super(potContainer, inv, titleIn);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        this.titleLabelX = this.imageWidth / 2 - 14;
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(RESOURCE, i, j, 0, 0, imageWidth, imageHeight);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        super.renderLabels(graphics, x, y);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

}
