package com.minecart.central_heater.structure;

import com.minecart.central_heater.CentralHeater;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.level.levelgen.structure.pools.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructurePoolElement;
import net.minecraft.world.level.levelgen.structure.pools.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.server.ServerAboutToStartEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber
public class StructureAddition {
    private static final ResourceKey<StructureProcessorList> EMPTY_PROCESSOR_LIST_KEY = ResourceKey.create(
            Registries.PROCESSOR_LIST,
            ResourceLocation.withDefaultNamespace("empty")
    );
    @SubscribeEvent
    public static void add(ServerAboutToStartEvent event){
        MinecraftServer server = event.getServer();
        Registry<StructureTemplatePool> registry = event.getServer().registryAccess().registryOrThrow(Registries.TEMPLATE_POOL);
        Registry<StructureProcessorList> processorListRegistry = server.registryAccess().registryOrThrow(Registries.PROCESSOR_LIST);

        StructureTemplatePool vanillaPlainsVillageHousePool = registry.get(ResourceLocation.withDefaultNamespace("village/plains/houses"));
        StructureTemplatePool vanillaTaigaVillageHousePool = registry.get(ResourceLocation.withDefaultNamespace("village/taiga/houses"));
        StructureTemplatePool vanillaSnowyVillageHousePool = registry.get(ResourceLocation.withDefaultNamespace("village/snowy/houses"));
        if (vanillaPlainsVillageHousePool != null){
            List<Pair<StructurePoolElement, Integer>> newPlainsVillage = new ArrayList<>();
            List<Pair<StructurePoolElement, Integer>> newTaigaVillage = new ArrayList<>();
            List<Pair<StructurePoolElement, Integer>> newSnowyVillage = new ArrayList<>();

            newPlainsVillage.add(Pair.of(SinglePoolElement.legacy(CentralHeater.modLoc("village/plains/houses/plains_huge_house_1").toString(),
                    processorListRegistry.getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY)).apply(StructureTemplatePool.Projection.RIGID), 2));
            newTaigaVillage.add(Pair.of(SinglePoolElement.legacy(CentralHeater.modLoc("village/taiga/houses/taiga_huge_house_1").toString(),
                    processorListRegistry.getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY)).apply(StructureTemplatePool.Projection.RIGID), 2));
            newSnowyVillage.add(Pair.of(SinglePoolElement.legacy(CentralHeater.modLoc("village/snowy/houses/snowy_huge_house_1").toString(),
                    processorListRegistry.getHolderOrThrow(EMPTY_PROCESSOR_LIST_KEY)).apply(StructureTemplatePool.Projection.RIGID), 2));

            for(Pair<StructurePoolElement, Integer> entry : newPlainsVillage)
                for(int i=0;i<entry.getSecond();i++)
                    vanillaPlainsVillageHousePool.templates.add(entry.getFirst());
            for(Pair<StructurePoolElement, Integer> entry : newTaigaVillage)
                for(int i=0;i<entry.getSecond();i++)
                    vanillaTaigaVillageHousePool.templates.add(entry.getFirst());
            for(Pair<StructurePoolElement, Integer> entry : newSnowyVillage)
                for(int i=0;i<entry.getSecond();i++)
                    vanillaSnowyVillageHousePool.templates.add(entry.getFirst());

            List<Pair<StructurePoolElement, Integer>> plainsVillageRawStructureList = new ArrayList<>(vanillaPlainsVillageHousePool.rawTemplates);
            List<Pair<StructurePoolElement, Integer>> taigaVillageRawStructureList = new ArrayList<>(vanillaTaigaVillageHousePool.rawTemplates);
            List<Pair<StructurePoolElement, Integer>> snowyVillageRawStructureList = new ArrayList<>(vanillaSnowyVillageHousePool.rawTemplates);

            plainsVillageRawStructureList.addAll(newPlainsVillage);
            taigaVillageRawStructureList.addAll(newTaigaVillage);
            snowyVillageRawStructureList.addAll(newSnowyVillage);

            vanillaPlainsVillageHousePool.rawTemplates = plainsVillageRawStructureList;
            vanillaTaigaVillageHousePool.rawTemplates = taigaVillageRawStructureList;
            vanillaSnowyVillageHousePool.rawTemplates = snowyVillageRawStructureList;
        }
    }
}
