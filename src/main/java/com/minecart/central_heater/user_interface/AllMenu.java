package com.minecart.central_heater.user_interface;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.user_interface.menu.BlazingFurnaceMenu;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllMenu {
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.MENU_TYPES, CentralHeater.MODID);

    public static final RegistryObject<MenuType<BlazingFurnaceMenu>> BLAZING_FURNACE = MENU_TYPES.register("blazing_furnace",
            () -> new MenuType<>(BlazingFurnaceMenu::new, FeatureFlags.VANILLA_SET));

    public static void register(IEventBus modEventBus){
        MENU_TYPES.register(modEventBus);
    }
}