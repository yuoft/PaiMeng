package com.yuo.PaiMeng.Event;

import com.google.common.collect.Multimap;
import com.yuo.PaiMeng.Capability.*;
import com.yuo.PaiMeng.ClientProxy;
import com.yuo.PaiMeng.Effects.EffectRegistry;
import com.yuo.PaiMeng.Effects.ReviveEffect;
import com.yuo.PaiMeng.Gui.RelicsButton;
import com.yuo.PaiMeng.Items.Food.PaiMengFood;
import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsBox;
import com.yuo.PaiMeng.Items.RelicsHelper;
import com.yuo.PaiMeng.NetWork.NetWorkHandler;
import com.yuo.PaiMeng.NetWork.RelicsGuiPacket;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.util.DamageSource;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;

/**
 * ???????????????
 */
@Mod.EventBusSubscriber(modid = PaiMeng.MOD_ID)
public class EventHandler {
    private final static Random random = new Random();
    public static List<String> playerCritical = new ArrayList<>();
    public static List<String> playerCriticalRate = new ArrayList<>();
    public static List<String> playerCriticalDamage = new ArrayList<>();

    public static final double attrCriticalRate = 0.12; //???????????????????????? ?????????
    public static final double attrCriticalDamage = 0.18; //????????????
    public static final double attrDefense = 2.5; //?????????
    public static final double attrAttackDamage = 2.0; //?????????
    public static final double attrAttackPhysics = 2.5; //????????????

    //???????????????????????????
    private static final List<Item> ITEMS = Arrays.asList(Items.ENCHANTED_GOLDEN_APPLE, Items.APPLE, Items.SWEET_BERRIES);

    //??????????????????????????????????????????????????????????????????
    @SubscribeEvent
    public static void playerScreen(GuiScreenEvent.InitGuiEvent.Post event){
        Screen gui = event.getGui();
        if (gui instanceof InventoryScreen){
            InventoryScreen screen = (InventoryScreen) gui;
            if (event.getWidgetList() != null)
                event.addWidget(new RelicsButton(screen,77,44, 16, 16));
        }
    }

    //?????????gui??????
    @SubscribeEvent
    public static void playerTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            if (ClientProxy.KEY_OPEN_RELICS.isPressed() && Minecraft.getInstance().isGameFocused()) {
                NetWorkHandler.INSTANCE.sendToServer(new RelicsGuiPacket(true));
            }
        }
    }

    /**
     * ????????????????????????????????????
     * @param stack ??????????????????
     * @return ???:true
     */
    private static boolean isExceptionItem(ItemStack stack){
        for (Item item : ITEMS) {
            if (stack.getItem() == item) return true;
        }
        return false;
    }

    //??????????????????
    @SubscribeEvent
    public static void eatFood(LivingEntityUseItemEvent.Start event){
        ItemStack item = event.getItem();
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (item.isFood() && EventHelper.getNameSpace(item).equals("minecraft") && !isExceptionItem(item)){//????????????
                    player.sendStatusMessage(new TranslationTextComponent("paimeng.message.food"), true);
                    event.setCanceled(true);
            }
        }
    }

    //???????????????
    @SubscribeEvent
    public static void eatFoodEnd(LivingEntityUseItemEvent.Finish event){
        ItemStack item = event.getItem();
        LivingEntity entityLiving = event.getEntityLiving();
        if (entityLiving instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) entityLiving;
            if (item.isFood() && item.getItem() instanceof PaiMengFood && !isExceptionItem(item)){
                PaiMengFood food = (PaiMengFood) item.getItem();
                int type = food.getTYPE();
                if (type == 0){
                    player.heal(food.getLEVEL() * 2);
                }
            }
        }
    }
    //??????????????????
    @SubscribeEvent
    public static void itemMessage(ItemTooltipEvent event){
        ItemStack stack = event.getItemStack();
        if (stack.isFood() && EventHelper.getNameSpace(stack).equals("minecraft") && !isExceptionItem(stack)){ //????????????
            List<ITextComponent> toolTip = event.getToolTip();
            toolTip.add(new TranslationTextComponent("paimeng.text.itemInfo.food"));
        }
    }

    //?????????????????????
    @SubscribeEvent
    public static void onAttachCapabilityEvent(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(PaiMeng.MOD_ID, "blow"), new BlowCapabilityProvider());
            event.addCapability(new ResourceLocation(PaiMeng.MOD_ID, "relics"), new RelicsItemCapabilityProvider());
        }
    }

    //????????????
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

    //???????????? ????????????
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

    //????????????
    @SubscribeEvent
    public static void attackEntity(LivingHurtEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        Entity source = event.getSource().getTrueSource(); //?????????
        if (source instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) source;
            DamageSource damageSource = event.getSource();
            if (damageSource == DamageSource.GENERIC){ //???????????? ??????????????????
                int attackPhysics = EventHelper.getEffectLevel(player, EffectRegistry.ATTACK_PHYSICS.get());
                if (attackPhysics > 0){ //???buff
                    event.setAmount((float) (event.getAmount() + attackPhysics * attrAttackPhysics));
                }
                float amount = event.getAmount(); //??????????????????
                event.setAmount((float) (amount + amount * RelicsHelper.getRelicsAttackPhysics(player)));
            }
            //???????????????????????? ????????????????????????????????????????????????
            LazyOptional<IBlowCapability> critical = player.getCapability(ModCapability.BLOW_CAPABILITY);
            critical.ifPresent(e ->{
                double criticalRate = e.getCriticalRate();
                double criticalDamage = e.getCriticalDamage();
                float amount = event.getAmount();
                if (random.nextFloat() <= criticalRate){ //?????????
                    event.setAmount((float) (amount * (1 + criticalDamage)));
                    World world = entityLiving.getEntityWorld();
                    for (int i = 0; i < 10; i++) //????????????
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
            //??????buff
            int revive = EventHelper.getEffectLevel(player, EffectRegistry.REVIVE.get());
            if (revive > 0){
                event.setCanceled(true);
                ReviveEffect.revive(player,revive);
            }
            //??????????????????
            LazyOptional<IBlowCapability> capability = player.getCapability(ModCapability.BLOW_CAPABILITY);
            capability.ifPresent(IBlowCapability::resetCritical);
        }
    }

    @SubscribeEvent
    public static void playerLogin(PlayerEvent.PlayerLoggedInEvent event){
        PlayerEntity player = event.getPlayer();
        boolean isRemote = player.world.isRemote;
        String key = player.getGameProfile().getName()+":"+ isRemote;
        //?????????????????????key ?????????????????? ??????????????????
        if (!playerCriticalRate.contains(key) && !isRemote){
            LazyOptional<IBlowCapability> capability = player.getCapability(ModCapability.BLOW_CAPABILITY);
            capability.ifPresent(IBlowCapability::resetCritical);
        }
        //????????????
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
        if (state.getBlock() == Blocks.SPRUCE_LEAVES){ //????????????????????????
            ItemStack mainhand = player.getHeldItemMainhand();
            int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.FORTUNE, mainhand);
            if (random.nextInt(100) < level * 5 + 5){
                ItemEntity item = new ItemEntity((World) world, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ItemRegistry.songguo.get(),
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
            float amount = event.getAmount(); //??????????????????
            event.setAmount((float) (amount + amount * RelicsHelper.getRelicsHeal(player)));
        }
    }

    @SubscribeEvent
    public static void wearRelics(PlayerInteractEvent.RightClickItem event){
        PlayerEntity player = event.getPlayer();
        ItemStack stack = event.getItemStack();
        if (stack.getItem() instanceof Relics){ //?????????????????????
            LazyOptional<RelicsItemHandler> capability = player.getCapability(ModCapability.RELICS_CAPABILITY);
            capability.ifPresent(e ->{
                NonNullList<ItemStack> stacks = e.getStacks();
                EventHelper.wearRelics(player, event.getWorld(), RelicsHelper.getTypeForStack(stack).getId(), stacks, stack);
            });
        }
    }

    @SubscribeEvent
    public static void dropRelicsBox(LivingDropsEvent event){
        LivingEntity entityLiving = event.getEntityLiving();
        Entity source = event.getSource().getTrueSource();
        if (source instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) source; //????????????????????????????????????????????????
            int luck = EventHelper.getEffectLevel(player, Effects.LUCK);
            int unLuck = EventHelper.getEffectLevel(player, Effects.UNLUCK);
            int looting = event.getLootingLevel();
            if (entityLiving instanceof EnderDragonEntity || entityLiving instanceof WitherEntity){ //???boss
                ItemStack stack = new ItemStack(ItemRegistry.relicsBoxOne.get(), (random.nextInt(2) + 1 + (luck - unLuck)) * (looting + 1));
                ((RelicsBox) stack.getItem()).setLevel(1);
                EventHelper.addEntityDrops(event, stack, entityLiving);
                ItemStack stack1 = new ItemStack(ItemRegistry.relicsBoxTwo.get(), (random.nextInt(3) + 1 + (luck - unLuck)) * (looting + 1));
                ((RelicsBox) stack1.getItem()).setLevel(2);
                EventHelper.addEntityDrops(event, stack1, entityLiving);
                ItemStack stack2 = new ItemStack(ItemRegistry.relicsBoxThree.get(), (random.nextInt(4) + 1 + (luck - unLuck)) * (looting + 1));
                ((RelicsBox) stack2.getItem()).setLevel(3);
                EventHelper.addEntityDrops(event, stack2, entityLiving);
            }else {
                //0.01%?????????????????????
                if (random.nextDouble() < (0.0001 + 0.0002 * (luck - unLuck) + 0.0003 * looting)){
                    ItemStack stack = new ItemStack(ItemRegistry.relicsBoxOne.get(), (1 + (luck - unLuck)) * (looting + 1));
                    ((RelicsBox) stack.getItem()).setLevel(1);
                    EventHelper.addEntityDrops(event, stack, entityLiving);
                }
                //0.1%?????????????????????
                if (random.nextDouble() < (0.001 + 0.002 * (luck - unLuck) + 0.003 * looting)){
                    ItemStack stack = new ItemStack(ItemRegistry.relicsBoxTwo.get(), (random.nextInt(2) + (luck - unLuck)) * (looting + 1));
                    ((RelicsBox) stack.getItem()).setLevel(2);
                    EventHelper.addEntityDrops(event, stack, entityLiving);
                }
                //1%?????????????????????
                if (random.nextDouble() < (0.01 + 0.02 * (luck - unLuck) + 0.03 * looting)){
                    ItemStack stack = new ItemStack(ItemRegistry.relicsBoxThree.get(), (random.nextInt(3) + (luck - unLuck)) * (looting + 1));
                    ((RelicsBox) stack.getItem()).setLevel(3);
                    EventHelper.addEntityDrops(event, stack, entityLiving);
                }
            }
        }
    }

    //???????????????
//    @SubscribeEvent
    public static void lootTableAdd(LootTableLoadEvent event){
        ResourceLocation name = event.getName();
        for (ResourceLocation r : EventHelper.RS){
            if (name.equals(r)){
                LootTable table = event.getTable();
                table.addPool(EventHelper.getPool(EventHelper.songguoEntry));
                event.setTable(table);
            }
        }
    }

}

