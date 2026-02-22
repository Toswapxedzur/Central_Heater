package com.minecart.central_heater.capability;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Mod.EventBusSubscriber
public class AllCapabilities {
    @SubscribeEvent
    public static void registerCapabilities(AttachCapabilitiesEvent<ItemStack> event){
        ItemStack stack = event.getObject();

        if (stack.is(Items.GLASS_BOTTLE) || stack.is(Items.POTION)) {
            event.addCapability(new ResourceLocation(CentralHeater.MODID, "potion_fluid"), new ICapabilityProvider() {
                final LazyOptional<IFluidHandlerItem> cap = LazyOptional.of(() -> new PotionWrapper(stack));
                @Override
                public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
                    return capability == ForgeCapabilities.FLUID_HANDLER_ITEM ? cap.cast() : LazyOptional.empty();
                }
            });
        }

        if (stack.is(AllBlockItem.STURDY_TANK.get().asItem())) {
            event.addCapability(new ResourceLocation(CentralHeater.MODID, "tank_fluid"), new ICapabilityProvider() {
                final LazyOptional<IFluidHandlerItem> cap = LazyOptional.of(() -> new FluidHandlerItemStack(stack, 500));
                @Override
                public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> capability, @Nullable Direction arg) {
                    return capability == ForgeCapabilities.FLUID_HANDLER_ITEM ? cap.cast() : LazyOptional.empty();
                }
            });
        }
    }
}