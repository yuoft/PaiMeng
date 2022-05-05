package com.yuo.PaiMeng.Effects;

import com.yuo.PaiMeng.Event.EventHandler;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, PaiMeng.MOD_ID);

    public static final RegistryObject<Effect> CRITICAL_RATE = EFFECTS.register("critical_rate", CriticalRateEffect::new);
    public static final RegistryObject<Effect> CRITICAL_DAMAGE = EFFECTS.register("critical_damage", CriticalDamageEffect::new);
    public static final RegistryObject<Effect> REVIVE = EFFECTS.register("revive", ReviveEffect::new);
    public static final RegistryObject<Effect> DEFENSE = EFFECTS.register("defense",
            () -> (new ModPlainEffect(445566)).addAttributesModifier(Attributes.ARMOR, "21e94267-8e6d-421a-b573-712fd18c5784", EventHandler.attrDefense, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Effect> ATTACK = EFFECTS.register("attack",
            () -> (new ModPlainEffect(456456)).addAttributesModifier(Attributes.ATTACK_DAMAGE, "b7449603-3692-46d6-9848-02e2cf735cf0", EventHandler.attrAttackDamage, AttributeModifier.Operation.ADDITION));
    public static final RegistryObject<Effect> ATTACK_PHYSICS = EFFECTS.register("attack_physics", () -> new ModPlainEffect(123123));
}
