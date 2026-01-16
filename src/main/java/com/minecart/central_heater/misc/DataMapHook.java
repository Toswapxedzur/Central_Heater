package com.minecart.central_heater.misc;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber
public class DataMapHook {
    public static final DataMapType<Item, Integer> NETHER_FUEL_MAPS = DataMapType.builder(
            CentralHeater.modLoc("nether_fuel"),
            Registries.ITEM,
            Codec.INT
    ).build();

    public static final DataMapType<Item, Float> FIRE_ASH_DROP_CHANCE = DataMapType.builder(
            CentralHeater.modLoc("fire_ash_drop_chance"),
            Registries.ITEM,
            Codec.FLOAT
    ).build();

    public static final DataMapType<Item, Float> SCORCHED_DUST_DROP_CHANCE = DataMapType.builder(
            CentralHeater.modLoc("scorched_dust_drop_chance"),
            Registries.ITEM,
            Codec.FLOAT
    ).build();

    @SubscribeEvent
    public static void registerDataMapTypes(RegisterDataMapTypesEvent event){
        event.register(NETHER_FUEL_MAPS);
        event.register(FIRE_ASH_DROP_CHANCE);
        event.register(SCORCHED_DUST_DROP_CHANCE);
    }

    public static <R> R getOrDefault(DataMapType<Item, R> data, ItemStack key, R defaultValue){
        Holder<Item> holder = key.getItemHolder();
        R value = holder.getData(data);
        if(value != null)
            return value;
        return defaultValue;
    }

    public static int getNetherFuelBurnTime(ItemStack stack){
        return getOrDefault(NETHER_FUEL_MAPS, stack, 0);
    }

    public static float getFireAshDropChance(ItemStack stack){
        if(stack.getBurnTime(RecipeType.SMELTING) == 0)
            return 0f;
        return getOrDefault(FIRE_ASH_DROP_CHANCE, stack, AllBlockItem.DEFAULT_FIRE_ASH_DROP_CHANCE);
    }

    public static float getScorchedDustDropChance(ItemStack stack){
        if(getNetherFuelBurnTime(stack) == 0)
            return 0f;
        return getOrDefault(SCORCHED_DUST_DROP_CHANCE, stack, AllBlockItem.DEFAULT_SCORCHED_DUST_DROP_CHANCE);
    }
}
