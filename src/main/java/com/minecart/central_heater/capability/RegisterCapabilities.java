package com.minecart.central_heater.capability;

import com.minecart.central_heater.AllBlockEntity;
import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.AllDataComponents;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;

@EventBusSubscriber
public class RegisterCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.pot.get(),
                (entity, side) -> side.equals(Direction.DOWN) ? null : entity.getFluidTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.stone_pot.get(),
                (entity, side) -> side.equals(Direction.DOWN) ? null : entity.getFluidTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.golden_cauldron.get(),
                (entity, side) -> side.equals(Direction.DOWN) ? null : entity.getFluidTank());

        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new PotionWrapper(stack), Items.GLASS_BOTTLE, Items.POTION);

        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidHandlerItemStack(AllDataComponents.SIMPLE_FLUID_CONTENT, stack, 500), AllBlockItem.sturdy_tank.asItem());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.sturdy_tank.get(),
                (entity, side) -> entity.getTank());
    }
}
