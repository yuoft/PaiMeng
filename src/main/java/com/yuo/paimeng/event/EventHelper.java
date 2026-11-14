package com.yuo.paimeng.event;

import com.yuo.paimeng.capability.IBlowCapability;
import com.yuo.paimeng.capability.ModCapability;
import com.yuo.paimeng.effects.EffectRegistry;
import com.yuo.paimeng.items.RelicsHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.registries.ForgeRegistries;

public class EventHelper {

    /**
     * 获取物品的命名空间
     * @param stack 物品
     * @return 命名空间
     */
    public static String getNameSpace(ItemStack stack){
        //1.20.1: Item#getRegistryName 已删除，改用注册表查询
        ResourceLocation registryName = ForgeRegistries.ITEMS.getKey(stack.getItem());
        if (registryName != null) return registryName.getNamespace();
        else return "null";
    }

    /**
     * 修改玩家能力值 buff生效
     * @param key 当前玩家标识
     * @param player 玩家
     * @param cap 玩家的能力
     */
    public static void changePlayerCap(String key, Player player, LazyOptional<IBlowCapability> cap){
        int criticalRate = getEffectLevel(player, EffectRegistry.CRITICAL_RATE.get());
        int criticalDamage = getEffectLevel(player, EffectRegistry.CRITICAL_DAMAGE.get());
        //暴击率
        if (criticalRate > 0){
            int level = player.getPersistentData().getInt("paimeng:critical_rate_level");
            if (level != criticalRate){ //buff等级变更
                player.getPersistentData().putInt("paimeng:critical_rate_level", criticalRate);
                EventHandler.playerCriticalRate.remove(key);
            }
            if (!EventHandler.playerCriticalRate.contains(key)){
                player.getPersistentData().putInt("paimeng:critical_rate_level", criticalRate);
                cap.ifPresent(e -> e.setCriticalRate(EventHandler.attrCriticalRate * criticalRate)); //暴击率增加
                EventHandler.playerCriticalRate.add(key); //有效果add
            }
        }else {
            if (EventHandler.playerCriticalRate.contains(key)){
                int level = player.getPersistentData().getInt("paimeng:critical_rate_level");
                cap.ifPresent(e -> e.setCriticalRate(-EventHandler.attrCriticalRate * level));
                EventHandler.playerCriticalRate.remove(key);
            }
        }
        //暴击伤害
        if (criticalDamage > 0){
            int level = player.getPersistentData().getInt("paimeng:critical_damage_level");
            if (level != criticalDamage){ //buff等级变更
                player.getPersistentData().putInt("paimeng:critical_damage_level", criticalDamage);
                EventHandler.playerCriticalDamage.remove(key);
            }
            if (!EventHandler.playerCriticalDamage.contains(key)){
                player.getPersistentData().putInt("paimeng:critical_damage_level", criticalDamage);
                cap.ifPresent(e -> e.setCriticalDamage(EventHandler.attrCriticalDamage * criticalDamage)); //暴击伤害增加
                EventHandler.playerCriticalDamage.add(key); //有效果add
            }
        }else {
            if (EventHandler.playerCriticalDamage.contains(key)){
                int level = player.getPersistentData().getInt("paimeng:critical_damage_level");
                cap.ifPresent(e -> e.setCriticalDamage(-EventHandler.attrCriticalDamage * level));
                EventHandler.playerCriticalDamage.remove(key);
            }
        }
    }


    /**
     * 更改玩家能力数据
     * @param player 玩家
     */
    public static void changeAttribute(Player player){
        //确定某一个玩家
        String key = player.getGameProfile().getName()+":"+player.level().isClientSide;
        LazyOptional<IBlowCapability> capability = player.getCapability(ModCapability.BLOW_CAPABILITY);
        if (capability.isPresent()) {//能力不为空
            changePlayerCap(key, player, capability);
            IBlowCapability blowCapability = capability.orElseThrow(NoClassDefFoundError::new);
            if (EventHandler.playerCritical.contains(key)){
                RelicsHelper.downCritical(player, blowCapability);
            }
            RelicsHelper.upCritical(player, blowCapability, key);
        }
    }

    /**
     * 快速穿戴圣遗物
     * @param player 玩家
     * @param world 世界
     * @param index 部位
     * @param stacks 圣遗物数据
     * @param stack 当前使用圣遗物
     */
    public static void wearRelics(Player player, InteractionHand hand, Level world, int index, NonNullList<ItemStack> stacks, ItemStack stack){
        if (stacks.get(index).isEmpty()){
            stacks.set(index, stack.copy());
            if (!player.isCreative())
                stack.shrink(1);
            player.swing(hand);
            world.playSound(player, player.blockPosition(), SoundEvents.ARMOR_EQUIP_DIAMOND, SoundSource.PLAYERS, 1.0f, 3.0f);
        }
    }

    /**
     * 获取玩家某一生效buff等级， 0表示无此buff
     * @param player 玩家
     * @param effect buff
     * @return buff等级
     */
    public static int getEffectLevel(Player player, MobEffect effect){
        MobEffectInstance effectInstance = player.getEffect(effect);
        if (effectInstance != null){
            return effectInstance.getAmplifier();
        }
        return -1;
    }

    /**
     * 添加生物掉落
     * @param event 事件
     * @param stack 要添加的掉落物品
     * @param livingEntity 生物
     */
    protected static void addEntityDrops(LivingDropsEvent event, ItemStack stack, LivingEntity livingEntity){
        event.getDrops().add(new ItemEntity(livingEntity.level(), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), stack ));
    }

    /**
     * 添加额外掉落
     * @param item 需要掉落的物品
     * @param count 数量
     * @param world 世界
     * @param pos 坐标
     * @param event 事件
     */
    protected static void spawnDrops(Item item, int count, Level world, BlockPos pos, LivingDropsEvent event){
        if (count <= 0) return;
        ItemStack stack1 = new ItemStack(item, count);
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack1);
        event.getDrops().add(itemEntity);
    }
}