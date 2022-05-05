package com.yuo.PaiMeng.Container;

import com.yuo.PaiMeng.Items.EvolutionDust;
import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.Recipes.ModRecipeType;
import com.yuo.PaiMeng.Recipes.SynPlatRecipe;
import com.yuo.PaiMeng.Tiles.SynPlatTile;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.IRecipeHelperPopulator;
import net.minecraft.inventory.container.RecipeBookContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.RecipeBookCategory;
import net.minecraft.item.crafting.RecipeItemHelper;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.world.World;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class SynPlatContainer extends RecipeBookContainer<CraftingInventory> {
    private final SynPlatInventory inputInventory;
    private final SynPlatInventoryResult outputInventory;
    private final SynPlatTile synPlatTile; //存储食材
    private final World world;
    private final PlayerEntity player;

    public SynPlatContainer(int id, PlayerInventory playerInventory){
        this(id, playerInventory , new SynPlatTile());
    }

    public SynPlatContainer(int id, PlayerInventory playerInventory, SynPlatTile tile) {
        super(ContainerTypeRegistry.synPlatContainer.get(), id);
        this.synPlatTile = tile;
        this.inputInventory = new SynPlatInventory(this, tile);
        this.outputInventory = new SynPlatInventoryResult(tile);
        this.player = playerInventory.player;
        this.world = playerInventory.player.world;
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

        onCraftMatrixChanged(inputInventory);
    }

    //输入改变时设置输出
    @Override
    public void onCraftMatrixChanged(IInventory matrix) {
        if (world.isRemote) return;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity)player;
        ItemStack itemstack = ItemStack.EMPTY;
        if (isPotionAndCheng(inputInventory)){
            ItemStack drug = getDrug(inputInventory);
            if (!drug.isEmpty())
                itemstack = drug;
        }else {
            //获取配方
            Optional<SynPlatRecipe> optional = world.getRecipeManager().getRecipe(ModRecipeType.SYN_PLAT, matrix, world);
            if (optional.isPresent()) {
                SynPlatRecipe recipe = optional.get();
                if (outputInventory.canUseRecipe(world, serverPlayer, recipe)) {
                    itemstack = recipe.getCraftingResult(matrix); //获取配方输出
                }
            }
        }
        outputInventory.setInventorySlotContents(4, itemstack); //设置产出
        serverPlayer.connection.sendPacket(new SSetSlotPacket(windowId, 4, itemstack)); //发包同步数据
    }

    /**
     * 输入是否是药水,药剂瓶,善变之尘 输入相同
     * @param inventory 输入物品栏
     * @return 是 true
     */
    private boolean isPotionAndCheng(SynPlatInventory inventory){
        if(inventory.isEmpty()) return false;
        ItemStack slot0 = inventory.getStackInSlot(0);
        ItemStack slot1 = inventory.getStackInSlot(1);
        ItemStack slot2 = inventory.getStackInSlot(2);
        ItemStack slot3 = inventory.getStackInSlot(3); //转呗催化剂
        return isPotionEqual(slot0, slot1) && slot0.getItem() instanceof PotionItem
                && slot2.getItem() == ItemRegistry.drugBottle.get() && slot1.getItem() instanceof PotionItem
                && slot3.getItem() == ItemRegistry.shanbianzhichen.get();
    }

    /**
     * 判断物品是否相同 药水类型是否相同
     * @param stack 物品0
     * @param stack1 物品1
     * @return 相同 true
     */
    private boolean isPotionEqual(ItemStack stack, ItemStack stack1){
        if (stack.isItemEqual(stack1)){ //物品相同
            if (stack.getOrCreateTag().contains("Potion")){ //酿造药水
                Potion potion0 = PotionUtils.getPotionFromItem(stack);
                Potion potion1 = PotionUtils.getPotionFromItem(stack1);
                return potion0 == potion1;
            }else if (stack.getOrCreateTag().contains("CustomPotionEffects")){
                List<EffectInstance> effects0 = PotionUtils.getEffectsFromStack(stack);
                List<EffectInstance> effects1 = PotionUtils.getEffectsFromStack(stack1);
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
        ItemStack slot0 = inventory.getStackInSlot(0);
        if (slot0.getOrCreateTag().contains("Potion")){
            return PotionUtils.addPotionToItemStack(new ItemStack(ItemRegistry.drug.get()), PotionUtils.getPotionFromItem(slot0));
        }else if (slot0.getOrCreateTag().contains("CustomPotionEffects")){
            return PotionUtils.appendEffects(new ItemStack(ItemRegistry.drug.get()), PotionUtils.getEffectsFromStack(slot0));
        }else return ItemStack.EMPTY;
    }

    /**
     * 合并集合 重复保留值高者
     * @param mainList 需要追加的列表
     * @param list 追加列表
     */
    private void appendList(List<EffectInstance> mainList, List<EffectInstance> list){
        for (EffectInstance next : mainList) {
            Iterator<EffectInstance> iterator1 = list.iterator();
            while (iterator1.hasNext()) {
                EffectInstance next1 = iterator1.next();
                if (next.getPotion() == next1.getPotion()) {
                    next.combine(next1); //保留值高者
                    iterator1.remove(); //去除以转移值
                }
            }
        }
        mainList.addAll(list); //讲不重复的
    }

    @Override
    public boolean canInteractWith(PlayerEntity playerIn) {
        return this.synPlatTile.isUsableByPlayer(playerIn);
    }

    //玩家shift行为
    @Override
    public ItemStack transferStackInSlot(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if (slot != null && slot.getHasStack()) {
            ItemStack itemStack1 = slot.getStack();
            itemstack = itemStack1.copy();
            if (index == 4){
                if (!this.mergeItemStack(itemStack1, 5, 41, true)) return ItemStack.EMPTY;
                slot.onSlotChange(itemStack1, itemstack);
            } else if (index > 4){
                if (!itemStack1.isDamageable()) //包含在配方中
                    if (!this.mergeItemStack(itemStack1, 0, 3, false)) return ItemStack.EMPTY;
                if (itemstack.getItem() instanceof EvolutionDust) //嬗变之尘
                    if (!this.mergeItemStack(itemStack1, 3, 4, false)) return ItemStack.EMPTY;
                if (index < 32) { //从物品栏到快捷栏
                    if (!this.mergeItemStack(itemStack1, 33, 41, false)) return ItemStack.EMPTY;
                } else if (index < 41) {
                    if (!this.mergeItemStack(itemStack1, 5, 32, false)) return ItemStack.EMPTY;
                }
            }else if (!this.mergeItemStack(itemStack1, 5, 41, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
            else slot.onSlotChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }

    @Override
    public void fillStackedContents(RecipeItemHelper itemHelperIn) {
        if (this.inputInventory instanceof IRecipeHelperPopulator) {
            ((IRecipeHelperPopulator)this.inputInventory).fillStackedContents(itemHelperIn);
        }
    }

    @Override
    public void clear() {
        this.inputInventory.clear();
        this.outputInventory.clear();
    }

    @Override
    public boolean matches(IRecipe<? super CraftingInventory> recipeIn) {
        return recipeIn.matches(this.inputInventory, this.player.world);
    }

    @Override
    public int getOutputSlot() {
        return 4;
    }

    @Override
    public int getWidth() {
        return 4;
    }

    @Override
    public int getHeight() {
        return 1;
    }

    @Override
    public int getSize() {
        return 5;
    }

    @Override
    public RecipeBookCategory func_241850_m() {
        return null;
    }
}
