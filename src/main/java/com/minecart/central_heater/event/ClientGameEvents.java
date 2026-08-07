package com.minecart.central_heater.event;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.client.ClientSimilarStackControls;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ScreenEvent;

@EventBusSubscriber(modid = CentralHeater.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.GAME)
public class ClientGameEvents {
    @SubscribeEvent
    public static void onKeyPressed(ScreenEvent.KeyPressed.Pre event) {
        ClientSimilarStackControls.onKeyPressed(event);
    }
}
