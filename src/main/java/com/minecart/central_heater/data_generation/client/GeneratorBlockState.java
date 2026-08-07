package com.minecart.central_heater.data_generation.client;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.block.misc.BurntLogBlock;
import com.minecart.central_heater.block.stove.CopperStoveBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
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

        horizontalBlock(AllBlockItem.STURDY_ANVIL.get(), models().getExistingFile(modLoc("block/sturdy_anvil")));
        horizontalBlock(AllBlockItem.CHIPPED_STURDY_ANVIL.get(), models().getExistingFile(modLoc("block/chipped_sturdy_anvil")));
        horizontalBlock(AllBlockItem.DAMAGED_STURDY_ANVIL.get(), models().getExistingFile(modLoc("block/damaged_sturdy_anvil")));

        // Coal Brick Family
        simpleBlock(AllBlockItem.COAL_BRICKS.get());
        stairsBlock(AllBlockItem.COAL_BRICK_STAIR.get(), blockTexture(AllBlockItem.COAL_BRICKS.get()));
        slabBlock(AllBlockItem.COAL_BRICK_SLAB.get(), blockTexture(AllBlockItem.COAL_BRICKS.get()), blockTexture(AllBlockItem.COAL_BRICKS.get()));
        simpleBlock(AllBlockItem.COAL_BRICK_TILE.get());
        stairsBlock(AllBlockItem.COAL_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.COAL_BRICK_TILE.get()));
        slabBlock(AllBlockItem.COAL_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.COAL_BRICK_TILE.get()), blockTexture(AllBlockItem.COAL_BRICK_TILE.get()));
        wallBlock(AllBlockItem.COAL_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.COAL_BRICK_TILE.get()));

        // Golden Brick Family
        simpleBlock(AllBlockItem.GOLDEN_BRICKS.get());
        stairsBlock(AllBlockItem.GOLDEN_BRICK_STAIR.get(), blockTexture(AllBlockItem.GOLDEN_BRICKS.get()));
        slabBlock(AllBlockItem.GOLDEN_BRICK_SLAB.get(), blockTexture(AllBlockItem.GOLDEN_BRICKS.get()), blockTexture(AllBlockItem.GOLDEN_BRICKS.get()));
        simpleBlock(AllBlockItem.GOLDEN_BRICK_TILE.get());
        stairsBlock(AllBlockItem.GOLDEN_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.GOLDEN_BRICK_TILE.get()));
        slabBlock(AllBlockItem.GOLDEN_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.GOLDEN_BRICK_TILE.get()), blockTexture(AllBlockItem.GOLDEN_BRICK_TILE.get()));
        wallBlock(AllBlockItem.GOLDEN_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.GOLDEN_BRICK_TILE.get()));

        // Netherite Brick Family
        simpleBlock(AllBlockItem.NETHERITE_BRICKS.get());
        stairsBlock(AllBlockItem.NETHERITE_BRICK_STAIR.get(), blockTexture(AllBlockItem.NETHERITE_BRICKS.get()));
        slabBlock(AllBlockItem.NETHERITE_BRICK_SLAB.get(), blockTexture(AllBlockItem.NETHERITE_BRICKS.get()), blockTexture(AllBlockItem.NETHERITE_BRICKS.get()));
        simpleBlock(AllBlockItem.NETHERITE_BRICK_TILE.get());
        stairsBlock(AllBlockItem.NETHERITE_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.NETHERITE_BRICK_TILE.get()));
        slabBlock(AllBlockItem.NETHERITE_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.NETHERITE_BRICK_TILE.get()), blockTexture(AllBlockItem.NETHERITE_BRICK_TILE.get()));
        wallBlock(AllBlockItem.NETHERITE_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.NETHERITE_BRICK_TILE.get()));

        // Scorched Brick Family
        simpleBlock(AllBlockItem.SCORCHED_BRICKS.get());
        stairsBlock(AllBlockItem.SCORCHED_BRICK_STAIR.get(), blockTexture(AllBlockItem.SCORCHED_BRICKS.get()));
        slabBlock(AllBlockItem.SCORCHED_BRICK_SLAB.get(), blockTexture(AllBlockItem.SCORCHED_BRICKS.get()), blockTexture(AllBlockItem.SCORCHED_BRICKS.get()));
        simpleBlock(AllBlockItem.SCORCHED_BRICK_TILE.get());
        stairsBlock(AllBlockItem.SCORCHED_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.SCORCHED_BRICK_TILE.get()));
        slabBlock(AllBlockItem.SCORCHED_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.SCORCHED_BRICK_TILE.get()), blockTexture(AllBlockItem.SCORCHED_BRICK_TILE.get()));
        wallBlock(AllBlockItem.SCORCHED_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.SCORCHED_BRICK_TILE.get()));

        // Sturdy Bricks (no wall variant in 1.21.1)
        simpleBlock(AllBlockItem.STURDY_BRICKS.get());
        stairsBlock(AllBlockItem.STURDY_BRICK_STAIR.get(), modLoc("block/sturdy_bricks"));
        slabBlock(AllBlockItem.STURDY_BRICK_SLAB.get(), modLoc("block/sturdy_bricks"), modLoc("block/sturdy_bricks"));

        // Burnt Door
        doorBlockWithRenderType(AllBlockItem.BURNT_DOOR.get(),
                modLoc("block/burnt_door_bottom"),
                modLoc("block/burnt_door_top"),
                "cutout");

        // Copper Stoves: weathering series + waxed series share the underlying model name
        copperStoveBlockState(AllBlockItem.COPPER_STOVE.get(), "copper_stove");
        copperStoveBlockState(AllBlockItem.EXPOSED_COPPER_STOVE.get(), "exposed_copper_stove");
        copperStoveBlockState(AllBlockItem.WEATHERED_COPPER_STOVE.get(), "weathered_copper_stove");
        copperStoveBlockState(AllBlockItem.OXIDIZED_COPPER_STOVE.get(), "oxidized_copper_stove");

        copperStoveBlockState(AllBlockItem.WAXED_COPPER_STOVE.get(), "copper_stove");
        copperStoveBlockState(AllBlockItem.WAXED_EXPOSED_COPPER_STOVE.get(), "exposed_copper_stove");
        copperStoveBlockState(AllBlockItem.WAXED_WEATHERED_COPPER_STOVE.get(), "weathered_copper_stove");
        copperStoveBlockState(AllBlockItem.WAXED_OXIDIZED_COPPER_STOVE.get(), "oxidized_copper_stove");

        // Generic Burnt Wood Set
        simpleBlock(AllBlockItem.BURNT_PLANKS.get());
        stairsBlock(AllBlockItem.BURNT_STAIRS.get(), blockTexture(AllBlockItem.BURNT_PLANKS.get()));
        slabBlock(AllBlockItem.BURNT_SLAB.get(), blockTexture(AllBlockItem.BURNT_PLANKS.get()), blockTexture(AllBlockItem.BURNT_PLANKS.get()));
        fenceBlock(AllBlockItem.BURNT_FENCE.get(), blockTexture(AllBlockItem.BURNT_PLANKS.get()));
        fenceGateBlock(AllBlockItem.BURNT_FENCE_GATE.get(), blockTexture(AllBlockItem.BURNT_PLANKS.get()));
        trapdoorBlockWithRenderType(AllBlockItem.BURNT_TRAPDOOR.get(), blockTexture(AllBlockItem.BURNT_TRAPDOOR.get()), true, "cutout");
        buttonBlock(AllBlockItem.BURNT_BUTTON.get(), blockTexture(AllBlockItem.BURNT_PLANKS.get()));
        pressurePlateBlock(AllBlockItem.BURNT_PRESSURE_PLATE.get(), blockTexture(AllBlockItem.BURNT_PLANKS.get()));

        // Burnt Logs (per-wood + generic) with directional + layered states
        burntLogBlockState(AllBlockItem.BURNT_LOG.get(), "burnt_log");
        burntLogBlockState(AllBlockItem.BURNT_BIRCH_LOG.get(), "burnt_birch_log");
        burntLogBlockState(AllBlockItem.BURNT_JUNGLE_LOG.get(), "burnt_jungle_log");
        burntLogBlockState(AllBlockItem.BURNT_CHERRY_LOG.get(), "burnt_cherry_log");
        burntLogBlockState(AllBlockItem.BURNT_MANGROVE_LOG.get(), "burnt_mangrove_log");

        // Burnt Woods (full cube_column variants per axis)
        axisBlock(AllBlockItem.BURNT_WOOD.get(), modLoc("block/burnt_log"), modLoc("block/burnt_log"));
        axisBlock(AllBlockItem.BURNT_BIRCH_WOOD.get(), modLoc("block/burnt_birch_log"), modLoc("block/burnt_birch_log"));
        axisBlock(AllBlockItem.BURNT_JUNGLE_WOOD.get(), modLoc("block/burnt_jungle_log"), modLoc("block/burnt_jungle_log"));
        axisBlock(AllBlockItem.BURNT_CHERRY_WOOD.get(), modLoc("block/burnt_cherry_log"), modLoc("block/burnt_cherry_log"));
        axisBlock(AllBlockItem.BURNT_MANGROVE_WOOD.get(), modLoc("block/burnt_mangrove_log"), modLoc("block/burnt_mangrove_log"));
    }

    /**
     * Builds a CopperStove blockstate. The model name pattern matches the
     * GeneratorBlockModel output: {base}[_on][_lit].
     */
    public void copperStoveBlockState(Block block, String baseModelName) {
        getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(CopperStoveBlock.FACING);
            boolean isLit = state.getValue(CopperStoveBlock.LIT);
            boolean isPowered = state.getValue(CopperStoveBlock.POWERED);

            String modelName = baseModelName
                    + (isPowered ? "_on" : "")
                    + (isLit ? "_lit" : "");

            int rotationY = ((dir.get2DDataValue() & 3) * 90 + 180) % 360;

            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/" + modelName)))
                    .rotationY(rotationY)
                    .build();
        });
    }

    /**
     * Burnt-log blockstate helper that uses the FACING + LAYERS properties to
     * pick the right layered model and rotate it. Matches 1.21.1 verbatim.
     */
    public void burntLogBlockState(Block block, String baseModelName) {
        getVariantBuilder(block).forAllStates(state -> {
            Direction facing = state.getValue(BlockStateProperties.FACING);
            int layers = state.getValue(BurntLogBlock.LAYERS);

            String modelName = baseModelName + "_layer" + layers;

            int rotationX = 0;
            int rotationY = 0;

            switch (facing) {
                case DOWN -> rotationX = 180;
                case NORTH -> rotationX = 90;
                case SOUTH -> { rotationX = 90; rotationY = 180; }
                case WEST -> { rotationX = 90; rotationY = 270; }
                case EAST -> { rotationX = 90; rotationY = 90; }
                case UP -> {}
            }

            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/" + modelName)))
                    .rotationX(rotationX)
                    .rotationY(rotationY)
                    .build();
        });
    }
}
