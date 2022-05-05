package com.yuo.PaiMeng.WorldData;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.DimensionSavedDataManager;
import net.minecraft.world.storage.WorldSavedData;

import java.util.ArrayList;
import java.util.List;

//保存玩家合成等级信息
public class FoodLevelWorldSaveData extends WorldSavedData {
    private static final String NBT_NAME = "PaiMeng_FoodRecipes";
    private static final String NBT_PLAYER = "PaiMeng_FoodRecipes_PlayerName";
    private static final String NBT_LEVEL = "PaiMeng_FoodRecipes_Level";
    private static final String NBT_EXP = "PaiMeng_FoodRecipes_Exp";

    private final List<PlayerFoodRecipesInfo> players = new ArrayList<>();
    public FoodLevelWorldSaveData(String name) {
        super(name);
    }

    public FoodLevelWorldSaveData(){
        this(NBT_NAME);
    }

    @Override
    public void read(CompoundNBT nbt) {
        ListNBT listNBT = (ListNBT) nbt.get(NBT_NAME);
        if (listNBT != null){
            for (INBT inbt : listNBT) {
                CompoundNBT compoundNBT = (CompoundNBT) inbt;
                players.add(new PlayerFoodRecipesInfo(compoundNBT.getString(NBT_PLAYER),
                        compoundNBT.getInt(NBT_LEVEL), compoundNBT.getInt(NBT_EXP)));
            }
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        ListNBT listNBT = new ListNBT();
        for (PlayerFoodRecipesInfo player : players) {
            CompoundNBT nbt = new CompoundNBT();
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
        this.markDirty();
    }

    //修改数据
    public void change(String name, int level, int exp){
        for (PlayerFoodRecipesInfo player : players) {
            if (player.playerName.equals(name)){
                player.level = level;
                player.exp = exp;
                this.markDirty();
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
                    this.markDirty();
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

    public static FoodLevelWorldSaveData get(World worldIn) {
        if (!(worldIn instanceof ServerWorld)) {
            throw new RuntimeException("World is not server!");
        }
        ServerWorld world = (ServerWorld) worldIn;
        DimensionSavedDataManager storage = world.getSavedData();
        return storage.getOrCreate(FoodLevelWorldSaveData::new, NBT_NAME);
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
