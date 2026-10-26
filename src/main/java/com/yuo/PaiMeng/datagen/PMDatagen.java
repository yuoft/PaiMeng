package com.yuo.PaiMeng.datagen;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 数据生成入口（gradlew runData / IDEA 里运行 runData 配置）。
 * 产物输出到 src/generated/resources，由 build.gradle 的 sourceSets.main.resources 一并打进 jar。
 */
@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class PMDatagen {

    private PMDatagen() {
    }

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        event.getGenerator().addProvider(event.includeServer(),
                new PMWorldGenProvider(event.getGenerator().getPackOutput(), event.getLookupProvider()));
    }
}
