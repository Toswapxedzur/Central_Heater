package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.item.AllDataComponents;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.entries.EntryGroup;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
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
        add(AllBlockItem.BURNABLE_CAMPFIRE.get(), block -> createSilkTouchDispatchTable(block,
                applyExplosionCondition(block,
                        EntryGroup.list(
                                LootItem.lootTableItem(Items.CHARCOAL)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))),
                                LootItem.lootTableItem(Items.STICK)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))),
                                LootItem.lootTableItem(AllBlockItem.FIRE_ASH)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                        )
                )
        ));

        add(AllBlockItem.BURNABLE_SOUL_CAMPFIRE.get(), block -> createSilkTouchDispatchTable(block,
                applyExplosionCondition(block,
                        EntryGroup.list(
                                LootItem.lootTableItem(AllBlockItem.SCORCHED_COAL)
                                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))),
                                LootItem.lootTableItem(Items.STICK)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))),
                                LootItem.lootTableItem(AllBlockItem.SCORCHED_DUST)
                                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
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

        // --- Stoves ---
        add(AllBlockItem.MUD_BRICK_STOVE.get(), createSingleItemTable(AllBlockItem.MUD_BRICK_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.BRICK_STOVE.get(), createSingleItemTable(AllBlockItem.BRICK_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.STONE_STOVE.get(), createSingleItemTable(AllBlockItem.STONE_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.DEEPSLATE_STOVE.get(), createSingleItemTable(AllBlockItem.DEEPSLATE_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.RED_NETHER_BRICK_STOVE.get(), createSingleItemTable(AllBlockItem.RED_NETHER_BRICK_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.NETHER_BRICK_STOVE.get(), createSingleItemTable(AllBlockItem.NETHER_BRICK_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.BLACKSTONE_STOVE.get(), createSingleItemTable(AllBlockItem.BLACKSTONE_STOVE.asItem()).apply(copyNameFunction));

        // Copper Stoves
        add(AllBlockItem.COPPER_STOVE.get(), createSingleItemTable(AllBlockItem.COPPER_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.EXPOSED_COPPER_STOVE.get(), createSingleItemTable(AllBlockItem.EXPOSED_COPPER_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.WEATHERED_COPPER_STOVE.get(), createSingleItemTable(AllBlockItem.WEATHERED_COPPER_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.OXIDIZED_COPPER_STOVE.get(), createSingleItemTable(AllBlockItem.OXIDIZED_COPPER_STOVE.asItem()).apply(copyNameFunction));

        add(AllBlockItem.WAXED_COPPER_STOVE.get(), createSingleItemTable(AllBlockItem.WAXED_COPPER_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.WAXED_EXPOSED_COPPER_STOVE.get(), createSingleItemTable(AllBlockItem.WAXED_EXPOSED_COPPER_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.WAXED_WEATHERED_COPPER_STOVE.get(), createSingleItemTable(AllBlockItem.WAXED_WEATHERED_COPPER_STOVE.asItem()).apply(copyNameFunction));
        add(AllBlockItem.WAXED_OXIDIZED_COPPER_STOVE.get(), createSingleItemTable(AllBlockItem.WAXED_OXIDIZED_COPPER_STOVE.asItem()).apply(copyNameFunction));

        // --- Burnt Logs & Woods ---
        add(AllBlockItem.BURNT_LOG.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.CHARCOAL)));
        add(AllBlockItem.BURNT_WOOD.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.CHARCOAL)));

        add(AllBlockItem.BURNT_BIRCH_LOG.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.CHARCOAL)));
        add(AllBlockItem.BURNT_BIRCH_WOOD.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.CHARCOAL)));

        add(AllBlockItem.BURNT_JUNGLE_LOG.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.CHARCOAL)));
        add(AllBlockItem.BURNT_JUNGLE_WOOD.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.CHARCOAL)));

        add(AllBlockItem.BURNT_CHERRY_LOG.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.CHARCOAL)));
        add(AllBlockItem.BURNT_CHERRY_WOOD.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.CHARCOAL)));

        add(AllBlockItem.BURNT_MANGROVE_LOG.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.CHARCOAL)));
        add(AllBlockItem.BURNT_MANGROVE_WOOD.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.CHARCOAL)));

        // --- Burnt Wood Derivatives (Charcoal Bit Drops) ---
        add(AllBlockItem.BURNT_PLANKS.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get())));
        add(AllBlockItem.BURNT_STAIRS.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get())));
        add(AllBlockItem.BURNT_FENCE.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get())));
        add(AllBlockItem.BURNT_BUTTON.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get())));

        add(AllBlockItem.BURNT_PRESSURE_PLATE.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))));
        add(AllBlockItem.BURNT_FENCE_GATE.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))));
        add(AllBlockItem.BURNT_TRAPDOOR.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F)))));

        add(AllBlockItem.BURNT_SLAB.get(), block -> createSilkTouchOnlyTable(block));

        // Doors drop 2 bits, but ONLY if the lower half is broken so it doesn't drop 4 bits total
        add(AllBlockItem.BURNT_DOOR.get(), block -> createSilkTouchDispatchTable(block,
                applyExplosionCondition(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER)))
                )
        ));

        // --- Automatic Fallback for Simple Drops & Slabs ---
        for(Block block : getKnownBlocks()){
            if(!this.map.containsKey(block.getLootTable())){
                if(block instanceof SlabBlock slabBlock)
                    add(slabBlock, b -> createSlabItemTable(slabBlock));
                else
                    dropSelf(block); // Catches Planks, Fences, Trapdoors, Bricks, etc.
            }
        }
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        //append modified vanillaloot table blocks
        Stream<Block> modifiable = Stream.of();
        return Stream.concat(AllBlockItem.BLOCKS.getEntries().stream().map(Holder::value), modifiable)::iterator;
    }

}
