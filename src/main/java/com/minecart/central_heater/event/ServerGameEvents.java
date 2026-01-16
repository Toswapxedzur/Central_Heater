package com.minecart.central_heater.event;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.advancement.AllTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = CentralHeater.MODID)
public class ServerGameEvents {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (event.getEntity() instanceof ServerPlayer player && player.tickCount % 20 == 0) {
            if (player.getVehicle() instanceof AbstractMinecart minecart) {
                double speed = minecart.getDeltaMovement().length();
                AllTrigger.MINECART_SPEED.get().trigger(player, speed);
            }
        }
    }
}
