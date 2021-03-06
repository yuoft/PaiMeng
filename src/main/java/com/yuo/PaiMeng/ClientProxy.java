package com.yuo.PaiMeng;

import com.yuo.PaiMeng.Blocks.BlockRegistry;
import com.yuo.PaiMeng.Container.ContainerTypeRegistry;
import com.yuo.PaiMeng.Entity.EntityTypeRegister;
import com.yuo.PaiMeng.Entity.Render.BoarRender;
import com.yuo.PaiMeng.Entity.Render.CraneRender;
import com.yuo.PaiMeng.Gui.*;
import com.yuo.PaiMeng.Items.ItemRegistry;
import com.yuo.PaiMeng.Items.Relics;
import com.yuo.PaiMeng.Items.RelicsHelper;
import com.yuo.PaiMeng.Render.BenchTileTER;
import com.yuo.PaiMeng.Tiles.TileTypeRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.BushBlock;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemModelsProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

public class ClientProxy extends CommonProxy{
    public static final KeyBinding KEY_OPEN_RELICS = new KeyBinding("key.paimeng", GLFW.GLFW_KEY_P, "key.category.openRelics");

    private TileEntity referencedTE = null;

    public TileEntity getRefrencedTE() {
        return referencedTE;
    }

    public void setRefrencedTE(TileEntity tileEntity) {
        referencedTE = tileEntity;
    }

    @Override
    public void init() {
        IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(this::clientSetup);
    }

    private  void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            RenderTypeLookup.setRenderLayer(BlockRegistry.cookingPot.get(), RenderType.getCutoutMipped()); //?????????????????????
            RenderTypeLookup.setRenderLayer(BlockRegistry.cookingBench.get(), RenderType.getCutout());
            RenderTypeLookup.setRenderLayer(BlockRegistry.zhusunCrop.get(), RenderType.getCutout());
            ClientRegistry.bindTileEntityRenderer(TileTypeRegistry.BENCH_TILE.get(), BenchTileTER::new); //??????ter??????
        });
        //????????????
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegister.BOAR.get(), BoarRender::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypeRegister.CRANE.get(), CraneRender::new);
        //??????Container???ContainerScreen
        event.enqueueWork(() -> {
            ScreenManager.registerFactory(ContainerTypeRegistry.potContainer.get(), PotScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.benchContainer.get(), BenchScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.synPlatContainer.get(), SynPlatScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.seedBoxContainer.get(), SeedBoxScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.relicsContainer.get(), RelicsScreen::new);
            ScreenManager.registerFactory(ContainerTypeRegistry.strengthenTableContainer.get(), StrengthenTableScreen::new);
        });
        //?????????????????????
        for (RegistryObject<Block> r : BlockRegistry.BLOCKS.getEntries()){
            if (r.get() instanceof BushBlock){
                RenderTypeLookup.setRenderLayer(r.get(), RenderType.getCutout());
            }
        }
        //?????????
        event.enqueueWork( () ->{
            for (RegistryObject<Item> entry : ItemRegistry.ITEMS.getEntries()) {
                if (entry.get() instanceof Relics)
                    setRelicsProperty(entry.get());
            }
        });
    }

    //?????????????????????????????? ??????????????????????????????
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

}
