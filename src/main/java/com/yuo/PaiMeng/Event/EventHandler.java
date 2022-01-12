package com.yuo.PaiMeng.Event;

import com.yuo.PaiMeng.Capability.BlowCapanilityProvider;
import com.yuo.PaiMeng.Capability.IBlowCapability;
import com.yuo.PaiMeng.Capability.ModCapability;
import com.yuo.PaiMeng.Effects.EffectRegistry;
import com.yuo.PaiMeng.Items.Food.PaiMengFood;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * 事件处理类
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = PaiMeng.MOD_ID)
public class EventHandler {
    private final static Random random = new Random();
    public static List<String> playerCriticalRate = new ArrayList<>();
    public static List<String> playerCriticalDamage = new ArrayList<>();
    public static List<String> playerDefense = new ArrayList<>();
    public static List<String> playerAttack = new ArrayList<>();

    private static final float attrCriticalRate = 0.12f; //属性变更基础系数
    private static final float attrCriticalDamage = 0.18f;
    private static final float attrDefense = 2.0f;
    private static final float attrAttack = 3.0f;
    private static final float attrAttackPhysics = 2.0f;

    //禁用原版食物
    @SubscribeEvent
    public static void eatFood(LivingEntityUseItemEvent.Start event){
        ItemStack item = event.getItem();
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (item.isFood() && !(item.getItem() instanceof PaiMengFood)){//原版食物
                    player.sendStatusMessage(new TranslationTextComponent("paimeng.message.food"), true);
                    event.setCanceled(true);
            }
        }
    }

    //吃完食物后
    @SubscribeEvent
    public static void eatFoodEnd(LivingEntityUseItemEvent.Finish event){
        ItemStack item = event.getItem();
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (item.isFood() && item.getItem() instanceof PaiMengFood){
                PaiMengFood food = (PaiMengFood) item.getItem();
                int type = food.getTYPE();
                if (type == 0){
                    player.heal(food.getLEVEL() * 2);
                }
            }
        }
    }
    //添加物品信息
    @SubscribeEvent
    public static void itemMessage(ItemTooltipEvent event){
        ItemStack stack = event.getItemStack();
        if (stack.isFood() && !(stack.getItem() instanceof PaiMengFood)){ //原版食物
            List<ITextComponent> toolTip = event.getToolTip();
            toolTip.add(new TranslationTextComponent("paimeng.text.itemInfo.food"));
        }
    }

    //为实体附加能力
    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(PaiMeng.MOD_ID, "blow"), new BlowCapanilityProvider());
        }
    }

    //能力复制
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getOriginal().getCapability(ModCapability.BLOW_CAPABILITY).ifPresent(old -> {
            CompoundNBT bags = old.serializeNBT();
            event.getPlayer().getCapability(ModCapability.BLOW_CAPABILITY).ifPresent(c -> c.deserializeNBT(bags));
        });
//        if (!event.isWasDeath()) {
//            LazyOptional<IBlowCapability> oldSpeedCap = event.getOriginal().getCapability(ModCapability.BLOW_CAPABILITY);
//            LazyOptional<IBlowCapability> newSpeedCap = event.getPlayer().getCapability(ModCapability.BLOW_CAPABILITY);
//            if (oldSpeedCap.isPresent() && newSpeedCap.isPresent()) {
//                newSpeedCap.ifPresent((newCap) -> {
//                    oldSpeedCap.ifPresent((oldCap) -> {
//                        newCap.deserializeNBT(oldCap.serializeNBT());
//                    });
//                });
//            }
//        }
    }

    //实体更新 药水生效
    @SubscribeEvent
    public static void updatePlayer(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity && !entityLiving.world.isRemote){
            PlayerEntity player = (PlayerEntity) entityLiving;
            boolean criticalRate = player.getActivePotionEffect(EffectRegistry.CRITICAL_RATE.get()) != null;
            boolean criticalDamage = player.getActivePotionEffect(EffectRegistry.CRITICAL_DAMAGE.get()) != null;
            boolean defense = player.getActivePotionEffect(EffectRegistry.DEFENSE.get()) != null;
            boolean attack = player.getActivePotionEffect(EffectRegistry.ATTACK.get()) != null;
            //确定某一个玩家
            String key = player.getGameProfile().getName()+":"+player.world.isRemote;
            LazyOptional<IBlowCapability> capability = player.getCapability(ModCapability.BLOW_CAPABILITY);
            if (!capability.isPresent()) return; //能力为空
            //暴击率
            if (!playerCriticalRate.contains(key)){ //没有时
                if (criticalRate){
                    int amplifier = EventHelper.setBuffLevel(player, EffectRegistry.CRITICAL_RATE.get());
                    capability.ifPresent(e -> e.setCriticalRate(attrCriticalRate * amplifier)); //暴击率增加
                    playerCriticalRate.add(key); //有效果add
                }
            }else if (!criticalRate){ //效果结束时清楚增益
                int level = EventHelper.getBuffLevel(player, EffectRegistry.CRITICAL_RATE.get());
                capability.ifPresent(e -> e.setCriticalRate(-attrCriticalRate * level));
                playerCriticalRate.remove(key);
            }
            //暴击伤害
            if (!playerCriticalDamage.contains(key)){
                if (criticalDamage){
                    int amplifier = EventHelper.setBuffLevel(player, EffectRegistry.CRITICAL_DAMAGE.get());
                    capability.ifPresent(e -> e.setCriticalDamage(attrCriticalDamage * amplifier));
                    playerCriticalDamage.add(key);
                }
            }else if (!criticalDamage){
                int level = EventHelper.getBuffLevel(player, EffectRegistry.CRITICAL_DAMAGE.get());
                capability.ifPresent(e -> e.setCriticalDamage(-attrCriticalDamage * level));
                playerCriticalDamage.remove(key);
            }
            //防御
            if (!playerDefense.contains(key)){
                if (defense){
                    EventHelper.upAttribute(player, Attributes.ARMOR, EffectRegistry.DEFENSE.get(), attrDefense);
                    playerDefense.add(key);
                }
            }else if (!defense){
                EventHelper.downAttribute(player, Attributes.ARMOR, EffectRegistry.DEFENSE.get(), attrDefense);
                playerDefense.remove(key);
            }
            //攻击力
            if (!playerAttack.contains(key)){
                if (attack){
                    EventHelper.upAttribute(player, Attributes.ATTACK_DAMAGE, EffectRegistry.ATTACK.get(), attrAttack);
                    playerAttack.add(key);
                }
            }else if (!attack){
                EventHelper.downAttribute(player, Attributes.ATTACK_DAMAGE, EffectRegistry.ATTACK.get(), attrAttack);
                playerAttack.remove(key);
            }
        }
    }

    //实体受伤
    @SubscribeEvent
    public static void attackEntity(LivingHurtEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        Entity source = event.getSource().getTrueSource(); //攻击者
        if (source instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) source;
            DamageSource damageSource = event.getSource();
            if (damageSource == DamageSource.GENERIC){ //普通伤害
                boolean effect = player.getActivePotionEffect(EffectRegistry.ATTACK_PHYSICS.get()) != null;
                if (effect){
                    int amplifier = player.getActivePotionEffect(EffectRegistry.ATTACK_PHYSICS.get()).getAmplifier() + 1;
                    if (amplifier > 0){ //有buff
                        event.setAmount(event.getAmount() + amplifier * attrAttackPhysics);
                    }
                }
            }
            //暴击系统伤害计算 增益基础伤害（会受到护甲等减免）
            LazyOptional<IBlowCapability> critical = player.getCapability(ModCapability.BLOW_CAPABILITY);
            critical.ifPresent(e ->{
                float criticalRate = e.getCriticalRate();
                float criticalDamage = e.getCriticalDamage();
                float amount = event.getAmount();
                if (random.nextFloat() <= criticalRate){ //暴击了
                    event.setAmount(amount * (1 + criticalDamage));
                    World world = entityLiving.getEntityWorld();
                    for (int i = 0; i < 5; i++)
                        world.addParticle(ParticleTypes.HEART, entityLiving.getPosX() + random.nextDouble() / 2, entityLiving.getPosY() + random.nextDouble() / 2, entityLiving.getPosZ() + random.nextDouble() / 2,
                                random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
                }
            });
        }
    }

    //实体死亡时
    @SubscribeEvent
    public static void entityDeath(LivingDeathEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            //复活buff
            boolean revive = player.getActivePotionEffect(EffectRegistry.REVIVE.get()) != null;
            if (revive){
                int amplifier = player.getActivePotionEffect(EffectRegistry.REVIVE.get()).getAmplifier() + 1;
                if (amplifier > 0){
                    event.setCanceled(true);
                    if (player instanceof ServerPlayerEntity) {
                        ServerPlayerEntity serverplayerentity = (ServerPlayerEntity)player;
                        serverplayerentity.addStat(Stats.ITEM_USED.get(Items.TOTEM_OF_UNDYING));
                        CriteriaTriggers.USED_TOTEM.trigger(serverplayerentity, new ItemStack(Items.TOTEM_OF_UNDYING));
                    }
                    player.setHealth(2 * amplifier); //血量
                    player.clearActivePotions(); //清除buff
                    player.addPotionEffect(new EffectInstance(Effects.FIRE_RESISTANCE, (40 + 10 * amplifier) * 20, 0)); //防火
                    player.addPotionEffect(new EffectInstance(Effects.REGENERATION, (45 + 10 * amplifier) * 20, amplifier)); //生命恢复
                    player.addPotionEffect(new EffectInstance(Effects.ABSORPTION, (5 * amplifier) * 20, amplifier)); //伤害吸收
                    player.world.setEntityState(player, (byte) 35);
                }
            }
            //重置双爆属性
            LazyOptional<IBlowCapability> capability = player.getCapability(ModCapability.BLOW_CAPABILITY);
            capability.ifPresent(e -> e.resetCritical());
        }
    }

    //玩家登入
    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        //重置双爆属性
        PlayerEntity player = event.getPlayer();
        LazyOptional<IBlowCapability> capability = player.getCapability(ModCapability.BLOW_CAPABILITY);
        capability.ifPresent(e -> e.resetCritical());
        //重置玩家属性 防止属性叠加
        EventHelper.downAttribute(player, Attributes.ARMOR, EffectRegistry.DEFENSE.get(), attrDefense);
        EventHelper.downAttribute(player, Attributes.ATTACK_DAMAGE, EffectRegistry.ATTACK.get(), attrAttack);
        //发送消息
        player.sendMessage(new TranslationTextComponent("paimeng.message.login")
                .setStyle(Style.EMPTY.setHoverEvent(HoverEvent.Action.SHOW_TEXT.deserialize(new TranslationTextComponent("paimeng.message.login0")))
                        .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/21854371"))), UUID.randomUUID());
    }

    //玩家攻击时 物理伤害提升
    @SubscribeEvent
    public static void playerAttack(LivingAttackEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;

        }
    }
    /*
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onLivingDeath(LivingDeathEvent event) {

        if(PotionRevival.instance != null && event.entityLiving.isPotionActive(PotionRevival.instance)) {

            int level = event.entityLiving.getActivePotionEffect(PotionRevival.instance).getAmplifier() + 1;

            event.setCanceled(true);

            event.entityLiving.setHealth(PotionRevival.reviveHealth*level);
            event.entityLiving.worldObj.playSoundAtEntity(event.entityLiving, "random.levelup", 1.0F, 0.6F);

            PacketBuffer out = new PacketBuffer(Unpooled.buffer());

            out.writeInt(PacketHandlerClient.REVIVAL_HEARTS);
            out.writeInt(event.entityLiving.getEntityId());

            SToCMessage packet = new SToCMessage(out);
            PotionCore.networkWrapper.sendToAllAround(packet, new TargetPoint(event.entityLiving.worldObj.provider.getDimensionId(), event.entityLiving.posX, event.entityLiving.posY, event.entityLiving.posZ, 16));

            event.entityLiving.removePotionEffect(PotionRevival.instance.getId());
        }
    }*/
}

