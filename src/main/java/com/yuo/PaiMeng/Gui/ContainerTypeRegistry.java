package com.yuo.PaiMeng.Gui;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ContainerTypeRegistry {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, PaiMeng.MOD_ID);

    public static final RegistryObject<ContainerType<PotContainer>> potContainer = CONTAINERS.register("pot_container", () ->
            IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) ->
                    new PotContainer(windowId, inv)));
    public static final RegistryObject<ContainerType<BenchContainer>> benchContainer = CONTAINERS.register("bench_container", () ->
            IForgeContainerType.create((int windowId, PlayerInventory inv, PacketBuffer data) ->
                    new BenchContainer(windowId, inv)));

}
