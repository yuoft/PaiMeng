package com.yuo.PaiMeng.Tiles;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileTypeRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, PaiMeng.MOD_ID);

    public static final RegistryObject<TileEntityType<PotTile>> POT_TILE = TILE_ENTITIES.register("pot_tile",
            () -> TileEntityType.Builder.create(PotTile::new, BlockRegistry.cookingPot.get()).build(null));
    public static final RegistryObject<TileEntityType<BenchTile>> BENCH_TILE = TILE_ENTITIES.register("bench_tile",
            () -> TileEntityType.Builder.create(BenchTile::new, BlockRegistry.cookingBench.get()).build(null));
    public static final RegistryObject<TileEntityType<SynPlatTile>> SYN_PLAT_TILE = TILE_ENTITIES.register("syn_plat_tile",
            () -> TileEntityType.Builder.create(SynPlatTile::new, BlockRegistry.syntheticPlatform.get()).build(null));
    public static final RegistryObject<TileEntityType<StrengthenTableTile>> strengthenTableTile = TILE_ENTITIES.register("strengthen_table_tile",
            () -> TileEntityType.Builder.create(StrengthenTableTile::new, BlockRegistry.strengthenTable.get()).build(null));

    public static final RegistryObject<TileEntityType<LieYanHauPlantTile>> lieyanhuaPlant = TILE_ENTITIES.register("lieyanhua_plant",
            () -> TileEntityType.Builder.create(LieYanHauPlantTile::new, BlockRegistry.lieyanhuaHuaruiPlant.get()).build(null));
    public static final RegistryObject<TileEntityType<LieYanHauCropTile>> lieyanhuaCrop = TILE_ENTITIES.register("lieyanhua_crop",
            () -> TileEntityType.Builder.create(LieYanHauCropTile::new, BlockRegistry.lieyanhuaHuaruiCrop.get()).build(null));
    public static final RegistryObject<TileEntityType<BingWuHuaPlantTile>> bingwuhuaPlant = TILE_ENTITIES.register("bingwuhua_plant",
            () -> TileEntityType.Builder.create(BingWuHuaPlantTile::new, BlockRegistry.bingwuhuaHuaduoPlant.get()).build(null));
    public static final RegistryObject<TileEntityType<BingWuHuaCropTile>> bingwuhuaCrop = TILE_ENTITIES.register("bingwuhua_crop",
            () -> TileEntityType.Builder.create(BingWuHuaCropTile::new, BlockRegistry.bingwuhuaHuaduoCrop.get()).build(null));

}
