package com.yuo.PaiMeng.NetWork;

import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.Recipes.ModRecipeType;
import com.yuo.PaiMeng.Tiles.BenchTile;
import com.yuo.PaiMeng.Tiles.PotTile;
import com.yuo.PaiMeng.Tiles.TileUtils;
import com.yuo.PaiMeng.WorldData.FoodLevelWorldSaveData;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.Random;
import java.util.function.Supplier;

//控制
public class CookingPacket {
    private static BlockPos pos; //坐标
    private static int count; //数量
    private static int exp; //经验
    private static int star; //星级
    public CookingPacket(PacketBuffer buffer) {
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

    public void toBytes(PacketBuffer buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(count);
        buf.writeInt(exp);
        buf.writeInt(star);
    }

    public static void handler(CookingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayerEntity player = ctx.get().getSender();
            if (player == null) return;
            World world = player.world;
            if (!world.isRemote && world.isBlockLoaded(pos)){ //检查此坐标区块是否加载
                TileEntity tile = world.getTileEntity(pos);
                if (tile instanceof PotTile){
                    PotTile potTile = (PotTile) tile;
                    potTile.setInventorySlotContents(4, TileUtils.getTileStack(world, ModRecipeType.POT, potTile, count, star));
                    TileUtils.shirkItem(potTile, TileUtils.getRecipeInputs(world, ModRecipeType.POT, potTile), count);
                }
                if (tile instanceof BenchTile){
                    BenchTile benchTile = (BenchTile) tile;
                    benchTile.setInventorySlotContents(4, TileUtils.getTileStack(world, ModRecipeType.BENCH, benchTile, count, star));
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
        return (level + 1) * 30 + (int) Math.pow(level + 1,  2) * 10; //升级经验
    }

    /**
     * 生成额外产出食物
     * @param world 世界
     * @param tile 方块实体
     */
    public static void spawnFood(World world, TileEntity tile){
        Random rand = world.rand;
        if (count > 0 && rand.nextDouble() > 0.99 - (star - 1) * 0.01d){ //1% + 每级星级增加1%概率产生 派蒙
            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(),pos.getZ(), new ItemStack(PMItems.paimengFood.get())));
        }
        //烹饪失败
        if (count == 0 && rand.nextDouble() > 0.97 - (star - 1) * 0.01d){ //3% + 每级星级增加1%概率产生 裁决之时
            ItemStack stack = new ItemStack(PMItems.bugFood.get());
            if (tile instanceof PotTile){
                ItemStack itemStack = ((PotTile) tile).getStackInSlot(4);
                if (itemStack.isEmpty()){
                    ((PotTile) tile).setInventorySlotContents(4, stack);
                    return;
                }
            }
            if (tile instanceof BenchTile){
                ItemStack itemStack = ((BenchTile) tile).getStackInSlot(4);
                if (itemStack.isEmpty()){
                    ((BenchTile) tile).setInventorySlotContents(4, stack);
                    return;
                }
            }
            world.addEntity(new ItemEntity(world, pos.getX(), pos.getY(),pos.getZ(), stack));
        }
    }
}
