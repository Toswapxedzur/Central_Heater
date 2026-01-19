package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GeneratorBlockTag extends BlockTagsProvider {
    public GeneratorBlockTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, CentralHeater.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // --- Pickaxe Mineable ---
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                AllBlockItem.STONE_STOVE.get(),
                AllBlockItem.DEEPSLATE_STOVE.get(),
                AllBlockItem.RED_NETHER_BRICK_STOVE.get(),
                AllBlockItem.NETHER_BRICK_STOVE.get(),
                AllBlockItem.BLACKSTONE_STOVE.get(),
                AllBlockItem.BRICK_STOVE.get(),
                AllBlockItem.MUD_BRICK_STOVE.get(),
                AllBlockItem.MUD_BRICK_POT.get(),
                AllBlockItem.BRICK_CAULDRON.get(),
                AllBlockItem.IRON_CAULDRON.get(),
                AllBlockItem.GOLDEN_CAULDRON.get(),
                AllBlockItem.STURDY_TANK.get(),
                AllBlockItem.GOLD_BARS.get(),
                AllBlockItem.BLAZING_FURNACE.get(),

                // Tiles
                AllBlockItem.STONE_BRICK_TILE.get(),
                AllBlockItem.STONE_BRICK_TILE_STAIR.get(),
                AllBlockItem.STONE_BRICK_TILE_SLAB.get(),
                AllBlockItem.STONE_BRICK_TILE_WALL.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_STAIR.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_SLAB.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_WALL.get(),
                AllBlockItem.MUD_BRICK_TILE.get(),
                AllBlockItem.MUD_BRICK_TILE_STAIR.get(),
                AllBlockItem.MUD_BRICK_TILE_SLAB.get(),
                AllBlockItem.MUD_BRICK_TILE_WALL.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_STAIR.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_SLAB.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_WALL.get(),
                AllBlockItem.STURDY_BRICK_TILE.get(),
                AllBlockItem.STURDY_BRICK_TILE_STAIR.get(),
                AllBlockItem.STURDY_BRICK_TILE_SLAB.get(),
                AllBlockItem.STURDY_BRICK_TILE_WALL.get()
        );

        // --- Axe Mineable ---
        tag(BlockTags.MINEABLE_WITH_AXE).add(
                AllBlockItem.BURNT_LOG.get(),
                AllBlockItem.BURNT_WOOD.get()
        );

        // --- Shovel Mineable ---
        tag(BlockTags.MINEABLE_WITH_SHOVEL).add(
                AllBlockItem.CLAY_CAULDRON.get()
        );

        // --- Tool Tiers ---

        // Tier 1: Needs Stone
        tag(BlockTags.NEEDS_STONE_TOOL).add(
                AllBlockItem.BLAZING_FURNACE.get(),
                AllBlockItem.IRON_CAULDRON.get(),

                AllBlockItem.DEEPSLATE_STOVE.get(),
                AllBlockItem.BLACKSTONE_STOVE.get(),
                AllBlockItem.NETHER_BRICK_STOVE.get(),
                AllBlockItem.RED_NETHER_BRICK_STOVE.get(),

                // Deepslate & Blackstone Tiles
                AllBlockItem.DEEPSLATE_BRICK_TILE.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_STAIR.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_SLAB.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_WALL.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_STAIR.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_SLAB.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_WALL.get()
        );

        // Tier 2: Needs Iron
        tag(BlockTags.NEEDS_IRON_TOOL).add(
                AllBlockItem.GOLDEN_CAULDRON.get(),
                AllBlockItem.GOLD_BARS.get(),
                AllBlockItem.STURDY_TANK.get(),

                // Sturdy Tiles
                AllBlockItem.STURDY_BRICK_TILE.get(),
                AllBlockItem.STURDY_BRICK_TILE_STAIR.get(),
                AllBlockItem.STURDY_BRICK_TILE_SLAB.get(),
                AllBlockItem.STURDY_BRICK_TILE_WALL.get()
        );

        // --- Shape Tags ---
        tag(BlockTags.STAIRS).add(
                AllBlockItem.STONE_BRICK_TILE_STAIR.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_STAIR.get(),
                AllBlockItem.MUD_BRICK_TILE_STAIR.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_STAIR.get(),
                AllBlockItem.STURDY_BRICK_TILE_STAIR.get()
        );

        tag(BlockTags.SLABS).add(
                AllBlockItem.STONE_BRICK_TILE_SLAB.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_SLAB.get(),
                AllBlockItem.MUD_BRICK_TILE_SLAB.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_SLAB.get(),
                AllBlockItem.STURDY_BRICK_TILE_SLAB.get()
        );

        tag(BlockTags.WALLS).add(
                AllBlockItem.STONE_BRICK_TILE_WALL.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_WALL.get(),
                AllBlockItem.MUD_BRICK_TILE_WALL.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_WALL.get(),
                AllBlockItem.STURDY_BRICK_TILE_WALL.get()
        );

        // --- Functional Tags ---
        tag(BlockTags.CAULDRONS).add(
                AllBlockItem.CLAY_CAULDRON.get(),
                AllBlockItem.BRICK_CAULDRON.get(),
                AllBlockItem.IRON_CAULDRON.get(),
                AllBlockItem.GOLDEN_CAULDRON.get()
        );

        tag(BlockTags.LOGS_THAT_BURN).add(
                AllBlockItem.BURNT_LOG.get(),
                AllBlockItem.BURNT_WOOD.get()
        );

        tag(BlockTags.LOGS).add(
                AllBlockItem.BURNT_LOG.get(),
                AllBlockItem.BURNT_WOOD.get()
        );

        tag(BlockTags.GUARDED_BY_PIGLINS).add(
                AllBlockItem.GOLDEN_CAULDRON.get(),
                AllBlockItem.GOLD_BARS.get()
        );
    }
}