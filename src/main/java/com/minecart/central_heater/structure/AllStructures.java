package com.minecart.central_heater.structure;

import com.minecart.central_heater.CentralHeater;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;

import java.util.List;

public class AllStructures {

    // ResourceKeys for your custom pools (if you want to reference them as parents)
    public static final ResourceKey<StructureTemplatePool> PLAINS_HUGE_HOUSE = ResourceKey.create(Registries.TEMPLATE_POOL, new ResourceLocation(CentralHeater.MODID, "village/plains/houses"));

    public static void bootstrapPools(BootstapContext<StructureTemplatePool> context) {
        HolderGetter<StructureTemplatePool> pools = context.lookup(Registries.TEMPLATE_POOL);
        HolderGetter<StructureProcessorList> processors = context.lookup(Registries.PROCESSOR_LIST);

        // Reference to the 'empty' processor list
        Holder<StructureProcessorList> emptyProcessor = processors.getOrThrow(ResourceKey.create(Registries.PROCESSOR_LIST, new ResourceLocation("minecraft", "empty")));

        /* * If you want to define your own pools that vanilla pools can point to,
         * you register them here.
         * * Example: Registering a specific pool for your huge house
         */
        context.register(PLAINS_HUGE_HOUSE, new StructureTemplatePool(
                pools.getOrThrow(Pools.EMPTY), // Reference to parent pool (usually empty for terminal pieces)
                List.of(
                        Pair.of(
                                StructurePoolElement.legacy(CentralHeater.MODID + ":village/plains/houses/plains_huge_house_1", emptyProcessor),
                                1 // weight
                        )
                ),
                StructureTemplatePool.Projection.RIGID
        ));
    }
}