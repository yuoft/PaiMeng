package com.yuo.PaiMeng;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collections;
import java.util.List;

public class PMConfig {
    public static ForgeConfigSpec SERVER_CONFIG;
    public static ForgeConfigSpec CLIENT_CONFIG;
    public static ServerConfig SERVER;
//    public static ClientConfig CLIENT;

    static {
//        {
//            final Pair<ClientConfig, ForgeConfigSpec> specPair1 = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
//            CLIENT = specPair1.getLeft();
//            CLIENT_CONFIG = specPair1.getRight();
//        }
        {
            final Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
            SERVER_CONFIG = specPair.getRight();
            SERVER = specPair.getLeft();
        }
    }

    public static class ServerConfig{
        public final ForgeConfigSpec.IntValue lieYanHuaTime; // 烈焰花植物（作物）特殊效果间隔
        public final ForgeConfigSpec.IntValue bingWUHuaTime; // 冰雾花植物（作物）特殊效果间隔
        public final ForgeConfigSpec.IntValue lieYanHuaAndBingWUHuaTime; // 冰雾花和烈焰花，植物（作物）冻结岩浆（融化冰块）时间间隔

        public ServerConfig(ForgeConfigSpec.Builder builder){
            builder.comment("Endless Base Config").push("general");
            this.lieYanHuaTime = buildInt(builder, "LieYanHua Time", 40, 10, 200, "LieYanHua Plant (crop) special effect interval");
            this.bingWUHuaTime = buildInt(builder, "BingWUHua Time", 40, 10, 200, "BingWUHua Plant (crop) special effect interval");
            this.lieYanHuaAndBingWUHuaTime = buildInt(builder, "LieYanHua And BingWUHua Time", 5, 1, 10, "BingWUHua, Time interval of plant (crop) freezing magma (melting ice)");
            builder.pop();
        }
    }

    public static class ClientConfig{

    }

    private static ForgeConfigSpec.BooleanValue buildBoolean(ForgeConfigSpec.Builder builder, String name, boolean defaultValue, String comment){
        return builder.comment(comment).translation(name).define(name, defaultValue);
    }

    private static ForgeConfigSpec.IntValue buildInt(ForgeConfigSpec.Builder builder, String name, int defaultValue, int min, int max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.DoubleValue buildDouble(ForgeConfigSpec.Builder builder, String name, double defaultValue, double min, double max, String comment){
        return builder.comment(comment).translation(name).defineInRange(name, defaultValue, min, max);
    }

    private static ForgeConfigSpec.ConfigValue<List<? extends String>> buildConfig(ForgeConfigSpec.Builder builder, String name, String comment){
        return builder.comment(comment).translation(name).defineList(name, Collections.emptyList(), s -> s instanceof String && ResourceLocation.tryCreate((String) s) != null);
    }
}
