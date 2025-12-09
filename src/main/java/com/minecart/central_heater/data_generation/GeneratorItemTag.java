package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.util.Alltags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GeneratorItemTag extends ItemTagsProvider {
    public GeneratorItemTag(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(Tags.Items.BRICKS).add(AllBlockItem.mud_brick.asItem(), AllBlockItem.stone_brick.asItem(), AllBlockItem.deepslate_brick.asItem(), AllBlockItem.red_nether_brick.asItem());
        tag(Tags.Items.NUGGETS).add(AllBlockItem.diamond_shard.asItem());

        tag(ItemTags.TRIMMABLE_ARMOR).add(AllBlockItem.sturdy_chestplate.asItem(), AllBlockItem.sturdy_helmet.asItem(),
                AllBlockItem.sturdy_leggings.asItem(), AllBlockItem.sturdy_boots.asItem());

        tag(Alltags.Items.OVERBURNT).add(AllBlockItem.burnt_beef.asItem(), AllBlockItem.burnt_chicken.asItem(), AllBlockItem.burnt_cod.asItem(),
                AllBlockItem.burnt_mutton.asItem(), AllBlockItem.burnt_salmon.asItem(), AllBlockItem.burnt_porkchop.asItem(),
                AllBlockItem.burnt_rabbit.asItem(), AllBlockItem.burnt_potato.asItem());
    }
}
