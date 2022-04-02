package com.yuo.PaiMeng.Items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import com.yuo.PaiMeng.Capability.IBlowCapability;
import com.yuo.PaiMeng.Capability.ModCapability;
import com.yuo.PaiMeng.Capability.RelicsItemHandler;
import com.yuo.PaiMeng.Event.EventHandler;
import com.yuo.PaiMeng.PaiMeng;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.util.LazyOptional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

//圣遗物操作类
public class RelicsHelper {
    //数据保存字段
    public static final String NBT_RELICS = "nbt_relics";
    public static final String NBT_STAR = "nbt_star";
    public static final String NBT_TYPE = "nbt_type";
    public static final String NBT_LEVEL = "nbt_level";
    public static final String NBT_EXP = "nbt_exp";
    public static final String NBT_ATTR  = "nbt_attr";
    public static final String NBT_ATTR1 = "nbt_attr1";
    public static final String NBT_ATTR2 = "nbt_attr2";
    public static final String NBT_ATTR3 = "nbt_attr3";
    public static final String NBT_ATTR4 = "nbt_attr4";
    public static final String NBT_ATTR_TYPE = "nbt_attr_type";
    public static final String NBT_ATTR_TYPE1 = "nbt_attr_type1";
    public static final String NBT_ATTR_TYPE2 = "nbt_attr_type2";
    public static final String NBT_ATTR_TYPE3 = "nbt_attr_type3";
    public static final String NBT_ATTR_TYPE4 = "nbt_attr_type4";
    //副属性初始值和增长值
    public static final float[] criticalRateFive = {0.027f, 0.031f, 0.035f, 0.039f};
    public static final float[] criticalRateFour = {0.022f, 0.025f, 0.028f, 0.031f};
    public static final float[] criticalRateThree = {0.017f, 0.019f, 0.021f, 0.023f};
    public static final float[] criticalRateTwo = {0.012f, 0.013f, 0.014f, 0.015f};

    public static final float[] criticalDamageFive = {0.054f, 0.062f, 0.07f, 0.078f};
    public static final float[] criticalDamageFour = {0.044f, 0.05f, 0.056f, 0.062f};
    public static final float[] criticalDamageThree = {0.034f, 0.038f, 0.042f, 0.046f};
    public static final float[] criticalDamageTwo = {0.024f, 0.026f, 0.028f, 0.03f};

    public static final float[] attackDamageRateFive = {0.041f, 0.047f, 0.053f, 0.058f};
    public static final float[] attackDamageRateFour = {0.033f, 0.037f, 0.042f, 0.047f};
    public static final float[] attackDamageRateThree = {0.025f, 0.028f, 0.032f, 0.036f};
    public static final float[] attackDamageRateTwo = {0.017f, 0.020f, 0.023f, 0.026f};

    public static final float[] attackDamageFive = {1.4f, 1.6f, 1.8f, 1.9f};
    public static final float[] attackDamageFour = {1.1f, 1.2f, 1.4f, 1.6f};
    public static final float[] attackDamageThree = {0.8f, 1.0f, 1.1f, 1.3f};
    public static final float[] attackDamageTwo = {0.5f, 0.7f, 0.8f, 1.0f};

    public static final float[] defenseRateFive = {0.051f, 0.058f, 0.066f, 0.073f};
    public static final float[] defenseRateFour = {0.041f, 0.047f, 0.053f, 0.058f};
    public static final float[] defenseRateThree = {0.031f, 0.036f, 0.041f, 0.046f};
    public static final float[] defenseRateTwo = {0.021f, 0.025f, 0.029f, 0.031f};

    public static final float[] defenseFive = {1.6f, 1.9f, 2.1f, 2.3f};
    public static final float[] defenseFour = {1.3f, 1.5f, 1.7f, 1.9f};
    public static final float[] defenseThree = {1.0f, 1.2f, 1.4f, 1.4f};
    public static final float[] defenseTwo = {0.7f, 0.8f, 1.0f, 1.1f};

    public static final float[] healthRateFive = {0.041f, 0.047f, 0.053f, 0.058f};
    public static final float[] healthRateFour = {0.033f, 0.037f, 0.042f, 0.047f};
    public static final float[] healthRateThree = {0.025f, 0.028f, 0.032f, 0.036f};
    public static final float[] healthRateTwo = {0.017f, 0.020f, 0.023f, 0.026f};

    public static final float[] healthFive = {2.09f, 2.39f, 2.69f, 2.99f};
    public static final float[] healthFour = {1.67f, 1.91f, 2.15f, 2.39f};
    public static final float[] healthThree = {1.23f, 1.4f, 1.63f, 1.85f};
    public static final float[] healthTwo = {0.99f, 1.12f, 1.31f, 1.56f};

    //主属性
    public static final float[] mainHealthBase = {1.29f, 2.58f, 4.3f, 6.45f, 7.17f};
    public static final float[] mainHealthMAx = {3.24f, 8.43f, 18.93f, 35.71f, 47.8f};
    public static final float[] mainHealthAttackRateBase = {0.031f, 0.042f, 0.052f, 0.063f, 0.07f};
    public static final float[] mainHealthAttackRateMAx = {0.079f, 0.137f, 0.231f, 0.348f, 0.466f};

    public static final float[] mainAttackDamageBase = {0.8f, 1.7f, 2.8f, 4.2f, 4.7f};
    public static final float[] mainAttackDamageMAx = {2.1f, 5.5f, 12.3f, 23.2f, 31.1f};

    public static final float[] mainDefensePhysicsRateBase = {0.039f, 0.052f, 0.066f, 0.079f, 0.087f};
    public static final float[] mainDefensePhysicsRateMAx = {0.099f, 0.171f, 0.288f, 0.435f, 0.583f};

    public static final float[] mainCriticalRateBase = {0.021f, 0.028f, 0.035f, 0.042f, 0.047f};
    public static final float[] mainCriticalRateMAx = {0.053f, 0.091f, 0.154f, 0.232f, 0.311f};

    public static final float[] mainCriticalDamageBase = {0.042f, 0.056f, 0.07f, 0.084f, 0.093f};
    public static final float[] mainCriticalDamageMAx = {0.105f, 0.183f, 0.308f, 0.464f, 0.622f};

    public static final float[] mainHealRateBase = {0.024f, 0.032f, 0.04f, 0.048f, 0.054f};
    public static final float[] mainHealRateMAx = {0.061f, 0.105f, 0.178f, 0.268f, 0.359f};

    public static Random random = new Random();

    /**
     * 判断物品是否含有等级
     * @param stack 物品
     * @return 结果
     */
    public static boolean hasLevelNbt(ItemStack stack){
        CompoundNBT compoundNBT =(CompoundNBT) stack.getOrCreateTag().get(NBT_RELICS);
        if (compoundNBT == null) return false;
        return compoundNBT.contains(NBT_LEVEL);
    }

    /**是否有主属性？
     * @param stack 物品
     * @return 结果
     */
    public static boolean hasMainAttrNbt(ItemStack stack){
        CompoundNBT compoundNBT =(CompoundNBT) stack.getOrCreateTag().get(NBT_RELICS);
        if (compoundNBT == null) return false;
        return compoundNBT.getFloat(NBT_ATTR) > 0 && compoundNBT.getInt(NBT_ATTR_TYPE) > 0;
    }

    /**
     * 判断是否有副属性
     * @param stack 物品
     * @return 结果
     */
    public static boolean hasViceAttr(ItemStack stack){
        CompoundNBT compoundNBT =(CompoundNBT) stack.getOrCreateTag().get(NBT_RELICS);
        if (compoundNBT == null) return false;
        float attr1 = compoundNBT.getFloat(NBT_ATTR1);
        float attr2 = compoundNBT.getFloat(NBT_ATTR2);
        float attr3 = compoundNBT.getFloat(NBT_ATTR3);
        float attr4 = compoundNBT.getFloat(NBT_ATTR4);
        int attrType1 = compoundNBT.getInt(NBT_ATTR_TYPE1);
        int attrType2 = compoundNBT.getInt(NBT_ATTR_TYPE2);
        int attrType3 = compoundNBT.getInt(NBT_ATTR_TYPE3);
        int attrType4 = compoundNBT.getInt(NBT_ATTR_TYPE4);
        return (attr1 > 0 && attrType1 > 0) || (attr2 > 0 && attrType2 > 0) || (attr3 > 0 && attrType3 > 0) || (attr4 > 0 && attrType4 > 0);
    }

    /**
     * 获取物品等级
     * @param stack 物品
     * @return 等级
     */
    public static int getRelicsLevel(ItemStack stack){
        CompoundNBT compoundNBT = (CompoundNBT) stack.getOrCreateTag().get(NBT_RELICS);
        if (compoundNBT != null && compoundNBT.contains(NBT_LEVEL))
            return compoundNBT.getInt(NBT_LEVEL);
        return 0;
    }

    /**
     * 已字符形式返回物品属性值 保留一位小数
     * @param value 属性类
     * @return 转化的字符值
     */
    public static String getMathTwo(RandomAttrValue value){
        BigDecimal bd;
        if (value.attrType.isRate()){ //是百分比表示
            bd = new BigDecimal(value.baseValue * 100).setScale(1, RoundingMode.HALF_UP);
            return bd + "%";
        }else return new BigDecimal(value.baseValue).setScale(1, RoundingMode.HALF_UP).toString();
    }

    /**
     * 根据属性类别 星级 等级 计算当前主属性数值
     * @param star 星级
     * @param level 等级
     * @param type 属性分类
     * @return 结果
     */
    public static float getMainAttrUp(int star, int level, RelicsAttrType type){
        float base = 0;
        switch (type){
            case HEALTH:
                break;
            case HEALTH_RATE:
                break;
            case DEFENSE:
                break;
            case DEFENSE_RATE:
                break;
            case ATTACK_DAMAGE:
                break;
            case ATTACK_DAMAGE_RATE:
                break;
            case CRITICAL_RATE:
                break;
            case CRITICAL_DAMAGE:
                break;
            case HEAL:
                break;
            case ATTACK_PHYSICS:
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        if (star == 1) return base * (0.15f + 0.85f * (level / 15f));
        return base * (0.15f + 0.85f * (level / 20f));
    }

    /**
     * 获取物品星级数据
     * @param stack 物品
     * @return 星级
     */
    public static int getStarFormStack(ItemStack stack){
        return stack.getOrCreateTag().getCompound(NBT_RELICS).getInt(NBT_STAR);
    }

    /**
     * 获取圣遗物部位
     * @param stack 物品
     * @return 部位
     */
    public static RelicsType getTypeForStack(ItemStack stack){
        int id = stack.getOrCreateTag().getCompound(NBT_RELICS).getInt(NBT_TYPE);
        return RelicsType.getTypeForId(id);
    }

    /**
     * 获取主属性
     * @param stack 物品
     * @return 属性
     */
    public static RandomAttrValue getMainAttrTypeFormStack(ItemStack stack){
        CompoundNBT nbt = (CompoundNBT) stack.getOrCreateTag().get(NBT_RELICS);
        if (hasMainAttrNbt(stack) && nbt != null){
            return new RandomAttrValue(nbt.getFloat(NBT_ATTR), RelicsAttrType.getRelicsAttrTypeFormId(nbt.getInt(NBT_ATTR_TYPE)));
        }
        return new RandomAttrValue(0, RelicsAttrType.EMPTY);
    }

    /**
     * 获取副属性
     * @param stack 物品
     * @return 属性列表
     */
    public static List<RandomAttrValue> getViceAttrTypeFormStack(ItemStack stack){
        CompoundNBT nbt = (CompoundNBT) stack.getOrCreateTag().get(NBT_RELICS);
        List<RandomAttrValue> list = new ArrayList<>();
        if (hasViceAttr(stack) && nbt != null){
            if (nbt.contains(NBT_ATTR_TYPE1)){
                list.add(new RandomAttrValue(nbt.getFloat(NBT_ATTR1), RelicsAttrType.getRelicsAttrTypeFormId(nbt.getInt(NBT_ATTR_TYPE1))));
            }
            if (nbt.contains(NBT_ATTR_TYPE2)){
                list.add(new RandomAttrValue(nbt.getFloat(NBT_ATTR2), RelicsAttrType.getRelicsAttrTypeFormId(nbt.getInt(NBT_ATTR_TYPE2))));
            }
            if (nbt.contains(NBT_ATTR_TYPE3)){
                list.add(new RandomAttrValue(nbt.getFloat(NBT_ATTR3), RelicsAttrType.getRelicsAttrTypeFormId(nbt.getInt(NBT_ATTR_TYPE3))));
            }
            if (nbt.contains(NBT_ATTR_TYPE4)){
                list.add(new RandomAttrValue(nbt.getFloat(NBT_ATTR4), RelicsAttrType.getRelicsAttrTypeFormId(nbt.getInt(NBT_ATTR_TYPE4))));
            }
        }
        return list;
    }

    /**
     * 为物品初始化属性
     * @param crafting 物品
     */
    public static void addRelicsBaseAttr(ItemStack crafting) {
        Relics relics = (Relics) crafting.getItem();
        CompoundNBT orCreateTag = crafting.getOrCreateTag();
        CompoundNBT nbt = new CompoundNBT();
        int star = MathHelper.nextInt(random, relics.getMinStar(), relics.getMaxStar());
        nbt.putInt(NBT_STAR, star);
        relics.setStar(star);
        int type = random.nextInt(5);
        nbt.putInt(NBT_TYPE, type);
        relics.setType(RelicsType.getTypeForId(type));
        setMainAttr(nbt, star, RelicsType.getTypeForId(type));
        nbt.putInt(NBT_LEVEL, 0);
        nbt.putInt(NBT_EXP, 0);
        if (star > 1)
            setViceAttr(nbt, nbt.copy(), star);
        orCreateTag.put(NBT_RELICS, nbt);
    }

    /**
     * 根据装备部位 生成圣遗物初始主属性
     * @param nbt 数据
     * @param star 星级
     * @param type 部位
     */
    private static void setMainAttr(CompoundNBT nbt, int star, RelicsType type){
        float baseValue;
        RelicsAttrType attrType;
        switch (type){
            case HEAD:
                RandomAttrValue attr = setRandomMainAttr(RelicsAttrType.ALL_HEAD, star);
                baseValue = attr.baseValue;
                attrType = attr.attrType;
                break;
            case CUP:
                RandomAttrValue attr1 = setRandomMainAttr(RelicsAttrType.ALL_CUP, star);
                baseValue = attr1.baseValue;
                attrType = attr1.attrType;
                break;
            case CLOCK:
                RandomAttrValue attr2 = setRandomMainAttr(RelicsAttrType.ALL_CLOCK, star);
                baseValue = attr2.baseValue;
                attrType = attr2.attrType;
                break;
            case FEATHER:
                attrType = RelicsAttrType.ATTACK_DAMAGE;
                baseValue = mainAttackDamageBase[star -1];
                break;
            case FLOWER:
                attrType = RelicsAttrType.HEALTH;
                baseValue = mainHealthBase[star - 1];
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + type);
        }
        nbt.putFloat(NBT_ATTR, baseValue);
        nbt.putInt(NBT_ATTR_TYPE, attrType.getId());
    }

    /**
     * 为其他部位设置随机主属性
     * @param attrTypes 可能属性组
     * @param star 星级
     */
    private static RandomAttrValue setRandomMainAttr(RelicsAttrType[] attrTypes, int star){
        RelicsAttrType attrType = attrTypes[random.nextInt(attrTypes.length)];
        float baseValue;
        switch (attrType.getId()){
            case 2: //攻击和生命值%
            case 6:
                baseValue = mainHealthAttackRateBase[star - 1];
                break;
            case 4: //防御和物理伤害%
            case 10:
                baseValue = mainDefensePhysicsRateBase[star - 1];
                break;
            case 7:
                baseValue = mainCriticalRateBase[star - 1];
                break;
            case 8:
                baseValue = mainCriticalDamageBase[star - 1];
                break;
            case 9:
                baseValue = mainHealRateBase[star - 1];
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + attrType.getId());
        }
        return new RandomAttrValue(baseValue, attrType);
    }

    /**
     * 根据星级生成副属性
     * @param nbt 数据
     * @param star 星级
     */
    private static void setViceAttr(CompoundNBT nbt, CompoundNBT mainNbt, int star){
        List<RandomAttrValue> list = getRandomViceAttrForStar(mainNbt, star);
        switch (list.size()) {
            case 0: return;
            case 1:
                nbt.putFloat(NBT_ATTR1, list.get(0).baseValue);
                nbt.putInt(NBT_ATTR_TYPE1, list.get(0).attrType.getId());
                break;
            case 2:
                nbt.putFloat(NBT_ATTR1, list.get(0).baseValue);
                nbt.putInt(NBT_ATTR_TYPE1, list.get(0).attrType.getId());
                nbt.putFloat(NBT_ATTR2, list.get(1).baseValue);
                nbt.putInt(NBT_ATTR_TYPE2, list.get(1).attrType.getId());
                break;
            case 3:
                nbt.putFloat(NBT_ATTR1, list.get(0).baseValue);
                nbt.putInt(NBT_ATTR_TYPE1, list.get(0).attrType.getId());
                nbt.putFloat(NBT_ATTR2, list.get(1).baseValue);
                nbt.putInt(NBT_ATTR_TYPE2, list.get(1).attrType.getId());
                nbt.putFloat(NBT_ATTR3, list.get(2).baseValue);
                nbt.putInt(NBT_ATTR_TYPE3, list.get(2).attrType.getId());
                break;
            case 4:
                nbt.putFloat(NBT_ATTR1, list.get(0).baseValue);
                nbt.putInt(NBT_ATTR_TYPE1, list.get(0).attrType.getId());
                nbt.putFloat(NBT_ATTR2, list.get(1).baseValue);
                nbt.putInt(NBT_ATTR_TYPE2, list.get(1).attrType.getId());
                nbt.putFloat(NBT_ATTR3, list.get(2).baseValue);
                nbt.putInt(NBT_ATTR_TYPE3, list.get(2).attrType.getId());
                nbt.putFloat(NBT_ATTR4, list.get(3).baseValue);
                nbt.putInt(NBT_ATTR_TYPE4, list.get(3).attrType.getId());
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + list.size());
        }
    }

    /**
     * 生成副属性
     * @param star 星级
     * @return 属性
     */
    private static List<RandomAttrValue> getRandomViceAttrForStar(CompoundNBT mainNbt, int star){
        List<RandomAttrValue> list = new ArrayList<>();
        if (random.nextDouble() < 0.8d) { // 副属性不满
            if (star <= 2) return list;
            int i = star - 2;
            while (i != 0){
                RandomAttrValue viceAttr = spawnViceAttr(mainNbt, star);
                if (isRepeat(list, viceAttr)){
                    i--;
                    list.add(viceAttr);
                }
            }
        }else {
            int i = star - 1;
            if (star <= 1) return list;
            while (i != 0){
                RandomAttrValue viceAttr = spawnViceAttr(mainNbt, star);
                if (isRepeat(list, viceAttr)){
                    i--;
                    list.add(viceAttr);
                }
            }
        }
        return list;
    }

    /**
     * 属性是否与类别属性冲突
     * @param list 属性leibv
     * @param viceAttr 属性
     * @return 结果
     */
    private static boolean isRepeat(List<RandomAttrValue> list, RandomAttrValue viceAttr){
        int j = 0;
        for (RandomAttrValue attrValue : list) {
            if (attrValue.attrType.isEquals(viceAttr.attrType)) j++;
        }
        return !(j > 0);
    }

    /**
     * 随机生成一个副属性
     * @param mainNbt 主属性nbt
     * @param star 星级
     * @return 副属性
     */
    private static RandomAttrValue spawnViceAttr(CompoundNBT mainNbt, int star){
        RandomAttrValue attrValue = new RandomAttrValue(0, RelicsAttrType.EMPTY);
        int mainTypeId = mainNbt.getInt(NBT_ATTR_TYPE); //主属性类型
        RelicsAttrType mainType = RelicsAttrType.getRelicsAttrTypeFormId(mainTypeId);
        if (mainType != null){
            RelicsAttrType viceAttrType = getViceAttrType(mainType);
            float[] baseValues = getViceAttrBaseValue(viceAttrType, star);
            return new RandomAttrValue(baseValues[random.nextInt(4)], viceAttrType);
        }
        return attrValue;
    }

    /**
     * 获取一个不和主属性冲突的副属性类型
     * @param mainType 主属性类型
     * @return 副属性类型
     */
    private static RelicsAttrType getViceAttrType(RelicsAttrType mainType){
        RelicsAttrType attrType;
        int i = 0;
        while (i < 100){
            attrType = RelicsAttrType.ALL_VICE[random.nextInt(RelicsAttrType.ALL_VICE.length)];
            if (!mainType.isEquals(attrType)) return attrType;
            i++;
        }
        return RelicsAttrType.EMPTY;
    }

    /**
     * 通过属性类型 和星级 获取一个副属性基础值
     * @param attrType 属性类型
     * @param star 星级
     * @return 属性值组
     */
    private static float[] getViceAttrBaseValue(RelicsAttrType attrType, int star){
        float[] values;
        switch (attrType){
            case HEALTH:
                switch (star){
                    case 2:
                        values = healthTwo;
                        break;
                    case 3:
                        values = healthThree;
                        break;
                    case 4:
                        values = healthFour;
                        break;
                    case 5:
                        values = healthFive;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + star);
                }
                break;
            case HEALTH_RATE:
                switch (star){
                    case 2:
                        values = healthRateTwo;
                        break;
                    case 3:
                        values = healthRateThree;
                        break;
                    case 4:
                        values = healthRateFour;
                        break;
                    case 5:
                        values = healthRateFive;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + star);
                }
                break;
            case DEFENSE:
                switch (star){
                    case 2:
                        values = defenseTwo;
                        break;
                    case 3:
                        values = defenseThree;
                        break;
                    case 4:
                        values = defenseFour;
                        break;
                    case 5:
                        values = defenseFive;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + star);
                }
                break;
            case DEFENSE_RATE:
                switch (star){
                    case 2:
                        values = defenseRateTwo;
                        break;
                    case 3:
                        values = defenseRateThree;
                        break;
                    case 4:
                        values = defenseRateFour;
                        break;
                    case 5:
                        values = defenseRateFive;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + star);
                }
                break;
            case ATTACK_DAMAGE:
                switch (star){
                    case 2:
                        values = attackDamageTwo;
                        break;
                    case 3:
                        values = attackDamageThree;
                        break;
                    case 4:
                        values = attackDamageFour;
                        break;
                    case 5:
                        values = attackDamageFive;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + star);
                }
                break;
            case ATTACK_DAMAGE_RATE:
                switch (star){
                    case 2:
                        values = attackDamageRateTwo;
                        break;
                    case 3:
                        values = attackDamageRateThree;
                        break;
                    case 4:
                        values = attackDamageRateFour;
                        break;
                    case 5:
                        values = attackDamageRateFive;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + star);
                }
                break;
            case CRITICAL_RATE:
                switch (star){
                    case 2:
                        values = criticalRateTwo;
                        break;
                    case 3:
                        values = criticalRateThree;
                        break;
                    case 4:
                        values = criticalRateFour;
                        break;
                    case 5:
                        values = criticalRateFive;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + star);
                }
                break;
            case CRITICAL_DAMAGE:
                switch (star){
                    case 2:
                        values = criticalDamageTwo;
                        break;
                    case 3:
                        values = criticalDamageThree;
                        break;
                    case 4:
                        values = criticalDamageFour;
                        break;
                    case 5:
                        values = criticalDamageFive;
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + star);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + attrType);
        }
        return values;
    }

    /**
     * 获取玩家所有圣遗物属性之和
     * @param player 玩家
     */
    public static List<RandomAttrValue> getRelicsData(PlayerEntity player){
        LazyOptional<RelicsItemHandler> capability = player.getCapability(ModCapability.RELICS_CAPABILITY);
        List<RandomAttrValue> list = new ArrayList<>();
        list.add(new RandomAttrValue(0, RelicsAttrType.EMPTY));
        if (capability.isPresent()){
            RelicsItemHandler itemHandler = capability.orElseThrow(NullPointerException::new);
            for (ItemStack stack : itemHandler.getStacks()) {
                if (stack.getItem() instanceof Relics){
                    RandomAttrValue mainAttr = getMainAttrTypeFormStack(stack);
                    List<RandomAttrValue> viceAttr = getViceAttrTypeFormStack(stack);
                    viceAttr.add(mainAttr);
                    addList(list, viceAttr);
                }
            }
        }
        return list;
    }

    /**
     * 比较2个列表 并将2中与1相同属性的值添加到1上 如1中无2中属性，则添加到1列表中
     * @param main 主列表
     * @param vice 副列表
     */
    private static void addList(List<RandomAttrValue> main, List<RandomAttrValue> vice){
        for (RandomAttrValue attrValue : main) {
            Iterator<RandomAttrValue> iterator = vice.iterator();
            while (iterator.hasNext()){
                RandomAttrValue value = iterator.next();
                if (attrValue.attrType.isEquals(value.attrType)){ //副列表中属性与主列表相同
                    attrValue.baseValue += value.baseValue;
                    iterator.remove(); //删除已匹配值
                }
            }
        }
        if (vice.size() > 0){ //将副列表中未匹配的属性添加到主列表中
            main.addAll(vice);
        }
    }

    //原版增益属性 用于属性修饰符
    private static final RelicsAttrType[] ATTRS = new RelicsAttrType[]{RelicsAttrType.HEALTH, RelicsAttrType.HEALTH_RATE, RelicsAttrType.ATTACK_DAMAGE_RATE,
            RelicsAttrType.ATTACK_DAMAGE, RelicsAttrType.DEFENSE, RelicsAttrType.DEFENSE_RATE};

    /**
     * 获取玩家圣遗物所有生效属性修饰符
     * @param player 玩家
     * @return 修饰符集合
     */
    public static Multimap<Attribute, AttributeModifier> getRelicsModifier(PlayerEntity player){
        List<RandomAttrValue> relicsData = getRelicsData(player);
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        for (RandomAttrValue data : relicsData) {
            for (RelicsAttrType attr : ATTRS) {
                if (attr.isEquals(data.attrType) && attr.isAttr()){
                    builder.put(attr.getAttribute(), new AttributeModifier(attr.getUuid(),
                            PaiMeng.MOD_ID + attr.getTip(), data.baseValue, attr.getOperation()));
                }
            }
        }
        return builder.build();
    }

    //圣遗物暴击系统加层 数据保存字段
    private final static String RELICS_CRITICAL_RATE = PaiMeng.MOD_ID + ":relics_critical_rate";
    private final static String RELICS_CRITICAL_DAMAGE = PaiMeng.MOD_ID + ":relics_critical_damage";

    /**
     * 更新圣遗物带来的暴击系统增益
     * @param player 玩家
     * @param blowCapability 暴击系统
     */
    public static void upCritical(PlayerEntity player, IBlowCapability blowCapability, String key){
        List<RandomAttrValue> relicsData = getRelicsData(player);
        if (relicsData.size() <= 1) return;
        //添加新的加层
        for (RandomAttrValue value : relicsData) {
            if (value.attrType.isEquals(RelicsAttrType.CRITICAL_RATE)){
                blowCapability.setCriticalRate(value.baseValue);
                player.getPersistentData().putDouble(RELICS_CRITICAL_RATE, value.baseValue);
                if (!EventHandler.playerCritical.contains(key))
                    EventHandler.playerCritical.add(key);
            }
            if (value.attrType.isEquals(RelicsAttrType.CRITICAL_DAMAGE)){
                blowCapability.setCriticalDamage(value.baseValue);
                player.getPersistentData().putDouble(RELICS_CRITICAL_DAMAGE, value.baseValue);
                if (!EventHandler.playerCritical.contains(key))
                    EventHandler.playerCritical.add(key);
            }
        }
    }

    /**
     * 减少暴击系统
     * @param player 玩家
     * @param blowCapability 暴击系统
     */
    public static void downCritical(PlayerEntity player, IBlowCapability blowCapability){
        CompoundNBT persistentData = player.getPersistentData();
        if (persistentData.contains(RELICS_CRITICAL_RATE)){
            double criticalRate = persistentData.getDouble(RELICS_CRITICAL_RATE);
            //减去上次加层
            if (criticalRate > 0){
                blowCapability.setCriticalRate(-criticalRate);
                persistentData.remove(RELICS_CRITICAL_RATE);
            }
        }
        if (persistentData.contains(RELICS_CRITICAL_DAMAGE)){
            double criticalDamage = persistentData.getDouble(RELICS_CRITICAL_DAMAGE);
            if (criticalDamage > 0){
                blowCapability.setCriticalDamage(-criticalDamage);
                persistentData.remove(RELICS_CRITICAL_DAMAGE);
            }
        }
    }

    /**
     * 获取圣遗物治疗加层
     * @param player 玩家
     * @return 值 无则返回0
     */
    public static double getRelicsHeal(PlayerEntity player){
        for (RandomAttrValue value : getRelicsData(player)) {
            if (value.attrType.isEquals(RelicsAttrType.HEAL)) return value.baseValue;
        }
        return 0;
    }

    public static double getRelicsAttackPhysics(PlayerEntity player){
        for (RandomAttrValue value : getRelicsData(player)) {
            if (value.attrType.isEquals(RelicsAttrType.ATTACK_PHYSICS)) return value.baseValue;
        }
        return 0;
    }

    /**
     * 清除圣遗物添加的属性修饰符
     * @param player 玩家
     */
    public static void clearModifier(PlayerEntity player){
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        for (RelicsAttrType attrType : RelicsAttrType.ALL_MODIFIER) {
            builder.put(attrType.getAttribute(), new AttributeModifier(attrType.getUuid(),
                    PaiMeng.MOD_ID + attrType.getTip(), 0, attrType.getOperation()));
        }
        player.getAttributeManager().removeModifiers(builder.build());
    }

    /**
     * 属性工具类
     */
    public static class RandomAttrValue {
        private float baseValue;
        private final RelicsAttrType attrType;
        public RandomAttrValue(float vualue, RelicsAttrType type){
            this.attrType = type;
            this.baseValue = vualue;
        }

        public float getBaseValue() {
            return baseValue;
        }

        public RelicsAttrType getAttrType() {
            return attrType;
        }

        public boolean isEmpty(){
            return this.baseValue == 0 || this.attrType == null || this.attrType.isEquals(RelicsAttrType.EMPTY);
        }
    }

}
