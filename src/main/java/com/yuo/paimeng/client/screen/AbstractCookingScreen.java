package com.yuo.paimeng.client.screen;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.menu.AbstractCookingContainer;
import com.yuo.paimeng.menu.SuperBenchContainer;
import com.yuo.paimeng.tiles.TileUtils;
import net.minecraft.client.gui.GuiGraphics;
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
    protected CookingButton button;
    protected CookingButtonCount buttonCount;

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
        // 从烹饪小游戏返回时 Minecraft 会再次调用 init()，但不会清空旧控件，这里先清一次
        this.clearWidgets();
        this.button = new CookingButton(this, this.leftPos + 75, this.topPos + 58, 30, 20,
                Component.translatable("gui.paimeng.cooking_button"));
        this.buttonCount = new CookingButtonCount(this, this.leftPos + 110, this.topPos + 58, 30, 20,
                Component.translatable("gui.paimeng.cooking_button_count"));
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
        graphics.drawString(this.font, this.menu instanceof SuperBenchContainer ? "∞" : Integer.toString(this.menu.getTime()), 65, 25, 0x696969, false);
        graphics.drawString(this.font, Integer.toString(this.menu.getFoodRecipesLevel()), getLevelTextX(), 6, 0x696969, false);
        super.renderLabels(graphics, x, y);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        // 按钮的可用状态由各自的按钮类在 renderWidget 里计算
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
    }

    /**
     * 执行烹饪：高级烹饪台直接发包给服务端，普通锅/工作台打开旋盘小游戏。
     *
     * @param count 烹饪份数（1 = 单次，>1 = 批量）
     */
    public void cook(int count) {
        if (this.minecraft == null || !this.menu.canRecipe()) return;
        int level = this.menu.getFoodLevel();
        if (this.menu instanceof SuperBenchContainer) {
            TileUtils.sendPotPacket(count, level, level, this.menu.getPos());
        } else {
            this.minecraft.setScreen(new CookingScreen(Component.translatable("gui.paimeng.cooking"), this, level, count));
        }
    }
}
