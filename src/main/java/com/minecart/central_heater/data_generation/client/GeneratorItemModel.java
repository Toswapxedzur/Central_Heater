package com.minecart.central_heater.data_generation.client;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class GeneratorItemModel extends ItemModelProvider {
    public GeneratorItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CentralHeater.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        withExistingParent("burnt_log", modLoc("block/burnt_log_layer4"));
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

        withExistingParent("sturdy_brick_tile_stair", modLoc("block/sturdy_brick_tile_stair"));
        withExistingParent("sturdy_brick_tile_slab", modLoc("block/sturdy_brick_tile_slab"));
        wallInventory("sturdy_brick_tile_wall", modLoc("block/sturdy_brick_tile"));

        withExistingParent("burnt_wood", modLoc("block/burnt_wood"));
        basicItem(AllBlockItem.SCORCHED_BRIQUETTES.get().asItem());

        withExistingParent("sturdy_bricks", modLoc("block/sturdy_bricks"));
        withExistingParent("sturdy_brick_stair", modLoc("block/sturdy_brick_stair"));
        withExistingParent("sturdy_brick_slab", modLoc("block/sturdy_brick_slab"));

        withExistingParent("sturdy_anvil", modLoc("block/sturdy_anvil"));
        withExistingParent("chipped_sturdy_anvil", modLoc("block/chipped_sturdy_anvil"));
        withExistingParent("damaged_sturdy_anvil", modLoc("block/damaged_sturdy_anvil"));

        basicItem(AllBlockItem.COBBLE.get().asItem());
        basicItem(AllBlockItem.DEEPSLATE_COBBLE.get().asItem());
        basicItem(AllBlockItem.STONE_BRICK.get().asItem());
        basicItem(AllBlockItem.MUD_BRICK.get().asItem());
        basicItem(AllBlockItem.RED_NETHER_BRICK.get().asItem());
        basicItem(AllBlockItem.DEEPSLATE_BRICK.get().asItem());
        basicItem(AllBlockItem.DIAMOND_SHARD.get().asItem());
        basicItem(AllBlockItem.BLACKSTONE_BRICK.get().asItem());
        basicItem(AllBlockItem.STURDY_BRICK.get().asItem());
        basicItem(AllBlockItem.STURDY_TANK_ITEM.get().asItem());
        basicItem(AllBlockItem.STURDY_NUGGET.get().asItem());
        basicItem(AllBlockItem.SCORCHED_COAL.get().asItem());
        basicItem(AllBlockItem.SCORCHED_DUST.get().asItem());
        basicItem(AllBlockItem.FIRE_ASH.get().asItem());
        basicItem(AllBlockItem.CLAY_BIT.get().asItem());
        basicItem(AllBlockItem.CLAY_BRICK.get().asItem());
        basicItem(AllBlockItem.SOUL_MIXTURE.get().asItem());
        basicItem(AllBlockItem.WHEAT_DOUGH.get().asItem());
        basicItem(AllBlockItem.WHEAT_FLOUR.get().asItem());
        basicItem(AllBlockItem.WOOD_CHIPS.get().asItem());
        basicItem(AllBlockItem.CLAY_CAULDRON.get().asItem());
        basicItem(AllBlockItem.BRICK_CAULDRON.get().asItem());
        withExistingParent("iron_cauldron", mcLoc("item/cauldron"));
        basicItem(AllBlockItem.GOLDEN_CAULDRON.get().asItem());
        withExistingParent("blazing_furnace", modLoc("block/blazing_furnace"));
        basicItemWithTexture(AllBlockItem.GOLD_BARS.get().asItem(), "block/gold_bars");

        handheldItem(AllBlockItem.STURDY_PICKAXE.get());
        handheldItem(AllBlockItem.STURDY_AXE.get());
        handheldItem(AllBlockItem.STURDY_SHOVEL.get());
        handheldItem(AllBlockItem.STURDY_HOE.get());
        handheldItem(AllBlockItem.STURDY_SWORD.get());
        basicItem(AllBlockItem.BLAZING_FURNACE_MINECART.get().asItem());

        basicItem(AllBlockItem.BURNT_BEEF.get().asItem());
        basicItem(AllBlockItem.BURNT_CHICKEN.get().asItem());
        basicItem(AllBlockItem.BURNT_COD.get().asItem());
        basicItem(AllBlockItem.BURNT_MUTTON.get().asItem());
        basicItem(AllBlockItem.BURNT_PORKCHOP.get().asItem());
        basicItem(AllBlockItem.BURNT_RABBIT.get().asItem());
        basicItem(AllBlockItem.BURNT_SALMON.get().asItem());

        // --- Standard Items ---
        basicItem(AllBlockItem.BRIQUETTES.get().asItem());
        basicItem(AllBlockItem.SOAP.get().asItem());
        handheldItem(AllBlockItem.STURDY_SHEARS.get());

        // --- Block Items (Delegating to Block Models) ---
        // Coal
        withExistingParent("coal_bricks", modLoc("block/coal_bricks"));
        withExistingParent("coal_brick_stair", modLoc("block/coal_brick_stair"));
        withExistingParent("coal_brick_slab", modLoc("block/coal_brick_slab"));
        withExistingParent("coal_brick_tile", modLoc("block/coal_brick_tile"));
        withExistingParent("coal_brick_tile_stair", modLoc("block/coal_brick_tile_stair"));
        withExistingParent("coal_brick_tile_slab", modLoc("block/coal_brick_tile_slab"));
        wallInventory("coal_brick_tile_wall", modLoc("block/coal_brick_tile"));

        // Golden
        withExistingParent("golden_bricks", modLoc("block/golden_bricks"));
        withExistingParent("golden_brick_stair", modLoc("block/golden_brick_stair"));
        withExistingParent("golden_brick_slab", modLoc("block/golden_brick_slab"));
        withExistingParent("golden_brick_tile", modLoc("block/golden_brick_tile"));
        withExistingParent("golden_brick_tile_stair", modLoc("block/golden_brick_tile_stair"));
        withExistingParent("golden_brick_tile_slab", modLoc("block/golden_brick_tile_slab"));
        wallInventory("golden_brick_tile_wall", modLoc("block/golden_brick_tile"));

        // Netherite
        withExistingParent("netherite_bricks", modLoc("block/netherite_bricks"));
        withExistingParent("netherite_brick_stair", modLoc("block/netherite_brick_stair"));
        withExistingParent("netherite_brick_slab", modLoc("block/netherite_brick_slab"));
        withExistingParent("netherite_brick_tile", modLoc("block/netherite_brick_tile"));
        withExistingParent("netherite_brick_tile_stair", modLoc("block/netherite_brick_tile_stair"));
        withExistingParent("netherite_brick_tile_slab", modLoc("block/netherite_brick_tile_slab"));
        wallInventory("netherite_brick_tile_wall", modLoc("block/netherite_brick_tile"));

        // Scorched
        withExistingParent("scorched_bricks", modLoc("block/scorched_bricks"));
        withExistingParent("scorched_brick_stair", modLoc("block/scorched_brick_stair"));
        withExistingParent("scorched_brick_slab", modLoc("block/scorched_brick_slab"));
        withExistingParent("scorched_brick_tile", modLoc("block/scorched_brick_tile"));
        withExistingParent("scorched_brick_tile_stair", modLoc("block/scorched_brick_tile_stair"));
        withExistingParent("scorched_brick_tile_slab", modLoc("block/scorched_brick_tile_slab"));
        wallInventory("scorched_brick_tile_wall", modLoc("block/scorched_brick_tile"));

        withExistingParent("ashtray", modLoc("block/ashtray"));

        // --- Normal Copper Stove Items ---
        withExistingParent("copper_stove", modLoc("block/copper_stove"));
        withExistingParent("exposed_copper_stove", modLoc("block/exposed_copper_stove"));
        withExistingParent("weathered_copper_stove", modLoc("block/weathered_copper_stove"));
        withExistingParent("oxidized_copper_stove", modLoc("block/oxidized_copper_stove"));

        // --- Waxed Copper Stove Items ---
        withExistingParent("waxed_copper_stove", modLoc("block/copper_stove"));
        withExistingParent("waxed_exposed_copper_stove", modLoc("block/exposed_copper_stove"));
        withExistingParent("waxed_weathered_copper_stove", modLoc("block/weathered_copper_stove"));
        withExistingParent("waxed_oxidized_copper_stove", modLoc("block/oxidized_copper_stove"));

        // --- Burnt Logs & Woods ---
        withExistingParent("burnt_birch_log", modLoc("block/burnt_birch_log_layer4"));
        withExistingParent("burnt_birch_wood", modLoc("block/burnt_birch_wood"));
        withExistingParent("burnt_jungle_log", modLoc("block/burnt_jungle_log_layer4"));
        withExistingParent("burnt_jungle_wood", modLoc("block/burnt_jungle_wood"));
        withExistingParent("burnt_cherry_log", modLoc("block/burnt_cherry_log_layer4"));
        withExistingParent("burnt_cherry_wood", modLoc("block/burnt_cherry_wood"));
        withExistingParent("burnt_mangrove_log", modLoc("block/burnt_mangrove_log_layer4"));
        withExistingParent("burnt_mangrove_wood", modLoc("block/burnt_mangrove_wood"));

        basicItem(AllBlockItem.BURNT_DOOR.get().asItem());
        basicItem(AllBlockItem.BURNT_BOAT.get().asItem());
        basicItem(AllBlockItem.BURNT_CHEST_BOAT.get().asItem());
        basicItem(AllBlockItem.COAL_BIT.get().asItem());
        basicItem(AllBlockItem.CHARCOAL_BIT.get().asItem());

        basicItemWithTexture(AllBlockItem.BURNABLE_CAMPFIRE.get().asItem(), mcLoc("item/campfire"));
        basicItemWithTexture(AllBlockItem.BURNABLE_SOUL_CAMPFIRE.get().asItem(), mcLoc("item/soul_campfire"));

        // --- Generic Burnt Wood Set ---
        withExistingParent("burnt_planks", modLoc("block/burnt_planks"));
        withExistingParent("burnt_stairs", modLoc("block/burnt_stairs"));
        withExistingParent("burnt_slab", modLoc("block/burnt_slab"));
        fenceInventory("burnt_fence", modLoc("block/burnt_planks"));
        withExistingParent("burnt_fence_gate", modLoc("block/burnt_fence_gate"));
        withExistingParent("burnt_trapdoor", modLoc("block/burnt_trapdoor_bottom"));
        buttonInventory("burnt_button", modLoc("block/burnt_planks"));
        withExistingParent("burnt_pressure_plate", modLoc("block/burnt_pressure_plate"));

        generateArmorTrims((ArmorItem) AllBlockItem.STURDY_CHESTPLATE.get());
        generateArmorTrims((ArmorItem) AllBlockItem.STURDY_HELMET.get());
        generateArmorTrims((ArmorItem) AllBlockItem.STURDY_LEGGINGS.get());
        generateArmorTrims((ArmorItem) AllBlockItem.STURDY_BOOTS.get());
    }

    public ItemModelBuilder basicItemWithTexture(Item item, String key) {
        return basicItemWithTexture(item, modLoc(key));
    }

    public ItemModelBuilder basicItemWithTexture(Item item, ResourceLocation key) {
        return getBuilder(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", key);
    }

    public void generateArmorTrims(ArmorItem armorItem) {
        ResourceLocation armorItemRes = ForgeRegistries.ITEMS.getKey(armorItem);
        String armorPath = armorItemRes.getPath();
        String armorNamespace = armorItemRes.getNamespace();

        String armorPart = switch (armorItem.getEquipmentSlot()) {
            case HEAD -> "helmet";
            case CHEST -> "chestplate";
            case LEGS -> "leggings";
            case FEET -> "boots";
            default -> null;
        };
        if (armorPart == null) return;

        Map<String, Float> trimMaterials = new LinkedHashMap<>();
        trimMaterials.put("quartz", 0.1F);
        trimMaterials.put("iron", 0.2F);
        trimMaterials.put("netherite", 0.3F);
        trimMaterials.put("redstone", 0.4F);
        trimMaterials.put("copper", 0.5F);
        trimMaterials.put("gold", 0.6F);
        trimMaterials.put("emerald", 0.7F);
        trimMaterials.put("diamond", 0.8F);
        trimMaterials.put("lapis", 0.9F);
        trimMaterials.put("amethyst", 1.0F);

        ItemModelBuilder builder = getBuilder(armorPath)
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(armorNamespace, "item/" + armorPath));

        for (Map.Entry<String, Float> entry : trimMaterials.entrySet()) {
            String materialName = entry.getKey();
            float trimValue = entry.getValue();

            String trimModelName = armorPath + "_" + materialName + "_trim";

            ResourceLocation trimTexture = new ResourceLocation("minecraft", "trims/items/" + armorPart + "_trim_" + materialName);

            existingFileHelper.trackGenerated(trimTexture, PackType.CLIENT_RESOURCES, ".png", "textures");

            getBuilder(trimModelName)
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", new ResourceLocation(armorNamespace, "item/" + armorPath))
                    .texture("layer1", trimTexture);

            builder.override()
                    .predicate(new ResourceLocation("trim_type"), trimValue)
                    .model(new ModelFile.UncheckedModelFile(modLoc("item/" + trimModelName)))
                    .end();
        }
    }

    private ItemModelBuilder handheldItem(Item item) {
        return withExistingParent(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath(), mcLoc("item/handheld"))
                .texture("layer0", modLoc("item/" + Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).getPath()));
    }
}
