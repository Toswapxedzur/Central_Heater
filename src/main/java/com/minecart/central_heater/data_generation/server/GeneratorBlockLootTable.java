package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.nbt.ContextNbtProvider;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;
import java.util.stream.Stream;

public class GeneratorBlockLootTable extends BlockLootSubProvider {

    public GeneratorBlockLootTable() {
        super(Set.of(), FeatureFlags.VANILLA_SET);
    }

    @Override
    protected void generate() {
        // --- Burnable Campfires (1.21.1 parity) ---
        add(AllBlockItem.BURNABLE_CAMPFIRE.get(), block -> createSilkTouchDispatchTable(block,
                applyExplosionCondition(block, LootItem.lootTableItem(Items.CHARCOAL)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
                        .append(LootItem.lootTableItem(Items.STICK)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .append(LootItem.lootTableItem(AllBlockItem.FIRE_ASH.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
        ));

        add(AllBlockItem.BURNABLE_SOUL_CAMPFIRE.get(), block -> createSilkTouchDispatchTable(block,
                applyExplosionCondition(block, LootItem.lootTableItem(AllBlockItem.SCORCHED_COAL.get())
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(1.0F))))
                        .append(LootItem.lootTableItem(Items.STICK)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
                        .append(LootItem.lootTableItem(AllBlockItem.SCORCHED_DUST.get())
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F))))
        ));

        // 1.20.1 NBT-based name copy (Data Components don't exist yet)
        CopyNameFunction.Builder copyNameFunction = CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY);

        // For the Sturdy Tank: Copy Name + Fluid NBT data
        CopyNbtFunction.Builder sturdyTankCopyFunction = CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy("FluidData", "FluidData");

        add(AllBlockItem.STURDY_TANK.get(), block -> LootTable.lootTable().withPool(
                applyExplosionCondition(block, LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F))
                        .add(LootItem.lootTableItem(AllBlockItem.STURDY_TANK_ITEM.get())
                                .apply(copyNameFunction)
                                .apply(sturdyTankCopyFunction)))));

        // Basic Container blocks with Name Copy
        Stream.of(
                AllBlockItem.MUD_BRICK_POT, AllBlockItem.BRICK_CAULDRON, AllBlockItem.IRON_CAULDRON,
                AllBlockItem.GOLDEN_CAULDRON, AllBlockItem.MUD_BRICK_STOVE, AllBlockItem.BRICK_STOVE,
                AllBlockItem.STONE_STOVE, AllBlockItem.DEEPSLATE_STOVE, AllBlockItem.RED_NETHER_BRICK_STOVE,
                AllBlockItem.NETHER_BRICK_STOVE, AllBlockItem.BLACKSTONE_STOVE,
                // Copper Stoves (oxidization series + waxed series)
                AllBlockItem.COPPER_STOVE, AllBlockItem.EXPOSED_COPPER_STOVE,
                AllBlockItem.WEATHERED_COPPER_STOVE, AllBlockItem.OXIDIZED_COPPER_STOVE,
                AllBlockItem.WAXED_COPPER_STOVE, AllBlockItem.WAXED_EXPOSED_COPPER_STOVE,
                AllBlockItem.WAXED_WEATHERED_COPPER_STOVE, AllBlockItem.WAXED_OXIDIZED_COPPER_STOVE
        ).forEach(reg -> {
            Block block = reg.get();
            add(block, b -> createSingleItemTable(b).apply(copyNameFunction));
        });

        // --- Burnt Logs & Woods (silk-touch -> self, otherwise CHARCOAL) ---
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

        // --- Burnt Wood Derivatives (drop charcoal bits when broken without silk touch) ---
        add(AllBlockItem.BURNT_PLANKS.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get())));
        add(AllBlockItem.BURNT_STAIRS.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get())));
        add(AllBlockItem.BURNT_FENCE.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get())));
        add(AllBlockItem.BURNT_BUTTON.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get())));

        add(AllBlockItem.BURNT_PRESSURE_PLATE.get(), block -> createSilkTouchDispatchTable(block,
                LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))));
        add(AllBlockItem.BURNT_FENCE_GATE.get(), block -> createSilkTouchDispatchTable(block,
                LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))));
        add(AllBlockItem.BURNT_TRAPDOOR.get(), block -> createSilkTouchDispatchTable(block,
                LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get()).apply(SetItemCountFunction.setCount(ConstantValue.exactly(3.0F)))));

        // Slab: silk-touch only, no fallback drop
        add(AllBlockItem.BURNT_SLAB.get(), block -> createSilkTouchOnlyTable(block));

        // Door drops 2 bits, but ONLY from the lower half (so a destroyed door yields 2 not 4)
        add(AllBlockItem.BURNT_DOOR.get(), block -> createSilkTouchDispatchTable(block,
                applyExplosionCondition(block, LootItem.lootTableItem(AllBlockItem.CHARCOAL_BIT.get())
                        .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F)))
                        .when(LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                                .setProperties(StatePropertiesPredicate.Builder.properties()
                                        .hasProperty(DoorBlock.HALF, DoubleBlockHalf.LOWER)))
                )
        ));

        // Fallback for everything else
        for (Block block : getKnownBlocks()) {
            if (!this.map.containsKey(block)) {
                if (block instanceof SlabBlock slabBlock)
                    add(slabBlock, b -> createSlabItemTable(slabBlock));
                else
                    dropSelf(block);
            }
        }
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return AllBlockItem.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
