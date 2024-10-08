package com.yuo.PaiMeng.Event;

import com.cazsius.solcarrot.SOLCarrot;
import com.cazsius.solcarrot.SOLCarrotConfig;
import com.cazsius.solcarrot.tracking.CapabilityHandler;
import com.cazsius.solcarrot.tracking.FoodList;
import com.cazsius.solcarrot.tracking.MaxHealthHandler;
import com.cazsius.solcarrot.tracking.ProgressInfo;
import com.google.common.collect.Multimap;
import com.yuo.PaiMeng.Capability.*;
import com.yuo.PaiMeng.Effects.EffectRegistry;
import com.yuo.PaiMeng.Effects.ReviveEffect;
import com.yuo.PaiMeng.Client.Gui.RelicsButton;
import com.yuo.PaiMeng.Items.Food.PaiMengFood;
import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsBox;
import com.yuo.PaiMeng.Items.RelicsHelper;
import com.yuo.PaiMeng.PaiMeng;
import com.yuo.PaiMeng.Tiles.SevenGodTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.*;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
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

    //在玩家物品栏界面上添加切换到圣遗物界面的按钮
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void playerScreen(GuiScreenEvent.InitGuiEvent.Post event){
        Screen gui = event.getGui();
        if (gui instanceof InventoryScreen){
            InventoryScreen screen = (InventoryScreen) gui;
            if (event.getWidgetList() != null)
                event.addWidget(new RelicsButton(screen,77,44, 16, 16));
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

    private static void spawnParticles(ServerWorld world, PlayerEntity player, IParticleData type, int count) {
        // this overload sends a packet to the client
        world.addParticle(type, player.getPosX(), player.getPosY() + player.getEyeHeight(), player.getPosZ(), count,
                0.5F, 0.5F
        );
    }

    public static IFormattableTextComponent localizedComponent(String domain, String path, Object... args) {
        return new TranslationTextComponent(keyString(domain, path), args);
    }

    /** e.g. keyString("tooltip", "eaten_status.not_eaten_1") -> "tooltip.solcarrot.eatenStatus.not_eaten_1") */
    public static String keyString(String domain, String path) {
        return domain + "." + SOLCarrot.MOD_ID + "." + path;
    }

    private static void showChatMessage(PlayerEntity player, TextFormatting color, ITextComponent message) {
        ITextComponent component = localizedComponent("message", "chat_wrapper", message).mergeStyle(color);
        player.sendStatusMessage(component, false);
    }

    public static IFormattableTextComponent localizedQuantityComponent(String domain, String path, int number) {
        return number == 1
                ? new TranslationTextComponent(keyString(domain, path + ".singular"))
                : new TranslationTextComponent(keyString(domain, path + ".plural"), number);
    }

    /**
     * 生活调味料 胡萝卜版
     * @param player 玩家
     * @param stack  食物
     */
    private static void sol(PlayerEntity player, ItemStack stack){
        if (player.world.isRemote) return;
        ServerWorld world = (ServerWorld) player.world;
        ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
        boolean isInSurvival = !serverPlayer.isCreative() && !serverPlayer.isSpectator();
        if (SOLCarrotConfig.limitProgressionToSurvival() && !isInSurvival) return;

        Item usedItem = stack.getItem();
        if (!usedItem.isFood()) return;

        FoodList foodList = FoodList.get(player);
        boolean hasTriedNewFood = foodList.addFood(usedItem);

        // check this before syncing, because the sync entails an hp update
        boolean newMilestoneReached = MaxHealthHandler.updateFoodHPModifier(player);

        CapabilityHandler.syncFoodList(player);
        ProgressInfo progressInfo = foodList.getProgressInfo();

        if (newMilestoneReached) {
            if (SOLCarrotConfig.shouldPlayMilestoneSounds()) {
                // passing the player makes it not play for some reason
                world.playSound(
                        null,
                        player.getPosition(),
                        SoundEvents.ENTITY_PLAYER_LEVELUP, SoundCategory.PLAYERS,
                        1.0F, 1.0F
                );
            }

            if (SOLCarrotConfig.shouldSpawnMilestoneParticles()) {
                spawnParticles(world, player, ParticleTypes.HEART, 12);

                if (progressInfo.hasReachedMax()) {
                    spawnParticles(world, player, ParticleTypes.HAPPY_VILLAGER, 16);
                }
            }

            ITextComponent heartsDescription = localizedQuantityComponent("message", "hearts", SOLCarrotConfig.getHeartsPerMilestone());

            if (SOLCarrotConfig.shouldShowProgressAboveHotbar()) {
                String messageKey = progressInfo.hasReachedMax() ? "finished.hotbar" : "milestone_achieved";
                player.sendStatusMessage(localizedComponent("message", messageKey, heartsDescription), true);
            } else {
                showChatMessage(player, TextFormatting.DARK_AQUA, localizedComponent("message", "milestone_achieved", heartsDescription));
                if (progressInfo.hasReachedMax()) {
                    showChatMessage(player, TextFormatting.GOLD, localizedComponent("message", "finished.chat"));
                }
            }
        } else if (hasTriedNewFood) {
            if (SOLCarrotConfig.shouldSpawnIntermediateParticles()) {
                spawnParticles(world, player, ParticleTypes.END_ROD, 12);
            }
        }
    }

    //禁用原版食物
    @SubscribeEvent
    public static void eatFood(LivingEntityUseItemEvent.Start event){
        ItemStack item = event.getItem();
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (item.isFood() && EventHelper.getNameSpace(item).equals("minecraft") && !isExceptionItem(item)){//原版食物
                    player.sendStatusMessage(new TranslationTextComponent("paimeng.message.food"), true);
                if (ModList.get().isLoaded("solcarrot")){ //食物加血兼容
                    sol(player, item);
                }
                event.setCanceled(true);
            }
        }
    }

    //吃完食物后
    @SubscribeEvent()
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
    @OnlyIn(Dist.CLIENT)
    public static void itemMessage(ItemTooltipEvent event){
        ItemStack stack = event.getItemStack();
        if (stack.isFood() && EventHelper.getNameSpace(stack).equals("minecraft") && !isExceptionItem(stack)){ //原版食物
            List<ITextComponent> toolTip = event.getToolTip();
            toolTip.add(new TranslationTextComponent("paimeng.text.itemInfo.food"));
        }
    }

    //为实体附加能力
    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(PaiMeng.MOD_ID, "blow"), new BlowCapabilityProvider());
            event.addCapability(new ResourceLocation(PaiMeng.MOD_ID, "relics"), new RelicsItemCapabilityProvider());
        }
    }

    //能力复制
    @SubscribeEvent
    public static void onPlayerCloned(PlayerEvent.Clone event) {
        event.getOriginal().getCapability(ModCapability.BLOW_CAPABILITY).ifPresent(old -> {
            CompoundNBT bags = old.serializeNBT();
            event.getPlayer().getCapability(ModCapability.BLOW_CAPABILITY).ifPresent(c -> c.deserializeNBT(bags));
        });
        event.getOriginal().getCapability(ModCapability.RELICS_CAPABILITY).ifPresent(old -> {
            CompoundNBT bags = old.serializeNBT();
            event.getPlayer().getCapability(ModCapability.RELICS_CAPABILITY).ifPresent(c -> c.deserializeNBT(bags));
        });
    }

    //实体更新 药水生效
    @SubscribeEvent
    public static void updatePlayer(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity && !entityLiving.world.isRemote){
            PlayerEntity player = (PlayerEntity) entityLiving;
            LazyOptional<RelicsItemHandler> capability = player.getCapability(ModCapability.RELICS_CAPABILITY);
            capability.ifPresent(e -> {
                RelicsHelper.clearModifier(player);
                Multimap<Attribute, AttributeModifier> relicsModifier = RelicsHelper.getRelicsModifier(player);
                player.getAttributeManager().reapplyModifiers(relicsModifier);
            });
            EventHelper.changeAttribute(player);
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
            if (damageSource == DamageSource.GENERIC){ //普通伤害 物理伤害提升
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
                    World world = entityLiving.getEntityWorld();
                    for (int i = 0; i < 10; i++) //暴击粒子
                        world.addParticle(ParticleTypes.CRIT, entityLiving.getPosX() + random.nextDouble() / 2, entityLiving.getPosY() + random.nextDouble() / 2, entityLiving.getPosZ() + random.nextDouble() / 2,
                                random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
                }
            });
        }
    }

    @SubscribeEvent
    public static void entityDeath(LivingDeathEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            BlockPos pos = player.getPosition();
            AxisAlignedBB bb = new AxisAlignedBB(pos.add(-16, -8, -16), pos.add(16, 8, 16));
            World world = player.world;
            if (!world.isRemote){
                Iterator<BlockPos> iterator = BlockPos.getAllInBox(bb).iterator();
                while (iterator.hasNext()){
                    BlockPos blockPos = iterator.next();
                    TileEntity tile = world.getTileEntity(blockPos);
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
        PlayerEntity player = event.getPlayer();
        boolean isRemote = player.world.isRemote;
        String key = player.getGameProfile().getName()+":"+ isRemote;
        //重启游戏时添加key 重置双爆属性 防止属性叠加
        if (!playerCriticalRate.contains(key) && !isRemote){
            LazyOptional<IBlowCapability> capability = player.getCapability(ModCapability.BLOW_CAPABILITY);
            capability.ifPresent(IBlowCapability::resetCritical);
        }
        //发送消息
        player.sendMessage(new TranslationTextComponent("paimeng.message.login")
                .setStyle(Style.EMPTY.setHoverEvent(HoverEvent.Action.SHOW_TEXT.deserialize(new TranslationTextComponent("paimeng.message.login0")))
                        .setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://space.bilibili.com/21854371"))), UUID.randomUUID());
    }

    @SubscribeEvent
    public static void playerLoginOut(PlayerEvent.PlayerLoggedOutEvent event){
        PlayerEntity player = event.getPlayer();
        boolean isRemote = player.world.isRemote;
        String key = player.getGameProfile().getName()+":"+ isRemote;
        if (!isRemote)
            playerCritical.remove(key);
    }

    @SubscribeEvent
    public static void blockDrop(BlockEvent.BreakEvent event){
        BlockPos pos = event.getPos();
        IWorld world = event.getWorld();
        BlockState state = world.getBlockState(pos);
        PlayerEntity player = event.getPlayer();
        if (state.getBlock() == Blocks.SPRUCE_LEAVES){ //云杉树叶掉落松果
            ItemStack mainhand = player.getHeldItemMainhand();
            int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, mainhand);
            if (random.nextInt(100) < level * 5 + 5){
                ItemEntity item = new ItemEntity((World) world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(PMItems.songguo.get(),
                        random.nextInt(2 + level)));
                world.addEntity(item);
            }
        }
    }

    @SubscribeEvent
    public static void heal(LivingHealEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            float amount = event.getAmount(); //增加血量回复
            event.setAmount((float) (amount + amount * RelicsHelper.getRelicsHeal(player)));
        }
    }

    @SubscribeEvent
    public static void wearRelics(PlayerInteractEvent.RightClickItem event){
        PlayerEntity player = event.getPlayer();
        ItemStack relics = event.getItemStack();
        if (relics.getItem() instanceof Relics){ //快速装备圣遗物
            if (player.isSneaking()){
                // 为没有属性的圣遗物初始化属性
                if (!RelicsHelper.hasMainAttrNbt(relics)){
                    RelicsHelper.addRelicsBaseAttr0(relics);
                }
            }else {
                LazyOptional<RelicsItemHandler> capability = player.getCapability(ModCapability.RELICS_CAPABILITY);
                capability.ifPresent(e ->{
                    NonNullList<ItemStack> stacks = e.getStacks();
                    EventHelper.wearRelics(player, event.getHand() , event.getWorld(), RelicsHelper.getTypeForStack(relics).getId(), stacks, relics);
                });
            }
        }
    }

    @SubscribeEvent
    public static void dropRelicsBox(LivingDropsEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        Entity source = event.getSource().getTrueSource();
        if (source instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) source; //圣物匣掉落受幸运，不幸和抢夺影响
            int luck = EventHelper.getEffectLevel(player, Effects.LUCK);
            int unLuck = EventHelper.getEffectLevel(player, Effects.UNLUCK);
            int looting = event.getLootingLevel();
            if (entityLiving instanceof EnderDragonEntity || entityLiving instanceof WitherEntity){ //是boss
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
        LivingEntity entityLiving = event.getEntityLiving();
        World world = entityLiving.world;
        BlockPos pos = entityLiving.getPosition();
        Random random = new Random();
        Entity source = event.getSource().getTrueSource();
        if (!(source instanceof PlayerEntity)) return; //伤害来源于玩家
        ItemStack stack = ((PlayerEntity) source).getHeldItemMainhand();
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.LOOTING, stack); //抢夺
        if (entityLiving instanceof EnderDragonEntity){ //末影龙额外掉落 创世结晶
            spawnDrops(PMItems.jiejing.get(), 1, world, pos, event);
            spawnDrops(PMItems.yuanshi.get(), random.nextInt(4) + level, world, pos, event);
        }
        if (entityLiving instanceof WitherEntity){ //凋零 创世结晶
            spawnDrops(PMItems.jiejing.get(), 1, world, pos, event);
            spawnDrops(PMItems.yuanshi.get(), random.nextInt(4) + level, world, pos, event);
        }
        //所有非boss生物掉落 原石
        if (!(entityLiving instanceof EnderDragonEntity) && !(entityLiving instanceof WitherEntity)){
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
    private static void spawnDrops(Item item, int count, World world, BlockPos pos, LivingDropsEvent event){
        ItemStack stack1 = new ItemStack(item, count);
        ItemEntity itemEntity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack1);
        event.getDrops().add(itemEntity);
    }

}

