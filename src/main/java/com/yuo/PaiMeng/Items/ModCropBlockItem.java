package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.tab.ModGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

//种子
public class ModCropBlockItem extends BlockNamedItem {
    public static final Item.Properties GROUP = new Item.Properties().group(ModGroup.PaiMengCrop);
    private CropUseBlockEnum useBlockEnum;

    public ModCropBlockItem(Block blockIn, CropUseBlockEnum cropUseBlockEnum) {
        super(blockIn, GROUP);
        this.useBlockEnum = cropUseBlockEnum;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        CropUseBlockEnum useBlockEnum = getUseBlockEnum();
        switch (useBlockEnum){
            case COMMON:
                tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.common_seed"));break;
            case FERTILE:
                tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.fertile_seed"));break;
            case AQUATIC:
                tooltip.add(new TranslationTextComponent("paimeng.text.itemInfo.aquatic_seed"));break;
        }
    }

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        PlayerEntity player = context.getPlayer();
        BlockState blockGrow = world.getBlockState(pos);//泥土
        switch (useBlockEnum) {
            case COMMON:
                if (blockGrow.matchesBlock(BlockRegistry.commonFarmland.get()))
                    return resultType(context);
                else return funb(world, player);
            case FERTILE:
                if (blockGrow.matchesBlock(BlockRegistry.fertileFarmland.get()))
                    return resultType(context);
                else return funb(world, player);
            case AQUATIC:
                if (blockGrow.matchesBlock(BlockRegistry.aquaticFarmland.get()))
                    return resultType(context);
                else return funb(world, player);
            default:
                return funb(world, player);
        }
    }

    //原版种植
    private ActionResultType resultType(ItemUseContext context){
        ActionResultType actionresulttype = this.tryPlace(new BlockItemUseContext(context));
        return !actionresulttype.isSuccessOrConsume() && this.isFood() ? this.onItemRightClick(context.getWorld(), context.getPlayer(), context.getHand()).getType() : actionresulttype;
    }


    //消息
    private ActionResultType funb(World world, PlayerEntity playerentity){
        if (!world.isRemote)
            playerentity.sendStatusMessage(new TranslationTextComponent("paimeng.message.un_plant"), true);
        return ActionResultType.FAIL;
    }

    public CropUseBlockEnum getUseBlockEnum() {
        return useBlockEnum;
    }
}
