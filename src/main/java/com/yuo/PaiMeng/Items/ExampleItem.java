package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.Capability.IBlowCapability;
import com.yuo.PaiMeng.Capability.ModCapability;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class ExampleItem extends Item {
    public ExampleItem() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level worldIn, Player playerIn, InteractionHand handIn) {
        if (!worldIn.isClientSide && handIn == InteractionHand.MAIN_HAND){
            LazyOptional<IBlowCapability> capability = playerIn.getCapability(ModCapability.BLOW_CAPABILITY);
            //当返回值不是null时需要做什么。这里的lambda表达式参数e就是之前getCapbility调用的返回值
            capability.ifPresent( e -> {
                double criticalRate = e.getCriticalRate();
                double criticalDamage = e.getCriticalDamage();
                playerIn.sendSystemMessage(Component.literal("暴击率：" + getMath(criticalRate) + " 暴击伤害：" + getMath(criticalDamage)
                + " 防御力：" + getAttrValue(Attributes.ARMOR, playerIn) + " 攻击力：" + getAttrValue(Attributes.ATTACK_DAMAGE, playerIn)
                + "生命值：" + getAttrValue(Attributes.MAX_HEALTH, playerIn)));
            });
        }
        return super.use(worldIn, playerIn, handIn);
    }

    private double getAttrValue(Attribute attribute, Player player){
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null){
            return getMath0(instance.getValue());
        }
        return 0;
    }

    //返回百分比数值
    private String getMath(double a){
        BigDecimal bd = new BigDecimal(a * 100).setScale(2, RoundingMode.HALF_EVEN);
        return bd + "%";
    }

    private Double getMath0(double a){
        BigDecimal bd = new BigDecimal(a).setScale(2, RoundingMode.HALF_EVEN);
        return bd.doubleValue();
    }
}
