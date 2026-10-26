package com.yuo.PaiMeng.Client.Gui;

import com.yuo.PaiMeng.Container.BenchContainer;
import com.yuo.PaiMeng.ModUtils;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BenchScreen extends AbstractContainerScreen<BenchContainer> {
    private final ResourceLocation RESOURCE = ModUtils.fa("textures/gui/pot.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;
    private Button button; //按钮
    private Button buttonCount;

    public BenchScreen(BenchContainer potContainer, Inventory inv, Component titleIn) {
        super(potContainer, inv, titleIn);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        this.titleLabelX = this.imageWidth / 2 - 14;
    }

    @Override
    protected void init() {
        super.init();
        this.button = Button.builder(Component.translatable("gui.paimeng.cooking_button"), this::button)
                .bounds(this.leftPos + 75, this.topPos + 58, 30, 20).build(); //回调
        this.buttonCount = Button.builder(Component.translatable("gui.paimeng.cooking_button_count"), this::buttonCount)
                .bounds(this.leftPos + 110, this.topPos + 58, 30, 20).build(); //回调
        this.addRenderableWidget(button);
        this.addRenderableWidget(buttonCount);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(RESOURCE, i, j, 0, 0, imageWidth, imageHeight);
        int burnTime = this.menu.getBurnTime();
        if (burnTime > 0) {
            graphics.blit(RESOURCE, i + 65, j + 36 + 14 - burnTime, 176, 14 - burnTime, 14, burnTime);
        }
        if (!menu.canRecipe()) { //叉号
            graphics.blit(RESOURCE, i + 90, j + 35, 190, 0, 22, 15);
        }
        graphics.blit(RESOURCE, i + 105, j + 9, 176, 15, 51, 5);
        int exp = menu.getExp();
        graphics.blit(RESOURCE, i + 105, j + 9, 176, 20, 1 + exp, 5);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        graphics.drawString(this.font, Integer.toString(this.menu.getTime()), 65, 25, 0x696969);
        graphics.drawString(this.font, Integer.toString(this.menu.getFoodRecipesLevel()), 98, 8, 0x696969);
        super.renderLabels(graphics, x, y);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        //切换按钮可用状态
        this.button.active = menu.canRecipe();
        this.buttonCount.active = menu.canRecipe() && menu.getFoodRecipesLevel() >= menu.getFoodLevel() && menu.getItemMaxCount() > 1;
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    //按下按钮后
    private void button(Button button) {
        if (minecraft != null) {
            if (menu.canRecipe())
                minecraft.setScreen(new CookingScreen(Component.translatable("gui.paimeng.cooking"), this, menu.getFoodLevel(), 1));
        }
    }

    private void buttonCount(Button button) {
        if (minecraft != null) {
            if (menu.canRecipe())
                minecraft.setScreen(new CookingScreen(Component.translatable("gui.paimeng.cooking"), this, menu.getFoodLevel(), menu.getItemMaxCount()));
        }
    }

}
