package com.yuo.PaiMeng.Effects;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.potion.Effect;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EffectRegistry {
    public static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, PaiMeng.MOD_ID);

    public static final RegistryObject<Effect> CRITICAL_RATE = EFFECTS.register("critical_rate", CriticalRateEffect::new);
    public static final RegistryObject<Effect> CRITICAL_DAMAGE = EFFECTS.register("critical_damage", CriticalRateEffect::new);
    public static final RegistryObject<Effect> REVIVE = EFFECTS.register("revive", ReviveEffect::new);
    public static final RegistryObject<Effect> DEFENSE = EFFECTS.register("defense", DefenseEffect::new);
    public static final RegistryObject<Effect> ATTACK = EFFECTS.register("attack", AttackEffect::new);
    public static final RegistryObject<Effect> ATTACK_PHYSICS = EFFECTS.register("attack_physics", AttackEffect::new);
}
