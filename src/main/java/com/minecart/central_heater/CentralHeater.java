package com.minecart.central_heater;

import com.minecart.central_heater.advancement.AllTrigger;
import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.entity.AllEntity;
import com.minecart.central_heater.heat.HeatAttachments;
import com.minecart.central_heater.item.AllDataComponents;
import com.minecart.central_heater.misc.Config;
import com.minecart.central_heater.misc.CreativeTab;
import com.minecart.central_heater.misc.NewCauldronInteraction;
import com.minecart.central_heater.network.AllNetwork;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.user_interface.AllMenu;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;


@Mod(CentralHeater.MODID)
public class CentralHeater {

    public static final String MODID = "central_heater";

    private static final Logger LOGGER = LogUtils.getLogger();

    public CentralHeater(IEventBus modEventBus, ModContainer modContainer) {

        modEventBus.addListener(this::commonSetup);

        CreativeTab.register(modEventBus);

        AllBlockItem.register(modEventBus);

        AllBlockEntity.register(modEventBus);

        AllRecipe.register(modEventBus);

        AllDataComponents.register(modEventBus);

        AllEntity.register(modEventBus);

        AllMenu.register(modEventBus);

        AllTrigger.register(modEventBus);

        HeatAttachments.register(modEventBus);

        modEventBus.addListener(CreativeTab::addCreative);
        modEventBus.addListener(AllNetwork::register);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        NewCauldronInteraction.bootStrap();
    }

    public static ResourceLocation modLoc(String path){
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
