package com.minecart.central_heater.entity;

import com.minecart.central_heater.CentralHeater;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class AllEntity {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CentralHeater.MODID);

    public static final RegistryObject<EntityType<ThrowablePebbleEntity>> PEBBLE = ENTITY_TYPES.register("pebble", () -> EntityType.Builder.<ThrowablePebbleEntity>of(
            ThrowablePebbleEntity::new,
            MobCategory.MISC
    ).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("pebble"));

    public static final RegistryObject<EntityType<MinecartBlazingFurnace>> BLAZING_FURNACE_MINECART = ENTITY_TYPES.register("blazing_furnace_minecart",
            () -> EntityType.Builder.<MinecartBlazingFurnace>of(
                            MinecartBlazingFurnace::new,
                            MobCategory.MISC)
                    .sized(0.98F, 0.7F)
                    .clientTrackingRange(8)
                    .build("blazing_furnace_minecart")
    );

    public static void register(IEventBus modEventBus){
        ENTITY_TYPES.register(modEventBus);
    }
}