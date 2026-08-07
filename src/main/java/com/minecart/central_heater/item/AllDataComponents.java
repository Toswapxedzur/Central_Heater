package com.minecart.central_heater.item;

import com.minecart.central_heater.CentralHeater;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AllDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister.createDataComponents(CentralHeater.MODID);

    public static final Supplier<DataComponentType<SimpleFluidContent>> SIMPLE_FLUID_CONTENT = DATA_COMPONENTS.registerComponentType("simple_fluid_content",
            builder -> builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC).cacheEncoding());

    public static final Supplier<DataComponentType<ItemContainerContents>> SIMILAR_STACK_CONTENTS = DATA_COMPONENTS.registerComponentType("similar_stack_contents",
            builder -> builder.persistent(ItemContainerContents.CODEC).networkSynchronized(ItemContainerContents.STREAM_CODEC).cacheEncoding());

    public static void register(IEventBus modEventBus){
        DATA_COMPONENTS.register(modEventBus);
    }
}
