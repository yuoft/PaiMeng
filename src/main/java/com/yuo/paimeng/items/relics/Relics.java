package com.yuo.paimeng.items.relics;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.capability.ModCapability;
import com.yuo.paimeng.capability.RelicsItemHandler;
import com.yuo.paimeng.event.EventHelper;
import com.yuo.paimeng.items.PMItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
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
    private final GodEyeType godEyeType;

    public Relics(int minLevel, int maxLevel) {
        super(new Properties().stacksTo(1).fireResistant());
        this.type = RelicsType.EMPTY;
        this.minStar = minLevel;
        this.maxStar = maxLevel;
        this.godEyeType = null;
    }

    /** 神之眼*/
    public Relics(GodEyeType godEyeType){
        super(new Properties().stacksTo(1).fireResistant());
        this.type = RelicsType.EYE;
        this.minStar = 5;
        this.maxStar = 5;
        this.godEyeType = godEyeType;
    }

    /** 神之眼元素；普通圣遗物返回 null */
    public GodEyeType getGodEyeType() {
        return godEyeType;
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
        if (godEyeType == null)
            tooltip.add(Component.translatable("paimeng.text.itemInfo." + getI18nName() + relicsType.getName()));
        else {
            tooltip.add(Component.translatable("paimeng.text.itemInfo.god_eye"));
            tooltip.add(Component.translatable("paimeng.text.god_eye."+ godEyeType.getId() + ".desc"));
        }
        //属性
        if (godEyeType != null){
            if (!(godEyeType == GodEyeType.WIND || godEyeType == GodEyeType.STONE || godEyeType == GodEyeType.WATER)) return;

        }
        tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_attr"));
        if (RelicsHelper.hasMainAttrNbt(stack)) {
            RelicsHelper.RandomAttrValue mainAttr = RelicsHelper.getMainAttrTypeFormStack(stack);
            tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_" + mainAttr.getAttrType().getTip(),
                    RelicsHelper.getMathTwo(mainAttr)).withStyle(ChatFormatting.BLUE));
        } else {
            if (godEyeType == null)
                tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_no_attr").withStyle(ChatFormatting.BLUE));
        }
        if (star > 1){
            if (godEyeType == GodEyeType.WATER) return;
            tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_attr1"));
            if (RelicsHelper.hasViceAttr(stack)){
                for (RelicsHelper.RandomAttrValue type : RelicsHelper.getViceAttrTypeFormStack(stack)) {
                    if (type.getAttrType().getId() != 0){
                        tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_" + type.getAttrType().getTip(),
                                RelicsHelper.getMathTwo(type)).withStyle(ChatFormatting.BLUE));
                    }
                }
            } else tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_no_attr").withStyle(ChatFormatting.BLUE));
        }
    }

    @Override
    public void onInventoryTick(ItemStack stack, Level level, Player player, int slotIndex, int selectedIndex) {
        super.onInventoryTick(stack, level, player, slotIndex, selectedIndex);
        if (godEyeType != null){
            if (!RelicsHelper.hasMainAttrNbt(stack)){
                RelicsHelper.addRelicsBaseAttr0(stack);
            }
        }
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack relics = player.getItemInHand(hand);
        if (relics.getItem() instanceof Relics){ //快速装备圣遗物
            if (player.isShiftKeyDown()){
                // 为没有属性的圣遗物初始化属性
                if (!RelicsHelper.hasMainAttrNbt(relics)){
                    RelicsHelper.addRelicsBaseAttr0(relics);
                }
            }else {
                LazyOptional<RelicsItemHandler> capability = player.getCapability(ModCapability.RELICS_CAPABILITY);
                capability.ifPresent(e ->{
                    NonNullList<ItemStack> stacks = e.getStacks();
                    EventHelper.wearRelics(player, hand , level, RelicsHelper.getTypeForStack(relics).getId(), stacks, relics);
                });
            }
        }
        return super.use(level, player, hand);
    }

    /**
     * 把所有圣遗物按 星级 × 部位 展开后加入创造模式物品栏。
     * @param items 物品栏输出
     */
    public static void fillCreativeItems(CreativeModeTab.Output items) {
        for (RegistryObject<Item> entry : PMItems.ITEMS.getEntries()) {
            Item item = entry.get();
            if (item instanceof Relics relics) {
                if (relics.getGodEyeType() != null) { //神之眼没有星级/部位变体
                    items.accept(relics.getItemStack(5, 5));
                    continue;
                }
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
    @Override
    public Component getName(ItemStack stack) {
        MutableComponent component = Component.translatable(getDescriptionId(stack));
        if (godEyeType != null) { //神之眼按元素上色
            return component.withStyle(godEyeType.getColor());
        }
        return ModUtils.getTierName(RelicsHelper.getStarFormStack(stack), component);
    }

    @Override
    public String getDescriptionId(ItemStack stack) {
        if (godEyeType != null) { //神之眼没有部位后缀，直接用物品 id
            return getDescriptionId();
        }
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
