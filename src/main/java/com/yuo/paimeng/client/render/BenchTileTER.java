package com.yuo.paimeng.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yuo.paimeng.blocks.CookingBench;
import com.yuo.paimeng.tiles.BenchTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BenchTileTER implements BlockEntityRenderer<BenchTile> {

    public BenchTileTER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(BenchTile benchTile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int lightIn, int overlayIn) {
        if (!benchTile.getBlockState().getValue(CookingBench.FIRE)) return; //未燃烧则不渲染
        matrixStackIn.pushPose();
        Direction direction = benchTile.getBlockState().getValue(CookingBench.FACING); //移动
        switch (direction) {
            case EAST:
                matrixStackIn.translate(0.75, 0.32, 0.5);
                break;
            case WEST:
                matrixStackIn.translate(0.25, 0.32, 0.5);
                break;
            case SOUTH:
                matrixStackIn.translate(0.5, 0.32, 0.75);
                break;
            case NORTH:
                matrixStackIn.translate(0.5, 0.32, 0.25);
        }
        matrixStackIn.scale(0.25f, 0.25f, 0.25f); //缩放
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F)); //旋转
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = benchTile.getItem(6); //渲染物品
        //原版渲染方式
        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, lightIn, overlayIn, matrixStackIn, bufferIn,
                benchTile.getLevel(), (int) benchTile.getBlockPos().asLong());
        matrixStackIn.popPose();
    }
}
