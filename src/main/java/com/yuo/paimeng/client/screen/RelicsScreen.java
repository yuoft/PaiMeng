package com.yuo.paimeng.client.screen;

import com.yuo.paimeng.menu.RelicsContainer;
import com.yuo.paimeng.items.RelicsHelper;
import com.yuo.paimeng.ModUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.math.BigDecimal;
import java.math.RoundingMode;

@OnlyIn(Dist.CLIENT)
public class RelicsScreen extends AbstractContainerScreen<RelicsContainer> {
    private final ResourceLocation RESOURCE = ModUtils.fa( "textures/gui/relics.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;
    private float oldMouseX;
    private float oldMouseY;

    public RelicsScreen(RelicsContainer relicsContainer, Inventory inv, Component titleIn) {
        super(relicsContainer, inv, titleIn);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        this.titleLabelX = this.imageWidth - 65;
        this.titleLabelY = 3;
    }

    @Override
    protected void init() {
        super.init();
        //关闭按钮
        RelicsButton button = new RelicsButton(this, this.leftPos + 157, this.topPos + 63, 16, 16); //回调
        this.addRenderableWidget(button);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F); 
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(RESOURCE, i, j, 0, 0, imageWidth, imageHeight);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawEntityOnScreen(graphics, i + 81, j + 75, 30, (float) (i + 51) - this.oldMouseX, (float) (j + 75 - 50) - this.oldMouseY, this.minecraft.player);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        if (this.minecraft != null && this.minecraft.player != null) {
            int i = 0;
            for (RelicsHelper.RandomAttrValue value : RelicsHelper.getRelicsData(this.minecraft.player)) {
                if (!value.isEmpty()) {
                    int startX = 110;
                    int startY = 12 + i * 8; // 稍微增加了行距，从 8 改为 10，看起来不那么拥挤

                    String nameText =value.getAttrType().getAttrName() + ": ";
                    graphics.drawString(this.font, nameText, startX, startY, 0x696969, false);
                    int nameWidth = this.font.width(nameText);
                    float scale = 0.85f; // 缩放比例：0.8f 为原大小的 80%
                    
                    graphics.pose().pushPose(); // 保存当前坐标系
                    // 平移坐标系到属性名右侧 (+1.5f 是为了微调垂直对齐)
                    graphics.pose().translate(startX + nameWidth, startY + 1.5f, 0);
                    graphics.pose().scale(scale, scale, 1.0f);
                    
                    String valueText = getMath(value.getBaseValue());
                    graphics.drawString(this.font, valueText, 0, 0, 0x8B008B, false);
                    graphics.pose().popPose();
                    i++;
                }
            }
        }
        super.renderLabels(graphics, x, y);
    }

    private String getMath(double a) {
        if (a < 1) {
            BigDecimal bd = new BigDecimal(a * 100).setScale(2, RoundingMode.HALF_EVEN);
            return bd + "%";
        }
        return new BigDecimal(a).setScale(2, RoundingMode.HALF_EVEN).toString();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
        this.oldMouseX = (float) mouseX;
        this.oldMouseY = (float) mouseY;
    }

    public static void drawEntityOnScreen(GuiGraphics graphics, int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity livingEntity) {
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, posX, posY, scale, mouseX, mouseY, livingEntity);
    }

    @Override
    public void onClose() {
        if (minecraft != null && minecraft.player != null) {
            super.onClose();
        }
    }
}