package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.misc.Alltags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
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
        // Forge 1.20.1 requires the ModID and ExistingFileHelper in the constructor
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
    }
}