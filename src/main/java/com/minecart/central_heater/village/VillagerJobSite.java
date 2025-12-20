package com.minecart.central_heater.village;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.world.poi.ExtendPoiTypesEvent;

import java.util.Set;

@EventBusSubscriber
public class VillagerJobSite {
    @SubscribeEvent
    public static void onExtendPoiTypes(ExtendPoiTypesEvent event) {
        event.addBlockToPoi(PoiTypes.LEATHERWORKER, AllBlockItem.iron_cauldron.get());
    }
}
