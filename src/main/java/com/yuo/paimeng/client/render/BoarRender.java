package com.yuo.paimeng.client.render;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.client.model.BoarModel;
import com.yuo.paimeng.entity.BoarEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BoarRender extends MobRenderer<BoarEntity, BoarModel<BoarEntity>> {
    private final ResourceLocation TEXTURE = ModUtils.fa("textures/entity/boar.png");

    public BoarRender(EntityRendererProvider.Context context) {
        super(context, new BoarModel<>(context.bakeLayer(BoarModel.LAYER)), 0.55f); //阴影大小
    }

    @Override
    public ResourceLocation getTextureLocation(BoarEntity entity) {
        return TEXTURE;
    }
}
