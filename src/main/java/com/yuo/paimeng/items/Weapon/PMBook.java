package com.yuo.paimeng.items.Weapon;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.entity.EntityTypeRegister;
import com.yuo.paimeng.entity.PMBookBallEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PMBook extends Item {
    private final int tier;
    public PMBook(int tierIn) {
        super(new Properties());
        this.tier = tierIn;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (!worldIn.isClientSide){
            PMBookBallEntity ball = new PMBookBallEntity(EntityTypeRegister.PM_BALL.get(), playerIn, worldIn, getBallDamage());
            ball.shootFromRotation(playerIn, playerIn.getXRot(), playerIn.getYRot(), 0, 1.0f, 1.0f);
            worldIn.addFreshEntity(ball);
            playerIn.playSound(SoundEvents.SNOWBALL_THROW, 1.0f, 3.0f);
            ItemStack heldItem = playerIn.getItemInHand(handIn);
            heldItem.hurtAndBreak(1, playerIn, e -> e.broadcastBreakEvent(handIn));
            playerIn.swing(handIn);
            playerIn.getCooldowns().addCooldown(heldItem.getItem(), 5);
        }
        return super.use(worldIn, playerIn, handIn);
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
    public Component getName(ItemStack stack) {
        MutableComponent component = Component.translatable(this.getDescriptionId(stack));
        return ModUtils.getTierName(this.tier, component);
    }
}
