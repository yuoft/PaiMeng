package com.yuo.paimeng.event;

import com.yuo.paimeng.capability.IBlowCapability;
import com.yuo.paimeng.capability.ModCapability;
import com.yuo.paimeng.effects.EffectRegistry;
import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.items.RelicsHelper;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.ApplyExplosionDecay;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.BonusLevelTableCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.UUID;

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
     * 为玩家添加正面属性修饰器
     * @param player 玩家
     * @param attr 属性枚举
     * @param buffLevel buff等级
     * @param name buff保存名称
     * @param instance 属性
     * @param keys 已添加玩家列表
     * @param key 当前玩家信息
     */
    public static void upAttribute(Player player, ATTR_TYPE attr, int buffLevel, String name, AttributeInstance instance,
                                   List<String> keys, String key, float attrValue){
        int lv = player.getPersistentData().getInt("paimeng:"+ name +"_level");
        if (lv != buffLevel && instance != null){ //buff等级变更
            instance.addPermanentModifier(EventHelper.getModifier(attr, -lv * attrValue));
            player.getPersistentData().putInt("paimeng:"+ name +"_level", buffLevel);
//            player.getAttributeManager().reapplyModifiers(); //重新应用修饰符
            keys.remove(key);
        }
        if (!keys.contains(key) && instance != null){ //未添加属性
            instance.addPermanentModifier(EventHelper.getModifier(attr, buffLevel * attrValue)); //添加修饰器
            player.getPersistentData().putInt("paimeng:"+ name +"_level", buffLevel); //将数据保存到玩家身上
            keys.add(key); //将此玩家标记为已更改
        }
    }

    /**
     * 为玩家添加正面属性修饰器
     * @param player 玩家
     * @param attr 属性枚举
     * @param name buff保存名称
     * @param instance 属性
     * @param keys 已添加玩家列表
     * @param key 当前玩家信息
     */
    public static void downAttribute(Player player, ATTR_TYPE attr, String name, AttributeInstance instance,
                                     List<String> keys, String key, float attrValue){
        if (keys.contains(key) && instance != null){ //已添加属性
            int level = player.getPersistentData().getInt("paimeng:"+ name +"_level");
            instance.addPermanentModifier(EventHelper.getModifier(attr, -level * attrValue));
            keys.remove(key);
        }
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
                cap.ifPresent(e -> e.setCriticalDamage(EventHandler.attrCriticalDamage * criticalDamage)); //暴击率增加
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

    //获取属性修饰器
    //1.20.1: AttributeModifier 需要稳定 UUID（旧版用名字作为标识），否则每次都会新建修饰器
    public static AttributeModifier getModifier(ATTR_TYPE type, float value){
        return new AttributeModifier(type.getUuid(), type.getName(), value, type.getType());
    }

    public enum ATTR_TYPE{
        HEALTH("generic.maxHealth", "155feba1-312e-43f8-9d00-509f2c5586fd", AttributeModifier.Operation.ADDITION),
        KNOCK_BACK("generic.knockbackResistance", "951ded27-55ba-4c38-91f9-5c70254bb16d",AttributeModifier.Operation.ADDITION),
        MOVE_SPEED("generic.movementSpeed", "90c16c30-64b5-47a8-be9a-7debf0a026b5",AttributeModifier.Operation.ADDITION),
        ATTACK_DAMAGE("generic.attackDamage", "b7449603-3692-46d6-9848-02e2cf735cf0",AttributeModifier.Operation.ADDITION),
        ATTACK_SPEED("generic.attackSpeed", "ffc4f036-c284-4405-bed8-7ac7662cf93e",AttributeModifier.Operation.ADDITION),
        REACH_DISTANCE("forge.reach_distance", "f1cb6a25-cf4b-478b-8099-b1cab83492e3", AttributeModifier.Operation.ADDITION),
        ARMOR("generic.armor", "21e94267-8e6d-421a-b573-712fd18c5784",AttributeModifier.Operation.ADDITION);

        private final String name;
        private final UUID uuid;
        private final AttributeModifier.Operation type;
        ATTR_TYPE(String attrName, String uuid, AttributeModifier.Operation operation){
            this.name = attrName;
            this.uuid = UUID.fromString(uuid);
            this.type = operation;
        }

        public String getName() {
            return name;
        }

        public AttributeModifier.Operation getType() {
            return type;
        }

        public UUID getUuid() {
            return uuid;
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
    public static void addEntityDrops(LivingDropsEvent event, ItemStack stack, LivingEntity livingEntity){
        event.getDrops().add(new ItemEntity(livingEntity.level(), livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), stack ));
    }

    public static final ResourceLocation[] RS = { //将要追加的战利品表 列表
            new ResourceLocation("blocks/spruce_leaves"), //云杉树叶
            new ResourceLocation("entities/fox"), //云杉树叶
            new ResourceLocation("entities/parrot"), //云杉树叶
            new ResourceLocation("entities/wolf"), //云杉树叶
          };

    /**
     * 获取一个战利品奖池
     * @return 含有5个项的奖池
     */
    public static LootPool getPool(LootPoolEntryContainer.Builder<?> entry){
        LootPool.Builder builder = LootPool.lootPool().add(entry).add(entry).add(entry)
                .setBonusRolls(UniformGenerator.between(0.0F, 2.0F));
        return builder.build();
    }

    //兽肉
    public static LootPoolSingletonContainer.Builder<?> shourouEntry = LootItem.lootTableItem(PMItems.shourou.get())
            .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
            .apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 3.0F)));
    //松果
    public static LootPoolSingletonContainer.Builder<?> songguoEntry = LootItem.lootTableItem(PMItems.songguo.get())
            .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
            .apply(ApplyExplosionDecay.explosionDecay())
            .when(BonusLevelTableCondition.bonusLevelFlatChance(Enchantments.BLOCK_FORTUNE, 0.05f, 0.073f, 0.105f, 0.125f, 0.15f));

}
