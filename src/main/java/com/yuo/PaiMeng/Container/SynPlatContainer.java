package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Items.EvolutionDust;
import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.Recipes.ModRecipeType;
import com.yuo.PaiMeng.Recipes.SynPlatRecipe;
import com.yuo.PaiMeng.Tiles.SynPlatTile;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.level.block.Blocks;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

//1.20.1: RecipeBookContainer<CraftingInventory> -> RecipeBookMenu<CraftingContainer>
public class SynPlatContainer extends RecipeBookMenu<CraftingContainer> {
    private final SynPlatInventory inputInventory;
    private final SynPlatInventoryResult outputInventory;
    private final SynPlatTile synPlatTile; //存储食材
    private final Level world;
    private final Player player;

    public SynPlatContainer(int id, Inventory playerInventory){
        //1.20.1 的方块实体没有无参构造，这里创建一个只用于客户端的空容器
        this(id, playerInventory , new SynPlatTile(BlockPos.ZERO, Blocks.AIR.defaultBlockState()));
    }

    public SynPlatContainer(int id, Inventory playerInventory, SynPlatTile tile) {
        super(ContainerTypeRegistry.synPlatContainer.get(), id);
        this.synPlatTile = tile;
        this.inputInventory = new SynPlatInventory(this, tile);
        this.outputInventory = new SynPlatInventoryResult(tile);
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

    //输入改变时设置输出 1.20.1: onCraftMatrixChanged -> slotsChanged
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
                //1.20.1: canUseRecipe -> RecipeHolder#setRecipeUsed(Level, ServerPlayer, Recipe)
                if (this.outputInventory.setRecipeUsed(this.world, serverPlayer, recipe)) {
                    itemstack = recipe.getCraftingResult(matrix); //获取配方输出
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
    private boolean isPotionAndCheng(SynPlatInventory inventory){
        if(inventory.isEmpty()) return false;
        ItemStack slot0 = inventory.getItem(0);
        ItemStack slot1 = inventory.getItem(1);
        ItemStack slot2 = inventory.getItem(2);
        ItemStack slot3 = inventory.getItem(3); //转呗催化剂
        return isPotionEqual(slot0, slot1) && slot0.getItem() instanceof PotionItem
                && slot2.getItem() == PMItems.drugBottle.get() && slot1.getItem() instanceof PotionItem
                && slot3.getItem() == PMItems.shanbianzhichen.get();
    }

    /**
     * 判断物品是否相同 药水类型是否相同
     * @param stack 物品0
     * @param stack1 物品1
     * @return 相同 true
     */
    private boolean isPotionEqual(ItemStack stack, ItemStack stack1){
        if (ItemStack.isSameItem(stack, stack1)){ //物品相同（1.20.1: isItemEqual -> isSameItem）
            if (stack.getOrCreateTag().contains("Potion")){ //酿造药水
                Potion potion0 = PotionUtils.getPotion(stack);
                Potion potion1 = PotionUtils.getPotion(stack1);
                return potion0 == potion1;
            }else if (stack.getOrCreateTag().contains("CustomPotionEffects")){
                List<MobEffectInstance> effects0 = PotionUtils.getCustomEffects(stack);
                List<MobEffectInstance> effects1 = PotionUtils.getCustomEffects(stack1);
                if (effects0.size() != effects1.size()) return false;
                else return effects0.containsAll(effects1);
            }
        }
        return false;
    }

    /**
     * 获取药剂
     * @param inventory 输入栏
     * @return 药剂
     */
    private ItemStack getDrug(SynPlatInventory inventory){
        ItemStack slot0 = inventory.getItem(0);
        if (slot0.getOrCreateTag().contains("Potion")){
            //1.20.1: addPotionToItemStack -> setPotion
            return PotionUtils.setPotion(new ItemStack(PMItems.drug.get()), PotionUtils.getPotion(slot0));
        }else if (slot0.getOrCreateTag().contains("CustomPotionEffects")){
            //1.20.1: appendEffects -> setCustomEffects
            return PotionUtils.setCustomEffects(new ItemStack(PMItems.drug.get()), PotionUtils.getCustomEffects(slot0));
        }else return ItemStack.EMPTY;
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
                if (next.getEffect() == next1.getEffect()) { //1.20.1: getPotion -> getEffect
                    next.update(next1); //保留值高者（1.20.1: combine -> update）
                    iterator1.remove(); //去除以转移值
                }
            }
        }
        mainList.addAll(list); //讲不重复的
    }

    //1.20.1: canInteractWith -> stillValid
    @Override
    public boolean stillValid(Player playerIn) {
        return this.synPlatTile.stillValid(playerIn);
    }

    //玩家shift行为 1.20.1: transferStackInSlot -> quickMoveStack
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
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

    //1.20.1: fillStackedContents -> fillCraftSlotsStackedContents
    @Override
    public void fillCraftSlotsStackedContents(StackedContents itemHelperIn) {
        this.inputInventory.fillStackedContents(itemHelperIn);
    }

    //1.20.1: clear -> clearCraftingContent
    @Override
    public void clearCraftingContent() {
        this.inputInventory.clearContent();
        this.outputInventory.setItem(4, ItemStack.EMPTY);
    }

    //1.20.1: matches -> recipeMatches
    @Override
    public boolean recipeMatches(Recipe<? super CraftingContainer> recipeIn) {
        return recipeIn.matches(this.inputInventory, this.player.level());
    }

    //1.20.1: getOutputSlot -> getResultSlotIndex
    @Override
    public int getResultSlotIndex() {
        return 4;
    }

    //1.20.1: getWidth -> getGridWidth
    @Override
    public int getGridWidth() {
        return 4;
    }

    //1.20.1: getHeight -> getGridHeight
    @Override
    public int getGridHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 5;
    }

    //1.20.1: func_241850_m -> getRecipeBookType
    @Override
    public RecipeBookType getRecipeBookType() {
        return null;
    }

    @Override
    public boolean shouldMoveToInventory(int index) {
        return index != this.getResultSlotIndex();
    }
}
