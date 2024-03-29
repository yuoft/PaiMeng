package com.yuo.PaiMeng.Items.Food;

import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.tab.PMGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

//食物物品
public class PaiMengFood extends Item {
    private final int LEVEL; //食物星级
    private final int TYPE; //食物类型

    public PaiMengFood(Food food, int lv, int type) {
        super(new Properties().food(food).group(PMGroup.PaiMengFood));
        this.LEVEL = lv;
        this.TYPE = type;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (LEVEL != 0 && LEVEL <= 6){ //星级信息
            tooltip.add(new StringTextComponent(new TranslationTextComponent("paimeng.text.itemInfo.star").getString()
                    + new TranslationTextComponent("paimeng.text.itemInfo.star" + LEVEL).getString()));
        }
        tooltip.add(new StringTextComponent(new TranslationTextComponent("paimeng.text.itemInfo.type").getString()
                + new TranslationTextComponent("paimeng.text.itemInfo.type" + TYPE).getString()));
        if (stack.getItem() == PMItems.bugFood.get()){
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.bug_food"));
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.bug_food1"));
        }
        if (stack.getItem() == PMItems.paimengFood.get()){
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.good_food"));
        }
    }

    //根据物品星级确定颜色名称
    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        TranslationTextComponent component = new TranslationTextComponent(getTranslationKey(stack));
        switch (LEVEL){
            case 1: return component.mergeStyle(TextFormatting.WHITE);
            case 2: return component.mergeStyle(TextFormatting.GREEN);
            case 3: return component.mergeStyle(TextFormatting.BLUE);
            case 4: return component.mergeStyle(TextFormatting.DARK_PURPLE);
            case 5: return component.mergeStyle(TextFormatting.GOLD);
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