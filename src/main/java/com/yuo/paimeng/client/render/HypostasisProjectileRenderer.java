package com.yuo.paimeng.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yuo.paimeng.entity.HypostasisProjectile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.data.ModelData;

/** 抛掷方块投射物的渲染：旋转的小方块，直接使用原版方块纹理。 */
public class HypostasisProjectileRenderer extends EntityRenderer<HypostasisProjectile> {

    private static final ResourceLocation DUMMY_TEXTURE = new ResourceLocation("textures/misc/white.png");
    private final BlockRenderDispatcher blockRenderer;

    public HypostasisProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.blockRenderer = Minecraft.getInstance().getBlockRenderer();
        this.shadowRadius = 0.3F;
    }

    @Override
    public ResourceLocation getTextureLocation(HypostasisProjectile entity) {
        return DUMMY_TEXTURE;
    }

    @Override
    public void render(HypostasisProjectile entity, float entityYaw, float partialTick, PoseStack pose,
                       MultiBufferSource buffer, int packedLight) {
        float spin = (entity.tickCount + partialTick) * 20.0F;
        pose.pushPose();
        pose.translate(0.0D, 0.32D, 0.0D);
        pose.mulPose(Axis.YP.rotationDegrees(spin));
        pose.mulPose(Axis.XP.rotationDegrees(spin * 0.65F));
        pose.scale(0.6F, 0.6F, 0.6F);
        pose.translate(-0.5D, -0.5D, -0.5D);
        this.blockRenderer.renderSingleBlock(entity.getBlockState(), pose, buffer, packedLight, OverlayTexture.NO_OVERLAY, ModelData.EMPTY, RenderType.solid());
        pose.popPose();
    }
}
