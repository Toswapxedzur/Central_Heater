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
    }
}
