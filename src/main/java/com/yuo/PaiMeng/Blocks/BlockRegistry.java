package com.yuo.PaiMeng.Blocks;

import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.block.Block;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//方块注册
public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PaiMeng.MOD_ID);

    public static RegistryObject<Block> cookingPot = BLOCKS.register("cooking_pot", CookingPot::new);
    public static RegistryObject<Block> cookingBench = BLOCKS.register("cooking_bench", CookingBench::new);
}
