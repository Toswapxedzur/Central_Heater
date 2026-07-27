package com.minecart.central_heater.data_generation.client;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.armortrim.TrimMaterial;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class GeneratorItemModel extends ItemModelProvider {
    public GeneratorItemModel(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, CentralHeater.MODID, existingFileHelper);
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
        withExistingParent("sturdy_brick_tile_stair", modLoc("block/sturdy_brick_tile_stair"));
        withExistingParent("sturdy_brick_tile_slab", modLoc("block/sturdy_brick_tile_slab"));
        wallInventory("sturdy_brick_tile_wall", modLoc("block/sturdy_brick_tile"));
        withExistingParent("burnt_wood", modLoc("block/burnt_wood"));
        basicItem(AllBlockItem.SCORCHED_BRIQUETTES.asItem());

        withExistingParent("sturdy_bricks", modLoc("block/sturdy_bricks"));
        withExistingParent("sturdy_brick_stair", modLoc("block/sturdy_brick_stair"));
        withExistingParent("sturdy_brick_slab", modLoc("block/sturdy_brick_slab"));

        withExistingParent("sturdy_anvil", modLoc("block/sturdy_anvil"));
        withExistingParent("chipped_sturdy_anvil", modLoc("block/chipped_sturdy_anvil"));
        withExistingParent("damaged_sturdy_anvil", modLoc("block/damaged_sturdy_anvil"));
        basicItem(AllBlockItem.COBBLE.asItem());
        basicItem(AllBlockItem.DEEPSLATE_COBBLE.asItem());
        basicItem(AllBlockItem.STONE_BRICK.asItem());
        basicItem(AllBlockItem.MUD_BRICK.asItem());
        basicItem(AllBlockItem.RED_NETHER_BRICK.asItem());
        basicItem(AllBlockItem.DEEPSLATE_BRICK.asItem());
        basicItem(AllBlockItem.DIAMOND_SHARD.asItem());
        basicItem(AllBlockItem.BLACKSTONE_BRICK.asItem());
        basicItem(AllBlockItem.STURDY_BRICK.asItem());
        basicItem(AllBlockItem.STURDY_TANK_ITEM.asItem());
        basicItem(AllBlockItem.STURDY_NUGGET.asItem());
        basicItem(AllBlockItem.SCORCHED_COAL.asItem());
        basicItem(AllBlockItem.SCORCHED_DUST.asItem());
        basicItem(AllBlockItem.FIRE_ASH.asItem());
        basicItem(AllBlockItem.CLAY_BIT.asItem());
        basicItem(AllBlockItem.CLAY_BRICK.asItem());
        basicItem(AllBlockItem.SOUL_MIXTURE.asItem());
        basicItem(AllBlockItem.WHEAT_DOUGH.asItem());
        basicItem(AllBlockItem.WHEAT_FLOUR.asItem());
        basicItem(AllBlockItem.WOOD_CHIPS.asItem());
        basicItem(AllBlockItem.CLAY_CAULDRON.asItem());
        basicItem(AllBlockItem.BRICK_CAULDRON.asItem());
        withExistingParent("iron_cauldron", mcLoc("item/cauldron"));
        basicItem(AllBlockItem.GOLDEN_CAULDRON.asItem());
        withExistingParent("blazing_furnace", modLoc("block/blazing_furnace"));
        basicItemWithTexture(AllBlockItem.GOLD_BARS.asItem(), "block/gold_bars");
        handheldItem(AllBlockItem.STURDY_PICKAXE.asItem());
        handheldItem(AllBlockItem.STURDY_AXE.asItem());
        handheldItem(AllBlockItem.STURDY_SHOVEL.asItem());
        handheldItem(AllBlockItem.STURDY_HOE.asItem());
        handheldItem(AllBlockItem.STURDY_SWORD.asItem());
        basicItem(AllBlockItem.BLAZING_FURNACE_MINECART.asItem());

        basicItem(AllBlockItem.BURNT_BEEF.asItem());
        basicItem(AllBlockItem.BURNT_CHICKEN.asItem());
        basicItem(AllBlockItem.BURNT_COD.asItem());
        basicItem(AllBlockItem.BURNT_MUTTON.asItem());
        basicItem(AllBlockItem.BURNT_PORKCHOP.asItem());
        basicItem(AllBlockItem.BURNT_RABBIT.asItem());
        basicItem(AllBlockItem.BURNT_SALMON.asItem());

        // --- Standard Items ---
        basicItem(AllBlockItem.BRIQUETTES.asItem());
        basicItem(AllBlockItem.SOAP.asItem());
        handheldItem(AllBlockItem.STURDY_SHEARS.asItem());

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

        // Iron
//        withExistingParent("iron_bricks", modLoc("block/iron_bricks"));
//        withExistingParent("iron_brick_stair", modLoc("block/iron_brick_stair"));
//        withExistingParent("iron_brick_slab", modLoc("block/iron_brick_slab"));
//        withExistingParent("iron_brick_tile", modLoc("block/iron_brick_tile"));
//        withExistingParent("iron_brick_tile_stair", modLoc("block/iron_brick_tile_stair"));
//        withExistingParent("iron_brick_tile_slab", modLoc("block/iron_brick_tile_slab"));
//        wallInventory("iron_brick_tile_wall", modLoc("block/iron_brick_tile"));

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

        for (AllBlockItem.BurntWoodSet set : AllBlockItem.BURNT_WOOD_SETS) {
            burntWoodSetItems(set);
        }

        basicItem(AllBlockItem.BURNT_BOAT.asItem());
        basicItem(AllBlockItem.BURNT_CHEST_BOAT.asItem());
        basicItem(AllBlockItem.COAL_BIT.asItem());
        basicItem(AllBlockItem.CHARCOAL_BIT.asItem());

        basicItemWithTexture(AllBlockItem.BURNABLE_CAMPFIRE.asItem(), mcLoc("item/campfire"));
        basicItemWithTexture(AllBlockItem.BURNABLE_SOUL_CAMPFIRE.asItem(), mcLoc("item/soul_campfire"));

        generateArmorTrims(AllBlockItem.STURDY_CHESTPLATE.get());
        generateArmorTrims(AllBlockItem.STURDY_HELMET.get());
        generateArmorTrims(AllBlockItem.STURDY_LEGGINGS.get());
        generateArmorTrims(AllBlockItem.STURDY_BOOTS.get());
    }

    private void burntWoodSetItems(AllBlockItem.BurntWoodSet set) {
        String prefix = set.prefix();
        withExistingParent(prefix + "_log", modLoc("block/" + prefix + "_log_layer4"));
        withExistingParent(prefix + "_wood", modLoc("block/" + prefix + "_wood"));
        withExistingParent(prefix + "_planks", modLoc("block/" + prefix + "_planks"));
        withExistingParent(prefix + "_stairs", modLoc("block/" + prefix + "_stairs"));
        withExistingParent(prefix + "_slab", modLoc("block/" + prefix + "_slab"));
        fenceInventory(prefix + "_fence", modLoc("block/burnt_planks"));
        withExistingParent(prefix + "_fence_gate", modLoc("block/" + prefix + "_fence_gate"));
        withExistingParent(prefix + "_trapdoor", modLoc("block/" + prefix + "_trapdoor_bottom"));
        buttonInventory(prefix + "_button", modLoc("block/burnt_planks"));
        withExistingParent(prefix + "_pressure_plate", modLoc("block/" + prefix + "_pressure_plate"));
        basicItemWithTexture(set.door().asItem(), modLoc("item/burnt_door"));
    }

    public ItemModelBuilder basicItemWithTexture(Item item, String key) {
        return basicItemWithTexture(item, modLoc(key));
    }

    public ItemModelBuilder basicItemWithTexture(Item item, ResourceLocation key) {
        return getBuilder(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)).toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", key);
    }

    public void trimMaterial(TrimMaterial material){
        String meterialName = material.assetName();
        for(Item item : BuiltInRegistries.ITEM){
            if(item instanceof ArmorItem armorItem){
                String armorPart = switch (armorItem.getEquipmentSlot()){
                    case HEAD -> "helmet";
                    case CHEST -> "chestplate";
                    case LEGS -> "leggings";
                    case FEET -> "boots";
                    default -> "";
                };
                if(armorPart == "")
                    continue;
                String armorNameSpace = BuiltInRegistries.ITEM.getKey(armorItem.asItem()).getNamespace();
                String armorPath = BuiltInRegistries.ITEM.getKey(armorItem.asItem()).getPath();
                existingFileHelper.trackGenerated(mcLoc("trims/items/" + armorPart + "_trim_" + meterialName), PackType.CLIENT_RESOURCES, ".png", "textures");
                getBuilder(armorPath + "_" + meterialName + "_trim").parent(new ModelFile.UncheckedModelFile("item/generated"))
                        .texture("layer0", ResourceLocation.fromNamespaceAndPath(armorNameSpace, "item/" + armorPath))
                        .texture("layer1", mcLoc("trims/items/" + armorPart + "_trim_" + meterialName));
            }
        }
    }

    public void generateArmorTrims(ArmorItem armorItem) {
        ResourceLocation armorItemRes = BuiltInRegistries.ITEM.getKey(armorItem);
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
                .texture("layer0", ResourceLocation.fromNamespaceAndPath(armorNamespace, "item/" + armorPath));

        for (Map.Entry<String, Float> entry : trimMaterials.entrySet()) {
            String materialName = entry.getKey();
            float trimValue = entry.getValue();

            String trimModelName = armorPath + "_" + materialName + "_trim";

            getBuilder(trimModelName)
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", ResourceLocation.fromNamespaceAndPath(armorNamespace, "item/" + armorPath))
                    .texture("layer1", mcLoc("trims/items/" + armorPart + "_trim_" + materialName));

            builder.override()
                    .predicate(mcLoc("trim_type"), trimValue)
                    .model(new ModelFile.UncheckedModelFile(modLoc("item/" + trimModelName))).end();
        }
    }
}
