package com.minecart.central_heater.event.lifecycle_event;

import com.minecart.central_heater.AllBlockEntity;
import net.minecraft.core.Direction;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

@EventBusSubscriber
public class RegisterCapability {
    @SubscribeEvent
    public static void register(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.brick_pot_be.get(),
                 (entity, side) -> side.equals(Direction.DOWN) ? null : entity.getFluidTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.stone_pot_be.get(),
                (entity, side) -> side.equals(Direction.DOWN) ? null : entity.getFluidTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.nether_pot_be.get(),
                (entity, side) -> side.equals(Direction.DOWN) ? null : entity.getFluidTank());
    }
}
