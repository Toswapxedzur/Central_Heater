package com.minecart.central_heater.item;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.furnace.FurnaceFuelBurnTimeEvent;

@EventBusSubscriber
public class RegisterBurnTime {
    @SubscribeEvent
    public static void onFuelBurnTime(FurnaceFuelBurnTimeEvent event){
    }
}
