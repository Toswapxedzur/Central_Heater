package com.minecart.central_heater.item;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.armortrim.TrimMaterial;

import java.util.Map;

public class AllTrimMaterials {
    public static final ResourceKey<TrimMaterial> STURDY_KEY = ResourceKey.create(Registries.TRIM_MATERIAL, CentralHeater.modLoc("sturdy"));

    public static final TrimMaterial STURDY = TrimMaterial.create(STURDY_KEY.location().getPath(),
            AllBlockItem.STURDY_BRICK.asItem(), 1.1f, Component.translatable("central_heater.trim_material.sturdy").withStyle(Style.EMPTY.withColor(0x0b120c)), Map.of());

    public static void bootstrap(BootstrapContext<TrimMaterial> context){
        context.register(STURDY_KEY, STURDY);
    }
}
