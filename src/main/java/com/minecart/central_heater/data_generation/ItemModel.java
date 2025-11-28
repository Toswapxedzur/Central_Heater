package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.Central_heater;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;

public class ItemModel extends ItemModelProvider {
    public ItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Central_heater.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("stone_stove", modLoc("block/stone_stove_off"));
        withExistingParent("red_nether_brick_stove", modLoc("block/red_nether_brick_stove_off"));
        withExistingParent("brick_stove", modLoc("block/brick_stove_off"));
        withExistingParent("deepslate_stove", modLoc("block/deepslate_stove_off"));
        withExistingParent("nether_brick_stove", modLoc("block/nether_brick_stove_off"));
        withExistingParent("mud_brick_stove", modLoc("block/mud_brick_stove_off"));
        withExistingParent("blackstone_stove", modLoc("block/blackstone_stove_off"));
        withExistingParent("stone_brick_tile_stair", modLoc("block/stone_brick_tile_stair"));
        withExistingParent("stone_brick_tile_slab", modLoc("block/stone_brick_tile_slab"));
        wallInventory("stone_brick_tile_wall", modLoc("block/stone_brick_tile"));
        withExistingParent("deepslate_brick_tile_stair", modLoc("block/deepslate_brick_tile_stair"));
        withExistingParent("deepslate_brick_tile_slab", modLoc("block/deepslate_brick_tile_slab"));
        wallInventory("deepslate_brick_tile_wall", modLoc("block/deepslate_brick_tile"));
        withExistingParent("blackstone_brick_tile_stair", modLoc("block/blackstone_brick_tile_stair"));
        withExistingParent("blackstone_brick_tile_slab", modLoc("block/blackstone_brick_tile_slab"));
        wallInventory("blackstone_brick_tile_wall", modLoc("block/blackstone_brick_tile"));
        withExistingParent("mud_brick_tile_stair", modLoc("block/mud_brick_tile_stair"));
        withExistingParent("mud_brick_tile_slab", modLoc("block/mud_brick_tile_slab"));
        wallInventory("mud_brick_tile_wall", modLoc("block/mud_brick_tile"));
        basicItem(AllBlockItem.cobble.get());
        basicItem(AllBlockItem.deepslate_cobble.get());
        basicItem(AllBlockItem.stone_brick.get());
        basicItem(AllBlockItem.mud_brick.get());
        basicItem(AllBlockItem.red_nether_brick.get());
        basicItem(AllBlockItem.deepslate_brick.get());
        basicItem(AllBlockItem.diamond_shard.get());
        basicItem(AllBlockItem.blackstone_brick.get());
        basicItemWithTexture(AllBlockItem.gold_bars.asItem(), "block/gold_bars");
    }

    public ItemModelBuilder basicItemWithTexture(Item item, String key) {
        return getBuilder(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)).toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc(key));
    }
}
