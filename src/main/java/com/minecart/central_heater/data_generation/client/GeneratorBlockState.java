package com.minecart.central_heater.data_generation.client;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.block.misc.BurntLogBlock;
import com.minecart.central_heater.block.stove.CopperStoveBlock;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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

        simpleBlock(AllBlockItem.COAL_BRICKS.get());
        stairsBlock((StairBlock) AllBlockItem.COAL_BRICK_STAIR.get(), blockTexture(AllBlockItem.COAL_BRICKS.get()));
        slabBlock((SlabBlock) AllBlockItem.COAL_BRICK_SLAB.get(), blockTexture(AllBlockItem.COAL_BRICKS.get()), blockTexture(AllBlockItem.COAL_BRICKS.get()));
        simpleBlock(AllBlockItem.COAL_BRICK_TILE.get());
        stairsBlock((StairBlock) AllBlockItem.COAL_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.COAL_BRICK_TILE.get()));
        slabBlock((SlabBlock) AllBlockItem.COAL_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.COAL_BRICK_TILE.get()), blockTexture(AllBlockItem.COAL_BRICK_TILE.get()));
        wallBlock((WallBlock) AllBlockItem.COAL_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.COAL_BRICK_TILE.get()));

        simpleBlock(AllBlockItem.GOLDEN_BRICKS.get());
        stairsBlock((StairBlock) AllBlockItem.GOLDEN_BRICK_STAIR.get(), blockTexture(AllBlockItem.GOLDEN_BRICKS.get()));
        slabBlock((SlabBlock) AllBlockItem.GOLDEN_BRICK_SLAB.get(), blockTexture(AllBlockItem.GOLDEN_BRICKS.get()), blockTexture(AllBlockItem.GOLDEN_BRICKS.get()));
        simpleBlock(AllBlockItem.GOLDEN_BRICK_TILE.get());
        stairsBlock((StairBlock) AllBlockItem.GOLDEN_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.GOLDEN_BRICK_TILE.get()));
        slabBlock((SlabBlock) AllBlockItem.GOLDEN_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.GOLDEN_BRICK_TILE.get()), blockTexture(AllBlockItem.GOLDEN_BRICK_TILE.get()));
        wallBlock((WallBlock) AllBlockItem.GOLDEN_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.GOLDEN_BRICK_TILE.get()));

//        simpleBlock(AllBlockItem.IRON_BRICKS.get());
//        stairsBlock((StairBlock) AllBlockItem.IRON_BRICK_STAIR.get(), blockTexture(AllBlockItem.IRON_BRICKS.get()));
//        slabBlock((SlabBlock) AllBlockItem.IRON_BRICK_SLAB.get(), blockTexture(AllBlockItem.IRON_BRICKS.get()), blockTexture(AllBlockItem.IRON_BRICKS.get()));
//        simpleBlock(AllBlockItem.IRON_BRICK_TILE.get());
//        stairsBlock((StairBlock) AllBlockItem.IRON_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.IRON_BRICK_TILE.get()));
//        slabBlock((SlabBlock) AllBlockItem.IRON_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.IRON_BRICK_TILE.get()), blockTexture(AllBlockItem.IRON_BRICK_TILE.get()));
//        wallBlock((WallBlock) AllBlockItem.IRON_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.IRON_BRICK_TILE.get()));

        simpleBlock(AllBlockItem.NETHERITE_BRICKS.get());
        stairsBlock((StairBlock) AllBlockItem.NETHERITE_BRICK_STAIR.get(), blockTexture(AllBlockItem.NETHERITE_BRICKS.get()));
        slabBlock((SlabBlock) AllBlockItem.NETHERITE_BRICK_SLAB.get(), blockTexture(AllBlockItem.NETHERITE_BRICKS.get()), blockTexture(AllBlockItem.NETHERITE_BRICKS.get()));
        simpleBlock(AllBlockItem.NETHERITE_BRICK_TILE.get());
        stairsBlock((StairBlock) AllBlockItem.NETHERITE_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.NETHERITE_BRICK_TILE.get()));
        slabBlock((SlabBlock) AllBlockItem.NETHERITE_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.NETHERITE_BRICK_TILE.get()), blockTexture(AllBlockItem.NETHERITE_BRICK_TILE.get()));
        wallBlock((WallBlock) AllBlockItem.NETHERITE_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.NETHERITE_BRICK_TILE.get()));

        simpleBlock(AllBlockItem.SCORCHED_BRICKS.get());
        stairsBlock((StairBlock) AllBlockItem.SCORCHED_BRICK_STAIR.get(), blockTexture(AllBlockItem.SCORCHED_BRICKS.get()));
        slabBlock((SlabBlock) AllBlockItem.SCORCHED_BRICK_SLAB.get(), blockTexture(AllBlockItem.SCORCHED_BRICKS.get()), blockTexture(AllBlockItem.SCORCHED_BRICKS.get()));
        simpleBlock(AllBlockItem.SCORCHED_BRICK_TILE.get());
        stairsBlock((StairBlock) AllBlockItem.SCORCHED_BRICK_TILE_STAIR.get(), blockTexture(AllBlockItem.SCORCHED_BRICK_TILE.get()));
        slabBlock((SlabBlock) AllBlockItem.SCORCHED_BRICK_TILE_SLAB.get(), blockTexture(AllBlockItem.SCORCHED_BRICK_TILE.get()), blockTexture(AllBlockItem.SCORCHED_BRICK_TILE.get()));
        wallBlock((WallBlock) AllBlockItem.SCORCHED_BRICK_TILE_WALL.get(), blockTexture(AllBlockItem.SCORCHED_BRICK_TILE.get()));

        simpleBlock(AllBlockItem.STURDY_BRICKS.get());
        stairsBlock(AllBlockItem.STURDY_BRICK_STAIR.get(), modLoc("block/sturdy_bricks"));
        slabBlock(AllBlockItem.STURDY_BRICK_SLAB.get(), modLoc("block/sturdy_bricks"), modLoc("block/sturdy_bricks"));

        copperStoveBlockState(AllBlockItem.COPPER_STOVE.get(), "copper_stove");
        copperStoveBlockState(AllBlockItem.EXPOSED_COPPER_STOVE.get(), "exposed_copper_stove");
        copperStoveBlockState(AllBlockItem.WEATHERED_COPPER_STOVE.get(), "weathered_copper_stove");
        copperStoveBlockState(AllBlockItem.OXIDIZED_COPPER_STOVE.get(), "oxidized_copper_stove");

        copperStoveBlockState(AllBlockItem.WAXED_COPPER_STOVE.get(), "copper_stove");
        copperStoveBlockState(AllBlockItem.WAXED_EXPOSED_COPPER_STOVE.get(), "exposed_copper_stove");
        copperStoveBlockState(AllBlockItem.WAXED_WEATHERED_COPPER_STOVE.get(), "weathered_copper_stove");
        copperStoveBlockState(AllBlockItem.WAXED_OXIDIZED_COPPER_STOVE.get(), "oxidized_copper_stove");

        for (AllBlockItem.BurntWoodSet set : AllBlockItem.BURNT_WOOD_SETS) {
            registerBurntWoodSet(set);
        }
    }

    private void registerBurntWoodSet(AllBlockItem.BurntWoodSet set) {
        simpleBlock(set.planks().get(), models().cubeAll(set.prefix() + "_planks", modLoc("block/burnt_planks")));
        stairsBlock(set.stairs().get(), modLoc("block/burnt_planks"));
        slabBlock(set.slab().get(), modLoc("block/burnt_planks"), modLoc("block/burnt_planks"));
        fenceBlock(set.fence().get(), modLoc("block/burnt_planks"));
        fenceGateBlock(set.fenceGate().get(), modLoc("block/burnt_planks"));
        trapdoorBlockWithRenderType(set.trapdoor().get(), modLoc("block/burnt_trapdoor"), true, "cutout");
        buttonBlock(set.button().get(), modLoc("block/burnt_planks"));
        pressurePlateBlock(set.pressurePlate().get(), modLoc("block/burnt_planks"));
        doorBlockWithRenderType(set.door().get(), modLoc("block/burnt_door_bottom"), modLoc("block/burnt_door_top"), "cutout");

        burntLogBlockState(set.log().get(), set.prefix() + "_log");
        axisBlock(set.wood().get(), logTexture(set), logTexture(set));
    }

    private net.minecraft.resources.ResourceLocation logTexture(AllBlockItem.BurntWoodSet set) {
        return switch (set.material()) {
            case "birch", "jungle", "cherry", "mangrove" -> modLoc("block/" + set.prefix() + "_log");
            default -> modLoc("block/burnt_log");
        };
    }

    public void copperStoveBlockState(Block block, String baseModelName) {
        getVariantBuilder(block).forAllStates(state -> {
            Direction dir = state.getValue(CopperStoveBlock.FACING);
            boolean isLit = state.getValue(CopperStoveBlock.LIT);
            boolean isPowered = state.getValue(CopperStoveBlock.POWERED);

            // Constructs names exactly matching the models generated above
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

    // --- Custom Burnt Log BlockState Helper ---
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
                case UP -> {} // Default 0,0
            }

            return ConfiguredModel.builder()
                    .modelFile(models().getExistingFile(modLoc("block/" + modelName)))
                    .rotationX(rotationX)
                    .rotationY(rotationY)
                    .build();
        });
    }
}
