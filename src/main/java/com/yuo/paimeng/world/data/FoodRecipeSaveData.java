package com.yuo.paimeng.world.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//保存玩家合成等级信息
public class FoodRecipeSaveData extends SavedData {
    private static final String NBT_NAME = "PaiMeng_FoodRecipes";
    private static final String NBT_UUID = "PaiMeng_FoodRecipes_PlayerUUID";
    private static final String NBT_LEVEL = "PaiMeng_FoodRecipes_Level";
    private static final String NBT_EXP = "PaiMeng_FoodRecipes_Exp";
    private static final String DATA_NAME = "paimeng_food_data";

    private final List<PlayerFoodRecipesInfo> players = new ArrayList<>();

    public FoodRecipeSaveData() {
    }

    public static FoodRecipeSaveData load(CompoundTag nbt) {
        FoodRecipeSaveData data = new FoodRecipeSaveData();
        data.read(nbt);
        return data;
    }

    public void read(CompoundTag nbt) {
        players.clear();
        ListTag listNBT = nbt.getList(NBT_NAME, Tag.TAG_COMPOUND);
        for (int i = 0; i < listNBT.size(); i++) {
            CompoundTag compoundNBT = listNBT.getCompound(i);
            
            // 只读取 UUID，不保留任何 Name 兼容逻辑
            if (compoundNBT.hasUUID(NBT_UUID)) {
                UUID uuid = compoundNBT.getUUID(NBT_UUID);
                players.add(new PlayerFoodRecipesInfo(uuid, 
                        compoundNBT.getInt(NBT_LEVEL), 
                        compoundNBT.getInt(NBT_EXP)));
            }
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag listNBT = new ListTag();
        for (PlayerFoodRecipesInfo player : players) {
            CompoundTag nbt = new CompoundTag();
            if (player.getPlayerUUID() != null) {
                nbt.putUUID(NBT_UUID, player.getPlayerUUID());
            }
            nbt.putInt(NBT_LEVEL, player.getLevel());
            nbt.putInt(NBT_EXP, player.getExp());
            listNBT.add(nbt);
        }
        compound.put(NBT_NAME, listNBT);
        return compound;
    }

    // 添加新数据
    public void add(UUID uuid, int level, int exp) {
        players.add(new PlayerFoodRecipesInfo(uuid, level, exp));
        this.setDirty();
    }

    // 修改数据 (直接传参)
    public void change(UUID uuid, int level, int exp) {
        for (PlayerFoodRecipesInfo player : players) {
            if (player.getPlayerUUID() != null && player.getPlayerUUID().equals(uuid)) {
                player.setLevel(level);
                player.setExp(exp);
                this.setDirty();
                break;
            }
        }
    }

    // 修改数据 (传对象)
    public void change(PlayerFoodRecipesInfo info) {
        if (!info.isEmpty()) {
            UUID uuid = info.getPlayerUUID();
            for (PlayerFoodRecipesInfo player : players) {
                if (player.getPlayerUUID() != null && player.getPlayerUUID().equals(uuid)) {
                    player.setLevel(info.getLevel());
                    player.setExp(info.getExp());
                    this.setDirty();
                    break;
                }
            }
        }
    }

    // 判断某玩家是否存在数据
    public boolean hasPlayer(UUID uuid) {
        for (PlayerFoodRecipesInfo player : players) {
            if (player.getPlayerUUID() != null && player.getPlayerUUID().equals(uuid)) {
                return true;
            }
        }
        return false;
    }

    // 根据玩家 UUID 获取数据
    public PlayerFoodRecipesInfo getInfo(UUID uuid) {
        for (PlayerFoodRecipesInfo player : players) {
            if (player.getPlayerUUID() != null && player.getPlayerUUID().equals(uuid)) {
                return player;
            }
        }
        return new PlayerFoodRecipesInfo(null, 0, 0);
    }

    public static FoodRecipeSaveData get(Level worldIn) {
        if (!(worldIn instanceof ServerLevel world)) {
            throw new RuntimeException("Level is not server!");
        }
        return world.getDataStorage().computeIfAbsent(FoodRecipeSaveData::load,
                FoodRecipeSaveData::new, DATA_NAME);
    }

    public static class PlayerFoodRecipesInfo {
        private final UUID playerUUID;
        private int exp;
        private int level;

        public PlayerFoodRecipesInfo(UUID uuid, int level, int exp) {
            this.playerUUID = uuid;
            this.level = level;
            this.exp = exp;
        }

        public UUID getPlayerUUID() {
            return playerUUID;
        }

        /** 当前玩家烹饪等级 用于解锁批量制作*/
        public int getLevel() {
            return level;
        }

        public int getExp() {
            return exp;
        }

        public void setLevel(int level) {
            this.level = level;
        }

        public void setExp(int exp) {
            this.exp = exp;
        }

        public boolean isEmpty() {
            return playerUUID == null;
        }
    }
}