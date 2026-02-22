package com.minecart.central_heater;

import com.minecart.central_heater.advancement.AllTrigger;
import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.entity.AllEntity;
import com.minecart.central_heater.misc.Config;
import com.minecart.central_heater.misc.CreativeTab;
import com.minecart.central_heater.misc.DataMapHook;
import com.minecart.central_heater.misc.NewCauldronInteraction;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.user_interface.AllMenu;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CentralHeater.MODID)
public class CentralHeater {

    public static final String MODID = "central_heater";

    public static final Logger LOGGER = LogUtils.getLogger();

    public CentralHeater() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);

        CreativeTab.register(modEventBus);

        AllBlockItem.register(modEventBus);

        AllBlockEntity.register(modEventBus);

        AllRecipe.register(modEventBus);

        AllEntity.register(modEventBus);

        AllMenu.register(modEventBus);

        modEventBus.addListener(CreativeTab::addCreative);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    public static ResourceLocation modLoc(String path){
        return new ResourceLocation(MODID, path);
    }
}
