package com.yuo.PaiMeng;

import net.minecraft.tileentity.TileEntity;

public class ClientProxy extends CommonProxy{
    private TileEntity referencedTE = null;

    public TileEntity getRefrencedTE() {
        return referencedTE;
    }

    public void setRefrencedTE(TileEntity tileEntity) {
        referencedTE = tileEntity;
    }

}
