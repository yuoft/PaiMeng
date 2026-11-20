# 无相之岩（Hypostasis）设计说明

参考《原神》无相系列 BOSS，为 PaiMeng 模组补全的浮空方块 BOSS。

## 外形
- **外壳**：8 个原版矿物方块拼成 2×2×2 立方体，方块贴图完全来自原版资源
  （铁块 / 金块 / 钻石块 / 绿宝石块 / 青金石块 / 红石块 / 煤炭块 / 铜块）。
  渲染方式：`BlockRenderDispatcher#renderSingleBlock`，不需要自制 PNG。
- **核心**：基岩方块，尺寸 0.72，绕 Y/X/Z 各 45° 倾斜，自发光（全亮渲染），
  二阶段脉动放大。
- 碰撞箱 2×2、无重力、无方块碰撞（可悬浮穿过地形）。

## 战斗
| 项 | 数值 |
| --- | --- |
| 生命 | 450（半血进入二阶段） |
| 攻击力 | 13（X 型 ×1.2，十字 ×0.85） |
| 护甲 / 护甲韧性 | 10 / 4 |
| 跟随范围 | 48 |
| 经验 | 150 |

### 近战：外壳变形砸击（5 格内）
| 形态 | 外壳排布 | 命中区域（以自身为原点，f=朝前、r=横向） | 伤害倍率 |
| --- | --- | --- | --- |
| 一字型 MELEE_LINE | 8 块排成横向长条 | f∈[0.5,6]，\|r\|≤3.9 | ×1.0 |
| 十字型 MELEE_CROSS | 十字排布 | f∈[1.2,6]，\|r\|≤3.2 | ×0.85 |
| X 型 MELEE_X | 双向对角 | f∈[0.5,6]，\|r\|≤4.4 | ×1.2 |

砸击落点会**击碎硬度 0~10 的方块并正常掉落**；基岩、带方块实体的方块、流体不受影响，
每次最多 10 块（二阶段 14 块）。命中生物附带击退与上挑。

### 远程：抛掷随机矿物方块（6~28 格）
真正的实体投射物 `paimeng:hypostasis_projectile`，一次出招抛 3 块（8/18/28 tick），
命中生物 8 点伤害（二阶段 10 点）+ 击退，命中方块消失。

### 雷击：多层环状（5~16 格，按概率触发）
二阶段前 40% 概率、二阶段 60% 概率；以自身为中心 6/9/12（二阶段加 15）格画 3~4 层环，
每层 8~14 个环点，只劈击环点 2.2 格内最近的生物（视觉闪电 + 8/10 点雷电伤害 + 短暂点燃）。

### 二阶段（半血）
外壳散开、转速加快、移动速度 0.16→0.24、出招冷却整体缩短，
入场时爆炸粒子 + 推开 7 格内生物。

### 掉落
原版不死图腾（`data/paimeng/loot_tables/entities/hypostasis.json`）。

## 出现方式
- 刷怪蛋 `paimeng:hypostasis_spawn_egg`（创造物品栏「其它」分类）
- `/summon paimeng:hypostasis`
- **召唤祭坛**：用 8 种各不相同的矿物块搭 2×2×2 实心方块堆，
  手持「下界之心」右击其中任意方块 → 召唤 BOSS，消耗 1 个下界之心（祭坛方块保留）。

## 代码地图
| 文件 | 作用 |
| --- | --- |
| `entity/HypostasisEntity.java` | 状态机、两阶段、近战判定、方块破坏、投射物、环状雷击、Boss 血条、掉落表 |
| `entity/AI/HypostasisHoverGoal.java` | 悬浮跟随 |
| `entity/AI/HypostasisMeleeGoal.java` | 近战形态随机 |
| `entity/AI/HypostasisRangedGoal.java` | 远程抛掷 |
| `entity/AI/HypostasisThunderGoal.java` | 雷击概率与距离窗口 |
| `entity/HypostasisProjectile.java` | 方块投射物 |
| `client/render/HypostasisRenderer.java` | 外壳/核心渲染与全部形态动画 |
| `client/render/HypostasisProjectileRenderer.java` | 投射物渲染 |
| `event/HypostasisAltarEvent.java` | 召唤祭坛多方块检测 |
| `items/NetherHeart.java` | 下界之心 |

## 已知限制
- 外壳是渲染出来的方块，不是真实方块，无法被挖掘或推动。
- 无方块碰撞（`noPhysics`），会穿过墙体和地形。
- 砸击的方块破坏不会绕过领地/保护插件（服务端未接入权限判断）。
