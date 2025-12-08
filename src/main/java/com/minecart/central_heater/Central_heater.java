package com.minecart.central_heater;

import com.minecart.central_heater.block_entity_renderer.pot.BrickPotBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.pot.CauldronBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.pot.GoldenCauldronBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.pot.StonePotBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.stove.BrickStoveBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.BurnableCampfireBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.stove.GoldenStoveBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.stove.StoneStoveBlockEntityRenderer;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;


@Mod(Central_heater.MODID)
public class Central_heater {

    public static final String MODID = "central_heater";

    private static final Logger LOGGER = LogUtils.getLogger();

    public Central_heater(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);

        NeoForge.EVENT_BUS.register(this);

        CreativeTab.register(modEventBus);

        AllBlockItem.register(modEventBus);

        AllBlockEntity.register(modEventBus);

        AllRecipe.register(modEventBus);

        AllDataComponents.register(modEventBus);

        modEventBus.addListener(CreativeTab::addCreative);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
        }

        @SubscribeEvent
        public static void onRegisterBlockEntityRenderer(EntityRenderersEvent.RegisterRenderers event){
            event.registerBlockEntityRenderer(AllBlockEntity.stone_stove.get(), StoneStoveBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntity.red_nether_brick_stove.get(), GoldenStoveBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntity.brick_stove.get(), BrickStoveBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntity.burnable_campfire.get(), BurnableCampfireBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntity.pot.get(), BrickPotBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntity.stone_pot.get(), StonePotBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntity.iron_cauldron.get(), CauldronBlockEntityRenderer::new);
            event.registerBlockEntityRenderer(AllBlockEntity.golden_cauldron.get(), GoldenCauldronBlockEntityRenderer::new);
        }
    }

    public static ResourceLocation modLoc(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
