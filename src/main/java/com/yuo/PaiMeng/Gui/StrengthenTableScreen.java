package com.yuo.PaiMeng.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yuo.PaiMeng.Container.StrengthenTableContainer;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class StrengthenTableScreen extends ContainerScreen<StrengthenTableContainer> {
    private final ResourceLocation RESOURCE = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/strengthen_table.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;
    private StrengthenButton button; //按钮

    public StrengthenTableScreen(StrengthenTableContainer strengthenTableContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(strengthenTableContainer, inv, titleIn);
        this.xSize = textureWidth;
        this.ySize = textureHeight;
        this.titleX = this.xSize / 2 + 55;
        this.playerInventoryTitleX += 135;
    }

    @Override
    protected void init() {
        this.button = new StrengthenButton(this, (this.width - this.xSize) / 2 + 87,(this.height - this.ySize) / 2 + 30,
                22, 22);
        this.addButton(button);
        super.init();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        this.minecraft.getTextureManager().bindTexture(RESOURCE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        blit(matrixStack, i, j, 0, 0, xSize, ySize);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        if (container.hasRightItem()){
            font.drawString(matrixStack, "Exp:" + container.getLeftExp() + "/" + container.getRightExp(), 87,15, 0x8B008B);
        }
        if (container.hasItem()){
            int level = container.getLevelUp();
            if (level < 0){
                font.drawString(matrixStack, "Level:+ Max", 87, 60, 0xff0000);
            } else font.drawString(matrixStack, "Level:+ " + level, 87, 60, 0xffffff);
        }
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.button.render(matrixStack, mouseX, mouseY, partialTicks);
        //切换按钮可用状态
        this.button.active = container.hasItem();
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }
}
