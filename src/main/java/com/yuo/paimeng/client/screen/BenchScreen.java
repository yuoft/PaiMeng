package com.yuo.paimeng.client.screen;

import com.yuo.paimeng.menu.BenchContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class BenchScreen extends AbstractCookingScreen<BenchContainer> {

    public BenchScreen(BenchContainer benchContainer, Inventory inv, Component titleIn) {
        super(benchContainer, inv, titleIn);
    }

    // 工作台的经验条和文字偏移
    @Override
    protected int getExpBarX() {
        return 105;
    }

    @Override
    protected int getLevelTextX() {
        return 98;
    }
}