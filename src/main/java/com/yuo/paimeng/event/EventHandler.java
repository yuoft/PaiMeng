package com.yuo.paimeng.event;

import com.cazsius.solcarrot.tracking.FoodList;
import com.google.common.collect.Multimap;
import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.PaiMeng;
import com.yuo.paimeng.capability.*;
import com.yuo.paimeng.effects.EffectRegistry;
import com.yuo.paimeng.effects.ReviveEffect;
import com.yuo.paimeng.items.Food.PaiMengFood;
import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.items.Relics;
import com.yuo.paimeng.items.RelicsBox;
import com.yuo.paimeng.items.RelicsHelper;
import com.yuo.paimeng.tiles.SevenGodTile;
import com.yuo.paimeng.world.data.SevenGodSavedData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

/**
 * 事件处理类
 */
@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID)
public class EventHandler {
    private final static Random random = new Random();
    public static List<String> playerCritical = new ArrayList<>();
    public static List<String> playerCriticalRate = new ArrayList<>();
    public static List<String> playerCriticalDamage = new ArrayList<>();

    public static final double attrCriticalRate = 0.12; //属性变更基础系数 暴击率
    public static final double attrCriticalDamage = 0.18; //暴击伤害
    public static final double attrDefense = 2.5; //防御力
    public static final double attrAttackDamage = 2.0; //攻击力
    public static final double attrAttackPhysics = 2.5; //物理攻击

    //不被禁用的原版食物
    private static final List<Item> ITEMS = Arrays.asList(Items.ENCHANTED_GOLDEN_APPLE, Items.GOLDEN_APPLE, Items.SWEET_BERRIES, Items.APPLE);

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END && event.player instanceof ServerPlayer player) {
            if (player.tickCount % 100 == 0) {
                // 从全局数据获取该玩家拥有的所有神像
                SevenGodSavedData data = SevenGodSavedData.get(player.serverLevel());
                List<SevenGodSavedData.ShrineData> shrines = data.getPlayerShrines(player.getUUID());

                for (SevenGodSavedData.ShrineData shrine : shrines) {
                    for (Map.Entry<Item, Integer> entry : shrine.donatedItems.entrySet()) {
                        int amount = entry.getValue();
                        MobEffect effect = SevenGodSavedData.getEffectForMaterial(entry.getKey());

                        if (effect != null) {
                            // 🔴 使用 getMaxLevel 限制最大等级
                            int maxLevel = SevenGodSavedData.getMaxLevel(effect);
                            int level = Math.min(amount / 64, maxLevel);

                            if (level > 0) {
                                player.addEffect(new MobEffectInstance(effect, 120, level - 1, false, true));
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * 判断食物是否在例外列表中
     * @param stack 要判断的食物
     * @return 是:true
     */
    private static boolean isExceptionItem(ItemStack stack){
        for (Item item : ITEMS) {
            if (stack.getItem() == item) return true;
        }
        return false;
    }

    //禁用原版食物
    @SubscribeEvent
    public static void eatFood(LivingEntityUseItemEvent.Start event){
        ItemStack item = event.getItem();
        LivingEntity entityLiving = event.getEntity();
        if (entityLiving instanceof Player player){
            if (item.isEdible() && EventHelper.getNameSpace(item).equals("minecraft") && !isExceptionItem(item)){//原版食物
                player.displayClientMessage(Component.translatable("paimeng.message.food"), true);
                FoodList.get(player).addFood(item.getItem());
                event.setCanceled(true);
            }
        }
    }

    //吃完食物后
    @SubscribeEvent()
    public static void eatFoodEnd(LivingEntityUseItemEvent.Finish event){
        ItemStack item = event.getItem();
        LivingEntity entityLiving = event.getEntity();
        if (entityLiving instanceof Player player){
            if (item.isEdible() && item.getItem() instanceof PaiMengFood food){
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
        if (stack.isEdible() && EventHelper.getNameSpace(stack).equals("minecraft") && !isExceptionItem(stack)){ //原版食物
            List<Component> toolTip = event.getToolTip();
            toolTip.add(Component.translatable("paimeng.text.itemInfo.food"));
        }
    }

    //为实体附加能力
    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player) {
            event.addCapability(ModUtils.fa("blow"), new BlowCapabilityProvider());
            event.addCapability(ModUtils.fa("relics"), new RelicsItemCapabilityProvider());
        }
    }

    //能力复制
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        //1.20.1: event.getPlayer() -> event.getEntity()
        event.getOriginal().getCapability(ModCapability.BLOW_CAPABILITY).ifPresent(old -> {
            CompoundTag bags = old.serializeNBT();
            event.getEntity().getCapability(ModCapability.BLOW_CAPABILITY).ifPresent(c -> c.deserializeNBT(bags));
        });
        event.getOriginal().getCapability(ModCapability.RELICS_CAPABILITY).ifPresent(old -> {
            CompoundTag bags = old.serializeNBT();
            event.getEntity().getCapability(ModCapability.RELICS_CAPABILITY).ifPresent(c -> c.deserializeNBT(bags));
        });
    }

    //实体更新 药水生效
    @SubscribeEvent
    public static void updatePlayer(LivingEvent.LivingTickEvent event) {
        LivingEntity entityLiving = event.getEntity();
        if (entityLiving instanceof Player player && !player.level().isClientSide){
            LazyOptional<RelicsItemHandler> capability = player.getCapability(ModCapability.RELICS_CAPABILITY);
            capability.ifPresent(e -> {
                RelicsHelper.clearModifier(player);
                Multimap<Attribute, AttributeModifier> relicsModifier = RelicsHelper.getRelicsModifier(player);
                //1.20.1: Entity#getAttributeManager() 已删除，逐属性重新应用永久修饰器
                relicsModifier.forEach((attribute, modifier) -> {
                    AttributeInstance instance = player.getAttribute(attribute);
                    if (instance != null && !instance.hasModifier(modifier)) {
                        instance.addPermanentModifier(modifier);
                    }
                });
            });
            EventHelper.changeAttribute(player);
        }
    }

    //实体受伤
    @SubscribeEvent
    public static void attackEntity(LivingHurtEvent event){
        LivingEntity entityLiving = event.getEntity();
        Entity source = event.getSource().getEntity(); //攻击者
        if (source instanceof Player player){
            DamageSource damageSource = event.getSource();
            if (damageSource.is(DamageTypes.GENERIC)){ //普通伤害 物理伤害提升
                int attackPhysics = EventHelper.getEffectLevel(player, EffectRegistry.ATTACK_PHYSICS.get());
                if (attackPhysics > 0){ //有buff
                    event.setAmount((float) (event.getAmount() + attackPhysics * attrAttackPhysics));
                }
                float amount = event.getAmount(); //物理伤害加成
                event.setAmount((float) (amount + amount * RelicsHelper.getRelicsAttackPhysics(player)));
            }
            //暴击系统伤害计算 增益基础伤害（会受到护甲等减免）
            LazyOptional<IBlowCapability> critical = player.getCapability(ModCapability.BLOW_CAPABILITY);
            critical.ifPresent(e ->{
                double criticalRate = e.getCriticalRate();
                double criticalDamage = e.getCriticalDamage();
                float amount = event.getAmount();
                if (random.nextFloat() <= criticalRate){ //暴击了
                    event.setAmount((float) (amount * (1 + criticalDamage)));
                    //暴击粒子 1.20.1: 服务端用 ServerLevel#sendParticles
                    if (entityLiving.level() instanceof ServerLevel serverLevel){
                        for (int i = 0; i < 10; i++)
                            serverLevel.sendParticles(ParticleTypes.CRIT,
                                    entityLiving.getX() + random.nextDouble() / 2,
                                    entityLiving.getY() + random.nextDouble() / 2,
                                    entityLiving.getZ() + random.nextDouble() / 2,
                                    1, random.nextGaussian(), random.nextGaussian(), random.nextGaussian(), 0.0D);
                    }
                }
            });
        }
    }

    @SubscribeEvent
    public static void entityDeath(LivingDeathEvent event){
        LivingEntity entityLiving = event.getEntity();
        if (entityLiving instanceof Player player){
            BlockPos pos = player.blockPosition();
            Level world = player.level();
            if (!world.isClientSide){
                //1.20.1: BlockPos.getAllInBox(AABB) 已删除，改用 BlockPos.betweenClosed
                for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-16, -8, -16), pos.offset(16, 8, 16))) {
                    BlockEntity tile = world.getBlockEntity(blockPos);
                    if (tile instanceof SevenGodTile){
                        ReviveEffect.revive(player, 2, false);
                        event.setCanceled(true);
                    }
                }
            }
            //复活buff
            int revive = EventHelper.getEffectLevel(player, EffectRegistry.REVIVE.get());
            if (revive >= 0){
                ReviveEffect.revive(player, revive + 1, true);
                event.setCanceled(true);
            }
            //重置双爆属性
            LazyOptional<IBlowCapability> capability = player.getCapability(ModCapability.BLOW_CAPABILITY);
            capability.ifPresent(IBlowCapability::resetCritical);
        }
    }

    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        Player player = event.getEntity();
        boolean isRemote = player.level().isClientSide;
        String key = player.getGameProfile().getName()+":"+ isRemote;
        //重启游戏时添加key 重置双爆属性 防止属性叠加
        if (!playerCriticalRate.contains(key) && !isRemote){
            LazyOptional<IBlowCapability> capability = player.getCapability(ModCapability.BLOW_CAPABILITY);
            capability.ifPresent(IBlowCapability::resetCritical);
        }
        if (!player.getPersistentData().getBoolean("paimeng:login")){
            player.getPersistentData().putBoolean("paimeng:login", true);
            //发送消息
            player.sendSystemMessage(Component.translatable("paimeng.message.login")
                    .setStyle(Style.EMPTY.withHoverEvent(HoverEvent.Action.SHOW_TEXT.deserializeFromLegacy(Component.translatable("paimeng.message.login0")))
                            .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/21854371"))));
        }
    }

    @SubscribeEvent
    public static void playerLoginOut(PlayerEvent.PlayerLoggedOutEvent event){
        Player player = event.getEntity();
        boolean isRemote = player.level().isClientSide;
        String key = player.getGameProfile().getName()+":"+ isRemote;
        if (!isRemote)
            playerCritical.remove(key);
    }

    @SubscribeEvent
    public static void blockDrop(BlockEvent.BreakEvent event){
        BlockPos pos = event.getPos();
        Player player = event.getPlayer();
        if (!(event.getLevel() instanceof Level world)) return;
        BlockState state = world.getBlockState(pos);
        if (state.getBlock() == Blocks.SPRUCE_LEAVES){ //云杉树叶掉落松果
            ItemStack mainhand = player.getMainHandItem();
            int level = mainhand.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
            if (random.nextInt(100) < level * 5 + 5){
                ItemEntity item = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(PMItems.songguo.get(),
                        random.nextInt(2 + level)));
                world.addFreshEntity(item);
            }
        }
    }

    @SubscribeEvent
    public static void heal(LivingHealEvent event){
        LivingEntity entityLiving = event.getEntity();
        if (entityLiving instanceof Player player){
            float amount = event.getAmount(); //增加血量回复
            event.setAmount((float) (amount + amount * RelicsHelper.getRelicsHeal(player)));
        }
    }

    @SubscribeEvent
    public static void wearRelics(PlayerInteractEvent.RightClickItem event){
        Player player = event.getEntity();
        ItemStack relics = event.getItemStack();
        if (relics.getItem() instanceof Relics){ //快速装备圣遗物
            if (player.isShiftKeyDown()){
                // 为没有属性的圣遗物初始化属性
                if (!RelicsHelper.hasMainAttrNbt(relics)){
                    RelicsHelper.addRelicsBaseAttr0(relics);
                }
            }else {
                LazyOptional<RelicsItemHandler> capability = player.getCapability(ModCapability.RELICS_CAPABILITY);
                capability.ifPresent(e ->{
                    NonNullList<ItemStack> stacks = e.getStacks();
                    EventHelper.wearRelics(player, event.getHand() , event.getLevel(), RelicsHelper.getTypeForStack(relics).getId(), stacks, relics);
                });
            }
        }
    }

    // 统一处理所有生物的额外掉落（包含圣物匣、兽肉、原石等）
    @SubscribeEvent
    public static void livingDrops(LivingDropsEvent event){
        LivingEntity entityLiving = event.getEntity();
        Level world = entityLiving.level();
        BlockPos pos = entityLiving.blockPosition();
        Entity source = event.getSource().getEntity();

        // 伤害必须来源于玩家
        if (!(source instanceof Player player)) return;

        // 获取玩家属性
        ItemStack stack = player.getMainHandItem();
        int looting = stack.getEnchantmentLevel(Enchantments.MOB_LOOTING); // 抢夺等级
        int luck = EventHelper.getEffectLevel(player, MobEffects.LUCK);
        int unLuck = EventHelper.getEffectLevel(player, MobEffects.UNLUCK);

        // ================= 1. 圣物匣掉落逻辑 =================
        if (entityLiving instanceof EnderDragon || entityLiving instanceof WitherBoss){ // 是 boss
            ItemStack box1 = new ItemStack(PMItems.relicsBoxOne.get(), (random.nextInt(2) + 1 + (luck - unLuck)) * (looting + 1));
            ((RelicsBox) box1.getItem()).setLevel(1);
            EventHelper.addEntityDrops(event, box1, entityLiving);

            ItemStack box2 = new ItemStack(PMItems.relicsBoxTwo.get(), (random.nextInt(3) + 1 + (luck - unLuck)) * (looting + 1));
            ((RelicsBox) box2.getItem()).setLevel(2);
            EventHelper.addEntityDrops(event, box2, entityLiving);

            ItemStack box3 = new ItemStack(PMItems.relicsBoxThree.get(), (random.nextInt(4) + 1 + (luck - unLuck)) * (looting + 1));
            ((RelicsBox) box3.getItem()).setLevel(3);
            EventHelper.addEntityDrops(event, box3, entityLiving);
        } else {
            // 0.01% 掉落一等圣物匣
            if (random.nextDouble() < (0.0001 + 0.0002 * (luck - unLuck) + 0.0003 * looting)){
                ItemStack box = new ItemStack(PMItems.relicsBoxOne.get(), (1 + (luck - unLuck)) * (looting + 1));
                ((RelicsBox) box.getItem()).setLevel(1);
                EventHelper.addEntityDrops(event, box, entityLiving);
            }
            // 0.1% 掉落二等圣物匣
            if (random.nextDouble() < (0.001 + 0.002 * (luck - unLuck) + 0.003 * looting)){
                ItemStack box = new ItemStack(PMItems.relicsBoxTwo.get(), (random.nextInt(2) + (luck - unLuck)) * (looting + 1));
                ((RelicsBox) box.getItem()).setLevel(2);
                EventHelper.addEntityDrops(event, box, entityLiving);
            }
            // 1% 掉落三等圣物匣
            if (random.nextDouble() < (0.01 + 0.02 * (luck - unLuck) + 0.03 * looting)){
                ItemStack box = new ItemStack(PMItems.relicsBoxThree.get(), (random.nextInt(3) + (luck - unLuck)) * (looting + 1));
                ((RelicsBox) box.getItem()).setLevel(3);
                EventHelper.addEntityDrops(event, box, entityLiving);
            }
        }

        // ================= 2. 特殊物品与原石掉落逻辑 =================
        if (entityLiving instanceof EnderDragon || entityLiving instanceof WitherBoss){ // 末影龙/凋零 额外掉落
            EventHelper.spawnDrops(PMItems.jiejing.get(), 1, world, pos, event);
            EventHelper.spawnDrops(PMItems.yuanshi.get(), random.nextInt(4) + looting, world, pos, event);
        } else {
            // 击杀特定动物掉落兽肉
            if (entityLiving instanceof Fox || entityLiving instanceof Wolf || entityLiving instanceof PolarBear
                    || entityLiving instanceof Panda || entityLiving instanceof Llama) {
                EventHelper.spawnDrops(PMItems.shourou.get(), random.nextInt(3) + looting, world, pos, event);
            }

            // 击杀鹦鹉掉落禽肉
            if (entityLiving instanceof Parrot) {
                // 🔴 注意：你之前的代码这里复制粘贴写成了 shourou.get()，如果是禽肉，请确认物品 ID 是否为 qinrou
                EventHelper.spawnDrops(PMItems.qinrou.get(), random.nextInt(3) + looting, world, pos, event);
            }

            // 所有非 boss 生物小概率掉落原石
            if (random.nextInt(100) > (94 - looting * 5)){
                EventHelper.spawnDrops(PMItems.yuanshi.get(), 1, world, pos, event);
            }
        }
    }

}
