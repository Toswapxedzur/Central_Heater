package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllStructures;
import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.item.AllTrimMaterials;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class GeneratorDataRegistries extends DatapackBuiltinEntriesProvider {
    public GeneratorDataRegistries(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, BUILDER, Set.of(Central_heater.MODID));
    }

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.TEMPLATE_POOL, AllStructures::bootstrapPools)
            .add(Registries.TRIM_MATERIAL, AllTrimMaterials::bootstrap);
}
