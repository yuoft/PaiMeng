package com.yuo.paimeng.network;

import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.recipe.ModRecipeType;
import com.yuo.paimeng.tiles.BenchTile;
import com.yuo.paimeng.tiles.PotTile;
import com.yuo.paimeng.tiles.SuperBenchTile;
import com.yuo.paimeng.tiles.TileUtils;
import com.yuo.paimeng.world.data.FoodRecipeSaveData;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class CookingPacket {
    // 🔴 移除 static 修饰符，改为实例字段
    private final BlockPos pos;
    private final int count;
    private final int exp;
    private final int star;

    // 反序列化构造
    public CookingPacket(FriendlyByteBuf buffer) {
        this.pos = buffer.readBlockPos();
        this.count = buffer.readInt();
        this.exp = buffer.readInt();
        this.star = buffer.readInt();
    }

    // 序列化构造
    public CookingPacket(BlockPos pos, int count, int exp, int star) {
        this.pos = pos;
        this.count = count;
        this.exp = exp;
        this.star = star;
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.count);
        buf.writeInt(this.exp);
        buf.writeInt(this.star);
    }

    public static void handler(CookingPacket msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer player = ctx.get().getSender();
            if (player == null) return;
            Level world = player.level();
            
            // 🔴 安全校验：玩家必须距离方块 8 格以内，防止远程作弊发包
            if (player.distanceToSqr(msg.pos.getX() + 0.5, msg.pos.getY() + 0.5, msg.pos.getZ() + 0.5) > 64.0) {
                return;
            }

            if (world.isLoaded(msg.pos)) {
                BlockEntity tile = world.getBlockEntity(msg.pos);
                // 处理锅的烹饪结果
                if (tile instanceof PotTile potTile) {
                    potTile.setItem(5, TileUtils.getTileStack(world, ModRecipeType.POT, potTile, msg.count, msg.star));
                    TileUtils.shirkItem(potTile, TileUtils.getRecipeInputs(world, ModRecipeType.POT, potTile), msg.count);
                }

                // 处理灶台的烹饪结果
                if (tile instanceof BenchTile benchTile) {
                    benchTile.setItem(5, TileUtils.getTileStack(world, ModRecipeType.BENCH, benchTile, msg.count, msg.star));
                    TileUtils.shirkItem(benchTile, TileUtils.getRecipeInputs(world, ModRecipeType.BENCH, benchTile), msg.count);
                }
                if (tile instanceof SuperBenchTile superBenchTile) {
                    superBenchTile.setItem(5, TileUtils.getTileStack(world, ModRecipeType.BENCH, superBenchTile, msg.count, msg.star));
                    TileUtils.shirkItem(superBenchTile, TileUtils.getRecipeInputs(world, ModRecipeType.BENCH, superBenchTile), msg.count);
                }

                // 额外产出判定
                spawnOtherFood(world, tile, msg.pos, msg.count, msg.star);
                
                // 增加烹饪经验
                if (msg.exp > 0) {
                    FoodRecipeSaveData data = FoodRecipeSaveData.get(world);
                    if (!data.hasPlayer(player.getUUID())) {
                        data.add(player.getUUID(), 0, 0);
                    }
                    FoodRecipeSaveData.PlayerFoodRecipesInfo foodRecipesInfo = data.getInfo(player.getUUID());
                    if (!foodRecipesInfo.isEmpty()) {
                        int foodRecipLv = foodRecipesInfo.getLevel();
                        int foodRecipeExp = foodRecipesInfo.getExp();
                        if (foodRecipLv > 5) {
                            foodRecipesInfo.setExp(0);
                        } else {
                            int foodRecipeUpExp = getLvUpExp(foodRecipLv, foodRecipeExp, msg.exp);
                            if (foodRecipeUpExp > 0) {
                                foodRecipesInfo.setLevel(++foodRecipLv);
                                foodRecipesInfo.setExp(foodRecipeUpExp);
                            } else {
                                foodRecipesInfo.setExp(foodRecipeExp + msg.exp);
                            }
                        }
                    }
                    data.change(foodRecipesInfo);
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }

    /**
     * 获取升级后的剩余经验 为0则不可升级
     */
    public static int getLvUpExp(int level, int exp, int upExp) {
        if (level > 5) return 0;
        int expAll = exp + upExp;
        int expDown = getUpExp(level);
        if (expAll > expDown) {
            return expAll - expDown;
        }
        return 0;
    }

    // 经验计算
    public static int getUpExp(int level) {
        return (level * 2 + 1) * 100 + (int) Math.pow(2, level) * 100;
    }

    /**
     * 生成额外产出食物
     */
    public static void spawnOtherFood(Level world, BlockEntity tile, BlockPos pos, int count, int star) {
        RandomSource rand = world.random;
        // 1% + 每级星级增加1%概率产生 派蒙
        if (count > 0 && rand.nextDouble() > 0.99 - (star - 1) * 0.01d) { 
            world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, new ItemStack(PMItems.paimengFood.get())));
        }
        // 烹饪失败：3% + 每级星级增加1%概率产生 裁决之时
        if (count == 0 && rand.nextDouble() > 0.97 - (star - 1) * 0.01d) { 
            ItemStack stack = new ItemStack(PMItems.bugFood.get());
            if (tile instanceof PotTile potTile) {
                ItemStack itemStack = potTile.getItem(5);
                if (itemStack.isEmpty()) {
                    potTile.setItem(5, stack);
                    return;
                }
            }
            if (tile instanceof BenchTile benchTile) {
                ItemStack itemStack = benchTile.getItem(5);
                if (itemStack.isEmpty()) {
                    benchTile.setItem(5, stack);
                    return;
                }
            }
            world.addFreshEntity(new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, stack));
        }
    }
}