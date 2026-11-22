package com.yuo.paimeng.client.screen;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.tiles.TileUtils;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.Random;

//烹饪界面
@OnlyIn(Dist.CLIENT)
public class CookingScreen extends Screen {
    private final ResourceLocation RESOURCE = ModUtils.fa( "textures/gui/cooking.png");
    private Button button;
    private static final int textureWidth = 202;
    private static final int textureHeight = 12;
    private final AbstractContainerScreen<?> screen; //容器
    private final int FOOD_RECIPE_LEVEL; //食品等级
    private double FOOD_RECEIPTING_POS = 0; //指针位置
    private boolean FOOD_FACING = true; //指针方向
    private final int minValue; //成功区间最小值
    private final int maxValue; //最大值
    private int count; //烹饪数量

    protected CookingScreen(Component titleIn, AbstractContainerScreen<?> screen, int lv, int count) {
        super(titleIn);
        this.FOOD_RECIPE_LEVEL = lv;
        this.count = count;
        this.screen = screen;
        Random random = new Random();
        this.minValue = random.nextInt(80 + FOOD_RECIPE_LEVEL * 20);
        this.maxValue = this.minValue + (120 - FOOD_RECIPE_LEVEL * 20);
    }

    @Override
    protected void init() {
        // 每次打开都是新界面，但保险起见先清空，避免控件叠加
        this.clearWidgets();
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
        int i = (this.width - textureWidth) / 2;
        int j = (this.height - textureHeight) / 2;
        graphics.blit(RESOURCE, i, j, 0, 0, textureWidth, textureHeight);
        graphics.blit(RESOURCE, i + minValue, j + 1, 0, 12, 120 - FOOD_RECIPE_LEVEL * 20, 10); //成功区间
        foodReceiptingTick(); //模拟指针移动
        graphics.blit(RESOURCE, i + (int) Math.floor(FOOD_RECEIPTING_POS), j - 5, 0, 22, 10, 18); //指针
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void onClose() {
        // 取消烹饪：直接回到容器界面，不再走 super.onClose()（那会把界面关成 null 再重开，
        // 还会通过 button.onClick 误触发一次烹饪并再次 init 父界面）
        if (this.minecraft != null) {
            this.minecraft.setScreen(this.screen);
        }
    }

    private void button(Button button) {
        if (FOOD_RECEIPTING_POS < minValue || FOOD_RECEIPTING_POS > maxValue) { //成功烹饪
            count = 0;
        }
        if (screen instanceof PotScreen pot) {
            BlockPos pos = pot.getMenu().getPos();
            TileUtils.sendPotPacket(count, FOOD_RECIPE_LEVEL, FOOD_RECIPE_LEVEL, pos); //发送数据包设置产出
        }
        if (screen instanceof BenchScreen bench) {
            BlockPos pos = bench.getMenu().getPos();

            TileUtils.sendPotPacket(count, FOOD_RECIPE_LEVEL, FOOD_RECIPE_LEVEL, pos);
        }
        if (minecraft != null) {
            minecraft.setScreen(screen);
        }
    }

    /** 指针位置更新 */
    private void foodReceiptingTick() {
        double facing = FOOD_FACING ? 1 : -1;
        switch (FOOD_RECIPE_LEVEL) {
            case 1:
                FOOD_RECEIPTING_POS += 0.4d * facing;
                break;
            case 2:
                FOOD_RECEIPTING_POS += 0.6d * facing;
                break;
            case 3:
                FOOD_RECEIPTING_POS += 0.9d * facing;
                break;
            case 4:
                FOOD_RECEIPTING_POS += 1.2d * facing;
                break;
            case 5:
                FOOD_RECEIPTING_POS += 1.5d * facing;
                break;
            default:
                FOOD_RECEIPTING_POS += 0.75f * facing;
                break;
        }
        if (FOOD_RECEIPTING_POS >= 200 || FOOD_RECEIPTING_POS <= 0) FOOD_FACING = !FOOD_FACING;
    }
}
