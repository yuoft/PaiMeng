package com.yuo.paimeng.menu;

import com.yuo.paimeng.PaiMeng;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class PMMenuTypes {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, PaiMeng.MOD_ID);

    public static final RegistryObject<MenuType<PotContainer>> potContainer = CONTAINERS.register("pot_container",
            () -> IForgeMenuType.create(PotContainer::new));
    public static final RegistryObject<MenuType<BenchContainer>> benchContainer = CONTAINERS.register("bench_container",
            () -> IForgeMenuType.create(BenchContainer::new));
    public static final RegistryObject<MenuType<SynPlatContainer>> synPlatContainer = CONTAINERS.register("syn_plat_container",
            () -> IForgeMenuType.create(SynPlatContainer::new));
    public static final RegistryObject<MenuType<SeedBoxContainer>> seedBoxContainer = CONTAINERS.register("seed_box_container",
            () -> IForgeMenuType.create((int windowId, Inventory inv, FriendlyByteBuf data) ->
                    new SeedBoxContainer(windowId, inv)));
    public static final RegistryObject<MenuType<RelicsContainer>> relicsContainer = CONTAINERS.register("relics_container",
            () -> IForgeMenuType.create((int windowId, Inventory inv, FriendlyByteBuf data) ->
                    new RelicsContainer(windowId, inv)));
    public static final RegistryObject<MenuType<StrengthenTableContainer>> strengthenTableContainer = CONTAINERS.register("strengthen_table_container",
            () -> IForgeMenuType.create(StrengthenTableContainer::new));

}
