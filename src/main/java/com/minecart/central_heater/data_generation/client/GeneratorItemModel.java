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

        // Basic Items
        basicItem(AllBlockItem.COBBLE.get());
        basicItem(AllBlockItem.DEEPSLATE_COBBLE.get());
        basicItem(AllBlockItem.STONE_BRICK.get());
        basicItem(AllBlockItem.MUD_BRICK.get());
        basicItem(AllBlockItem.RED_NETHER_BRICK.get());
        basicItem(AllBlockItem.DEEPSLATE_BRICK.get());
        basicItem(AllBlockItem.DIAMOND_SHARD.get());
        basicItem(AllBlockItem.BLACKSTONE_BRICK.get());
        basicItem(AllBlockItem.STURDY_BRICK.get());
        basicItem(AllBlockItem.STURDY_TANK_ITEM.get());
        basicItem(AllBlockItem.STURDY_NUGGET.get());
        basicItem(AllBlockItem.SCORCHED_COAL.get());
        basicItem(AllBlockItem.SCORCHED_DUST.get());
        basicItem(AllBlockItem.FIRE_ASH.get());
        basicItem(AllBlockItem.CLAY_BIT.get());
        basicItem(AllBlockItem.CLAY_BRICK.get());
        basicItem(AllBlockItem.SOUL_MIXTURE.get());
        basicItem(AllBlockItem.WHEAT_DOUGH.get());
        basicItem(AllBlockItem.WHEAT_FLOUR.get());
        basicItem(AllBlockItem.WOOD_CHIPS.get());
        basicItem(AllBlockItem.CLAY_CAULDRON.get().asItem());
        basicItem(AllBlockItem.BRICK_CAULDRON.get().asItem());
        withExistingParent("iron_cauldron", mcLoc("item/cauldron"));
        basicItem(AllBlockItem.GOLDEN_CAULDRON.get().asItem());
        withExistingParent("blazing_furnace", modLoc("block/blazing_furnace"));
        basicItemWithTexture(AllBlockItem.GOLD_BARS.get().asItem(), "block/gold_bars");

        // Tools
        handheldItem(AllBlockItem.STURDY_PICKAXE.get());
        handheldItem(AllBlockItem.STURDY_AXE.get());
        handheldItem(AllBlockItem.STURDY_SHOVEL.get());
        handheldItem(AllBlockItem.STURDY_HOE.get());
        handheldItem(AllBlockItem.STURDY_SWORD.get());

        // Entity / Food
        basicItem(AllBlockItem.BLAZING_FURNACE_MINECART.get());
        basicItem(AllBlockItem.BURNT_BEEF.get());
        basicItem(AllBlockItem.BURNT_CHICKEN.get());
        basicItem(AllBlockItem.BURNT_COD.get());
        basicItem(AllBlockItem.BURNT_MUTTON.get());
        basicItem(AllBlockItem.BURNT_PORKCHOP.get());
        basicItem(AllBlockItem.BURNT_RABBIT.get());
        basicItem(AllBlockItem.BURNT_SALMON.get());

        // Armor Trims
        generateArmorTrims((ArmorItem) AllBlockItem.STURDY_CHESTPLATE.get());
        generateArmorTrims((ArmorItem) AllBlockItem.STURDY_HELMET.get());
        generateArmorTrims((ArmorItem) AllBlockItem.STURDY_LEGGINGS.get());
        generateArmorTrims((ArmorItem) AllBlockItem.STURDY_BOOTS.get());
    }

    public ItemModelBuilder basicItemWithTexture(Item item, String key) {
        return getBuilder(Objects.requireNonNull(ForgeRegistries.ITEMS.getKey(item)).toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", modLoc(key));
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