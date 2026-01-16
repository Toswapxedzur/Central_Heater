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

        generateArmorTrims(AllBlockItem.STURDY_CHESTPLATE.get());
        generateArmorTrims(AllBlockItem.STURDY_HELMET.get());
        generateArmorTrims(AllBlockItem.STURDY_LEGGINGS.get());
        generateArmorTrims(AllBlockItem.STURDY_BOOTS.get());
    }

    public ItemModelBuilder basicItemWithTexture(Item item, String key) {
        return getBuilder(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)).toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc(key));
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
