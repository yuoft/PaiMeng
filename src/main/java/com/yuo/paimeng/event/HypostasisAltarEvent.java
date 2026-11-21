package com.yuo.paimeng.event;

import com.yuo.paimeng.PaiMeng;
import com.yuo.paimeng.entity.HypostasisEntity;
import com.yuo.paimeng.entity.PMEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class HypostasisAltarEvent {

    public static final List<Block> ALTAR_BLOCKS = List.of(
            Blocks.IRON_BLOCK,
            Blocks.GOLD_BLOCK,
            Blocks.DIAMOND_BLOCK,
            Blocks.EMERALD_BLOCK,
            Blocks.LAPIS_BLOCK,
            Blocks.REDSTONE_BLOCK,
            Blocks.NETHERITE_BLOCK,
            Blocks.COPPER_BLOCK);

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void onRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getItemStack();
        if (!stack.is(Items.NETHER_STAR)) return;

        Level level = event.getLevel();
        Player player = event.getEntity();

        // 客户端直接拦截，防止客户端发送原版交互包，导致动作不同步
        if (level.isClientSide) {
            if (findAltar(level, event.getPos()) != null) {
                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
            }
            return;
        }

        // ================= 以下为服务端逻辑 =================
        BlockPos altarOrigin = findAltar(level, event.getPos());
        
        if (altarOrigin != null) {
            if (summonBoss(level, altarOrigin, player)) {
                // 扣除物品
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                player.displayClientMessage(Component.translatable("paimeng.text.hypostasis.altar_success"), false);
            }
            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        } else {
            player.displayClientMessage(Component.translatable("paimeng.text.hypostasis.altar_fail"), true);
        }
    }

    private static BlockPos findAltar(Level level, BlockPos clicked) {
        for (int ox = clicked.getX() - 1; ox <= clicked.getX() + 1; ox++) {
            for (int oy = clicked.getY() - 1; oy <= clicked.getY() + 1; oy++) {
                for (int oz = clicked.getZ() - 1; oz <= clicked.getZ() + 1; oz++) {
                    BlockPos origin = new BlockPos(ox, oy, oz);
                    if (isCompleteAltar(level, origin)) {
                        return origin;
                    }
                }
            }
        }
        return null;
    }

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

    private static boolean summonBoss(Level level, BlockPos origin, Player player) {
        HypostasisEntity boss = PMEntityTypes.HYPOSTASIS.get().create(level);
        if (boss == null) return false;

        double x = origin.getX() + 1.0D;
        double y = origin.getY() + 3.1D;
        double z = origin.getZ() + 1.0D;
        boss.moveTo(x, y, z, player.getYRot() + 180.0F, 0.0F);
        boss.setTarget(player);
        
        if (!level.addFreshEntity(boss)) return false;

        // 🔴 新增：清除祭坛方块（消耗仪式材料）
        for (int dx = 0; dx <= 1; dx++) {
            for (int dy = 0; dy <= 1; dy++) {
                for (int dz = 0; dz <= 1; dz++) {
                    BlockPos pos = origin.offset(dx, dy, dz);
                    // 移除方块，false 表示不触发方块破坏的额外掉落
                    level.removeBlock(pos, false);
                    
                    // 在方块位置生成烟雾和爆炸粒子，表现"被吸干"的效果
                    if (level instanceof ServerLevel server) {
                        server.sendParticles(ParticleTypes.CLOUD, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 
                                6, 0.3D, 0.3D, 0.3D, 0.05D);
                        server.sendParticles(ParticleTypes.EXPLOSION, pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D, 
                                1, 0.0D, 0.0D, 0.0D, 0.0D);
                    }
                }
            }
        }

        // 播放仪式音效
        level.playSound(null, origin, SoundEvents.END_PORTAL_SPAWN, SoundSource.HOSTILE, 1.5F, 0.7F);
        level.playSound(null, origin, SoundEvents.BEACON_ACTIVATE, SoundSource.HOSTILE, 1.2F, 0.9F);
        level.playSound(null, origin, SoundEvents.GLASS_BREAK, SoundSource.HOSTILE, 1.0F, 0.5F);
        
        // 生成中心爆发粒子
        if (level instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.END_ROD, x, y, z, 80, 1.4D, 1.0D, 1.4D, 0.2D);
            server.sendParticles(ParticleTypes.EXPLOSION_EMITTER, x, y, z, 2, 1.2D, 0.8D, 1.2D, 0.0D);
        }
        return true;
    }
}