package com.yuo.paimeng.event;

import com.yuo.paimeng.PaiMeng;
import com.yuo.paimeng.advancement.PMTriggers;
import com.yuo.paimeng.items.Food.PaiMengFood;
import com.yuo.paimeng.items.PMItems;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * 记录玩家吃过的料理，集齐全部（排除「裁决之时」与「派蒙料理」）时触发「大胃王」进度。
 *
 * <p>已吃料理存进玩家的 Forge 持久化数据（{@code PaimengEatenFoods}），存档重启不会丢失。
 */
@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID)
public class FoodAdvancementHandler {

    private static final String EATEN_KEY = "PaimengEatenFoods";

    /** 需要吃到的料理总数（延迟计算，注册表就绪后才准确）。 */
    private static int totalFoods = -1;

    @SubscribeEvent
    public static void onUseFinished(LivingEntityUseItemEvent.Finish event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.level().isClientSide) return;

        ItemStack stack = event.getItem();
        if (!(stack.getItem() instanceof PaiMengFood)) return;

        Item item = stack.getItem();
        // 两种特殊料理不计入「大胃王」
        if (item == PMItems.bugFood.get() || item == PMItems.paimengFood.get()) return;

        ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
        if (key == null) return;
        String id = key.toString();

        ListTag eaten = player.getPersistentData().getList(EATEN_KEY, Tag.TAG_STRING);
        for (Tag tag : eaten) {
            if (tag.getAsString().equals(id)) return; // 吃过了
        }

        ListTag updated = eaten.copy();
        updated.add(StringTag.valueOf(id));
        player.getPersistentData().put(EATEN_KEY, updated);

        if (updated.size() >= totalFoods()) {
            PMTriggers.ALL_FOOD_EATEN.trigger(player);
        }
    }

    /** 统计模组里的料理总数（PaiMengFood，排除两种特殊料理）。 */
    private static int totalFoods() {
        if (totalFoods < 0) {
            int count = 0;
            Item bugFood = PMItems.bugFood.get();
            Item paimengFood = PMItems.paimengFood.get();
            for (Item item : ForgeRegistries.ITEMS.getValues()) {
                if (item instanceof PaiMengFood && item != bugFood && item != paimengFood) {
                    count++;
                }
            }
            totalFoods = count;
        }
        return totalFoods;
    }
}
