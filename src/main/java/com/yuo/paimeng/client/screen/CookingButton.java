package com.yuo.paimeng.client.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

/**
 * 「制作」按钮（单次烹饪），回调内置。
 *
 * <p>外观说明：原版 {@link Button} 的三行贴图是 46=禁用、66=普通（黑边）、86=悬停（白边），
 * 其中“悬停”行同时被<b>焦点</b>触发。点击后控件会一直持有焦点，鼠标移开也保持白边，
 * 看起来就像“按下去没弹起来”。这里只按鼠标是否悬停选行，不受焦点影响。
 */
public class CookingButton extends Button {

    private final AbstractCookingScreen<?> screen;

    public CookingButton(AbstractCookingScreen<?> screen, int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message, button -> {
        }, Button.DEFAULT_NARRATION);
        this.screen = screen;
    }

    @Override
    public void onPress() {
        // 有配方才烹饪；setFocused(false) / isHovered=false 在这里都没用：
        // 前者只在键盘输入下影响外观，后者每帧会被 AbstractWidget.render 按鼠标位置重算。
        this.screen.cook(1);
    }

    @Override
    protected void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        this.active = this.screen.getMenu().canRecipe();

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
}
