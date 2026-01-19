package com.minecart.central_heater.structure;

import com.minecart.central_heater.CentralHeater;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(modid = CentralHeater.MODID)
public class StructureAddition {
    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(
            Registries.PROCESSOR_LIST,
            new ResourceLocation("minecraft", "empty")
    );

    @SubscribeEvent
    public static void add(ServerAboutToStartEvent event) {
        MinecraftServer server = event.getServer();
        Registry<StructureTemplatePool> registry = server.registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
        Registry<StructureProcessorList> processorListRegistry = server.registryAccess().registryOrThrow(Registries.PROCESSOR_LIST);

        // Fetch the pools
        StructureTemplatePool plainsPool = registry.get(new ResourceLocation("minecraft", "village/plains/houses"));
        StructureTemplatePool taigaPool = registry.get(new ResourceLocation("minecraft", "village/taiga/houses"));
        StructureTemplatePool snowyPool = registry.get(new ResourceLocation("minecraft", "village/snowy/houses"));

        if (plainsPool != null) {
            Holder<StructureProcessorList> emptyProcessor = processorListRegistry.getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY);

            // Add Plains Houses
            addHouseToPool(plainsPool, "village/plains/houses/plains_huge_house_1", emptyProcessor, 2);
            // Add Taiga Houses
            addHouseToPool(taigaPool, "village/taiga/houses/taiga_huge_house_1", emptyProcessor, 2);
            // Add Snowy Houses
            addHouseToPool(snowyPool, "village/snowy/houses/snowy_huge_house_1", emptyProcessor, 2);
        }
    }

    /**
     * Helper to inject elements into an existing pool.
     * Note: This requires templates and rawTemplates to be non-final via Access Transformer.
     */
    private static void addHouseToPool(StructureTemplatePool pool, String nbtPath, Holder<StructureProcessorList> processor, int weight) {
        if (pool == null) return;

        StructurePoolElement element = SinglePoolElement.legacy(CentralHeater.MODID + ":" + nbtPath, processor)
                .apply(StructureTemplatePool.Projection.RIGID);

        // 1. Add to the active templates list (used for actual generation)
        for (int i = 0; i < weight; i++) {
            pool.templates.add(element);
        }

        // 2. Add to the raw templates list (to maintain consistency if re-evaluated)
        List<Pair<StructurePoolElement, Integer>> rawList = new ArrayList<>(pool.rawTemplates);
        rawList.add(Pair.of(element, weight));
        pool.rawTemplates = rawList;
    }
}