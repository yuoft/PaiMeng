package com.yuo.PaiMeng.Client.Model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuo.PaiMeng.Entity.CraneEntity;
import com.yuo.PaiMeng.ModUtils;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class CraneModel<C extends CraneEntity> extends EntityModel<C> {
    /** 模型层，主控需要在 ClientProxy 中通过 LayerDefinitions.register(CraneModel.LAYER, CraneModel::createBodyLayer) 注册 */
    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ModUtils.fa("crane"), "main");

    private final ModelPart body;
    private final ModelPart tail;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;
    private final ModelPart head;
    private final ModelPart head2;
    private final ModelPart beak1;
    private final ModelPart beak2;
    private final ModelPart feather;
    private final ModelPart legLeft;
    private final ModelPart legRight;

    public CraneModel(ModelPart root) {
        this.body = root.getChild("body");
        this.tail = root.getChild("tail");
        this.wingLeft = root.getChild("left_wing");
        this.wingRight = root.getChild("right_wing");
        this.head = root.getChild("head");
        this.head2 = this.head.getChild("head2");
        this.beak1 = this.head.getChild("beak1");
        this.beak2 = this.head.getChild("beak2");
        this.feather = this.head.getChild("feather");
        this.legLeft = root.getChild("left_leg");
        this.legRight = root.getChild("right_leg");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();
        root.addOrReplaceChild("body", CubeListBuilder.create().texOffs(2, 8)
                .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.offset(0.0F, 16.5F, -3.0F));
        root.addOrReplaceChild("tail", CubeListBuilder.create().texOffs(22, 1)
                .addBox(-1.5F, -1.0F, -1.0F, 3.0F, 4.0F, 1.0F), PartPose.offset(0.0F, 21.07F, 1.16F));
        root.addOrReplaceChild("left_wing", CubeListBuilder.create().texOffs(19, 8)
                .addBox(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F), PartPose.offset(1.5F, 16.94F, -2.76F));
        root.addOrReplaceChild("right_wing", CubeListBuilder.create().texOffs(19, 8)
                .addBox(-0.5F, 0.0F, -1.5F, 1.0F, 5.0F, 3.0F), PartPose.offset(-1.5F, 16.94F, -2.76F));
        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(2, 2)
                .addBox(-1.0F, -1.5F, -1.0F, 2.0F, 3.0F, 2.0F), PartPose.offset(0.0F, 15.69F, -2.76F));
        head.addOrReplaceChild("head2", CubeListBuilder.create().texOffs(10, 0)
                .addBox(-1.0F, -0.5F, -2.0F, 2.0F, 1.0F, 4.0F), PartPose.offset(0.0F, -2.0F, -1.0F));
        head.addOrReplaceChild("beak1", CubeListBuilder.create().texOffs(11, 7)
                .addBox(-0.5F, -1.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offset(0.0F, -0.5F, -1.5F));
        head.addOrReplaceChild("beak2", CubeListBuilder.create().texOffs(16, 7)
                .addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F), PartPose.offset(0.0F, -1.75F, -2.45F));
        head.addOrReplaceChild("feather", CubeListBuilder.create().texOffs(2, 18)
                .addBox(0.0F, -4.0F, -2.0F, 0.0F, 5.0F, 4.0F), PartPose.offset(0.0F, -2.15F, 0.15F));
        CubeListBuilder leg = CubeListBuilder.create().texOffs(14, 18).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 2.0F, 1.0F);
        root.addOrReplaceChild("left_leg", leg, PartPose.offset(1.0F, 22.0F, -1.05F));
        root.addOrReplaceChild("right_leg", leg, PartPose.offset(-1.0F, 22.0F, -1.05F));
        return LayerDefinition.create(mesh, 32, 32);
    }

    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(this.body, this.wingLeft, this.wingRight, this.tail, this.head, this.legLeft, this.legRight);
    }

    /**
     * Sets this entity's model rotation angles
     */
    @Override
    public void setupAnim(C entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setRotationAngles(getParrotState(entityIn), entityIn.tickCount, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    public void setRotationAngles(CraneEntity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.setRotationAngles(getParrotState(entityIn), entityIn.tickCount, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
    }

    @Override
    public void prepareMobModel(C entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.setLivingAnimations(getParrotState(entityIn));
    }

    public void setLivingAnimations(CraneEntity entityIn, float limbSwing, float limbSwingAmount, float partialTick) {
        this.setLivingAnimations(getParrotState(entityIn));
    }

    public void renderOnShoulder(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float p_228284_5_, float p_228284_6_, float p_228284_7_, float p_228284_8_, int p_228284_9_) {
        this.setLivingAnimations(CraneModel.State.ON_SHOULDER);
        this.setRotationAngles(CraneModel.State.ON_SHOULDER, p_228284_9_, p_228284_5_, p_228284_6_, 0.0F, p_228284_7_, p_228284_8_);
        this.getParts().forEach((part) -> part.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn));
    }

    private void setRotationAngles(CraneModel.State state, int ticksExisted, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.head.xRot = headPitch * ((float) Math.PI / 180F);
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F);
        this.head.zRot = 0.0F;
        this.head.x = 0.0F;
        this.body.x = 0.0F;
        this.tail.x = 0.0F;
        this.wingRight.x = -1.5F;
        this.wingLeft.x = 1.5F;
        switch (state) {
            case SITTING:
                break;
            case PARTY:
                float f = Mth.cos((float) ticksExisted);
                float f1 = Mth.sin((float) ticksExisted);
                this.head.x = f;
                this.head.y = 15.69F + f1;
                this.head.xRot = 0.0F;
                this.head.yRot = 0.0F;
                this.head.zRot = Mth.sin((float) ticksExisted) * 0.4F;
                this.body.x = f;
                this.body.y = 16.5F + f1;
                this.wingLeft.zRot = -0.0873F - ageInTicks;
                this.wingLeft.x = 1.5F + f;
                this.wingLeft.y = 16.94F + f1;
                this.wingRight.zRot = 0.0873F + ageInTicks;
                this.wingRight.x = -1.5F + f;
                this.wingRight.y = 16.94F + f1;
                this.tail.x = f;
                this.tail.y = 21.07F + f1;
                break;
            case STANDING:
                this.legLeft.xRot += Mth.cos(limbSwing * 0.6662F) * 1.4F * limbSwingAmount;
                this.legRight.xRot += Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.4F * limbSwingAmount;
            case FLYING:
            case ON_SHOULDER:
            default:
                float f2 = ageInTicks * 0.3F;
                this.head.y = 15.69F + f2;
                this.tail.xRot = 1.015F + Mth.cos(limbSwing * 0.6662F) * 0.3F * limbSwingAmount;
                this.tail.y = 21.07F + f2;
                this.body.y = 16.5F + f2;
                this.wingLeft.zRot = -0.0873F - ageInTicks;
                this.wingLeft.y = 16.94F + f2;
                this.wingRight.zRot = 0.0873F + ageInTicks;
                this.wingRight.y = 16.94F + f2;
                this.legLeft.y = 22.0F + f2;
                this.legRight.y = 22.0F + f2;
        }

    }

    private void setLivingAnimations(CraneModel.State state) {
        this.feather.xRot = -0.2214F;
        this.body.xRot = 0.4937F;
        this.wingLeft.xRot = -0.6981F;
        this.wingLeft.yRot = -(float) Math.PI;
        this.wingRight.xRot = -0.6981F;
        this.wingRight.yRot = -(float) Math.PI;
        this.legLeft.xRot = -0.0299F;
        this.legRight.xRot = -0.0299F;
        this.legLeft.y = 22.0F;
        this.legRight.y = 22.0F;
        this.legLeft.zRot = 0.0F;
        this.legRight.zRot = 0.0F;
        switch (state) {
            case SITTING:
                float f = 1.9F;
                this.head.y = 17.59F;
                this.tail.xRot = 1.5388988F;
                this.tail.y = 22.97F;
                this.body.y = 18.4F;
                this.wingLeft.zRot = -0.0873F;
                this.wingLeft.y = 18.84F;
                this.wingRight.zRot = 0.0873F;
                this.wingRight.y = 18.84F;
                ++this.legLeft.y;
                ++this.legRight.y;
                ++this.legLeft.xRot;
                ++this.legRight.xRot;
                break;
            case PARTY:
                this.legLeft.zRot = -0.34906584F;
                this.legRight.zRot = 0.34906584F;
            case STANDING:
            case ON_SHOULDER:
            default:
                break;
            case FLYING:
                this.legLeft.xRot += 0.6981317F;
                this.legRight.xRot += 0.6981317F;
        }

    }

    private static CraneModel.State getParrotState(CraneEntity crane) {
        if (crane.isPartyParrot()) {
            return CraneModel.State.PARTY;
        } else if (crane.isInSittingPose()) {
            return CraneModel.State.SITTING;
        } else {
            return crane.isFlying() ? CraneModel.State.FLYING : CraneModel.State.STANDING;
        }
    }

    @Override
    public void renderToBuffer(PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        this.getParts().forEach((part) -> part.render(matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha));
    }

    @OnlyIn(Dist.CLIENT)
    public static enum State {
        FLYING,
        STANDING,
        SITTING,
        PARTY,
        ON_SHOULDER;
    }
}
