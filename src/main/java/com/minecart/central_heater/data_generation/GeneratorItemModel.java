package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.Central_heater;
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
        super(output, Central_heater.MODID, existingFileHelper);
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
        basicItem(AllBlockItem.cobble.asItem());
        basicItem(AllBlockItem.deepslate_cobble.asItem());
        basicItem(AllBlockItem.stone_brick.asItem());
        basicItem(AllBlockItem.mud_brick.asItem());
        basicItem(AllBlockItem.red_nether_brick.asItem());
        basicItem(AllBlockItem.deepslate_brick.asItem());
        basicItem(AllBlockItem.diamond_shard.asItem());
        basicItem(AllBlockItem.blackstone_brick.asItem());
        basicItem(AllBlockItem.sturdy_brick.asItem());
        basicItem(AllBlockItem.sturdy_tank_item.asItem());
        basicItem(AllBlockItem.sturdy_nugget.asItem());
        basicItem(AllBlockItem.scorched_coal.asItem());
        basicItem(AllBlockItem.scorched_dust.asItem());
        basicItem(AllBlockItem.fire_ash.asItem());
        basicItem(AllBlockItem.clay_bit.asItem());
        basicItem(AllBlockItem.clay_brick.asItem());
        basicItem(AllBlockItem.soul_mixture.asItem());
        basicItem(AllBlockItem.wheat_dough.asItem());
        basicItem(AllBlockItem.wheat_flour.asItem());
        basicItem(AllBlockItem.wood_chips.asItem());
        basicItem(AllBlockItem.clay_cauldron.asItem());
        basicItem(AllBlockItem.brick_cauldron.asItem());
        withExistingParent("iron_cauldron", mcLoc("item/cauldron"));
        basicItem(AllBlockItem.golden_cauldron.asItem());
        basicItemWithTexture(AllBlockItem.gold_bars.asItem(), "block/gold_bars");
        handheldItem(AllBlockItem.sturdy_pickaxe.asItem());
        handheldItem(AllBlockItem.sturdy_axe.asItem());
        handheldItem(AllBlockItem.sturdy_shovel.asItem());
        handheldItem(AllBlockItem.sturdy_hoe.asItem());
        handheldItem(AllBlockItem.sturdy_sword.asItem());

        basicItem(AllBlockItem.burnt_beef.asItem());
        basicItem(AllBlockItem.burnt_chicken.asItem());
        basicItem(AllBlockItem.burnt_cod.asItem());
        basicItem(AllBlockItem.burnt_mutton.asItem());
        basicItem(AllBlockItem.burnt_porkchop.asItem());
        basicItem(AllBlockItem.burnt_rabbit.asItem());
        basicItem(AllBlockItem.burnt_salmon.asItem());

        generateArmorTrims(AllBlockItem.sturdy_chestplate.get());
        generateArmorTrims(AllBlockItem.sturdy_helmet.get());
        generateArmorTrims(AllBlockItem.sturdy_leggings.get());
        generateArmorTrims(AllBlockItem.sturdy_boots.get());
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
