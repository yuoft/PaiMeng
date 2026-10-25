package com.yuo.PaiMeng.Effects;

import com.yuo.PaiMeng.Event.EventHandler;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry {
    public static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, PaiMeng.MOD_ID);

    public static final RegistryObject<MobEffect> CRITICAL_RATE = EFFECTS.register("critical_rate", CriticalRateEffect::new);
    public static final RegistryObject<MobEffect> CRITICAL_DAMAGE = EFFECTS.register("critical_damage", CriticalDamageEffect::new);
    public static final RegistryObject<MobEffect> REVIVE = EFFECTS.register("revive", ReviveEffect::new);
    public static final RegistryObject<MobEffect> DEFENSE = EFFECTS.register("defense",
            () -> (new ModPlainEffect(445566)).addAttributeModifier(Attributes.ARMOR, "21e94267-8e6d-421a-b573-712fd18c5784", EventHandler.attrDefense, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> ATTACK = EFFECTS.register("attack",
            () -> (new ModPlainEffect(456456)).addAttributeModifier(Attributes.ATTACK_DAMAGE, "b7449603-3692-46d6-9848-02e2cf735cf0", EventHandler.attrAttackDamage, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<MobEffect> ATTACK_PHYSICS = EFFECTS.register("attack_physics", () -> new ModPlainEffect(123123));
}
