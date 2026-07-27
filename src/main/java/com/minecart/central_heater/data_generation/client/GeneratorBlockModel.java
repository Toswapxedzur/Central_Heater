package com.minecart.central_heater.data_generation.client;

import com.minecart.central_heater.CentralHeater;
import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.model.generators.BlockModelBuilder;
import net.neoforged.neoforge.client.model.generators.BlockModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

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
                modLoc("block/blazing_furnace_side"), modLoc("block/golden_brick_tile"));

        potModel("mud_brick_pot", CentralHeater.modLoc("block/mud_brick_tile"), CentralHeater.modLoc("block/mud_brick_pot_support"));

        BlockModelBuilder blazingFurnaceOn = furnaceModel("blazing_furnace_on", modLoc("block/blazing_furnace_top"), modLoc("block/blazing_furnace_front_on"),
                modLoc("block/blazing_furnace_side"), modLoc("block/blackstone_brick_tile"));

        anvilModel("sturdy_anvil", "anvil",
                modLoc("block/sturdy_anvil"), modLoc("block/sturdy_anvil_top"));

        anvilModel("chipped_sturdy_anvil", "chipped_anvil",
                modLoc("block/sturdy_anvil"), modLoc("block/chipped_sturdy_anvil_top"));

        anvilModel("damaged_sturdy_anvil", "damaged_anvil",
                modLoc("block/sturdy_anvil"), modLoc("block/damaged_sturdy_anvil_top"));

        cauldronModel("ashtray", modLoc("block/sturdy_cauldron_side"), modLoc("block/sturdy_cauldron_top"),
                modLoc("block/sturdy_cauldron_bottom"), modLoc("block/sturdy_cauldron_inner"));

        for (int i = 1; i <= 6; i++) {
            ashtrayAshModel("ashtray_" + i + "_normal", modLoc("block/campfire_dust"), i);
            ashtrayAshModel("ashtray_" + i + "_scorched", mcLoc("block/soul_sand"), i);
        }

// --- 1. Unaffected Copper Models ---
        stoveModel("copper_stove", modLoc("block/sturdy_brick_tile"), "block/sturdy_stove_side", "block/sturdy_stove_front", "block/copper_grid");
        stoveModelBurn("copper_stove_lit", modLoc("block/sturdy_brick_tile"), "block/sturdy_stove_side", "block/sturdy_stove_front", "block/copper_grid");
        stoveModel("copper_stove_on", modLoc("block/sturdy_brick_tile"), "block/sturdy_stove_side_on", "block/sturdy_stove_front_on", "block/copper_grid_on");
        stoveModelBurn("copper_stove_on_lit", modLoc("block/sturdy_brick_tile"), "block/sturdy_stove_side_on", "block/sturdy_stove_front_on", "block/copper_grid_on");

// --- 2. Exposed Copper Models ---
        stoveModel("exposed_copper_stove", modLoc("block/sturdy_brick_tile"), "block/exposed_sturdy_stove_side", "block/exposed_sturdy_stove_front", "block/exposed_copper_grid");
        stoveModelBurn("exposed_copper_stove_lit", modLoc("block/sturdy_brick_tile"), "block/exposed_sturdy_stove_side", "block/exposed_sturdy_stove_front", "block/exposed_copper_grid");
        stoveModel("exposed_copper_stove_on", modLoc("block/sturdy_brick_tile"), "block/exposed_sturdy_stove_side_on", "block/exposed_sturdy_stove_front_on", "block/exposed_copper_grid_on");
        stoveModelBurn("exposed_copper_stove_on_lit", modLoc("block/sturdy_brick_tile"), "block/exposed_sturdy_stove_side_on", "block/exposed_sturdy_stove_front_on", "block/exposed_copper_grid_on");

// --- 3. Weathered Copper Models ---
        stoveModel("weathered_copper_stove", modLoc("block/sturdy_brick_tile"), "block/weathered_sturdy_stove_side", "block/weathered_sturdy_stove_front", "block/weathered_copper_grid");
        stoveModelBurn("weathered_copper_stove_lit", modLoc("block/sturdy_brick_tile"), "block/weathered_sturdy_stove_side", "block/weathered_sturdy_stove_front", "block/weathered_copper_grid");
        stoveModel("weathered_copper_stove_on", modLoc("block/sturdy_brick_tile"), "block/weathered_sturdy_stove_side_on", "block/weathered_sturdy_stove_front_on", "block/weathered_copper_grid_on");
        stoveModelBurn("weathered_copper_stove_on_lit", modLoc("block/sturdy_brick_tile"), "block/weathered_sturdy_stove_side_on", "block/weathered_sturdy_stove_front_on", "block/weathered_copper_grid_on");

// --- 4. Oxidized Copper Models ---
        stoveModel("oxidized_copper_stove", modLoc("block/sturdy_brick_tile"), "block/oxidized_sturdy_stove_side", "block/oxidized_sturdy_stove_front", "block/oxidized_copper_grid");
        stoveModelBurn("oxidized_copper_stove_lit", modLoc("block/sturdy_brick_tile"), "block/oxidized_sturdy_stove_side", "block/oxidized_sturdy_stove_front", "block/oxidized_copper_grid");
        stoveModel("oxidized_copper_stove_on", modLoc("block/sturdy_brick_tile"), "block/oxidized_sturdy_stove_side_on", "block/oxidized_sturdy_stove_front_on", "block/oxidized_copper_grid_on");
        stoveModelBurn("oxidized_copper_stove_on_lit", modLoc("block/sturdy_brick_tile"), "block/oxidized_sturdy_stove_side_on", "block/oxidized_sturdy_stove_front_on", "block/oxidized_copper_grid_on");

        String[] burntWoods = {"burnt", "burnt_spruce", "burnt_birch", "burnt_jungle", "burnt_acacia", "burnt_dark_oak", "burnt_mangrove", "burnt_cherry"};

        for (String type : burntWoods) {
            ResourceLocation sideTex = logTexture(type, false);
            ResourceLocation topTex = logTexture(type, true);

            for (int layer = 1; layer <= 4; layer++) {
                burntLogLayerModel(type + "_log_layer" + layer, layer, sideTex, topTex);
            }
        }
    }

    private ResourceLocation logTexture(String type, boolean top) {
        String texture = top ? "_log_top" : "_log";
        return switch (type) {
            case "burnt_birch", "burnt_jungle", "burnt_cherry", "burnt_mangrove" -> modLoc("block/" + type + texture);
            default -> modLoc("block/burnt" + texture);
        };
    }

    // --- Custom Burnt Log Helpers ---
    public void burntLogLayerModel(String name, int layer, ResourceLocation side, ResourceLocation top) {
        withExistingParent(name, modLoc("block/burnt_layer" + layer))
                .texture("top", top)
                .texture("bottom", top)
                .texture("side", side)
                .texture("particle", side);
    }

    public void burntLogFullModel(String name, ResourceLocation side, ResourceLocation top) {
        withExistingParent(name, mcLoc("block/cube_bottom_top"))
                .texture("top", top)
                .texture("bottom", top)
                .texture("side", side)
                .texture("particle", side);
    }

    public void burntWoodModel(String name, ResourceLocation texture) {
        withExistingParent(name, mcLoc("block/cube_column"))
                .texture("end", texture)
                .texture("side", texture);
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

    public void anvilModel(String name, String parentModel, ResourceLocation body, ResourceLocation top) {
        withExistingParent(name, mcLoc("block/" + parentModel))
                .texture("particle", body)
                .texture("body", body)
                .texture("top", top);
    }

    public void ashtrayAshModel(String name, ResourceLocation texture, int level) {
        int height = 4 + (level * 2);
        getBuilder(name)
                .ao(false)
                .texture("particle", texture)
                .texture("ash", texture)
                .element()
                .from(2, 2, 2).to(14, height, 14)
                .allFaces((dir, face) -> face.texture("#ash"))
                .end();
    }
}
