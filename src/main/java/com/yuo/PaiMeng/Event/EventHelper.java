package com.yuo.PaiMeng.Event;

import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effect;

public class EventHelper {

//     ModifiableAttributeInstance attribute = player.getAttribute(Attributes.ARMOR);
//     attribute.setBaseValue(attribute.getValue() + 10); //加10点护甲
//     ModifiableAttributeInstance attribute = player.getAttribute(Attributes.ARMOR);
//     attribute.setBaseValue(attribute.getValue() - 10);

    /**
     * 获取buff等级 保存buff数据
     *
     * @param player 玩家
     * @return buff等级
     */
    public static int setBuffLevel(PlayerEntity player, Effect effect) {
        int amplifier = player.getActivePotionEffect(effect).getAmplifier() + 1; //获取buff等级
        CompoundNBT data = player.getPersistentData(); //玩家数据 存储状态id + 等级
        ListNBT listNBT = new ListNBT();
        CompoundNBT nbtId = new CompoundNBT();
        CompoundNBT nbtLevel = new CompoundNBT();
        nbtId.putString("id", effect.getName());
        nbtLevel.putInt("level", amplifier);
        listNBT.add(0, nbtId);
        listNBT.add(1, nbtLevel);
        data.put(effect.getName(), listNBT);
        return amplifier;
    }

    /**
     * 获取保存的buff信息
     *
     * @param player 玩家
     * @return buff等级 为0则没有
     */
    public static int getBuffLevel(PlayerEntity player, Effect effect) {
        CompoundNBT data = player.getPersistentData();
        //根据buff等级改变属性
        int level = 0;
        ListNBT listNBT = (ListNBT) data.get(effect.getName());
        if (listNBT == null || listNBT.isEmpty()) return level;
        CompoundNBT nbtId = (CompoundNBT) listNBT.get(0);
        CompoundNBT nbtLevel = (CompoundNBT) listNBT.get(1);
        if ((nbtId == null || nbtLevel == null) || (nbtId.isEmpty() || nbtLevel.isEmpty())) return level;
        //获取信息
        String id = nbtId.getString("id"); 
        if (id.equals(effect.getName())) level = nbtLevel.getInt("level");
        return level;
    }

    /**
     * 增加玩家属性 基于buff等级和变更系数决定
     * @param player 玩家
     * @param attr 需要变更的属性
     * @param effect 生效buff
     * @param value 变更系数
     */
    public static void upAttribute(PlayerEntity player, Attribute attr, Effect effect , float value){
        ModifiableAttributeInstance attribute = player.getAttribute(attr);
        if (attribute == null) return;
        int level = EventHelper.setBuffLevel(player, effect);
        attribute.setBaseValue(attribute.getValue() + level * value);
    }

    /**
     * 减少玩家属性 基于buff等级和变更系数决定
     * @param player 玩家
     * @param attr 需要变更的属性
     * @param effect 生效buff
     * @param value 变更系数
     */
    public static void downAttribute(PlayerEntity player, Attribute attr, Effect effect, float value){
        ModifiableAttributeInstance attribute = player.getAttribute(attr);
        if (attribute == null) return;
        int level = EventHelper.getBuffLevel(player, effect);
        attribute.setBaseValue(attribute.getValue() - level * value);
    }

    /**
     * 重置玩家属性
     * @param player 玩家
     * @param effect  buff
     * @param attr 属性
     * @param value 变更系数
     */
    public static void resetPlayerAttribute(PlayerEntity player, Effect effect, Attribute attr, float value){
        CompoundNBT data = player.getPersistentData();
        ListNBT listNBT = (ListNBT) data.get(effect.getName());
        if (!listNBT.isEmpty()){
            CompoundNBT nbtId = (CompoundNBT) listNBT.get(0);
            CompoundNBT nbtLevel = (CompoundNBT) listNBT.get(1);
            if (!nbtId.isEmpty() && !nbtLevel.isEmpty()){
                String id = nbtId.getString("id");
                if(id.equals(effect.getName())){
                }
            }
        }
        ModifiableAttributeInstance attribute = player.getAttribute(attr);
        attribute.setBaseValue(attribute.getValue() - 0);
    }
}
