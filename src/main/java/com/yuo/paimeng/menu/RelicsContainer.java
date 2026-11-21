package com.yuo.paimeng.menu;

import com.yuo.paimeng.capability.IBlowCapability;
import com.yuo.paimeng.capability.ModCapability;
import com.yuo.paimeng.capability.RelicsItemHandler;
import com.yuo.paimeng.items.relics.Relics;
import com.yuo.paimeng.items.relics.RelicsHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.util.LazyOptional;

public class RelicsContainer extends AbstractContainerMenu {
    private final RelicsItemHandler itemHandler; //存储食材
    private final ContainerData data; //属性同步数据
    private final Player player;

    public RelicsContainer(int id, Inventory playerInventory) {
        super(PMMenuTypes.relicsContainer.get(), id);
        this.player = playerInventory.player;
        this.itemHandler = player.getCapability(ModCapability.RELICS_CAPABILITY).orElseThrow(NullPointerException::new);
        //圣遗物槽
        for (int m = 0; m < 2; ++m){
            for (int n = 0; n < 3; ++n){
                this.addSlot(new RelicsSlot(this.itemHandler, n + m * 3, 8 + m * 28, 8 + n * 23));
            }
        }
        //添加玩家物品栏
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }
        //添加玩家快捷栏
        for(int k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.data = new SimpleContainerData(16);
        this.addDataSlots(this.data);
    }

    // 🔴 每一 tick 同步服务端数据到客户端
    @Override
    public void broadcastChanges() {
        if (this.player instanceof ServerPlayer sp) {
            // 统一乘 10000 转换为 int 传输，防止精度丢失
            this.data.set(0, (int)(sp.getAttributeValue(Attributes.MAX_HEALTH) * 10000));
            this.data.set(1, (int)(sp.getAttributeValue(Attributes.ARMOR) * 10000));
            this.data.set(2, (int)(sp.getAttributeValue(Attributes.ARMOR_TOUGHNESS) * 10000));
            this.data.set(3, (int)(sp.getAttributeValue(Attributes.ATTACK_DAMAGE) * 10000));
            this.data.set(4, (int)(sp.getAttributeValue(Attributes.ATTACK_SPEED) * 10000));
            this.data.set(5, (int)(sp.getAttributeValue(Attributes.MOVEMENT_SPEED) * 10000));
            this.data.set(6, (int)(sp.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE) * 10000));
            this.data.set(7, (int)(sp.getAttributeValue(Attributes.ATTACK_KNOCKBACK) * 10000));
            this.data.set(8, (int)(sp.getAttributeValue(Attributes.LUCK) * 10000));
            this.data.set(11, (int)(sp.getAttributeValue(ForgeMod.SWIM_SPEED.get()) * 10000));
//            this.data.set(12, (int)(sp.getAttributeValue(ForgeMod.NAMETAG_DISTANCE.get()) * 10000));
//            this.data.set(13, (int)(sp.getAttributeValue(ForgeMod.ENTITY_GRAVITY.get()) * 10000));
            this.data.set(12, (int)(sp.getAttributeValue(ForgeMod.BLOCK_REACH.get()) * 10000));
            this.data.set(13, (int)(sp.getAttributeValue(ForgeMod.ENTITY_REACH.get()) * 10000));
            this.data.set(14, (int)((sp.getAttributeValue(ForgeMod.STEP_HEIGHT_ADDITION.get()) + 0.6) * 10000));
            this.data.set(15, (int)(sp.getAbilities().getFlyingSpeed() * 10000));

            LazyOptional<IBlowCapability> cap = sp.getCapability(ModCapability.BLOW_CAPABILITY);
            cap.ifPresent(e -> {
                this.data.set(9, (int)(e.getCriticalRate() * 10000));
                this.data.set(10, (int)(e.getCriticalDamage() * 10000));
            });
        }
        super.broadcastChanges();
    }

    // 🔴 提供 getter 给 Screen 使用 (统一除以 10000)
    public double getSyncedHealth() { return this.data.get(0) / 10000.0; }
    public double getSyncedArmor() { return this.data.get(1) / 10000.0; }
    public double getSyncedToughness() { return this.data.get(2) / 10000.0; }
    public double getSyncedAttack() { return this.data.get(3) / 10000.0; }
    public double getSyncedAttackSpeed() { return this.data.get(4) / 10000.0; }
    public double getSyncedMoveSpeed() { return this.data.get(5) / 10000.0; }
    public double getSyncedKnockbackRes() { return this.data.get(6) / 10000.0; }
    public double getSyncedAttackKnockback() { return this.data.get(7) / 10000.0; }
    public double getSyncedLuck() { return this.data.get(8) / 10000.0; }
    public double getSyncedCritRate() { return this.data.get(9) / 10000.0; }
    public double getSyncedCritDamage() { return this.data.get(10) / 10000.0; }
    public double getSyncedSwimSpeed() { return this.data.get(11) / 10000.0; }
//    public double getSyncedNametagDistance() { return this.data.get(12) / 10000.0; }
//    public double getSyncedEntityGravity() { return this.data.get(13) / 10000.0; }
    public double getSyncedBlockReach() { return this.data.get(12) / 10000.0; }
    public double getSyncedEntityReach() { return this.data.get(13) / 10000.0; }
    public double getSyncedStepHeight() { return this.data.get(14) / 10000.0; }
    public double getSyncedFlySpeed() { return this.data.get(15) / 10000.0; }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack itemStack1 = slot.getItem();
            itemstack = itemStack1.copy();
            if (index >= 6){
                if (RelicsHelper.isGodEye(itemStack1)) { //神之眼进第 6 格
                    if (!this.moveItemStackTo(itemStack1, 5, 6, false)) return ItemStack.EMPTY;
                }
                if (itemStack1.getItem() instanceof Relics){
                    switch (RelicsHelper.getTypeForStack(itemStack1)){
                        case HEAD:
                            if (!this.moveItemStackTo(itemStack1, 0, 1, false)) return ItemStack.EMPTY;
                            break;
                        case CUP:
                            if (!this.moveItemStackTo(itemStack1, 1, 2, false)) return ItemStack.EMPTY;
                            break;
                        case CLOCK:
                            if (!this.moveItemStackTo(itemStack1, 2, 3, false)) return ItemStack.EMPTY;
                            break;
                        case FEATHER:
                            if (!this.moveItemStackTo(itemStack1, 3, 4, false)) return ItemStack.EMPTY;
                            break;
                        case FLOWER:
                            if (!this.moveItemStackTo(itemStack1, 4, 5, false)) return ItemStack.EMPTY;
                            break;
                    }
                }
                if (index < 33) { //从物品栏到快捷栏
                    if (!this.moveItemStackTo(itemStack1, 33, 42, false)) return ItemStack.EMPTY;
                } else if (index < 42) {
                    if (!this.moveItemStackTo(itemStack1, 6, 33, false)) return ItemStack.EMPTY;
                }
            }else if (!this.moveItemStackTo(itemStack1, 6, 42, false)) return ItemStack.EMPTY; //取出来

            if (itemStack1.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (itemStack1.getCount() == itemstack.getCount()) return ItemStack.EMPTY;
            slot.onTake(playerIn, itemStack1);
        }

        return itemstack;
    }
}
