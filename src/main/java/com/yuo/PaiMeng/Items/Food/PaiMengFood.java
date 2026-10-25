package com.yuo.PaiMeng.Items.Food;

import com.yuo.PaiMeng.Items.PMItems;
import net.minecraft.ChatFormatting;
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
    private final int LEVEL; //食物星级
    private final int TYPE; //食物类型

    public PaiMengFood(FoodProperties food, int lv, int type) {
        super(new Properties().food(food));
        this.LEVEL = lv;
        this.TYPE = type;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (LEVEL != 0 && LEVEL <= 6){ //星级信息
            tooltip.add(Component.literal(Component.translatable("paimeng.text.itemInfo.star").getString()
                    + Component.translatable("paimeng.text.itemInfo.star" + LEVEL).getString()));
        }
        tooltip.add(Component.literal(Component.translatable("paimeng.text.itemInfo.type").getString()
                + Component.translatable("paimeng.text.itemInfo.type" + TYPE).getString()));
        if (stack.getItem() == PMItems.bugFood.get()){
            tooltip.add(Component.translatable("paimeng.text.itemInfo.bug_food"));
            tooltip.add(Component.translatable("paimeng.text.itemInfo.bug_food1"));
        }
        if (stack.getItem() == PMItems.paimengFood.get()){
            tooltip.add(Component.translatable("paimeng.text.itemInfo.good_food"));
        }
    }

    //根据物品星级确定颜色名称（1.16.5 的 getDisplayName 在 1.20.1 中叫 getName）
    @Override
    public Component getName(ItemStack stack) {
        MutableComponent component = Component.translatable(this.getDescriptionId(stack));
        switch (LEVEL){
            case 1: return component.withStyle(ChatFormatting.WHITE);
            case 2: return component.withStyle(ChatFormatting.GREEN);
            case 3: return component.withStyle(ChatFormatting.BLUE);
            case 4: return component.withStyle(ChatFormatting.DARK_PURPLE);
            case 5: return component.withStyle(ChatFormatting.GOLD);
            default: return component;
        }
    }


    public int getLEVEL() {
        return LEVEL;
    }

    public int getTYPE() {
        return TYPE;
    }

}
