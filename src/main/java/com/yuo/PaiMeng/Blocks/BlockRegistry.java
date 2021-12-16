package com.yuo.PaiMeng.Blocks;

import com.yuo.PaiMeng.Blocks.Crop.AppleCrop;
import com.yuo.PaiMeng.Blocks.Crop.ModCropBlock;
import com.yuo.PaiMeng.Blocks.Crop.ModXCropBlock;
import com.yuo.PaiMeng.Blocks.Tree.AppleSapling;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.WorldGen.TreeInit;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.RotatedPillarBlock;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//方块注册
public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PaiMeng.MOD_ID);
    public static final Block.Properties LOG = Block.Properties.from(Blocks.BIRCH_LOG); //树干
    public static final Block.Properties LEAF = Block.Properties.from(Blocks.BIRCH_LEAVES); //树叶
    public static final Block.Properties SAPLING = Block.Properties.from(Blocks.BIRCH_SAPLING); //树苗

    public static RegistryObject<Block> cookingPot = BLOCKS.register("cooking_pot", CookingPot::new);
    public static RegistryObject<Block> cookingBench = BLOCKS.register("cooking_bench", CookingBench::new);

    //X型作物
    public static RegistryObject<Block> baocaiCrop = BLOCKS.register("baocai_crop", ModXCropBlock::new);
    public static RegistryObject<Block> bingwuhuaHuaduoCrop = BLOCKS.register("bingwuhua_huaduo_crop", ModXCropBlock::new);
    public static RegistryObject<Block> boheCrop = BLOCKS.register("bohe_crop", ModXCropBlock::new);
    public static RegistryObject<Block> ceciliaHuaCrop = BLOCKS.register("cecilia_hua_crop", ModXCropBlock::new);
    public static RegistryObject<Block> dudulianCrop = BLOCKS.register("dudulian_crop", ModXCropBlock::new);
    public static RegistryObject<Block> fanqieCrop = BLOCKS.register("fanqie_crop", ModXCropBlock::new);

    //方型作物
    public static RegistryObject<Block> bailuoboCrop = BLOCKS.register("bailuobo_crop", ModCropBlock::new);
    public static RegistryObject<Block> huluoboCrop = BLOCKS.register("huluobo_crop", ModCropBlock::new);

    //树木
    public static RegistryObject<Block> appleLog = BLOCKS.register("apple_log", () -> {
        return new RotatedPillarBlock(LOG);
    });
    public static RegistryObject<Block> appleLeaf = BLOCKS.register("apple_leaf", () -> {
        return new LeavesBlock(LEAF);
    });
    public static RegistryObject<Block> appleSapling = BLOCKS.register("apple_sapling", () -> {
        return new AppleSapling(TreeInit.APPLE_TREE, SAPLING);
    });
    public static RegistryObject<Block> sunAppleSapling = BLOCKS.register("sun_apple_sapling", () -> {
        return new AppleSapling(TreeInit.SUN_APPLE_TREE, SAPLING);
    });
    public static RegistryObject<Block> purpleAppleSapling = BLOCKS.register("purple_apple_sapling", () -> {
        return new AppleSapling(TreeInit.PURPLE_APPLE_TREE, SAPLING);
    });
    public static RegistryObject<Block> appleCrop = BLOCKS.register("apple_crop", AppleCrop::new);
    public static RegistryObject<Block> sunAppleCrop = BLOCKS.register("sun_apple_crop", AppleCrop::new);
    public static RegistryObject<Block> purpleAppleCrop = BLOCKS.register("purple_apple_crop", AppleCrop::new);
}
