package com.yuo.paimeng.items;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

/**
 * 下界之心：召唤无相之岩的祭坛钥匙。
 * 实际的多方块检测在 {@link com.yuo.paimeng.event.HypostasisAltarEvent} 里。
 */
public class NetherHeart extends Item {

    public NetherHeart() {
        super(new Item.Properties().stacksTo(16).rarity(Rarity.RARE).fireResistant());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.translatable("paimeng.text.nether_heart.desc").withStyle(ChatFormatting.GRAY));
        tooltip.add(Component.translatable("paimeng.text.nether_heart.usage").withStyle(ChatFormatting.DARK_RED));
    }
}
