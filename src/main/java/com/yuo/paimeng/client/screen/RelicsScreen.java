package com.yuo.paimeng.client.screen;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.items.RelicsHelper;
import com.yuo.paimeng.menu.RelicsContainer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.math.BigDecimal;
import java.math.RoundingMode;

@OnlyIn(Dist.CLIENT)
public class RelicsScreen extends AbstractContainerScreen<RelicsContainer> {
    private final ResourceLocation RESOURCE = ModUtils.fa( "textures/gui/relics.png");
    private final int textureWidth = 176;
    private final int textureHeight = 166;
    private float oldMouseX;
    private float oldMouseY;
    // 🔴 左侧属性面板参数
    private static final int LEFT_PANEL_WIDTH = 120;
    private static final int LEFT_PANEL_GAP = 10; // 两界面之间的间距

    public RelicsScreen(RelicsContainer relicsContainer, Inventory inv, Component titleIn) {
        super(relicsContainer, inv, titleIn);
        this.imageWidth = textureWidth;
        this.imageHeight = textureHeight;
        this.titleLabelX = this.imageWidth - 65;
        this.titleLabelY = 5;
    }

    @Override
    protected void init() {
        super.init();
        //关闭按钮
        RelicsButton button = new RelicsButton(this, this.leftPos + 157, this.topPos + 63, 16, 16); //回调
        this.addRenderableWidget(button);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int x, int y) {
        int i = this.leftPos;
        int j = this.topPos;

        // 🔴 1. 在左侧绘制属性面板背景
        int leftX = i - LEFT_PANEL_WIDTH - LEFT_PANEL_GAP;
        if (leftX > 0) { // 确保屏幕宽度足够
            graphics.fill(leftX, j, leftX + LEFT_PANEL_WIDTH, j + imageHeight, 0xAA000000);
            graphics.renderOutline(leftX, j, LEFT_PANEL_WIDTH, imageHeight, 0xFF888888);
        }

        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(RESOURCE, i, j, 0, 0, imageWidth, imageHeight);
        graphics.setColor(1.0F, 1.0F, 1.0F, 1.0F);
        drawEntityOnScreen(graphics, i + 81, j + 75, 30, (float) (i + 51) - this.oldMouseX, (float) (j + 75 - 50) - this.oldMouseY, this.minecraft.player);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int x, int y) {
        if (this.minecraft == null || this.minecraft.player == null) return;
        Player player = this.minecraft.player;

        // 🔴 调用独立的左侧属性绘制方法
        renderLeftAttributePanel(graphics, player);

        // 渲染右侧圣遗物加成属性
        int r = 0;
        for (RelicsHelper.RandomAttrValue value : RelicsHelper.getRelicsData(this.minecraft.player)) {
            if (!value.isEmpty()) {
                int startX = 110;
                int startY = 14 + r * 8;

                String nameText = value.getAttrType().getAttrName() + ": ";
                graphics.drawString(this.font, nameText, startX, startY, 0x696969, false);
                int nameWidth = this.font.width(nameText);
                float scale = 0.85f; //缩小数字

                graphics.pose().pushPose();
                graphics.pose().translate(startX + nameWidth, startY + 1.5f, 0);
                graphics.pose().scale(scale, scale, 1.0f);

                String valueText = formatValue(value.getBaseValue(), value.getAttrType().isRate());
                graphics.drawString(this.font, valueText, 0, 0, 0x8B008B, false);
                graphics.pose().popPose();
                r++;
            }
        }

        super.renderLabels(graphics, x, y);
    }

    /**
     * 独立的左侧总属性渲染方法
     */
    private void renderLeftAttributePanel(GuiGraphics graphics, Player player) {
        int leftX = this.leftPos - LEFT_PANEL_WIDTH - LEFT_PANEL_GAP;
        if (leftX <= 0) return;

        int baseX = -LEFT_PANEL_WIDTH - LEFT_PANEL_GAP;
        int baseY = 5;
        int lineHeight = 9;

        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_value").getString(), baseX + 40, baseY, 0xFFD700, true);
        baseY += 10;

        // 绘制一条水平分割线
        graphics.fill(baseX + 10, baseY, baseX + LEFT_PANEL_WIDTH - 10, baseY + 1, 0xff55FF55);
        baseY += 5;

        // 🔴 全部从 menu 获取同步后的服务端真实数据
        double health = menu.getSyncedHealth();
        double armor = menu.getSyncedArmor();
        double toughness = menu.getSyncedToughness();
        double attack = menu.getSyncedAttack();
        double attackSpeed = menu.getSyncedAttackSpeed();
        double moveSpeed = menu.getSyncedMoveSpeed();
        double critRate = menu.getSyncedCritRate();
        double critDamage = menu.getSyncedCritDamage();
        double knockbackRes = menu.getSyncedKnockbackRes();
        double attackKnockback = menu.getSyncedAttackKnockback();
        double luck = menu.getSyncedLuck();
        double swimSpeed = menu.getSyncedSwimSpeed();
        double blockReach = menu.getSyncedBlockReach();
        double entityReach = menu.getSyncedEntityReach();
        double stepHeight = menu.getSyncedStepHeight();
        double flySpeed = menu.getSyncedFlySpeed();

        int textX = baseX + 10;
        int goldColor = 0xFFD700;   // 双爆专属金色
        int normalColor = 0xAAAAAA; // 普通属性浅灰色
        int forgeColor = 0x2CBAA8; //forge 钻石色

        // 🔴 暴击率，标签灰色，数值金色
        Component critRateLabel = Component.translatable("gui.relics.player_attr_crit_rate", ""); // 传空串占位获取纯标签
        graphics.drawString(this.font, critRateLabel, textX, baseY, normalColor, false);
        graphics.drawString(this.font, formatValue(critRate, true), textX + this.font.width(critRateLabel), baseY, goldColor, false);baseY += lineHeight;

        Component critDamageLabel = Component.translatable("gui.relics.player_attr_crit_damage", "");
        graphics.drawString(this.font, critDamageLabel, textX, baseY, normalColor, false);
        graphics.drawString(this.font, formatValue(critDamage, true), textX + this.font.width(critDamageLabel), baseY, goldColor, false);baseY += lineHeight;

        // 基础属性
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_health", formatValue(health, false)), textX, baseY, normalColor, false); baseY += lineHeight;
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_armor", formatValue(armor, false)), textX, baseY, normalColor, false); baseY += lineHeight;
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_toughness", formatValue(toughness, false)), textX, baseY, normalColor, false); baseY += lineHeight;
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_attack", formatValue(attack, false)), textX, baseY, normalColor, false); baseY += lineHeight;
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_attack_speed", formatValue(attackSpeed, false)), textX, baseY, normalColor, false); baseY += lineHeight;
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_move", formatValue(moveSpeed, false)), textX, baseY, normalColor, false); baseY += lineHeight;
        // 其他属性
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_knockback_res", formatValue(knockbackRes, false)), textX, baseY, normalColor, false); baseY += lineHeight;
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_attack_knockback", formatValue(attackKnockback, false)), textX, baseY, normalColor, false); baseY += lineHeight;
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_luck", formatValue(luck, false)), textX, baseY, normalColor, false); baseY += lineHeight;
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_fly_speed", formatValue(flySpeed, false)), textX, baseY, normalColor, false); baseY += lineHeight;
        //forge属性
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_swim_speed", formatValue(swimSpeed, false)), textX, baseY, forgeColor, false); baseY += lineHeight;
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_block_reach", formatValue(blockReach, false)), textX, baseY, forgeColor, false); baseY += lineHeight;
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_entity_reach", formatValue(entityReach, false)), textX, baseY, forgeColor, false); baseY += lineHeight;
        graphics.drawString(this.font, Component.translatable("gui.relics.player_attr_step_height", formatValue(stepHeight, false)), textX, baseY, forgeColor, false);
    }

    /**
     * 格式化数字文本（保留两位小数）
     * @param a 数值
     * @param flag 是否转为百分比
     */
    private String formatValue(double a, boolean flag) {
        if (flag) {
            BigDecimal bd = new BigDecimal(a * 100).setScale(2, RoundingMode.HALF_EVEN);
            return bd + "%";
        }
        return new BigDecimal(a).setScale(2, RoundingMode.HALF_EVEN).toString();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(graphics, mouseX, mouseY);
        this.oldMouseX = (float) mouseX;
        this.oldMouseY = (float) mouseY;
    }

    /** 渲染玩家模型*/
    public static void drawEntityOnScreen(GuiGraphics graphics, int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity livingEntity) {
        InventoryScreen.renderEntityInInventoryFollowsMouse(graphics, posX, posY, scale, mouseX, mouseY, livingEntity);
    }

    @Override
    public void onClose() {
        if (minecraft != null && minecraft.player != null) {
            super.onClose();
        }
    }
}