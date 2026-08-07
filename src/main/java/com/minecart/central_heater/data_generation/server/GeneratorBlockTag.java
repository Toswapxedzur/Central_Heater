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

                // Anvils
                AllBlockItem.STURDY_ANVIL.get(),
                AllBlockItem.CHIPPED_STURDY_ANVIL.get(),
                AllBlockItem.DAMAGED_STURDY_ANVIL.get(),

                AllBlockItem.STURDY_BRICKS.get(),
                AllBlockItem.STURDY_BRICK_STAIR.get(),
                AllBlockItem.STURDY_BRICK_SLAB.get(),

                // Original Tiles
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
                AllBlockItem.STURDY_BRICK_TILE_WALL.get(),

                // Coal Bricks
                AllBlockItem.COAL_BRICKS.get(),
                AllBlockItem.COAL_BRICK_STAIR.get(),
                AllBlockItem.COAL_BRICK_SLAB.get(),
                AllBlockItem.COAL_BRICK_TILE.get(),
                AllBlockItem.COAL_BRICK_TILE_STAIR.get(),
                AllBlockItem.COAL_BRICK_TILE_SLAB.get(),
                AllBlockItem.COAL_BRICK_TILE_WALL.get(),

                // Golden Bricks
                AllBlockItem.GOLDEN_BRICKS.get(),
                AllBlockItem.GOLDEN_BRICK_STAIR.get(),
                AllBlockItem.GOLDEN_BRICK_SLAB.get(),
                AllBlockItem.GOLDEN_BRICK_TILE.get(),
                AllBlockItem.GOLDEN_BRICK_TILE_STAIR.get(),
                AllBlockItem.GOLDEN_BRICK_TILE_SLAB.get(),
                AllBlockItem.GOLDEN_BRICK_TILE_WALL.get(),

                // Netherite Bricks
                AllBlockItem.NETHERITE_BRICKS.get(),
                AllBlockItem.NETHERITE_BRICK_STAIR.get(),
                AllBlockItem.NETHERITE_BRICK_SLAB.get(),
                AllBlockItem.NETHERITE_BRICK_TILE.get(),
                AllBlockItem.NETHERITE_BRICK_TILE_STAIR.get(),
                AllBlockItem.NETHERITE_BRICK_TILE_SLAB.get(),
                AllBlockItem.NETHERITE_BRICK_TILE_WALL.get(),

                // Scorched Bricks
                AllBlockItem.SCORCHED_BRICKS.get(),
                AllBlockItem.SCORCHED_BRICK_STAIR.get(),
                AllBlockItem.SCORCHED_BRICK_SLAB.get(),
                AllBlockItem.SCORCHED_BRICK_TILE.get(),
                AllBlockItem.SCORCHED_BRICK_TILE_STAIR.get(),
                AllBlockItem.SCORCHED_BRICK_TILE_SLAB.get(),
                AllBlockItem.SCORCHED_BRICK_TILE_WALL.get()
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
                AllBlockItem.STURDY_BRICK_TILE_WALL.get(),

                // Anvils
                AllBlockItem.STURDY_ANVIL.get(),
                AllBlockItem.CHIPPED_STURDY_ANVIL.get(),
                AllBlockItem.DAMAGED_STURDY_ANVIL.get(),

                // Golden Bricks
                AllBlockItem.GOLDEN_BRICKS.get(),
                AllBlockItem.GOLDEN_BRICK_STAIR.get(),
                AllBlockItem.GOLDEN_BRICK_SLAB.get(),
                AllBlockItem.GOLDEN_BRICK_TILE.get(),
                AllBlockItem.GOLDEN_BRICK_TILE_STAIR.get(),
                AllBlockItem.GOLDEN_BRICK_TILE_SLAB.get(),
                AllBlockItem.GOLDEN_BRICK_TILE_WALL.get()
        );

        // Tier 3: Needs Diamond
        tag(BlockTags.NEEDS_DIAMOND_TOOL).add(
                // Netherite Bricks
                AllBlockItem.NETHERITE_BRICKS.get(),
                AllBlockItem.NETHERITE_BRICK_STAIR.get(),
                AllBlockItem.NETHERITE_BRICK_SLAB.get(),
                AllBlockItem.NETHERITE_BRICK_TILE.get(),
                AllBlockItem.NETHERITE_BRICK_TILE_STAIR.get(),
                AllBlockItem.NETHERITE_BRICK_TILE_SLAB.get(),
                AllBlockItem.NETHERITE_BRICK_TILE_WALL.get()
        );

        // --- Shape Tags ---
        tag(BlockTags.STAIRS).add(
                AllBlockItem.STONE_BRICK_TILE_STAIR.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_STAIR.get(),
                AllBlockItem.MUD_BRICK_TILE_STAIR.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_STAIR.get(),
                AllBlockItem.STURDY_BRICK_TILE_STAIR.get(),

                AllBlockItem.STURDY_BRICK_STAIR.get(),

                // New Stairs
                AllBlockItem.COAL_BRICK_STAIR.get(),
                AllBlockItem.COAL_BRICK_TILE_STAIR.get(),
                AllBlockItem.GOLDEN_BRICK_STAIR.get(),
                AllBlockItem.GOLDEN_BRICK_TILE_STAIR.get(),
                AllBlockItem.NETHERITE_BRICK_STAIR.get(),
                AllBlockItem.NETHERITE_BRICK_TILE_STAIR.get(),
                AllBlockItem.SCORCHED_BRICK_STAIR.get(),
                AllBlockItem.SCORCHED_BRICK_TILE_STAIR.get()
        );

        tag(BlockTags.SLABS).add(
                AllBlockItem.STONE_BRICK_TILE_SLAB.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_SLAB.get(),
                AllBlockItem.MUD_BRICK_TILE_SLAB.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_SLAB.get(),
                AllBlockItem.STURDY_BRICK_TILE_SLAB.get(),

                AllBlockItem.STURDY_BRICK_SLAB.get(),
                // New Slabs
                AllBlockItem.COAL_BRICK_SLAB.get(),
                AllBlockItem.COAL_BRICK_TILE_SLAB.get(),
                AllBlockItem.GOLDEN_BRICK_SLAB.get(),
                AllBlockItem.GOLDEN_BRICK_TILE_SLAB.get(),
                AllBlockItem.NETHERITE_BRICK_SLAB.get(),
                AllBlockItem.NETHERITE_BRICK_TILE_SLAB.get(),
                AllBlockItem.SCORCHED_BRICK_SLAB.get(),
                AllBlockItem.SCORCHED_BRICK_TILE_SLAB.get()
        );

        tag(BlockTags.WALLS).add(
                AllBlockItem.STONE_BRICK_TILE_WALL.get(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_WALL.get(),
                AllBlockItem.MUD_BRICK_TILE_WALL.get(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_WALL.get(),
                AllBlockItem.STURDY_BRICK_TILE_WALL.get(),

                // New Walls
                AllBlockItem.COAL_BRICK_TILE_WALL.get(),
                AllBlockItem.GOLDEN_BRICK_TILE_WALL.get(),
                AllBlockItem.NETHERITE_BRICK_TILE_WALL.get(),
                AllBlockItem.SCORCHED_BRICK_TILE_WALL.get()
        );

        // --- Functional Tags ---

        // Defines these blocks as Cauldrons (often used for logic checks)
        tag(BlockTags.CAULDRONS).add(
                AllBlockItem.CLAY_CAULDRON.get(),
                AllBlockItem.BRICK_CAULDRON.get(),
                AllBlockItem.IRON_CAULDRON.get(),
                AllBlockItem.GOLDEN_CAULDRON.get()
        );

        // Logs that burn (used for fuel calc)
        tag(BlockTags.LOGS_THAT_BURN).add(
                AllBlockItem.BURNT_LOG.get(),
                AllBlockItem.BURNT_WOOD.get()
        );

        // General Logs (used for crafting recipes like generic sticks)
        tag(BlockTags.LOGS).add(
                AllBlockItem.BURNT_LOG.get(),
                AllBlockItem.BURNT_WOOD.get()
        );

        // Piglin Interaction: mining these will anger Piglins
        tag(BlockTags.GUARDED_BY_PIGLINS).add(
                AllBlockItem.GOLDEN_CAULDRON.get(),
                AllBlockItem.GOLD_BARS.get(),
                AllBlockItem.GOLDEN_BRICKS.get(),
                AllBlockItem.GOLDEN_BRICK_STAIR.get(),
                AllBlockItem.GOLDEN_BRICK_SLAB.get(),
                AllBlockItem.GOLDEN_BRICK_TILE.get(),
                AllBlockItem.GOLDEN_BRICK_TILE_STAIR.get(),
                AllBlockItem.GOLDEN_BRICK_TILE_SLAB.get(),
                AllBlockItem.GOLDEN_BRICK_TILE_WALL.get()
        );

        // --- Anvils ---
        tag(BlockTags.ANVIL).add(
                AllBlockItem.STURDY_ANVIL.get(),
                AllBlockItem.CHIPPED_STURDY_ANVIL.get(),
                AllBlockItem.DAMAGED_STURDY_ANVIL.get()
        );

        // Copper stoves -> pickaxe
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                AllBlockItem.COPPER_STOVE.get(),
                AllBlockItem.EXPOSED_COPPER_STOVE.get(),
                AllBlockItem.WEATHERED_COPPER_STOVE.get(),
                AllBlockItem.OXIDIZED_COPPER_STOVE.get(),
                AllBlockItem.WAXED_COPPER_STOVE.get(),
                AllBlockItem.WAXED_EXPOSED_COPPER_STOVE.get(),
                AllBlockItem.WAXED_WEATHERED_COPPER_STOVE.get(),
                AllBlockItem.WAXED_OXIDIZED_COPPER_STOVE.get(),
                AllBlockItem.ASHTRAY.get()
        );

        tag(BlockTags.PIGLIN_REPELLENTS).add(AllBlockItem.BURNABLE_SOUL_CAMPFIRE.get());

        tag(BlockTags.MINEABLE_WITH_AXE).add(
                // Specific Burnt Logs
                AllBlockItem.BURNT_BIRCH_LOG.get(),
                AllBlockItem.BURNT_BIRCH_WOOD.get(),
                AllBlockItem.BURNT_JUNGLE_LOG.get(),
                AllBlockItem.BURNT_JUNGLE_WOOD.get(),
                AllBlockItem.BURNT_CHERRY_LOG.get(),
                AllBlockItem.BURNT_CHERRY_WOOD.get(),
                AllBlockItem.BURNT_MANGROVE_LOG.get(),
                AllBlockItem.BURNT_MANGROVE_WOOD.get(),

                AllBlockItem.BURNT_PLANKS.get(),
                AllBlockItem.BURNT_STAIRS.get(),
                AllBlockItem.BURNT_SLAB.get(),
                AllBlockItem.BURNT_FENCE.get(),
                AllBlockItem.BURNT_FENCE_GATE.get(),
                AllBlockItem.BURNT_TRAPDOOR.get(),
                AllBlockItem.BURNT_BUTTON.get(),
                AllBlockItem.BURNT_PRESSURE_PLATE.get(),
                AllBlockItem.BURNT_DOOR.get(),

                // Campfires
                AllBlockItem.BURNABLE_CAMPFIRE.get(),
                AllBlockItem.BURNABLE_SOUL_CAMPFIRE.get()
        );

        // --- Add to Existing Needs Stone Tool ---
        tag(BlockTags.NEEDS_STONE_TOOL).add(
                AllBlockItem.COPPER_STOVE.get(),
                AllBlockItem.EXPOSED_COPPER_STOVE.get(),
                AllBlockItem.WEATHERED_COPPER_STOVE.get(),
                AllBlockItem.OXIDIZED_COPPER_STOVE.get(),
                AllBlockItem.WAXED_COPPER_STOVE.get(),
                AllBlockItem.WAXED_EXPOSED_COPPER_STOVE.get(),
                AllBlockItem.WAXED_WEATHERED_COPPER_STOVE.get(),
                AllBlockItem.WAXED_OXIDIZED_COPPER_STOVE.get()
        );

        // --- Add to Existing Shape Tags ---
        tag(BlockTags.STAIRS).add(AllBlockItem.BURNT_STAIRS.get());
        tag(BlockTags.WOODEN_STAIRS).add(AllBlockItem.BURNT_STAIRS.get());

        tag(BlockTags.SLABS).add(AllBlockItem.BURNT_SLAB.get());
        tag(BlockTags.WOODEN_SLABS).add(AllBlockItem.BURNT_SLAB.get());

        // --- New Shape Tags for the Burnt Set ---
        tag(BlockTags.FENCES).add(AllBlockItem.BURNT_FENCE.get());
        tag(BlockTags.WOODEN_FENCES).add(AllBlockItem.BURNT_FENCE.get());

        tag(BlockTags.FENCE_GATES).add(AllBlockItem.BURNT_FENCE_GATE.get());

        tag(BlockTags.DOORS).add(AllBlockItem.BURNT_DOOR.get());
        tag(BlockTags.WOODEN_DOORS).add(AllBlockItem.BURNT_DOOR.get());

        tag(BlockTags.TRAPDOORS).add(AllBlockItem.BURNT_TRAPDOOR.get());
        tag(BlockTags.WOODEN_TRAPDOORS).add(AllBlockItem.BURNT_TRAPDOOR.get());

        tag(BlockTags.BUTTONS).add(AllBlockItem.BURNT_BUTTON.get());
        tag(BlockTags.WOODEN_BUTTONS).add(AllBlockItem.BURNT_BUTTON.get());

        tag(BlockTags.PRESSURE_PLATES).add(AllBlockItem.BURNT_PRESSURE_PLATE.get());
        tag(BlockTags.WOODEN_PRESSURE_PLATES).add(AllBlockItem.BURNT_PRESSURE_PLATE.get());

        // --- Add to Existing Functional Tags ---
        tag(BlockTags.LOGS_THAT_BURN).add(
                AllBlockItem.BURNT_BIRCH_LOG.get(),
                AllBlockItem.BURNT_BIRCH_WOOD.get(),
                AllBlockItem.BURNT_JUNGLE_LOG.get(),
                AllBlockItem.BURNT_JUNGLE_WOOD.get(),
                AllBlockItem.BURNT_CHERRY_LOG.get(),
                AllBlockItem.BURNT_CHERRY_WOOD.get(),
                AllBlockItem.BURNT_MANGROVE_LOG.get(),
                AllBlockItem.BURNT_MANGROVE_WOOD.get()
        );

        tag(BlockTags.LOGS).add(
                AllBlockItem.BURNT_BIRCH_LOG.get(),
                AllBlockItem.BURNT_BIRCH_WOOD.get(),
                AllBlockItem.BURNT_JUNGLE_LOG.get(),
                AllBlockItem.BURNT_JUNGLE_WOOD.get(),
                AllBlockItem.BURNT_CHERRY_LOG.get(),
                AllBlockItem.BURNT_CHERRY_WOOD.get(),
                AllBlockItem.BURNT_MANGROVE_LOG.get(),
                AllBlockItem.BURNT_MANGROVE_WOOD.get()
        );

        tag(BlockTags.PLANKS).add(AllBlockItem.BURNT_PLANKS.get());

        tag(BlockTags.CAMPFIRES).add(
                AllBlockItem.BURNABLE_CAMPFIRE.get(),
                AllBlockItem.BURNABLE_SOUL_CAMPFIRE.get()
        );
    }
}
