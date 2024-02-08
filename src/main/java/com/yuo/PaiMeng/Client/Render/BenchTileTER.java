package com.yuo.PaiMeng.Client.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yuo.PaiMeng.Blocks.CookingBench;
import com.yuo.PaiMeng.Tiles.BenchTile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.vector.Quaternion;

public class BenchTileTER extends TileEntityRenderer<BenchTile> {
    public BenchTileTER(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(BenchTile tileEntityIn, float partialTicks, MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int combinedLightIn, int combinedOverlayIn) {
        if (!tileEntityIn.getBlockState().get(CookingBench.FIRE)) return; //未燃烧则不渲染
        matrixStackIn.push();
        Direction direction = tileEntityIn.getBlockState().get(CookingBench.FACING); //移动
        switch (direction){
            case EAST:
                matrixStackIn.translate(0.75,0.32,0.5);break;
            case WEST:
                matrixStackIn.translate(0.25,0.32,0.5);break;
            case SOUTH:
                matrixStackIn.translate(0.5,0.32,0.75);break;
            case NORTH:
                matrixStackIn.translate(0.5,0.32,0.25);
        }
        matrixStackIn.scale(0.25f,0.25f,0.25f); //缩放
        matrixStackIn.rotate(new Quaternion(90,0,0, true)); //旋转
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = new ItemStack(tileEntityIn.items.get(6).getItem()); //渲染物品
        IBakedModel ibakedmodel = itemRenderer.getItemModelWithOverrides(stack, tileEntityIn.getWorld(), null); //获取模型
        //原版渲染方式
        itemRenderer.renderItem(stack, ItemCameraTransforms.TransformType.FIXED, true, matrixStackIn, bufferIn, combinedLightIn, combinedOverlayIn, ibakedmodel);
        matrixStackIn.pop();
    }
}
