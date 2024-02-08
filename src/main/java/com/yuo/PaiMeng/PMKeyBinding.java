package com.yuo.PaiMeng;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.IKeyConflictContext;

/**
 * 自定义按键类
 */
public class PMKeyBinding extends KeyBinding {

    public PMKeyBinding(String description, IKeyConflictContext keyConflictContext, InputMappings.Type inputType, int keyCode, String category) {
        super(String.format("key.%s.%s", PaiMeng.MOD_ID, description), keyConflictContext, inputType.getOrMakeInput(keyCode), category);
        PMKeyManager.KEYS.add(this); //将注册的按键添加到Mod按键管理器
    }
}


