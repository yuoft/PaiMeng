package com.yuo.paimeng.items;

import com.yuo.paimeng.blocks.PMBlocks;
import com.yuo.paimeng.entity.PMEntityTypes;
import com.yuo.paimeng.items.Food.ModFoodEffects;
import com.yuo.paimeng.items.Food.PaiMengFood;
import com.yuo.paimeng.items.Weapon.*;
import com.yuo.paimeng.PaiMeng;
import com.yuo.paimeng.items.relics.GodEyeType;
import com.yuo.paimeng.items.relics.Relics;
import com.yuo.paimeng.items.relics.RelicsBox;
import com.yuo.paimeng.items.relics.RelicsExp;
import net.minecraft.world.item.*;
import net.minecraft.world.food.Foods;
import net.minecraftforge.registries.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//物品注册管理器
public class PMItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PaiMeng.MOD_ID);
	public static final Item.Properties GROUP = new Item.Properties();

	//物品
	public static RegistryObject<Item> shanbianzhichen = ITEMS.register("shanbianzhichen", EvolutionDust::new);
	public static RegistryObject<Item> yimengrongmei = ITEMS.register("yimengrongmei", EvolutionDust::new);
	public static RegistryObject<Item> huazhongxia = ITEMS.register("huazhongxia", SeedBox::new);
	public static RegistryObject<Item> drug = ITEMS.register("drug", Drug::new);
	public static RegistryObject<Item> drugBottle = ITEMS.register("drug_bottle", DrugBottle::new);
	public static RegistryObject<Item> yuanshi = ITEMS.register("yuanshi", OrdinaryItem::new);
	public static RegistryObject<Item> jiejing = ITEMS.register("jiejing", OrdinaryItem::new);

	public static RegistryObject<ModSpawnEgg> boarSpawnEgg = ITEMS.register("boar_spawn_egg", () ->
			new ModSpawnEgg(PMEntityTypes.BOAR, 0x112233, 0x332211));
	public static RegistryObject<ModSpawnEgg> craneSpawnEgg = ITEMS.register("crane_spawn_egg", () ->
			new ModSpawnEgg(PMEntityTypes.CRANE, 0x155683, 0x338271));
	public static RegistryObject<ModSpawnEgg> hypostasisSpawnEgg = ITEMS.register("hypostasis_spawn_egg", () ->
			new ModSpawnEgg(PMEntityTypes.HYPOSTASIS, 0x26262B, 0xC0A24A));

	//武器
	public static RegistryObject<Item> danshoujian = ITEMS.register("danshoujian", PMWeaponMaterial::new);
	public static RegistryObject<Item> shuangshoujian = ITEMS.register("shuangshoujian", PMWeaponMaterial::new);
	public static RegistryObject<Item> changqiang = ITEMS.register("changqiang", PMWeaponMaterial::new);
	public static RegistryObject<Item> faqi = ITEMS.register("faqi", PMWeaponMaterial::new);
	public static RegistryObject<Item> gong = ITEMS.register("gong", PMWeaponMaterial::new);

	public static RegistryObject<Item> oneSword0 = ITEMS.register("one_sword0", () -> new OneSword(PMItemTier.ONE));
	public static RegistryObject<Item> oneSword1 = ITEMS.register("one_sword1", () -> new OneSword(PMItemTier.TWO));
	public static RegistryObject<Item> oneSword2 = ITEMS.register("one_sword2", () -> new OneSword(PMItemTier.THREE));
	public static RegistryObject<Item> oneSword3 = ITEMS.register("one_sword3", () -> new OneSword(PMItemTier.FOUR));
	public static RegistryObject<Item> oneSword4 = ITEMS.register("one_sword4", () -> new OneSword(PMItemTier.FIVE));
	public static RegistryObject<Item> twoSword0 = ITEMS.register("two_sword0", () -> new TwoSword(PMItemTier.ONE));
	public static RegistryObject<Item> twoSword1 = ITEMS.register("two_sword1", () -> new TwoSword(PMItemTier.TWO));
	public static RegistryObject<Item> twoSword2 = ITEMS.register("two_sword2", () -> new TwoSword(PMItemTier.THREE));
	public static RegistryObject<Item> twoSword3 = ITEMS.register("two_sword3", () -> new TwoSword(PMItemTier.FOUR));
	public static RegistryObject<Item> twoSword4 = ITEMS.register("two_sword4", () -> new TwoSword(PMItemTier.FIVE));
	public static RegistryObject<Item> spear0 = ITEMS.register("spear0", () -> new Spear(PMItemTier.ONE));
	public static RegistryObject<Item> spear1 = ITEMS.register("spear1", () -> new Spear(PMItemTier.TWO));
	public static RegistryObject<Item> spear2 = ITEMS.register("spear2", () -> new Spear(PMItemTier.THREE));
	public static RegistryObject<Item> spear3 = ITEMS.register("spear3", () -> new Spear(PMItemTier.FOUR));
	public static RegistryObject<Item> spear4 = ITEMS.register("spear4", () -> new Spear(PMItemTier.FIVE));
	public static RegistryObject<Item> pmBook0 = ITEMS.register("pm_book0", () -> new PMBook(1));
	public static RegistryObject<Item> pmBook1 = ITEMS.register("pm_book1", () -> new PMBook(2));
	public static RegistryObject<Item> pmBook2 = ITEMS.register("pm_book2", () -> new PMBook(3));
	public static RegistryObject<Item> pmBook3 = ITEMS.register("pm_book3", () -> new PMBook(4));
	public static RegistryObject<Item> pmBook4 = ITEMS.register("pm_book4", () -> new PMBook(5));
	public static RegistryObject<Item> pmBow0 = ITEMS.register("pm_bow0", () -> new PMBow(1));
	public static RegistryObject<Item> pmBow1 = ITEMS.register("pm_bow1", () -> new PMBow(2));
	public static RegistryObject<Item> pmBow2 = ITEMS.register("pm_bow2", () -> new PMBow(3));
	public static RegistryObject<Item> pmBow3 = ITEMS.register("pm_bow3", () -> new PMBow(4));
	public static RegistryObject<Item> pmBow4 = ITEMS.register("pm_bow4", () -> new PMBow(5));

	//素材
	public static RegistryObject<Item> bailuobo = ITEMS.register("stuff_bailuobo", PMFoodItem::new);
	public static RegistryObject<Item> baocai = ITEMS.register("stuff_baocai", PMFoodItem::new);
	public static RegistryObject<Item> bohe = ITEMS.register("stuff_bohe", PMFoodItem::new);
	public static RegistryObject<Item> ceciliaHua = ITEMS.register("stuff_cecilia_hua", PMFoodItem::new);
	public static RegistryObject<Item> daomi = ITEMS.register("stuff_daomi", PMFoodItem::new);
	public static RegistryObject<Item> doufu = ITEMS.register("stuff_doufu", PMFoodItem::new);
	public static RegistryObject<Item> dudulian = ITEMS.register("stuff_dudulian", PMFoodItem::new);
	public static RegistryObject<Item> fanqie = ITEMS.register("stuff_fanqie", PMFoodItem::new);
	public static RegistryObject<Item> feiyingXiuqiu = ITEMS.register("stuff_feiying_xiuqiu", PMFoodItem::new);
	public static RegistryObject<Item> fengcheju = ITEMS.register("stuff_fengcheju", PMFoodItem::new);
	public static RegistryObject<Item> gougouguo = ITEMS.register("stuff_gougouguo", PMFoodItem::new);
	public static RegistryObject<Item> guidouchong = ITEMS.register("stuff_guidouchong", PMFoodItem::new);
	public static RegistryObject<Item> guojiang = ITEMS.register("stuff_guojiang", PMFoodItem::new);
	public static RegistryObject<Item> haicao = ITEMS.register("stuff_haicao", PMFoodItem::new);
	public static RegistryObject<Item> hailingzhi = ITEMS.register("stuff_hailingzhi", PMFoodItem::new);
	public static RegistryObject<Item> huangyou = ITEMS.register("stuff_huangyou", PMFoodItem::new);
	public static RegistryObject<Item> hujiao = ITEMS.register("stuff_hujiao", PMFoodItem::new);
	public static RegistryObject<Item> huluobo = ITEMS.register("stuff_huluobo", PMFoodItem::new);
	public static RegistryObject<Item> huotui = ITEMS.register("stuff_huotui", PMFoodItem::new);
	public static RegistryObject<Item> jinghuaGusui = ITEMS.register("jinghua_gusui", PMMaterial::new);
	public static RegistryObject<Item> jingua = ITEMS.register("stuff_jingua", PMFoodItem::new);
	public static RegistryObject<Item> jinyucao = ITEMS.register("stuff_jinyucao", PMFoodItem::new);
	public static RegistryObject<Item> jueyunJiaojiao = ITEMS.register("stuff_jueyun_jiaojiao", PMFoodItem::new);
	public static RegistryObject<Item> lenxianrou = ITEMS.register("stuff_lenxianrou", PMFoodItem::new);
	public static RegistryObject<Item> lianpeng = ITEMS.register("stuff_lianpeng", PMFoodItem::new);
	public static RegistryObject<Item> liuliBaihe = ITEMS.register("stuff_liuli_baihe", PMFoodItem::new);
	public static RegistryObject<Item> liuliDai = ITEMS.register("stuff_liuli_dai", PMFoodItem::new);
	public static RegistryObject<Item> luoluomei = ITEMS.register("stuff_luoluomei", PMFoodItem::new);
	public static RegistryObject<Item> manrou = ITEMS.register("stuff_manrou", PMFoodItem::new);
	public static RegistryObject<Item> mianfen = ITEMS.register("stuff_mianfen", PMFoodItem::new);
	public static RegistryObject<Item> mingcao = ITEMS.register("stuff_mingcao", PMFoodItem::new);
	public static RegistryObject<Item> mogu = ITEMS.register("stuff_mogu", PMFoodItem::new);
	public static RegistryObject<Item> mufeng_mogu = ITEMS.register("stuff_mufeng_mogu", PMFoodItem::new);
	public static RegistryObject<Item> nailao = ITEMS.register("stuff_nailao", PMFoodItem::new);
	public static RegistryObject<Item> naiyou = ITEMS.register("stuff_naiyou", PMFoodItem::new);
	public static RegistryObject<Item> niaodan = ITEMS.register("stuff_niaodan", PMFoodItem::new);
	public static RegistryObject<Item> nichanghua = ITEMS.register("stuff_nichanghua", PMFoodItem::new);
	public static RegistryObject<Item> niunai = ITEMS.register("stuff_niunai", PMFoodItem::new);
	public static RegistryObject<Item> pangxie = ITEMS.register("stuff_pangxie", PMFoodItem::new);
	public static RegistryObject<Item> peigen = ITEMS.register("stuff_peigen", PMFoodItem::new);
	public static RegistryObject<Item> pugongying_zhongzi = ITEMS.register("stuff_pugongying_zhongzi", PMFoodItem::new);
	public static RegistryObject<Item> qingxin = ITEMS.register("stuff_qingxin", PMFoodItem::new);
	public static RegistryObject<Item> qinrou = ITEMS.register("stuff_qinrou", PMFoodItem::new);
	public static RegistryObject<Item> shanhu_zhenzhu = ITEMS.register("shanhu_zhenzhu", PMMaterial::new);
	public static RegistryObject<Item> shipo = ITEMS.register("shipo", PMMaterial::new);
	public static RegistryObject<Item> shourou = ITEMS.register("stuff_shourou", PMFoodItem::new);
	public static RegistryObject<Item> shumei = ITEMS.register("stuff_shumei", PMFoodItem::new);
	public static RegistryObject<Item> songguo = ITEMS.register("stuff_songguo", PMFoodItem::new);
	public static RegistryObject<Item> songrong = ITEMS.register("stuff_songrong", PMFoodItem::new);
	public static RegistryObject<Item> tang = ITEMS.register("stuff_tang", PMFoodItem::new);
	public static RegistryObject<Item> tiantianhua = ITEMS.register("stuff_tiantianhua", PMFoodItem::new);
	public static RegistryObject<Item> tianyun_caoshi = ITEMS.register("stuff_tianyun_caoshi", PMFoodItem::new);
	public static RegistryObject<Item> tudou = ITEMS.register("stuff_tudou", PMFoodItem::new);
	public static RegistryObject<Item> xiangchang = ITEMS.register("stuff_xiangchang", PMFoodItem::new);
	public static RegistryObject<Item> xiaodengcao = ITEMS.register("stuff_xiaodengcao", PMFoodItem::new);
	public static RegistryObject<Item> xiaomai = ITEMS.register("stuff_xiaomai", PMFoodItem::new);
	public static RegistryObject<Item> xiaren = ITEMS.register("stuff_xiaren", PMFoodItem::new);
	public static RegistryObject<Item> xiehuang = ITEMS.register("stuff_xiehuang", PMFoodItem::new);
	public static RegistryObject<Item> xingluo = ITEMS.register("xingluo", PMMaterial::new);
	public static RegistryObject<Item> xingren = ITEMS.register("stuff_xingren", PMFoodItem::new);
	public static RegistryObject<Item> xuekui = ITEMS.register("stuff_xuekui", PMFoodItem::new);
	public static RegistryObject<Item> yan = ITEMS.register("stuff_yan", PMFoodItem::new);
	public static RegistryObject<Item> yangcong = ITEMS.register("stuff_yangcong", PMFoodItem::new);
	public static RegistryObject<Item> yanxunrou = ITEMS.register("stuff_yanxunrou", PMFoodItem::new);
	public static RegistryObject<Item> yeboshi = ITEMS.register("yeboshi", PMMaterial::new);
	public static RegistryObject<Item> youdengxun = ITEMS.register("stuff_youdengxun", PMFoodItem::new);
	public static RegistryObject<Item> yurou = ITEMS.register("stuff_yurou", PMFoodItem::new);
	public static RegistryObject<Item> zhusun = ITEMS.register("stuff_zhusun", PMFoodItem::new);

	public static RegistryObject<Item> baitie = ITEMS.register("baitie", PMMaterial::new);
	public static RegistryObject<Item> bingwuhua_huaduo = ITEMS.register("stuff_bingwuhua_huaduo", PMFoodItem::new);
	public static RegistryObject<Item> dianqi_shuijing = ITEMS.register("dianqi_shuijing", PMMaterial::new);
	public static RegistryObject<Item> faguangsui = ITEMS.register("faguangsui", PMMaterial::new);
	public static RegistryObject<Item> heitie = ITEMS.register("heitie", PMMaterial::new);
	public static RegistryObject<Item> jinghe = ITEMS.register("jinghe", PMMaterial::new);
	public static RegistryObject<Item> lieyanhua_huarui = ITEMS.register("stuff_lieyanhua_huarui", PMFoodItem::new);
	public static RegistryObject<Item> longya1 = ITEMS.register("longya1", PMMaterial::new);
	public static RegistryObject<Item> mawei = ITEMS.register("stuff_mawei", PMFoodItem::new);
	public static RegistryObject<Item> mojing = ITEMS.register("mojing", PMMaterial::new);
	public static RegistryObject<Item> qingwa = ITEMS.register("qingwa", PMMaterial::new);
	public static RegistryObject<Item> qiuqiu_baoyu = ITEMS.register("qiuqiu_baoyu", PMMaterial::new);
	public static RegistryObject<Item> shuijing = ITEMS.register("shuijing", PMMaterial::new);
	public static RegistryObject<Item> xingyin = ITEMS.register("xingyin", PMMaterial::new);
	public static RegistryObject<Item> xiyi_weiba = ITEMS.register("xiyi_weiba", PMMaterial::new);
	public static RegistryObject<Item> zijing = ITEMS.register("zijing", PMMaterial::new);

	//3.0~
	public static RegistryObject<Item> dunduntao = ITEMS.register("stuff_dunduntao", PMFoodItem::new);
	public static RegistryObject<Item> padishalan = ITEMS.register("stuff_padishalan", PMFoodItem::new);
	public static RegistryObject<Item> piboye = ITEMS.register("stuff_piboye", PMFoodItem::new);
	public static RegistryObject<Item> shuwangShengtigu = ITEMS.register("stuff_shuwang_shengtigu", PMFoodItem::new);
	public static RegistryObject<Item> tutudou = ITEMS.register("stuff_tutudou", PMFoodItem::new);
	public static RegistryObject<Item> xiangxinguo = ITEMS.register("stuff_xiangxinguo", PMFoodItem::new);
	public static RegistryObject<Item> xiangxinliao = ITEMS.register("stuff_xiangxinliao", PMFoodItem::new);
	public static RegistryObject<Item> xumiQiangwei = ITEMS.register("stuff_xumi_qiangwei", PMFoodItem::new);
	public static RegistryObject<Item> zaoye = ITEMS.register("stuff_zaoye", PMFoodItem::new);

	//4.0~
	public static RegistryObject<Item> fajiaoGuoshizhi = ITEMS.register("stuff_fajiao_guoshizhi", PMFoodItem::new);
	public static RegistryObject<Item> kafeidou = ITEMS.register("stuff_kafeidou", PMFoodItem::new);
	public static RegistryObject<Item> mojiecao = ITEMS.register("stuff_mojiecao", PMFoodItem::new);
	public static RegistryObject<Item> xizao = ITEMS.register("stuff_xizao", PMFoodItem::new);
	public static RegistryObject<Item> shenmideRou = ITEMS.register("stuff_shenmide_rou", PMFoodItem::new);

	//食物
	public static RegistryObject<Item> baizhiShishuHuirou = ITEMS.register("food_baizhi_shishu_huirou",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO,2,2));
	public static RegistryObject<Item> baochaoRoupian = ITEMS.register("food_baochao_roupian",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE, 1,1));
	public static RegistryObject<Item> beidiPingguoMenrou = ITEMS.register("food_beidi_pingguo_menrou",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE, 3,0));
	public static RegistryObject<Item> beidiYanxunji = ITEMS.register("food_beidi_yanxunji",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO, 2,2));
	public static RegistryObject<Item> boheGuodong = ITEMS.register("food_bohe_guodong",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE, 1,0));
	public static RegistryObject<Item> chouziShucaiDunrou = ITEMS.register("food_chouzi_shucai_dunrou",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO_AND_FIRE,2,2));
	public static RegistryObject<Item> chuanchuanSanwei = ITEMS.register("food_chuanchuan_sanwei",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> cishenPinpan = ITEMS.register("food_cishen_pinpan",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> cuicuiJituibao = ITEMS.register("food_cuicui_jituibao",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> daHuangjinShunzhiji = ITEMS.register("food_da_huangjin_shunzhiji",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> danbaofan = ITEMS.register("food_danbaofan",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> dudulianHaixiangeng = ITEMS.register("food_dudulian_haixiangeng",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_THREE,3,4));
	public static RegistryObject<Item> duigaogao = ITEMS.register("food_duigaogao",
			() -> new PaiMengFood(ModFoodEffects.RATE_THREE,3,5));
	public static RegistryObject<Item> dulaiQinrou = ITEMS.register("food_dulai_qinrou",
			() -> new PaiMengFood(ModFoodEffects.RATE_THREE,3,5));
	public static RegistryObject<Item> fantuan = ITEMS.register("food_fantuan",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO, 1, 0));
	public static RegistryObject<Item> feiyingTianfuluo = ITEMS.register("food_feiying_tianfuluo",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_SHIELD,3,4));
	public static RegistryObject<Item> feiyingbing = ITEMS.register("food_feiyingbing",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_THREE,3,1));
	public static RegistryObject<Item> feiyuShijindai = ITEMS.register("food_feiyu_shijindai",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> fengshenZahuicai = ITEMS.register("food_fengshen_zahuicai",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> ganchaoYuhe = ITEMS.register("food_ganchao_yuhe",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> ganguoLarou = ITEMS.register("food_ganguo_larou",
			() -> new PaiMengFood(ModFoodEffects.RATE_THREE,3,5));
	public static RegistryObject<Item> gantianzhu = ITEMS.register("food_gantianzhu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_THREE,3,1));
	public static RegistryObject<Item> honghuiShourou = ITEMS.register("food_honghui_shourou",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_PHYSICS,3,3));
	public static RegistryObject<Item> huangjinxie = ITEMS.register("food_huangjinxie",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_FOUR_RECOVER,4,4));
	public static RegistryObject<Item> huangyouJianyu = ITEMS.register("food_huangyou_jianyu",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_SHIELD,3,4));
	public static RegistryObject<Item> huangyouSongrong = ITEMS.register("food_huangyou_songrong",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> huangyouXiexie = ITEMS.register("food_huangyou_xiexie",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_FOUR_RECOVER,4,4));
	public static RegistryObject<Item> huoHuoRoujiangmian = ITEMS.register("food_huohuo_roujiangmian",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> jidouhua = ITEMS.register("food_jidouhua",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> jinqiangyuShousi = ITEMS.register("food_jinqiangyu_shousi",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> jinsiXiaqiu = ITEMS.register("food_jinsi_xiaqiu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_THREE,3,1));
	public static RegistryObject<Item> jueyunGuoba = ITEMS.register("food_jueyun_guoba",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_PHYSICS,3,3));
	public static RegistryObject<Item> kaoChihuyu = ITEMS.register("food_kao_chihuyu",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> kaoMogupisa = ITEMS.register("food_kao_mogupisa",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> kaoroupai = ITEMS.register("food_kaoroupai",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> kousansi = ITEMS.register("food_kousansi",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_SHIELD,3,4));
	public static RegistryObject<Item> lailaicai = ITEMS.register("food_lailaicai",
			() -> new PaiMengFood(ModFoodEffects.RATE_THREE,3,5));
	public static RegistryObject<Item> larouWowotou = ITEMS.register("food_larou_wowotou",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_FOUR_SHIELD,4,4));
	public static RegistryObject<Item> lenrouPinpan = ITEMS.register("food_lenrou_pinpan",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_PHYSICS,3,3));
	public static RegistryObject<Item> liangbanBohe = ITEMS.register("food_liangban_bohe",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_TWO,2,3));
	public static RegistryObject<Item> lianhuasu = ITEMS.register("food_lianhuasu",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_THREE,3,4));
	public static RegistryObject<Item> lianziQindangeng = ITEMS.register("food_lianzi_qindangeng",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO,2,2));
	public static RegistryObject<Item> luoboShishutang = ITEMS.register("food_luobo_shishutang",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> manzuShala = ITEMS.register("food_manzu_shala",
			() -> new PaiMengFood(ModFoodEffects.RATE_TWO,2,5));
	public static RegistryObject<Item> maoxianjiaDanta = ITEMS.register("food_maoxianjia_danta",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> mengdeTudoubing = ITEMS.register("food_mengde_tudoubing",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> mengdeKaoyu = ITEMS.register("food_mengde_kaoyu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> mifanBuding = ITEMS.register("food_mifan_buding",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> mijiangHuluoboJianrou = ITEMS.register("food_mijiang_huluobo_jianrou",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> mingyuedan = ITEMS.register("food_mingyuedan",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_THREE,3,1));
	public static RegistryObject<Item> miwowo = ITEMS.register("food_miwowo",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> molarou = ITEMS.register("food_molarou",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> niaodanShousi = ITEMS.register("food_niaodan_shousi",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1, 0));
	public static RegistryObject<Item> niaodanshao = ITEMS.register("food_niaodanshao",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> pushaoManrou = ITEMS.register("food_pushao_manrou",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> qingcezhuangNongjiacai = ITEMS.register("food_qingcezhuang_nongjiacai",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> qingchaoXiaren = ITEMS.register("food_qingchao_xiaren",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_SHIELD,3,4));
	public static RegistryObject<Item> sancaiTuanzi = ITEMS.register("food_sancai_tuanzi",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> shanzhenRelumian = ITEMS.register("food_shanzhen_relumian",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO,2,2));
	public static RegistryObject<Item> shijinChaomian = ITEMS.register("food_shijin_chaomian",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> shijingZazhu = ITEMS.register("food_shijing_zazhu",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_THREE,3,4));
	public static RegistryObject<Item> shourouBohejuan = ITEMS.register("food_shourou_bohejuan",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> shouguLamain = ITEMS.register("food_shougu_lamain",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> shuijingxia = ITEMS.register("food_shuijingxia",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> shuizhuHeibeilu = ITEMS.register("food_shuizhu_heibeilu",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> sifangHeping = ITEMS.register("food_sifang_heping",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> songrongNiangroujuan = ITEMS.register("food_songrong_niangroujuan",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> songshuyu = ITEMS.register("food_songshuyu",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> subaoyu = ITEMS.register("food_subaoyu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> tianshurou = ITEMS.register("food_tianshurou",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_PHYSICS_RATE,4,3));
	public static RegistryObject<Item> tiantianhuaNiangji = ITEMS.register("food_tiantianhua_niangji",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> tianxiaShousi = ITEMS.register("food_tianxia_shousi",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> tiwateJiandan = ITEMS.register("food_tiwate_jiandan",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> weicengtang = ITEMS.register("food_weicengtang",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> wenxinDoufu = ITEMS.register("food_wenxin_doufu",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> xiangnenJiaojiaoji = ITEMS.register("food_xiangnen_jiaojiaoji",
			() -> new PaiMengFood(ModFoodEffects.RATE_TWO,2,5));
	public static RegistryObject<Item> xiangnongTudouni = ITEMS.register("food_xiangnong_tudouni",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> xiantiaoqiang = ITEMS.register("food_xiantiaoqiang",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FIVE_RATE_DAMAGE,5,3));
	public static RegistryObject<Item> xianxiaCuishuzhan = ITEMS.register("food_xianxia_cuishuzhan",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> xiehuangDoufu = ITEMS.register("food_xiehuang_doufu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> xiehuangHuotuiXunshishu = ITEMS.register("food_xiehuang_huotui_xunshishu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_THREE,3,1));
	public static RegistryObject<Item> xiehuangKekeshao = ITEMS.register("food_xiehuang_kekeshao",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_TWO,2,3));
	public static RegistryObject<Item> xingrenDoufu = ITEMS.register("food_xingren_doufu",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_TWO,2,3));
	public static RegistryObject<Item> yangangSanxian = ITEMS.register("food_yangang_sanxian",
			() -> new PaiMengFood(ModFoodEffects.RATE_TWO,2,5));
	public static RegistryObject<Item> yanduxian = ITEMS.register("food_yanduxian",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> yeguJirouchuan = ITEMS.register("food_yegu_jirouchuan",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> yueliangpai = ITEMS.register("food_yueliangpai",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_FOUR_SHIELD,4,4));
	public static RegistryObject<Item> yurenTusi = ITEMS.register("food_yuren_tusi",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_TWO,2,4));
	public static RegistryObject<Item> zhaLuoboWanzi = ITEMS.register("food_zha_luobo_wanzi",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_TWO,2,3));
	public static RegistryObject<Item> zhenzhuFeicuiBaiyutang = ITEMS.register("food_zhenzhu_feicui_baiyutang",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_TWO,2,4));
	public static RegistryObject<Item> zhongyuanZasui = ITEMS.register("food_zhongyuan_zasui",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> zhuangyuanKaosongbing = ITEMS.register("food_zhuangyuan_kaosongbing",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> pingguo = ITEMS.register("food_pingguo",
			() -> new PaiMengFood(Foods.APPLE,0,0));
	public static RegistryObject<Item> riluoguo = ITEMS.register("food_riluoguo",
			() -> new PaiMengFood(Foods.GOLDEN_APPLE,0,0));

	public static RegistryObject<Item> duoduoshao = ITEMS.register("food_duoduoshao",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_PHYSICS,3,3));
	public static RegistryObject<Item> feiyingXiaxianbei = ITEMS.register("food_feiying_xiaxianbei",
			() -> new PaiMengFood(ModFoodEffects.HEALTH_BOOST,3,6));
	public static RegistryObject<Item> ganshaoXiangyu = ITEMS.register("food_ganshao_xiangyu",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> manrouChapaofan = ITEMS.register("food_manrou_chapaofan",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> qiaomaimian = ITEMS.register("food_qiaomaimian",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> riluoChouyushao = ITEMS.register("food_riluo_chouyushao",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> ruozhuzhu = ITEMS.register("food_ruozhuzhu",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_TWO,2,4));
	public static RegistryObject<Item> shumeiShuimantou = ITEMS.register("food_shumei_shuimantou",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> tuanziNiunai = ITEMS.register("food_tuanzi_niunai",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2, 0));
	public static RegistryObject<Item> wubaoYancai = ITEMS.register("food_wubao_yancai",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> xianyuDunluobo = ITEMS.register("food_xianyu_dunluobo",
			() -> new PaiMengFood(ModFoodEffects.RATE_TWO,2,5));
	public static RegistryObject<Item> bugFood = ITEMS.register("food_bug_food",
			() -> new PaiMengFood(ModFoodEffects.BUG_FOOD,6,0));
	public static RegistryObject<Item> paimengFood = ITEMS.register("food_paimeng_food",
			() -> new PaiMengFood(ModFoodEffects.PAIMENG_FOOD,6,0));

	//3.0~之后版本
	public static RegistryObject<Item> aruBanfan = ITEMS.register("food_aru_banfan",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO,2,2));
	public static RegistryObject<Item> boheDoutang = ITEMS.register("food_bohe_doutang",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> fengnianYouyu = ITEMS.register("food_fengnian_youyu",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> galixia = ITEMS.register("food_galixia",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,2,0));
	public static RegistryObject<Item> huangyouji = ITEMS.register("food_huangyouji",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> jiariGuoniang = ITEMS.register("food_jiari_guoniang",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_THREE,3,4));
	public static RegistryObject<Item> kaoroujuan = ITEMS.register("food_kaoroujuan",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_TWO,2,3));
	public static RegistryObject<Item> koudaibing = ITEMS.register("food_koudaibing",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> lanbadeYujuan = ITEMS.register("food_lanbade_yujuan",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> longxumian = ITEMS.register("food_longxumian",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> lvzhiChuiqiu = ITEMS.register("food_lvzhi_chuiqiu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> masalaZhishiqiu = ITEMS.register("food_masala_zhishiqiu",
			() -> new PaiMengFood(ModFoodEffects.RATE_TWO,2,3));
	public static RegistryObject<Item> miyuanta = ITEMS.register("food_miyuanta",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> naijiangXianyu = ITEMS.register("food_naijiang_xianyu",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> naixiangjunguCuita = ITEMS.register("food_naixiangjungu_cuita",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> padishalanBuding = ITEMS.register("food_padishalan_buding",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> qiancengsusu = ITEMS.register("food_qiancengsusu",
			() -> new PaiMengFood(ModFoodEffects.RATE_THREE,3,5));
	public static RegistryObject<Item> qiangweiNaihu = ITEMS.register("food_qiangwei_naihu",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> qipaoSuanmeizhi = ITEMS.register("food_qipao_suanmeizhi",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> riluoMeimeicha = ITEMS.register("food_riluo_meimeicha",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> rourouKaomoguPisa = ITEMS.register("food_rourou_kaomogu_pisa",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> sabasangZhajiao = ITEMS.register("food_sabasang_zhajiao",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> shanjiashao = ITEMS.register("food_shanjiashao",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO,2,2));
	public static RegistryObject<Item> shoumiXiangxiang = ITEMS.register("food_shoumi_xiangxiang",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_FOUR_RECOVER,4,4));
	public static RegistryObject<Item> tangduerShaoji = ITEMS.register("food_tangduer_shaoji",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> tudouchuan = ITEMS.register("food_tudouchuan",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_THREE,3,1));
	public static RegistryObject<Item> wudongmian = ITEMS.register("food_wudongmian",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> xingxun = ITEMS.register("food_xingxun",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> yetanbing = ITEMS.register("food_yetanbing",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,1));
	public static RegistryObject<Item> yezhaoMitang = ITEMS.register("food_yezhao_mitang",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_THREE,3,4));
	public static RegistryObject<Item> yulinShala = ITEMS.register("food_yulin_shala",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO,2,2));
	public static RegistryObject<Item> zaguHuicui = ITEMS.register("food_zagu_huicui",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_TWO,2,4));
	public static RegistryObject<Item> zharoupaiSanmingzhi = ITEMS.register("food_zharoupai_sanmingzhi",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_TWO,2,3));
	public static RegistryObject<Item> ziyuanYunni = ITEMS.register("food_ziyuan_yunni",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));

	//4.0~
	public static RegistryObject<Item> baisongXiantang = ITEMS.register("food_baisong_xiantang",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,0));
	public static RegistryObject<Item> baiweiYilu = ITEMS.register("food_baiwei_yilu",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,3));
	public static RegistryObject<Item> boheManguocha = ITEMS.register("food_bohe_manguocha",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,4));
	public static RegistryObject<Item> chaojiZhizhunPisa = ITEMS.register("food_chaoji_zhizhun_pisa",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,4,3));
	public static RegistryObject<Item> chunjieZhishui = ITEMS.register("food_chunjie_zhishui",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,3));
	public static RegistryObject<Item> cuibingFati = ITEMS.register("food_cuibing_fati",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,2,1));
	public static RegistryObject<Item> dujinguo = ITEMS.register("food_dujinguo",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,4));
	public static RegistryObject<Item> eyuRougan = ITEMS.register("food_eyu_rougan",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,2,3));
	public static RegistryObject<Item> fengda = ITEMS.register("food_fengda",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,2,0));
	public static RegistryObject<Item> fengdanFeigan = ITEMS.register("food_fengdan_feigan",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,4));
	public static RegistryObject<Item> fengdanYangcongtang = ITEMS.register("food_fengdan_yangcongtang",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,2,4));
	public static RegistryObject<Item> fuluBaishuang = ITEMS.register("food_fulu_baishuang",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,2,0));
	public static RegistryObject<Item> gangwanNiudu = ITEMS.register("food_gangwan_niudu",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,1));
	public static RegistryObject<Item> guoguoRuantang = ITEMS.register("food_guoguo_ruantang",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,3));
	public static RegistryObject<Item> guoguoXianlao = ITEMS.register("food_guoguo_xianlao",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,1));
	public static RegistryObject<Item> huaguoSancongzou = ITEMS.register("food_huaguo_sancongzou",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> jinluogao = ITEMS.register("food_jinluogao",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,4));
	public static RegistryObject<Item> jujuBobing = ITEMS.register("food_juju_bobing",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> kafeiBabalu = ITEMS.register("food_kafei_babalu",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,0));
	public static RegistryObject<Item> kasulaiShaguo = ITEMS.register("food_kasulai_shaguo",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,4));
	public static RegistryObject<Item> mijinPaoguo = ITEMS.register("food_mijin_paoguo",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> mixiangRoutuan = ITEMS.register("food_mixiang_routuan",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,0));
	public static RegistryObject<Item> naboneiFanqiegu = ITEMS.register("food_nabonei_fanqiegu",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,3));
	public static RegistryObject<Item> naiyouMogutang = ITEMS.register("food_naiyou_mogutang",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,2,1));
	public static RegistryObject<Item> pingguoHeibuding = ITEMS.register("food_pingguo_heibuding",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,2,2));
	public static RegistryObject<Item> qioyuGuotang = ITEMS.register("food_qioyu_guotang",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,4));
	public static RegistryObject<Item> roujiangQiancengmian = ITEMS.register("food_roujiang_qiancengmian",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,0));
	public static RegistryObject<Item> sabuziDunrou = ITEMS.register("food_sabuzi_dunrou",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,0));
	public static RegistryObject<Item> shengrouTata = ITEMS.register("food_shengrou_tata",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,0));
	public static RegistryObject<Item> shuangguoQinglu = ITEMS.register("food_shuangguo_qinglu",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,3));
	public static RegistryObject<Item> shuixiangRoudong = ITEMS.register("food_shuixiang_roudong",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,2,3));
	public static RegistryObject<Item> suanxiangMianbaogun = ITEMS.register("food_suanxiang_mianbaogun",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,2,3));
	public static RegistryObject<Item> tasaisiZahui = ITEMS.register("food_tasaisi_zahui",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,2,0));
	public static RegistryObject<Item> weixiji = ITEMS.register("food_weixiji",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,4,3));
	public static RegistryObject<Item> xiangkaoLeipai = ITEMS.register("food_xiangkao_leipai",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,2,0));
	public static RegistryObject<Item> xingrenZunyu = ITEMS.register("food_xingren_zunyu",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,1,0));
	public static RegistryObject<Item> yangzaHajisi = ITEMS.register("food_yangza_hajisi",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,4,4));
	public static RegistryObject<Item> youfengYatui = ITEMS.register("food_youfeng_yatui",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,1,0));
	public static RegistryObject<Item> yuyuYongchangpai = ITEMS.register("food_yuyu_yongchangpai",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,1));
	public static RegistryObject<Item> zhayuShutiao = ITEMS.register("food_zhayu_shutiao",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> paopaoju = ITEMS.register("food_paopaoju",
			() -> new PaiMengFood(Foods.APPLE,0,0));

	//圣遗物
	public static RegistryObject<Item> xingyunerRelics = ITEMS.register("xingyuner_relics", () -> new Relics(1, 3));
	public static RegistryObject<Item> maoxianjiaRelics = ITEMS.register("maoxianjia_relics", () -> new Relics(1, 3));
	public static RegistryObject<Item> youyiRelics = ITEMS.register("youyi_relics", () -> new Relics(1, 3));
	public static RegistryObject<Item> yongshiRelics = ITEMS.register("yongshi_relics", () -> new Relics(3, 4));
	public static RegistryObject<Item> jiaoguanRelics = ITEMS.register("jiaoguan_relics", () -> new Relics(3, 4));
	public static RegistryObject<Item> liufangzheRelics = ITEMS.register("liufangzhe_relics", () -> new Relics(3, 4));
	public static RegistryObject<Item> juedoushiRelics = ITEMS.register("juedoushi_relics", () -> new Relics(4, 5));
	public static RegistryObject<Item> yuetuanRelics = ITEMS.register("yuetuan_relics", () -> new Relics(4, 5));
	//神之眼
	public static RegistryObject<Item> godEyeWind = ITEMS.register("god_eye_wind", () -> new Relics(GodEyeType.WIND));
	public static RegistryObject<Item> godEyeStone = ITEMS.register("god_eye_stone", () -> new Relics(GodEyeType.STONE));
	public static RegistryObject<Item> godEyeThunder = ITEMS.register("god_eye_thunder", () -> new Relics(GodEyeType.THUNDER));
	public static RegistryObject<Item> godEyeGrass = ITEMS.register("god_eye_grass", () -> new Relics(GodEyeType.GRASS));
	public static RegistryObject<Item> godEyeWater = ITEMS.register("god_eye_water", () -> new Relics(GodEyeType.WATER));
	public static RegistryObject<Item> godEyeFire = ITEMS.register("god_eye_fire", () -> new Relics(GodEyeType.FIRE));
	public static RegistryObject<Item> godEyeIce = ITEMS.register("god_eye_ice", () -> new Relics(GodEyeType.ICE));

	//圣遗物经验瓶
	public static RegistryObject<Item> relicsExpBig = ITEMS.register("relics_exp_big", () -> new RelicsExp(10000));
	public static RegistryObject<Item> relicsExpSmall = ITEMS.register("relics_exp_small", () -> new RelicsExp(1000));

	//圣物匣
	public static RegistryObject<Item> relicsBoxOne = ITEMS.register("relics_box_one", () -> new RelicsBox(1));
	public static RegistryObject<Item> relicsBoxTwo = ITEMS.register("relics_box_two", () -> new RelicsBox(2));
	public static RegistryObject<Item> relicsBoxThree = ITEMS.register("relics_box_three", () -> new RelicsBox(3));

	//注册方块物品
	public static RegistryObject<BlockItem> cookingPot = ITEMS.register("cooking_pot",
			() -> new BlockItem(PMBlocks.cookingPot.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> cookingBench = ITEMS.register("cooking_bench",
			() -> new BlockItem(PMBlocks.cookingBench.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> cookingSuperBench = ITEMS.register("cooking_super_bench",
			() -> new BlockItem(PMBlocks.cookingSuperBench.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> syntheticPlatform = ITEMS.register("synthetic_platform",
			() -> new BlockItem(PMBlocks.syntheticPlatform.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> commonFarmland = ITEMS.register("common_farmland",
			() -> new BlockItem(PMBlocks.commonFarmland.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> fertileFarmland = ITEMS.register("fertile_farmland",
			() -> new BlockItem(PMBlocks.fertileFarmland.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> aquaticFarmland = ITEMS.register("aquatic_farmland",
			() -> new BlockItem(PMBlocks.aquaticFarmland.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> baitieOre = ITEMS.register("baitie_ore",
			() -> new BlockItem(PMBlocks.baitieOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> dianqiShuijingOre = ITEMS.register("dianqi_shuijing_ore",
			() -> new BlockItem(PMBlocks.dianqiShuijingOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> heitieOre = ITEMS.register("heitie_ore",
			() -> new BlockItem(PMBlocks.heitieOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> jinghuaGusuiOre = ITEMS.register("jinghua_gusui_ore",
			() -> new BlockItem(PMBlocks.jinghuaGusuiOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> mojingOre = ITEMS.register("mojing_ore",
			() -> new BlockItem(PMBlocks.mojingOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> shipoOre = ITEMS.register("shipo_ore",
			() -> new BlockItem(PMBlocks.shipoOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> shuijingOre = ITEMS.register("shuijing_ore",
			() -> new BlockItem(PMBlocks.shuijingOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> xingyinOre = ITEMS.register("xingyin_ore",
			() -> new BlockItem(PMBlocks.xingyinOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> yeboshiOre = ITEMS.register("yeboshi_ore",
			() -> new BlockItem(PMBlocks.yeboshiOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> zijingOre = ITEMS.register("zijing_ore",
			() -> new BlockItem(PMBlocks.zijingOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> deepBaitieOre = ITEMS.register("deep_baitie_ore",
			() -> new BlockItem(PMBlocks.deepBaitieOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> deepHeitieOre = ITEMS.register("deep_heitie_ore",
			() -> new BlockItem(PMBlocks.deepHeitieOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> deepShuijingOre = ITEMS.register("deep_shuijing_ore",
			() -> new BlockItem(PMBlocks.deepShuijingOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> deepXingyinOre = ITEMS.register("deep_xingyin_ore",
			() -> new BlockItem(PMBlocks.deepXingyinOre.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> strengthenTable = ITEMS.register("strengthen_table",
			() -> new BlockItem(PMBlocks.strengthenTable.get(), new Item.Properties()));
	public static RegistryObject<BlockItem> sevenGod = ITEMS.register("seven_god",
			() -> new BlockItem(PMBlocks.sevenGod.get(), new Item.Properties().rarity(Rarity.RARE)));

	public static CropUseBlockEnum COMMON = CropUseBlockEnum.COMMON;
	public static CropUseBlockEnum FERTILE = CropUseBlockEnum.FERTILE;
	public static CropUseBlockEnum AQUATIC = CropUseBlockEnum.AQUATIC;
	public static CropUseBlockEnum LOGS = CropUseBlockEnum.LOGS;

	//X型作物种子
	public static RegistryObject<BlockItem> bailuoboSeed = ITEMS.register("bailuobo_seed",
			() -> new CropBlockItem(PMBlocks.bailuoboCrop.get(), COMMON));
	public static RegistryObject<BlockItem> baocaiSeed = ITEMS.register("baocai_seed",
			() -> new CropBlockItem(PMBlocks.baocaiCrop.get(), COMMON));
	public static RegistryObject<BlockItem> bingwuhuaHuaduoSeed = ITEMS.register("bingwuhua_huaduo_seed",
			() -> new CropBlockItem(PMBlocks.bingwuhuaHuaduoCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> boheSeed = ITEMS.register("bohe_seed",
			() -> new CropBlockItem(PMBlocks.boheCrop.get(), COMMON));
	public static RegistryObject<BlockItem> ceciliaHuaSeed = ITEMS.register("cecilia_hua_seed",
			() -> new CropBlockItem(PMBlocks.ceciliaHuaCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> dudulianSeed = ITEMS.register("dudulian_seed",
			() -> new CropBlockItem(PMBlocks.dudulianCrop.get(), COMMON));
	public static RegistryObject<BlockItem> fanqieSeed = ITEMS.register("fanqie_seed",
			() -> new CropBlockItem(PMBlocks.fanqieCrop.get(), COMMON));
	public static RegistryObject<BlockItem> fengchejuSeed = ITEMS.register("fengcheju_seed",
			() -> new CropBlockItem(PMBlocks.fengchejuCrop.get(), COMMON));
	public static RegistryObject<BlockItem> gougouguoSeed = ITEMS.register("gougouguo_seed",
			() -> new CropBlockItem(PMBlocks.gougouguoCrop.get(), COMMON));
	public static RegistryObject<BlockItem> haicaoSeed = ITEMS.register("haicao_seed",
			() -> new CropBlockItem(PMBlocks.haicaoCrop.get(), AQUATIC));
	public static RegistryObject<BlockItem> hailingzhiSeed = ITEMS.register("hailingzhi_seed",
			() -> new CropBlockItem(PMBlocks.hailingzhiCrop.get(), AQUATIC));
	public static RegistryObject<BlockItem> huluoboSeed = ITEMS.register("huluobo_seed",
			() -> new CropBlockItem(PMBlocks.huluoboCrop.get(), COMMON));
	public static RegistryObject<BlockItem> jinyucaoSeed = ITEMS.register("jinyucao_seed",
			() -> new CropBlockItem(PMBlocks.jinyucaoCrop.get(), COMMON));
	public static RegistryObject<BlockItem> jueyunJiaojiaoSeed = ITEMS.register("jueyun_jiaojiao_seed",
			() -> new CropBlockItem(PMBlocks.jueyunJiaojiaoCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> lianpengSeed = ITEMS.register("lianpeng_seed",
			() -> new CropBlockItem(PMBlocks.lianpengCrop.get(), AQUATIC));
	public static RegistryObject<BlockItem> lieyanhuaHuaruiSeed = ITEMS.register("lieyanhua_huarui_seed",
			() -> new CropBlockItem(PMBlocks.lieyanhuaHuaruiCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> liuliBaiheSeed = ITEMS.register("liuli_baihe_seed",
			() -> new CropBlockItem(PMBlocks.liuliBaiheCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> liuliDaiSeed = ITEMS.register("liuli_dai_seed",
			() -> new CropBlockItem(PMBlocks.liuliDaiCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> luoluomeiSeed = ITEMS.register("luoluomei_seed",
			() -> new CropBlockItem(PMBlocks.luoluomeiCrop.get(), COMMON));
	public static RegistryObject<BlockItem> maweiSeed = ITEMS.register("mawei_seed",
			() -> new CropBlockItem(PMBlocks.maweiCrop.get(), AQUATIC));
	public static RegistryObject<BlockItem> mingcaoSeed = ITEMS.register("mingcao_seed",
			() -> new CropBlockItem(PMBlocks.mingcaoCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> moguSeed = ITEMS.register("mogu_seed",
			() -> new CropBlockItem(PMBlocks.moguCrop.get(), LOGS));
	public static RegistryObject<BlockItem> mufengMoguSeed = ITEMS.register("mufeng_mogu_seed",
			() -> new CropBlockItem(PMBlocks.mufengMoguCrop.get(), LOGS));
	public static RegistryObject<BlockItem> nichanghuaSeed = ITEMS.register("nichanghua_seed",
			() -> new CropBlockItem(PMBlocks.nichanghuaCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> qingxinSeed = ITEMS.register("qingxin_seed",
			() -> new CropBlockItem(PMBlocks.qingxinCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> pugongyingSeed = ITEMS.register("pugongying_seed",
			() -> new CropBlockItem(PMBlocks.pugongyingCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> shumeiSeed = ITEMS.register("shumei_seed",
			() -> new CropBlockItem(PMBlocks.shumeiCrop.get(), COMMON));
	public static RegistryObject<BlockItem> songrongSeed = ITEMS.register("songrong_seed",
			() -> new CropBlockItem(PMBlocks.songrongCrop.get(), LOGS));
	public static RegistryObject<BlockItem> tiantianhuaSeed = ITEMS.register("tiantianhua_seed",
			() -> new CropBlockItem(PMBlocks.tiantianhuaCrop.get(), COMMON));
	public static RegistryObject<BlockItem> tianyunCaoshiSeed = ITEMS.register("tianyun_caoshi_seed",
			() -> new CropBlockItem(PMBlocks.tianyunCaoshiCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> xiaodengcaoSeed = ITEMS.register("xiaodengcao_seed",
			() -> new CropBlockItem(PMBlocks.xiaodengcaoCrop.get(), COMMON));
	public static RegistryObject<BlockItem> xiaomaiSeed = ITEMS.register("xiaomai_seed",
			() -> new CropBlockItem(PMBlocks.xiaomaiCrop.get(), COMMON));
	public static RegistryObject<BlockItem> xuekuiSeed = ITEMS.register("xuekui_seed",
			() -> new CropBlockItem(PMBlocks.xuekuiCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> yangcongSeed = ITEMS.register("yangcong_seed",
			() -> new CropBlockItem(PMBlocks.yangcongCrop.get(), COMMON));
	public static RegistryObject<BlockItem> youdengxunSeed = ITEMS.register("youdengxun_seed",
			() -> new CropBlockItem(PMBlocks.youdengxunCrop.get(), LOGS));
	public static RegistryObject<BlockItem> zhusunSeed = ITEMS.register("zhusun_seed",
			() -> new CropBlockItem(PMBlocks.zhusunCrop.get(), FERTILE));
	//3.0~
	public static RegistryObject<BlockItem> dunduntaoSeed = ITEMS.register("dunduntao_seed",
			() -> new CropBlockItem(PMBlocks.dunduntaoCrop.get(), COMMON));
	public static RegistryObject<BlockItem> padishalanSeed = ITEMS.register("padishalan_seed",
			() -> new CropBlockItem(PMBlocks.padishalanCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> piboyeSeed = ITEMS.register("piboye_seed",
			() -> new CropBlockItem(PMBlocks.piboyeCrop.get(), FERTILE));
	public static RegistryObject<BlockItem> shuwangShengtiguSeed = ITEMS.register("shuwang_shengtigu_seed",
			() -> new CropBlockItem(PMBlocks.shuwangShengtiguCrop.get(), LOGS));
	public static RegistryObject<BlockItem> xiangxinguoSeed = ITEMS.register("xiangxinguo_seed",
			() -> new CropBlockItem(PMBlocks.xiangxinguoCrop.get(), COMMON));
	public static RegistryObject<BlockItem> xumiQiangweiSeed = ITEMS.register("xumi_qiangwei_seed",
			() -> new CropBlockItem(PMBlocks.xumiQiangweiCrop.get(), COMMON));
	//4.0~
	public static RegistryObject<BlockItem> mojiecaoSeed = ITEMS.register("mojiecao_seed",
			() -> new CropBlockItem(PMBlocks.mojiecaoCrop.get(), COMMON));


	//树
	public static RegistryObject<BlockItem> appleLog = ITEMS.register("apple_log",
			() -> new BlockItem(PMBlocks.appleLog.get(), GROUP));
	public static RegistryObject<BlockItem> witheredLog = ITEMS.register("withered_log",
			() -> new BlockItem(PMBlocks.witheredLog.get(), GROUP));
	public static RegistryObject<BlockItem> applePlank = ITEMS.register("apple_plank",
			() -> new BlockItem(PMBlocks.applePlank.get(), GROUP));
	public static RegistryObject<BlockItem> appleLeaf = ITEMS.register("apple_leaf",
			() -> new BlockItem(PMBlocks.appleLeaf.get(), GROUP));
	public static RegistryObject<BlockItem> appleSapling = ITEMS.register("apple_sapling",
			() -> new BlockItem(PMBlocks.appleSapling.get(), GROUP));
	public static RegistryObject<BlockItem> sunAppleSapling = ITEMS.register("sun_apple_sapling",
			() -> new BlockItem(PMBlocks.sunAppleSapling.get(), GROUP));
	public static RegistryObject<BlockItem> purpleAppleSapling = ITEMS.register("purple_apple_sapling",
			() -> new BlockItem(PMBlocks.purpleAppleSapling.get(), GROUP));
	public static RegistryObject<BlockItem> zaoyeSapling = ITEMS.register("zaoye_sapling",
			() -> new BlockItem(PMBlocks.zaoyeSapling.get(), GROUP));
	public static RegistryObject<BlockItem> paopaojuSapling = ITEMS.register("paopaoju_sapling",
			() -> new BlockItem(PMBlocks.paopaojuSapling.get(), GROUP));

}
