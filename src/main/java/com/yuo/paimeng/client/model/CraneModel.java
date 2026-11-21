package com.yuo.paimeng.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.entity.CraneEntity;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * 原神风格仙鹤：修长白身 + 长颈 + 红冠 + 长喙 + 黑色翅尖与尾羽 + 细长腿。
 * 贴图 textures/entity/crane.png（64×64），UV 由同一套盒体规格打包生成。
 */
@OnlyIn(Dist.CLIENT)
public class CraneModel<C extends CraneEntity> extends EntityModel<C> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ModUtils.fa("crane"), "main");

    private final ModelPart body;
    private final ModelPart neck;
    private final ModelPart head;
    private final ModelPart beak;
    private final ModelPart crown;
    private final ModelPart wingLeft;
    private final ModelPart wingRight;
    private final ModelPart tail;
    private final ModelPart legLeft;
    private final ModelPart legRight;

    public CraneModel(ModelPart root) {
        this.body = root.getChild("body");
        this.neck = this.body.getChild("neck");
        this.head = this.neck.getChild("head");
        this.beak = this.head.getChild("beak");
        this.crown = this.head.getChild("crown");
        this.wingLeft = this.body.getChild("wing_left");
        this.wingRight = this.body.getChild("wing_right");
        this.tail = this.body.getChild("tail");
        this.legLeft = root.getChild("leg_left");
        this.legRight = root.getChild("leg_right");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // 躯干：8×6×12，脚底在 y=24
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(1, 1).addBox(-4.0F, -6.0F, -6.0F, 8.0F, 6.0F, 12.0F),
                PartPose.offset(0.0F, 18.0F, 0.0F));

        // 长颈
        PartDefinition neck = body.addOrReplaceChild("neck", CubeListBuilder.create()
                        .texOffs(26, 37).addBox(-1.5F, -8.0F, -1.5F, 3.0F, 8.0F, 3.0F),
                PartPose.offset(0.0F, -6.0F, -5.0F));

        // 头 + 喙 + 红冠
        PartDefinition head = neck.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(39, 37).addBox(-2.0F, -4.0F, -6.0F, 4.0F, 4.0F, 6.0F),
                PartPose.offset(0.0F, -8.0F, -1.0F));
        head.addOrReplaceChild("beak", CubeListBuilder.create()
                        .texOffs(11, 50).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 4.0F),
                PartPose.offset(0.0F, -1.0F, -6.0F));
        head.addOrReplaceChild("crown", CubeListBuilder.create()
                        .texOffs(24, 50).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 1.0F, 3.0F),
                PartPose.offset(0.0F, -3.0F, -1.0F));

        // 收拢的翅膀（白色，翅尖黑）
        body.addOrReplaceChild("wing_left", CubeListBuilder.create()
                        .texOffs(1, 20).addBox(-0.5F, 0.0F, -5.0F, 1.0F, 6.0F, 10.0F),
                PartPose.offset(4.0F, -6.0F, 0.0F));
        body.addOrReplaceChild("wing_right", CubeListBuilder.create()
                        .texOffs(24, 20).addBox(-0.5F, 0.0F, -5.0F, 1.0F, 6.0F, 10.0F),
                PartPose.offset(-4.0F, -6.0F, 0.0F));

        // 尾羽
        body.addOrReplaceChild("tail", CubeListBuilder.create()
                        .texOffs(1, 37).addBox(-2.0F, -4.0F, 0.0F, 4.0F, 4.0F, 8.0F),
                PartPose.offset(0.0F, -2.0F, 6.0F));

        // 细长腿
        root.addOrReplaceChild("leg_left", CubeListBuilder.create()
                        .texOffs(1, 50).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F),
                PartPose.offset(1.5F, 18.0F, 0.0F));
        root.addOrReplaceChild("leg_right", CubeListBuilder.create()
                        .texOffs(6, 50).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 6.0F, 1.0F),
                PartPose.offset(-1.5F, 18.0F, 0.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(this.body, this.legLeft, this.legRight);
    }

    @Override
    public void setupAnim(C entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        boolean flying = entity.isFlying();
        boolean sitting = entity.isInSittingPose();

        // 头部朝向：长颈让它更“探头探脑”
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F) * 0.6F;
        this.head.xRot = headPitch * ((float) Math.PI / 180F) * 0.6F;
        this.neck.yRot = this.head.yRot * 0.5F;
        this.neck.xRot = Mth.cos(ageInTicks * 0.08F) * 0.08F + this.head.xRot * 0.4F;

        if (flying) {
            // 飞行：翅膀展开扇动，腿收起，身体前倾
            float flap = Mth.cos(ageInTicks * 0.9F) * 0.6F;
            this.wingLeft.zRot = -1.15F - flap;
            this.wingRight.zRot = 1.15F + flap;
            this.wingLeft.yRot = -0.25F;
            this.wingRight.yRot = 0.25F;
            this.body.xRot = 0.35F;
            this.legLeft.xRot = -0.9F;
            this.legRight.xRot = -0.9F;
            this.tail.xRot = -0.25F;
        } else {
            // 站立 / 行走：翅膀收拢贴着身体
            this.wingLeft.zRot = -0.12F + Mth.cos(ageInTicks * 0.1F) * 0.03F;
            this.wingRight.zRot = 0.12F - Mth.cos(ageInTicks * 0.1F) * 0.03F;
            this.wingLeft.yRot = 0.0F;
            this.wingRight.yRot = 0.0F;
            this.body.xRot = 0.0F;
            this.tail.xRot = -0.1F + Mth.cos(ageInTicks * 0.08F) * 0.05F;
            if (sitting) {
                this.legLeft.xRot = 0.0F;
                this.legRight.xRot = 0.0F;
                this.body.y = 20.0F;
            } else {
                this.body.y = 18.0F;
                this.legLeft.xRot = Mth.cos(limbSwing * 0.9F) * 1.1F * limbSwingAmount;
                this.legRight.xRot = Mth.cos(limbSwing * 0.9F + (float) Math.PI) * 1.1F * limbSwingAmount;
            }
        }
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        this.getParts().forEach(part -> part.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha));
    }
}
