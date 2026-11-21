package com.yuo.paimeng.client.model;

import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.entity.BoarEntity;
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
 * 原神风格野猪：矮壮身躯 + 背部鬃毛 + 大鼻吻 + 上翘獠牙 + 短腿。
 * 贴图 textures/entity/boar.png（64×64），UV 由同一套盒体规格打包生成。
 */
@OnlyIn(Dist.CLIENT)
public class BoarModel<T extends BoarEntity> extends EntityModel<T> {

    public static final ModelLayerLocation LAYER = new ModelLayerLocation(ModUtils.fa("boar"), "main");

    private final ModelPart body;
    private final ModelPart mane;
    private final ModelPart head;
    private final ModelPart snout;
    private final ModelPart tuskLeft;
    private final ModelPart tuskRight;
    private final ModelPart earLeft;
    private final ModelPart earRight;
    private final ModelPart legFrontLeft;
    private final ModelPart legFrontRight;
    private final ModelPart legBackLeft;
    private final ModelPart legBackRight;
    private final ModelPart tail;

    public BoarModel(ModelPart root) {
        this.body = root.getChild("body");
        this.mane = this.body.getChild("mane");
        this.head = this.body.getChild("head");
        this.snout = this.head.getChild("snout");
        this.tuskLeft = this.head.getChild("tusk_left");
        this.tuskRight = this.head.getChild("tusk_right");
        this.earLeft = this.head.getChild("ear_left");
        this.earRight = this.head.getChild("ear_right");
        this.legFrontLeft = root.getChild("leg_front_left");
        this.legFrontRight = root.getChild("leg_front_right");
        this.legBackLeft = root.getChild("leg_back_left");
        this.legBackRight = root.getChild("leg_back_right");
        this.tail = this.body.getChild("tail");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition root = mesh.getRoot();

        // 躯干：10×8×14，脚底在 y=24
        PartDefinition body = root.addOrReplaceChild("body", CubeListBuilder.create()
                        .texOffs(1, 1).addBox(-5.0F, -8.0F, -7.0F, 10.0F, 8.0F, 14.0F),
                PartPose.offset(0.0F, 18.0F, 0.0F));

        // 背鬃
        body.addOrReplaceChild("mane", CubeListBuilder.create()
                        .texOffs(1, 24).addBox(-1.0F, -3.0F, -7.0F, 2.0F, 3.0F, 14.0F),
                PartPose.offset(0.0F, -8.0F, 0.0F));

        // 头（含鼻吻、獠牙、耳朵）
        PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create()
                        .texOffs(34, 24).addBox(-3.5F, -6.0F, -6.0F, 7.0F, 6.0F, 6.0F),
                PartPose.offset(0.0F, -2.0F, -7.0F));
        head.addOrReplaceChild("snout", CubeListBuilder.create()
                        .texOffs(1, 52).addBox(-2.0F, -1.5F, -3.0F, 4.0F, 3.0F, 3.0F),
                PartPose.offset(0.0F, -1.5F, -6.0F));
        head.addOrReplaceChild("tusk_left", CubeListBuilder.create()
                        .texOffs(16, 52).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(2.0F, -3.5F, -6.5F, -0.35F, 0.0F, -0.12F));
        head.addOrReplaceChild("tusk_right", CubeListBuilder.create()
                        .texOffs(21, 52).addBox(-0.5F, 0.0F, -0.5F, 1.0F, 3.0F, 1.0F),
                PartPose.offsetAndRotation(-2.0F, -3.5F, -6.5F, -0.35F, 0.0F, 0.12F));
        head.addOrReplaceChild("ear_left", CubeListBuilder.create()
                        .texOffs(31, 52).addBox(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(2.5F, -4.5F, -3.5F, -0.25F, 0.0F, -0.35F));
        head.addOrReplaceChild("ear_right", CubeListBuilder.create()
                        .texOffs(38, 52).addBox(-1.0F, -2.0F, -0.5F, 2.0F, 2.0F, 1.0F),
                PartPose.offsetAndRotation(-2.5F, -4.5F, -3.5F, -0.25F, 0.0F, 0.35F));

        // 短腿
        CubeListBuilder frontLeg = CubeListBuilder.create().texOffs(1, 42).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F);
        root.addOrReplaceChild("leg_front_left", frontLeg, PartPose.offset(3.0F, 18.0F, -4.5F));
        root.addOrReplaceChild("leg_front_right", CubeListBuilder.create().texOffs(14, 42)
                .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.offset(-3.0F, 18.0F, -4.5F));
        root.addOrReplaceChild("leg_back_left", CubeListBuilder.create().texOffs(27, 42)
                .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.offset(3.0F, 18.0F, 4.5F));
        root.addOrReplaceChild("leg_back_right", CubeListBuilder.create().texOffs(40, 42)
                .addBox(-1.5F, 0.0F, -1.5F, 3.0F, 6.0F, 3.0F), PartPose.offset(-3.0F, 18.0F, 4.5F));

        // 尾巴
        body.addOrReplaceChild("tail", CubeListBuilder.create()
                        .texOffs(26, 52).addBox(-0.5F, 0.0F, 0.0F, 1.0F, 3.0F, 1.0F),
                PartPose.offset(0.0F, -6.0F, 7.0F));

        return LayerDefinition.create(mesh, 64, 64);
    }

    public Iterable<ModelPart> getParts() {
        return ImmutableList.of(this.body, this.legFrontLeft, this.legFrontRight,
                this.legBackLeft, this.legBackRight);
    }

    @Override
    public void setupAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks,
                          float netHeadYaw, float headPitch) {
        this.head.yRot = netHeadYaw * ((float) Math.PI / 180F) * 0.7F;
        this.head.xRot = headPitch * ((float) Math.PI / 180F) * 0.7F;
        this.head.zRot = 0.0F;

        // 四条短腿交替摆动
        this.legFrontLeft.xRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;
        this.legBackRight.xRot = Mth.cos(limbSwing * 0.6662F) * 1.2F * limbSwingAmount;
        this.legFrontRight.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.2F * limbSwingAmount;
        this.legBackLeft.xRot = Mth.cos(limbSwing * 0.6662F + (float) Math.PI) * 1.2F * limbSwingAmount;

        // 小尾巴左右甩 + 头部轻微起伏
        this.tail.zRot = Mth.cos(ageInTicks * 0.15F) * 0.35F;
        this.body.y = 18.0F + Mth.cos(ageInTicks * 0.1F) * 0.2F + limbSwingAmount * 0.3F;
        this.mane.zRot = Mth.cos(ageInTicks * 0.08F) * 0.05F;
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay,
                               float red, float green, float blue, float alpha) {
        this.getParts().forEach(part -> part.render(poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha));
    }
}
