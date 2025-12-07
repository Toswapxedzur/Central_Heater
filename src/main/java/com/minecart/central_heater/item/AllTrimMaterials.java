package com.minecart.central_heater.item;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.Central_heater;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.minecraft.world.item.armortrim.TrimMaterials;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Map;

public class AllTrimMaterials {
    public static final ResourceKey<TrimMaterial> STURDY_KEY = ResourceKey.create(Registries.TRIM_MATERIAL, Central_heater.modLoc("sturdy"));

    public static final TrimMaterial STURDY = TrimMaterial.create(STURDY_KEY.location().getPath(),
            AllBlockItem.sturdy_brick.asItem(), 1.1f, Component.translatable("central_heater.trim_material.sturdy").withStyle(Style.EMPTY.withColor(0x0b120c)), Map.of());

    public static void bootstrap(BootstrapContext<TrimMaterial> context){
        context.register(STURDY_KEY, STURDY);
    }
}
