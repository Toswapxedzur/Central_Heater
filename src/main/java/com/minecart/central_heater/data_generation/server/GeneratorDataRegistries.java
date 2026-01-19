package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.structure.AllStructures;
import com.minecart.central_heater.item.AllTrimMaterials;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GeneratorDataRegistries extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.TEMPLATE_POOL, AllStructures::bootstrapPools);

    public GeneratorDataRegistries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        // Forge 1.20.1 constructor signature
        super(output, registries, BUILDER, Set.of(CentralHeater.MODID));
    }
}