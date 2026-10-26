package com.yuo.paimeng.items;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

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
        super(new Properties().fireResistant());
        this.level = level;
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(Component.translatable("paimeng.text.itemInfo.relics_box" + level));
    }

    //1.16.5 的 onItemRightClick(World, PlayerEntity, Hand) -> 1.20.1 的 use(Level, Player, InteractionHand)
    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack heldItem = playerIn.getItemInHand(handIn);
        if (!worldIn.isClientSide){
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
        return InteractionResultHolder.success(heldItem);
    }

    /**
     * 获取一个随机圣遗物
     * @param list 圣遗物种类
     * @return 圣遗物
     */
    private ItemStack getOneRelics(List<Item> list){
        ItemStack relics = new ItemStack(list.get(RelicsHelper.random.nextInt(list.size())));
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
    private void addRelics(ItemStack relics, Player player, Level world, ItemStack heldItem){
        if (!relics.isEmpty()){
            BlockPos pos = player.blockPosition();
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), relics));
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

    //1.16.5 的 getDisplayName(ItemStack) -> 1.20.1 的 getName(ItemStack)
    @Override
    public Component getName(ItemStack stack) {
        MutableComponent component = Component.translatable(this.getDescriptionId());
        switch (level){
            case 1: return component.withStyle(ChatFormatting.GOLD);
            case 2: return component.withStyle(ChatFormatting.DARK_PURPLE);
            case 3: return component.withStyle(ChatFormatting.BLUE);
            default: return component;
        }
    }
}
