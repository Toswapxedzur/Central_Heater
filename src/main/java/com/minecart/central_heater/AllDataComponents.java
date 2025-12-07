package com.minecart.central_heater;

import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AllDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(Central_heater.MODID);

    public static final Supplier<DataComponentType<SimpleFluidContent>> SIMPLE_FLUID_CONTENT = DATA_COMPONENTS.registerComponentType("simple_fluid_content",
            builder -> builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC).cacheEncoding());

    public static void register(IEventBus modEventBus){
        DATA_COMPONENTS.register(modEventBus);
    }
}
