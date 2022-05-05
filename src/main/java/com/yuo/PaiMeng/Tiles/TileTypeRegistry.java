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

}
