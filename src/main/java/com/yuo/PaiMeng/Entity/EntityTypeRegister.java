package com.yuo.PaiMeng.Entity;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeRegister {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PaiMeng.MOD_ID);

    public static final RegistryObject<EntityType<BoarEntity>> BOAR = ENTITY_TYPE.register("boar",
            () -> EntityType.Builder.of(BoarEntity::new, MobCategory.CREATURE).sized(1, 0.9f).build(PaiMeng.MOD_ID + ":boar"));
    public static final RegistryObject<EntityType<CraneEntity>> CRANE = ENTITY_TYPE.register("crane",
            () -> EntityType.Builder.of(CraneEntity::new, MobCategory.CREATURE).sized(0.3f, 0.5f).build(PaiMeng.MOD_ID + ":crane"));

    public static final RegistryObject<EntityType<PMBookBallEntity>> PM_BALL = ENTITY_TYPE.register("pm_ball",
            () -> EntityType.Builder.<PMBookBallEntity>of(PMBookBallEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(PaiMeng.MOD_ID + ":pm_ball"));
}
