package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.Central_heater;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

import static com.ibm.icu.util.LocalePriorityList.add;

public class GeneratorBlockTag extends BlockTagsProvider {
    public GeneratorBlockTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, Central_heater.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AllBlockItem.brick_stove.get(), AllBlockItem.mud_brick_stove.get(), AllBlockItem.stone_stove.get(),
                AllBlockItem.deepslate_stove.get(), AllBlockItem.nether_brick_stove.get(), AllBlockItem.red_nether_brick_stove.get(), AllBlockItem.blackstone_stove.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AllBlockItem.mud_brick_pot.get(), AllBlockItem.stone_pot.get(), AllBlockItem.deepslate_pot.get(),
                AllBlockItem.iron_cauldron.get(), AllBlockItem.golden_cauldron.get());

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AllBlockItem.stone_brick_tile.get(), AllBlockItem.stone_brick_tile_stair.get(), AllBlockItem.stone_brick_tile_slab.get(), AllBlockItem.stone_brick_tile_wall.get(),
                AllBlockItem.deepslate_brick_tile.get(), AllBlockItem.deepslate_brick_tile_stair.get(), AllBlockItem.deepslate_brick_tile_slab.get(), AllBlockItem.deepslate_brick_tile_wall.get(),
                AllBlockItem.mud_brick_tile.get(), AllBlockItem.mud_brick_tile_stair.get(), AllBlockItem.mud_brick_tile_slab.get(), AllBlockItem.mud_brick_tile_wall.get(),
                AllBlockItem.blackstone_brick_tile.get(), AllBlockItem.blackstone_brick_tile_stair.get(), AllBlockItem.blackstone_brick_tile_slab.get(), AllBlockItem.blackstone_brick_tile_wall.get(),
                AllBlockItem.sturdy_brick_tile.get(), AllBlockItem.sturdy_brick_tile_stair.get(), AllBlockItem.sturdy_brick_tile_slab.get(), AllBlockItem.sturdy_brick_tile_wall.get()
                );

        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(AllBlockItem.gold_bars.get(), AllBlockItem.sturdy_tank.get());

        tag(BlockTags.NEEDS_STONE_TOOL).add(AllBlockItem.nether_brick_stove.get(), AllBlockItem.red_nether_brick_stove.get(), AllBlockItem.iron_cauldron.get(), AllBlockItem.golden_cauldron.get(), AllBlockItem.gold_bars.get());

        tag(BlockTags.STAIRS).add(AllBlockItem.stone_brick_tile_stair.value(), AllBlockItem.deepslate_brick_tile_stair.get(), AllBlockItem.mud_brick_tile_stair.get(),
                AllBlockItem.blackstone_brick_tile_stair.get(), AllBlockItem.sturdy_brick_tile_stair.get());
        tag(BlockTags.SLABS).add(AllBlockItem.stone_brick_tile_slab.value(), AllBlockItem.deepslate_brick_tile_slab.get(), AllBlockItem.mud_brick_tile_slab.get(),
                AllBlockItem.blackstone_brick_tile_slab.get(), AllBlockItem.sturdy_brick_tile_slab.get());
        tag(BlockTags.WALLS).add(AllBlockItem.stone_brick_tile_wall.value(), AllBlockItem.deepslate_brick_tile_wall.get(), AllBlockItem.mud_brick_tile_wall.get(),
                AllBlockItem.blackstone_brick_tile_wall.get(), AllBlockItem.sturdy_brick_tile_wall.get());
    }
}
