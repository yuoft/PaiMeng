package com.yuo.PaiMeng;

import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.NetWork.RelicsGuiPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PaiMeng.MOD_ID)
public class PMKeyManager {
    public static final List<KeyBinding> KEYS = new ArrayList<>(); //按键集合

    //圣遗物界面开启
    public static KeyBinding KEY_OPEN_RELICS;
    //    public static final KeyBinding KEY_TESTS = new PMKeyBinding("TEST", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_R, "key.paimeng");

    public static void init() {
        //***** 按键描述 使用场景 输入按键分类 键位 按键分类
        KEY_OPEN_RELICS =  new PMKeyBinding("openRelics", KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_P, "key.paimeng");
        for (KeyBinding key : KEYS) {
            ClientRegistry.registerKeyBinding(key);
        }
    }

    //圣遗物GUI打开
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void openRelicsGUi(InputEvent.KeyInputEvent event) {
//        Minecraft mc = Minecraft.getInstance();
        if (KEY_OPEN_RELICS.isKeyDown()) {
            NetWorkHandler.INSTANCE.sendToServer(new RelicsGuiPacket(true));
        }
//        if (KEY_TESTS.isKeyDown()){
//            ClientPlayerEntity player = mc.player;
//            double posX = player.getPosX();
//            double posY = player.getPosY();
//            double posZ = player.getPosZ();
//            Random random = new Random();
//            player.setPosition(posX + random.nextInt(20), posY + random.nextInt(10), posZ + random.nextInt(20));
//        }
    }
}
