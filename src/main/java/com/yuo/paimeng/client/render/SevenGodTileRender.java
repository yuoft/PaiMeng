package com.yuo.paimeng.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yuo.paimeng.tiles.SevenGodTile;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BeaconRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SevenGodTileRender implements BlockEntityRenderer<SevenGodTile> {

    public SevenGodTileRender(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(SevenGodTile tile, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int light, int overlay) {
        Level level = tile.getLevel();
        if (level == null) return;
        long gameTime = level.getGameTime();
        int k = 2;
        float[] colors = DyeColor.WHITE.getTextureDiffuseColors();
        renderBeamSegment(poseStack, buffer, partialTick, gameTime, k, 1024, colors);
    }

    private static void renderBeamSegment(PoseStack poseStack, MultiBufferSource buffer, float partialTick, long gameTime, int yOffset, int height, float[] colors) {
        BeaconRenderer.renderBeaconBeam(poseStack, buffer, BeaconRenderer.BEAM_LOCATION, partialTick, 1.0F, gameTime, yOffset, height, colors, 0.2F, 0.25F);
    }

    @Override
    public boolean shouldRenderOffScreen(SevenGodTile tileEntity) {
        return true;
    }
}
