package com.yuo.PaiMeng.Items.Weapon;

import com.yuo.PaiMeng.Entity.EntityTypeRegister;
import com.yuo.PaiMeng.Entity.PMBookBallEntity;
import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.tab.PMGroup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class PMBook extends Item {
    private final int tier;
    public PMBook(int tierIn) {
        super(new Properties().group(PMGroup.PaiMengWeapon));
        this.tier = tierIn;
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote){
            PMBookBallEntity ball = new PMBookBallEntity(EntityTypeRegister.PM_BALL.get(), playerIn, worldIn, getBallDamage());
            ball.setDirectionAndMovement(playerIn, playerIn.rotationPitch, playerIn.rotationYaw,
                    0, 1.0f,1.0f);
            worldIn.addEntity(ball);
            playerIn.playSound(SoundEvents.ENTITY_SNOWBALL_THROW, 1.0f, 3.0f);
            ItemStack heldItem = playerIn.getHeldItem(handIn);
            heldItem.damageItem(1, playerIn, e -> e.getHeldItem(handIn));
            playerIn.swingArm(handIn);
            playerIn.getCooldownTracker().setCooldown(heldItem.getItem(), 5);
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    public float getBallDamage(){
        float damage = 0.1f;
        switch (tier){
            case 1: damage = 3.0f;break;
            case 2: damage = 7.0f;break;
            case 3: damage = 11.0f;break;
            case 4: damage = 16.0f;break;
            case 5: damage = 25.0f;break;
            default: break;
        }
        return damage;
    }

    @Override
    public ITextComponent getDisplayName(ItemStack stack) {
        TranslationTextComponent component = new TranslationTextComponent(getTranslationKey(stack));
        switch (tier) {
            case 1: return component.mergeStyle(TextFormatting.WHITE);
            case 2: return component.mergeStyle(TextFormatting.GREEN);
            case 3: return component.mergeStyle(TextFormatting.BLUE);
            case 4: return component.mergeStyle(TextFormatting.DARK_PURPLE);
            case 5: return component.mergeStyle(TextFormatting.GOLD);
            default: return component;
        }
    }
}
