package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Entity.EntityTypeRegister;
import com.yuo.PaiMeng.Items.Food.ModFoodEffects;
import com.yuo.PaiMeng.Items.Food.PaiMengFood;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.tab.ModGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Foods;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//物品注册管理器
public class PMItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PaiMeng.MOD_ID);
	public static final Item.Properties GROUP = new Item.Properties().group(ModGroup.PaiMengCrop);

	//物品
	public static RegistryObject<Item> shanbianzhichen = ITEMS.register("shanbianzhichen", EvolutionDust::new);
	public static RegistryObject<Item> yimengrongmei = ITEMS.register("yimengrongmei", EvolutionDust::new);
	public static RegistryObject<Item> huazhongxia = ITEMS.register("huazhongxia", SeedBox::new);
	public static RegistryObject<Item> aaa = ITEMS.register("aaa", ExampleItem::new);
	public static RegistryObject<Item> drug = ITEMS.register("drug", Drug::new);
	public static RegistryObject<Item> drugBottle = ITEMS.register("drug_bottle", DrugBottle::new);

	public static RegistryObject<ModSpawnEgg> boarSpawnEgg = ITEMS.register("boar_spawn_egg", () ->
			new ModSpawnEgg(EntityTypeRegister.BOAR, 0x112233, 0x332211));
	public static RegistryObject<ModSpawnEgg> craneSpawnEgg = ITEMS.register("crane_spawn_egg", () ->
			new ModSpawnEgg(EntityTypeRegister.CRANE, 0x155683, 0x338271));

	//素材
	public static RegistryObject<Item> bailuobo = ITEMS.register("bailuobo", PMFoodItem::new);
	public static RegistryObject<Item> baocai = ITEMS.register("baocai", PMFoodItem::new);
	public static RegistryObject<Item> bohe = ITEMS.register("bohe", PMFoodItem::new);
	public static RegistryObject<Item> ceciliaHua = ITEMS.register("cecilia_hua", PMFoodItem::new);
	public static RegistryObject<Item> daomi = ITEMS.register("daomi", PMFoodItem::new);
	public static RegistryObject<Item> doufu = ITEMS.register("doufu", PMFoodItem::new);
	public static RegistryObject<Item> dudulian = ITEMS.register("dudulian", PMFoodItem::new);
	public static RegistryObject<Item> fanqie = ITEMS.register("fanqie", PMFoodItem::new);
	public static RegistryObject<Item> feiyingXiuqiu = ITEMS.register("feiying_xiuqiu", PMFoodItem::new);
	public static RegistryObject<Item> fengcheju = ITEMS.register("fengcheju", PMFoodItem::new);
	public static RegistryObject<Item> gougouguo = ITEMS.register("gougouguo", PMFoodItem::new);
	public static RegistryObject<Item> guidouchong = ITEMS.register("guidouchong", PMFoodItem::new);
	public static RegistryObject<Item> guojiang = ITEMS.register("guojiang", PMFoodItem::new);
	public static RegistryObject<Item> haicao = ITEMS.register("haicao", PMFoodItem::new);
	public static RegistryObject<Item> hailingzhi = ITEMS.register("hailingzhi", PMFoodItem::new);
	public static RegistryObject<Item> huangyou = ITEMS.register("huangyou", PMFoodItem::new);
	public static RegistryObject<Item> hujiao = ITEMS.register("hujiao", PMFoodItem::new);
	public static RegistryObject<Item> huluobo = ITEMS.register("huluobo", PMFoodItem::new);
	public static RegistryObject<Item> huotui = ITEMS.register("huotui", PMFoodItem::new);
	public static RegistryObject<Item> jinghuaGusui = ITEMS.register("jinghua_gusui", PMMaterial::new);
	public static RegistryObject<Item> jingua = ITEMS.register("jingua", PMFoodItem::new);
	public static RegistryObject<Item> jinyucao = ITEMS.register("jinyucao", PMFoodItem::new);
	public static RegistryObject<Item> jueyunJiaojiao = ITEMS.register("jueyun_jiaojiao", PMFoodItem::new);
	public static RegistryObject<Item> lenxianrou = ITEMS.register("lenxianrou", PMFoodItem::new);
	public static RegistryObject<Item> lianpeng = ITEMS.register("lianpeng", PMFoodItem::new);
	public static RegistryObject<Item> liuliBaihe = ITEMS.register("liuli_baihe", PMFoodItem::new);
	public static RegistryObject<Item> liuliDai = ITEMS.register("liuli_dai", PMFoodItem::new);
	public static RegistryObject<Item> luoluomei = ITEMS.register("luoluomei", PMFoodItem::new);
	public static RegistryObject<Item> manrou = ITEMS.register("manrou", PMFoodItem::new);
	public static RegistryObject<Item> mianfen = ITEMS.register("mianfen", PMFoodItem::new);
	public static RegistryObject<Item> mingcao = ITEMS.register("mingcao", PMFoodItem::new);
	public static RegistryObject<Item> mogu = ITEMS.register("mogu", PMFoodItem::new);
	public static RegistryObject<Item> mufeng_mogu = ITEMS.register("mufeng_mogu", PMFoodItem::new);
	public static RegistryObject<Item> nailao = ITEMS.register("nailao", PMFoodItem::new);
	public static RegistryObject<Item> naiyou = ITEMS.register("naiyou", PMFoodItem::new);
	public static RegistryObject<Item> niaodan = ITEMS.register("niaodan", PMFoodItem::new);
	public static RegistryObject<Item> nichanghua = ITEMS.register("nichanghua", PMFoodItem::new);
	public static RegistryObject<Item> niunai = ITEMS.register("niunai", PMFoodItem::new);
	public static RegistryObject<Item> pangxie = ITEMS.register("pangxie", PMFoodItem::new);
	public static RegistryObject<Item> peigen = ITEMS.register("peigen", PMFoodItem::new);
	public static RegistryObject<Item> pugongying_zhongzi = ITEMS.register("pugongying_zhongzi", PMFoodItem::new);
	public static RegistryObject<Item> qingxin = ITEMS.register("qingxin", PMFoodItem::new);
	public static RegistryObject<Item> qinrou = ITEMS.register("qinrou", PMFoodItem::new);
	public static RegistryObject<Item> shanhu_zhenzhu = ITEMS.register("shanhu_zhenzhu", PMMaterial::new);
	public static RegistryObject<Item> shipo = ITEMS.register("shipo", PMMaterial::new);
	public static RegistryObject<Item> shourou = ITEMS.register("shourou", PMFoodItem::new);
	public static RegistryObject<Item> shumei = ITEMS.register("shumei", PMFoodItem::new);
	public static RegistryObject<Item> songguo = ITEMS.register("songguo", PMFoodItem::new);
	public static RegistryObject<Item> songrong = ITEMS.register("songrong", PMFoodItem::new);
	public static RegistryObject<Item> tang = ITEMS.register("tang", PMFoodItem::new);
	public static RegistryObject<Item> tiantianhua = ITEMS.register("tiantianhua", PMFoodItem::new);
	public static RegistryObject<Item> tianyun_caoshi = ITEMS.register("tianyun_caoshi", PMFoodItem::new);
	public static RegistryObject<Item> tudou = ITEMS.register("tudou", PMFoodItem::new);
	public static RegistryObject<Item> xiangchang = ITEMS.register("xiangchang", PMFoodItem::new);
	public static RegistryObject<Item> xiaodengcao = ITEMS.register("xiaodengcao", PMFoodItem::new);
	public static RegistryObject<Item> xiaomai = ITEMS.register("xiaomai", PMFoodItem::new);
	public static RegistryObject<Item> xiaren = ITEMS.register("xiaren", PMFoodItem::new);
	public static RegistryObject<Item> xiehuang = ITEMS.register("xiehuang", PMFoodItem::new);
	public static RegistryObject<Item> xingluo = ITEMS.register("xingluo", PMMaterial::new);
	public static RegistryObject<Item> xingren = ITEMS.register("xingren", PMFoodItem::new);
	public static RegistryObject<Item> xuekui = ITEMS.register("xuekui", PMFoodItem::new);
	public static RegistryObject<Item> yan = ITEMS.register("yan", PMFoodItem::new);
	public static RegistryObject<Item> yangcong = ITEMS.register("yangcong", PMFoodItem::new);
	public static RegistryObject<Item> yanxunrou = ITEMS.register("yanxunrou", PMFoodItem::new);
	public static RegistryObject<Item> yeboshi = ITEMS.register("yeboshi", PMMaterial::new);
	public static RegistryObject<Item> youdengxun = ITEMS.register("youdengxun", PMFoodItem::new);
	public static RegistryObject<Item> yurou = ITEMS.register("yurou", PMFoodItem::new);
	public static RegistryObject<Item> zhusun = ITEMS.register("zhusun", PMFoodItem::new);

	public static RegistryObject<Item> baitie = ITEMS.register("baitie", PMMaterial::new);
	public static RegistryObject<Item> bingwuhua_huaduo = ITEMS.register("bingwuhua_huaduo", PMFoodItem::new);
	public static RegistryObject<Item> changqiang = ITEMS.register("changqiang", PMMaterial::new);
	public static RegistryObject<Item> danshoujian = ITEMS.register("danshoujian", PMMaterial::new);
	public static RegistryObject<Item> dianqi_shuijing = ITEMS.register("dianqi_shuijing", PMMaterial::new);
	public static RegistryObject<Item> faguangsui = ITEMS.register("faguangsui", PMMaterial::new);
	public static RegistryObject<Item> faqi = ITEMS.register("faqi", PMMaterial::new);
	public static RegistryObject<Item> gong = ITEMS.register("gong", PMMaterial::new);
	public static RegistryObject<Item> heitie = ITEMS.register("heitie", PMMaterial::new);
	public static RegistryObject<Item> jinghe = ITEMS.register("jinghe", PMMaterial::new);
	public static RegistryObject<Item> lieyanhua_huarui = ITEMS.register("lieyanhua_huarui", PMFoodItem::new);
	public static RegistryObject<Item> longya1 = ITEMS.register("longya1", PMMaterial::new);
	public static RegistryObject<Item> mawei = ITEMS.register("mawei", PMFoodItem::new);
	public static RegistryObject<Item> mojing = ITEMS.register("mojing", PMMaterial::new);
	public static RegistryObject<Item> qingwa = ITEMS.register("qingwa", PMMaterial::new);
	public static RegistryObject<Item> qiuqiu_baoyu = ITEMS.register("qiuqiu_baoyu", PMMaterial::new);
	public static RegistryObject<Item> shuangshoujian = ITEMS.register("shuangshoujian", PMMaterial::new);
	public static RegistryObject<Item> shuijing = ITEMS.register("shuijing", PMMaterial::new);
	public static RegistryObject<Item> xingyin = ITEMS.register("xingyin", PMMaterial::new);
	public static RegistryObject<Item> xiyi_weiba = ITEMS.register("xiyi_weiba", PMMaterial::new);
	public static RegistryObject<Item> zijing = ITEMS.register("zijing", PMMaterial::new);

	//3.0~
	public static RegistryObject<Item> dunduntao = ITEMS.register("dunduntao", PMFoodItem::new);
	public static RegistryObject<Item> padishalan = ITEMS.register("padishalan", PMFoodItem::new);
	public static RegistryObject<Item> piboye = ITEMS.register("piboye", PMFoodItem::new);
	public static RegistryObject<Item> shuwnagShengtigu = ITEMS.register("shuwnag_shengtigu", PMFoodItem::new);
	public static RegistryObject<Item> tutudou = ITEMS.register("tutudou", PMFoodItem::new);
	public static RegistryObject<Item> xiangxinguo = ITEMS.register("xiangxinguo", PMFoodItem::new);
	public static RegistryObject<Item> xiangxinliao = ITEMS.register("xiangxinliao", PMFoodItem::new);
	public static RegistryObject<Item> xumiQiangwei = ITEMS.register("xumi_qiangwei", PMFoodItem::new);
	public static RegistryObject<Item> zaoye = ITEMS.register("zaoye", PMFoodItem::new);

	//食物
	public static RegistryObject<Item> baizhiShishuHuirou = ITEMS.register("baizhi_shishu_huirou",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO,2,2));
	public static RegistryObject<Item> baochaoRoupian = ITEMS.register("baochao_roupian",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE, 1,1));
	public static RegistryObject<Item> beidiPingguoMenrou = ITEMS.register("beidi_pingguo_menrou",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE, 3,0));
	public static RegistryObject<Item> beidiYanxunji = ITEMS.register("beidi_yanxunji",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO, 2,2));
	public static RegistryObject<Item> boheGuodong = ITEMS.register("bohe_guodong",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE, 1,0));
	public static RegistryObject<Item> chouziShucaiDunrou = ITEMS.register("chouzi_shucai_dunrou",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO_AND_FIRE,2,2));
	public static RegistryObject<Item> chuanchuanSanwei = ITEMS.register("chuanchuan_sanwei",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> cishenPinpan = ITEMS.register("cishen_pinpan",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> cuicuiJituibao = ITEMS.register("cuicui_jituibao",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> daHuangjinShunzhiji = ITEMS.register("da_huangjin_shunzhiji",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> danbaofan = ITEMS.register("danbaofan",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> dudulianHaixiangeng = ITEMS.register("dudulian_haixiangeng",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_THREE,3,4));
	public static RegistryObject<Item> duigaogao = ITEMS.register("duigaogao",
			() -> new PaiMengFood(ModFoodEffects.RATE_THREE,3,5));
	public static RegistryObject<Item> dulaiQinrou = ITEMS.register("dulai_qinrou",
			() -> new PaiMengFood(ModFoodEffects.RATE_THREE,3,5));
	public static RegistryObject<Item> fantuan = ITEMS.register("fantuan",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO, 1, 0));
	public static RegistryObject<Item> feiyingTianfuluo = ITEMS.register("feiying_tianfuluo",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_SHIELD,3,4));
	public static RegistryObject<Item> feiyingbing = ITEMS.register("feiyingbing",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_THREE,3,1));
	public static RegistryObject<Item> feiyuShijindai = ITEMS.register("feiyu_shijindai",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> fengshenZahuicai = ITEMS.register("fengshen_zahuicai",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> ganchaoYuhe = ITEMS.register("ganchao_yuhe",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> ganguoLarou = ITEMS.register("ganguo_larou",
			() -> new PaiMengFood(ModFoodEffects.RATE_THREE,3,5));
	public static RegistryObject<Item> gantianzhu = ITEMS.register("gantianzhu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_THREE,3,1));
	public static RegistryObject<Item> honghuiShourou = ITEMS.register("honghui_shourou",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_PHYSICS,3,3));
	public static RegistryObject<Item> huangjinxie = ITEMS.register("huangjinxie",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_FOUR_RECOVER,4,4));
	public static RegistryObject<Item> huangyouJianyu = ITEMS.register("huangyou_jianyu",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_SHIELD,3,4));
	public static RegistryObject<Item> huangyouSongrong = ITEMS.register("huangyou_songrong",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> huangyouXiexie = ITEMS.register("huangyou_xiexie",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_FOUR_RECOVER,4,4));
	public static RegistryObject<Item> huoHuoRoujiangmian = ITEMS.register("huohuo_roujiangmian",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> jidouhua = ITEMS.register("jidouhua",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> jinqiangyuShousi = ITEMS.register("jinqiangyu_shousi",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> jinsiXiaqiu = ITEMS.register("jinsi_xiaqiu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_THREE,3,1));
	public static RegistryObject<Item> jueyunGuoba = ITEMS.register("jueyun_guoba",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_PHYSICS,3,3));
	public static RegistryObject<Item> kaoChihuyu = ITEMS.register("kao_chihuyu",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> kaoMogupisa = ITEMS.register("kao_mogupisa",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> kaoroupai = ITEMS.register("kaoroupai",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> kousansi = ITEMS.register("kousansi",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_SHIELD,3,4));
	public static RegistryObject<Item> lailaicai = ITEMS.register("lailaicai",
			() -> new PaiMengFood(ModFoodEffects.RATE_THREE,3,5));
	public static RegistryObject<Item> larouWowotou = ITEMS.register("larou_wowotou",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_FOUR_SHIELD,4,4));
	public static RegistryObject<Item> lenrouPinpan = ITEMS.register("lenrou_pinpan",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_PHYSICS,3,3));
	public static RegistryObject<Item> liangbanBohe = ITEMS.register("liangban_bohe",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_TWO,2,3));
	public static RegistryObject<Item> lianhuasu = ITEMS.register("lianhuasu",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_THREE,3,4));
	public static RegistryObject<Item> lianziQindangeng = ITEMS.register("lianzi_qindangeng",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO,2,2));
	public static RegistryObject<Item> luoboShishutang = ITEMS.register("luobo_shishutang",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> manzuShala = ITEMS.register("manzu_shala",
			() -> new PaiMengFood(ModFoodEffects.RATE_TWO,2,5));
	public static RegistryObject<Item> maoxianjiaDanta = ITEMS.register("maoxianjia_danta",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> mengdeTudoubing = ITEMS.register("mengde_tudoubing",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> mengdeKaoyu = ITEMS.register("mengde_kaoyu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> mifanBuding = ITEMS.register("mifan_buding",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> mijiangHuluoboJianrou = ITEMS.register("mijiang_huluobo_jianrou",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> mingyuedan = ITEMS.register("mingyuedan",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_THREE,3,1));
	public static RegistryObject<Item> miwowo = ITEMS.register("miwowo",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> molarou = ITEMS.register("molarou",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> niaodanShousi = ITEMS.register("niaodan_shousi",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1, 0));
	public static RegistryObject<Item> niaodanshao = ITEMS.register("niaodanshao",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> pushaoManrou = ITEMS.register("pushao_manrou",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> qingcezhuangNongjiacai = ITEMS.register("qingcezhuang_nongjiacai",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> qingchaoXiaren = ITEMS.register("qingchao_xiaren",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_SHIELD,3,4));
	public static RegistryObject<Item> sancaiTuanzi = ITEMS.register("sancai_tuanzi",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> shanzhenRelumian = ITEMS.register("shanzhen_relumian",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO,2,2));
	public static RegistryObject<Item> shijinChaomian = ITEMS.register("shijin_chaomian",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> shijingZazhu = ITEMS.register("shijing_zazhu",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_THREE,3,4));
	public static RegistryObject<Item> shourouBohejuan = ITEMS.register("shourou_bohejuan",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> shouguLamain = ITEMS.register("shougu_lamain",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> shuijingxia = ITEMS.register("shuijingxia",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> shuizhuHeibeilu = ITEMS.register("shuizhu_heibeilu",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> sifangHeping = ITEMS.register("sifang_heping",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> songrongNiangroujuan = ITEMS.register("songrong_niangroujuan",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> songshuyu = ITEMS.register("songshuyu",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> subaoyu = ITEMS.register("subaoyu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> tianshurou = ITEMS.register("tianshurou",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_PHYSICS_RATE,4,3));
	public static RegistryObject<Item> tiantianhuaNiangji = ITEMS.register("tiantianhua_niangji",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> tianxiaShousi = ITEMS.register("tianxia_shousi",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> tiwateJiandan = ITEMS.register("tiwate_jiandan",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> weicengtang = ITEMS.register("weicengtang",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> wenxinDoufu = ITEMS.register("wenxin_doufu",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> xiangnenJiaojiaoji = ITEMS.register("xiangnen_jiaojiaoji",
			() -> new PaiMengFood(ModFoodEffects.RATE_TWO,2,5));
	public static RegistryObject<Item> xiangnongTudouni = ITEMS.register("xiangnong_tudouni",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> xiantiaoqiang = ITEMS.register("xiantiaoqiang",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FIVE_RATE_DAMAGE,5,3));
	public static RegistryObject<Item> xianxiaCuishuzhan = ITEMS.register("xianxia_cuishuzhan",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> xiehuangDoufu = ITEMS.register("xiehuang_doufu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> xiehuangHuotuiXunshishu = ITEMS.register("xiehuang_huotui_xunshishu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_THREE,3,1));
	public static RegistryObject<Item> xiehuangKekeshao = ITEMS.register("xiehuang_kekeshao",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_TWO,2,3));
	public static RegistryObject<Item> xingrenDoufu = ITEMS.register("xingren_doufu",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_TWO,2,3));
	public static RegistryObject<Item> yangangSanxian = ITEMS.register("yangang_sanxian",
			() -> new PaiMengFood(ModFoodEffects.RATE_TWO,2,5));
	public static RegistryObject<Item> yanduxian = ITEMS.register("yanduxian",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> yeguJirouchuan = ITEMS.register("yegu_jirouchuan",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> yueliangpai = ITEMS.register("yueliangpai",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_FOUR_SHIELD,4,4));
	public static RegistryObject<Item> yurenTusi = ITEMS.register("yuren_tusi",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_TWO,2,4));
	public static RegistryObject<Item> zhaLuoboWanzi = ITEMS.register("zha_luobo_wanzi",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_TWO,2,3));
	public static RegistryObject<Item> zhenzhuFeicuiBaiyutang = ITEMS.register("zhenzhu_feicui_baiyutang",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_TWO,2,4));
	public static RegistryObject<Item> zhongyuanZasui = ITEMS.register("zhongyuan_zasui",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> zhuangyuanKaosongbing = ITEMS.register("zhuangyuan_kaosongbing",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> pingguo = ITEMS.register("pingguo",
			() -> new PaiMengFood(Foods.APPLE,0,0));
	public static RegistryObject<Item> riluoguo = ITEMS.register("riluoguo",
			() -> new PaiMengFood(Foods.GOLDEN_APPLE,0,0));

	public static RegistryObject<Item> duoduoshao = ITEMS.register("duoduoshao",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_PHYSICS,3,3));
	public static RegistryObject<Item> feiyingXiaxianbei = ITEMS.register("feiying_xiaxianbei",
			() -> new PaiMengFood(ModFoodEffects.HEALTH_BOOST,3,6));
	public static RegistryObject<Item> ganshaoXiangyu = ITEMS.register("ganshao_xiangyu",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> manrouChapaofan = ITEMS.register("manrou_chapaofan",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> qiaomaimian = ITEMS.register("qiaomaimian",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> riluoChouyushao = ITEMS.register("riluo_chouyushao",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> ruozhuzhu = ITEMS.register("ruozhuzhu",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_TWO,2,4));
	public static RegistryObject<Item> shumeiShuimantou = ITEMS.register("shumei_shuimantou",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> tuanziNiunai = ITEMS.register("tuanzi_niunai",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2, 0));
	public static RegistryObject<Item> wubaoYancai = ITEMS.register("wubao_yancai",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> xianyuDunluobo = ITEMS.register("xianyu_dunluobo",
			() -> new PaiMengFood(ModFoodEffects.RATE_TWO,2,5));
	public static RegistryObject<Item> bugFood = ITEMS.register("bug_food",
			() -> new PaiMengFood(ModFoodEffects.BUG_FOOD,6,0));
	public static RegistryObject<Item> paimengFood = ITEMS.register("paimeng_food",
			() -> new PaiMengFood(ModFoodEffects.PAIMENG_FOOD,6,0));

	//3.0~之后版本
	public static RegistryObject<Item> aruBanfan = ITEMS.register("aru_banfan",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO,2,2));
	public static RegistryObject<Item> boheDoutang = ITEMS.register("bohe_doutang",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_ONE,1,1));
	public static RegistryObject<Item> fengnianYouyu = ITEMS.register("fengnian_youyu",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> galixia = ITEMS.register("galixia",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,2,0));
	public static RegistryObject<Item> huangyouji = ITEMS.register("huangyouji",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> jiariGuoniang = ITEMS.register("jiari_guoniang",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_THREE,3,4));
	public static RegistryObject<Item> kaoroujuan = ITEMS.register("kaoroujuan",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_TWO,2,3));
	public static RegistryObject<Item> koudaibing = ITEMS.register("koudaibing",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> lanbadeYujuan = ITEMS.register("lanbade_yujuan",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> longxumian = ITEMS.register("longxumian",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> lvzhiChuiqiu = ITEMS.register("lvzhi_chuiqiu",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> masalaZhishiqiu = ITEMS.register("masala_zhishiqiu",
			() -> new PaiMengFood(ModFoodEffects.RATE_TWO,2,3));
	public static RegistryObject<Item> miyuanta = ITEMS.register("miyuanta",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> naijiangXianyu = ITEMS.register("naijiang_xianyu",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> naixiangjunguCuita = ITEMS.register("naixiangjungu_cuita",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,0));
	public static RegistryObject<Item> padishalanBuding = ITEMS.register("padishalan_buding",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));
	public static RegistryObject<Item> qiancengsusu = ITEMS.register("qiancengsusu",
			() -> new PaiMengFood(ModFoodEffects.RATE_THREE,3,5));
	public static RegistryObject<Item> qiangweiNaihu = ITEMS.register("qiangwei_naihu",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> qipaoSuanmeizhi = ITEMS.register("qipao_suanmeizhi",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> riluoMeimeicha = ITEMS.register("riluo_meimeicha",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_THREE,3,3));
	public static RegistryObject<Item> rourouKaomoguPisa = ITEMS.register("rourou_kaomogu_pisa",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> sabasangZhajiao = ITEMS.register("sabasang_zhajiao",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_TWO,2,0));
	public static RegistryObject<Item> shanjiashao = ITEMS.register("shanjiashao",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO,2,2));
	public static RegistryObject<Item> shoumiXiangxiang = ITEMS.register("shoumi_xiangxiang",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_FOUR_RECOVER,4,4));
	public static RegistryObject<Item> tangduerShaoji = ITEMS.register("tangduer_shaoji",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_FOUR_RATE,4,3));
	public static RegistryObject<Item> tudouchuan = ITEMS.register("tudouchuan",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_THREE,3,1));
	public static RegistryObject<Item> wudongmian = ITEMS.register("wudongmian",
			() -> new PaiMengFood(ModFoodEffects.REVIVE_TWO,2,1));
	public static RegistryObject<Item> xingxun = ITEMS.register("xingxun",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_ONE,1,0));
	public static RegistryObject<Item> yetanbing = ITEMS.register("yetanbing",
			() -> new PaiMengFood(ModFoodEffects.RECOVER_THREE,3,1));
	public static RegistryObject<Item> yezhaoMitang = ITEMS.register("yezhao_mitang",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_THREE,3,4));
	public static RegistryObject<Item> yulinShala = ITEMS.register("yulin_shala",
			() -> new PaiMengFood(ModFoodEffects.FOOD_TWO,2,2));
	public static RegistryObject<Item> zaguHuicui = ITEMS.register("zagu_huicui",
			() -> new PaiMengFood(ModFoodEffects.DEFENSE_TWO,2,4));
	public static RegistryObject<Item> zharoupaiSanmingzhi = ITEMS.register("zharoupai_sanmingzhi",
			() -> new PaiMengFood(ModFoodEffects.ATTACK_TWO,2,3));
	public static RegistryObject<Item> ziyuanYunni = ITEMS.register("ziyuan_yunni",
			() -> new PaiMengFood(ModFoodEffects.FOOD_THREE,3,2));

	//圣遗物
	public static RegistryObject<Item> xingyunerRelics = ITEMS.register("xingyuner_relics", () -> new Relics(1, 3));
	public static RegistryObject<Item> maoxianjiaRelics = ITEMS.register("maoxianjia_relics", () -> new Relics(1, 3));
	public static RegistryObject<Item> youyiRelics = ITEMS.register("youyi_relics", () -> new Relics(1, 3));
	public static RegistryObject<Item> yongshiRelics = ITEMS.register("yongshi_relics", () -> new Relics(3, 4));
	public static RegistryObject<Item> jiaoguanRelics = ITEMS.register("jiaoguan_relics", () -> new Relics(3, 4));
	public static RegistryObject<Item> liufangzheRelics = ITEMS.register("liufangzhe_relics", () -> new Relics(3, 4));
	public static RegistryObject<Item> juedoushiRelics = ITEMS.register("juedoushi_relics", () -> new Relics(4, 5));
	public static RegistryObject<Item> yuetuanRelics = ITEMS.register("yuetuan_relics", () -> new Relics(4, 5));

	//圣遗物经验瓶
	public static RegistryObject<Item> relicsExpBig = ITEMS.register("relics_exp_big", () -> new RelicsExp(10000));
	public static RegistryObject<Item> relicsExpSmall = ITEMS.register("relics_exp_small", () -> new RelicsExp(1000));

	//圣物匣
	public static RegistryObject<Item> relicsBoxOne = ITEMS.register("relics_box_one", () -> new RelicsBox(1));
	public static RegistryObject<Item> relicsBoxTwo = ITEMS.register("relics_box_two", () -> new RelicsBox(2));
	public static RegistryObject<Item> relicsBoxThree = ITEMS.register("relics_box_three", () -> new RelicsBox(3));

	//注册方块物品
	public static RegistryObject<BlockItem> cookingPot = ITEMS.register("cooking_pot",
			() -> new BlockItem(PMBlocks.cookingPot.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> cookingBench = ITEMS.register("cooking_bench",
			() -> new BlockItem(PMBlocks.cookingBench.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> syntheticPlatform = ITEMS.register("synthetic_platform",
			() -> new BlockItem(PMBlocks.syntheticPlatform.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> commonFarmland = ITEMS.register("common_farmland",
			() -> new BlockItem(PMBlocks.commonFarmland.get(), new Item.Properties().group(ModGroup.PaiMengCrop)));
	public static RegistryObject<BlockItem> fertileFarmland = ITEMS.register("fertile_farmland",
			() -> new BlockItem(PMBlocks.fertileFarmland.get(), new Item.Properties().group(ModGroup.PaiMengCrop)));
	public static RegistryObject<BlockItem> aquaticFarmland = ITEMS.register("aquatic_farmland",
			() -> new BlockItem(PMBlocks.aquaticFarmland.get(), new Item.Properties().group(ModGroup.PaiMengCrop)));
	public static RegistryObject<BlockItem> baitieOre = ITEMS.register("baitie_ore",
			() -> new BlockItem(PMBlocks.baitieOre.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> dianqiShuijingOre = ITEMS.register("dianqi_shuijing_ore",
			() -> new BlockItem(PMBlocks.dianqiShuijingOre.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> heitieOre = ITEMS.register("heitie_ore",
			() -> new BlockItem(PMBlocks.heitieOre.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> jinghuaGusuiOre = ITEMS.register("jinghua_gusui_ore",
			() -> new BlockItem(PMBlocks.jinghuaGusuiOre.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> mojingOre = ITEMS.register("mojing_ore",
			() -> new BlockItem(PMBlocks.mojingOre.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> shipoOre = ITEMS.register("shipo_ore",
			() -> new BlockItem(PMBlocks.shipoOre.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> shuijingOre = ITEMS.register("shuijing_ore",
			() -> new BlockItem(PMBlocks.shuijingOre.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> xingyinOre = ITEMS.register("xingyin_ore",
			() -> new BlockItem(PMBlocks.xingyinOre.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> yeboshiOre = ITEMS.register("yeboshi_ore",
			() -> new BlockItem(PMBlocks.yeboshiOre.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> zijingOre = ITEMS.register("zijing_ore",
			() -> new BlockItem(PMBlocks.zijingOre.get(), new Item.Properties().group(ModGroup.PaiMengOther)));
	public static RegistryObject<BlockItem> strengthenTable = ITEMS.register("strengthen_table",
			() -> new BlockItem(PMBlocks.strengthenTable.get(), new Item.Properties().group(ModGroup.PaiMengOther)));

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
}
