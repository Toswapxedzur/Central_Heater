package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.misc.Alltags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import java.util.concurrent.CompletableFuture;

public class GeneratorItemTag extends ItemTagsProvider {
    public GeneratorItemTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Items.BRICKS).add(AllBlockItem.MUD_BRICK.asItem(), AllBlockItem.STONE_BRICK.asItem(), AllBlockItem.DEEPSLATE_BRICK.asItem(), AllBlockItem.RED_NETHER_BRICK.asItem(), AllBlockItem.MUD_BRICK.asItem());
        tag(Tags.Items.NUGGETS).add(AllBlockItem.DIAMOND_SHARD.asItem(), AllBlockItem.STURDY_NUGGET.asItem());
        tag(Tags.Items.INGOTS).add(AllBlockItem.STURDY_BRICK.asItem());

        tag(ItemTags.TRIMMABLE_ARMOR).add(AllBlockItem.STURDY_CHESTPLATE.asItem(), AllBlockItem.STURDY_HELMET.asItem(),
                AllBlockItem.STURDY_LEGGINGS.asItem(), AllBlockItem.STURDY_BOOTS.asItem());

        tag(Alltags.Items.OVERBURNT).add(AllBlockItem.BURNT_BEEF.asItem(), AllBlockItem.BURNT_CHICKEN.asItem(), AllBlockItem.BURNT_COD.asItem(),
                AllBlockItem.BURNT_MUTTON.asItem(), AllBlockItem.BURNT_SALMON.asItem(), AllBlockItem.BURNT_PORKCHOP.asItem(),
                AllBlockItem.BURNT_RABBIT.asItem());

        tag(Alltags.Items.DOUGH).add(AllBlockItem.WHEAT_DOUGH.asItem());
        tag(Alltags.Items.FLOUR).add(AllBlockItem.WHEAT_FLOUR.asItem());

        tag(Alltags.Items.SHOULD_DISPLAY_ITEM).add(Items.REDSTONE);

        tag(ItemTags.ANVIL).add(AllBlockItem.STURDY_ANVIL.asItem(), AllBlockItem.CHIPPED_STURDY_ANVIL.asItem(), AllBlockItem.DAMAGED_STURDY_ANVIL.asItem());

        tag(ItemTags.MINING_ENCHANTABLE).add(AllBlockItem.STURDY_SHEARS.asItem());
        tag(ItemTags.DURABILITY_ENCHANTABLE).add(AllBlockItem.STURDY_SHEARS.asItem(), AllBlockItem.SOAP.asItem());
        tag(Tags.Items.TOOLS_SHEAR).add(AllBlockItem.STURDY_SHEARS.asItem());
        tag(ItemTags.VANISHING_ENCHANTABLE).add(AllBlockItem.STURDY_SHEARS.asItem(), AllBlockItem.SOAP.asItem());

        tag(ItemTags.PIGLIN_LOVED).add(
                AllBlockItem.GOLDEN_CAULDRON.asItem(),
                AllBlockItem.GOLD_BARS.asItem(),
                AllBlockItem.GOLDEN_BRICKS.asItem(),
                AllBlockItem.GOLDEN_BRICK_STAIR.asItem(),
                AllBlockItem.GOLDEN_BRICK_SLAB.asItem(),
                AllBlockItem.GOLDEN_BRICK_TILE.asItem(),
                AllBlockItem.GOLDEN_BRICK_TILE_STAIR.asItem(),
                AllBlockItem.GOLDEN_BRICK_TILE_SLAB.asItem(),
                AllBlockItem.GOLDEN_BRICK_TILE_WALL.asItem()
        );

        // --- Boats ---
        tag(ItemTags.BOATS).add(AllBlockItem.BURNT_BOAT.asItem());
        tag(ItemTags.CHEST_BOATS).add(AllBlockItem.BURNT_CHEST_BOAT.asItem());

        // --- Burnt Wood Items ---
        tag(ItemTags.LOGS_THAT_BURN).add(
                AllBlockItem.BURNT_LOG.asItem(),
                AllBlockItem.BURNT_WOOD.asItem(),
                AllBlockItem.BURNT_BIRCH_LOG.asItem(),
                AllBlockItem.BURNT_BIRCH_WOOD.asItem(),
                AllBlockItem.BURNT_JUNGLE_LOG.asItem(),
                AllBlockItem.BURNT_JUNGLE_WOOD.asItem(),
                AllBlockItem.BURNT_CHERRY_LOG.asItem(),
                AllBlockItem.BURNT_CHERRY_WOOD.asItem(),
                AllBlockItem.BURNT_MANGROVE_LOG.asItem(),
                AllBlockItem.BURNT_MANGROVE_WOOD.asItem()
        );

        tag(ItemTags.PIGLIN_REPELLENTS).add(AllBlockItem.BURNABLE_SOUL_CAMPFIRE.asItem());

        tag(Alltags.Items.BURNT_LOG).add(
                AllBlockItem.BURNT_LOG.asItem(),
                AllBlockItem.BURNT_WOOD.asItem(),
                AllBlockItem.BURNT_BIRCH_LOG.asItem(),
                AllBlockItem.BURNT_BIRCH_WOOD.asItem(),
                AllBlockItem.BURNT_JUNGLE_LOG.asItem(),
                AllBlockItem.BURNT_JUNGLE_WOOD.asItem(),
                AllBlockItem.BURNT_CHERRY_LOG.asItem(),
                AllBlockItem.BURNT_CHERRY_WOOD.asItem(),
                AllBlockItem.BURNT_MANGROVE_LOG.asItem(),
                AllBlockItem.BURNT_MANGROVE_WOOD.asItem()
        );

        tag(ItemTags.LOGS).add(
                AllBlockItem.BURNT_LOG.asItem(),
                AllBlockItem.BURNT_WOOD.asItem(),
                AllBlockItem.BURNT_BIRCH_LOG.asItem(),
                AllBlockItem.BURNT_BIRCH_WOOD.asItem(),
                AllBlockItem.BURNT_JUNGLE_LOG.asItem(),
                AllBlockItem.BURNT_JUNGLE_WOOD.asItem(),
                AllBlockItem.BURNT_CHERRY_LOG.asItem(),
                AllBlockItem.BURNT_CHERRY_WOOD.asItem(),
                AllBlockItem.BURNT_MANGROVE_LOG.asItem(),
                AllBlockItem.BURNT_MANGROVE_WOOD.asItem()
        );

        tag(ItemTags.PLANKS).add(AllBlockItem.BURNT_PLANKS.asItem());
        tag(ItemTags.WOODEN_STAIRS).add(AllBlockItem.BURNT_STAIRS.asItem());
        tag(ItemTags.WOODEN_SLABS).add(AllBlockItem.BURNT_SLAB.asItem());
        tag(ItemTags.WOODEN_FENCES).add(AllBlockItem.BURNT_FENCE.asItem());
        tag(ItemTags.WOODEN_DOORS).add(AllBlockItem.BURNT_DOOR.asItem());
        tag(ItemTags.WOODEN_TRAPDOORS).add(AllBlockItem.BURNT_TRAPDOOR.asItem());
        tag(ItemTags.WOODEN_BUTTONS).add(AllBlockItem.BURNT_BUTTON.asItem());
        tag(ItemTags.WOODEN_PRESSURE_PLATES).add(AllBlockItem.BURNT_PRESSURE_PLATE.asItem());

        for (AllBlockItem.BurntWoodSet set : AllBlockItem.BURNT_WOOD_SETS) {
            tag(ItemTags.LOGS_THAT_BURN).add(set.log().asItem(), set.wood().asItem());
            tag(Alltags.Items.BURNT_LOG).add(set.log().asItem(), set.wood().asItem());
            tag(ItemTags.LOGS).add(set.log().asItem(), set.wood().asItem());
            tag(ItemTags.PLANKS).add(set.planks().asItem());
            tag(ItemTags.WOODEN_STAIRS).add(set.stairs().asItem());
            tag(ItemTags.WOODEN_SLABS).add(set.slab().asItem());
            tag(ItemTags.WOODEN_FENCES).add(set.fence().asItem());
            tag(ItemTags.FENCE_GATES).add(set.fenceGate().asItem());
            tag(ItemTags.WOODEN_DOORS).add(set.door().asItem());
            tag(ItemTags.WOODEN_TRAPDOORS).add(set.trapdoor().asItem());
            tag(ItemTags.WOODEN_BUTTONS).add(set.button().asItem());
            tag(ItemTags.WOODEN_PRESSURE_PLATES).add(set.pressurePlate().asItem());
            tag(Alltags.Items.SIMILAR_STACKABLE).add(set.log().asItem(), set.wood().asItem(), set.planks().asItem(),
                    set.stairs().asItem(), set.slab().asItem(), set.fence().asItem(), set.fenceGate().asItem(),
                    set.trapdoor().asItem(), set.button().asItem(), set.pressurePlate().asItem(), set.door().asItem());
        }
    }
}
