package com.yuo.PaiMeng.Event;

import com.yuo.PaiMeng.Items.ModSpawnEgg;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.entity.EntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class MobEvent {

//    @SubscribeEvent 1.16.5适用
//    public static void addEntityAttributes(EntityAttributeCreationEvent  event) {
//        event.put(EntityTypeRegister.BOAR.get(), BoarEntity.setCustomAttributes().create());
//    }

    //注册刷怪蛋
    @SubscribeEvent
    public static void onRegisterEntities(RegistryEvent.Register<EntityType<?>> event) {
        ModSpawnEgg.initSpawnEggs();
    }
}
