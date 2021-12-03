package com.yuo.PaiMeng.Gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Random;

//烹饪界面
public class CookingScreen extends Screen {
    private final ResourceLocation RESOURCE = new ResourceLocation(PaiMeng.MOD_ID, "textures/gui/cooking.png");
    private Button button;
    private final int textureWidth = 202;
    private final int textureHeight = 12;
    private final Container container; //容器
    private final int LEVEL; //食品等级
    private int LEVEL_GROW; //指针速度
    private int minValue;
    private int maxValue;
    private Random random = new Random();

    protected CookingScreen(ITextComponent titleIn, Container container, int lv) {
        super(titleIn);
        this.LEVEL = lv;
        this.LEVEL_GROW = lv;
        this.container = container;
    }

//    public CookingScreen(Container screenContainer, PlayerInventory inv, int LEVEL) {
//        super(screenContainer, inv, new TranslationTextComponent("111"));
//        this.xSize = textureWidth;
//        this.ySize = textureHeight;
//        this.LEVEL = LEVEL;
//        this.minValue = random.nextInt(180);
//        this.maxValue = minValue + 20;
//    }


    @Override
    protected void init() {
        this.button = new Button(this.width / 2 -15, this.height / 2 + 20, 30, 20,
                new TranslationTextComponent("gui.paimeng.pot_button"), this::button); //回调
        this.addButton(button);
        super.init();
    }

//    @Override
//    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
//        this.renderBackground(matrixStack);
//        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
//        this.minecraft.getTextureManager().bindTexture(RESOURCE);
//        int i = (width - textureWidth) / 2;
//        int j = (height - textureHeight) / 2;
//        this.blit(matrixStack, i, j, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
//        this.blit(matrixStack, i + LEVEL, j - 8,0,12,120 - (LEVEL * 20),18,10,18);
//        super.render(matrixStack, mouseX, mouseY, partialTicks);
//    }

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
        LEVEL_GROW += LEVEL;
        blit(matrixStack, i, j, 0, 0, textureWidth, textureHeight);
        blit(matrixStack, i + minValue, j + 1, 0,12, 120 - LEVEL * 20, 10); //成功区间
        blit(matrixStack, i + LEVEL_GROW, j - 5, 0, 22, 10, 18); //指针
        super.renderBackground(matrixStack);
    }

    private void button(Button button){
        if (container instanceof PotContainer)
            ((PotContainer) container).setFood(new ItemStack(ItemRegistry.chicken.get()));
        this.closeScreen();
    }
}
