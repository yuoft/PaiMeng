package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.Capability.IBlowCapability;
import com.yuo.PaiMeng.Capability.ModCapability;
import com.yuo.PaiMeng.tab.ModGroup;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;

public class ExampleItem extends Item {
    public ExampleItem() {
        super(new Properties().group(ModGroup.PaiMengOther));
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        if (!worldIn.isRemote && handIn == Hand.MAIN_HAND){
            LazyOptional<IBlowCapability> capability = playerIn.getCapability(ModCapability.BLOW_CAPABILITY);
            //当返回值不是null时需要做什么。这里的lambda表达式参数e就是之前getCapbility调用的返回值
            capability.ifPresent( e -> {
                double criticalRate = e.getCriticalRate();
                double criticalDamage = e.getCriticalDamage();
                playerIn.sendMessage(new StringTextComponent("暴击率：" + getMath(criticalRate) + " 暴击伤害：" + getMath(criticalDamage)
                + " 防御力：" + getAttrValue(Attributes.ARMOR, playerIn) + " 攻击力：" + getAttrValue(Attributes.ATTACK_DAMAGE, playerIn)
                + "生命值：" + getAttrValue(Attributes.MAX_HEALTH, playerIn)), UUID.randomUUID());
            });
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }

    private double getAttrValue(Attribute attribute, PlayerEntity player){
        ModifiableAttributeInstance instance = player.getAttribute(attribute);
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
