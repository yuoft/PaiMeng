package com.yuo.paimeng.event;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.yuo.paimeng.PaiMeng;
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
                                .then(Commands.literal("level")
                                        .then(Commands.argument("targets", EntityArgument.players())
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