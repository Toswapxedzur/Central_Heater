package com.minecart.central_heater.user_interface;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.user_interface.menu.BlazingFurnaceMenu;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class AllMenu {
    public static DeferredRegister<MenuType<?>> MENU_TYPES;

    public static Supplier<MenuType<BlazingFurnaceMenu>> BLAZING_FURNACE;

    static {
        MENU_TYPES = DeferredRegister.create(Registries.MENU, CentralHeater.MODID);

        BLAZING_FURNACE = MENU_TYPES.register("blazing_furnace", ()->new MenuType<>(BlazingFurnaceMenu::new, FeatureFlags.DEFAULT_FLAGS));
    }

    public static void register(IEventBus modEventBus){
        MENU_TYPES.register(modEventBus);
    }
}
