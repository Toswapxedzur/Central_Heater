package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.Central_heater;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class BlockState extends BlockStateProvider {
    public BlockState(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Central_heater.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(AllBlockItem.stone_brick_tile.get(), cubeAll(AllBlockItem.stone_brick_tile.get()));
        simpleBlockWithItem(AllBlockItem.deepslate_brick_tile.get(), cubeAll(AllBlockItem.deepslate_brick_tile.get()));
        simpleBlockWithItem(AllBlockItem.mud_brick_tile.get(), cubeAll(AllBlockItem.mud_brick_tile.get()));
        simpleBlockWithItem(AllBlockItem.blackstone_brick_tile.get(), cubeAll(AllBlockItem.blackstone_brick_tile.get()));

        stairsBlock(AllBlockItem.stone_brick_tile_stair.get(), blockTexture(AllBlockItem.stone_brick_tile.get()));
        slabBlock(AllBlockItem.stone_brick_tile_slab.get(), blockTexture(AllBlockItem.stone_brick_tile.get()), blockTexture(AllBlockItem.stone_brick_tile.get()));
        wallBlock(AllBlockItem.stone_brick_tile_wall.get(), blockTexture(AllBlockItem.stone_brick_tile.get()));

        stairsBlock(AllBlockItem.deepslate_brick_tile_stair.get(), blockTexture(AllBlockItem.deepslate_brick_tile.get()));
        slabBlock(AllBlockItem.deepslate_brick_tile_slab.get(), blockTexture(AllBlockItem.deepslate_brick_tile.get()), blockTexture(AllBlockItem.deepslate_brick_tile.get()));
        wallBlock(AllBlockItem.deepslate_brick_tile_wall.get(), blockTexture(AllBlockItem.deepslate_brick_tile.get()));

        stairsBlock(AllBlockItem.mud_brick_tile_stair.get(), blockTexture(AllBlockItem.mud_brick_tile.get()));
        slabBlock(AllBlockItem.mud_brick_tile_slab.get(), blockTexture(AllBlockItem.mud_brick_tile.get()), blockTexture(AllBlockItem.mud_brick_tile.get()));
        wallBlock(AllBlockItem.mud_brick_tile_wall.get(), blockTexture(AllBlockItem.mud_brick_tile.get()));

        stairsBlock(AllBlockItem.blackstone_brick_tile_stair.get(), blockTexture(AllBlockItem.blackstone_brick_tile.get()));
        slabBlock(AllBlockItem.blackstone_brick_tile_slab.get(), blockTexture(AllBlockItem.blackstone_brick_tile.get()), blockTexture(AllBlockItem.blackstone_brick_tile.get()));
        wallBlock(AllBlockItem.blackstone_brick_tile_wall.get(), blockTexture(AllBlockItem.blackstone_brick_tile.get()));

        simpleBlockWithItem(AllBlockItem.iron_lid.get(), models().getExistingFile(modLoc("block/iron_lid")));
        simpleBlockWithItem(AllBlockItem.gold_lid.get(), models().getExistingFile(modLoc("block/gold_lid")));

        simpleBlockWithItem(AllBlockItem.brick_pot.get(), models().getExistingFile(modLoc("block/brick_pot")));
        simpleBlockWithItem(AllBlockItem.mud_brick_pot.get(), models().getExistingFile(modLoc("block/mud_brick_pot")));
        simpleBlockWithItem(AllBlockItem.stone_pot.get(), models().getExistingFile(modLoc("block/stone_pot")));
        simpleBlockWithItem(AllBlockItem.deepslate_pot.get(), models().getExistingFile(modLoc("block/deepslate_pot")));
        simpleBlockWithItem(AllBlockItem.red_nether_brick_pot.get(), models().getExistingFile(modLoc("block/red_nether_brick_pot")));
        simpleBlockWithItem(AllBlockItem.nether_brick_pot.get(), models().getExistingFile(modLoc("block/nether_brick_pot")));
        simpleBlockWithItem(AllBlockItem.blackstone_pot.get(), models().getExistingFile(modLoc("block/blackstone_pot")));
    }
}
