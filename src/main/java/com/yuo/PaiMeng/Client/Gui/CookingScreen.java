package com.yuo.PaiMeng.Client.Gui;

import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Tiles.TileUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

//烹饪界面
@OnlyIn(Dist.CLIENT)
public class CookingScreen extends Screen {
    private final ResourceLocation RESOURCE = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/cooking.png");
    private Button button;
    private final int textureWidth = 202;
    private final int textureHeight = 12;
    private AbstractContainerScreen<?> screen; //容器
    private final int LEVEL; //食品等级
    private double LEVEL_GROW = 0; //指针速度
    private int minValue; //成功区间最小值
    private int maxValue; //最大值
    private final Random random = new Random();
    private int count;

    protected CookingScreen(Component titleIn, AbstractContainerScreen<?> screen, int lv, int count) {
        super(titleIn);
        this.LEVEL = lv;
        this.count = count;
        this.screen = screen;
        this.minValue = random.nextInt(80 + LEVEL * 20);
        this.maxValue = this.minValue + (120 - LEVEL * 20);
    }

    @Override
    protected void init() {
        this.button = Button.builder(Component.translatable("gui.paimeng.cooking_button"), this::button)
                .bounds(this.width / 2 - 15, this.height / 2 + 20, 30, 20).build(); //回调
        this.addRenderableWidget(button);
        super.init();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        int i = (this.width - this.textureWidth) / 2;
        int j = (this.height - this.textureHeight) / 2;
        graphics.blit(RESOURCE, i, j, 0, 0, textureWidth, textureHeight);
        graphics.blit(RESOURCE, i + minValue, j + 1, 0, 12, 120 - LEVEL * 20, 10); //成功区间
        if (LEVEL_GROW <= 200) {
            setLEVEL_GROW(LEVEL); //模拟指针移动
            graphics.blit(RESOURCE, i + (int) Math.floor(LEVEL_GROW), j - 5, 0, 22, 10, 18); //指针
        }
        if (LEVEL_GROW > 200) this.button.onClick(0, 0);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private void button(Button button) {
        if (LEVEL_GROW < minValue || LEVEL_GROW > maxValue) { //成功烹饪
            count = 0;
        }
        if (screen instanceof PotScreen) {
            TileUtils.sendPotPacket(count, LEVEL, LEVEL); //发送数据包设置产出
        }
        if (screen instanceof BenchScreen) {
            TileUtils.sendPotPacket(count, LEVEL, LEVEL);
        }
        if (minecraft != null) {
            minecraft.setScreen(screen);
        }
    }

    //设置指针速度
    private void setLEVEL_GROW(int lv) {
        switch (lv) {
            case 1:
                LEVEL_GROW += 0.4d;
                break;
            case 2:
                LEVEL_GROW += 0.6d;
                break;
            case 3:
                LEVEL_GROW += 0.9d;
                break;
            case 4:
                LEVEL_GROW += 1.2d;
                break;
            case 5:
                LEVEL_GROW += 1.5d;
                break;
            default:
                LEVEL_GROW += 1d;
                break;
        }
    }
}
