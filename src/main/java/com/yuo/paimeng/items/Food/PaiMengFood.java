package com.yuo.paimeng.items.Food;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.items.PMItems;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

//食物物品
public class PaiMengFood extends Item {
    private final int FOOD_LEVEL; //食物星级
    private final int FOOD_TYPE; //食物类型

    public PaiMengFood(FoodProperties food, int lv, int type) {
        super(new Properties().food(food));
        this.FOOD_LEVEL = lv;
        this.FOOD_TYPE = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (FOOD_LEVEL != 0 && FOOD_LEVEL <= 6){ //星级信息
            tooltip.add(Component.literal(Component.translatable("paimeng.text.itemInfo.star").getString()
                    + Component.translatable("paimeng.text.itemInfo.star" + FOOD_LEVEL).getString()));
        }
        tooltip.add(Component.literal(Component.translatable("paimeng.text.itemInfo.type").getString()
                + Component.translatable("paimeng.text.itemInfo.type" + FOOD_TYPE).getString()));
        if (stack.getItem() == PMItems.bugFood.get()){
            tooltip.add(Component.translatable("paimeng.text.itemInfo.bug_food"));
            tooltip.add(Component.translatable("paimeng.text.itemInfo.bug_food1"));
        }
        if (stack.getItem() == PMItems.paimengFood.get()){
            tooltip.add(Component.translatable("paimeng.text.itemInfo.good_food"));
        }
    }

    //根据物品星级确定颜色名称
    @Override
    public Component getName(ItemStack stack) {
        MutableComponent component = Component.translatable(this.getDescriptionId(stack));
        return ModUtils.getTierName(FOOD_LEVEL, component);
    }


    public int getFOOD_LEVEL() {
        return FOOD_LEVEL;
    }

    public int getFOOD_TYPE() {
        return FOOD_TYPE;
    }

}
