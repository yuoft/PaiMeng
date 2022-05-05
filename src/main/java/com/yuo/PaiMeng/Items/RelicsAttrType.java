package com.yuo.PaiMeng.Items;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;

import java.util.UUID;

//属性类型
public enum RelicsAttrType {
    EMPTY("empty", "空:",false, 0), //空
    HEALTH("health", "生命:",false, "8782da04-c2ad-4397-8ffc-563980d90276", Attributes.MAX_HEALTH, AttributeModifier.Operation.ADDITION, 1), //生命值
    HEALTH_RATE("healthRate", "生命:",true, "a9cb9720-5a61-4957-bfce-810c6fc29c40", Attributes.MAX_HEALTH, AttributeModifier.Operation.MULTIPLY_BASE, 2), //生命值
    DEFENSE("defense","防御:",false, "774c225c-9fde-49af-b06a-db86a15d80ba", Attributes.ARMOR, AttributeModifier.Operation.ADDITION, 3), //防御
    DEFENSE_RATE("defenseRate", "防御：",true,"770a2427-7bb9-41fc-a090-fc7c8147f2c0", Attributes.ARMOR, AttributeModifier.Operation.MULTIPLY_BASE, 4), //防御
    ATTACK_DAMAGE("attackDamage","攻击：", false,"34e05069-2fae-404a-979e-3fc912d7bd87", Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.ADDITION, 5), //攻击力
    ATTACK_DAMAGE_RATE("attackDamageRate","攻击:", true,"3b7791fa-0a7e-4701-adbb-cc6d56e7ee5f", Attributes.ATTACK_DAMAGE, AttributeModifier.Operation.MULTIPLY_BASE, 6), //攻击力
    CRITICAL_RATE("criticalRate", "暴率:", true,7), //暴击率
    CRITICAL_DAMAGE("criticalDamage","暴伤:", true,8), //暴击伤害
    HEAL("heal","治疗:", true,9), //治疗加成
    ATTACK_PHYSICS("attackPhysics", "物伤:", true,10); //物理伤害加成

    //怀表可选值
    public static final RelicsAttrType[] ALL_CLOCK = new RelicsAttrType[]{HEALTH_RATE, DEFENSE_RATE, ATTACK_DAMAGE_RATE};
    //杯可选值
    public static final RelicsAttrType[] ALL_CUP = new RelicsAttrType[]{HEALTH_RATE, DEFENSE_RATE, ATTACK_DAMAGE_RATE,
            ATTACK_PHYSICS};
    //头可选值
    public static final RelicsAttrType[] ALL_HEAD = new RelicsAttrType[]{HEALTH_RATE, DEFENSE_RATE, ATTACK_DAMAGE_RATE,
            CRITICAL_RATE, CRITICAL_DAMAGE, HEAL, ATTACK_PHYSICS};
    //副属性 可选值
    public static final RelicsAttrType[] ALL_VICE = new RelicsAttrType[]{HEALTH, HEALTH_RATE, DEFENSE, DEFENSE_RATE, ATTACK_DAMAGE,
            ATTACK_DAMAGE_RATE, CRITICAL_RATE, CRITICAL_DAMAGE};
    //主属性 可选值
    public static final RelicsAttrType[] ALL_MAIN = new RelicsAttrType[]{HEALTH, HEALTH_RATE, DEFENSE_RATE, ATTACK_DAMAGE,
            ATTACK_DAMAGE_RATE, CRITICAL_RATE, CRITICAL_DAMAGE, HEAL, ATTACK_PHYSICS};
    //含有修饰符属性
    public static final RelicsAttrType[] ALL_MODIFIER = new RelicsAttrType[]{HEALTH, HEALTH_RATE, DEFENSE, DEFENSE_RATE, ATTACK_DAMAGE,
            ATTACK_DAMAGE_RATE};
    public static final RelicsAttrType[] ALL = new RelicsAttrType[]{HEALTH, HEALTH_RATE, DEFENSE, DEFENSE_RATE, ATTACK_DAMAGE,
            ATTACK_DAMAGE_RATE, CRITICAL_RATE, CRITICAL_DAMAGE, HEAL, ATTACK_PHYSICS};

    private final String tip; //属性描述字段
    private final String attrName; //属性描述字段
    private final int id; //属性id
    private final boolean isRate; //是数值还是百分比
    private UUID uuid;
    private Attribute attribute;
    private AttributeModifier.Operation operation;

    RelicsAttrType(String str, String attrName, boolean flag, String uuid, Attribute attribute, AttributeModifier.Operation operation, int id) {
        this.tip = str;
        this.id = id;
        this.isRate = flag;
        this.attrName = attrName;
        this.uuid = UUID.fromString(uuid);
        this.attribute = attribute;
        this.operation = operation;
    }

    RelicsAttrType(String str, String attrName, boolean flag, int id) {
        this.tip = str;
        this.id = id;
        this.isRate = flag;
        this.attrName = attrName;
    }

    public int getId() {
        return id;
    }

    public String getTip() {
        return tip;
    }

    public String getAttrName() {
        return attrName;
    }

    public boolean isRate() {
        return isRate;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean isAttr() {
        return attribute != null;
    }

    public Attribute getAttribute() {
        return attribute;
    }

    public AttributeModifier.Operation getOperation() {
        return operation;
    }

    /**
     * 获取指定id的属性类型 无则返回空类型
     *
     * @param id id
     * @return 属性
     */
    public static RelicsAttrType getRelicsAttrTypeFormId(int id) {
        for (RelicsAttrType attrType : ALL) {
            if (attrType.getId() == id) return attrType;
        }
        return RelicsAttrType.EMPTY;
    }

    public boolean isEquals(RelicsAttrType attrType) {
        return this.id == attrType.id && this.tip.equals(attrType.tip);
    }
}
