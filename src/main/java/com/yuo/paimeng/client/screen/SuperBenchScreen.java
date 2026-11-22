package com.yuo.paimeng.client.screen;

import com.yuo.paimeng.menu.SuperBenchContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SuperBenchScreen extends AbstractCookingScreen<SuperBenchContainer> {

    public SuperBenchScreen(SuperBenchContainer benchContainer, Inventory inv, Component titleIn) {
        super(benchContainer, inv, titleIn);
        this.titleLabelX = this.imageWidth / 2 - 30;
    }

    // 工作台的经验条和文字偏移
    @Override
    protected int getExpBarX() {
        return 112;
    }

    @Override
    protected int getLevelTextX() {
        return 105;
    }
}