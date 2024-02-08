package com.yuo.PaiMeng.Client.Render;

import com.yuo.PaiMeng.Entity.CraneEntity;
import com.yuo.PaiMeng.Client.Model.CraneModel;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;

public class CraneRender extends MobRenderer<CraneEntity, CraneModel<CraneEntity>> {
    private final ResourceLocation TEXTURE = new ResourceLocation(PaiMeng.MOD_ID, "textures/entity/crane.png");

    public CraneRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new CraneModel<>(), 0.3f); //阴影大小
    }

    @Override
    public ResourceLocation getEntityTexture(CraneEntity entity) {
        return TEXTURE;
    }

    @Override
    public float handleRotationFloat(CraneEntity livingBase, float partialTicks) {
        float f = MathHelper.lerp(partialTicks, livingBase.oFlap, livingBase.flap);
        float f1 = MathHelper.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.flapSpeed);
        return (MathHelper.sin(f) + 1.0F) * f1;
    }
}
