package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.item.AllDataComponents;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.storage.loot.entries.EntryGroup;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;
import java.util.stream.Stream;

public class GeneratorBlockLootTable extends BlockLootSubProvider {
    public GeneratorBlockLootTable(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        add(Blocks.CAMPFIRE, block -> createSilkTouchDispatchTable(block,
                applyExplosionCondition(block,
                        EntryGroup.list(
                                LootItem.lootTableItem(Items.CHARCOAL)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))),
                                LootItem.lootTableItem(Items.STICK)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F)))
                        )
                )
        ));

        CopyComponentsFunction.Builder copyNameFunction = CopyComponentsFunction.
                copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY).include(DataComponents.CUSTOM_NAME);

        CopyComponentsFunction.Builder sturdyTankCopyFunction = CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                .include(DataComponents.CUSTOM_NAME)
                .include(AllDataComponents.SIMPLE_FLUID_CONTENT.get());

        add(AllBlockItem.STURDY_TANK.get(), createSingleItemTable(AllBlockItem.STURDY_TANK_ITEM).apply(sturdyTankCopyFunction));

        add(AllBlockItem.MUD_BRICK_POT.get(), createSingleItemTable(AllBlockItem.MUD_BRICK_POT.asItem()).apply(copyNameFunction));
        add(AllBlockItem.BRICK_CAULDRON.get(), createSingleItemTable(AllBlockItem.BRICK_CAULDRON.asItem()).apply(copyNameFunction));
        add(AllBlockItem.IRON_CAULDRON.get(), createSingleItemTable(AllBlockItem.IRON_CAULDRON.asItem()).apply(copyNameFunction));
        add(AllBlockItem.GOLDEN_CAULDRON.get(), createSingleItemTable(AllBlockItem.GOLDEN_CAULDRON.asItem()).apply(copyNameFunction));

        add(AllBlockItem.MUD_BRICK_STOVE.get(), createSingleItemTable(AllBlockItem.MUD_BRICK_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.BRICK_STOVE.get(), createSingleItemTable(AllBlockItem.BRICK_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.STONE_STOVE.get(), createSingleItemTable(AllBlockItem.STONE_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.DEEPSLATE_STOVE.get(), createSingleItemTable(AllBlockItem.DEEPSLATE_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.RED_NETHER_BRICK_STOVE.get(), createSingleItemTable(AllBlockItem.RED_NETHER_BRICK_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.NETHER_BRICK_STOVE.get(), createSingleItemTable(AllBlockItem.NETHER_BRICK_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.BLACKSTONE_STOVE.get(), createSingleItemTable(AllBlockItem.BLACKSTONE_STOVE.asItem()).apply(copyNameFunction));

        add(AllBlockItem.BURNT_LOG.get(), block -> createSilkTouchDispatchTable(block,
                LootItem.lootTableItem(Items.CHARCOAL))
        );

        add(AllBlockItem.BURNT_WOOD.get(), block -> createSilkTouchDispatchTable(block,
                LootItem.lootTableItem(Items.CHARCOAL))
        );

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
