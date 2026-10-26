package com.yuo.PaiMeng.Client.Render;

import com.yuo.PaiMeng.Entity.BoarEntity;
import com.yuo.PaiMeng.ModUtils;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.model.PigModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoarRender extends MobRenderer<BoarEntity, PigModel<BoarEntity>> {
    private final ResourceLocation TEXTURE = ModUtils.fa("textures/entity/boar.png");

    public BoarRender(EntityRendererProvider.Context context) {
        super(context, new PigModel<>(context.bakeLayer(ModelLayers.PIG)), 0.5f); //阴影大小
    }

    @Override
    public ResourceLocation getTextureLocation(BoarEntity entity) {
        return TEXTURE;
    }
}
