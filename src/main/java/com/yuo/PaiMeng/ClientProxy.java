package com.yuo.PaiMeng;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.glfw.GLFW;

public class ClientProxy extends CommonProxy{
    public static final KeyBinding KEY_OPEN_RELICS = new KeyBinding("key.paimeng", GLFW.GLFW_KEY_P, "key.category.openRelics");

    private TileEntity referencedTE = null;

    public TileEntity getRefrencedTE() {
        return referencedTE;
    }

    public void setRefrencedTE(TileEntity tileEntity) {
        referencedTE = tileEntity;
    }

}
