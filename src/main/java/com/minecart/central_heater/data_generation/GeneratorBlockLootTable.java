package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.AllDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;
import java.util.stream.Stream;

public class GeneratorBlockLootTable extends BlockLootSubProvider {
    public GeneratorBlockLootTable(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        add(Blocks.CAMPFIRE, items->createSilkTouchDispatchTable(items, (LootPoolEntryContainer.Builder) applyExplosionCondition(items ,LootItem.lootTableItem(AllBlockItem.fire_ash).apply(SetItemCountFunction.setCount(UniformGenerator.between(3f, 8f))))));

        CopyComponentsFunction.Builder copyNameFunction = CopyComponentsFunction.
                copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME);

        CopyComponentsFunction.Builder sturdyTankCopyFunction = CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                .include(DataComponents.CUSTOM_NAME)
                .include(AllDataComponents.SIMPLE_FLUID_CONTENT.get());

        add(AllBlockItem.sturdy_tank.get(), createSingleItemTable(AllBlockItem.sturdy_tank_item).apply(sturdyTankCopyFunction));

        add(AllBlockItem.mud_brick_pot.get(), createSingleItemTable(AllBlockItem.mud_brick_pot.asItem()).apply(copyNameFunction));
        add(AllBlockItem.brick_cauldron.get(), createSingleItemTable(AllBlockItem.brick_cauldron.asItem()).apply(copyNameFunction));
        add(AllBlockItem.iron_cauldron.get(), createSingleItemTable(AllBlockItem.iron_cauldron.asItem()).apply(copyNameFunction));
        add(AllBlockItem.golden_cauldron.get(), createSingleItemTable(AllBlockItem.golden_cauldron.asItem()).apply(copyNameFunction));

        add(AllBlockItem.mud_brick_stove.get(), createSingleItemTable(AllBlockItem.mud_brick_stove.asItem()).apply(copyNameFunction));
        add(AllBlockItem.brick_stove.get(), createSingleItemTable(AllBlockItem.brick_stove.asItem()).apply(copyNameFunction));
        add(AllBlockItem.stone_stove.get(), createSingleItemTable(AllBlockItem.stone_stove.asItem()).apply(copyNameFunction));
        add(AllBlockItem.deepslate_stove.get(), createSingleItemTable(AllBlockItem.deepslate_stove.asItem()).apply(copyNameFunction));
        add(AllBlockItem.red_nether_brick_stove.get(), createSingleItemTable(AllBlockItem.red_nether_brick_stove.asItem()).apply(copyNameFunction));
        add(AllBlockItem.nether_brick_stove.get(), createSingleItemTable(AllBlockItem.nether_brick_stove.asItem()).apply(copyNameFunction));
        add(AllBlockItem.blackstone_stove.get(), createSingleItemTable(AllBlockItem.blackstone_stove.asItem()).apply(copyNameFunction));

        for(Block block : getKnownBlocks()){
            if(!this.map.containsKey(block.getLootTable())){
                if(block instanceof SlabBlock slabBlock)
                    add(slabBlock, b -> createSlabItemTable(slabBlock));
                else
                    dropSelf(block);
            }
        }
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        Stream<Block> modifiable = Stream.of(Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE);
        return Stream.concat(AllBlockItem.BLOCKS.getEntries().stream().map(Holder::value), modifiable)::iterator;
    }

}
