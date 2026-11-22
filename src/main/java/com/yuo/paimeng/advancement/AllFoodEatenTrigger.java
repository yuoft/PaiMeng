package com.yuo.paimeng.advancement;

import com.google.gson.JsonObject;
import com.yuo.paimeng.ModUtils;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

/**
 * 「吃遍全部料理」判据。
 *
 * <p>是否达成由 {@code com.yuo.paimeng.event.FoodAdvancementHandler} 判断后主动触发，
 * 因此 JSON 里不需要任何 conditions。
 */
public class AllFoodEatenTrigger extends SimpleCriterionTrigger<AllFoodEatenTrigger.Instance> {

    public static final ResourceLocation ID = ModUtils.fa("all_food_eaten");

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    protected Instance createInstance(JsonObject json, ContextAwarePredicate player, DeserializationContext context) {
        return new Instance(ID, player);
    }

    /** 玩家集齐时调用。 */
    public void trigger(ServerPlayer player) {
        this.trigger(player, instance -> true);
    }

    public static class Instance extends AbstractCriterionTriggerInstance {
        public Instance(ResourceLocation id, ContextAwarePredicate player) {
            super(id, player);
        }
    }
}
