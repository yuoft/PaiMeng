package com.yuo.PaiMeng.Items;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * 刷怪蛋。
 * 1.20.1 里 EntityType 在构造期往往还没注册完，所以刷怪蛋的实体类型必须延迟获取；
 * 这里直接继承 Forge 提供的 {@link ForgeSpawnEggItem}，它已经实现了
 * “延迟 EntityType + 发射器行为 + 颜色注册 + fromEntityType 映射”。
 */
public class ModSpawnEgg extends ForgeSpawnEggItem {
    protected static final List<ModSpawnEgg> UNADDED_EGGS = new ArrayList<>();

    public ModSpawnEgg(final RegistryObject<? extends EntityType<?>> entityTypeSupplier, int primaryColorIn, int secondaryColorIn) {
        super(asMobSupplier(entityTypeSupplier), primaryColorIn, secondaryColorIn, new Item.Properties());
        UNADDED_EGGS.add(this);
    }

    @SuppressWarnings("unchecked")
    private static Supplier<? extends EntityType<? extends Mob>> asMobSupplier(RegistryObject<? extends EntityType<?>> supplier) {
        return () -> (EntityType<? extends Mob>) supplier.get();
    }

    /**
     * 1.16.5 需要反射往 SpawnEggItem 的 EGG_MAP 里塞入刷怪蛋并手动注册发射器行为；
     * 1.20.1 的 ForgeSpawnEggItem 已在 FMLCommonSetupEvent 中自动完成这两件事，
     * 这里保留同名入口只是为了兼容旧的调用点（MobEvent#onRegisterEntities）。
     */
    public static void initSpawnEggs() {
        UNADDED_EGGS.clear();
    }
}
