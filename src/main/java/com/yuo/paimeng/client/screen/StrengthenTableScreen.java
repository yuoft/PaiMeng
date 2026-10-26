package com.yuo.paimeng.client.screen;

import com.yuo.paimeng.menu.StrengthenTableContainer;
import com.yuo.paimeng.ModUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class StrengthenTableScreen extends AbstractContainerScreen<StrengthenTableContainer> {
    private final ResourceLocation RESOURCE = ModUtils.fa("textures/gui/strengthen_table.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;
    private StrengthenButton button; //按钮

    public StrengthenTableScreen(StrengthenTableContainer strengthenTableContainer, Inventory inv, Component titleIn) {
        super(strengthenTableContainer, inv, titleIn);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        this.titleLabelX = this.imageWidth / 2 + 55;
        this.inventoryLabelX += 135;
    }

    @Override
    protected void init() {
        super.init();
        //StrengthenButton 内部会补上 getGuiLeft()/getGuiTop()，这里传相对坐标
        this.button = new StrengthenButton(this, 87, 30, 22, 22);
        this.addRenderableWidget(button);
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
        if (menu.hasRightItem()) {
            graphics.drawString(this.font, "Exp:" + menu.getLeftExp() + "/" + menu.getRightExp(), 87, 15, 0x8B008B, false);
        }
        if (menu.hasItem()) {
            int level = menu.getLevelUp();
            if (level < 0) {
                graphics.drawString(this.font, "Level:+ Max", 87, 60, 0xff0000, false);
            } else graphics.drawString(this.font, "Level:+ " + level, 87, 60, 0xffffff, false);
        }
        super.renderLabels(graphics, x, y);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //切换按钮可用状态
        this.button.active = menu.hasItem();
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }
}
