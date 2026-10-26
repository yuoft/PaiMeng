package com.yuo.paimeng.tiles;

import com.yuo.paimeng.blocks.PMBlocks;
import com.yuo.paimeng.PaiMeng;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class TileTypeRegistry {
    public static final DeferredRegister<BlockEntityType<?>> TILE_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, PaiMeng.MOD_ID);

    public static final RegistryObject<BlockEntityType<PotTile>> POT_TILE = TILE_ENTITIES.register("pot_tile",
            () -> BlockEntityType.Builder.of(PotTile::new, PMBlocks.cookingPot.get()).build(null));
    public static final RegistryObject<BlockEntityType<BenchTile>> BENCH_TILE = TILE_ENTITIES.register("bench_tile",
            () -> BlockEntityType.Builder.of(BenchTile::new, PMBlocks.cookingBench.get()).build(null));
    public static final RegistryObject<BlockEntityType<SynPlatTile>> SYN_PLAT_TILE = TILE_ENTITIES.register("syn_plat_tile",
            () -> BlockEntityType.Builder.of(SynPlatTile::new, PMBlocks.syntheticPlatform.get()).build(null));
    public static final RegistryObject<BlockEntityType<StrengthenTableTile>> strengthenTableTile = TILE_ENTITIES.register("strengthen_table_tile",
            () -> BlockEntityType.Builder.of(StrengthenTableTile::new, PMBlocks.strengthenTable.get()).build(null));

    public static final RegistryObject<BlockEntityType<LieYanHauPlantTile>> lieyanhuaPlant = TILE_ENTITIES.register("lieyanhua_plant",
            () -> BlockEntityType.Builder.of(LieYanHauPlantTile::new, PMBlocks.lieyanhuaHuaruiPlant.get()).build(null));
    public static final RegistryObject<BlockEntityType<LieYanHauCropTile>> lieyanhuaCrop = TILE_ENTITIES.register("lieyanhua_crop",
            () -> BlockEntityType.Builder.of(LieYanHauCropTile::new, PMBlocks.lieyanhuaHuaruiCrop.get()).build(null));
    public static final RegistryObject<BlockEntityType<BingWuHuaPlantTile>> bingwuhuaPlant = TILE_ENTITIES.register("bingwuhua_plant",
            () -> BlockEntityType.Builder.of(BingWuHuaPlantTile::new, PMBlocks.bingwuhuaHuaduoPlant.get()).build(null));
    public static final RegistryObject<BlockEntityType<BingWuHuaCropTile>> bingwuhuaCrop = TILE_ENTITIES.register("bingwuhua_crop",
            () -> BlockEntityType.Builder.of(BingWuHuaCropTile::new, PMBlocks.bingwuhuaHuaduoCrop.get()).build(null));

    public static final RegistryObject<BlockEntityType<SevenGodTile>> SEVEN_GOD = TILE_ENTITIES.register("seven_god",
            () -> BlockEntityType.Builder.of(SevenGodTile::new, PMBlocks.sevenGod.get()).build(null));

}
