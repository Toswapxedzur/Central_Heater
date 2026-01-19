package com.minecart.central_heater.misc;

import com.minecart.central_heater.AllBlockItem;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber
public class DataMapHook {

    private static final Map<Item, Float> FIRE_ASH_MAP = new HashMap<>();
    private static final Map<TagKey<Item>, Float> FIRE_ASH_TAG_MAP = new HashMap<>();

    private static final Map<Item, Integer> NETHER_FUEL_MAP = new HashMap<>();

    private static final Map<Item, Float> SCORCHED_DUST_MAP = new HashMap<>();

    // Call this method during FMLCommonSetupEvent to safely populate maps
    public static void init() {
        // --- Fire Ash Drop Chance ---
        FIRE_ASH_MAP.put(Items.COAL_BLOCK, 1.0f);
        FIRE_ASH_MAP.put(Items.CHARCOAL, 0.8f);
        FIRE_ASH_MAP.put(Items.COAL, 0.7f);
        FIRE_ASH_MAP.put(Items.DRIED_KELP_BLOCK, 0.5f);

        FIRE_ASH_TAG_MAP.put(ItemTags.LOGS, 0.5f);
        FIRE_ASH_TAG_MAP.put(ItemTags.LOGS_THAT_BURN, 0.5f);
        FIRE_ASH_TAG_MAP.put(ItemTags.BOATS, 0.3f);
        FIRE_ASH_TAG_MAP.put(ItemTags.CHEST_BOATS, 0.3f);
        FIRE_ASH_TAG_MAP.put(ItemTags.PLANKS, 0.25f);
        FIRE_ASH_TAG_MAP.put(ItemTags.WOODEN_STAIRS, 0.25f);
        FIRE_ASH_TAG_MAP.put(ItemTags.WOODEN_DOORS, 0.25f);
        FIRE_ASH_TAG_MAP.put(ItemTags.WOODEN_FENCES, 0.2f);
        FIRE_ASH_TAG_MAP.put(ItemTags.FENCE_GATES, 0.2f);
        FIRE_ASH_TAG_MAP.put(ItemTags.SIGNS, 0.2f);
        FIRE_ASH_TAG_MAP.put(ItemTags.HANGING_SIGNS, 0.2f);
        FIRE_ASH_TAG_MAP.put(ItemTags.BANNERS, 0.2f);
        FIRE_ASH_TAG_MAP.put(ItemTags.WOODEN_PRESSURE_PLATES, 0.2f);
        FIRE_ASH_TAG_MAP.put(ItemTags.WOODEN_BUTTONS, 0.2f);

        FIRE_ASH_MAP.put(Items.CRAFTING_TABLE, 0.2f);
        FIRE_ASH_MAP.put(Items.CARTOGRAPHY_TABLE, 0.2f);
        FIRE_ASH_MAP.put(Items.FLETCHING_TABLE, 0.2f);
        FIRE_ASH_MAP.put(Items.SMITHING_TABLE, 0.2f);
        FIRE_ASH_MAP.put(Items.LOOM, 0.2f);
        FIRE_ASH_MAP.put(Items.COMPOSTER, 0.2f);
        FIRE_ASH_MAP.put(Items.BARREL, 0.2f);
        FIRE_ASH_MAP.put(Items.CHEST, 0.2f);
        FIRE_ASH_MAP.put(Items.TRAPPED_CHEST, 0.2f);
        FIRE_ASH_MAP.put(Items.BOOKSHELF, 0.2f);
        FIRE_ASH_MAP.put(Items.LECTERN, 0.2f);
        FIRE_ASH_MAP.put(Items.NOTE_BLOCK, 0.2f);
        FIRE_ASH_MAP.put(Items.JUKEBOX, 0.2f);
        FIRE_ASH_MAP.put(Items.DAYLIGHT_DETECTOR, 0.2f);
        FIRE_ASH_MAP.put(Items.CHISELED_BOOKSHELF, 0.2f);

        FIRE_ASH_TAG_MAP.put(ItemTags.WOODEN_SLABS, 0.12f);
        FIRE_ASH_TAG_MAP.put(ItemTags.WOODEN_TRAPDOORS, 0.12f);
        FIRE_ASH_TAG_MAP.put(ItemTags.WOOL, 0.1f);

        FIRE_ASH_MAP.put(Items.BLAZE_ROD, 0.1f);
        FIRE_ASH_MAP.put(Items.STICK, 0.05f);
        FIRE_ASH_TAG_MAP.put(ItemTags.SAPLINGS, 0.05f);
        FIRE_ASH_MAP.put(Items.BOWL, 0.05f);
        FIRE_ASH_TAG_MAP.put(ItemTags.WOOL_CARPETS, 0.05f);
        FIRE_ASH_MAP.put(Items.SCAFFOLDING, 0.05f);
        FIRE_ASH_MAP.put(Items.LADDER, 0.05f);
        FIRE_ASH_MAP.put(Items.TORCH, 0.05f);
        FIRE_ASH_MAP.put(Items.SOUL_TORCH, 0.05f);
        FIRE_ASH_MAP.put(Items.DRIED_KELP, 0.05f);
        FIRE_ASH_MAP.put(Items.BAMBOO, 0.02f);
        FIRE_ASH_MAP.put(Items.LAVA_BUCKET, 0.0f);

        // Mod Items
        FIRE_ASH_MAP.put(AllBlockItem.FIRE_ASH.get(), 0.5f);
        FIRE_ASH_MAP.put(AllBlockItem.WOOD_CHIPS.get(), 0.25f);
        FIRE_ASH_MAP.put(AllBlockItem.BURNT_LOG.get().asItem(), 0.8f);
        FIRE_ASH_MAP.put(AllBlockItem.BURNT_WOOD.get().asItem(), 0.8f);

        // --- Nether Fuel Burn Time ---
        NETHER_FUEL_MAP.put(Items.SOUL_SAND, 100);
        NETHER_FUEL_MAP.put(Items.SOUL_SOIL, 100);
        NETHER_FUEL_MAP.put(AllBlockItem.SCORCHED_DUST.get(), 100);
        NETHER_FUEL_MAP.put(AllBlockItem.SOUL_MIXTURE.get(), 300);
        NETHER_FUEL_MAP.put(AllBlockItem.SCORCHED_COAL.get(), 600);

        // --- Scorched Dust Drop Chance ---
        SCORCHED_DUST_MAP.put(Items.SOUL_SAND, 0.1f);
        SCORCHED_DUST_MAP.put(Items.SOUL_SOIL, 0.1f);
        SCORCHED_DUST_MAP.put(AllBlockItem.SCORCHED_DUST.get(), 0.2f);
        SCORCHED_DUST_MAP.put(AllBlockItem.SOUL_MIXTURE.get(), 0.2f);
        SCORCHED_DUST_MAP.put(AllBlockItem.SCORCHED_COAL.get(), 0.5f);
    }

    public static int getNetherFuelBurnTime(ItemStack stack){
        return NETHER_FUEL_MAP.getOrDefault(stack.getItem(), 0);
    }

    public static float getFireAshDropChance(ItemStack stack){
        if(ForgeHooks.getBurnTime(stack, RecipeType.SMELTING) == 0)
            return 0f;

        if(FIRE_ASH_MAP.containsKey(stack.getItem()))
            return FIRE_ASH_MAP.get(stack.getItem());

        for(Map.Entry<TagKey<Item>, Float> entry : FIRE_ASH_TAG_MAP.entrySet()){
            if(stack.is(entry.getKey()))
                return entry.getValue();
        }

        return AllBlockItem.DEFAULT_FIRE_ASH_DROP_CHANCE;
    }

    public static float getScorchedDustDropChance(ItemStack stack){
        if(getNetherFuelBurnTime(stack) == 0)
            return 0f;

        if(SCORCHED_DUST_MAP.containsKey(stack.getItem()))
            return SCORCHED_DUST_MAP.get(stack.getItem());

        return AllBlockItem.DEFAULT_SCORCHED_DUST_DROP_CHANCE;
    }
}