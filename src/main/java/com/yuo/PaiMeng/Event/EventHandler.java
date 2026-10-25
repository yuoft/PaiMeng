package com.yuo.PaiMeng.Event;

import com.cazsius.solcarrot.tracking.FoodList;
import com.google.common.collect.Multimap;
import com.yuo.PaiMeng.Capability.*;
import com.yuo.PaiMeng.Effects.EffectRegistry;
import com.yuo.PaiMeng.Effects.ReviveEffect;
import com.yuo.PaiMeng.Items.Food.PaiMengFood;
import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsBox;
import com.yuo.PaiMeng.Items.RelicsHelper;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Tiles.SevenGodTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
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
                // TODO(port): 生活调味料(solcarrot)兼容未移植 —— 1.20.1 工程没有 solcarrot 依赖，
                //  原 sol(player, item) 逻辑（FoodList/MaxHealthHandler/CapabilityHandler）随之移除。
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
            event.addCapability(new ResourceLocation(PaiMeng.MOD_ID, "blow"), new BlowCapabilityProvider());
            event.addCapability(new ResourceLocation(PaiMeng.MOD_ID, "relics"), new RelicsItemCapabilityProvider());
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
        //发送消息
        player.sendSystemMessage(Component.translatable("paimeng.message.login")
                .setStyle(Style.EMPTY.withHoverEvent(HoverEvent.Action.SHOW_TEXT.deserializeFromLegacy(Component.translatable("paimeng.message.login0")))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/21854371"))));
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
            int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, mainhand);
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

    @SubscribeEvent
    public static void dropRelicsBox(LivingDropsEvent event){
        LivingEntity entityLiving = event.getEntity();
        Entity source = event.getSource().getEntity();
        if (source instanceof Player player){ //圣物匣掉落受幸运，不幸和抢夺影响
            int luck = EventHelper.getEffectLevel(player, MobEffects.LUCK);
            int unLuck = EventHelper.getEffectLevel(player, MobEffects.UNLUCK);
            int looting = event.getLootingLevel();
            if (entityLiving instanceof EnderDragon || entityLiving instanceof WitherBoss){ //是boss
                ItemStack stack = new ItemStack(PMItems.relicsBoxOne.get(), (random.nextInt(2) + 1 + (luck - unLuck)) * (looting + 1));
                ((RelicsBox) stack.getItem()).setLevel(1);
                EventHelper.addEntityDrops(event, stack, entityLiving);
                ItemStack stack1 = new ItemStack(PMItems.relicsBoxTwo.get(), (random.nextInt(3) + 1 + (luck - unLuck)) * (looting + 1));
                ((RelicsBox) stack1.getItem()).setLevel(2);
                EventHelper.addEntityDrops(event, stack1, entityLiving);
                ItemStack stack2 = new ItemStack(PMItems.relicsBoxThree.get(), (random.nextInt(4) + 1 + (luck - unLuck)) * (looting + 1));
                ((RelicsBox) stack2.getItem()).setLevel(3);
                EventHelper.addEntityDrops(event, stack2, entityLiving);
            }else {
                //0.01%掉落一等圣物匣
                if (random.nextDouble() < (0.0001 + 0.0002 * (luck - unLuck) + 0.0003 * looting)){
                    ItemStack stack = new ItemStack(PMItems.relicsBoxOne.get(), (1 + (luck - unLuck)) * (looting + 1));
                    ((RelicsBox) stack.getItem()).setLevel(1);
                    EventHelper.addEntityDrops(event, stack, entityLiving);
                }
                //0.1%掉落二等圣物匣
                if (random.nextDouble() < (0.001 + 0.002 * (luck - unLuck) + 0.003 * looting)){
                    ItemStack stack = new ItemStack(PMItems.relicsBoxTwo.get(), (random.nextInt(2) + (luck - unLuck)) * (looting + 1));
                    ((RelicsBox) stack.getItem()).setLevel(2);
                    EventHelper.addEntityDrops(event, stack, entityLiving);
                }
                //1%掉落三等圣物匣
                if (random.nextDouble() < (0.01 + 0.02 * (luck - unLuck) + 0.03 * looting)){
                    ItemStack stack = new ItemStack(PMItems.relicsBoxThree.get(), (random.nextInt(3) + (luck - unLuck)) * (looting + 1));
                    ((RelicsBox) stack.getItem()).setLevel(3);
                    EventHelper.addEntityDrops(event, stack, entityLiving);
                }
            }
        }
    }

    //原版生物额外掉落
    @SubscribeEvent
    public static void enderDragonDrops(LivingDropsEvent event){
        LivingEntity entityLiving = event.getEntity();
        Level world = entityLiving.level();
        BlockPos pos = entityLiving.blockPosition();
        Random random = new Random();
        Entity source = event.getSource().getEntity();
        if (!(source instanceof Player player)) return; //伤害来源于玩家
        ItemStack stack = player.getMainHandItem();
        int level = EnchantmentHelper.getItemEnchantmentLevel(Enchantments.MOB_LOOTING, stack); //抢夺
        if (entityLiving instanceof EnderDragon){ //末影龙额外掉落 创世结晶
            spawnDrops(PMItems.jiejing.get(), 1, world, pos, event);
            spawnDrops(PMItems.yuanshi.get(), random.nextInt(4) + level, world, pos, event);
        }
        if (entityLiving instanceof WitherBoss){ //凋零 创世结晶
            spawnDrops(PMItems.jiejing.get(), 1, world, pos, event);
            spawnDrops(PMItems.yuanshi.get(), random.nextInt(4) + level, world, pos, event);
        }
        //所有非boss生物掉落 原石
        if (!(entityLiving instanceof EnderDragon) && !(entityLiving instanceof WitherBoss)){
            int j = random.nextInt(100);
            if (j > (94 - level * 5)){
                spawnDrops(PMItems.yuanshi.get(), 1, world, pos, event);
            }
        }
    }

    /**
     * 添加额外掉落
     * @param item 需要掉落的物品
     * @param count 数量
     * @param world 世界
     * @param pos 坐标
     * @param event 事件
     */
    private static void spawnDrops(Item item, int count, Level world, BlockPos pos, LivingDropsEvent event){
        ItemStack stack1 = new ItemStack(item, count);
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack1);
        event.getDrops().add(itemEntity);
    }

}
