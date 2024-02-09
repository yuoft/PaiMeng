package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.tab.PMGroup;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

//圣遗物匣
public class RelicsBox extends Item {
    private int level;

    private final static List<Item> RELICS_FIVE = Arrays.asList(PMItems.juedoushiRelics.get(), PMItems.yuetuanRelics.get());
    private final static List<Item> RELICS_FOUR = Arrays.asList(PMItems.yongshiRelics.get(), PMItems.liufangzheRelics.get(),
            PMItems.jiaoguanRelics.get());
    private final static List<Item> RELICS_THREE = Arrays.asList(PMItems.xingyunerRelics.get(), PMItems.youyiRelics.get(),
            PMItems.maoxianjiaRelics.get());

    public RelicsBox(int level) {
        super(new Properties().group(PMGroup.PaiMengRelics).isImmuneToFire());
        this.level = level;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.relics_box" + level));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        ItemStack heldItem = playerIn.getHeldItem(handIn);
        if (!worldIn.isRemote){
            switch (getLevel()){
                case 1:
                    ItemStack relics = getOneRelics(RELICS_FIVE);
                    addRelics(relics, playerIn, worldIn, heldItem);
                    break;
                case 2:
                    ItemStack relics1 = getOneRelics(RELICS_FOUR);
                    addRelics(relics1, playerIn, worldIn, heldItem);
                    break;
                case 3:
                    ItemStack relics2 = getOneRelics(RELICS_THREE);
                    addRelics(relics2, playerIn, worldIn, heldItem);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + getLevel());
            }
        }
        return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
    }

    /**
     * 获取一个随机圣遗物
     * @param list 圣遗物种类
     * @return 圣遗物
     */
    private ItemStack getOneRelics(List<Item> list){
        ItemStack relics = new ItemStack(list.get(random.nextInt(list.size())));
        RelicsHelper.addRelicsBaseAttr(relics);
        return relics;
    }

    /**
     * 添加物品时间生成 消耗圣物匣
     * @param relics 圣遗物
     * @param player 玩家
     * @param world 世界
     * @param heldItem 圣遗匣
     */
    private void addRelics(ItemStack relics, PlayerEntity player, World world, ItemStack heldItem){
        if (!relics.isEmpty()){
            BlockPos pos = player.getPosition();
            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), relics));
        }
        if (!player.isCreative())
            heldItem.shrink(1);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        TranslationTextComponent component = new TranslationTextComponent(this.getTranslationKey());
        switch (level){
            case 1: return component.mergeStyle(TextFormatting.GOLD);
            case 2: return component.mergeStyle(TextFormatting.DARK_PURPLE);
            case 3: return component.mergeStyle(TextFormatting.BLUE);
            default: return component;
        }
    }
}
