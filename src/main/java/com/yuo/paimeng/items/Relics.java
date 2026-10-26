package com.yuo.paimeng.items;

import com.yuo.paimeng.tab.PMGroup;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

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
        super(new Properties().stacksTo(1).fireResistant());
        this.type = RelicsType.EMPTY;
        this.minStar = minLevel;
        this.maxStar = maxLevel;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        RelicsType relicsType = RelicsHelper.getTypeForStack(stack);
        //部位
        tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_" + relicsType.getName()));
        int star = RelicsHelper.getStarFormStack(stack);
        //等级
        if (RelicsHelper.hasLevelNbt(stack)){
            int relicsLevel = RelicsHelper.getRelicsLevel(stack);
            tooltip.add(Component.literal("+" + relicsLevel).withStyle(ChatFormatting.GOLD));
            if (relicsLevel == star * 4){
                tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_exp_max").withStyle(ChatFormatting.RED));
            }else tooltip.add(Component.literal( RelicsHelper.getRelicsExp(stack)+ "/" + RelicsHelper.getUpExpForLevel(star, relicsLevel)).withStyle(ChatFormatting.DARK_PURPLE));
        }
        //描述
        tooltip.add(Component.translatable("paimeng.text.itemInfo." + getI18nName() + relicsType.getName()));
        //属性
        tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_attr"));
        if (RelicsHelper.hasMainAttrNbt(stack)) {
            RelicsHelper.RandomAttrValue mainAttr = RelicsHelper.getMainAttrTypeFormStack(stack);
            tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_" + mainAttr.getAttrType().getTip(),
                    RelicsHelper.getMathTwo(mainAttr)).withStyle(ChatFormatting.BLUE));
        }
        else tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_no_attr").withStyle(ChatFormatting.BLUE));
        if (star > 1){
            tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_attr1"));
            if (RelicsHelper.hasViceAttr(stack)){
                for (RelicsHelper.RandomAttrValue type : RelicsHelper.getViceAttrTypeFormStack(stack)) {
                    if (type.getAttrType().getId() != 0){
                        tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_" + type.getAttrType().getTip(),
                                RelicsHelper.getMathTwo(type)).withStyle(ChatFormatting.BLUE));
                    }
                }
            }
            else tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_no_attr").withStyle(ChatFormatting.BLUE));
        }
    }

    //生成同一圣遗物不同星级
    //1.20.1：Item#fillItemGroup(ItemGroup, NonNullList) 已删除，创造页内容改由 CreativeModeTab#displayItems 决定。
    //这里保留一个同义方法（不再是 @Override）供 tab.PMGroup / 其它代码显式调用，
    //同时提供静态的 fillCreativeItems(CreativeModeTab.Output) 方便在物品栏构建时统一收集。
    public void fillItemCategory(CreativeModeTab group, CreativeModeTab.Output items) {
        if (group == PMGroup.PaiMengRelics.get()){
            for (int i = this.minStar; i <= this.maxStar; i++){ //星级
                for (int j = 0; j < 5 ; j++){ //部位
                    items.accept(getItemStack(i, j));
                }
            }
        }
    }

    /**
     * 把所有圣遗物按 星级 × 部位 展开后加入创造模式物品栏。
     * 1.20.1 中创造页内容由 CreativeModeTab#displayItems 生成，PMGroup 里调用本方法即可保留
     * 1.16.5 里 fillItemGroup 的展示效果（每个星级 / 部位各一个）。
     * @param items 物品栏输出
     */
    public static void fillCreativeItems(CreativeModeTab.Output items) {
        for (RegistryObject<Item> entry : PMItems.ITEMS.getEntries()) {
            Item item = entry.get();
            if (item instanceof Relics relics) {
                for (int i = relics.getMinStar(); i <= relics.getMaxStar(); i++) { //星级
                    for (int j = 0; j < 5; j++) { //部位
                        items.accept(relics.getItemStack(i, j));
                    }
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
        CompoundTag orCreateTag = stack.getOrCreateTag();
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(RelicsHelper.NBT_STAR, i);
        nbt.putInt(RelicsHelper.NBT_TYPE, j);
        orCreateTag.put(RelicsHelper.NBT_RELICS, nbt);
        return stack;
    }

    public int getMinStar() {
        return this.minStar;
    }

    public int getMaxStar() {
        return this.maxStar;
    }

    public int getStar() {
        return this.star;
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
    //1.16.5 的 getDisplayName(ItemStack) -> 1.20.1 的 getName(ItemStack)
    @Override
    public Component getName(ItemStack stack) {
        MutableComponent component = Component.translatable(getDescriptionId(stack));
        switch (RelicsHelper.getStarFormStack(stack)){
            case 1: return component.withStyle(ChatFormatting.WHITE);
            case 2: return component.withStyle(ChatFormatting.GREEN);
            case 3: return component.withStyle(ChatFormatting.BLUE);
            case 4: return component.withStyle(ChatFormatting.DARK_PURPLE);
            case 5: return component.withStyle(ChatFormatting.GOLD);
            default: return component;
        }
    }

    //1.16.5 的 getTranslationKey(ItemStack) -> 1.20.1 的 getDescriptionId(ItemStack)
    @Override
    public String getDescriptionId(ItemStack stack) {
        return getRelicsName(getDescriptionId(), stack);
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
                return "null_relics";
        }
        return str.replaceAll("relics", name);
    }

    //获取种类名
    private String getI18nName(){
        String[] split = this.getDescriptionId().split("\\.");
        String[] s = split[split.length - 1].split("_");
        return s[0] == null ? "" : s[0] + "_";
    }
}
