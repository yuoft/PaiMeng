package com.yuo.PaiMeng.Jei;

import com.yuo.PaiMeng.Container.SynPlatContainer;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.NetWork.TransferPacket;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.gui.ingredient.IGuiIngredient;
import mezz.jei.api.gui.ingredient.IGuiItemStackGroup;
import mezz.jei.api.helpers.IStackHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferError;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandlerHelper;
import mezz.jei.api.recipe.transfer.IRecipeTransferInfo;
import mezz.jei.config.ServerInfo;
import mezz.jei.transfer.RecipeTransferUtil;
import mezz.jei.util.Translator;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.*;

public class CookingTransferHandler<C extends Container> implements IRecipeTransferHandler<C> {
    private static final Logger LOGGER = LogManager.getLogger();

    private final IStackHelper stackHelper;
    private final IRecipeTransferHandlerHelper handlerHelper;
    private final IRecipeTransferInfo<C> transferHelper;

    public CookingTransferHandler(IStackHelper stackHelper, IRecipeTransferHandlerHelper handlerHelper, IRecipeTransferInfo<C> transferHelper) {
        this.stackHelper = stackHelper;
        this.handlerHelper = handlerHelper;
        this.transferHelper = transferHelper;
    }

    //配方转移逻辑
    @Nullable
    @Override
    public IRecipeTransferError transferRecipe(C container, Object recipe, IRecipeLayout recipeLayout, PlayerEntity player, boolean maxTransfer, boolean doTransfer) {
        if (!ServerInfo.isJeiOnServer()) {
            String tooltipMessage = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.no.server");
            return handlerHelper.createUserErrorWithTooltip(tooltipMessage);
        }

        if (!transferHelper.canHandle(container)) {
            return handlerHelper.createInternalError();
        }

        Map<Integer, Slot> inventorySlots = new HashMap<>();
        for (Slot slot : transferHelper.getInventorySlots(container)) {
            inventorySlots.put(slot.slotNumber, slot);
        }

        Map<Integer, Slot> craftingSlots = new HashMap<>();
        for (Slot slot : transferHelper.getRecipeSlots(container)) {
            craftingSlots.put(slot.slotNumber, slot);
        }

        int inputCount = 0;
        IGuiItemStackGroup itemStackGroup = recipeLayout.getItemStacks();
        for (IGuiIngredient<ItemStack> ingredient : itemStackGroup.getGuiIngredients().values()) {
            if (ingredient.isInput() && !ingredient.getAllIngredients().isEmpty()) {
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
            final ItemStack stack = slot.getStack();
            if (!stack.isEmpty()) {
                if (!slot.canTakeStack(player)) {
                    LOGGER.error("Recipe Transfer helper {} does not work for container {}. Player can't move item out of Crafting Slot number {}", transferHelper.getClass(), container.getClass(), slot.slotNumber);
                    return handlerHelper.createInternalError();
                }
                filledCraftSlotCount++;
                availableItemStacks.put(slot.slotNumber, stack.copy());
            }
        }

        for (Slot slot : inventorySlots.values()) {
            final ItemStack stack = slot.getStack();
            if (!stack.isEmpty()) {
                availableItemStacks.put(slot.slotNumber, stack.copy());
            } else {
                emptySlotCount++;
            }
        }

        // check if we have enough inventory space to shuffle items around to their final locations
        if (filledCraftSlotCount - inputCount > emptySlotCount) {
            String message = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.inventory.full");
            return handlerHelper.createUserErrorWithTooltip(message);
        }

        //获取所需物品槽位
        // 修改此处为槽位序号+物品数量
        MatchingItemsResult matchingItemsResult =
                getMatchingItems(stackHelper, availableItemStacks, itemStackGroup.getGuiIngredients(), getContainerClass());

        if (matchingItemsResult.missingItems.size() > 0) {
            String message = Translator.translateToLocal("jei.tooltip.error.recipe.transfer.missing");
            return handlerHelper.createUserErrorForSlots(message, matchingItemsResult.missingItems);
        }

        List<Integer> craftingSlotIndexes = new ArrayList<>(craftingSlots.keySet());
        Collections.sort(craftingSlotIndexes);

        List<Integer> inventorySlotIndexes = new ArrayList<>(inventorySlots.keySet());
        Collections.sort(inventorySlotIndexes);

        // check that the slots exist and can be altered
        for (Map.Entry<Integer, MatchingItem> entry : matchingItemsResult.matchingItems.entrySet()) {
            int craftNumber = entry.getKey();
            int slotNumber = craftingSlotIndexes.get(craftNumber);
            if (slotNumber < 0 || slotNumber >= container.inventorySlots.size()) {
                LOGGER.error("Recipes Transfer Helper {} references slot {} outside of the inventory's size {}", transferHelper.getClass(), slotNumber, container.inventorySlots.size());
                return handlerHelper.createInternalError();
            }
        }

        if (doTransfer) {
            //修改处
            TransferPacket packet = new TransferPacket(matchingItemsResult.matchingItems, craftingSlotIndexes, inventorySlotIndexes, maxTransfer, transferHelper.requireCompleteSets());
            NetWorkHandler.INSTANCE.sendToServer(packet);
        }

        return null;
    }

    @Override
    public Class<C> getContainerClass() {
        return transferHelper.getContainerClass();
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

    public MatchingItemsResult getMatchingItems(IStackHelper stackhelper, Map<Integer, ItemStack> availableItemStacks, Map<Integer, ? extends IGuiIngredient<ItemStack>> ingredientsMap, Class<C> containerClass) {
        MatchingItemsResult matchingItemResult = new MatchingItemsResult();

        int recipeSlotNumber = -1;
        SortedSet<Integer> keys = new TreeSet<>(ingredientsMap.keySet());
        for (Integer key : keys) {
            IGuiIngredient<ItemStack> ingredient = ingredientsMap.get(key);
            if (!ingredient.isInput()) {
                continue;
            }
            recipeSlotNumber++;

            List<ItemStack> requiredStacks = ingredient.getAllIngredients();
            if (requiredStacks.isEmpty()) {
                continue;
            }

            Integer matching = RecipeTransferUtil.containsAnyStackIndexed(stackhelper, availableItemStacks, requiredStacks);
            if (matching == null) {
                matchingItemResult.missingItems.add(key);
            } else {
                //修改处
                ItemStack matchingStack = availableItemStacks.get(matching);
                int count = requiredStacks.get(0).getCount();
                matchingStack.shrink(count);
                if (matchingStack.getCount() == 0) {
                    availableItemStacks.remove(matching);
                }
                if (containerClass.equals(SynPlatContainer.class)){
                    matchingItemResult.matchingItems.put(key, new MatchingItem(matching, count));
                }
                else matchingItemResult.matchingItems.put(recipeSlotNumber, new MatchingItem(matching, count));
            }
        }

        return matchingItemResult;
    }
}
