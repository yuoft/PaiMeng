package com.yuo.PaiMeng.Client.Render;

import com.yuo.PaiMeng.Client.Model.CraneModel;
import com.yuo.PaiMeng.Entity.CraneEntity;
import com.yuo.PaiMeng.ModUtils;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CraneRender extends MobRenderer<CraneEntity, CraneModel<CraneEntity>> {
    private final ResourceLocation TEXTURE = ModUtils.fa("textures/entity/crane.png");

    public CraneRender(EntityRendererProvider.Context context) {
        super(context, new CraneModel<>(context.bakeLayer(CraneModel.LAYER)), 0.3f); //阴影大小
    }

    @Override
    public ResourceLocation getTextureLocation(CraneEntity entity) {
        return TEXTURE;
    }

    @Override
    protected float getBob(CraneEntity livingBase, float partialTicks) {
        float f = Mth.lerp(partialTicks, livingBase.oFlap, livingBase.flap);
        float f1 = Mth.lerp(partialTicks, livingBase.oFlapSpeed, livingBase.flapSpeed);
        return (Mth.sin(f) + 1.0F) * f1;
    }
}
