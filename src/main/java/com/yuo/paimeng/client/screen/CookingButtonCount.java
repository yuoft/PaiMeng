package com.yuo.paimeng.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * 「批量」按钮：按材料数量一次烹饪多份，回调内置。
 *
 * <p>贴图行与 {@link CookingButton} 一致：46=禁用、66=普通（黑边）、86=悬停（白边），
 * 只用鼠标悬停判断，不用焦点，避免点击后白边残留。
 */
public class CookingButtonCount extends Button {

    private final AbstractCookingScreen<?> screen;

    public CookingButtonCount(AbstractCookingScreen<?> screen, int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message, button -> {
        }, Button.DEFAULT_NARRATION);
        this.screen = screen;
    }

    @Override
    public void onPress() {
        if (!canBatchCook()) return;
        this.screen.cook(batchCount());
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.active = canBatchCook();

        int row = !this.active ? 46 : (this.isHovered ? 86 : 66);
        graphics.setColor(1.0F, 1.0F, 1.0F, this.alpha);
        RenderSystem.enableBlend();
        RenderSystem.enableDepthTest();
        graphics.blitNineSliced(WIDGETS_LOCATION, this.getX(), this.getY(), this.getWidth(), this.getHeight(),
                20, 4, 200, 20, 0, row);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);

        int color = this.active ? 0xFFFFFF : 0xA0A0A0;
        this.renderString(graphics, Minecraft.getInstance().font, color | ((int) (this.alpha * 255.0F) << 24));
    }

    /** 是否满足批量烹饪条件（「批量」按钮的可用条件）。 */
    public boolean canBatchCook() {
        return this.screen.getMenu().canRecipe()
                && this.screen.getMenu().getFoodRecipesLevel() >= this.screen.getMenu().getFoodLevel()
                && batchCount() > 1;
    }

    /** 批量烹饪的数量。 */
    public int batchCount() {
        return this.screen.getMenu().getItemMaxCount();
    }
}
