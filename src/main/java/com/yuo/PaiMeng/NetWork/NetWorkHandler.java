package com.yuo.PaiMeng.NetWork;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

public class NetWorkHandler {
    public static SimpleChannel INSTANCE;
//    public static final String VERSION = "1.0";
    private static final String VERSION = "1.0";
    private static int ID = 0;

    public static int nextID() {
        return ID++;
    }


    public static void registerMessage() {
        INSTANCE = NetworkRegistry.newSimpleChannel(
                new ResourceLocation(PaiMeng.MOD_ID, "network"), //标识符
                () -> VERSION, //数据包版本
                (version) -> version.equals(VERSION), //客户端和服务端可以接收的版本号
                (version) -> version.equals(VERSION)
        );
        INSTANCE.registerMessage(nextID(), //数据包序号
                        CookingPacket.class,  //自定义数据包类
                        CookingPacket::toBytes, //序列化数据包
                        CookingPacket::new, //反序列化
                        CookingPacket::handler); //接收数据后进行操作
    }
}
