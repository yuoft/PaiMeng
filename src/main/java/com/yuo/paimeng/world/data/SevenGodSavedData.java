package com.yuo.paimeng.world.data;

import com.yuo.paimeng.ModUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class SevenGodSavedData extends SavedData {
    private static final String DATA_NAME = "paimeng_seven_god_data";

    // 🔴 使用 Map 保存材料与 Buff 的映射关系
    public static final Map<Item, MobEffect> MATERIAL_EFFECTS = new HashMap<>();

    static {
        MATERIAL_EFFECTS.put(Items.SUGAR, MobEffects.MOVEMENT_SPEED);          // 糖 -> 速度
        MATERIAL_EFFECTS.put(Items.RABBIT_FOOT, MobEffects.JUMP);              // 兔子脚 -> 跳跃
        MATERIAL_EFFECTS.put(Items.BLAZE_POWDER, MobEffects.DAMAGE_BOOST);     // 烈焰粉 -> 力量
        MATERIAL_EFFECTS.put(Items.GHAST_TEAR, MobEffects.REGENERATION);       // 恶魂之泪 -> 生命恢复
        MATERIAL_EFFECTS.put(Items.PUFFERFISH, MobEffects.WATER_BREATHING);    // 河豚 -> 水下呼吸
        MATERIAL_EFFECTS.put(Items.MAGMA_CREAM, MobEffects.FIRE_RESISTANCE);   // 岩浆膏 -> 抗火
        MATERIAL_EFFECTS.put(Items.GOLDEN_CARROT, MobEffects.NIGHT_VISION);    // 金胡萝卜 -> 夜视
        MATERIAL_EFFECTS.put(Items.SCUTE, MobEffects.DAMAGE_RESISTANCE);       // 海龟鳞 -> 抗性提升
        MATERIAL_EFFECTS.put(Items.PRISMARINE_SHARD, MobEffects.DIG_SPEED);       //海晶碎片  -> 急迫
        MATERIAL_EFFECTS.put(Items.FERMENTED_SPIDER_EYE, MobEffects.INVISIBILITY);       //发酵蛛眼  -> 隐身
        MATERIAL_EFFECTS.put(Items.ENCHANTED_GOLDEN_APPLE, MobEffects.HEALTH_BOOST);       //附魔金苹果  -> 生命提升
        MATERIAL_EFFECTS.put(Items.GOLDEN_APPLE, MobEffects.ABSORPTION);       //金苹果  -> 伤害吸收
        MATERIAL_EFFECTS.put(Items.CAKE, MobEffects.SATURATION);       //蛋糕  -> 饱和
        MATERIAL_EFFECTS.put(Items.DRAGON_BREATH, MobEffects.LUCK);       //龙息  -> 幸运
        MATERIAL_EFFECTS.put(Items.PHANTOM_MEMBRANE, MobEffects.SLOW_FALLING);       //幻翼膜 -> 缓降
        MATERIAL_EFFECTS.put(Items.CONDUIT, MobEffects.CONDUIT_POWER);       //潮涌核心  -> 潮涌能量
        MATERIAL_EFFECTS.put(Items.HEART_OF_THE_SEA, MobEffects.DOLPHINS_GRACE);       //海洋之心  -> 海豚的恩惠
        MATERIAL_EFFECTS.put(Items.EMERALD, MobEffects.HERO_OF_THE_VILLAGE);       //绿宝石  -> 村庄英雄
    }

    // 玩家 UUID -> 该玩家拥有的所有神像数据
    private final Map<UUID, List<ShrineData>> playerShrines = new HashMap<>();

    public static class ShrineData {
        public BlockPos pos;
        public ResourceKey<Level> dimension;
        public Map<Item, Integer> donatedItems = new HashMap<>();

        public ShrineData(BlockPos pos, ResourceKey<Level> dimension) {
            this.pos = pos;
            this.dimension = dimension;
        }
    }

    public static SevenGodSavedData get(ServerLevel level) {
        // 统一存储在服务器的主世界存档目录下
        ServerLevel overworld = level.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(
                SevenGodSavedData::load, SevenGodSavedData::new, DATA_NAME
        );
    }

    /**
     * 添加神像供奉数据
     * @param owner 玩家
     * @param data 供奉数据
     */
    public void addShrine(UUID owner, ShrineData data) {
        List<ShrineData> list = playerShrines.computeIfAbsent(owner, k -> new ArrayList<>());
        if (!list.isEmpty()) {
            list.clear(); // 防呆：如果被绕过限制放出来了，清除旧的，保留新的
        }
        list.add(data);
        setDirty();
    }

    public void removeShrine(UUID owner, BlockPos pos, ResourceKey<Level> dim) {
        List<ShrineData> list = playerShrines.get(owner);
        if (list != null) {
            list.removeIf(s -> s.pos.equals(pos) && s.dimension.equals(dim));
            if (list.isEmpty()) playerShrines.remove(owner);
            setDirty();
        }
    }

    public List<ShrineData> getPlayerShrines(UUID owner) {
        return playerShrines.getOrDefault(owner, Collections.emptyList());
    }

    /**
     * 投入材料（返回是否成功）
     * @param owner 玩家
     * @param pos 坐标
     * @param dim 世界
     * @param stack 物品
     * @param isShift 是否潜行
     */
    public boolean donateItem(UUID owner, BlockPos pos, ResourceKey<Level> dim, ItemStack stack, boolean isShift) {
        List<ShrineData> list = playerShrines.get(owner);
        if (list == null) return false;
        for (ShrineData data : list) {
            if (data.pos.equals(pos) && data.dimension.equals(dim)) {
                Item item = stack.getItem();
                if (isValidMaterial(item)) {
                    data.donatedItems.put(item, data.donatedItems.getOrDefault(item, 0) + (isShift ? stack.getCount() : 1));
                    if (isShift) {
                        stack.setCount(0);
                    } else {
                        stack.shrink(1);
                    }
                    setDirty();
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 从保存数据中获取材信息
     * @param item 材料
     * @return 数量
     */
    public int getMaterialCount(UUID owner, BlockPos pos, ResourceKey<Level> dim, Item item) {
        List<ShrineData> list = playerShrines.get(owner);
        if (list == null) return 0;
        for (ShrineData data : list) {
            if (data.pos.equals(pos) && data.dimension.equals(dim)) {
                return data.donatedItems.getOrDefault(item, 0);
            }
        }
        return 0;
    }

    // 🔴 原版酿造材料校验：只要 Map 中包含该物品即为有效
    public static boolean isValidMaterial(Item item) {
        return MATERIAL_EFFECTS.containsKey(item);
    }

    // 🔴 材料对应的 Buff：直接从 Map 中获取
    public static MobEffect getEffectForMaterial(Item item) {
        return MATERIAL_EFFECTS.get(item);
    }

    @Override
    public CompoundTag save(CompoundTag nbt) {
        ListTag playerList = new ListTag();
        for (Map.Entry<UUID, List<ShrineData>> entry : playerShrines.entrySet()) {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("Player", entry.getKey());
            
            ListTag shrines = new ListTag();
            for (ShrineData data : entry.getValue()) {
                CompoundTag shrineTag = new CompoundTag();
                shrineTag.putLong("Pos", data.pos.asLong());
                shrineTag.putString("Dim", data.dimension.location().toString());
                
                ListTag items = new ListTag();
                for (Map.Entry<Item, Integer> itemEntry : data.donatedItems.entrySet()) {
                    CompoundTag itemTag = new CompoundTag();
                    ResourceLocation key = ForgeRegistries.ITEMS.getKey(itemEntry.getKey());
                    if (key != null) {
                        itemTag.putString("id", key.toString());
                        itemTag.putInt("Count", itemEntry.getValue());
                        items.add(itemTag);
                    }
                }
                shrineTag.put("Items", items);
                shrines.add(shrineTag);
            }
            playerTag.put("Shrines", shrines);
            playerList.add(playerTag);
        }
        nbt.put("PlayerData", playerList);
        return nbt;
    }

    public static SevenGodSavedData load(CompoundTag nbt) {
        SevenGodSavedData data = new SevenGodSavedData();
        if (nbt.contains("PlayerData")) {
            ListTag playerList = nbt.getList("PlayerData", Tag.TAG_COMPOUND);
            for (int i = 0; i < playerList.size(); i++) {
                CompoundTag playerTag = playerList.getCompound(i);
                UUID uuid = playerTag.getUUID("Player");
                
                List<ShrineData> shrines = new ArrayList<>();
                ListTag shrineList = playerTag.getList("Shrines", Tag.TAG_COMPOUND);
                for (int j = 0; j < shrineList.size(); j++) {
                    CompoundTag shrineTag = shrineList.getCompound(j);
                    BlockPos pos = BlockPos.of(shrineTag.getLong("Pos"));
                    ResourceKey<Level> dim = ResourceKey.create(Registries.DIMENSION, ModUtils.def(shrineTag.getString("Dim")));
                    ShrineData shrineData = new ShrineData(pos, dim);
                    
                    ListTag items = shrineTag.getList("Items", Tag.TAG_COMPOUND);
                    for (int k = 0; k < items.size(); k++) {
                        CompoundTag itemTag = items.getCompound(k);
                        Item item = ForgeRegistries.ITEMS.getValue(ModUtils.def(itemTag.getString("id")));
                        if (item != null) {
                            shrineData.donatedItems.put(item, itemTag.getInt("Count"));
                        }
                    }
                    shrines.add(shrineData);
                }
                data.playerShrines.put(uuid, shrines);
            }
        }
        return data;
    }

    /**
     * 获取该 Buff 允许的最高等级
     * 部分 Buff（如夜视、隐身）超过 1 级会导致视觉异常或无效
     */
    public static int getMaxLevel(MobEffect effect) {
        if (effect == MobEffects.NIGHT_VISION ||
                effect == MobEffects.INVISIBILITY ||
                effect == MobEffects.WATER_BREATHING ||
                effect == MobEffects.FIRE_RESISTANCE ||
                effect == MobEffects.CONDUIT_POWER ||
                effect == MobEffects.DOLPHINS_GRACE ||
                effect == MobEffects.SLOW_FALLING) {
            return 1; // 最高 1 级
        }
        return 10;
    }
}