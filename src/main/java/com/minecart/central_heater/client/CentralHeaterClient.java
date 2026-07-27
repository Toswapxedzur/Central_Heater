package com.minecart.central_heater.client;

import com.minecart.central_heater.CentralHeater;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;

@Mod(value = CentralHeater.MODID, dist = Dist.CLIENT)
public class CentralHeaterClient {
    public CentralHeaterClient(ModContainer modContainer, IEventBus modEventBus) {
        NeoForge.EVENT_BUS.addListener(ClientHeatDebugRenderer::renderHeatLabels);
    }
}
