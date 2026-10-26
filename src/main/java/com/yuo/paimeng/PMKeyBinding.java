package com.yuo.paimeng;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.settings.KeyConflictContext;

/**
 * 自定义按键类
 */
public class PMKeyBinding extends KeyMapping {

    public PMKeyBinding(String description, KeyConflictContext keyConflictContext,
                        InputConstants.Type inputType, int keyCode, String category) {
        super(String.format("key.%s.%s", PaiMeng.MOD_ID, description), inputType, keyCode, category);
        PMKeyManager.KEYS.add(this); //将注册的按键添加到Mod按键管理器
    }
}
