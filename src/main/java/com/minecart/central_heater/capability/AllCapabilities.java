package com.minecart.central_heater.capability;

import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.item.AllDataComponents;
import net.minecraft.core.Direction;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.capability.templates.FluidHandlerItemStack;

@EventBusSubscriber
public class AllCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event){
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.brick_stove.get(),
                CapabilityFunction::stoveCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.stone_stove.get(),
                CapabilityFunction::stoveCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.red_nether_brick_stove.get(),
                CapabilityFunction::stoveCapability);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.pot.get(),
                (pot, side) -> pot.getContainer());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.brick_cauldron.get(),
                (pot, side) -> pot.getContainer());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.iron_cauldron.get(),
                (pot, side) -> pot.getContainer());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.golden_cauldron.get(),
                (pot, side) -> pot.getContainer());

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.pot.get(),
                (entity, side) -> side.equals(Direction.DOWN) ? null : entity.getFluidTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.brick_cauldron.get(),
                (entity, side) -> side.equals(Direction.DOWN) ? null : entity.getFluidTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.golden_cauldron.get(),
                (entity, side) -> side.equals(Direction.DOWN) ? null : entity.getFluidTank());

        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new PotionWrapper(stack), Items.GLASS_BOTTLE, Items.POTION);

        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidHandlerItemStack(AllDataComponents.SIMPLE_FLUID_CONTENT, stack, 500), AllBlockItem.STURDY_TANK.asItem());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.sturdy_tank.get(),
                (entity, side) -> entity.getTank());
    }
}
