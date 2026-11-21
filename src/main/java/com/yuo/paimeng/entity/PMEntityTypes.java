package com.yuo.paimeng.entity;

import com.yuo.paimeng.PaiMeng;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PMEntityTypes {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPE = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, PaiMeng.MOD_ID);

    public static final RegistryObject<EntityType<BoarEntity>> BOAR = ENTITY_TYPE.register("boar",
            () -> EntityType.Builder.of(BoarEntity::new, MobCategory.CREATURE).sized(1.1f, 1.1f).build(PaiMeng.MOD_ID + ":boar"));
    public static final RegistryObject<EntityType<CraneEntity>> CRANE = ENTITY_TYPE.register("crane",
            () -> EntityType.Builder.of(CraneEntity::new, MobCategory.CREATURE).sized(0.7f, 1.5f).build(PaiMeng.MOD_ID + ":crane"));

    public static final RegistryObject<EntityType<PMBookBallEntity>> PM_BALL = ENTITY_TYPE.register("pm_ball",
            () -> EntityType.Builder.<PMBookBallEntity>of(PMBookBallEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build(PaiMeng.MOD_ID + ":pm_ball"));
    public static final RegistryObject<EntityType<HypostasisEntity>> HYPOSTASIS = ENTITY_TYPE.register("hypostasis",
            () -> EntityType.Builder.of(HypostasisEntity::new, MobCategory.MONSTER)
                    .sized(2.0f, 2.0f) // 碰撞箱 2x2x2
                    .fireImmune()
                    .clientTrackingRange(10)
                    .build(PaiMeng.MOD_ID + ":hypostasis"));

    public static final RegistryObject<EntityType<HypostasisProjectile>> HYPOSTASIS_PROJECTILE = ENTITY_TYPE.register("hypostasis_projectile",
            () -> EntityType.Builder.<HypostasisProjectile>of(HypostasisProjectile::new, MobCategory.MISC)
                    .sized(0.6f, 0.6f)
                    .clientTrackingRange(8)
                    .updateInterval(10)
                    .build(PaiMeng.MOD_ID + ":hypostasis_projectile"));
}
