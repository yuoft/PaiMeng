package com.yuo.PaiMeng.Client.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Tiles.TileUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

//烹饪界面
public class CookingScreen extends Screen {
    private final ResourceLocation RESOURCE = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/cooking.png");
    private Button button;
    private final int textureWidth = 202;
    private final int textureHeight = 12;
    private ContainerScreen<?> screen; //容器
    private final int LEVEL; //食品等级
    private double LEVEL_GROW = 0; //指针速度
    private int minValue; //成功区间最小值
    private int maxValue; //最大值
    private final Random random = new Random();
    private int count;

    protected CookingScreen(ITextComponent titleIn, ContainerScreen<?> screen, int lv, int count) {
        super(titleIn);
        this.LEVEL = lv;
        this.count = count;
        this.screen = screen;
        this.minValue = random.nextInt(80 + LEVEL * 20);
        this.maxValue = this.minValue + (120 - LEVEL * 20);
    }

    @Override
    protected void init() {
        this.button = new Button(this.width / 2 - 15, this.height / 2 + 20, 30, 20,
                new TranslationTextComponent("gui.paimeng.cooking_button"), this::button); //回调
        this.addButton(button);
        super.init();
    }

    @Override
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        this.button.render(matrixStack, mouseX, mouseY, partialTicks);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(MatrixStack matrixStack) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F); //确保颜色正常
        this.minecraft.getTextureManager().bindTexture(RESOURCE);
        int i = (this.width - this.textureWidth) / 2;
        int j = (this.height - this.textureHeight) / 2;
        blit(matrixStack, i, j, 0, 0, textureWidth, textureHeight);
        blit(matrixStack, i + minValue, j + 1, 0,12, 120 - LEVEL * 20, 10); //成功区间
        if (LEVEL_GROW <= 200) {
            setLEVEL_GROW(LEVEL); //模拟指针移动
            blit(matrixStack, i + (int) Math.floor(LEVEL_GROW), j - 5, 0, 22, 10, 18); //指针
        }
        if (LEVEL_GROW > 200) this.button.onClick(0,0);
    }

    private void button(Button button){
        if (LEVEL_GROW < minValue || LEVEL_GROW > maxValue){ //成功烹饪
            count = 0;
        }
        if (screen instanceof PotScreen){
            TileUtils.sendPotPacket(count, LEVEL, LEVEL);//发送数据包设置产出
        }
        if (screen instanceof BenchScreen){
            TileUtils.sendPotPacket(count, LEVEL, LEVEL);
        }
        if (minecraft != null){
            minecraft.displayGuiScreen(screen);
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
