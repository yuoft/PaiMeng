package com.yuo.paimeng.event;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.yuo.paimeng.PaiMeng;
import com.yuo.paimeng.network.CookingPacket;
import com.yuo.paimeng.world.data.FoodRecipeSaveData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID)
public class PMCommandEventHandler {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();

        dispatcher.register(
                Commands.literal("paimeng")
                        .requires(source -> source.hasPermission(2))
                        .then(Commands.literal("food")
                                // /paimeng food query <targets>  查询等级 + 经验 + 升级所需
                                .then(Commands.literal("query")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                .executes(context -> queryFood(
                                                        context.getSource(),
                                                        EntityArgument.getPlayers(context, "targets")
                                                ))
                                        )
                                )
                                .then(Commands.literal("level")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                // /paimeng food level <targets>  查询等级
                                                .executes(context -> queryFoodLevel(
                                                        context.getSource(),
                                                        EntityArgument.getPlayers(context, "targets")
                                                ))
                                                // /paimeng food level <targets> <level>  设置等级
                                                .then(Commands.argument("level", IntegerArgumentType.integer(0, 5))
                                                        .executes(context -> setFoodLevel(
                                                                context.getSource(),
                                                                EntityArgument.getPlayers(context, "targets"),
                                                                IntegerArgumentType.getInteger(context, "level")
                                                        ))
                                                )
                                        )
                                )
                                .then(Commands.literal("exp")
                                        .then(Commands.argument("targets", EntityArgument.players())
                                                // /paimeng food exp <targets>  查询经验
                                                .executes(context -> queryFoodExp(
                                                        context.getSource(),
                                                        EntityArgument.getPlayers(context, "targets")
                                                ))
                                                // /paimeng food exp <targets> <exp>  设置经验
                                                .then(Commands.argument("exp", IntegerArgumentType.integer(0))
                                                        .executes(context -> setFoodExp(
                                                                context.getSource(),
                                                                EntityArgument.getPlayers(context, "targets"),
                                                                IntegerArgumentType.getInteger(context, "exp")
                                                        ))
                                                )
                                        )
                                )
                        )
        );
    }

    // ------------------------- 查询 -------------------------

    /** /paimeng food query &lt;targets&gt;：等级 + 经验 + 升到下一级所需经验 */
    private static int queryFood(CommandSourceStack source, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            FoodRecipeSaveData.PlayerFoodRecipesInfo info = getInfo(player);
            int level = info.getLevel();
            int exp = info.getExp();
            int need = CookingPacket.getUpExp(level);
            source.sendSuccess(() -> Component.translatable(
                    "paimeng.command.food.query",
                    player.getName().getString(),
                    level,
                    exp,
                    need
            ), false);
        }
        return targets.size();
    }

    /** /paimeng food level &lt;targets&gt;：只查等级 */
    private static int queryFoodLevel(CommandSourceStack source, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            int level = getInfo(player).getLevel();
            source.sendSuccess(() -> Component.translatable(
                    "paimeng.command.food.query.level",
                    player.getName().getString(),
                    level
            ), false);
        }
        return targets.size();
    }

    /** /paimeng food exp &lt;targets&gt;：只查经验 */
    private static int queryFoodExp(CommandSourceStack source, Collection<ServerPlayer> targets) {
        for (ServerPlayer player : targets) {
            int exp = getInfo(player).getExp();
            source.sendSuccess(() -> Component.translatable(
                    "paimeng.command.food.query.exp",
                    player.getName().getString(),
                    exp
            ), false);
        }
        return targets.size();
    }

    /** 读取玩家烹饪数据（没有记录时返回 0 级 0 经验，不会创建空条目）。 */
    private static FoodRecipeSaveData.PlayerFoodRecipesInfo getInfo(ServerPlayer player) {
        FoodRecipeSaveData data = FoodRecipeSaveData.get(player.level());
        return data.hasPlayer(player.getUUID())
                ? data.getInfo(player.getUUID())
                : new FoodRecipeSaveData.PlayerFoodRecipesInfo(player.getUUID(), 0, 0);
    }

    // ------------------------- 修改 -------------------------

    /**
     * 修改玩家烹饪等级
     */
    private static int setFoodLevel(CommandSourceStack source, Collection<ServerPlayer> targets, int level) {
        for (ServerPlayer player : targets) {
            FoodRecipeSaveData data = FoodRecipeSaveData.get(player.level());
            if (!data.hasPlayer(player.getUUID())) {
                data.add(player.getUUID(), level, 0);
            } else {
                int exp = data.getInfo(player.getUUID()).getExp();
                data.change(player.getUUID(), level, exp);
            }

            // 🔴 使用语言文件，依次传入玩家名、等级
            source.sendSuccess(() -> Component.translatable(
                    "paimeng.command.food.level.success",
                    player.getName().getString(),
                    level
            ), true);
        }
        return targets.size();
    }

    /**
     * 修改玩家烹饪经验
     */
    private static int setFoodExp(CommandSourceStack source, Collection<ServerPlayer> targets, int exp) {
        for (ServerPlayer player : targets) {
            FoodRecipeSaveData data = FoodRecipeSaveData.get(player.level());
            if (!data.hasPlayer(player.getUUID())) {
                data.add(player.getUUID(), 0, exp);
            } else {
                int level = data.getInfo(player.getUUID()).getLevel();
                data.change(player.getUUID(), level, exp);
            }

            // 🔴 使用语言文件，依次传入玩家名、经验
            source.sendSuccess(() -> Component.translatable(
                    "paimeng.command.food.exp.success",
                    player.getName().getString(),
                    exp
            ), true);
        }
        return targets.size();
    }
}
