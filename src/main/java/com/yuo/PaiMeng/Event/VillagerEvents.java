package com.yuo.PaiMeng.Event;

import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.PaiMeng;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffer;
import net.minecraft.util.IItemProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.event.village.WandererTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * 添加村民交易处理类
 */
@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID)
public class VillagerEvents {
    @SubscribeEvent
    public static void registerTrades(VillagerTradesEvent event) {
        VillagerProfession type = event.getType();
        if (VillagerProfession.BUTCHER.equals(type)){ //屠夫
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
            trades.get(3).add(new EmeraldForItemsTrade(ItemRegistry.xiangchang.get(), 8, 10, 3));
            trades.get(3).add(new EmeraldForItemsTrade(ItemRegistry.peigen.get(), 8, 9, 3));
            trades.get(4).add(new EmeraldForItemsTrade(ItemRegistry.huotui.get(), 10, 8, 4));
            trades.get(4).add(new EmeraldForItemsTrade(ItemRegistry.yanxunrou.get(), 10, 11, 4));

            trades.get(2).add(new ItemsForEmeraldsAndItemsTrade(Items.BEEF, 1,5, ItemRegistry.shourou.get(), 2,  12, 2));
            trades.get(2).add(new ItemsForEmeraldsAndItemsTrade( Items.RABBIT, 1,3, ItemRegistry.shourou.get(), 2,  10, 2));
            trades.get(2).add(new ItemsForEmeraldsAndItemsTrade(Items.MUTTON, 1,5, ItemRegistry.shourou.get(), 2,  12, 2));
            trades.get(2).add(new ItemsForEmeraldsAndItemsTrade(Items.PORKCHOP, 1,5, ItemRegistry.shourou.get(), 2,  12, 2));
            trades.get(2).add(new ItemsForEmeraldsAndItemsTrade(Items.CHICKEN, 1, 5, ItemRegistry.qinrou.get(), 2, 12, 2));
            trades.get(2).add(new ItemsForEmeraldsAndItemsTrade(Items.EGG, 1, 5, ItemRegistry.niaodan.get(), 2, 12, 2));

            trades.get(3).add(new ItemsForEmeraldsTrade(ItemRegistry.manrou.get(), 5, 1,8, 2));
            trades.get(3).add(new ItemsForEmeraldsTrade(ItemRegistry.lenxianrou.get(), 6, 1,10, 2));

            trades.get(4).add(new ItemsForEmeraldsTrade(ItemRegistry.huotui.get(), 11, 1,3, 4));
            trades.get(4).add(new ItemsForEmeraldsTrade(ItemRegistry.xiangchang.get(), 8, 1,4, 4));
            trades.get(4).add(new ItemsForEmeraldsTrade(ItemRegistry.peigen.get(), 9, 1,4, 4));
            trades.get(4).add(new ItemsForEmeraldsTrade(ItemRegistry.yanxunrou.get(), 10, 1,3, 4));
        }
        if (VillagerProfession.FISHERMAN.equals(type)){ //渔夫
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
            trades.get(2).add(new ItemsForEmeraldsAndItemsTrade(Items.COD, 1,4, ItemRegistry.yurou.get(), 2,  11, 2));
            trades.get(2).add(new ItemsForEmeraldsAndItemsTrade(Items.SALMON, 1,5, ItemRegistry.yurou.get(), 2,  12, 2));
            trades.get(2).add(new ItemsForEmeraldsAndItemsTrade(Items.TROPICAL_FISH, 1,5, ItemRegistry.yurou.get(), 2,  12, 2));

            trades.get(3).add(new ItemsForEmeraldsTrade(ItemRegistry.xiaren.get(), 5, 1,12, 3));
            trades.get(3).add(new ItemsForEmeraldsTrade(ItemRegistry.pangxie.get(), 6, 1,10, 3));
        }
        if (VillagerProfession.FARMER.equals(type)){ //农民
            Int2ObjectMap<List<VillagerTrades.ITrade>> trades = event.getTrades();
            trades.get(2).add(new EmeraldForItemsTrade(ItemRegistry.pingguo.get(), 2, 16, 2));
            trades.get(2).add(new EmeraldForItemsTrade(ItemRegistry.songguo.get(), 2, 16, 2));

            trades.get(3).add(new EmeraldForItemsTrade(ItemRegistry.riluoguo.get(), 3, 12, 3));
            trades.get(3).add(new EmeraldForItemsTrade(ItemRegistry.jingua.get(), 3, 12, 3));

            trades.get(2).add(new ItemsForEmeraldsTrade(ItemRegistry.yan.get(), 6, 1,7, 2));
            trades.get(2).add(new ItemsForEmeraldsTrade(ItemRegistry.hujiao.get(), 5, 1,8, 2));
            trades.get(2).add(new ItemsForEmeraldsTrade(ItemRegistry.tang.get(), 4, 1,8, 2));
            trades.get(2).add(new ItemsForEmeraldsTrade(ItemRegistry.xiaomai.get(), 4, 1,8, 2));
            trades.get(2).add(new ItemsForEmeraldsTrade(ItemRegistry.bailuobo.get(), 4, 1,16, 2));
            trades.get(2).add(new ItemsForEmeraldsTrade(ItemRegistry.baocai.get(), 4, 1,15, 2));
            trades.get(2).add(new ItemsForEmeraldsTrade(ItemRegistry.fanqie.get(), 5, 1,12, 2));
            trades.get(2).add(new ItemsForEmeraldsTrade(ItemRegistry.yangcong.get(), 5, 1,13, 2));

            trades.get(3).add(new ItemsForEmeraldsTrade(ItemRegistry.doufu.get(), 6, 1,8, 3));
            trades.get(3).add(new ItemsForEmeraldsTrade(ItemRegistry.xingren.get(), 8, 1,6, 3));
            trades.get(3).add(new ItemsForEmeraldsTrade(ItemRegistry.daomi.get(), 5, 1,7, 3));
            trades.get(3).add(new ItemsForEmeraldsTrade(ItemRegistry.guojiang.get(), 8, 1,6, 3));
            trades.get(3).add(new ItemsForEmeraldsTrade(ItemRegistry.mianfen.get(), 7, 1,8, 3));
            trades.get(3).add(new ItemsForEmeraldsTrade(ItemRegistry.niunai.get(), 6, 1,8, 3));
        }
    }

    //流浪商人
    @SubscribeEvent
    public static void addWanderer(WandererTradesEvent event){
        List<VillagerTrades.ITrade> genericTrades = event.getGenericTrades(); //普通交易
        List<VillagerTrades.ITrade> rareTrades = event.getRareTrades(); //稀有交易

        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.bailuobo.get(), 2, 2, 16, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.baocai.get(), 2, 2, 16, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.daomi.get(), 3, 1, 12, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.doufu.get(), 4, 1, 8, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.fanqie.get(), 3, 2, 16, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.jingua.get(), 5, 1, 8, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.lenxianrou.get(), 8, 1, 4, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.manrou.get(), 6, 1, 6, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.pangxie.get(), 5, 1, 8, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.riluoguo.get(), 3, 1, 16, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.songguo.get(), 3, 1, 16, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.xiaren.get(), 5, 1, 12, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.xingren.get(), 6, 1, 6, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.yangcong.get(), 3, 1, 16, 0));
        genericTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.shanbianzhichen.get(), 16, 16, 4, 0));

        rareTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.dianqi_shuijing.get(), 16, 1, 2,0));
        rareTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.faguangsui.get(), 16, 1, 2,0));
        rareTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.feiyingXiuqiu.get(), 24, 1, 5,0));
        rareTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.guidouchong.get(), 10, 1, 2,0));
        rareTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.jinghe.get(), 32, 1, 2,0));
        rareTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.jinghuaGusui.get(), 16, 1, 2,0));
        rareTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.longya1.get(), 64, 1, 1,0));
        rareTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.qingwa.get(), 16, 1, 2,0));
        rareTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.qiuqiu_baoyu.get(), 32, 1, 2,0));
        rareTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.shanhu_zhenzhu.get(), 16, 1, 2,0));
        rareTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.xingluo.get(), 16, 1, 2,0));
        rareTrades.add(new ItemsForEmeraldsTrade(ItemRegistry.xiyi_weiba.get(), 16, 1, 2,0));
    }
    //物品换绿宝石
    static class EmeraldForItemsTrade implements VillagerTrades.ITrade {
        private final Item tradeItem;
        private final int count;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public EmeraldForItemsTrade(IItemProvider tradeItemIn, int countIn, int maxUsesIn, int xpValueIn) {
            this.tradeItem = tradeItemIn.asItem();
            this.count = countIn;
            this.maxUses = maxUsesIn;
            this.xpValue = xpValueIn;
            this.priceMultiplier = 0.05F;
        }

        public MerchantOffer getOffer(Entity trader, Random rand) {
            ItemStack itemstack = new ItemStack(this.tradeItem, this.count);
            return new MerchantOffer(itemstack, new ItemStack(Items.EMERALD), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }
    //物品加绿宝石换物品
    static class ItemsForEmeraldsAndItemsTrade implements VillagerTrades.ITrade {
        private final ItemStack buyingItem;
        private final int buyingItemCount;
        private final int emeraldCount;
        private final ItemStack sellingItem;
        private final int sellingItemCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public ItemsForEmeraldsAndItemsTrade(IItemProvider p_i50533_1_, int p_i50533_2_, Item p_i50533_3_, int p_i50533_4_, int p_i50533_5_, int p_i50533_6_) {
            this(p_i50533_1_, p_i50533_2_, 1, p_i50533_3_, p_i50533_4_, p_i50533_5_, p_i50533_6_);
        }

        public ItemsForEmeraldsAndItemsTrade(IItemProvider p_i50534_1_, int p_i50534_2_, int p_i50534_3_, Item p_i50534_4_, int p_i50534_5_, int p_i50534_6_, int p_i50534_7_) {
            this.buyingItem = new ItemStack(p_i50534_1_);
            this.buyingItemCount = p_i50534_2_;
            this.emeraldCount = p_i50534_3_;
            this.sellingItem = new ItemStack(p_i50534_4_);
            this.sellingItemCount = p_i50534_5_;
            this.maxUses = p_i50534_6_;
            this.xpValue = p_i50534_7_;
            this.priceMultiplier = 0.05F;
        }

        @Nullable
        public MerchantOffer getOffer(Entity trader, Random rand) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCount), new ItemStack(this.buyingItem.getItem(), this.buyingItemCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }

    //绿宝石换物品
    static class ItemsForEmeraldsTrade implements VillagerTrades.ITrade {
        private final ItemStack sellingItem;
        private final int emeraldCount;
        private final int sellingItemCount;
        private final int maxUses;
        private final int xpValue;
        private final float priceMultiplier;

        public ItemsForEmeraldsTrade(Block sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForEmeraldsTrade(Item sellingItem, int emeraldCount, int sellingItemCount, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, 12, xpValue);
        }

        public ItemsForEmeraldsTrade(Item sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
            this(new ItemStack(sellingItem), emeraldCount, sellingItemCount, maxUses, xpValue);
        }

        public ItemsForEmeraldsTrade(ItemStack sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue) {
            this(sellingItem, emeraldCount, sellingItemCount, maxUses, xpValue, 0.05F);
        }

        public ItemsForEmeraldsTrade(ItemStack sellingItem, int emeraldCount, int sellingItemCount, int maxUses, int xpValue, float priceMultiplier) {
            this.sellingItem = sellingItem;
            this.emeraldCount = emeraldCount;
            this.sellingItemCount = sellingItemCount;
            this.maxUses = maxUses;
            this.xpValue = xpValue;
            this.priceMultiplier = priceMultiplier;
        }

        public MerchantOffer getOffer(Entity trader, Random rand) {
            return new MerchantOffer(new ItemStack(Items.EMERALD, this.emeraldCount), new ItemStack(this.sellingItem.getItem(), this.sellingItemCount), this.maxUses, this.xpValue, this.priceMultiplier);
        }
    }
}

