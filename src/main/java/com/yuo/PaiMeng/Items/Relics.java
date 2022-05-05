package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.tab.ModGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class Relics extends Item {
    private RelicsType type; //圣遗物使用位置
    private final int minStar; //圣遗物最小星级
    private final int maxStar;
    private int star; //星级 1~5
    private int level = 0; //当前等级 默认0
    private int exp = 0; //当前经验 0

    public Relics(int minLevel, int maxLevel) {
        super(new Properties().group(ModGroup.PaiMengRelics).maxStackSize(1));
        this.type = RelicsType.EMPTY;
        this.minStar = minLevel;
        this.maxStar = maxLevel;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        RelicsType relicsType = RelicsHelper.getTypeForStack(stack);
        //部位
        tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_" + relicsType.getName()));
        int star = RelicsHelper.getStarFormStack(stack);
        //等级
        if (RelicsHelper.hasLevelNbt(stack)){
            int relicsLevel = RelicsHelper.getRelicsLevel(stack);
            tooltip.add(new StringTextComponent("+" + relicsLevel).mergeStyle(TextFormatting.GOLD));
            if (relicsLevel == star * 4){
                tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_exp_max").mergeStyle(TextFormatting.RED));
            }else tooltip.add(new StringTextComponent( RelicsHelper.getRelicsExp(stack)+ "/" + RelicsHelper.getUpExpForLevel(star, relicsLevel)).mergeStyle(TextFormatting.DARK_PURPLE));
        }
        //描述
        tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo." + getI18nName() + relicsType.getName()));
        //属性
        tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_attr"));
        if (RelicsHelper.hasMainAttrNbt(stack)) {
            RelicsHelper.RandomAttrValue mainAttr = RelicsHelper.getMainAttrTypeFormStack(stack);
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_" + mainAttr.getAttrType().getTip(),
                    RelicsHelper.getMathTwo(mainAttr)).mergeStyle(TextFormatting.BLUE));
        }
        else tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_no_attr").mergeStyle(TextFormatting.BLUE));
        if (star > 1){
            tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_attr1"));
            if (RelicsHelper.hasViceAttr(stack)){
                for (RelicsHelper.RandomAttrValue type : RelicsHelper.getViceAttrTypeFormStack(stack)) {
                    if (type.getAttrType().getId() != 0){
                        tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_" + type.getAttrType().getTip(),
                                RelicsHelper.getMathTwo(type)).mergeStyle(TextFormatting.BLUE));
                    }
                }
            }
            else tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_no_attr").mergeStyle(TextFormatting.BLUE));
        }
    }

    //生成同一圣遗物不同星级
    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if (group == ModGroup.PaiMengRelics){
            for (int i = this.minStar; i <= this.maxStar; i++){ //星级
                for (int j = 0; j < 5 ; j++){ //部位
                    items.add(getItemStack(i, j));
                }
            }
        }
    }

    /**
     * 获取一个物品 添加等级数据 创造页星级
     * @param i 等级
     * @param j 部位
     * @return 物品
     */
    public ItemStack getItemStack(int i, int j){
        ItemStack stack = new ItemStack(this);
        CompoundNBT orCreateTag = stack.getOrCreateTag();
        CompoundNBT nbt = new CompoundNBT();
        nbt.putInt(RelicsHelper.NBT_STAR, i);
        nbt.putInt(RelicsHelper.NBT_TYPE, j);
        orCreateTag.put(RelicsHelper.NBT_RELICS, nbt);
        return stack;
    }

    public int getMinStar() {
        return minStar;
    }

    public int getMaxStar() {
        return maxStar;
    }

    public int getStar() {
        return star;
    }

    public void setStar(int star) {
        this.star = star;
    }

    public RelicsType getType() {
        return type;
    }

    public void setType(RelicsType type) {
        this.type = type;
    }

    public int getLevel() {
        return level;
    }

    public int getExp() {
        return exp;
    }

    //根据物品星级确定颜色名称
    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        TranslationTextComponent component = new TranslationTextComponent(getRelicsName(this.getTranslationKey(), stack));
        switch (RelicsHelper.getStarFormStack(stack)){
            case 1: return component.mergeStyle(TextFormatting.WHITE);
            case 2: return component.mergeStyle(TextFormatting.GREEN);
            case 3: return component.mergeStyle(TextFormatting.BLUE);
            case 4: return component.mergeStyle(TextFormatting.DARK_PURPLE);
            case 5: return component.mergeStyle(TextFormatting.GOLD);
            default: return component;
        }
    }

    //获取新的名字
    private String getRelicsName(String str, ItemStack stack){
        String name;
        switch (RelicsHelper.getTypeForStack(stack).getId()){
            case 0: name = "head";break;
            case 1: name = "cup";break;
            case 2: name = "clock";break;
            case 3: name = "feather";break;
            case 4: name = "flower";break;
            default:
                throw new IllegalStateException("Unexpected value: " + getType().getId());
        }
        return str.replaceAll("relics", name);
    }

    //获取种类名
    private String getI18nName(){
        String[] split = this.getTranslationKey().split("\\.");
        String[] s = split[split.length - 1].split("_");
        return s[0] == null ? "" : s[0] + "_";
    }
}
