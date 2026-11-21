package com.yuo.paimeng.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.entity.HypostasisEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.data.ModelData;

/**
 * 无相之岩渲染：8 个原版矿物方块拼成 2×2×2 外壳，中间是一块倾斜 45° 的基岩核心。
 * 外壳保持静态结构，不再自转或公转；核心会持续缓慢旋转（作为生命核心的特征）。
 */
public class HypostasisRenderer extends EntityRenderer<HypostasisEntity> {

    private static final BlockState[] SHELLS = {
            Blocks.IRON_BLOCK.defaultBlockState(),
            Blocks.GOLD_BLOCK.defaultBlockState(),
            Blocks.DIAMOND_BLOCK.defaultBlockState(),
            Blocks.EMERALD_BLOCK.defaultBlockState(),
            Blocks.LAPIS_BLOCK.defaultBlockState(),
            Blocks.REDSTONE_BLOCK.defaultBlockState(),
            Blocks.NETHERITE_BLOCK.defaultBlockState(),
            Blocks.COPPER_BLOCK.defaultBlockState()
    };

    private static final BlockState CORE = Blocks.BEDROCK.defaultBlockState();
    private static final int FULL_BRIGHT = 0xF000F0;
    private static final ResourceLocation DUMMY_TEXTURE = ModUtils.def("textures/misc/white.png");

    private static final float[][] CROSS_POINTS = {{0.0F, 0.0F}, {1.0F, 0.0F}, {-1.0F, 0.0F}, {0.0F, 1.0F}, {0.0F, -1.0F}};

    private final BlockRenderDispatcher blockRenderer;

    public HypostasisRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
        this.shadowRadius = 1.8F;
    }

    @Override
    public ResourceLocation getTextureLocation(HypostasisEntity entity) {
        return DUMMY_TEXTURE;
    }


    @Override
    public void render(HypostasisEntity entity, float entityYaw, float partialTick, PoseStack pose, MultiBufferSource buffer, int packedLight) {
        float time = entity.tickCount + partialTick;
        HypostasisEntity.AttackState state = entity.getAttackState();
        boolean phaseTwo = entity.getPhase() >= 2;
        float size = phaseTwo ? 1.1F : 1.0F;
        float progress = attackProgress(entity, state, partialTick);

        pose.pushPose();
        pose.translate(0.0D, 1.02D, 0.0D);
        pose.mulPose(Axis.YP.rotationDegrees(entityYaw));

        // ---- 🔴 倾斜基岩核心的渲染（在暴露时会停顿） ----
        float pulse = 1.0F + (phaseTwo ? 0.09F : 0.04F) * Mth.sin(time * 0.35F);
        float coreRot = time * 1.6F;

        if (entity.isCoreExposed()) {
            // 核心暴露期间：完全停止自转，用高频闪烁和放大来表现“停顿的破绽”
            coreRot = 0.0F; // 🔴 停止旋转
            pulse = 1.25F + 0.1F * Mth.sin(time * 2.0F); // 🔴 放大并闪烁
        }

        pose.pushPose();
        pose.scale(0.72F * size * pulse, 0.72F * size * pulse, 0.72F * size * pulse);
        pose.mulPose(Axis.YP.rotationDegrees(coreRot));
        pose.mulPose(Axis.XP.rotationDegrees(45.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(45.0F));
        renderBlock(CORE, pose, buffer, FULL_BRIGHT);
        pose.popPose();

        // ---- 外壳整体姿态：近战是“抬起 → 俯冲砸下 → 收招” ----
        float lift = 0.0F;
        float pitch = 0.0F;
        float forwardOffset = 0.0F;
        if (isMelee(state)) {
            float windup = 0.48F;
            float strikeEnd = 0.62F;
            if (progress < windup) {
                float k = smooth(progress / windup);
                lift = 1.5F * k;
                forwardOffset = 0.5F * k;
                pitch = -32.0F * k;
            } else if (progress < strikeEnd) {
                float k = smooth((progress - windup) / (strikeEnd - windup));
                lift = Mth.lerp(k, 1.5F, -2.3F);
                forwardOffset = Mth.lerp(k, 0.5F, 3.0F);
                pitch = Mth.lerp(k, -32.0F, 42.0F);
            } else {
                float k = smooth((progress - strikeEnd) / (1.0F - strikeEnd));
                lift = Mth.lerp(k, -2.3F, 0.0F);
                forwardOffset = Mth.lerp(k, 3.0F, 0.0F);
                pitch = Mth.lerp(k, 42.0F, 0.0F);
            }
        } else if (state == HypostasisEntity.AttackState.RANGED) {
            lift = 0.35F * Mth.sin(progress * Mth.PI);
            pitch = -12.0F * Mth.sin(progress * Mth.PI);
        } else if (state == HypostasisEntity.AttackState.THUNDER) {
            lift = 0.5F * Mth.sin(progress * Mth.PI);
        }

        pose.translate(0.0D, lift, 0.0D);
        pose.mulPose(Axis.XP.rotationDegrees(pitch));

        for (int i = 0; i < HypostasisEntity.SHELL_COUNT; i++) {
            // 🔴 传入 progress 用于形态过渡
            float[] placement = shellPlacement(state, i, time, size, forwardOffset, progress);

            pose.pushPose();
            pose.translate(placement[0], placement[1], placement[2]);
            float scale = placement[3];
            pose.scale(scale, scale, scale);
            renderBlock(SHELLS[i], pose, buffer, packedLight);
            pose.popPose();
        }

        pose.popPose();
    }

    /**
     * 返回 {x, y, z, 缩放}，引入 blend 插值消除从待机到攻击形态的瞬间瞬移（卡顿感）。
     */
    private float[] shellPlacement(HypostasisEntity.AttackState state, int index, float time, float size, float forwardOffset, float progress) {
        // 1. 获取待机形态坐标
        float[] idle = getIdlePlacement(index, time, size);
        // 2. 获取攻击目标坐标
        float[] target = getAttackPlacement(state, index, time, size, forwardOffset);

        // 3. 计算过渡系数 (blend)
        float blend = 1.0F;
        if (isMelee(state)) {
            // 前 15% 进度聚拢变身，后 15% 进度散开复原
            if (progress < 0.15F) {
                blend = smooth(progress / 0.15F);
            } else if (progress > 0.85F) {
                blend = 1.0F - smooth((progress - 0.85F) / 0.15F);
            }
        } else if (state == HypostasisEntity.AttackState.IDLE) {
            blend = 0.0F;
        }

        // 4. 线性插值，让方块平滑飞向目标点
        float x = Mth.lerp(blend, idle[0], target[0]);
        float y = Mth.lerp(blend, idle[1], target[1]);
        float z = Mth.lerp(blend, idle[2], target[2]);
        float scale = Mth.lerp(blend, idle[3], target[3]);

        return new float[]{x, y, z, scale};
    }

    private float[] getIdlePlacement(int index, float time, float size) {
        float baseX = ((index & 1) == 0 ? -1.0F : 1.0F) * 0.56F * size;
        float baseY = (((index >> 1) & 1) == 0 ? -1.0F : 1.0F) * 0.56F * size;
        float baseZ = (((index >> 2) & 1) == 0 ? -1.0F : 1.0F) * 0.56F * size;
        float x = baseX;
        float z = baseZ;
        float y = baseY + 0.06F * Mth.sin(time * 0.12F + index);
        float scale = 0.9F * size;
        return new float[]{x, y, z, scale};
    }

    private float[] getAttackPlacement(HypostasisEntity.AttackState state, int index, float time, float size, float forwardOffset) {
        float x = 0, y = 0, z = 0;
        float scale = 0.9F * size;

        switch (state) {
            case MELEE_LINE -> {
                x = (index - 3.5F) * 0.94F;
                y = 0.0F;
                z = forwardOffset;
                scale = 0.94F;
            }
            case MELEE_CROSS -> {
                if (index < CROSS_POINTS.length) {
                    x = CROSS_POINTS[index][0] * 0.95F;
                    y = CROSS_POINTS[index][1] * 0.95F;
                    z = forwardOffset;
                } else {
                    x = 0.0F;
                    y = 0.0F;
                    z = forwardOffset - 0.95F * (index - CROSS_POINTS.length + 1);
                }
                scale = 0.94F;
            }
            case MELEE_X -> {
                if (index < 4) {
                    float d = index - 1.5F;
                    x = d * 1.05F;
                    y = d * 0.9F;
                } else {
                    float d = (index - 5.5F);
                    x = d * 1.05F;
                    y = -d * 0.9F;
                }
                z = forwardOffset;
                scale = 0.94F;
            }
            case RANGED -> {
                double angle = time * 0.45D + index * (Math.PI * 2.0D / HypostasisEntity.SHELL_COUNT);
                double radius = 1.25D * size;
                x = (float) (Math.cos(angle) * radius);
                z = (float) (Math.sin(angle) * radius) + 0.7F;
                y = (index % 2 == 0 ? 0.55F : -0.55F) + 0.15F * Mth.sin(time * 0.2F + index);
                scale = 0.7F;
            }
            case THUNDER -> {
                double angle = time * 0.7D + index * (Math.PI * 2.0D / HypostasisEntity.SHELL_COUNT);
                double radius = 0.64D * size;
                x = (float) (Math.cos(angle) * radius);
                z = (float) (Math.sin(angle) * radius);
                y = (index % 2 == 0 ? 0.32F : -0.32F);
                scale = 0.78F;
            }
            default -> {
                return getIdlePlacement(index, time, size);
            }
        }
        return new float[]{x, y, z, scale};
    }

    // ... (保留你原有的 renderBlock, isMelee, attackProgress, smooth 方法)
    private void renderBlock(BlockState state, PoseStack pose, MultiBufferSource buffer, int light) {
        pose.pushPose();
        pose.translate(-0.5D, -0.5D, -0.5D);
        this.blockRenderer.renderSingleBlock(state, pose, buffer, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid());
        pose.popPose();
    }

    /**
     * 判断当前攻击状态是否属于近战攻击。
     * 近战攻击包括：一字型横扫、十字型镇压、X型砸击。
     *
     * @param state 实体当前所处的攻击状态枚举
     * @return 如果是近战状态返回 true，否则返回 false
     */
    private static boolean isMelee(HypostasisEntity.AttackState state) {
        return state == HypostasisEntity.AttackState.MELEE_LINE
                || state == HypostasisEntity.AttackState.MELEE_CROSS
                || state == HypostasisEntity.AttackState.MELEE_X;
    }

    /**
     * 计算当前攻击动画的播放进度。
     * 进度值被限制在 0.0 到 1.0 之间，用于控制外壳在不同阶段的位移和旋转（如近战的抬起、砸下、收招）。
     *
     * @param entity     实体对象，用于获取当前动画已流逝的 tick
     * @param state      实体当前的攻击状态
     * @param partialTick 渲染帧的插值时间（使得动画在低帧率下也流畅）
     * @return 动画进度值（0.0 表示刚开始，1.0 表示结束）
     */
    private static float attackProgress(HypostasisEntity entity, HypostasisEntity.AttackState state, float partialTick) {
        if (state == HypostasisEntity.AttackState.IDLE) return 0.0F;

        // 根据不同的攻击状态获取该动作的总持续时间
        int duration = switch (state) {
            case RANGED -> HypostasisEntity.DUR_RANGED;
            case THUNDER -> HypostasisEntity.DUR_THUNDER;
            default -> HypostasisEntity.DUR_MELEE;
        };
        // 当前已进行的 tick / 总 tick，并限制在 0~1 之间
        return Mth.clamp((entity.getAnimTick() + partialTick) / duration, 0.0F, 1.0F);
    }

    /**
     * 平滑步进函数（Smoothstep）。
     * 将线性的动画进度（0~1）转换为“缓入缓出”的非线性曲线（3x² - 2x³）。
     * 用于让动画的起步和结束更加柔和自然，避免机械生硬的速度变化。
     *
     * @param value 线性的进度值
     * @return 经过缓动处理后的进度值
     */
    private static float smooth(float value) {
        float t = Mth.clamp(value, 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }
}