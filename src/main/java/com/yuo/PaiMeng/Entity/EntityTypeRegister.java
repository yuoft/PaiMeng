package com.yuo.PaiMeng.Entity;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeRegister {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITIES, PaiMeng.MOD_ID);

    public static final RegistryObject<EntityType<BoarEntity>> BOAR = ENTITY_TYPE.register("boar",
            () -> EntityType.Builder.create(BoarEntity::new, EntityClassification.CREATURE).size(1, 0.9f).build("boar"));
    public static final RegistryObject<EntityType<CraneEntity>> CRANE = ENTITY_TYPE.register("crane",
            () -> EntityType.Builder.create(CraneEntity::new, EntityClassification.CREATURE).size(0.3f, 0.5f).build("crane"));

    public static final RegistryObject<EntityType<PMBookBallEntity>> PM_BALL = ENTITY_TYPE.register("pm_ball",
            () -> EntityType.Builder.<PMBookBallEntity>create(PMBookBallEntity::new, EntityClassification.MISC).size(0.5f, 0.5f).build("pm_ball"));
}
