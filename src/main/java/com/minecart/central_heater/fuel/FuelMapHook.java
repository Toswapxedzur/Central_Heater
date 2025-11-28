package com.minecart.central_heater.fuel;

import com.minecart.central_heater.Central_heater;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber
public class FuelMapHook {
    public static final DataMapType<Item, Integer> netherFuelMaps = DataMapType.builder(
            Central_heater.modLoc("nether_fuel"),
            Registries.ITEM,
            Codec.INT
    ).build();

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event){
        event.register(netherFuelMaps);
    }

    public static int getBurnTime(ItemStack stack){
        Holder<Item> holder = stack.getItemHolder();
        Integer burnTime = holder.getData(netherFuelMaps);
        if(burnTime == null)
            return 0;
        else
            return burnTime;
    }
}
