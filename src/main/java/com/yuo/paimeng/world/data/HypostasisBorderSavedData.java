package com.yuo.paimeng.world.data;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.saveddata.SavedData;

/**
 * 世界边界封锁的持久化快照。
 *
 * <p>无相之岩进入二阶段时会临时把世界边界收小到 6 格范围。世界边界是**整维度共享**的，
 * 如果服务器在封锁期间被直接关闭或崩溃，存档里就会残留一个极小的边界。
 * 这里把「封锁前的原值」写进存档，服务器下次启动时若发现边界仍是封锁尺寸就自动改回去。
 */
public class HypostasisBorderSavedData extends SavedData {

    private static final String DATA_NAME = "paimeng_hypostasis_border";

    private boolean active;
    private double size;
    private double centerX;
    private double centerZ;
    private double damagePerBlock;
    private double damageSafeZone;
    private int warningBlocks;
    private int warningTime;

    public static HypostasisBorderSavedData get(ServerLevel level) {
        ServerLevel overworld = level.getServer().overworld();
        return overworld.getDataStorage().computeIfAbsent(
                HypostasisBorderSavedData::load, HypostasisBorderSavedData::new, DATA_NAME);
    }

    /** 在修改边界之前调用，记录原值。 */
    public void snapshot(WorldBorder border) {
        this.active = true;
        this.size = border.getSize();
        this.centerX = border.getCenterX();
        this.centerZ = border.getCenterZ();
        this.damagePerBlock = border.getDamagePerBlock();
        this.damageSafeZone = border.getDamageSafeZone();
        this.warningBlocks = border.getWarningBlocks();
        this.warningTime = border.getWarningTime();
        this.setDirty();
    }

    public void clear() {
        if (!this.active) return;
        this.active = false;
        this.setDirty();
    }

    /**
     * 若存档里仍有未清理的封锁记录，且当前边界确实等于封锁尺寸，则恢复原值。
     *
     * @param blockedSize 封锁时使用的边界边长
     * @return 是否执行了恢复
     */
    public boolean restoreIfStillBlocked(WorldBorder border, double blockedSize) {
        if (!this.active) return false;
        if (Math.abs(border.getSize() - blockedSize) > 0.05D) {
            // 边界已被别人改过，说明不是我们残留的尺寸：丢弃记录即可
            this.clear();
            return false;
        }
        border.setCenter(this.centerX, this.centerZ);
        border.setSize(this.size);
        border.setWarningBlocks(this.warningBlocks);
        border.setWarningTime(this.warningTime);
        border.setDamagePerBlock(this.damagePerBlock);
        border.setDamageSafeZone(this.damageSafeZone);
        this.clear();
        return true;
    }

    public boolean isActive() {
        return this.active;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putBoolean("Active", this.active);
        tag.putDouble("Size", this.size);
        tag.putDouble("CenterX", this.centerX);
        tag.putDouble("CenterZ", this.centerZ);
        tag.putDouble("Damage", this.damagePerBlock);
        tag.putDouble("SafeZone", this.damageSafeZone);
        tag.putInt("WarnBlocks", this.warningBlocks);
        tag.putInt("WarnTime", this.warningTime);
        return tag;
    }

    public static HypostasisBorderSavedData load(CompoundTag tag) {
        HypostasisBorderSavedData data = new HypostasisBorderSavedData();
        data.active = tag.getBoolean("Active");
        data.size = tag.getDouble("Size");
        data.centerX = tag.getDouble("CenterX");
        data.centerZ = tag.getDouble("CenterZ");
        data.damagePerBlock = tag.getDouble("Damage");
        data.damageSafeZone = tag.getDouble("SafeZone");
        data.warningBlocks = tag.getInt("WarnBlocks");
        data.warningTime = tag.getInt("WarnTime");
        return data;
    }
}
