package com.yuo.PaiMeng.Client.Render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.yuo.PaiMeng.Tiles.SevenGodTile;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.BeaconTileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.BeaconTileEntity;
import net.minecraft.tileentity.BeaconTileEntity.BeamSegment;

import java.util.List;

public class SevenGodTileRender extends TileEntityRenderer<SevenGodTile> {
    public SevenGodTileRender(TileEntityRendererDispatcher rendererDispatcherIn) {
        super(rendererDispatcherIn);
    }

    @Override
    public void render(SevenGodTile tile, float v, MatrixStack matrixStack, IRenderTypeBuffer buffer, int i, int i1) {
        long gameTime = tile.getWorld().getGameTime();
        int k = 2;
        BeamSegment bs = new BeamSegment(DyeColor.WHITE.getColorComponentValues());
        renderBeamSegment(matrixStack, buffer, v, gameTime, k,1024, bs.getColors());
    }

    private static void renderBeamSegment(MatrixStack matrixStack, IRenderTypeBuffer buffer, float v, long l, int i, int i1, float[] floats) {
        BeaconTileEntityRenderer.renderBeamSegment(matrixStack, buffer, BeaconTileEntityRenderer.TEXTURE_BEACON_BEAM, v, 1.0F, l, i, i1, floats, 0.2F, 0.25F);
    }

    @Override
    public boolean isGlobalRenderer(SevenGodTile tileEntity) {
        return true;
    }
}
