# PaiMeng 1.16.5 → 1.20.1 移植指南 (Porting Guide)

本文件是本次移植的唯一事实来源。所有参与移植的 agent 必须遵守。

## 0. 项目坐标

| 用途 | 路径 |
| --- | --- |
| 原始 1.16.5 源码（只读参照，**不要修改**） | `D:\McMod16\PaiMeng\src\main\java\com\yuo\PaiMeng\` |
| 目标 1.20.1 工程（**只能改这里**） | `D:\McMod20\PaiMeng\src\main\java\com\yuo\PaiMeng\` |
| MC 1.20.1 + Forge 47.4.0 反编译源码（API 权威参照） | `D:\McMod20\_mcref_1201\src\` (包结构：`net\minecraft\...`, `net\minecraftforge\...`) |
| 同作者已完成的 1.20.1 移植工程（写法参照） | `D:\McMod20\Endless-20\src\main\java\com\yuo\endless\` |

modid = `paimeng`，包名 `com.yuo.paimeng`（**大小写保持不变**，不要改包名/类名/公共方法名）。
Forge 1.20.1-47.4.0，官方映射（official 1.20.1），Java 17，编码 UTF-8。

## 1. 编译检查工具（每个 agent 必须用）

不要把整个工程丢给 gradle（由主控负责全量编译）。用下面脚本单文件/多文件检查，它只打印属于你文件里的诊断：

```
cd D:\McMod20\PaiMeng
powershell -NoProfile -ExecutionPolicy Bypass -File port-tools\check.ps1 -Files "com/yuo/PaiMeng/Blocks/CookingPot.java,com/yuo/PaiMeng/Blocks/CookingBench.java"
```

输出 `OK: no diagnostics in the requested files` 表示你的文件已通过。
注意：因为工程里其它文件可能还没移植完，检查时**忽略不属于你负责文件的错误**。
如果 classpath.txt 丢失，先运行 `.\gradlew.bat --offline dumpClasspath`。

查 API 的推荐做法：直接 grep 反编译源码，例如
`Select-String -Path D:\McMod20\_mcref_1201\src\net\minecraft\world\level\block\CropBlock.java -Pattern "protected|public"`

## 2. 硬性规则

1. **保留公共名称**：类名、字段名、public 方法名/签名尽量与 1.16.5 一致，其它 agent 的代码依赖它们
   （例如 `PMItems.cookingPot`、`PMBlocks.cookingPot.get()`、`TileTypeRegistry.POT_TILE`、
   `ContainerTypeRegistry.potContainer`、`NetWorkHandler.INSTANCE`）。1.20.1 强制要求变化时，保留旧名做重载。
2. **不要删除功能**。确实暂时无法移植的，用 `// TODO(port): 原因` 注释掉最小范围，并在最终报告里列出。
3. **不要动别人的文件**（只读参照可以）。不要改 `build.gradle` / `gradle.properties` / 资源文件以外的构建配置。
4. 不要碰 `src/main/resources`（资源由主控处理），除非你负责的 Java 代码需要新的注册项（那不叫资源）。
5. 文件必须是 UTF-8 无 BOM，中文注释保留。
6. 你负责的文件在 `check.ps1` 下必须 0 error。warning 可忽略（-nowarn 已开）。

## 3. 已经做过的机械替换（别再重复做）

- 包名批量映射（`net.minecraft.item→net.minecraft.world.item`、`net.minecraft.block→net.minecraft.world.level.block`、
  `net.minecraft.tileentity→net.minecraft.world.level.block.entity`、`net.minecraft.inventory.container→net.minecraft.world.inventory`、
  `net.minecraft.entity.*→net.minecraft.world.entity.*`、`net.minecraft.util.text→net.minecraft.network.chat`、
  `net.minecraft.util.math→net.minecraft.core` 等）
- 类名：`TileEntity→BlockEntity`、`TileEntityType→BlockEntityType`、`ContainerType→MenuType`、
  `Container→AbstractContainerMenu`、`IInventory→Container`、`Inventory→SimpleContainer`、`PlayerEntity→Player`、
  `World→Level`、`ServerWorld→ServerLevel`、`ItemGroup→CreativeModeTab`、`Hand→InteractionHand`、
  `ActionResultType→InteractionResult`、`Effect→MobEffect`、`Effects→MobEffects`、`Food→FoodProperties`、
  `IRecipe→Recipe`、`IRecipeSerializer→RecipeSerializer`、`PacketBuffer→FriendlyByteBuf`、`NBT 类` 等
- Item.Properties：`group(...)` 已删除、`maxStackSize→stacksTo`、`isImmuneToFire→fireResistant`、`maxDamage→durability`

机械替换**不保证语义正确**（例如 `MapColor`、`Material`、`Properties.from(...)`、`copy(...)` 等仍需人工确认）。

## 4. 关键 1.16.5 → 1.20.1 API 对照（常用）

### 物品 Item
```java
// 1.16.5                                  // 1.20.1
new Item.Properties().group(TAB)           -> new Item.Properties()          // 创造页改由 CreativeModeTab.displayItems 决定
.maxStackSize(n)                           -> .stacksTo(n)
.maxDamage(n)                              -> .durability(n)
.isImmuneToFire()                          -> .fireResistant()
food(Food)                                 -> .food(FoodProperties)
onItemRightClick(world, player, hand)      -> use(Level, Player, InteractionHand) 返回 InteractionResultHolder<ItemStack>
onItemUse(ItemUseContext)                  -> useOn(UseOnContext)
onItemUseFinish                            -> finishUsingItem(ItemStack, Level, LivingEntity)
getUseDuration(ItemStack)                  -> getUseDuration(ItemStack)
getUseAction(ItemStack)                    -> getUseAnimation(ItemStack) 返回 UseAnim
addInformation(stack, world, tooltip, flag)-> appendHoverText(ItemStack, Level, List<Component>, TooltipFlag)
getAttributeModifiers(slot, stack)         -> getDefaultAttributeModifiers(EquipmentSlot)
fillItemGroup(group, items)                -> fillItemCategory(CreativeModeTab, CreativeModeTab.Output)
hasEffect(stack)                           -> isFoil(stack)
setCreativeTab ...                         -> 不需要
```
`ItemTier` 在 1.20.1 叫 `net.minecraft.world.item.Tier`（`getLevel/getUses/getSpeed/getAttackDamageBonus/getEnchantmentValue/getRepairIngredient`）。

### 方块 Block
```java
AbstractBlock.Properties.create(Material.X)  -> BlockBehaviour.Properties.of()
Block.Properties.from(other)                 -> BlockBehaviour.Properties.copy(other)   // 1.20.1 没有 from
.doesNotBlockMovement()                      -> .noCollission()
.tickRandomly()                              -> .randomTicks()
.zeroHardnessAndResistance()                 -> .instabreak()
.hardnessAndResistance(h, r)                 -> .strength(h, r)
.setLightLevel(s -> n)                       -> .lightLevel(s -> n)
.notSolid()                                  -> .noOcclusion()
slipperiness(x)                              -> .friction(x)
Blocks.GRASS                                 -> Blocks.GRASS_BLOCK
Material / MapColor 判断                    -> state.is(BlockTags.X) / state.getMapColor(...) / 属性 flags（见下）
```
方法：
```java
onBlockActivated(state, world, pos, player, hand, hit)   -> use(BlockState, Level, BlockPos, Player, InteractionHand, BlockHitResult)
onBlockClicked                                            -> attack(...)
onBlockPlacedBy                                           -> setPlacedBy(Level, BlockPos, BlockState, LivingEntity, ItemStack)
onBlockHarvested                                          -> playerDestroy(...)
neighborChanged(state, world, pos, fromBlock, fromPos, isMoving) -> neighborChanged(BlockState, Level, BlockPos, Block, BlockPos, boolean)
updatePostPlacement(state, facing, facingState, world, pos, facingPos) -> updateShape(BlockState, Direction, BlockState, LevelAccessor, BlockPos, BlockPos)
canSustainPlant / canSustainBush                           -> mayPlaceOn(BlockState, BlockGetter, BlockPos) (BushBlock)
getShape(state, world, pos, ctx)                            -> getShape(BlockState, BlockGetter, BlockPos, CollisionContext)
isNormalCube / isFullCube / isOpaqueCube                    -> isCollisionShapeFullBlock / propagatesSkylightDown / useShapeForLightOcclusion
randomTick(state, serverLevel, pos, rand)
entityInside(state, world, pos, entity)
tick(state, serverLevel, pos, rand)
getCloneItemStack(world,pos,state)                          -> getCloneItemStack(BlockGetter, BlockPos, BlockState)
isTransparent / isAir                                        -> 见 BlockBehaviour.Properties flags
```

### 方块实体 BlockEntity
```java
public class XxxTile extends BlockEntity {
    public XxxTile(BlockEntityType<?> type, BlockPos pos, BlockState state) { super(type, pos, state); }
    // 1.16 的 XxxTile() 无参构造 + registerTileEntity(type, "id", XxxTile::new) 现在要走 BlockEntityType.Builder.of(XxxTile::new, block)
    protected void save(CompoundTag nbt)                 -> protected void saveAdditional(CompoundTag nbt)
    public void read(BlockState s, CompoundTag nbt)      -> public void load(CompoundTag nbt)
    public void read(CompoundTag) / fromTag               -> load(CompoundTag)
    markDirty()                                           -> setChanged()
    world / getWorld()                                    -> level
    getPos()                                              -> getBlockPos()
    getUpdatePacket/getUpdateTag                          -> 同名，但 ITickableTileEntity 已删除
}
```
刻（tick）：1.20.1 没有 `ITickableTileEntity`。在 Block 里返回静态 Ticker：
```java
@Override
public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
    return level.isClientSide ? null : (lvl, pos, st, be) -> { if (be instanceof MyTile t) t.tick(lvl, pos, st); };
}
```
`BlockEntityType` 注册：`DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MOD_ID)`，
`BlockEntityType.Builder.of(PotTile::new, PMBlocks.cookingPot.get()).build(null)`。

### 容器 / GUI（Menu + Screen）
```java
net.minecraft.inventory.container.Container          -> net.minecraft.world.inventory.AbstractContainerMenu
  stillValid / canInteractWith                      -> stillValid(Player)
  getInventory()                                    -> slots / 自己保存
  transferStackInSlot(player, index)                -> quickMoveStack(Player, int)
  playerInventory                                   -> player.getInventory()
  INamedContainerProvider                           -> MenuProvider (createMenu(int, Inventory, Player))
  IForgeContainerType.create(...)                   -> IForgeMenuType.create((id, inv, buf) -> new XMenu(id, inv))
  Player.openContainer(provider)                    -> player.openMenu(provider)   // 服务端调用；在 Block#use 里 return InteractionResult.sidedSuccess(level.isClientSide)
  network 里打开 GUI：ServerPlayer#openMenu
```
Screen：
```java
net.minecraft.client.gui.screen.inventory.ContainerScreen -> net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
render(matrixStack, mouseX, mouseY, partialTick)          -> render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick)
drawGuiContainerBackgroundLayer(matrix, ...)              -> renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY)
drawGuiContainerForegroundLayer                           -> renderLabels(GuiGraphics, int, int)
renderTooltip(matrix, ...)                                -> graphics.renderTooltip(font, stack, mouseX, mouseY)
blit(x,y,u,v,w,h) / this.blit(...)                        -> graphics.blit(TEXTURE, x, y, u, v, w, h)
font.draw / drawString                                    -> graphics.drawString(font, ...) / graphics.drawCenteredString(...)
RenderSystem.color4f                                     -> graphics.setColor(r,g,b,a) + graphics.setColor(1,1,1,1) 复原
MatrixStack                                              -> PoseStack；RenderSystem.pushMatrix -> poseStack.pushPose()
```
菜单屏幕注册（客户端）：
```java
@SubscribeEvent public static void onClientSetup(FMLClientSetupEvent e) {
    MenuScreens.register(ContainerTypeRegistry.potContainer.get(), PotScreen::new);
}
```
1.20.1 推荐用 `RegisterMenuScreensEvent`（mod 总线）：`event.register(menuType, PotScreen::new)`。

### 网络 SimpleChannel（1.20.1 仍可用 NetworkRegistry.newSimpleChannel）
```java
INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation(MOD_ID, "network"),
        () -> "1.0", "1.0"::equals, "1.0"::equals);
INSTANCE.registerMessage(id, PacketClass.class, PacketClass::toBytes, PacketClass::new, PacketClass::handler);
// handler 内：ctx.get().enqueueWork(() -> ...); ctx.get().setPacketHandled(true);
// 发送：NetWorkHandler.INSTANCE.send(PacketDistributor.SERVER.noArg(), packet)
//      NetWorkHandler.INSTANCE.sendToServer(packet) / send(PacketDistributor.PLAYER.with(() -> sp), packet)
// 注意：1.20.1 里 PacketDistributor.PLAYER / TARGET / ALL / ALL_WITHOUT_DIMENSION 没变
```

### Capability（1.20.1 Forge 47 写法，**能力接口用类本体做 key**）
```java
// 注册 key（CapabilityManager 现在是 enum，只有 get(token)，没有 register！）
public static final Capability<IBlowCapability> BLOW_CAPABILITY =
        CapabilityManager.get(new CapabilityToken<>() {});
// mod 构造里 addListener(RegisterCapabilitiesEvent.class, e -> { e.register(IBlowCapability.class); ... });
// provider 实现 ICapabilityProvider + INBTSerializable<CompoundTag>（即 ICapabilitySerializable<CompoundTag>）
// 附加到玩家：AttachCapabilitiesEvent<Entity> 里 event.addCapability(new ResourceLocation(MOD_ID,"blow"), new BlowCapabilityProvider())
// 查询：player.getCapability(ModCapability.BLOW_CAPABILITY)  → LazyOptional<IBlowCapability>
```

### 效果 / 药水
```java
Effect            -> MobEffect（构造：new MobEffect(MobEffectCategory.BENEFICIAL, 0xRRGGBB)）
Effects.X         -> MobEffects.X
EffectInstance    -> MobEffectInstance
potion 注册：DeferredRegister.create(ForgeRegistries.POTIONS, ...)  ; BrewingRecipe / PotionBrewing.addMix
```

### 附魔 / 属性 / 实体
```java
AttributeModifier(..., "name") -> AttributeModifier(UUID, "name", ...) 仍在；也可用 AttributeModifier(String id, double, Operation)
EntityType.Builder.create(...) -> EntityType.Builder.of(Foo::new, MobCategory.CREATURE)
  .size(w,h).setTrackingRange(..).setUpdateInterval(..).setShouldReceiveVelocityUpdates(..)
  建造: .build("paimeng:boar")
生物属性注册：EntityAttributeCreationEvent (mod bus) → event.put(EntityTypeRegister.BOAR.get(), BoarEntity.createAttributes().build())
刷怪蛋：new ForgeSpawnEggItem(supplier, c1, c2, new Item.Properties())   // 1.16 的 ModSpawnEgg 自定义类可以改成这个或保留自定义
生成位置：SpawnPlacementRegisterEvent (Forge 事件总线)
```

### 配方 JSON / Recipe API
```java
IRecipe<T> -> Recipe<Container>（1.20.1：getSerializer/getType/getResultItem/getIngredients/canCraftInDimensions/ ...）
RecipeType.register("x") -> RecipeType.simple(new ResourceLocation(MOD_ID,"x")) 配合 Registry.register(Registries.RECIPE_TYPE, ...)
   （也可以用 DeferredRegister.create(Registries.RECIPE_TYPE, MOD_ID)）
IRecipeSerializer -> RecipeSerializer<T>：fromJson(ResourceLocation, JsonObject) / toJson / fromNetwork(FriendlyByteBuf) / toNetwork / getRegistryName
1.20.1 建议只实现 fromJson / toJson / fromNetwork / toNetwork（setRegistryName 不需要了）
```

### 世界生成（最难的一块，1.20.1 必须走 Feature + PlacedFeature）
- 自定义 Feature：`public class XFeature extends Feature<XConfig>`，`public boolean place(FeaturePlaceContext<XConfig> ctx)`；
  Config 必须实现 `Codec<XConfig>`（`RecordCodecBuilder.mapCodec(instance -> instance.group(...).apply(instance, XConfig::new))`）。
- 注册（mod 构造阶段，`Bootstrap`/`FeatureUtils` 风格）：
```java
public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, MOD_ID);
public static final RegistryObject<XFeature> X = FEATURES.register("x", () -> new XFeature(XConfig.CODEC.codec()));
```
- ConfiguredFeature / PlacedFeature 建议在 `FMLCommonSetupEvent`（或 `RegisterEvent` 之后）用
  `FeatureUtils.register("paimeng:x", X.get(), config)` 与 `PlacementUtils.register("paimeng:x", configured, PlacementUtils.HEIGHTMAP_WORLD_SURFACE, InSquarePlacement.spread(), CountPlacement.of(n), BiomeFilter.biome())` 注册。
- 生物群系注入：`assets` 不需要，写 `data/paimeng/forge/biome_modifier/*.json`（`forge:add_features`），
  BiomeModifier 的 feature 名用 PlacedFeature 的 ResourceKey。资源由主控添加，你只要在 Java 侧把 PlacedFeature 注册好，
  并把需要注入的 ResourceKey 列表以 `public static final ResourceKey<PlacedFeature> XXX` 暴露出来（主控按此写 JSON）。
- 旧的 `ConfiguredFeature`/`DecoratedFeatureConfig`/`FeatureConfig` 全部作废。

### JEI（1.20.1 用 jei-1.20.1-forge:15.2.0.27）
```java
@JeiPlugin   implements IModPlugin            // 1.16 是 IModPlugin 实现 + @JeiPlugin 相同
// 1.20.1 变化：
//  recipe type: mezz.jei.api.recipe.RecipeType.create(ResourceLocation, Class)
//  IRecipeCategory<T>: getRecipeType(), getTitle/getBackground/getIcon, setRecipe(IRecipeLayoutBuilder, ...),
//                      draw(IGuiHelper, GuiGraphics, ...), getTooltipStrings(...)
//  IRecipeCatalystRegistration: addRecipeCatalyst(ItemStack, RecipeType)
//  IRecipeRegistration: addRecipes(RecipeType, List)
//  1.16 的 draw(MatrixStack) / setRecipe(IGuiRecipeLayout...) 全改
```
若某个 JEI 类实在无法在 1.20.1 下工作，可先 `// TODO(port)` 简化，但 `JeiPlugin` 本身必须能编译。

## 5. 常见坑（按报错对号入座）

| 报错 | 修法 |
| --- | --- |
| `cannot find symbol: method from(Block)` | `Properties.copy(block)` |
| `cannot find symbol: Material` / `getMaterial()` | 用 `BlockBehaviour.Properties` 的 flag 或 `state.is(...)`；删掉 Material 逻辑 |
| `FarmlandBlock` 不存在 | `FarmBlock` |
| `CropBlock` 相关 | `net.minecraft.world.level.block.CropBlock`，`getBaseSeedId()` 已删除，用 `getCloneItemStack` |
| `BushBlock` 构造要 `Properties` | `super(properties)` |
| `Block#isOpaqueCube` 等 | 见上表 |
| `ItemStack.getDisplayName()` | `stack.getHoverName()` |
| `TextFormatting` | `ChatFormatting` |
| `StringTextComponent` | `Component.literal` |
| `CompoundNBT.putBoolean` 相同 | OK |
| `player.sendMessage(Component, UUID)` | `player.sendSystemMessage(Component)` |
| `world.isRemote` | `level.isClientSide` |
| `world.addEntity` | `addFreshEntity` |
| `Block.spawnAsEntity` | `popResource` |
| `state.getBlock().getItem(world,pos,state)` | 用 `new ItemStack(state.getBlock())` 或 `getCloneItemStack` |
| `EntityType.MOB` 等分类 | `MobCategory.X` |
| `SoundEvent` 注册 | `SoundEvent.createVariableRangeEvent(ResourceLocation)` |
| `LootTable`/`LootParameterSet` | 只在 1.20.1 结构里用 `LootContextParamSets`，一般不用改 |
| `ServerWorld.getChunk(...)` | `ServerLevel` 同名 |
| `player.getCapability(...)` | 同（`Entity#getCapability`） |

## 6. 完成标准

1. `check.ps1` 对你负责的所有文件输出 `OK`。
2. 公共类/字段名未变，其它包仍能引用。
3. 你的文件里没有残留 1.16.5 类型（`TileEntity`、`IInventory`、`Container` 作为菜单、`ItemGroup`、`Hand`、`World` 等）。
4. 报告：完成文件清单 + 任何被改动的公共签名 + 任何 `TODO(port)` + 资源侧需要主控配合的点（JSON/贴图重命名）。
