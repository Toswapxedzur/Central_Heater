package com.minecart.central_heater.entity;

import com.minecart.central_heater.CentralHeater;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllEntity {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES;

    public static final DeferredHolder<EntityType<?>, EntityType<ThrowablePebbleEntity>> PEBBLE;
    public static final DeferredHolder<EntityType<?>, EntityType<MinecartBlazingFurnace>> BLAZING_FURNACE_MINECART;

    static {
        ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, CentralHeater.MODID);

        PEBBLE = ENTITY_TYPES.register("pebble", ()-> EntityType.Builder.<ThrowablePebbleEntity>of(
                ThrowablePebbleEntity::new,
                MobCategory.MISC
        ).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("pebble"));

        BLAZING_FURNACE_MINECART = ENTITY_TYPES.register("blazing_furnace_minecart",
                () -> EntityType.Builder.<MinecartBlazingFurnace>of(
                                MinecartBlazingFurnace::new,
                                MobCategory.MISC)
                        .sized(0.98F, 0.7F)
                        .clientTrackingRange(8)
                        .build("blazing_furnace_minecart")
        );
    }

    public static void register(IEventBus modEventBus){
        ENTITY_TYPES.register(modEventBus);
    }
}
