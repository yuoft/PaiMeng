package com.yuo.PaiMeng.WorldGen;

import com.yuo.PaiMeng.Entity.BoarEntity;
import com.yuo.PaiMeng.Entity.CraneEntity;
import com.yuo.PaiMeng.Entity.EntityTypeRegister;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;

/**
 * 实体生成规则。
 * 注意：生物的 Attribute 在 Event\MobEvent 里用 EntityAttributeCreationEvent 注册（1.20.1 重复注册会崩），
 * 这里只负责刷怪位置规则。
 */
public class EntityGen {

    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        //野猪：主世界地面昼夜均可生成
        event.register(EntityTypeRegister.BOAR.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BoarEntity::checkBoarSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        //仙鹤：主世界地面
        event.register(EntityTypeRegister.CRANE.get(), SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CraneEntity::checkCraneSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
