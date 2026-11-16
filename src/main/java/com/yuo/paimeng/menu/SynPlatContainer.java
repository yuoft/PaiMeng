package com.yuo.paimeng.menu;

import com.yuo.paimeng.items.EvolutionDust;
import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.recipe.ModRecipeType;
import com.yuo.paimeng.recipe.SynPlatRecipe;
import com.yuo.paimeng.tiles.SynPlatTile;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.*;

public class SynPlatContainer extends RecipeBookMenu<CraftingContainer> {
    private final SynPlatInventory inputInventory;
    private final SynPlatInventoryResult outputInventory;
    private final SynPlatTile synPlatTile; //存储食材
    private final Level world;
    private final Player player;

    public SynPlatContainer(int id, Inventory playerInventory, FriendlyByteBuf buf){
        this(id, playerInventory , (Container) playerInventory.player.level().getBlockEntity(buf.readBlockPos()));
    }

    public SynPlatContainer(int id, Inventory playerInventory, Container tile) {
        super(PMMenuTypes.synPlatContainer.get(), id);
        this.synPlatTile = (SynPlatTile) tile;
        this.inputInventory = new SynPlatInventory(this, synPlatTile);
        this.outputInventory = new SynPlatInventoryResult(synPlatTile);
        this.player = playerInventory.player;
        this.world = playerInventory.player.level();
        //物品槽
        for (int m = 0; m < 3; m++){
            this.addSlot(new Slot(inputInventory, m, 8 + m * 43, 47));
        }
        //转变物质
        this.addSlot(new Slot(inputInventory, 3, 121,23));
        //输出槽
        this.addSlot(new ModReslutSlot(ModRecipeType.SYN_PLAT, player, inputInventory, outputInventory, 4, 152, 47));
        //添加玩家物品栏
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        //添加玩家快捷栏
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        slotsChanged(inputInventory);
    }

    @Override
    public void slotsChanged(Container matrix) {
        if (this.world.isClientSide) return;
        ServerPlayer serverPlayer = (ServerPlayer)player;
        ItemStack itemstack = ItemStack.EMPTY;
        if (isPotionAndCheng(inputInventory)){
            ItemStack drug = getDrug(inputInventory);
            if (!drug.isEmpty())
                itemstack = drug;
        }else {
            //获取配方
            Optional<SynPlatRecipe> optional = this.world.getRecipeManager().getRecipeFor(ModRecipeType.SYN_PLAT, matrix, this.world);
            if (optional.isPresent()) {
                SynPlatRecipe recipe = optional.get();
                if (this.outputInventory.setRecipeUsed(this.world, serverPlayer, recipe)) {
                    itemstack = recipe.getResult(); //获取配方输出
                }
            }
        }
        this.outputInventory.setItem(4, itemstack); //设置产出
        this.setRemoteSlot(4, itemstack);
        //发包同步数据
        serverPlayer.connection.send(new ClientboundContainerSetSlotPacket(this.containerId, this.incrementStateId(), 4, itemstack));
    }

    /**
     * 输入是否是药水,药剂瓶,善变之尘 输入相同
     * @param inventory 输入物品栏
     * @return 是 true
     */
    public boolean isPotionAndCheng(SynPlatInventory inventory){
        if(inventory.isEmpty()) return false;
        ItemStack slot0 = inventory.getItem(0); //药水
        ItemStack slot1 = inventory.getItem(1); //药水
        ItemStack slot2 = inventory.getItem(2); //瓶子
        ItemStack slot3 = inventory.getItem(3); //转变催化剂
        return slot0.getItem() instanceof PotionItem && slot1.getItem() instanceof PotionItem
                && slot2.getItem() == PMItems.drugBottle.get() && slot3.getItem() == PMItems.shanbianzhichen.get();
    }

    /**
     * 获取药剂
     * @param inventory 输入栏
     * @return 药剂
     */
    private ItemStack getDrug(SynPlatInventory inventory){
        ItemStack slot0 = inventory.getItem(0);
        ItemStack slot1 = inventory.getItem(1);
        ItemStack result = new ItemStack(PMItems.drug.get());

        // 获取两个药水的所有效果
        List<MobEffectInstance> effects0 = PotionUtils.getCustomEffects(slot0);
        if (effects0.isEmpty() && slot0.getOrCreateTag().contains("Potion")) {
            // 如果是基础药水，需要转换一下
            effects0 = new ArrayList<>(PotionUtils.getPotion(slot0).getEffects());
        }

        List<MobEffectInstance> effects1 = PotionUtils.getCustomEffects(slot1);
        if (effects1.isEmpty() && slot1.getOrCreateTag().contains("Potion")) {
            effects1 = new ArrayList<>(PotionUtils.getPotion(slot1).getEffects());
        }

        // 将效果1合并到效果0中（保留强度高的）
        if (!effects0.isEmpty() && !effects1.isEmpty()) {
            // 注意：这里为了不破坏原 List，最好复制一份
            List<MobEffectInstance> mergedList = new ArrayList<>(effects0);
            appendList(mergedList, new ArrayList<>(effects1));

            // 将合并后的效果设置给结果物品
            PotionUtils.setCustomEffects(result, mergedList);
            return result;
        } else if (!effects0.isEmpty()) {
            PotionUtils.setCustomEffects(result, effects0);
            return result;
        } else if (!effects1.isEmpty()) {
            PotionUtils.setCustomEffects(result, effects1);
            return result;
        }

        return ItemStack.EMPTY;
    }

    /**
     * 合并集合 重复保留值高者
     * @param mainList 需要追加的列表
     * @param list 追加列表
     */
    private void appendList(List<MobEffectInstance> mainList, List<MobEffectInstance> list){
        for (MobEffectInstance next : mainList) {
            Iterator<MobEffectInstance> iterator1 = list.iterator();
            while (iterator1.hasNext()) {
                MobEffectInstance next1 = iterator1.next();
                if (next.getEffect() == next1.getEffect()) {
                    next.update(next1); //保留值高者
                    iterator1.remove(); //去除以转移值
                }
            }
        }
        mainList.addAll(list); //讲不重复的
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.synPlatTile.stillValid(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (index == 4){
                if (!this.moveItemStackTo(itemStack1, 5, 41, true)) return ItemStack.EMPTY;
                slot.onQuickCraft(itemStack1, itemstack); //1.20.1: onSlotChange -> onQuickCraft
            } else if (index > 4){
                if (!itemStack1.isDamageableItem()) //包含在配方中
                    if (!this.moveItemStackTo(itemStack1, 0, 3, false)) return ItemStack.EMPTY;
                if (itemstack.getItem() instanceof EvolutionDust) //嬗变之尘
                    if (!this.moveItemStackTo(itemStack1, 3, 4, false)) return ItemStack.EMPTY;
                if (index < 32) { //从物品栏到快捷栏
                    if (!this.moveItemStackTo(itemStack1, 33, 41, false)) return ItemStack.EMPTY;
                } else if (index < 41) {
                    if (!this.moveItemStackTo(itemStack1, 5, 32, false)) return ItemStack.EMPTY;
                }
            }else if (!this.moveItemStackTo(itemStack1, 5, 41, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }

    @Override
    public void fillCraftSlotsStackedContents(StackedContents itemHelperIn) {
        this.inputInventory.fillStackedContents(itemHelperIn);
    }

    @Override
    public void clearCraftingContent() {
        this.inputInventory.clearContent();
        this.outputInventory.setItem(4, ItemStack.EMPTY);
    }

    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipeIn) {
        return recipeIn.matches(this.inputInventory, this.player.level());
    }

    @Override
    public int getResultSlotIndex() {
        return 4;
    }

    @Override
    public int getGridWidth() {
        return 4;
    }

    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public RecipeBookType getRecipeBookType() {
        return null;
    }

    @Override
    public boolean shouldMoveToInventory(int index) {
        return index != this.getResultSlotIndex();
    }
}
