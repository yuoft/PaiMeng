package com.yuo.paimeng.network;

import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.recipe.ModRecipeType;
import com.yuo.paimeng.tiles.BenchTile;
import com.yuo.paimeng.tiles.PotTile;
import com.yuo.paimeng.tiles.TileUtils;
import com.yuo.paimeng.savedata.FoodLevelWorldSaveData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.RandomSource;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

//控制
public class CookingPacket {
    private static BlockPos pos; //坐标
    private static int count; //数量
    private static int exp; //经验
    private static int star; //星级
    public CookingPacket(FriendlyByteBuf buffer) {
        pos = buffer.readBlockPos();
        count = buffer.readInt();
        exp = buffer.readInt();
        star = buffer.readInt();
    }

    public CookingPacket(BlockPos pos, int count, int exp, int star) {
        CookingPacket.pos = pos;
        CookingPacket.count = count;
        CookingPacket.exp = exp;
        CookingPacket.star = star;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(count);
        buf.writeInt(exp);
        buf.writeInt(star);
    }

    public static void handler(CookingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            Level world = player.level();
            if (!world.isClientSide && world.isLoaded(pos)){ //检查此坐标区块是否加载
                BlockEntity tile = world.getBlockEntity(pos);
                if (tile instanceof PotTile potTile){
                    potTile.setItem(5, TileUtils.getTileStack(world, ModRecipeType.POT, potTile, count, star));
                    TileUtils.shirkItem(potTile, TileUtils.getRecipeInputs(world, ModRecipeType.POT, potTile), count);
                }
                if (tile instanceof BenchTile benchTile){
                    benchTile.setItem(5, TileUtils.getTileStack(world, ModRecipeType.BENCH, benchTile, count, star));
                    TileUtils.shirkItem(benchTile, TileUtils.getRecipeInputs(world, ModRecipeType.BENCH, benchTile), count);
                }
                spawnFood(world, tile);
                //增加烹饪经验
                if (exp > 0){
                    FoodLevelWorldSaveData data = FoodLevelWorldSaveData.get(world);
                    String playerName = player.getName().getString();
                    if (!data.hasName(playerName)){ //不存在
                        data.add(playerName, 0, 0);
                    }
                    FoodLevelWorldSaveData.PlayerFoodRecipesInfo foodRecipesInfo = data.getInfo(playerName);
                    if (!foodRecipesInfo.isEmpty()){
                        int level = foodRecipesInfo.getLevel();
                        int exp0 = foodRecipesInfo.getExp();
                        if (level == 5){
                            foodRecipesInfo.setExp(0);
                        }else {
                            int levelUpExp = getLevelUpExp(level, exp0, exp);
                            if (levelUpExp > 0){
                                foodRecipesInfo.setLevel(++level);
                                foodRecipesInfo.setExp(levelUpExp);
                            }else foodRecipesInfo.setExp(exp0 + exp);
                        }
                    }
                    data.change(foodRecipesInfo);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    public static BlockPos getPos() {
        return pos;
    }

    /**
     * 获取升级后的剩余经验 为0则不可升级
     * @param level 当前等级
     * @param exp 当前经验
     * @param upExp 获取经验
     * @return 剩余经验
     */
    public static int getLevelUpExp(int level, int exp, int upExp){
        if (level == 5) return 0;
        int expAll = exp + upExp;
        int expDown = getUpExp(level);
        if (expAll > expDown){
            return expAll - expDown;
        }
        return 0;
    }

    //经验计算
    public static int getUpExp(int level){
        return (level * 2 + 1) * 100 + (int) Math.pow(2,  level) * 100; //升级经验
    }

    /**
     * 生成额外产出食物
     * @param world 世界
     * @param tile 方块实体
     */
    public static void spawnFood(Level world, BlockEntity tile){
        RandomSource rand = world.random;
        if (count > 0 && rand.nextDouble() > 0.99 - (star - 1) * 0.01d){ //1% + 每级星级增加1%概率产生 派蒙
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(),pos.getZ(), new ItemStack(PMItems.paimengFood.get())));
        }
        //烹饪失败
        if (count == 0 && rand.nextDouble() > 0.97 - (star - 1) * 0.01d){ //3% + 每级星级增加1%概率产生 裁决之时
            ItemStack stack = new ItemStack(PMItems.bugFood.get());
            if (tile instanceof PotTile potTile){
                ItemStack itemStack = potTile.getItem(5);
                if (itemStack.isEmpty()){
                    potTile.setItem(5, stack);
                    return;
                }
            }
            if (tile instanceof BenchTile benchTile){
                ItemStack itemStack = benchTile.getItem(5);
                if (itemStack.isEmpty()){
                    benchTile.setItem(5, stack);
                    return;
                }
            }
            world.addFreshEntity(new ItemEntity(world, pos.getX(), pos.getY(),pos.getZ(), stack));
        }
    }
}
