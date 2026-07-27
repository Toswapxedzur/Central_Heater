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
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.BRICK_STOVE.get(),
                CapabilityFunction::stoveCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.STONE_STOVE.get(),
                CapabilityFunction::stoveCapability);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.RED_NETHER_BRICK_STOVE.get(),
                CapabilityFunction::stoveCapability);

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.MUD_BRICK_POT.get(),
                (pot, side) -> pot.getContainer());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.BRICK_CAULDRON.get(),
                (pot, side) -> pot.getContainer());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.IRON_CAULDRON.get(),
                (pot, side) -> pot.getContainer());
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.GOLDEN_CAULDRON.get(),
                (pot, side) -> pot.getContainer());

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AllBlockEntity.ASHTRAY.get(),
                (tray, side) -> tray.inventory);

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.MUD_BRICK_POT.get(),
                (entity, side) -> side.equals(Direction.DOWN) ? null : entity.getFluidTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.BRICK_CAULDRON.get(),
                (entity, side) -> side.equals(Direction.DOWN) ? null : entity.getFluidTank());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.GOLDEN_CAULDRON.get(),
                (entity, side) -> side.equals(Direction.DOWN) ? null : entity.getFluidTank());

        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new PotionWrapper(stack), Items.GLASS_BOTTLE, Items.POTION);

        event.registerItem(Capabilities.FluidHandler.ITEM, (stack, ctx) -> new FluidHandlerItemStack(AllDataComponents.SIMPLE_FLUID_CONTENT, stack, 500), AllBlockItem.STURDY_TANK.asItem());
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AllBlockEntity.STURDY_TANK.get(),
                (entity, side) -> entity.getTank());
    }
}
