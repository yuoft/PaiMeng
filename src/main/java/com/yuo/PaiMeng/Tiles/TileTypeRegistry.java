package com.yuo.PaiMeng.Tiles;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileTypeRegistry {
    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, PaiMeng.MOD_ID);

    public static final RegistryObject<TileEntityType<PotTile>> POT_TILE = TILE_ENTITIES.register("pot_tile",
            () -> TileEntityType.Builder.create(PotTile::new, PMBlocks.cookingPot.get()).build(null));
    public static final RegistryObject<TileEntityType<BenchTile>> BENCH_TILE = TILE_ENTITIES.register("bench_tile",
            () -> TileEntityType.Builder.create(BenchTile::new, PMBlocks.cookingBench.get()).build(null));
    public static final RegistryObject<TileEntityType<SynPlatTile>> SYN_PLAT_TILE = TILE_ENTITIES.register("syn_plat_tile",
            () -> TileEntityType.Builder.create(SynPlatTile::new, PMBlocks.syntheticPlatform.get()).build(null));
    public static final RegistryObject<TileEntityType<StrengthenTableTile>> strengthenTableTile = TILE_ENTITIES.register("strengthen_table_tile",
            () -> TileEntityType.Builder.create(StrengthenTableTile::new, PMBlocks.strengthenTable.get()).build(null));

    public static final RegistryObject<TileEntityType<LieYanHauPlantTile>> lieyanhuaPlant = TILE_ENTITIES.register("lieyanhua_plant",
            () -> TileEntityType.Builder.create(LieYanHauPlantTile::new, PMBlocks.lieyanhuaHuaruiPlant.get()).build(null));
    public static final RegistryObject<TileEntityType<LieYanHauCropTile>> lieyanhuaCrop = TILE_ENTITIES.register("lieyanhua_crop",
            () -> TileEntityType.Builder.create(LieYanHauCropTile::new, PMBlocks.lieyanhuaHuaruiCrop.get()).build(null));
    public static final RegistryObject<TileEntityType<BingWuHuaPlantTile>> bingwuhuaPlant = TILE_ENTITIES.register("bingwuhua_plant",
            () -> TileEntityType.Builder.create(BingWuHuaPlantTile::new, PMBlocks.bingwuhuaHuaduoPlant.get()).build(null));
    public static final RegistryObject<TileEntityType<BingWuHuaCropTile>> bingwuhuaCrop = TILE_ENTITIES.register("bingwuhua_crop",
            () -> TileEntityType.Builder.create(BingWuHuaCropTile::new, PMBlocks.bingwuhuaHuaduoCrop.get()).build(null));

    public static final RegistryObject<TileEntityType<SevenGodTile>> SEVEN_GOD = TILE_ENTITIES.register("seven_god",
            () -> TileEntityType.Builder.create(SevenGodTile::new, PMBlocks.sevenGod.get()).build(null));

}
