package com.yuo.paimeng.client.screen;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.menu.AbstractCookingContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractCookingScreen<T extends AbstractCookingContainer<?>> extends AbstractContainerScreen<T> {
    protected final ResourceLocation RESOURCE = ModUtils.fa("textures/gui/bench.png");
    protected final int textureWidth = 176;
    protected final int textureHeight = 166;
    protected Button button;
    protected Button buttonCount;

    public AbstractCookingScreen(T menu, Inventory inv, Component titleIn) {
        super(menu, inv, titleIn);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        this.titleLabelX = this.imageWidth / 2 - 14;
    }

    // 🔴 由子类提供 UI 上的坐标偏移
    protected abstract int getExpBarX();
    protected abstract int getLevelTextX();

    @Override
    protected void init() {
        super.init();
        this.button = Button.builder(Component.translatable("gui.paimeng.cooking_button"), this::button)
                .bounds(this.leftPos + 75, this.topPos + 58, 30, 20).build();
        this.buttonCount = Button.builder(Component.translatable("gui.paimeng.cooking_button_count"), this::buttonCount)
                .bounds(this.leftPos + 110, this.topPos + 58, 30, 20).build();
        this.addRenderableWidget(button);
        this.addRenderableWidget(buttonCount);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        int i = this.leftPos;
        int j = this.topPos;
        graphics.blit(RESOURCE, i, j, 0, 0, imageWidth, imageHeight);
        
        // 绘制燃烧进度
        int burnTime = this.menu.getBurnTime();
        if (burnTime > 0) {
            graphics.blit(RESOURCE, i + 65, j + 36 + 14 - burnTime, 176, 14 - burnTime, 14, burnTime);
        }
        
        // 无配方叉号
        if (!this.menu.canRecipe()) {
            graphics.blit(RESOURCE, i + 90, j + 35, 190, 0, 22, 15);
        }
        
        // 绘制经验条
        int exp = this.menu.getExp();
        int expBarX = getExpBarX();
        graphics.blit(RESOURCE, i + expBarX, j + 8, 176, 15, 51, 5);
        graphics.blit(RESOURCE, i + expBarX, j + 8, 176, 20, 1 + exp, 5);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        // 绘制时间与等级文本
        graphics.drawString(this.font, Integer.toString(this.menu.getTime()), 65, 25, 0x696969, false);
        graphics.drawString(this.font, Integer.toString(this.menu.getFoodRecipesLevel()), getLevelTextX(), 6, 0x696969, false);
        super.renderLabels(graphics, x, y);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // 动态控制按钮是否可用
        this.button.active = this.menu.canRecipe();
        this.buttonCount.active = this.menu.canRecipe() && this.menu.getFoodRecipesLevel() >= this.menu.getFoodLevel() && this.menu.getItemMaxCount() > 1;
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    /** 按钮点击：单次烹饪 */
    protected void button(Button button) {
        if (this.minecraft != null && this.menu.canRecipe()) {
            this.minecraft.setScreen(new CookingScreen(Component.translatable("gui.paimeng.cooking"), this, this.menu.getFoodLevel(), 1));
        }
    }

    /** 按钮点击：批量烹饪 */
    protected void buttonCount(Button button) {
        if (this.minecraft != null && this.menu.canRecipe()) {
            this.minecraft.setScreen(new CookingScreen(Component.translatable("gui.paimeng.cooking"), this, this.menu.getFoodLevel(), this.menu.getItemMaxCount()));
        }
    }
}