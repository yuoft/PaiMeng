package com.yuo.paimeng.event;

import com.yuo.paimeng.PaiMeng;
import com.yuo.paimeng.entity.HypostasisEntity;
import com.yuo.paimeng.entity.PMEntityTypes;
import com.yuo.paimeng.items.PMItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 无相之岩召唤祭坛：
 * 玩家用 8 种各不相同的矿物块搭出 2×2×2 的实心方块堆，再手持「下界之心」右击其中任意方块即可唤醒 BOSS。
 * 祭坛方块不会被消耗（可以反复召唤），消耗的是下界之心。
 */
@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HypostasisAltarEvent {

    /** 允许的 8 种矿物块，顺序与外壳渲染 / 抛掷方块保持一致。 */
    public static final List<Block> ALTAR_BLOCKS = List.of(
            Blocks.IRON_BLOCK,
            Blocks.GOLD_BLOCK,
            Blocks.DIAMOND_BLOCK,
            Blocks.EMERALD_BLOCK,
            Blocks.LAPIS_BLOCK,
            Blocks.REDSTONE_BLOCK,
            Blocks.COAL_BLOCK,
            Blocks.COPPER_BLOCK);

    @SubscribeEvent
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (!stack.is(PMItems.netherHeart.get())) return;

        Level level = event.getLevel();
        // 判定只在服务端做，客户端照常挥手
        if (level.isClientSide) return;

        Player player = event.getEntity();
        if (trySummon(level, event.getPos(), player)) {
            if (!player.getAbilities().instabuild) stack.shrink(1);
            player.displayClientMessage(Component.translatable("paimeng.text.hypostasis.altar_success"), false);
        } else {
            player.displayClientMessage(Component.translatable("paimeng.text.hypostasis.altar_fail"), true);
        }

        event.setCancellationResult(InteractionResult.SUCCESS);
        event.setCanceled(true);
    }

    /** 在点击位置周围寻找一个完整祭坛（点击的方块可以是祭坛里的任意一块，或紧贴祭坛的方块）。 */
    private static boolean trySummon(Level level, BlockPos clicked, Player player) {
        for (int ox = clicked.getX() - 1; ox <= clicked.getX(); ox++) {
            for (int oy = clicked.getY() - 1; oy <= clicked.getY(); oy++) {
                for (int oz = clicked.getZ() - 1; oz <= clicked.getZ(); oz++) {
                    BlockPos origin = new BlockPos(ox, oy, oz);
                    if (!isCompleteAltar(level, origin)) continue;
                    if (summon(level, origin, player)) return true;
                }
            }
        }
        return false;
    }

    /** 2×2×2 共 8 个位置，必须全部是矿物块且 8 块互不相同。 */
    private static boolean isCompleteAltar(Level level, BlockPos origin) {
        Set<Block> found = new HashSet<>();
        for (int dx = 0; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                for (int dz = 0; dz <= 1; dz++) {
                    Block block = level.getBlockState(origin.offset(dx, dy, dz)).getBlock();
                    if (!ALTAR_BLOCKS.contains(block)) return false;
                    if (!found.add(block)) return false;
                }
            }
        }
        return found.size() == ALTAR_BLOCKS.size();
    }

    private static boolean summon(Level level, BlockPos origin, Player player) {
        HypostasisEntity boss = PMEntityTypes.HYPOSTASIS.get().create(level);
        if (boss == null) return false;

        double x = origin.getX() + 1.0D;
        double y = origin.getY() + 3.1D;
        double z = origin.getZ() + 1.0D;
        boss.moveTo(x, y, z, player.getYRot() + 180.0F, 0.0F);
        boss.setTarget(player);
        level.addFreshEntity(boss);

        level.playSound(null, origin, SoundEvents.END_PORTAL_SPAWN, SoundSource.HOSTILE, 1.5F, 0.7F);
        level.playSound(null, origin, SoundEvents.BEACON_ACTIVATE, SoundSource.HOSTILE, 1.2F, 0.9F);
        if (level instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.END_ROD, x, y, z, 80, 1.4D, 1.0D, 1.4D, 0.2D);
            server.sendParticles(ParticleTypes.EXPLOSION, x, y, z, 6, 1.2D, 0.8D, 1.2D, 0.0D);
        }
        return true;
    }
}
