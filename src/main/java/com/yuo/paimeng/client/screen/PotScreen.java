package com.yuo.paimeng.client.screen;

import com.yuo.paimeng.menu.PotContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class PotScreen extends AbstractCookingScreen<PotContainer> {

    public PotScreen(PotContainer potContainer, Inventory inv, Component titleIn) {
        super(potContainer, inv, titleIn);
    }

    // 锅的经验条和文字偏移
    @Override
    protected int getExpBarX() {
        return 110;
    }

    @Override
    protected int getLevelTextX() {
        return 103;
    }
}