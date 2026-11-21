package com.yuo.paimeng.proxy;

import com.yuo.paimeng.ModUtils;
import com.yuo.paimeng.PMKeyManager;
import com.yuo.paimeng.blocks.PMBlocks;
import com.yuo.paimeng.client.model.BoarModel;
import com.yuo.paimeng.client.model.CraneModel;
import com.yuo.paimeng.client.render.BenchTileTER;
import com.yuo.paimeng.client.render.BoarRender;
import com.yuo.paimeng.client.render.CraneRender;
import com.yuo.paimeng.client.render.HypostasisProjectileRenderer;
import com.yuo.paimeng.client.render.HypostasisRenderer;
import com.yuo.paimeng.client.render.SevenGodTileRender;
import com.yuo.paimeng.client.screen.*;
import com.yuo.paimeng.entity.PMEntityTypes;
import com.yuo.paimeng.items.PMItems;
import com.yuo.paimeng.items.relics.Relics;
import com.yuo.paimeng.items.relics.RelicsHelper;
import com.yuo.paimeng.menu.PMMenuTypes;
import com.yuo.paimeng.tiles.TileTypeRegistry;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

// 注意：这里不能用 @OnlyIn(Dist.CLIENT)，否则专用服务端在 DistExecutor.safeRunForDist 解析方法引用时会因为类被裁剪而崩溃。
public class ClientProxy extends CommonProxy{

    @Override
    public void registerHandlers(IEventBus modBus) {
        super.registerHandlers(modBus);
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::registerKeyMappings);
        //1.20.1 实体模型层必须在 mod 总线注册
        modBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) -> event.registerLayerDefinition(CraneModel.LAYER, CraneModel::createBodyLayer));
        modBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) -> event.registerLayerDefinition(BoarModel.LAYER, BoarModel::createBodyLayer));
        PMKeyManager.init();//快捷键注册
    }

    //1.20.1 快捷键需要在 RegisterKeyMappingsEvent 中注册
    private void registerKeyMappings(final RegisterKeyMappingsEvent event) {
        for (KeyMapping key : PMKeyManager.KEYS) {
            event.register(key);
        }
    }

    @SuppressWarnings("removal")
    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            setBowProperty(PMItems.pmBow0.get());
            setBowProperty(PMItems.pmBow1.get());
            setBowProperty(PMItems.pmBow2.get());
            setBowProperty(PMItems.pmBow3.get());
            setBowProperty(PMItems.pmBow4.get());
            ItemBlockRenderTypes.setRenderLayer(PMBlocks.cookingPot.get(), RenderType.cutoutMipped()); //不完整方块渲染
            ItemBlockRenderTypes.setRenderLayer(PMBlocks.cookingBench.get(), RenderType.cutout());
            ItemBlockRenderTypes.setRenderLayer(PMBlocks.zhusunCrop.get(), RenderType.cutout());
            BlockEntityRenderers.register(TileTypeRegistry.BENCH_TILE.get(), BenchTileTER::new); //特殊ter渲染
            BlockEntityRenderers.register(TileTypeRegistry.SEVEN_GOD.get(), SevenGodTileRender::new); //特殊ter渲染
            //实体渲染
            EntityRenderers.register(PMEntityTypes.BOAR.get(), BoarRender::new);
            EntityRenderers.register(PMEntityTypes.CRANE.get(), CraneRender::new);
            EntityRenderers.register(PMEntityTypes.PM_BALL.get(), ThrownItemRenderer::new);
            //无相之岩：外壳与核心直接按原版方块模型渲染，不走 EntityModel
            EntityRenderers.register(PMEntityTypes.HYPOSTASIS.get(), HypostasisRenderer::new);
            EntityRenderers.register(PMEntityTypes.HYPOSTASIS_PROJECTILE.get(), HypostasisProjectileRenderer::new);
            //绑定Container和ContainerScreen
            MenuScreens.register(PMMenuTypes.potContainer.get(), PotScreen::new);
            MenuScreens.register(PMMenuTypes.benchContainer.get(), BenchScreen::new);
            MenuScreens.register(PMMenuTypes.synPlatContainer.get(), SynPlatScreen::new);
            MenuScreens.register(PMMenuTypes.seedBoxContainer.get(), SeedBoxScreen::new);
            MenuScreens.register(PMMenuTypes.relicsContainer.get(), RelicsScreen::new);
            MenuScreens.register(PMMenuTypes.strengthenTableContainer.get(), StrengthenTableScreen::new);
            //透明方块的渲染
            for (RegistryObject<Block> r : PMBlocks.BLOCKS.getEntries()){
                if (r.get() instanceof BushBlock){
                    ItemBlockRenderTypes.setRenderLayer(r.get(), RenderType.cutout());
                }
            }
            //圣遗物
            for (RegistryObject<Item> entry : PMItems.ITEMS.getEntries()) {
                if (entry.get() instanceof Relics)
                    setRelicsProperty(entry.get());
            }
        });
    }

    //设置圣遗物的动态属性 根据不同部位更改贴图
    private void setRelicsProperty(Item item){
        ItemProperties.register(item, ModUtils.fa(
                "type"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return RelicsHelper.getTypeForStack(itemStack).getId();
            }
        });
    }

    //设置弓物品的动态属性
    private void setBowProperty(Item item){
        ItemProperties.register(item, ModUtils.fa(
                "pull"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getUseItem() != itemStack ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(item, ModUtils.fa(
                "pulling"), (itemStack, clientWorld, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);
    }

}
