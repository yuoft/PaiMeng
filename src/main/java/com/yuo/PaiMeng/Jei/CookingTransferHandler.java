package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Container.SynPlatContainer;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.NetWork.TransferPacket;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeSet;

/**
 * 自定义配方转移处理（1.20.1 / JEI 15 版本）。
 * 与 1.16.5 的差别：
 *  - IRecipeTransferHandler 变成了 {@code IRecipeTransferHandler<C, R>}，需要额外实现 getMenuType()/getRecipeType()；
 *  - transferRecipe 第 3 个参数由 IRecipeLayout 变成 IRecipeSlotsView；
 *  - mezz.jei.transfer.RecipeTransferUtil / mezz.jei.config.ServerInfo / mezz.jei.util.Translator 已从 API 移除，
 *    分别用本类的 containsAnyStackIndexed()、IRecipeTransferHandlerHelper#recipeTransferHasServerSupport()、
 *    Component.translatable() 替代。
 */
public class CookingTransferHandler<C extends AbstractContainerMenu, R> implements IRecipeTransferHandler<C, R> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final IStackHelper stackHelper;
    private final IRecipeTransferHandlerHelper handlerHelper;
    private final IRecipeTransferInfo<C, R> transferHelper;

    public CookingTransferHandler(IStackHelper stackHelper, IRecipeTransferHandlerHelper handlerHelper, IRecipeTransferInfo<C, R> transferHelper) {
        this.stackHelper = stackHelper;
        this.handlerHelper = handlerHelper;
        this.transferHelper = transferHelper;
    }

    //配方转移逻辑
    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(C container, R recipe, IRecipeSlotsView recipeSlots, Player player, boolean maxTransfer, boolean doTransfer) {
        if (!handlerHelper.recipeTransferHasServerSupport()) {
            Component tooltipMessage = Component.translatable("jei.tooltip.error.recipe.transfer.no.server");
            return handlerHelper.createUserErrorWithTooltip(tooltipMessage);
        }

        if (!transferHelper.canHandle(container, recipe)) {
            return handlerHelper.createInternalError();
        }

        Map<Integer, Slot> inventorySlots = new HashMap<>();
        for (Slot slot : transferHelper.getInventorySlots(container, recipe)) {
            inventorySlots.put(slot.index, slot);
        }

        Map<Integer, Slot> craftingSlots = new HashMap<>();
        for (Slot slot : transferHelper.getRecipeSlots(container, recipe)) {
            craftingSlots.put(slot.index, slot);
        }

        // JEI 15：每个槽位的候选物品通过 IRecipeSlotView 读取
        List<IRecipeSlotView> slotViews = recipeSlots.getSlotViews();
        Map<Integer, List<ItemStack>> ingredientsMap = new HashMap<>();
        int inputCount = 0;
        for (int key = 0; key < slotViews.size(); key++) {
            IRecipeSlotView slotView = slotViews.get(key);
            if (slotView.getRole() != RecipeIngredientRole.INPUT) {
                continue;
            }
            List<ItemStack> stacks = new ArrayList<>();
            slotView.getItemStacks().filter(stack -> !stack.isEmpty()).forEach(stacks::add);
            ingredientsMap.put(key, stacks);
            if (!stacks.isEmpty()) {
                inputCount++;
            }
        }

        if (inputCount > craftingSlots.size()) {
            LOGGER.error("Recipe Transfer helper {} does not work for container {}", transferHelper.getClass(), container.getClass());
            return handlerHelper.createInternalError();
        }

        Map<Integer, ItemStack> availableItemStacks = new HashMap<>();
        int filledCraftSlotCount = 0;
        int emptySlotCount = 0;

        for (Slot slot : craftingSlots.values()) {
            final ItemStack stack = slot.getItem();
            if (!stack.isEmpty()) {
                if (!slot.mayPickup(player)) {
                    LOGGER.error("Recipe Transfer helper {} does not work for container {}. Player can't move item out of Crafting Slot number {}", transferHelper.getClass(), container.getClass(), slot.index);
                    return handlerHelper.createInternalError();
                }
                filledCraftSlotCount++;
                availableItemStacks.put(slot.index, stack.copy());
            }
        }

        for (Slot slot : inventorySlots.values()) {
            final ItemStack stack = slot.getItem();
            if (!stack.isEmpty()) {
                availableItemStacks.put(slot.index, stack.copy());
            } else {
                emptySlotCount++;
            }
        }

        // check if we have enough inventory space to shuffle items around to their final locations
        if (filledCraftSlotCount - inputCount > emptySlotCount) {
            Component message = Component.translatable("jei.tooltip.error.recipe.transfer.inventory.full");
            return handlerHelper.createUserErrorWithTooltip(message);
        }

        //获取所需物品槽位
        // 修改此处为槽位序号+物品数量
        MatchingItemsResult matchingItemsResult =
                getMatchingItems(stackHelper, availableItemStacks, ingredientsMap, getContainerClass());

        if (matchingItemsResult.missingItems.size() > 0) {
            Component message = Component.translatable("jei.tooltip.error.recipe.transfer.missing");
            List<IRecipeSlotView> missingSlots = new ArrayList<>();
            for (Integer key : matchingItemsResult.missingItems) {
                if (key >= 0 && key < slotViews.size()) {
                    missingSlots.add(slotViews.get(key));
                }
            }
            return handlerHelper.createUserErrorForMissingSlots(message, missingSlots);
        }

        List<Integer> craftingSlotIndexes = new ArrayList<>(craftingSlots.keySet());
        Collections.sort(craftingSlotIndexes);

        List<Integer> inventorySlotIndexes = new ArrayList<>(inventorySlots.keySet());
        Collections.sort(inventorySlotIndexes);

        // check that the slots exist and can be altered
        for (Map.Entry<Integer, MatchingItem> entry : matchingItemsResult.matchingItems.entrySet()) {
            int craftNumber = entry.getKey();
            int slotNumber = craftingSlotIndexes.get(craftNumber);
            if (slotNumber < 0 || slotNumber >= container.slots.size()) {
                LOGGER.error("Recipes Transfer Helper {} references slot {} outside of the inventory's size {}", transferHelper.getClass(), slotNumber, container.slots.size());
                return handlerHelper.createInternalError();
            }
        }

        if (doTransfer) {
            //修改处
            TransferPacket packet = new TransferPacket(matchingItemsResult.matchingItems, craftingSlotIndexes, inventorySlotIndexes, maxTransfer, transferHelper.requireCompleteSets(container, recipe));
            NetWorkHandler.INSTANCE.sendToServer(packet);
        }

        return null;
    }

    @Override
    public Class<? extends C> getContainerClass() {
        return transferHelper.getContainerClass();
    }

    @Override
    public Optional<MenuType<C>> getMenuType() {
        return transferHelper.getMenuType();
    }

    @Override
    public RecipeType<R> getRecipeType() {
        return transferHelper.getRecipeType();
    }

    public static class MatchingItemsResult {
        //配方物品序号 《玩家物品序号，物品数量》
        public final Map<Integer, MatchingItem> matchingItems = new HashMap<>();
        public final List<Integer> missingItems = new ArrayList<>();
    }

    public static class MatchingItem{
        public Integer slotIndex = -1; //序号
        public Integer slotCount = -1; //数量

        public MatchingItem(Integer i, Integer j){
            this.slotIndex = i;
            this.slotCount = j;
        }

        public Integer getSlotIndex() {
            return slotIndex;
        }

        public Integer getSlotCount() {
            return slotCount;
        }
    }

    /**
     * 1.16.5 里调用的是已从 JEI 15 API 移除的 RecipeTransferUtil#containsAnyStackIndexed，
     * 这里按同样的语义（返回第一个匹配到的可用槽位号）本地实现。
     */
    @Nullable
    private static Integer containsAnyStackIndexed(IStackHelper stackHelper, Map<Integer, ItemStack> availableItemStacks, List<ItemStack> requiredStacks) {
        List<Integer> keys = new ArrayList<>(availableItemStacks.keySet());
        Collections.sort(keys);
        for (Integer key : keys) {
            ItemStack available = availableItemStacks.get(key);
            if (available == null || available.isEmpty()) {
                continue;
            }
            for (ItemStack required : requiredStacks) {
                if (required.isEmpty()) {
                    continue;
                }
                if (stackHelper.isEquivalent(available, required, UidContext.Ingredient)) {
                    return key;
                }
            }
        }
        return null;
    }

    public MatchingItemsResult getMatchingItems(IStackHelper stackhelper, Map<Integer, ItemStack> availableItemStacks, Map<Integer, List<ItemStack>> ingredientsMap, Class<? extends C> containerClass) {
        MatchingItemsResult matchingItemResult = new MatchingItemsResult();

        int recipeSlotNumber = -1;
        TreeSet<Integer> keys = new TreeSet<>(ingredientsMap.keySet());
        for (Integer key : keys) {
            List<ItemStack> requiredStacks = ingredientsMap.get(key);
            if (requiredStacks == null || requiredStacks.isEmpty()) {
                continue;
            }
            recipeSlotNumber++;

            Integer matching = containsAnyStackIndexed(stackhelper, availableItemStacks, requiredStacks);
            if (matching == null) {
                matchingItemResult.missingItems.add(key);
            } else {
                //修改处
                ItemStack matchingStack = availableItemStacks.get(matching);
                int count = requiredStacks.get(0).getCount();
                matchingStack.shrink(count);
                if (matchingStack.getCount() <= 0) {
                    availableItemStacks.remove(matching);
                }
                if (SynPlatContainer.class.equals(containerClass)){
                    matchingItemResult.matchingItems.put(key, new MatchingItem(matching, count));
                }
                else matchingItemResult.matchingItems.put(recipeSlotNumber, new MatchingItem(matching, count));
            }
        }

        return matchingItemResult;
    }
}
