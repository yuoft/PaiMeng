package com.yuo.PaiMeng;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Client.Gui.BenchScreen;
import com.yuo.PaiMeng.Client.Gui.PotScreen;
import com.yuo.PaiMeng.Client.Gui.RelicsScreen;
import com.yuo.PaiMeng.Client.Gui.SeedBoxScreen;
import com.yuo.PaiMeng.Client.Gui.StrengthenTableScreen;
import com.yuo.PaiMeng.Client.Gui.SynPlatScreen;
import com.yuo.PaiMeng.Client.Model.CraneModel;
import com.yuo.PaiMeng.Client.Render.BenchTileTER;
import com.yuo.PaiMeng.Client.Render.BoarRender;
import com.yuo.PaiMeng.Client.Render.CraneRender;
import com.yuo.PaiMeng.Client.Render.SevenGodTileRender;
import com.yuo.PaiMeng.Container.ContainerTypeRegistry;
import com.yuo.PaiMeng.Entity.EntityTypeRegister;
import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsHelper;
import com.yuo.PaiMeng.Tiles.TileTypeRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.BushBlock;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.RegistryObject;

// 注意：这里不能用 @OnlyIn(Dist.CLIENT)，否则专用服务端在 DistExecutor.safeRunForDist 解析方法引用时会因为类被裁剪而崩溃。
public class ClientProxy extends CommonProxy{
    private BlockEntity referencedTE = null;

    public BlockEntity getRefrencedTE() {
        return referencedTE;
    }

    public void setRefrencedTE(BlockEntity tileEntity) {
        referencedTE = tileEntity;
    }

    @SuppressWarnings("removal")
    @Override
    public void init() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::registerKeyMappings);
        //1.20.1 实体模型层必须在 mod 总线注册
        modBus.addListener((EntityRenderersEvent.RegisterLayerDefinitions event) -> event.registerLayerDefinition(CraneModel.LAYER, CraneModel::createBodyLayer));
        PMKeyManager.init();//快捷键注册
    }

    //1.20.1 快捷键需要在 RegisterKeyMappingsEvent 中注册
    private void registerKeyMappings(final RegisterKeyMappingsEvent event) {
        for (KeyMapping key : PMKeyManager.KEYS) {
            event.register(key);
        }
    }

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
            EntityRenderers.register(EntityTypeRegister.BOAR.get(), BoarRender::new);
            EntityRenderers.register(EntityTypeRegister.CRANE.get(), CraneRender::new);
            EntityRenderers.register(EntityTypeRegister.PM_BALL.get(), ThrownItemRenderer::new);
            //绑定Container和ContainerScreen
            MenuScreens.register(ContainerTypeRegistry.potContainer.get(), PotScreen::new);
            MenuScreens.register(ContainerTypeRegistry.benchContainer.get(), BenchScreen::new);
            MenuScreens.register(ContainerTypeRegistry.synPlatContainer.get(), SynPlatScreen::new);
            MenuScreens.register(ContainerTypeRegistry.seedBoxContainer.get(), SeedBoxScreen::new);
            MenuScreens.register(ContainerTypeRegistry.relicsContainer.get(), RelicsScreen::new);
            MenuScreens.register(ContainerTypeRegistry.strengthenTableContainer.get(), StrengthenTableScreen::new);
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
        ItemProperties.register(item, new ResourceLocation(PaiMeng.MOD_ID,
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
        ItemProperties.register(item, new ResourceLocation(PaiMeng.MOD_ID,
                "pull"), (itemStack, clientWorld, livingEntity, seed) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getUseItem() != itemStack ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getUseItemRemainingTicks()) / 20.0F;
            }
        });
        ItemProperties.register(item, new ResourceLocation(PaiMeng.MOD_ID,
                "pulling"), (itemStack, clientWorld, livingEntity, seed) -> livingEntity != null && livingEntity.isUsingItem() && livingEntity.getUseItem() == itemStack ? 1.0F : 0.0F);
    }

}
