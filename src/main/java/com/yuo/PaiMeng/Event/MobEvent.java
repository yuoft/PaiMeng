package com.yuo.PaiMeng.Event;

import com.yuo.PaiMeng.Entity.BoarEntity;
import com.yuo.PaiMeng.Entity.CraneEntity;
import com.yuo.PaiMeng.Entity.EntityTypeRegister;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobEvent {

    // TODO(port): 1.20.1 已彻底删除 RegistryEvent（以及 RegistryEvent.Register）,
    //  原 onRegisterEntities(RegistryEvent.Register<EntityType<?>>) 只调用 ModSpawnEgg.initSpawnEggs(),
    //  而 1.20.1 的 ForgeSpawnEggItem 由 Forge 自行完成刷怪蛋注册，故该方法整体删除。
    //  实体属性在下方 EntityAttributeCreationEvent 中注册。

    //实体属性
    @SubscribeEvent
    public static void onRegisterEntitiesAttr(EntityAttributeCreationEvent event) {
        //1.20.1: AttributeSupplier.Builder#create() -> build()
        event.put(EntityTypeRegister.BOAR.get(), BoarEntity.setCustomAttributes().build());
        event.put(EntityTypeRegister.CRANE.get(), CraneEntity.setCustomAttributes().build());
    }
}
