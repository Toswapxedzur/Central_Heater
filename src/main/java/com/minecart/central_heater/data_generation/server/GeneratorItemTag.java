package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.misc.Alltags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class GeneratorItemTag extends ItemTagsProvider {
    public GeneratorItemTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                            CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, CentralHeater.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // --- Forge Common Tags ---
        tag(Tags.Items.INGOTS_BRICK).add(
                AllBlockItem.MUD_BRICK.get(),
                AllBlockItem.STONE_BRICK.get(),
                AllBlockItem.DEEPSLATE_BRICK.get(),
                AllBlockItem.BLACKSTONE_BRICK.get()
        );

        tag(Tags.Items.INGOTS_NETHER_BRICK).add(
                AllBlockItem.RED_NETHER_BRICK.get()
        );

        tag(Tags.Items.NUGGETS).add(
                AllBlockItem.DIAMOND_SHARD.get(),
                AllBlockItem.STURDY_NUGGET.get()
        );

        tag(Tags.Items.INGOTS).add(
                AllBlockItem.STURDY_BRICK.get()
        );

        // --- Vanilla Tags ---
        tag(ItemTags.TRIMMABLE_ARMOR).add(
                AllBlockItem.STURDY_CHESTPLATE.get(),
                AllBlockItem.STURDY_HELMET.get(),
                AllBlockItem.STURDY_LEGGINGS.get(),
                AllBlockItem.STURDY_BOOTS.get()
        );

        // --- Custom Mod Tags ---
        tag(Alltags.Items.OVERBURNT).add(
                AllBlockItem.BURNT_BEEF.get(),
                AllBlockItem.BURNT_CHICKEN.get(),
                AllBlockItem.BURNT_COD.get(),
                AllBlockItem.BURNT_MUTTON.get(),
                AllBlockItem.BURNT_SALMON.get(),
                AllBlockItem.BURNT_PORKCHOP.get(),
                AllBlockItem.BURNT_RABBIT.get()
        );

        tag(Alltags.Items.DOUGH).add(AllBlockItem.WHEAT_DOUGH.get());
        tag(Alltags.Items.FLOUR).add(AllBlockItem.WHEAT_FLOUR.get());
        tag(Alltags.Items.SHOULD_DISPLAY_ITEM).add(Items.REDSTONE);

        // --- Anvil items ---
        tag(ItemTags.ANVIL).add(
                AllBlockItem.STURDY_ANVIL.get().asItem(),
                AllBlockItem.CHIPPED_STURDY_ANVIL.get().asItem(),
                AllBlockItem.DAMAGED_STURDY_ANVIL.get().asItem()
        );

        // --- Sturdy shears / Soap ---
        // 1.20.1 has no MINING/DURABILITY/VANISHING_ENCHANTABLE tags; use the
        // closest equivalents that exist (Forge tools/shears + Tags.Items).
        tag(Tags.Items.SHEARS).add(AllBlockItem.STURDY_SHEARS.get());

        // --- Piglin Loved items ---
        tag(ItemTags.PIGLIN_LOVED).add(
                AllBlockItem.GOLDEN_CAULDRON.get().asItem(),
                AllBlockItem.GOLD_BARS.get().asItem(),
                AllBlockItem.GOLDEN_BRICKS.get().asItem(),
                AllBlockItem.GOLDEN_BRICK_STAIR.get().asItem(),
                AllBlockItem.GOLDEN_BRICK_SLAB.get().asItem(),
                AllBlockItem.GOLDEN_BRICK_TILE.get().asItem(),
                AllBlockItem.GOLDEN_BRICK_TILE_STAIR.get().asItem(),
                AllBlockItem.GOLDEN_BRICK_TILE_SLAB.get().asItem(),
                AllBlockItem.GOLDEN_BRICK_TILE_WALL.get().asItem()
        );

        // --- Boats ---
        tag(ItemTags.BOATS).add(AllBlockItem.BURNT_BOAT.get().asItem());
        tag(ItemTags.CHEST_BOATS).add(AllBlockItem.BURNT_CHEST_BOAT.get().asItem());

        // --- Burnt Wood Items (item form of the corresponding block tags) ---
        tag(ItemTags.LOGS_THAT_BURN).add(
                AllBlockItem.BURNT_LOG.get().asItem(),
                AllBlockItem.BURNT_WOOD.get().asItem(),
                AllBlockItem.BURNT_BIRCH_LOG.get().asItem(),
                AllBlockItem.BURNT_BIRCH_WOOD.get().asItem(),
                AllBlockItem.BURNT_JUNGLE_LOG.get().asItem(),
                AllBlockItem.BURNT_JUNGLE_WOOD.get().asItem(),
                AllBlockItem.BURNT_CHERRY_LOG.get().asItem(),
                AllBlockItem.BURNT_CHERRY_WOOD.get().asItem(),
                AllBlockItem.BURNT_MANGROVE_LOG.get().asItem(),
                AllBlockItem.BURNT_MANGROVE_WOOD.get().asItem()
        );

        tag(ItemTags.PIGLIN_REPELLENTS).add(AllBlockItem.BURNABLE_SOUL_CAMPFIRE.get().asItem());

        tag(Alltags.Items.BURNT_LOG).add(
                AllBlockItem.BURNT_LOG.get().asItem(),
                AllBlockItem.BURNT_WOOD.get().asItem(),
                AllBlockItem.BURNT_BIRCH_LOG.get().asItem(),
                AllBlockItem.BURNT_BIRCH_WOOD.get().asItem(),
                AllBlockItem.BURNT_JUNGLE_LOG.get().asItem(),
                AllBlockItem.BURNT_JUNGLE_WOOD.get().asItem(),
                AllBlockItem.BURNT_CHERRY_LOG.get().asItem(),
                AllBlockItem.BURNT_CHERRY_WOOD.get().asItem(),
                AllBlockItem.BURNT_MANGROVE_LOG.get().asItem(),
                AllBlockItem.BURNT_MANGROVE_WOOD.get().asItem()
        );

        tag(ItemTags.LOGS).add(
                AllBlockItem.BURNT_LOG.get().asItem(),
                AllBlockItem.BURNT_WOOD.get().asItem(),
                AllBlockItem.BURNT_BIRCH_LOG.get().asItem(),
                AllBlockItem.BURNT_BIRCH_WOOD.get().asItem(),
                AllBlockItem.BURNT_JUNGLE_LOG.get().asItem(),
                AllBlockItem.BURNT_JUNGLE_WOOD.get().asItem(),
                AllBlockItem.BURNT_CHERRY_LOG.get().asItem(),
                AllBlockItem.BURNT_CHERRY_WOOD.get().asItem(),
                AllBlockItem.BURNT_MANGROVE_LOG.get().asItem(),
                AllBlockItem.BURNT_MANGROVE_WOOD.get().asItem()
        );

        tag(ItemTags.PLANKS).add(AllBlockItem.BURNT_PLANKS.get().asItem());
        tag(ItemTags.WOODEN_STAIRS).add(AllBlockItem.BURNT_STAIRS.get().asItem());
        tag(ItemTags.WOODEN_SLABS).add(AllBlockItem.BURNT_SLAB.get().asItem());
        tag(ItemTags.WOODEN_FENCES).add(AllBlockItem.BURNT_FENCE.get().asItem());
        tag(ItemTags.WOODEN_DOORS).add(AllBlockItem.BURNT_DOOR.get().asItem());
        tag(ItemTags.WOODEN_TRAPDOORS).add(AllBlockItem.BURNT_TRAPDOOR.get().asItem());
        tag(ItemTags.WOODEN_BUTTONS).add(AllBlockItem.BURNT_BUTTON.get().asItem());
        tag(ItemTags.WOODEN_PRESSURE_PLATES).add(AllBlockItem.BURNT_PRESSURE_PLATE.get().asItem());
    }
}
