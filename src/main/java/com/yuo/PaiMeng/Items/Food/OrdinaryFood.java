package com.yuo.PaiMeng.Items.Food;

import com.yuo.PaiMeng.tab.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

//食物物品
public class OrdinaryFood extends Item {
    private final int LEVEL;

    public OrdinaryFood(Food food, int lv) {
        super(new Properties().food(food).group(ModGroup.PaiMengFood));
        this.LEVEL = lv;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if (LEVEL != 0){ //星级信息
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.star" + LEVEL));
        }
    }

    public int getLEVEL() {
        return LEVEL;
    }
}