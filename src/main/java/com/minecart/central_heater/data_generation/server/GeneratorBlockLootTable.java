package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyNameFunction;
import net.minecraft.world.level.storage.loot.functions.CopyNbtFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
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
        // Campfire Override
        add(Blocks.CAMPFIRE, block -> createSilkTouchDispatchTable(block,
                this.applyExplosionCondition(block, LootItem.lootTableItem(Items.CHARCOAL)
                                .apply(SetItemCountFunction.setCount(ConstantValue.exactly(2.0F))))
                        .append(LootItem.lootTableItem(Items.STICK)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(3.0F, 5.0F))))
        ));

        // NBT Copy Functions (Replacement for Data Components)
        CopyNameFunction.Builder copyNameFunction = CopyNameFunction.copyName(CopyNameFunction.NameSource.BLOCK_ENTITY);

        // For the Sturdy Tank: Copy Name + Fluid NBT data
        CopyNbtFunction.Builder sturdyTankCopyFunction = CopyNbtFunction.copyData(ContextNbtProvider.BLOCK_ENTITY)
                .copy("FluidData", "FluidData"); // Assuming your BE saves fluid to this tag

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
                AllBlockItem.NETHER_BRICK_STOVE, AllBlockItem.BLACKSTONE_STOVE
        ).forEach(reg -> {
            Block block = reg.get();
            add(block, b -> createSingleItemTable(b).apply(copyNameFunction));
        });

        // Simple Charcoal Drops
        add(AllBlockItem.BURNT_LOG.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.CHARCOAL)));
        add(AllBlockItem.BURNT_WOOD.get(), block -> createSilkTouchDispatchTable(block, LootItem.lootTableItem(Items.CHARCOAL)));

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
        Stream<Block> modifiable = Stream.of(Blocks.CAMPFIRE, Blocks.SOUL_CAMPFIRE);
        return Stream.concat(AllBlockItem.BLOCKS.getEntries().stream().map(RegistryObject::get), modifiable)::iterator;
    }
}