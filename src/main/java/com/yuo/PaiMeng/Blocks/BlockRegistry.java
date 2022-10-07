package com.yuo.PaiMeng.Blocks;

import com.yuo.PaiMeng.Blocks.Crop.*;
import com.yuo.PaiMeng.Blocks.Tree.AppleSapling;
import com.yuo.PaiMeng.Items.CropUseBlockEnum;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.WorldGen.TreeInit;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//方块注册
public class BlockRegistry {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, PaiMeng.MOD_ID);
    public static final Block.Properties LOG = Block.Properties.from(Blocks.BIRCH_LOG); //树干
    public static final Block.Properties PLANK = Block.Properties.from(Blocks.BIRCH_PLANKS); //树干
    public static final Block.Properties LEAF = Block.Properties.from(Blocks.BIRCH_LEAVES); //树叶
    public static final Block.Properties SAPLING = Block.Properties.from(Blocks.BIRCH_SAPLING); //树苗
    public static final Block.Properties PLANT = Block.Properties.from(Blocks.GRASS); //植物
    private static final Block.Properties CROP = AbstractBlock.Properties.create(Material.PLANTS).doesNotBlockMovement().tickRandomly().
            zeroHardnessAndResistance().sound(SoundType.CROP); //X型作物
    public static CropUseBlockEnum COMMON = CropUseBlockEnum.COMMON;
    public static CropUseBlockEnum FERTILE = CropUseBlockEnum.FERTILE;
    public static CropUseBlockEnum AQUATIC = CropUseBlockEnum.AQUATIC;
    public static CropUseBlockEnum LOGS = CropUseBlockEnum.LOGS;

    public static RegistryObject<Block> cookingPot = BLOCKS.register("cooking_pot", CookingPot::new);
    public static RegistryObject<Block> cookingBench = BLOCKS.register("cooking_bench", CookingBench::new);
    public static RegistryObject<Block> syntheticPlatform = BLOCKS.register("synthetic_platform", SyntheticPlatform::new);
    public static RegistryObject<Block> strengthenTable = BLOCKS.register("strengthen_table", StrengthenTable::new);

    //矿物
    public static RegistryObject<Block> baitieOre = BLOCKS.register("baitie_ore", () -> new OrdinaryOre(1,  5));
    public static RegistryObject<Block> dianqiShuijingOre = BLOCKS.register("dianqi_shuijing_ore", () -> new OrdinaryOre(1,  5));
    public static RegistryObject<Block> heitieOre = BLOCKS.register("heitie_ore", () -> new OrdinaryOre(1,  5));
    public static RegistryObject<Block> jinghuaGusuiOre = BLOCKS.register("jinghua_gusui_ore", () -> new OrdinaryOre(1,  5));
    public static RegistryObject<Block> mojingOre = BLOCKS.register("mojing_ore", () -> new OrdinaryOre(1,  5));
    public static RegistryObject<Block> shipoOre = BLOCKS.register("shipo_ore", () -> new OrdinaryOre(1,  5));
    public static RegistryObject<Block> shuijingOre = BLOCKS.register("shuijing_ore", () -> new OrdinaryOre(1,  5));
    public static RegistryObject<Block> xingyinOre = BLOCKS.register("xingyin_ore", () -> new OrdinaryOre(1,  5));
    public static RegistryObject<Block> yeboshiOre = BLOCKS.register("yeboshi_ore", () -> new OrdinaryOre(1,  5));
    public static RegistryObject<Block> zijingOre = BLOCKS.register("zijing_ore", () -> new OrdinaryOre(1,  5));

    //X型作物
    public static RegistryObject<Block> bailuoboCrop = BLOCKS.register("bailuobo_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> baocaiCrop = BLOCKS.register("baocai_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> bingwuhuaHuaduoCrop = BLOCKS.register("bingwuhua_huaduo_crop", () -> new BingWuHuaCrop(FERTILE, CROP));
    public static RegistryObject<Block> boheCrop = BLOCKS.register("bohe_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> ceciliaHuaCrop = BLOCKS.register("cecilia_hua_crop", () -> new XCropBlock(FERTILE, CROP));
    public static RegistryObject<Block> dudulianCrop = BLOCKS.register("dudulian_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> fanqieCrop = BLOCKS.register("fanqie_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> fengchejuCrop = BLOCKS.register("fengcheju_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> gougouguoCrop = BLOCKS.register("gougouguo_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> haicaoCrop = BLOCKS.register("haicao_crop", () -> new WaterXCropBlock(AQUATIC));
    public static RegistryObject<Block> hailingzhiCrop = BLOCKS.register("hailingzhi_crop", () -> new WaterXCropBlock(AQUATIC));
    public static RegistryObject<Block> huluoboCrop = BLOCKS.register("huluobo_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> jinyucaoCrop = BLOCKS.register("jinyucao_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> jueyunJiaojiaoCrop = BLOCKS.register("jueyun_jiaojiao_crop", () -> new XCropBlock(FERTILE, CROP));
    public static RegistryObject<Block> lianpengCrop = BLOCKS.register("lianpeng_crop", () -> new WaterXCropBlock(AQUATIC));
    public static RegistryObject<Block> lieyanhuaHuaruiCrop = BLOCKS.register("lieyanhua_huarui_crop", () -> new LieYanHuaCrop(FERTILE, CROP.setLightLevel(e -> 15)));
    public static RegistryObject<Block> liuliBaiheCrop = BLOCKS.register("liuli_baihe_crop", () -> new XCropBlock(FERTILE, CROP));
    public static RegistryObject<Block> liuliDaiCrop = BLOCKS.register("liuli_dai_crop", () -> new XCropBlock(FERTILE, CROP));
    public static RegistryObject<Block> luoluomeiCrop = BLOCKS.register("luoluomei_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> maweiCrop = BLOCKS.register("mawei_crop", () -> new WaterXCropBlock(AQUATIC));
    public static RegistryObject<Block> mingcaoCrop = BLOCKS.register("mingcao_crop", () -> new XCropBlock(FERTILE, CROP));
    public static RegistryObject<Block> moguCrop = BLOCKS.register("mogu_crop", () -> new XCropBlock(LOGS, CROP));
    public static RegistryObject<Block> mufengMoguCrop = BLOCKS.register("mufeng_mogu_crop", () -> new XCropBlock(LOGS, CROP));
    public static RegistryObject<Block> nichanghuaCrop = BLOCKS.register("nichanghua_crop", () -> new XCropBlock(FERTILE, CROP));
    public static RegistryObject<Block> pugongyingCrop = BLOCKS.register("pugongying_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> qingxinCrop = BLOCKS.register("qingxin_crop", () -> new XCropBlock(FERTILE, CROP));
    public static RegistryObject<Block> shumeiCrop = BLOCKS.register("shumei_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> songrongCrop = BLOCKS.register("songrong_crop", () -> new XCropBlock(LOGS, CROP));
    public static RegistryObject<Block> tiantianhuaCrop = BLOCKS.register("tiantianhua_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> tianyunCaoshiCrop = BLOCKS.register("tianyun_caoshi_crop", () -> new XCropBlock(FERTILE, CROP));
    public static RegistryObject<Block> xiaodengcaoCrop = BLOCKS.register("xiaodengcao_crop", () -> new XCropBlock(COMMON, CROP.setLightLevel(e -> 9)));
    public static RegistryObject<Block> xiaomaiCrop = BLOCKS.register("xiaomai_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> xuekuiCrop = BLOCKS.register("xuekui_crop", () -> new XCropBlock(FERTILE, CROP));
    public static RegistryObject<Block> yangcongCrop = BLOCKS.register("yangcong_crop", () -> new XCropBlock(COMMON, CROP));
    public static RegistryObject<Block> youdengxunCrop = BLOCKS.register("youdengxun_crop", () -> new XCropBlock(LOGS, CROP.setLightLevel(e -> 6)));
    public static RegistryObject<Block> zhusunCrop = BLOCKS.register("zhusun_crop", () -> new XCropBlock(FERTILE, CROP));

    //植物
    public static RegistryObject<Block> bingwuhuaHuaduoPlant = BLOCKS.register("bingwuhua_huaduo_plant", () -> new BingWuHuaPlant(PLANT));
    public static RegistryObject<Block> bohePlant = BLOCKS.register("bohe_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> ceciliaHuaPlant = BLOCKS.register("cecilia_hua_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> dudulianPlant = BLOCKS.register("dudulian_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> fengchejuPlant = BLOCKS.register("fengcheju_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> gougouguoPlant = BLOCKS.register("gougouguo_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> haicaoPlant = BLOCKS.register("haicao_plant", () -> new WaterPlant(PLANT));
    public static RegistryObject<Block> hailingzhiPlant = BLOCKS.register("hailingzhi_plant", () -> new WaterPlant(PLANT));
    public static RegistryObject<Block> jinyucaoPlant = BLOCKS.register("jinyucao_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> jueyunJiaojiaoPlant = BLOCKS.register("jueyun_jiaojiao_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> lianpengPlant = BLOCKS.register("lianpeng_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> lieyanhuaHuaruiPlant = BLOCKS.register("lieyanhua_huarui_plant", () -> new LieYanHuaPlant(Block.Properties.from(Blocks.GRASS).setLightLevel(e -> 15)));
    public static RegistryObject<Block> liuliBaihePlant = BLOCKS.register("liuli_baihe_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> liuliDaiPlant = BLOCKS.register("liuli_dai_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> luoluomeiPlant = BLOCKS.register("luoluomei_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> maweiPlant = BLOCKS.register("mawei_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> mingcaoPlant = BLOCKS.register("mingcao_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> moguPlant = BLOCKS.register("mogu_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> mufengMoguPlant = BLOCKS.register("mufeng_mogu_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> nichanghuaPlant = BLOCKS.register("nichanghua_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> pugongyingPlant = BLOCKS.register("pugongying_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> qingxinPlant = BLOCKS.register("qingxin_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> shumeiPlant = BLOCKS.register("shumei_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> songrongPlant = BLOCKS.register("songrong_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> tiantianhuaPlant = BLOCKS.register("tiantianhua_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> tianyunCaoshiPlant = BLOCKS.register("tianyun_caoshi_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> xiaodengcaoPlant = BLOCKS.register("xiaodengcao_plant", () -> new ModPlant(Block.Properties.from(Blocks.GRASS).setLightLevel(e -> 9)));
    public static RegistryObject<Block> xuekuiPlant = BLOCKS.register("xuekui_plant", () -> new ModPlant(PLANT));
    public static RegistryObject<Block> youdengxunPlant = BLOCKS.register("youdengxun_plant", () -> new ModPlant(Block.Properties.from(Blocks.GRASS).setLightLevel(e -> 6)));
    public static RegistryObject<Block> zhusunPlant = BLOCKS.register("zhusun_plant", () -> new ModPlant(PLANT));
    //墙上植物
    public static RegistryObject<Block> moguWallPlant = BLOCKS.register("mogu_wall_plant", () -> new WallPlant(PLANT));
    public static RegistryObject<Block> mufengMoguWallPlant = BLOCKS.register("mufeng_mogu_wall_plant", () -> new WallPlant(PLANT));
    public static RegistryObject<Block> songrongWallPlant = BLOCKS.register("songrong_wall_plant", () -> new WallPlant(PLANT));
    public static RegistryObject<Block> youdengxunWallPlant = BLOCKS.register("youdengxun_wall_plant", () -> new WallPlant(PLANT));
    public static RegistryObject<Block> moguWallCrop = BLOCKS.register("mogu_wall_crop", () -> new WallXCrop(LOGS, CROP));
    public static RegistryObject<Block> mufengMoguWallCrop = BLOCKS.register("mufeng_mogu_wall_crop", () -> new WallXCrop(LOGS, CROP));
    public static RegistryObject<Block> songrongWallCrop = BLOCKS.register("songrong_wall_crop", () -> new WallXCrop(LOGS, CROP));
    public static RegistryObject<Block> youdengxunWallCrop = BLOCKS.register("youdengxun_wall_crop", () -> new WallXCrop(LOGS, CROP));

    //耕地
    public static RegistryObject<Block> commonFarmland = BLOCKS.register("common_farmland", CommonFarmland::new);
    public static RegistryObject<Block> fertileFarmland = BLOCKS.register("fertile_farmland", CommonFarmland::new);
    public static RegistryObject<Block> aquaticFarmland = BLOCKS.register("aquatic_farmland", CommonFarmland::new);

    //树木
    public static RegistryObject<Block> appleLog = BLOCKS.register("apple_log", () -> new RotatedPillarBlock(LOG));
    public static RegistryObject<Block> applePlank = BLOCKS.register("apple_plank", () -> new RotatedPillarBlock(PLANK));
    public static RegistryObject<Block> appleLeaf = BLOCKS.register("apple_leaf", () -> new LeavesBlock(LEAF));
    public static RegistryObject<Block> appleSapling = BLOCKS.register("apple_sapling", () -> new AppleSapling(TreeInit.APPLE_TREE, SAPLING));
    public static RegistryObject<Block> sunAppleSapling = BLOCKS.register("sun_apple_sapling", () -> new AppleSapling(TreeInit.SUN_APPLE_TREE, SAPLING));
    public static RegistryObject<Block> purpleAppleSapling = BLOCKS.register("purple_apple_sapling", () -> new AppleSapling(TreeInit.PURPLE_APPLE_TREE, SAPLING));
    public static RegistryObject<Block> appleCrop = BLOCKS.register("apple_crop", AppleCrop::new);
    public static RegistryObject<Block> sunAppleCrop = BLOCKS.register("sun_apple_crop", AppleCrop::new);
    public static RegistryObject<Block> purpleAppleCrop = BLOCKS.register("purple_apple_crop", AppleCrop::new);
}
