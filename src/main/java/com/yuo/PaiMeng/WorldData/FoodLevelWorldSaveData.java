package com.yuo.PaiMeng.WorldData;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.level.Level;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.ArrayList;
import java.util.List;

//保存玩家合成等级信息
//1.20.1: WorldSavedData -> net.minecraft.world.level.saveddata.SavedData
//        DimensionSavedDataManager#getOrCreate -> DimensionDataStorage#computeIfAbsent(load, create, name)
//        markDirty -> setDirty, write(CompoundTag) -> save(CompoundTag) 返回 CompoundTag
public class FoodLevelWorldSaveData extends SavedData {
    private static final String NBT_NAME = "PaiMeng_FoodRecipes";
    private static final String NBT_PLAYER = "PaiMeng_FoodRecipes_PlayerName";
    private static final String NBT_LEVEL = "PaiMeng_FoodRecipes_Level";
    private static final String NBT_EXP = "PaiMeng_FoodRecipes_Exp";
    private static final String DATA_NAME = "paimeng_food";

    private final List<PlayerFoodRecipesInfo> players = new ArrayList<>();

    public FoodLevelWorldSaveData() {
    }

    /**
     * 1.16.5 的 WorldSavedData(String) 构造在 1.20.1 已无意义，保留同名构造只为兼容旧调用点。
     */
    public FoodLevelWorldSaveData(String name) {
        this();
    }

    /**
     * 1.20.1 从磁盘读取（对应 1.16.5 的 read(CompoundTag)）
     */
    public static FoodLevelWorldSaveData load(CompoundTag nbt) {
        FoodLevelWorldSaveData data = new FoodLevelWorldSaveData();
        data.read(nbt);
        return data;
    }

    public void read(CompoundTag nbt) {
        players.clear();
        ListTag listNBT = (ListTag) nbt.get(NBT_NAME);
        if (listNBT != null) {
            for (Tag inbt : listNBT) {
                if (inbt instanceof CompoundTag compoundNBT) {
                    players.add(new PlayerFoodRecipesInfo(compoundNBT.getString(NBT_PLAYER),
                            compoundNBT.getInt(NBT_LEVEL), compoundNBT.getInt(NBT_EXP)));
                }
            }
        }
    }

    @Override
    public CompoundTag save(CompoundTag compound) {
        ListTag listNBT = new ListTag();
        for (PlayerFoodRecipesInfo player : players) {
            CompoundTag nbt = new CompoundTag();
            nbt.putString(NBT_PLAYER, player.getPlayerName());
            nbt.putInt(NBT_LEVEL, player.getLevel());
            nbt.putInt(NBT_EXP, player.getExp());
            listNBT.add(nbt);
        }
        compound.put(NBT_NAME, listNBT);
        return compound;
    }

    //添加新数据
    public void add(String name, int level, int exp){
        players.add(new PlayerFoodRecipesInfo(name, level, exp));
        this.setDirty();
    }

    //修改数据
    public void change(String name, int level, int exp){
        for (PlayerFoodRecipesInfo player : players) {
            if (player.playerName.equals(name)){
                player.level = level;
                player.exp = exp;
                this.setDirty();
            }
        }
    }

    public void change(PlayerFoodRecipesInfo info){
        if (!info.isEmpty()){
            String name = info.getPlayerName();
            for (PlayerFoodRecipesInfo player : players) {
                if (player.playerName.equals(name)){
                    player.level = info.level;
                    player.exp = info.exp;
                    this.setDirty();
                }
            }

        }
    }

    //判断某玩家是否存在数据
    public boolean hasPlayer(String name){
        for (PlayerFoodRecipesInfo player : players) {
            if (player.playerName.equals(name)) return true;
        }
        return false;
    }

    //根据玩家名称获取数据
    public PlayerFoodRecipesInfo getInfo(String name){
        for (PlayerFoodRecipesInfo player : players) {
            if (player.playerName.equals(name)) return player;
        }

        return new PlayerFoodRecipesInfo("null", 0, 0);
    }

    //是否含有指定玩家
    public boolean hasName(String name){
        for (PlayerFoodRecipesInfo player : players) {
            if (player.playerName.equals(name)) return true;
        }
        return false;
    }

    public static FoodLevelWorldSaveData get(Level worldIn) {
        if (!(worldIn instanceof ServerLevel world)) {
            throw new RuntimeException("Level is not server!");
        }
        return world.getDataStorage().computeIfAbsent(FoodLevelWorldSaveData::load,
                FoodLevelWorldSaveData::new, DATA_NAME);
    }

    public static class PlayerFoodRecipesInfo{
        private final String playerName;
        private int exp;
        private int level;
        public PlayerFoodRecipesInfo(String name, int i, int j){
            this.playerName = name;
            this.level = i;
            this.exp = j;
        }

        public int getLevel() {
            return level;
        }

        public String getPlayerName() {
            return playerName;
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

        public boolean isEmpty(){
            return playerName.equals("null");
        }

    }
}
