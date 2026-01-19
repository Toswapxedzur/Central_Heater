package com.minecart.central_heater.data_generation.client;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class GeneratorBlockState extends BlockStateProvider {
    public GeneratorBlockState(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, CentralHeater.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(AllBlockItem.STONE_BRICK_TILE.get(), cubeAll(AllBlockItem.STONE_BRICK_TILE.get()));
        simpleBlockWithItem(AllBlockItem.DEEPSLATE_BRICK_TILE.get(), cubeAll(AllBlockItem.DEEPSLATE_BRICK_TILE.get()));
        simpleBlockWithItem(AllBlockItem.MUD_BRICK_TILE.get(), cubeAll(AllBlockItem.MUD_BRICK_TILE.get()));
        simpleBlockWithItem(AllBlockItem.BLACKSTONE_BRICK_TILE.get(), cubeAll(AllBlockItem.BLACKSTONE_BRICK_TILE.get()));
        simpleBlockWithItem(AllBlockItem.STURDY_BRICK_TILE.get(), cubeAll(AllBlockItem.STURDY_BRICK_TILE.get()));

        stairsBlock(AllBlockItem.STONE_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.STONE_BRICK_TILE.get()));
        slabBlock(AllBlockItem.STONE_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.STONE_BRICK_TILE.get()), blockTexture(AllBlockItem.STONE_BRICK_TILE.get()));
        wallBlock(AllBlockItem.STONE_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.STONE_BRICK_TILE.get()));

        stairsBlock(AllBlockItem.DEEPSLATE_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.DEEPSLATE_BRICK_TILE.get()));
        slabBlock(AllBlockItem.DEEPSLATE_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.DEEPSLATE_BRICK_TILE.get()), blockTexture(AllBlockItem.DEEPSLATE_BRICK_TILE.get()));
        wallBlock(AllBlockItem.DEEPSLATE_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.DEEPSLATE_BRICK_TILE.get()));

        stairsBlock(AllBlockItem.MUD_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.MUD_BRICK_TILE.get()));
        slabBlock(AllBlockItem.MUD_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.MUD_BRICK_TILE.get()), blockTexture(AllBlockItem.MUD_BRICK_TILE.get()));
        wallBlock(AllBlockItem.MUD_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.MUD_BRICK_TILE.get()));

        stairsBlock(AllBlockItem.STURDY_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.STURDY_BRICK_TILE.get()));
        slabBlock(AllBlockItem.STURDY_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.STURDY_BRICK_TILE.get()), blockTexture(AllBlockItem.STURDY_BRICK_TILE.get()));
        wallBlock(AllBlockItem.STURDY_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.STURDY_BRICK_TILE.get()));

        stairsBlock(AllBlockItem.BLACKSTONE_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.BLACKSTONE_BRICK_TILE.get()));
        slabBlock(AllBlockItem.BLACKSTONE_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.BLACKSTONE_BRICK_TILE.get()), blockTexture(AllBlockItem.BLACKSTONE_BRICK_TILE.get()));
        wallBlock(AllBlockItem.BLACKSTONE_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.BLACKSTONE_BRICK_TILE.get()));

        simpleBlockWithItem(AllBlockItem.MUD_BRICK_POT.get(), models().getExistingFile(modLoc("block/mud_brick_pot")));
        simpleBlock(AllBlockItem.BRICK_CAULDRON.get(), models().getExistingFile(modLoc("block/brick_cauldron")));
        simpleBlock(AllBlockItem.CLAY_CAULDRON.get(), models().getExistingFile(modLoc("block/clay_cauldron")));
        simpleBlock(AllBlockItem.IRON_CAULDRON.get(), models().getExistingFile(mcLoc("block/cauldron")));
        simpleBlock(AllBlockItem.GOLDEN_CAULDRON.get(), models().getExistingFile(modLoc("block/golden_cauldron")));

        simpleBlock(AllBlockItem.STURDY_TANK.get(), models().getExistingFile(modLoc("block/sturdy_tank")));
    }
}