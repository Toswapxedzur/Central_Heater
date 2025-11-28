package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.Central_heater;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class BlockModel extends BlockModelProvider {
    public BlockModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Central_heater.MODID, existingFileHelper);
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


        lidModel("iron_lid", "block/iron_lid");
        lidModel("gold_lid", "block/gold_lid");

        potModel("brick_pot", mcLoc("block/bricks"));
        potModel("mud_brick_pot", modLoc("block/mud_brick_tile"));
        potModel("stone_pot", modLoc("block/stone_brick_tile"));
        potModel("deepslate_pot", modLoc("block/deepslate_brick_tile"));
        potModel("red_nether_brick_pot", mcLoc("block/red_nether_bricks"));
        potModel("nether_brick_pot", mcLoc("block/nether_bricks"));
        potModel("blackstone_pot", modLoc("block/blackstone_brick_tile"));
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


    public void lidModel(String name, String lid){
        lidModel(name, modLoc(lid));
    }

    public void lidModel(String name, ResourceLocation lid){
        getBuilder(name).parent(getExistingFile(modLoc("block/lid")))
                .texture("lid", lid)
                .texture("particle", lid);
    }

    public void potModel(String name, String bricks){
        potModel(name, modLoc(bricks));
    }

    public void potModel(String name, ResourceLocation bricks){
        getBuilder(name).parent(getExistingFile(modLoc("block/pot")))
                .texture("bricks", bricks)
                .texture("particle", bricks);
    }
}
