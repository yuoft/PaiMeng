package com.yuo.PaiMeng.Items;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.Items.Food.MyFoods;
import com.yuo.PaiMeng.Items.Food.OrdinaryFood;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.tab.ModGroup;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

//物品注册管理器
public class ItemRegistry {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, PaiMeng.MOD_ID);

	//食物
	public static RegistryObject<Item> baizhiShishuHuirou = ITEMS.register("baizhi_shishu_huirou",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> baochaoRoupian = ITEMS.register("baochao_roupian",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> beidiPingguoMenrou = ITEMS.register("beidi_pingguo_menrou",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> beidiYanxunji = ITEMS.register("beidi_yanxunji",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> boheGuodong = ITEMS.register("bohe_guodong",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> chouziShucaiDunrou = ITEMS.register("chouzi_shucai_dunrou",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> chuanchuanSanwei = ITEMS.register("chuanchuan_sanwei",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> cishenPinpan = ITEMS.register("cishen_pinpan",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> cuicuiJituibao = ITEMS.register("cuicui_jituibao",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> daHuangjinShunzhiji = ITEMS.register("da_huangjin_shunzhiji",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> danbaofan = ITEMS.register("danbaofan",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> dudulianHaixiangeng = ITEMS.register("dudulian_haixiangeng",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> duigaogao = ITEMS.register("duigaogao",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> dulaiQinrou = ITEMS.register("dulai_qinrou",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> fantuan = ITEMS.register("fantuan",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> feiyingTianfuluo = ITEMS.register("feiying_tianfuluo",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> feiyingbing = ITEMS.register("feiyingbing",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> feiyuShijindai = ITEMS.register("feiyu_shijindai",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> fengshenZahuicai = ITEMS.register("fengshen_zahuicai",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> ganchaoYuhe = ITEMS.register("ganchao_yuhe",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> ganguoLarou = ITEMS.register("ganguo_larou",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> gantianzhu = ITEMS.register("gantianzhu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> honghuiShourou = ITEMS.register("honghui_shourou",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> huangjinxie = ITEMS.register("huangjinxie",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> huangyouJianyu = ITEMS.register("huangyou_jianyu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> huangyouSongrong = ITEMS.register("huangyou_songrong",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> huangyouXiexie = ITEMS.register("huangyou_xiexie",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> huoHuoRoujiangmian = ITEMS.register("huohuo_roujiangmian",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> jidouhua = ITEMS.register("jidouhua",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> jinqiangyuShousi = ITEMS.register("jinqiangyu_shousi",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> jinsiXiaqiu = ITEMS.register("jinsi_xiaqiu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> jueyunGuoba = ITEMS.register("jueyun_guoba",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> kaoChihuyu = ITEMS.register("kao_chihuyu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> kaoMogupisa = ITEMS.register("kao_mogupisa",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> kaoroupai = ITEMS.register("kaoroupai",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> kousansi = ITEMS.register("kousansi",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> lailaicai = ITEMS.register("lailaicai",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> larouWowotou = ITEMS.register("larou_wowotou",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> lenrouPinpan = ITEMS.register("lenrou_pinpan",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> liangbanBohe = ITEMS.register("liangban_bohe",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> lianhuasu = ITEMS.register("lianhuasu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> lianziQindangeng = ITEMS.register("lianzi_qindangeng",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> luoboShishutang = ITEMS.register("luobo_shishutang",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> manzuShala = ITEMS.register("manzu_shala",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> maoxianjiaDanta = ITEMS.register("maoxianjia_danta",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> mengdeTudoubing = ITEMS.register("mengde_tudoubing",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> mifanBuding = ITEMS.register("mifan_buding",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> mijiangHuluoboJianrou = ITEMS.register("mijiang_huluobo_jianrou",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> mingyuedan = ITEMS.register("mingyuedan",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> miwowo = ITEMS.register("miwowo",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> molarou = ITEMS.register("molarou",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> liaodanShousi = ITEMS.register("niaodan_shousi",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> liaodanshao = ITEMS.register("niaodanshao",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> pushaoManrou = ITEMS.register("pushao_manrou",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> qingcezhuangNongjiacai = ITEMS.register("qingcezhuang_nongjiacai",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> qingchaoXiaren = ITEMS.register("qingchao_xiaren",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> sancaiTuanzi = ITEMS.register("sancai_tuanzi",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> shanzhenRelumian = ITEMS.register("shanzhen_relumian",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> shijinChaomian = ITEMS.register("shijin_chaomian",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> shijingZazhu = ITEMS.register("shijing_zazhu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> shourouBohejuan = ITEMS.register("shourou_bohejuan",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> shouguLamain = ITEMS.register("shougu_lamain",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> shuijingxia = ITEMS.register("shuijingxia",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> shuizhuHeibeilu = ITEMS.register("shuizhu_heibeilu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> sifangHeping = ITEMS.register("sifang_heping",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> songrongNiangroujuan = ITEMS.register("songrong_niangroujuan",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> songshuyu = ITEMS.register("songshuyu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> subaoyu = ITEMS.register("subaoyu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> tianshurou = ITEMS.register("tianshurou",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> tiantianhuaNiangji = ITEMS.register("tiantianhua_niangji",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> tianxiaShousi = ITEMS.register("tianxia_shousi",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> tiwateJiandan = ITEMS.register("tiwate_jiandan",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> weicengtang = ITEMS.register("weicengtang",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> wenxinDoufu = ITEMS.register("wenxin_doufu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> xiangnenJiaojiaoji = ITEMS.register("xiangnen_jiaojiaoji",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> xiangnongTudouni = ITEMS.register("xiangnong_tudouni",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> xiantiaoqiang = ITEMS.register("xiantiaoqiang",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> xianxiaCuishuzhan = ITEMS.register("xianxia_cuishuzhan",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> xiehuangDoufu = ITEMS.register("xiehuang_doufu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> xiehuangHuotuiXunrouShishu = ITEMS.register("xiehuang_huotui_xunrou_shishu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> xiehuangKekeshao = ITEMS.register("xiehuang_kekeshao",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> xingrenDoufu = ITEMS.register("xingren_doufu",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> yangangSanxian = ITEMS.register("yangang_sanxian",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> yanduxian = ITEMS.register("yanduxian",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> yueliangpai = ITEMS.register("yueliangpai",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> yurenTusi = ITEMS.register("yuren_tusi",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> zhaLuoboWanzi = ITEMS.register("zha_luobo_wanzi",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> zhenzhuFeicuiBaiyutang = ITEMS.register("zhenzhu_feicui_baiyutang",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> zhongyuanZasui = ITEMS.register("zhongyuan_zasui",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));
	public static RegistryObject<Item> zhuangyuanKaosongbing = ITEMS.register("zhuangyuan_kaosongbing",
			() -> new OrdinaryFood(MyFoods.TIANTIANHUA_NIANGJI));

	//注册方块物品
	public static RegistryObject<BlockItem> cookingPot = ITEMS.register("cooking_pot",
			() -> new BlockItem(BlockRegistry.cookingPot.get(), new Item.Properties().group(ModGroup.PaiMeng)));
	public static RegistryObject<BlockItem> cookingBench = ITEMS.register("cooking_bench",
			() -> new BlockItem(BlockRegistry.cookingBench.get(), new Item.Properties().group(ModGroup.PaiMeng)));
}
