package com.minecart.central_heater.misc;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class CreativeTab {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CentralHeater.MODID);

    public static final Supplier<CreativeModeTab> MAIN_TAB = CREATIVE_TABS.register("central_heater",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(AllBlockItem.BRICK_STOVE.get()))
                    .title(Component.translatable("central_heater.creativeTabs.central_heater"))
                    .displayItems((pParameters, pOutput) -> {
                        for (RegistryObject<Item> item : AllBlockItem.ITEMS.getEntries()) {
                            pOutput.accept(item.get());
                        }
                    })
                    .build());

    public static void register(IEventBus modEventBus){
        CREATIVE_TABS.register(modEventBus);
    }

    public static void addCreative(BuildCreativeModeTabContentsEvent event) {
    }
}