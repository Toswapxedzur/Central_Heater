package com.minecart.central_heater.data_generation.client;

import com.minecart.central_heater.CentralHeater;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.BlockModelBuilder;
import net.minecraftforge.client.model.generators.BlockModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class GeneratorBlockModel extends BlockModelProvider {
    public GeneratorBlockModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CentralHeater.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

        stoveModel("brick_stove_off", mcLoc("block/bricks"), "block/brick_stove_side", "block/brick_stove_front", "block/iron_grid");
        stoveModel("mud_brick_stove_off", "block/mud_brick_tile", "block/mud_brick_stove_side", "block/mud_brick_stove_front", "block/iron_grid");
        stoveModel("stone_stove_off", "block/stone_brick_tile", "block/stone_stove_side", "block/stone_stove_front", "block/iron_grid");
        stoveModel("deepslate_stove_off", "block/deepslate_brick_tile", "block/deepslate_stove_side", "block/deepslate_stove_front", "block/iron_grid");
        stoveModel("nether_brick_stove_off", mcLoc("block/nether_bricks"), "block/nether_bricks_stove_side", "block/nether_bricks_stove_front", "block/gold_grid");
        stoveModel("red_nether_brick_stove_off", mcLoc("block/red_nether_bricks"), "block/red_nether_bricks_stove_side", "block/red_nether_bricks_stove_front", "block/gold_grid");
        stoveModel("blackstone_stove_off", modLoc("block/blackstone_brick_tile"), "block/blackstone_stove_side", "block/blackstone_stove_front", "block/gold_grid");

        stoveModelBurn("brick_stove_on", mcLoc("block/bricks"), "block/brick_stove_side", "block/brick_stove_front", "block/iron_grid");
        stoveModelBurn("mud_brick_stove_on", "block/mud_brick_tile", "block/mud_brick_stove_side", "block/mud_brick_stove_front", "block/iron_grid");
        stoveModelBurn("stone_stove_on", "block/stone_brick_tile", "block/stone_stove_side", "block/stone_stove_front", "block/iron_grid");
        stoveModelBurn("deepslate_stove_on", "block/deepslate_brick_tile", "block/deepslate_stove_side", "block/deepslate_stove_front", "block/iron_grid");
        stoveModelBurn("nether_brick_stove_burn", mcLoc("block/nether_bricks"), "block/nether_bricks_stove_side", "block/nether_bricks_stove_front", "block/gold_grid");
        stoveModelBurn("red_nether_brick_stove_burn", mcLoc("block/red_nether_bricks"), "block/red_nether_bricks_stove_side", "block/red_nether_bricks_stove_front", "block/gold_grid");
        stoveModelBurn("blackstone_stove_burn", modLoc("block/blackstone_brick_tile"), "block/blackstone_stove_side", "block/blackstone_stove_front", "block/gold_grid");

        stoveModelSeeth("nether_brick_stove_soul", mcLoc("block/nether_bricks"), "block/nether_bricks_stove_side", "block/nether_bricks_stove_front", "block/gold_grid");
        stoveModelSeeth("red_nether_brick_stove_soul", mcLoc("block/red_nether_bricks"), "block/red_nether_bricks_stove_side", "block/red_nether_bricks_stove_front", "block/gold_grid");
        stoveModelSeeth("blackstone_stove_soul", modLoc("block/blackstone_brick_tile"), "block/blackstone_stove_side", "block/blackstone_stove_front", "block/gold_grid");

        cauldronModel("brick_cauldron", modLoc("block/brick_cauldron_side"), modLoc("block/brick_cauldron_top"),
                modLoc("block/brick_cauldron_bottom"), modLoc("block/brick_cauldron_inner"));

        cauldronModel("clay_cauldron", mcLoc("block/clay"), mcLoc("block/clay"),
                mcLoc("block/clay"), mcLoc("block/clay"));

        cauldronModel("golden_cauldron", modLoc("block/golden_cauldron_side"), modLoc("block/golden_cauldron_top"),
                modLoc("block/golden_cauldron_bottom"), modLoc("block/golden_cauldron_inner"));

        BlockModelBuilder blazingFurnace = furnaceModel("blazing_furnace", modLoc("block/blazing_furnace_top"), modLoc("block/blazing_furnace_front"),
                modLoc("block/blazing_furnace_side"), modLoc("block/blackstone_brick_tile"));

        potModel("mud_brick_pot", modLoc("block/mud_brick_tile"), modLoc("block/mud_brick_pot_support"));

        BlockModelBuilder blazingFurnaceOn = furnaceModel("blazing_furnace_on", modLoc("block/blazing_furnace_top"), modLoc("block/blazing_furnace_front_on"),
                modLoc("block/blazing_furnace_side"), modLoc("block/blackstone_brick_tile"));
    }

    public void stoveModel(String name, String bricks, String side, String front, String grid){
        stoveModel(name, modLoc(bricks), modLoc(side), modLoc(front), modLoc(grid));
    }

    public void stoveModel(String name, ResourceLocation bricks, String side, String front, String grid){
        stoveModel(name, bricks, modLoc(side), modLoc(front), modLoc(grid));
    }

    public void stoveModelBurn(String name, String bricks, String side, String front, String grid){
        stoveModelBurn(name, modLoc(bricks), modLoc(side), modLoc(front), modLoc(grid));
    }

    public void stoveModelBurn(String name, ResourceLocation bricks, String side, String front, String grid){
        stoveModelBurn(name, bricks, modLoc(side), modLoc(front), modLoc(grid));
    }

    public void stoveModelSeeth(String name, String bricks, String side, String front, String grid){
        stoveModelSeeth(name, modLoc(bricks), modLoc(side), modLoc(front), modLoc(grid));
    }

    public void stoveModelSeeth(String name, ResourceLocation bricks, String side, String front, String grid){
        stoveModelSeeth(name, bricks, modLoc(side), modLoc(front), modLoc(grid));
    }

    public void stoveModel(String name, ResourceLocation bricks, ResourceLocation side, ResourceLocation front, ResourceLocation grid){
        getBuilder(name).parent(getExistingFile(modLoc("block/stove_off")))
                .texture("0", bricks)
                .texture("1", side)
                .texture("4", front)
                .texture("2", grid)
                .texture("5", modLoc("block/campfire_dust"))
                .texture("particle", bricks);
    }

    public void stoveModelBurn(String name, ResourceLocation bricks, ResourceLocation side, ResourceLocation front, ResourceLocation grid){
        getBuilder(name).parent(getExistingFile(modLoc("block/stove_on")))
                .texture("0", bricks)
                .texture("1", side)
                .texture("4", front)
                .texture("2", grid)
                .texture("6", modLoc("block/campfire_dust"))
                .texture("5", mcLoc("block/campfire_fire"))
                .texture("particle", bricks);
    }

    public void stoveModelSeeth(String name, ResourceLocation bricks, ResourceLocation side, ResourceLocation front, ResourceLocation grid){
        getBuilder(name).parent(getExistingFile(modLoc("block/stove_on")))
                .texture("0", bricks)
                .texture("1", side)
                .texture("4", front)
                .texture("2", grid)
                .texture("6", modLoc("block/campfire_dust"))
                .texture("5", mcLoc("block/soul_campfire_fire"))
                .texture("particle", bricks);
    }

    public void potModel(String name, ResourceLocation bricks, ResourceLocation support) {
        withExistingParent(name, modLoc("block/pot"))
                .texture("particle", bricks)
                .texture("bricks", bricks)
                .texture("support", support);
    }

    public void cauldronModel(String name, ResourceLocation side, ResourceLocation top, ResourceLocation bottom, ResourceLocation inside) {
        withExistingParent(name, mcLoc("block/cauldron"))
                .texture("particle", side)
                .texture("side", side)
                .texture("top", top)
                .texture("bottom", bottom)
                .texture("inside", inside);
    }

    public BlockModelBuilder furnaceModel(String name, ResourceLocation top, ResourceLocation front, ResourceLocation side, ResourceLocation bottom){
        return withExistingParent(name, mcLoc("block/cube"))
                .texture("particle", top)
                .texture("up", top)
                .texture("down", bottom)
                .texture("north", front)
                .texture("south", side)
                .texture("west", side)
                .texture("east", side);
    }
}