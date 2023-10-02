package com.yuo.PaiMeng.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yuo.PaiMeng.Container.BenchContainer;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

public class BenchScreen extends ContainerScreen<BenchContainer> {
    private final ResourceLocation RESOURCE = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/pot.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;
    private Button button; //按钮
    private Button buttonCount;

    public BenchScreen(BenchContainer potContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(potContainer, inv, titleIn);
        this.xSize = textureWidth;
        this.ySize = textureHeight;
        this.titleX = this.xSize / 2 - 14;
    }

    @Override
    protected void init() {
        this.button = new Button((this.width - this.xSize) / 2 + 75, (this.height - this.ySize) / 2 + 58, 30, 20,
                new TranslationTextComponent("gui.paimeng.cooking_button"), this::button); //回调
        this.buttonCount = new Button((this.width - this.xSize) / 2 + 110, (this.height - this.ySize) / 2 + 58, 30, 20,
                new TranslationTextComponent("gui.paimeng.cooking_button_count"), this::buttonCount); //回调
        this.addButton(button);
        this.addButton(buttonCount);
        super.init();
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        this.minecraft.getTextureManager().bindTexture(RESOURCE);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        blit(matrixStack, i, j, 0, 0, xSize, ySize);
        int burnTime = this.container.getBurnTime();
        if (burnTime > 0) {
            blit(matrixStack, i + 65, j + 36 + 14 - burnTime, 176, 14 - burnTime, 14, burnTime);
        }
        if (!container.canRecipe()) { //叉号
            blit(matrixStack, i + 90, j + 35, 190, 0, 22, 15);
        }
        blit(matrixStack, i + 105, j + 9, 176, 15, 51, 5);
        int exp = container.getExp();
        blit(matrixStack, i + 105, j + 9, 176, 20, 1 + exp, 5);
    }

    @Override
    protected void drawGuiContainerForegroundLayer(MatrixStack matrixStack, int x, int y) {
        this.font.drawString(matrixStack, Integer.toString(this.container.getTime()), 65, 25, 0x696969);
        this.font.drawString(matrixStack, Integer.toString(this.container.getFoodRecipesLevel()), 98, 8, 0x696969);
        super.drawGuiContainerForegroundLayer(matrixStack, x, y);
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.button.render(matrixStack, mouseX, mouseY, partialTicks);
        this.buttonCount.render(matrixStack, mouseX, mouseY, partialTicks);
        //切换按钮可用状态
        this.button.active = container.canRecipe();
        this.buttonCount.active = container.canRecipe() && container.getFoodRecipesLevel() >= container.getFoodLevel() && container.getItemMaxCount() > 1;
        this.renderHoveredTooltip(matrixStack, mouseX, mouseY);
    }

    //按下按钮后
    private void button(Button button) {
        if (minecraft != null) {
            if (container.canRecipe())
                minecraft.displayGuiScreen(new CookingScreen(new TranslationTextComponent("gui.paimeng.cooking"), this, container.getFoodLevel(), 1));
        }
    }

    private void buttonCount(Button button) {
        if (minecraft != null) {
            if (container.canRecipe())
                minecraft.displayGuiScreen(new CookingScreen(new TranslationTextComponent("gui.paimeng.cooking"), this, container.getFoodLevel(), container.getItemMaxCount()));
        }
    }

}
