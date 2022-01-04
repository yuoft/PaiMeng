package com.yuo.PaiMeng.Event;

import com.yuo.PaiMeng.Items.Food.OrdinaryFood;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Random;

/**
 * 事件处理类
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PaiMeng.MOD_ID)
public class EventHandler {
    private final static Random random = new Random();

    //禁用原版食物
    @SubscribeEvent
    public static void eatFood(LivingEntityUseItemEvent.Start event){
        ItemStack item = event.getItem();
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (item.isFood() && !(item.getItem() instanceof OrdinaryFood)){//原版食物
                    player.sendStatusMessage(new TranslationTextComponent("paimeng.message.food"), true);
                    event.setCanceled(true);
            }
        }
    }

    //添加物品信息
    @SubscribeEvent
    public static void itemMessage(ItemTooltipEvent event){
        ItemStack stack = event.getItemStack();
        if (stack.isFood() && !(stack.getItem() instanceof OrdinaryFood)){ //原版食物
            List<ITextComponent> toolTip = event.getToolTip();
            toolTip.add(new TranslationTextComponent("paimeng.text.itemInfo.food"));
        }
    }
}

