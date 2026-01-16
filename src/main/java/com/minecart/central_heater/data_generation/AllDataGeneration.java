package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.data_generation.client.GeneratorBlockModel;
import com.minecart.central_heater.data_generation.client.GeneratorBlockState;
import com.minecart.central_heater.data_generation.client.GeneratorItemModel;
import com.minecart.central_heater.data_generation.server.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber
public class AllDataGeneration {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event){
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookUpProvider = event.getLookupProvider();

        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.EMPTY_SET,
                 List.of(new LootTableProvider.SubProviderEntry(GeneratorBlockLootTable::new, LootContextParamSets.BLOCK)), lookUpProvider));

        generator.addProvider(event.includeServer(), new GeneratorRecipe(output, lookUpProvider));
        generator.addProvider(event.includeServer(), new GeneratorDataRegistries(output, lookUpProvider));
        GeneratorBlockTag blockTag = new GeneratorBlockTag(output, lookUpProvider, fileHelper);
        generator.addProvider(event.includeServer(), new ProviderAdvancements(output, lookUpProvider, fileHelper));
        generator.addProvider(event.includeServer(), blockTag);
        generator.addProvider(event.includeServer(), new GeneratorItemTag(output, lookUpProvider, blockTag.contentsGetter()));

        generator.addProvider(event.includeClient(), new GeneratorBlockModel(output, fileHelper));
        generator.addProvider(event.includeClient(), new GeneratorBlockState(output, fileHelper));
        generator.addProvider(event.includeClient(), new GeneratorItemModel(output, fileHelper));
    }
}
