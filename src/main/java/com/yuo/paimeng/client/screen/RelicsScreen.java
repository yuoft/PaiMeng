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
    /** The old y position of the mouse pointer */
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
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(RESOURCE, i, j, 0, 0, imageWidth, imageHeight);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawEntityOnScreen(graphics, i + 81, j + 75, 30, (float) (i + 51) - this.oldMouseX, (float) (j + 75 - 50) - this.oldMouseY, this.minecraft.player);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        if (this.minecraft != null && this.minecraft.player != null) {
//            graphics.drawString(this.font, "玩家：" + this.minecraft.player.getName().getString(), 110, 12, 0x696969);
            //显示圣遗物属性
            int i = 0;
            for (RelicsHelper.RandomAttrValue value : RelicsHelper.getRelicsData(this.minecraft.player)) {
                if (!value.isEmpty()) {
                    graphics.drawString(this.font, value.getAttrType().getAttrName() + getMath(value.getBaseValue()), 110, 12 + i * 8, 0x696969, false);
                    i++;
                }
            }
        }
        super.renderLabels(graphics, x, y);
    }

    private String getMath(double a) {
        if (a < 1) {
            BigDecimal bd = new BigDecimal(a * 100).setScale(2, RoundingMode.HALF_EVEN);
            return bd.toString() + "%";
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

    //渲染玩家
    public static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity livingEntity) {
        drawEntityOnScreen(new GuiGraphics(Minecraft.getInstance(), Minecraft.getInstance().renderBuffers().bufferSource()),
                posX, posY, scale, mouseX, mouseY, livingEntity);
    }

    //渲染玩家（1.20.1 图形上下文版本）
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
