package com.yuo.paimeng.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
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
 * 直接借用原版方块模型与方块纹理，因此贴图全部来自原版资源，没有额外 PNG 依赖。
 *
 * <p>动画全部由服务端同步过来的状态（形态 + 动画帧）推导，客户端只做插值：
 * 一字型横扫、十字型镇压、X 型砸击、远程抛掷、雷击收拢，二阶段外壳散得更开、转速更快。
 */
public class HypostasisRenderer extends EntityRenderer<HypostasisEntity> {

    /** 外壳 8 种矿物方块，顺序与投射物、召唤祭坛保持一致。 */
    private static final BlockState[] SHELLS = {
            Blocks.IRON_BLOCK.defaultBlockState(),
            Blocks.GOLD_BLOCK.defaultBlockState(),
            Blocks.DIAMOND_BLOCK.defaultBlockState(),
            Blocks.EMERALD_BLOCK.defaultBlockState(),
            Blocks.LAPIS_BLOCK.defaultBlockState(),
            Blocks.REDSTONE_BLOCK.defaultBlockState(),
            Blocks.COAL_BLOCK.defaultBlockState(),
            Blocks.COPPER_BLOCK.defaultBlockState()
    };

    private static final BlockState CORE = Blocks.BEDROCK.defaultBlockState();
    private static final int FULL_BRIGHT = 0xF000F0;
    private static final ResourceLocation DUMMY_TEXTURE = new ResourceLocation("textures/misc/white.png");

    /** 十字型的 5 个正交点（XY 平面），多出来的 3 个方块往身后叠做厚度。 */
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
    public void render(HypostasisEntity entity, float entityYaw, float partialTick, PoseStack pose,
                       MultiBufferSource buffer, int packedLight) {
        float time = entity.tickCount + partialTick;
        HypostasisEntity.AttackState state = entity.getAttackState();
        boolean phaseTwo = entity.getPhase() >= 2;
        float size = phaseTwo ? 1.1F : 1.0F;
        float progress = attackProgress(entity, state, partialTick);

        pose.pushPose();
        pose.translate(0.0D, 1.02D, 0.0D);
        pose.mulPose(Axis.YP.rotationDegrees(entityYaw));

        // ---- 倾斜基岩核心（自发光，二阶段会脉动放大）
        float pulse = 1.0F + (phaseTwo ? 0.09F : 0.04F) * Mth.sin(time * 0.35F);
        pose.pushPose();
        pose.scale(0.72F * size * pulse, 0.72F * size * pulse, 0.72F * size * pulse);
        pose.mulPose(Axis.YP.rotationDegrees(time * 1.6F));
        pose.mulPose(Axis.XP.rotationDegrees(45.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(45.0F));
        renderBlock(CORE, pose, buffer, FULL_BRIGHT);
        pose.popPose();

        // ---- 外壳整体姿态：近战是“抬起 → 俯冲砸下 → 收招”
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
            float[] placement = shellPlacement(state, i, time, size, forwardOffset);
            if (placement == null) continue;

            pose.pushPose();
            pose.translate(placement[0], placement[1], placement[2]);
            float spin = time * 14.0F + i * 37.0F;
            pose.mulPose(Axis.YP.rotationDegrees(spin));
            pose.mulPose(Axis.XP.rotationDegrees(spin * 0.6F));
            float scale = placement[3];
            pose.scale(scale, scale, scale);
            renderBlock(SHELLS[i], pose, buffer, packedLight);
            pose.popPose();
        }

        pose.popPose();
    }

    /** 返回 {x, y, z, 缩放}，坐标相对核心中心；forwardOffset 是砸击时外壳整体前探的距离。 */
    private float[] shellPlacement(HypostasisEntity.AttackState state, int index, float time, float size, float forwardOffset) {
        float x;
        float y;
        float z;
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
                // 待机：2×2×2 立方外壳整体缓慢自转
                float baseX = ((index & 1) == 0 ? -1.0F : 1.0F) * 0.56F * size;
                float baseY = (((index >> 1) & 1) == 0 ? -1.0F : 1.0F) * 0.56F * size;
                float baseZ = (((index >> 2) & 1) == 0 ? -1.0F : 1.0F) * 0.56F * size;
                double angle = Math.toRadians(time * 2.4D);
                x = (float) (baseX * Math.cos(angle) - baseZ * Math.sin(angle));
                z = (float) (baseX * Math.sin(angle) + baseZ * Math.cos(angle));
                y = baseY + 0.06F * Mth.sin(time * 0.12F + index);
                scale = 0.9F * size;
            }
        }
        return new float[]{x, y, z, scale};
    }

    private void renderBlock(BlockState state, PoseStack pose, MultiBufferSource buffer, int light) {
        pose.pushPose();
        pose.translate(-0.5D, -0.5D, -0.5D);
        this.blockRenderer.renderSingleBlock(state, pose, buffer, light, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid());
        pose.popPose();
    }

    private static boolean isMelee(HypostasisEntity.AttackState state) {
        return state == HypostasisEntity.AttackState.MELEE_LINE
                || state == HypostasisEntity.AttackState.MELEE_CROSS
                || state == HypostasisEntity.AttackState.MELEE_X;
    }

    private static float attackProgress(HypostasisEntity entity, HypostasisEntity.AttackState state, float partialTick) {
        if (state == HypostasisEntity.AttackState.IDLE) return 0.0F;
        int duration = switch (state) {
            case RANGED -> HypostasisEntity.DUR_RANGED;
            case THUNDER -> HypostasisEntity.DUR_THUNDER;
            default -> HypostasisEntity.DUR_MELEE;
        };
        return Mth.clamp((entity.getAnimTick() + partialTick) / duration, 0.0F, 1.0F);
    }

    private static float smooth(float value) {
        float t = Mth.clamp(value, 0.0F, 1.0F);
        return t * t * (3.0F - 2.0F * t);
    }
}
