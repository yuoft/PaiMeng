package com.yuo.PaiMeng;

import com.yuo.PaiMeng.Blocks.PMBlocks;
import com.yuo.PaiMeng.Client.Render.SevenGodTileRender;
import com.yuo.PaiMeng.Container.ContainerTypeRegistry;
import com.yuo.PaiMeng.Entity.EntityTypeRegister;
import com.yuo.PaiMeng.Client.Render.BoarRender;
import com.yuo.PaiMeng.Client.Render.CraneRender;
import com.yuo.PaiMeng.Client.Gui.*;
import com.yuo.PaiMeng.Items.PMItems;
import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsHelper;
import com.yuo.PaiMeng.Client.Render.BenchTileTER;
import com.yuo.PaiMeng.Tiles.TileTypeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BushBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@OnlyIn(Dist.CLIENT)
public class ClientProxy extends CommonProxy{
    private TileEntity referencedTE = null;

    public TileEntity getRefrencedTE() {
        return referencedTE;
    }

    public void setRefrencedTE(TileEntity tileEntity) {
        referencedTE = tileEntity;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void init() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::clientSetup);
        PMKeyManager.init();//快捷键注册
    }

    private  void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            setBowProperty(PMItems.pmBow0.get());
            setBowProperty(PMItems.pmBow1.get());
            setBowProperty(PMItems.pmBow2.get());
            setBowProperty(PMItems.pmBow3.get());
            setBowProperty(PMItems.pmBow4.get());
            RenderTypeLookup.setRenderLayer(PMBlocks.cookingPot.get(), RenderType.getCutoutMipped()); //不完整方块渲染
            RenderTypeLookup.setRenderLayer(PMBlocks.cookingBench.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(PMBlocks.zhusunCrop.get(), RenderType.getCutout());
            ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.BENCH_TILE.get(), BenchTileTER::new); //特殊ter渲染
            ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.SEVEN_GOD.get(), SevenGodTileRender::new); //特殊ter渲染
        });
        //实体渲染
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegister.BOAR.get(), BoarRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegister.CRANE.get(), CraneRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegister.PM_BALL.get(), manager -> new SpriteRenderer<>(manager, Minecraft.getInstance().getItemRenderer()));
        //绑定Container和ContainerScreen
        event.enqueueWork(() -> {
            ScreenManager.registerFactory(ContainerTypeRegistry.potContainer.get(), PotScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.benchContainer.get(), BenchScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.synPlatContainer.get(), SynPlatScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.seedBoxContainer.get(), SeedBoxScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.relicsContainer.get(), RelicsScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.strengthenTableContainer.get(), StrengthenTableScreen::new);
        });
        //透明方块的渲染
        for (RegistryObject<Block> r : PMBlocks.BLOCKS.getEntries()){
            if (r.get() instanceof BushBlock){
                RenderTypeLookup.setRenderLayer(r.get(), RenderType.getCutout());
            }
        }
        //圣遗物
        event.enqueueWork( () ->{
            for (RegistryObject<Item> entry : PMItems.ITEMS.getEntries()) {
                if (entry.get() instanceof Relics)
                    setRelicsProperty(entry.get());
            }
        });
    }

    //设置圣遗物的动态属性 根据不同部位更改贴图
    private void setRelicsProperty(Item item){
        ItemModelsProperties.registerProperty(item, new ResourceLocation(PaiMeng.MOD_ID,
                "type"), (itemStack, clientWorld, livingEntity) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return RelicsHelper.getTypeForStack(itemStack).getId();
            }
        });
    }

    //设置弓物品的动态属性
    private void setBowProperty(Item item){
        ItemModelsProperties.registerProperty(item, new ResourceLocation(PaiMeng.MOD_ID,
                "pull"), (itemStack, clientWorld, livingEntity) -> {
            if (livingEntity == null) {
                return 0.0F;
            } else {
                return livingEntity.getActiveItemStack() != itemStack ? 0.0F : (float)(itemStack.getUseDuration() - livingEntity.getItemInUseCount()) / 20.0F;
            }
        });
        ItemModelsProperties.registerProperty(item, new ResourceLocation(PaiMeng.MOD_ID,
                "pulling"), (itemStack, clientWorld, livingEntity) -> livingEntity != null && livingEntity.isHandActive() && livingEntity.getActiveItemStack() == itemStack ? 1.0F : 0.0F);
    }

}
