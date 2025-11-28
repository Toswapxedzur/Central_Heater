package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;

import java.util.Set;

public class LootTable extends BlockLootSubProvider {
    public LootTable(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    public void generate() {
        dropSelf(AllBlockItem.stone_brick_tile.get());
        dropSelf(AllBlockItem.deepslate_brick_tile.get());
        dropSelf(AllBlockItem.mud_brick_tile.get());
        dropSelf(AllBlockItem.blackstone_brick_tile.get());
        dropSelf(AllBlockItem.stone_stove.get());
        dropSelf(AllBlockItem.red_nether_brick_stove.get());
        dropSelf(AllBlockItem.brick_stove.get());
        dropSelf(AllBlockItem.mud_brick_stove.get());
        dropSelf(AllBlockItem.deepslate_stove.get());
        dropSelf(AllBlockItem.nether_brick_stove.get());
        dropSelf(AllBlockItem.blackstone_stove.get());

        dropSelf(AllBlockItem.iron_lid.get());
        dropSelf(AllBlockItem.gold_lid.get());

        dropSelf(AllBlockItem.stone_brick_tile_stair.get());
        add(AllBlockItem.stone_brick_tile_slab.get(), block -> createSlabItemTable(AllBlockItem.stone_brick_tile_slab.get()));
        dropSelf(AllBlockItem.stone_brick_tile_wall.get());
        dropSelf(AllBlockItem.deepslate_brick_tile_stair.get());
        add(AllBlockItem.deepslate_brick_tile_slab.get(), block -> createSlabItemTable(AllBlockItem.deepslate_brick_tile_slab.get()));
        dropSelf(AllBlockItem.deepslate_brick_tile_wall.get());
        dropSelf(AllBlockItem.mud_brick_tile_stair.get());
        add(AllBlockItem.mud_brick_tile_slab.get(), block -> createSlabItemTable(AllBlockItem.mud_brick_tile_slab.get()));
        dropSelf(AllBlockItem.mud_brick_tile_wall.get());
        dropSelf(AllBlockItem.blackstone_brick_tile_stair.get());
        add(AllBlockItem.blackstone_brick_tile_slab.get(), block -> createSlabItemTable(AllBlockItem.blackstone_brick_tile_slab.get()));
        dropSelf(AllBlockItem.blackstone_brick_tile_wall.get());
        dropSelf(AllBlockItem.gold_bars.get());

        dropSelf(AllBlockItem.brick_pot.get());
        dropSelf(AllBlockItem.mud_brick_pot.get());
        dropSelf(AllBlockItem.stone_pot.get());
        dropSelf(AllBlockItem.deepslate_pot.get());
        dropSelf(AllBlockItem.red_nether_brick_pot.get());
        dropSelf(AllBlockItem.nether_brick_pot.get());
        dropSelf(AllBlockItem.blackstone_pot.get());
    }

    @Override
    public Iterable<Block> getKnownBlocks() {
        return AllBlockItem.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }

}
