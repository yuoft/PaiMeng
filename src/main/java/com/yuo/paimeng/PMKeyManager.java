package com.yuo.paimeng;

import com.mojang.blaze3d.platform.InputConstants;
import com.yuo.paimeng.network.NetWorkHandler;
import com.yuo.paimeng.network.RelicsGuiPacket;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PaiMeng.MOD_ID)
public class PMKeyManager {
    public static final List<KeyMapping> KEYS = new ArrayList<>(); //按键集合

    //圣遗物界面开启
    public static KeyMapping KEY_OPEN_RELICS;

    public static void init() {
        //***** 按键描述 使用场景 输入按键分类 键位 按键分类
        KEY_OPEN_RELICS = new PMKeyBinding("openRelics", KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_P, "key.paimeng");
    }

    //圣遗物GUI打开
    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void openRelicsGUi(InputEvent.Key event) {
        if (KEY_OPEN_RELICS != null && KEY_OPEN_RELICS.consumeClick() && Minecraft.getInstance().player != null) {
            NetWorkHandler.INSTANCE.sendToServer(new RelicsGuiPacket(true));
        }
    }
}
