package com.yuo.PaiMeng.Entity.Render;

import com.yuo.PaiMeng.Entity.BoarEntity;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.model.PigModel;
import net.minecraft.util.ResourceLocation;

public class BoarRender extends MobRenderer<BoarEntity, PigModel<BoarEntity>> {
    private final ResourceLocation TEXTURE = new ResourceLocation(PaiMeng.MOD_ID, "textures/entity/boar.png");

    public BoarRender(EntityRendererManager renderManagerIn) {
        super(renderManagerIn, new PigModel<>(), 0.5f); //阴影大小
    }

    @Override
    public ResourceLocation getEntityTexture(BoarEntity entity) {
        return TEXTURE;
    }
}
