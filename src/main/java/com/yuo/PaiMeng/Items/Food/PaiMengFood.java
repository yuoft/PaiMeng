package com.yuo.PaiMeng.Items.Food;

import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.tab.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

//食物物品
public class PaiMengFood extends Item {
    private final int LEVEL; //食物星级
    private final int TYPE; //食物类型

    public PaiMengFood(Food food, int lv, int type) {
        super(new Properties().food(food).group(ModGroup.PaiMengFood));
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

    public int getLEVEL() {
        return LEVEL;
    }

    public int getTYPE() {
        return TYPE;
    }

}