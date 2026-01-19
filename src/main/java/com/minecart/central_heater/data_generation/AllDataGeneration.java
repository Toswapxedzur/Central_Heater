package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.data_generation.client.GeneratorBlockModel;
import com.minecart.central_heater.data_generation.client.GeneratorBlockState;
import com.minecart.central_heater.data_generation.client.GeneratorItemModel;
import com.minecart.central_heater.data_generation.server.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Mod.EventBusSubscriber(modid = CentralHeater.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class AllDataGeneration {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookUpProvider = event.getLookupProvider();

        // Server Side
        generator.addProvider(event.includeServer(), new LootTableProvider(output, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(GeneratorBlockLootTable::new, LootContextParamSets.BLOCK))));

        generator.addProvider(event.includeServer(), new GeneratorRecipe(output));
        generator.addProvider(event.includeServer(), new GeneratorDataRegistries(output, lookUpProvider));

        // Block Tags
        GeneratorBlockTag blockTag = new GeneratorBlockTag(output, lookUpProvider, fileHelper);
        generator.addProvider(event.includeServer(), blockTag);

        // Item Tags (Requires Block Tag Provider's contentsGetter, ModID, and FileHelper in Forge 1.20.1)
        generator.addProvider(event.includeServer(), new GeneratorItemTag(output, lookUpProvider, blockTag.contentsGetter(), fileHelper));

        // Advancements
        generator.addProvider(event.includeServer(), new ProviderAdvancements(output, lookUpProvider, fileHelper));

        // Client Side
        generator.addProvider(event.includeClient(), new GeneratorBlockModel(output, fileHelper));
        generator.addProvider(event.includeClient(), new GeneratorBlockState(output, fileHelper));
        generator.addProvider(event.includeClient(), new GeneratorItemModel(output, fileHelper));
    }
}