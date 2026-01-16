package com.minecart.central_heater.event;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.world.poi.ExtendPoiTypesEvent;

@EventBusSubscriber
public class ServerModEvents {
    @SubscribeEvent
    public static void onExtendPoiTypes(ExtendPoiTypesEvent event) {
        event.addBlockToPoi(PoiTypes.LEATHERWORKER, AllBlockItem.BRICK_CAULDRON.get());
        event.addBlockToPoi(PoiTypes.LEATHERWORKER, AllBlockItem.IRON_CAULDRON.get());
        event.addBlockToPoi(PoiTypes.LEATHERWORKER, AllBlockItem.GOLDEN_CAULDRON.get());
    }
}
