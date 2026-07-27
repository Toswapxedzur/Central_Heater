package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.recipe.recipe_types.FireBrewingRecipe;
import com.minecart.central_heater.misc.Alltags;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GeneratorRecipe extends HeaterRecipeProvider implements IConditionBuilder {
    public GeneratorRecipe(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        emptyRecipe(recipeOutput, "minecraft:brick");
        emptyRecipe(recipeOutput, "minecraft:stone_bricks_from_stone_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:stone_brick_slab_from_stone_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:stone_brick_stairs_from_stone_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:stone_brick_walls_from_stone_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_bricks_from_cobbled_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_stairs_from_cobbled_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_slab_from_cobbled_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_wall_from_cobbled_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_bricks_from_polished_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_stairs_from_polished_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_slab_from_polished_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_wall_from_polished_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:nether_brick_slab_from_nether_bricks_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:charcoal");
        emptyRecipe(recipeOutput, "minecraft:campfire");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CLAY_BALL)
                .requires(AllBlockItem.CLAY_BIT, 4)
                .unlockedBy(getHasName(AllBlockItem.CLAY_BIT), has(AllBlockItem.CLAY_BIT))
                .group("clay_processing")
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.CLAY_BIT, 4)
                .requires(Items.CLAY_BALL)
                .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .group("clay_processing")
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.CLAY_BRICK)
                .pattern("##")
                .define('#', Items.CLAY_BALL)
                .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .group("clay_processing")
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CLAY_BALL, 2)
                .requires(AllBlockItem.CLAY_BRICK)
                .unlockedBy("has_clay_brick", has(AllBlockItem.CLAY_BRICK))
                .save(recipeOutput, "clay_balls_from_clay_brick");

        oreSmelting(recipeOutput, List.of(AllBlockItem.CLAY_BRICK), RecipeCategory.MISC, Items.BRICK, 0.1f, 200, "clay_processing");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLACK_DYE)
                .requires(Alltags.Items.OVERBURNT)
                .unlockedBy("has_burnt_food", has(Alltags.Items.OVERBURNT))
                .save(recipeOutput, "black_dye_from_burnt_food");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.DIAMOND_SHARD, 4).requires(Items.DIAMOND)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.DIAMOND).requires(AllBlockItem.DIAMOND_SHARD, 4)
                .unlockedBy(getHasName(AllBlockItem.DIAMOND_SHARD), has(AllBlockItem.DIAMOND_SHARD)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.STURDY_NUGGET, 9).requires(AllBlockItem.STURDY_BRICK)
                .unlockedBy(getHasName(AllBlockItem.STURDY_BRICK), has(AllBlockItem.STURDY_BRICK)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.STURDY_BRICK).requires(AllBlockItem.STURDY_NUGGET, 9)
                .unlockedBy(getHasName(AllBlockItem.STURDY_NUGGET), has(AllBlockItem.STURDY_NUGGET)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.COBBLE, 4).requires(Items.COBBLESTONE)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(Items.COBBLESTONE)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COBBLESTONE).requires(AllBlockItem.COBBLE, 4)
                .unlockedBy(getHasName(AllBlockItem.COBBLE), has(AllBlockItem.COBBLE)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.DEEPSLATE_COBBLE, 4).requires(Items.COBBLED_DEEPSLATE)
                .unlockedBy(getHasName(Items.COBBLED_DEEPSLATE), has(Items.COBBLED_DEEPSLATE)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COBBLED_DEEPSLATE).requires(AllBlockItem.DEEPSLATE_COBBLE, 4)
                .unlockedBy(getHasName(AllBlockItem.DEEPSLATE_COBBLE), has(AllBlockItem.DEEPSLATE_COBBLE)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CHARCOAL)
                .requires(AllBlockItem.BURNT_LOG)
                .unlockedBy("has_burnt_log", has(AllBlockItem.BURNT_LOG))
                .save(recipeOutput, "charcoal_from_burnt_log");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CHARCOAL)
                .requires(AllBlockItem.BURNT_WOOD)
                .unlockedBy("has_burnt_wood", has(AllBlockItem.BURNT_WOOD))
                .save(recipeOutput, "charcoal_from_burnt_wood");

        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(ItemTags.PLANKS), 4, "planks");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(ItemTags.WOODEN_STAIRS), 3, "stairs");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(ItemTags.WOODEN_SLABS), 2, "slabs");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(ItemTags.WOODEN_PRESSURE_PLATES), 1, "pressure_plates");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(ItemTags.WOODEN_BUTTONS), 1, "buttons");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(ItemTags.WOODEN_DOORS), 8, "doors");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(ItemTags.WOODEN_TRAPDOORS), 6, "trapdoors");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(ItemTags.BOATS), 12, "boats");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(ItemTags.WOODEN_FENCES), 2, "fences");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(ItemTags.FENCE_GATES), 3, "fence_gates");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(ItemTags.SIGNS), 1, "signs");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(Items.STICK), 2, "sticks");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(ItemTags.LOGS), 16, "logs");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(Items.WOODEN_SHOVEL), 3, "wooden_shovel");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(Items.WOODEN_SWORD), 5, "wooden_sword");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(Items.WOODEN_HOE), 6, "wooden_hoe");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(Items.WOODEN_PICKAXE), 8, "wooden_pickaxe");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(Items.WOODEN_AXE), 8, "wooden_axe");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(Items.BOWL), 2, "bowls");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS, Ingredient.of(Items.LADDER), 4, "ladders");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, AllBlockItem.WHEAT_FLOUR)
                .requires(Items.WHEAT)
                .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.SCORCHED_DUST)
                .requires(Items.SOUL_SOIL)
                .unlockedBy(getHasName(Items.SOUL_SOIL), has(Items.SOUL_SOIL))
                .save(recipeOutput, CentralHeater.modLoc("scorched_dust_from_soul_soil"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.SCORCHED_DUST)
                .requires(Items.SOUL_SAND)
                .unlockedBy(getHasName(Items.SOUL_SAND), has(Items.SOUL_SAND))
                .save(recipeOutput, CentralHeater.modLoc("scorched_dust_from_soul_sand"));

        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_STONE_BRICKS, AllBlockItem.STONE_BRICK_TILE);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILES, AllBlockItem.DEEPSLATE_BRICK_TILE);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_STAIRS, AllBlockItem.DEEPSLATE_BRICK_TILE);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_SLAB, AllBlockItem.DEEPSLATE_BRICK_TILE, 2);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_WALL, AllBlockItem.DEEPSLATE_BRICK_TILE);

        brickTileRecipe(recipeOutput, AllBlockItem.STONE_BRICK_TILE.asItem(), AllBlockItem.STONE_BRICK.asItem());
        brickTileRecipe(recipeOutput, AllBlockItem.DEEPSLATE_BRICK_TILE.asItem(), AllBlockItem.DEEPSLATE_BRICK.asItem());
        brickTileRecipe(recipeOutput, AllBlockItem.MUD_BRICK_TILE.asItem(), AllBlockItem.MUD_BRICK.asItem());
        brickTileRecipe(recipeOutput, Items.NETHER_BRICKS, Items.NETHER_BRICK);
        brickTileRecipe(recipeOutput, Items.RED_NETHER_BRICKS, AllBlockItem.RED_NETHER_BRICK.asItem());
        brickTileRecipe(recipeOutput, Items.BRICKS, Items.BRICK);
        brickTileRecipe(recipeOutput, AllBlockItem.BLACKSTONE_BRICK_TILE.asItem(), AllBlockItem.BLACKSTONE_BRICK.asItem());

        brickRecipe(recipeOutput, Items.STONE_BRICKS, AllBlockItem.STONE_BRICK.asItem());
        brickRecipe(recipeOutput, Items.DEEPSLATE_BRICKS, AllBlockItem.DEEPSLATE_BRICK.asItem());
        brickRecipe(recipeOutput, Items.MUD_BRICKS, AllBlockItem.MUD_BRICK.asItem());
        brickRecipe(recipeOutput, Items.POLISHED_BLACKSTONE_BRICKS, AllBlockItem.BLACKSTONE_BRICK.asItem());

        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.STONE_BRICK_TILE.asItem(), AllBlockItem.STONE_BRICK_TILE_STAIR.asItem(),
                AllBlockItem.STONE_BRICK_TILE_SLAB.asItem(), AllBlockItem.STONE_BRICK_TILE_WALL.asItem());
        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.DEEPSLATE_BRICK_TILE.asItem(), AllBlockItem.DEEPSLATE_BRICK_TILE_STAIR.asItem(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_SLAB.asItem(), AllBlockItem.DEEPSLATE_BRICK_TILE_WALL.asItem());
        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.MUD_BRICK_TILE.asItem(), AllBlockItem.MUD_BRICK_TILE_STAIR.asItem(),
                AllBlockItem.MUD_BRICK_TILE_SLAB.asItem(), AllBlockItem.MUD_BRICK_TILE_WALL.asItem());
        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.BLACKSTONE_BRICK_TILE.asItem(), AllBlockItem.BLACKSTONE_BRICK_TILE_STAIR.asItem(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_SLAB.asItem(), AllBlockItem.BLACKSTONE_BRICK_TILE_WALL.asItem());

        oreBlasting(recipeOutput, List.of(Items.PACKED_MUD), RecipeCategory.MISC, AllBlockItem.MUD_BRICK, 0.1f, 200, "brick");
        oreBlasting(recipeOutput, List.of(Items.POLISHED_BLACKSTONE), RecipeCategory.MISC, AllBlockItem.BLACKSTONE_BRICK, 0.1f, 200, "brick");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.STONE_BRICK)
                .requires(AllBlockItem.COBBLE).requires(Items.CLAY_BALL).requires(Items.FLINT).requires(Items.IRON_NUGGET)
                .unlockedBy(getHasName(AllBlockItem.COBBLE), has(AllBlockItem.COBBLE)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.DEEPSLATE_BRICK)
                .requires(AllBlockItem.DEEPSLATE_COBBLE).requires(Items.CLAY_BALL).requires(Items.FLINT).requires(Items.GOLD_NUGGET)
                .unlockedBy(getHasName(AllBlockItem.DEEPSLATE_COBBLE), has(AllBlockItem.DEEPSLATE_COBBLE)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.RED_NETHER_BRICK).requires(Items.NETHER_BRICK).requires(Ingredient.of(Items.NETHER_WART, Items.RED_DYE))
                .unlockedBy(getHasName(Items.NETHER_BRICK), has(Items.NETHER_BRICK)).group("misc").save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.GOLD_BARS, 16).pattern("###").pattern("###").define('#', Items.GOLD_INGOT)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT)).group("misc").save(recipeOutput);

        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.BRICK_STOVE, Items.BRICK, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.MUD_BRICK_STOVE, AllBlockItem.MUD_BRICK, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.STONE_STOVE, AllBlockItem.STONE_BRICK, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.DEEPSLATE_STOVE, AllBlockItem.DEEPSLATE_BRICK, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.NETHER_BRICK_STOVE, Items.NETHER_BRICK, Items.GOLD_INGOT, AllBlockItem.GOLD_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.RED_NETHER_BRICK_STOVE, AllBlockItem.RED_NETHER_BRICK, Items.GOLD_INGOT, AllBlockItem.GOLD_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.BLACKSTONE_STOVE, AllBlockItem.BLACKSTONE_BRICK, Items.GOLD_INGOT, AllBlockItem.GOLD_BARS);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.MUD_BRICK_POT)
                .pattern("# #").pattern("# #").pattern("###")
                .define('#', AllBlockItem.MUD_BRICK)
                .unlockedBy(getHasName(AllBlockItem.MUD_BRICK), has(AllBlockItem.MUD_BRICK))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.STURDY_TANK_ITEM)
                .pattern("# #").pattern(" # ").define('#', AllBlockItem.STURDY_BRICK)
                .unlockedBy(getHasName(AllBlockItem.STURDY_BRICK), has(AllBlockItem.STURDY_BRICK))
                .group("misc").save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.CLAY_CAULDRON)
                .pattern("# #").pattern("# #").pattern("###")
                .define('#', AllBlockItem.CLAY_BRICK)
                .unlockedBy(getHasName(AllBlockItem.CLAY_BRICK), has(AllBlockItem.CLAY_BRICK))
                .save(recipeOutput);

        oreSmelting(recipeOutput, List.of(AllBlockItem.CLAY_CAULDRON), RecipeCategory.MISC, AllBlockItem.BRICK_CAULDRON, 0.3f, 200, "cauldron");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.IRON_CAULDRON).pattern("# #").pattern("# #").pattern("###")
                .define('#', Items.IRON_INGOT).unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT)).save(recipeOutput, ResourceLocation.withDefaultNamespace("cauldron"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.GOLDEN_CAULDRON).pattern("# #").pattern("# #").pattern("###")
                .define('#', Items.GOLD_INGOT).unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.BLAZING_FURNACE)
                .pattern("NNN")
                .pattern("NFN")
                .pattern("BBB")
                .define('N', Items.NETHERRACK)
                .define('B', AllBlockItem.GOLDEN_BRICKS)
                .define('F', Items.BLAST_FURNACE)
                .unlockedBy("has_golden_bricks", has(AllBlockItem.GOLDEN_BRICKS))
                .save(recipeOutput);

        List<ItemLike> sturdyGear = List.of(
                AllBlockItem.STURDY_PICKAXE, AllBlockItem.STURDY_AXE, AllBlockItem.STURDY_SHOVEL, AllBlockItem.STURDY_HOE, AllBlockItem.STURDY_SWORD,
                AllBlockItem.STURDY_HELMET, AllBlockItem.STURDY_CHESTPLATE, AllBlockItem.STURDY_LEGGINGS, AllBlockItem.STURDY_BOOTS
        );

        toolsBundle(recipeOutput, AllBlockItem.STURDY_BRICK, AllBlockItem.STURDY_PICKAXE, AllBlockItem.STURDY_AXE, AllBlockItem.STURDY_SHOVEL, AllBlockItem.STURDY_HOE, AllBlockItem.STURDY_SWORD);
        armorsBundle(recipeOutput, AllBlockItem.STURDY_BRICK, AllBlockItem.STURDY_HELMET, AllBlockItem.STURDY_CHESTPLATE, AllBlockItem.STURDY_LEGGINGS, AllBlockItem.STURDY_BOOTS);

        oreSmelting(recipeOutput, sturdyGear, RecipeCategory.MISC, AllBlockItem.STURDY_NUGGET, 0.1f, 200, "sturdy_nugget");
        oreBlasting(recipeOutput, sturdyGear, RecipeCategory.MISC, AllBlockItem.STURDY_NUGGET, 0.1f, 100, "sturdy_nugget");

        burntRecipe(recipeOutput, Items.BEEF, Items.COOKED_BEEF, AllBlockItem.BURNT_BEEF);
        burntRecipe(recipeOutput, Items.CHICKEN, Items.COOKED_CHICKEN, AllBlockItem.BURNT_CHICKEN);
        burntRecipe(recipeOutput, Items.COD, Items.COOKED_COD, AllBlockItem.BURNT_COD);
        burntRecipe(recipeOutput, Items.MUTTON, Items.COOKED_MUTTON, AllBlockItem.BURNT_MUTTON);
        burntRecipe(recipeOutput, Items.PORKCHOP, Items.COOKED_PORKCHOP, AllBlockItem.BURNT_PORKCHOP);
        burntRecipe(recipeOutput, Items.RABBIT, Items.COOKED_RABBIT, AllBlockItem.BURNT_RABBIT);
        burntRecipe(recipeOutput, Items.SALMON, Items.COOKED_SALMON, AllBlockItem.BURNT_SALMON);

        smoldering(recipeOutput, Ingredient.of(AllBlockItem.WHEAT_FLOUR), new FluidStack(Fluids.WATER, 250),
                new ItemStack(AllBlockItem.WHEAT_DOUGH.asItem()), FluidStack.EMPTY, 100, 1, 0);

        oreCampfiring(recipeOutput, List.of(AllBlockItem.WHEAT_DOUGH), RecipeCategory.FOOD, Items.BREAD, 0.35f, 600, "bread");

        smoldering(recipeOutput, Ingredient.of(AllBlockItem.SCORCHED_DUST), new FluidStack(Fluids.LAVA, 125),
                new ItemStack(AllBlockItem.SOUL_MIXTURE.asItem()), FluidStack.EMPTY, 100, 2, 1);

        oreSeething(recipeOutput, List.of(AllBlockItem.SOUL_MIXTURE), RecipeCategory.MISC, AllBlockItem.SCORCHED_COAL, 1f, 600, "scorched");
        oreSeething(recipeOutput, List.of(Items.SAND), RecipeCategory.MISC, Items.SOUL_SAND, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.DIRT), RecipeCategory.MISC, Items.SOUL_SOIL, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.INK_SAC), RecipeCategory.MISC, Items.GLOW_INK_SAC, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.BRICK), RecipeCategory.MISC, Items.NETHER_BRICK, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.REDSTONE), RecipeCategory.MISC, Items.GLOWSTONE_DUST, 1f, 800, "misc");
        oreSeething(recipeOutput, List.of(Items.SWEET_BERRIES), RecipeCategory.MISC, Items.GLOW_BERRIES, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.VINE), RecipeCategory.MISC, Items.GLOW_LICHEN, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.POTATO), RecipeCategory.MISC, Items.POISONOUS_POTATO, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.RED_MUSHROOM), RecipeCategory.MISC, Items.CRIMSON_FUNGUS, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.BROWN_MUSHROOM), RecipeCategory.MISC, Items.WARPED_FUNGUS, 1f, 400, "misc");

// Melting ice is quick: 100 ticks (5s)
        smoldering(recipeOutput, Ingredient.of(Items.ICE), FluidStack.EMPTY, NonNullList.of(ItemStack.EMPTY), new FluidStack(Fluids.WATER, 1000), 100, 1, 1);

// Separation/infusion reactions: 200 ticks (10s)
        smoldering(recipeOutput, Ingredient.of(Items.MAGMA_CREAM), FluidStack.EMPTY, new ItemStack(Items.SLIME_BALL), new FluidStack(Fluids.LAVA, 250), 200, 3, 2);
        smoldering(recipeOutput, Ingredient.of(Items.CHARCOAL), new FluidStack(Fluids.LAVA, 250), new ItemStack(Items.COAL), FluidStack.EMPTY, 200, 2, 2);

// Burning bone to ash is fast: 100 ticks (5s)
        smoldering(recipeOutput, Ingredient.of(Items.BONE), FluidStack.EMPTY, new ItemStack(Items.BONE_MEAL, 4), FluidStack.EMPTY, 100, 1, 1);

// Complex gooey mixture: 200 ticks (10s)
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.EGG), Ingredient.of(Items.CLAY_BALL), Ingredient.of(AllBlockItem.WHEAT_DOUGH), Ingredient.of(Items.LIME_DYE)), new FluidStack(Fluids.WATER, 250), new ItemStack(Items.SLIME_BALL, 2), FluidStack.EMPTY, 200, 1, 1);

// Basic building materials (Blast Furnace speed): 100 ticks (5s)
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.DEEPSLATE_COBBLE), Ingredient.of(Items.GOLD_NUGGET)), new FluidStack(Fluids.LAVA, 125), new ItemStack(AllBlockItem.DEEPSLATE_BRICK.asItem()), FluidStack.EMPTY, 100, 2, 1);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.COBBLE), Ingredient.of(Items.IRON_NUGGET)), new FluidStack(Fluids.LAVA, 125), new ItemStack(AllBlockItem.STONE_BRICK.asItem()), FluidStack.EMPTY, 100, 2, 1);

// Higher-tier building material (Standard Furnace speed): 200 ticks (10s)
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.COBBLE), Ingredient.of(Items.IRON_INGOT), Ingredient.of(Items.KELP)), new FluidStack(Fluids.LAVA, 125), new ItemStack(AllBlockItem.STURDY_BRICK.asItem()), FluidStack.EMPTY, 200, 3, 1);

// Gunpowder chemical mix: 200 ticks (10s)
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.FIRE_ASH.asItem()), Ingredient.of(Items.SUGAR), Ingredient.of(Items.CHARCOAL)), FluidStack.EMPTY, NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.GUNPOWDER)), FluidStack.EMPTY, 200, 2, 1);

        for (VanillaWoodSet wood : vanillaWoodSets()) {
            AllBlockItem.BurntWoodSet burnt = burntWoodSet(wood.material());
            blockSmoldering(recipeOutput, wood.log(), burnt.log().get(), 200, 1, true);
            blockSmoldering(recipeOutput, wood.strippedLog(), burnt.log().get(), 200, 1, true);
            blockSmoldering(recipeOutput, wood.wood(), burnt.wood().get(), 200, 1, true);
            blockSmoldering(recipeOutput, wood.strippedWood(), burnt.wood().get(), 200, 1, true);
            blockSmoldering(recipeOutput, wood.planks(), burnt.planks().get(), 50, 1, true);
            blockSmoldering(recipeOutput, wood.stairs(), burnt.stairs().get(), 38, 1, true);
            blockSmoldering(recipeOutput, wood.slab(), burnt.slab().get(), 25, 1, true);
            blockSmoldering(recipeOutput, wood.fence(), burnt.fence().get(), 50, 1, true);
            blockSmoldering(recipeOutput, wood.fenceGate(), burnt.fenceGate().get(), 50, 1, true);
            blockSmoldering(recipeOutput, wood.trapdoor(), burnt.trapdoor().get(), 50, 1, true);
            blockSmoldering(recipeOutput, wood.button(), burnt.button().get(), 25, 1, true);
            blockSmoldering(recipeOutput, wood.pressurePlate(), burnt.pressurePlate().get(), 25, 1, true);
            blockSmoldering(recipeOutput, wood.door(), burnt.door().get(), 50, 1, true);
        }

        for (AllBlockItem.BurntWoodSet set : AllBlockItem.BURNT_WOOD_SETS) {
            ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.STICK, 4)
                    .pattern("#")
                    .pattern("#")
                    .define('#', set.planks().get())
                    .unlockedBy("has_" + set.prefix() + "_planks", has(set.planks().get()))
                    .save(recipeOutput, CentralHeater.modLoc("sticks_from_" + set.prefix() + "_planks"));
        }

        blockSmoldering(recipeOutput, Blocks.BRICKS, Blocks.NETHER_BRICKS, 200, 2, false);
        blockSmoldering(recipeOutput, Blocks.BRICK_STAIRS, Blocks.NETHER_BRICK_STAIRS, 200, 2, false);
        blockSmoldering(recipeOutput, Blocks.BRICK_SLAB, Blocks.NETHER_BRICK_SLAB, 200, 2, false);
        blockSmoldering(recipeOutput, Blocks.BRICK_WALL, Blocks.NETHER_BRICK_WALL, 200, 2, false);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.SOUL_TORCH, 4).pattern("C").pattern("S")
                .define('C', AllBlockItem.SCORCHED_COAL.asItem()).define('S', Items.STICK)
                .unlockedBy("has_scorched_coal", has(AllBlockItem.SCORCHED_COAL.asItem())).save(recipeOutput, ResourceLocation.parse("minecraft:soul_torch"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.SOUL_CAMPFIRE).pattern(" S ").pattern("SCS").pattern("LLL")
                .define('S', Items.STICK).define('C', AllBlockItem.SCORCHED_COAL.asItem()).define('L', ItemTags.LOGS_THAT_BURN)
                .unlockedBy("has_scorched_coal", has(AllBlockItem.SCORCHED_COAL.asItem())).save(recipeOutput, ResourceLocation.parse("minecraft:soul_campfire"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, AllBlockItem.BLAZING_FURNACE_MINECART.get())
                .requires(AllBlockItem.BLAZING_FURNACE.get())
                .requires(Items.MINECART)
                .unlockedBy("has_blazing_furnace", has(AllBlockItem.BLAZING_FURNACE.get()))
                .save(recipeOutput);

        SpecialRecipeBuilder.special(category -> new FireBrewingRecipe()).save(recipeOutput, "cauldron_potion_brewing");


        oreSeething(recipeOutput, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER, 0.1f, 400, "leather_curing");

        List<ItemLike> allSaplings = List.of(
                Items.OAK_SAPLING, Items.SPRUCE_SAPLING, Items.BIRCH_SAPLING, Items.JUNGLE_SAPLING,
                Items.ACACIA_SAPLING, Items.DARK_OAK_SAPLING, Items.CHERRY_SAPLING,
                Items.AZALEA, Items.FLOWERING_AZALEA, Items.BAMBOO
        );

        oreSmelting(recipeOutput, allSaplings, RecipeCategory.MISC, Items.DEAD_BUSH, 0.1f, 200, "dead_bush_from_sapling");

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.FIRE_ASH), Ingredient.of(Items.SAND)),
                new FluidStack(Fluids.WATER, 250),
                new ItemStack(AllBlockItem.CLAY_BIT.asItem()), FluidStack.EMPTY, 200, 1, 0);

        smoldering(recipeOutput, Ingredient.of(Items.WHITE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.WHITE_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.ORANGE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.ORANGE_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.MAGENTA_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.MAGENTA_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.LIGHT_BLUE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.LIGHT_BLUE_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.YELLOW_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.YELLOW_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.LIME_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.LIME_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.PINK_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.PINK_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.GRAY_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.GRAY_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.LIGHT_GRAY_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.LIGHT_GRAY_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.CYAN_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.CYAN_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.PURPLE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.PURPLE_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.BLUE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.BLUE_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.BROWN_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.BROWN_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.GREEN_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.GREEN_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.RED_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.RED_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.BLACK_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.BLACK_CONCRETE), FluidStack.EMPTY, 20, 0, 0);

        createCleaningRecipe(recipeOutput, Blocks.ORANGE_WOOL, Blocks.WHITE_WOOL, Items.ORANGE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.MAGENTA_WOOL, Blocks.WHITE_WOOL, Items.MAGENTA_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_BLUE_WOOL, Blocks.WHITE_WOOL, Items.LIGHT_BLUE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.YELLOW_WOOL, Blocks.WHITE_WOOL, Items.YELLOW_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIME_WOOL, Blocks.WHITE_WOOL, Items.LIME_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.PINK_WOOL, Blocks.WHITE_WOOL, Items.PINK_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.GRAY_WOOL, Blocks.WHITE_WOOL, Items.GRAY_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_GRAY_WOOL, Blocks.WHITE_WOOL, Items.LIGHT_GRAY_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.CYAN_WOOL, Blocks.WHITE_WOOL, Items.CYAN_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.PURPLE_WOOL, Blocks.WHITE_WOOL, Items.PURPLE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BLUE_WOOL, Blocks.WHITE_WOOL, Items.BLUE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BROWN_WOOL, Blocks.WHITE_WOOL, Items.BROWN_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.GREEN_WOOL, Blocks.WHITE_WOOL, Items.GREEN_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.RED_WOOL, Blocks.WHITE_WOOL, Items.RED_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BLACK_WOOL, Blocks.WHITE_WOOL, Items.BLACK_DYE, 1.0f);

        createCleaningRecipe(recipeOutput, Blocks.ORANGE_CARPET, Blocks.WHITE_CARPET, Items.ORANGE_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.MAGENTA_CARPET, Blocks.WHITE_CARPET, Items.MAGENTA_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_BLUE_CARPET, Blocks.WHITE_CARPET, Items.LIGHT_BLUE_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.YELLOW_CARPET, Blocks.WHITE_CARPET, Items.YELLOW_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.LIME_CARPET, Blocks.WHITE_CARPET, Items.LIME_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.PINK_CARPET, Blocks.WHITE_CARPET, Items.PINK_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.GRAY_CARPET, Blocks.WHITE_CARPET, Items.GRAY_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_GRAY_CARPET, Blocks.WHITE_CARPET, Items.LIGHT_GRAY_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.CYAN_CARPET, Blocks.WHITE_CARPET, Items.CYAN_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.PURPLE_CARPET, Blocks.WHITE_CARPET, Items.PURPLE_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.BLUE_CARPET, Blocks.WHITE_CARPET, Items.BLUE_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.BROWN_CARPET, Blocks.WHITE_CARPET, Items.BROWN_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.GREEN_CARPET, Blocks.WHITE_CARPET, Items.GREEN_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.RED_CARPET, Blocks.WHITE_CARPET, Items.RED_DYE, 0.60f);
        createCleaningRecipe(recipeOutput, Blocks.BLACK_CARPET, Blocks.WHITE_CARPET, Items.BLACK_DYE, 0.60f);

        // --- GLASS PANE (4.5% Drop Rate, Base: Glass Pane) ---
        createCleaningRecipe(recipeOutput, Blocks.WHITE_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.WHITE_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.ORANGE_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.ORANGE_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.MAGENTA_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.MAGENTA_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.LIGHT_BLUE_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.YELLOW_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.YELLOW_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.LIME_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.LIME_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.PINK_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.PINK_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.GRAY_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.GRAY_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.LIGHT_GRAY_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.CYAN_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.CYAN_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.PURPLE_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.PURPLE_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.BLUE_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.BLUE_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.BROWN_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.BROWN_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.GREEN_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.GREEN_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.RED_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.RED_DYE, 0.045f);
        createCleaningRecipe(recipeOutput, Blocks.BLACK_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.BLACK_DYE, 0.045f);

        // --- GLASS (12% Drop Rate, Base: Glass) ---
        createCleaningRecipe(recipeOutput, Blocks.WHITE_STAINED_GLASS, Blocks.GLASS, Items.WHITE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.ORANGE_STAINED_GLASS, Blocks.GLASS, Items.ORANGE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.MAGENTA_STAINED_GLASS, Blocks.GLASS, Items.MAGENTA_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.GLASS, Items.LIGHT_BLUE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.YELLOW_STAINED_GLASS, Blocks.GLASS, Items.YELLOW_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.LIME_STAINED_GLASS, Blocks.GLASS, Items.LIME_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.PINK_STAINED_GLASS, Blocks.GLASS, Items.PINK_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.GRAY_STAINED_GLASS, Blocks.GLASS, Items.GRAY_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.GLASS, Items.LIGHT_GRAY_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.CYAN_STAINED_GLASS, Blocks.GLASS, Items.CYAN_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.PURPLE_STAINED_GLASS, Blocks.GLASS, Items.PURPLE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.BLUE_STAINED_GLASS, Blocks.GLASS, Items.BLUE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.BROWN_STAINED_GLASS, Blocks.GLASS, Items.BROWN_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.GREEN_STAINED_GLASS, Blocks.GLASS, Items.GREEN_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.RED_STAINED_GLASS, Blocks.GLASS, Items.RED_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.BLACK_STAINED_GLASS, Blocks.GLASS, Items.BLACK_DYE, 0.12f);

        // --- TERRACOTTA (12% Drop Rate, Base: Terracotta) ---
        createCleaningRecipe(recipeOutput, Blocks.WHITE_TERRACOTTA, Blocks.TERRACOTTA, Items.WHITE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.ORANGE_TERRACOTTA, Blocks.TERRACOTTA, Items.ORANGE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.MAGENTA_TERRACOTTA, Blocks.TERRACOTTA, Items.MAGENTA_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.TERRACOTTA, Items.LIGHT_BLUE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.YELLOW_TERRACOTTA, Blocks.TERRACOTTA, Items.YELLOW_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.LIME_TERRACOTTA, Blocks.TERRACOTTA, Items.LIME_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.PINK_TERRACOTTA, Blocks.TERRACOTTA, Items.PINK_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.GRAY_TERRACOTTA, Blocks.TERRACOTTA, Items.GRAY_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.TERRACOTTA, Items.LIGHT_GRAY_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.CYAN_TERRACOTTA, Blocks.TERRACOTTA, Items.CYAN_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.PURPLE_TERRACOTTA, Blocks.TERRACOTTA, Items.PURPLE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.BLUE_TERRACOTTA, Blocks.TERRACOTTA, Items.BLUE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.BROWN_TERRACOTTA, Blocks.TERRACOTTA, Items.BROWN_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.GREEN_TERRACOTTA, Blocks.TERRACOTTA, Items.GREEN_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.RED_TERRACOTTA, Blocks.TERRACOTTA, Items.RED_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.BLACK_TERRACOTTA, Blocks.TERRACOTTA, Items.BLACK_DYE, 0.12f);

        // --- CONCRETE POWDER (12% Drop Rate, Base: White) ---
        createCleaningRecipe(recipeOutput, Blocks.ORANGE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.ORANGE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.MAGENTA_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.LIGHT_BLUE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.YELLOW_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.YELLOW_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.LIME_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.LIME_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.PINK_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.PINK_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.GRAY_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.GRAY_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.LIGHT_GRAY_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.CYAN_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.CYAN_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.PURPLE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.PURPLE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.BLUE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.BLUE_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.BROWN_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.BROWN_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.GREEN_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.GREEN_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.RED_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.RED_DYE, 0.12f);
        createCleaningRecipe(recipeOutput, Blocks.BLACK_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.BLACK_DYE, 0.12f);

        // --- SHULKER BOXES (100% Drop Rate, Base: Shulker Box) ---
        createCleaningRecipe(recipeOutput, Blocks.WHITE_SHULKER_BOX, Blocks.SHULKER_BOX, Items.WHITE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.ORANGE_SHULKER_BOX, Blocks.SHULKER_BOX, Items.ORANGE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.MAGENTA_SHULKER_BOX, Blocks.SHULKER_BOX, Items.MAGENTA_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.SHULKER_BOX, Items.LIGHT_BLUE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.YELLOW_SHULKER_BOX, Blocks.SHULKER_BOX, Items.YELLOW_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIME_SHULKER_BOX, Blocks.SHULKER_BOX, Items.LIME_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.PINK_SHULKER_BOX, Blocks.SHULKER_BOX, Items.PINK_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.GRAY_SHULKER_BOX, Blocks.SHULKER_BOX, Items.GRAY_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.SHULKER_BOX, Items.LIGHT_GRAY_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.CYAN_SHULKER_BOX, Blocks.SHULKER_BOX, Items.CYAN_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.PURPLE_SHULKER_BOX, Blocks.SHULKER_BOX, Items.PURPLE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BLUE_SHULKER_BOX, Blocks.SHULKER_BOX, Items.BLUE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BROWN_SHULKER_BOX, Blocks.SHULKER_BOX, Items.BROWN_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.GREEN_SHULKER_BOX, Blocks.SHULKER_BOX, Items.GREEN_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.RED_SHULKER_BOX, Blocks.SHULKER_BOX, Items.RED_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BLACK_SHULKER_BOX, Blocks.SHULKER_BOX, Items.BLACK_DYE, 1.0f);

        // --- OTHER UNSPECIFIED: BEDS (100% Drop Rate, Base: White) ---
        createCleaningRecipe(recipeOutput, Blocks.ORANGE_BED, Blocks.WHITE_BED, Items.ORANGE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.MAGENTA_BED, Blocks.WHITE_BED, Items.MAGENTA_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_BLUE_BED, Blocks.WHITE_BED, Items.LIGHT_BLUE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.YELLOW_BED, Blocks.WHITE_BED, Items.YELLOW_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIME_BED, Blocks.WHITE_BED, Items.LIME_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.PINK_BED, Blocks.WHITE_BED, Items.PINK_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.GRAY_BED, Blocks.WHITE_BED, Items.GRAY_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_GRAY_BED, Blocks.WHITE_BED, Items.LIGHT_GRAY_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.CYAN_BED, Blocks.WHITE_BED, Items.CYAN_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.PURPLE_BED, Blocks.WHITE_BED, Items.PURPLE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BLUE_BED, Blocks.WHITE_BED, Items.BLUE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BROWN_BED, Blocks.WHITE_BED, Items.BROWN_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.GREEN_BED, Blocks.WHITE_BED, Items.GREEN_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.RED_BED, Blocks.WHITE_BED, Items.RED_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BLACK_BED, Blocks.WHITE_BED, Items.BLACK_DYE, 1.0f);

        // --- CANDLES (100% Drop Rate, Base: Uncolored Candle) ---
        createCleaningRecipe(recipeOutput, Blocks.WHITE_CANDLE, Blocks.CANDLE, Items.WHITE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.ORANGE_CANDLE, Blocks.CANDLE, Items.ORANGE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.MAGENTA_CANDLE, Blocks.CANDLE, Items.MAGENTA_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_BLUE_CANDLE, Blocks.CANDLE, Items.LIGHT_BLUE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.YELLOW_CANDLE, Blocks.CANDLE, Items.YELLOW_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIME_CANDLE, Blocks.CANDLE, Items.LIME_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.PINK_CANDLE, Blocks.CANDLE, Items.PINK_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.GRAY_CANDLE, Blocks.CANDLE, Items.GRAY_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_GRAY_CANDLE, Blocks.CANDLE, Items.LIGHT_GRAY_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.CYAN_CANDLE, Blocks.CANDLE, Items.CYAN_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.PURPLE_CANDLE, Blocks.CANDLE, Items.PURPLE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BLUE_CANDLE, Blocks.CANDLE, Items.BLUE_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BROWN_CANDLE, Blocks.CANDLE, Items.BROWN_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.GREEN_CANDLE, Blocks.CANDLE, Items.GREEN_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.RED_CANDLE, Blocks.CANDLE, Items.RED_DYE, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BLACK_CANDLE, Blocks.CANDLE, Items.BLACK_DYE, 1.0f);

        // --- FLOOR BANNERS (100% Drop Rate, 6 Dyes, Base: White) ---
        createCleaningRecipe(recipeOutput, Blocks.ORANGE_BANNER, Blocks.WHITE_BANNER, Items.ORANGE_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.MAGENTA_BANNER, Blocks.WHITE_BANNER, Items.MAGENTA_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_BLUE_BANNER, Blocks.WHITE_BANNER, Items.LIGHT_BLUE_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.YELLOW_BANNER, Blocks.WHITE_BANNER, Items.YELLOW_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIME_BANNER, Blocks.WHITE_BANNER, Items.LIME_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.PINK_BANNER, Blocks.WHITE_BANNER, Items.PINK_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.GRAY_BANNER, Blocks.WHITE_BANNER, Items.GRAY_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.LIGHT_GRAY_BANNER, Blocks.WHITE_BANNER, Items.LIGHT_GRAY_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.CYAN_BANNER, Blocks.WHITE_BANNER, Items.CYAN_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.PURPLE_BANNER, Blocks.WHITE_BANNER, Items.PURPLE_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BLUE_BANNER, Blocks.WHITE_BANNER, Items.BLUE_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BROWN_BANNER, Blocks.WHITE_BANNER, Items.BROWN_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.GREEN_BANNER, Blocks.WHITE_BANNER, Items.GREEN_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.RED_BANNER, Blocks.WHITE_BANNER, Items.RED_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, Blocks.BLACK_BANNER, Blocks.WHITE_BANNER, Items.BLACK_DYE, 6, 1.0f);

        // --- WALL BANNERS (100% Drop Rate, 6 Dyes, Base: White, Explicitly Named) ---
        createCleaningRecipe(recipeOutput, "orange_wall_banner_cleaning", Blocks.ORANGE_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.ORANGE_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "magenta_wall_banner_cleaning", Blocks.MAGENTA_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.MAGENTA_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "light_blue_wall_banner_cleaning", Blocks.LIGHT_BLUE_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.LIGHT_BLUE_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "yellow_wall_banner_cleaning", Blocks.YELLOW_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.YELLOW_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "lime_wall_banner_cleaning", Blocks.LIME_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.LIME_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "pink_wall_banner_cleaning", Blocks.PINK_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.PINK_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "gray_wall_banner_cleaning", Blocks.GRAY_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.GRAY_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "light_gray_wall_banner_cleaning", Blocks.LIGHT_GRAY_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.LIGHT_GRAY_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "cyan_wall_banner_cleaning", Blocks.CYAN_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.CYAN_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "purple_wall_banner_cleaning", Blocks.PURPLE_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.PURPLE_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "blue_wall_banner_cleaning", Blocks.BLUE_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.BLUE_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "brown_wall_banner_cleaning", Blocks.BROWN_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.BROWN_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "green_wall_banner_cleaning", Blocks.GREEN_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.GREEN_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "red_wall_banner_cleaning", Blocks.RED_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.RED_DYE, 6, 1.0f);
        createCleaningRecipe(recipeOutput, "black_wall_banner_cleaning", Blocks.BLACK_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.BLACK_DYE, 6, 1.0f);

        smoldering(recipeOutput,
                NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.CHARCOAL, Items.COAL), Ingredient.of(Items.CLAY_BALL)),
                new FluidStack(Fluids.WATER, 250),
                new ItemStack(AllBlockItem.BRIQUETTES.get()),
                FluidStack.EMPTY,
                100, 2, 1);

        smoldering(recipeOutput,
                NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.SCORCHED_COAL), Ingredient.of(Items.MAGMA_CREAM)),
                new FluidStack(Fluids.LAVA, 250),
                new ItemStack(AllBlockItem.SCORCHED_BRIQUETTES.get()),
                FluidStack.EMPTY,
                100, 2, 2);

        smoldering(recipeOutput,
                NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.FIRE_ASH.get()), Ingredient.of(Items.SLIME_BALL), Ingredient.of(Items.PINK_DYE)),
                new FluidStack(Fluids.WATER, 250),
                new ItemStack(AllBlockItem.SOAP.get()),
                FluidStack.EMPTY,
                400, 2, 1);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AllBlockItem.STURDY_SHEARS.get())
                .pattern(" I")
                .pattern("I ")
                .define('I', AllBlockItem.STURDY_BRICK.get())
                .unlockedBy("has_sturdy_brick", has(AllBlockItem.STURDY_BRICK.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, AllBlockItem.STURDY_ANVIL.get())
                .pattern("BBB")
                .pattern(" i ")
                .pattern("iii")
                .define('B', AllBlockItem.STURDY_BRICKS.get())
                .define('i', AllBlockItem.STURDY_BRICK.get())
                .unlockedBy("has_sturdy_bricks", has(AllBlockItem.STURDY_BRICKS.get()))
                .save(recipeOutput);

        registerInterchangeableBrickFamily(recipeOutput, "coal", AllBlockItem.BRIQUETTES, AllBlockItem.COAL_BRICKS.get(), AllBlockItem.COAL_BRICK_SLAB.get(), AllBlockItem.COAL_BRICK_STAIR.get(), AllBlockItem.COAL_BRICK_TILE.get(), AllBlockItem.COAL_BRICK_TILE_SLAB.get(), AllBlockItem.COAL_BRICK_TILE_STAIR.get(), AllBlockItem.COAL_BRICK_TILE_WALL.get());

        registerInterchangeableBrickFamily(recipeOutput, "gold", Items.GOLD_INGOT, AllBlockItem.GOLDEN_BRICKS.get(), AllBlockItem.GOLDEN_BRICK_SLAB.get(), AllBlockItem.GOLDEN_BRICK_STAIR.get(), AllBlockItem.GOLDEN_BRICK_TILE.get(), AllBlockItem.GOLDEN_BRICK_TILE_SLAB.get(), AllBlockItem.GOLDEN_BRICK_TILE_STAIR.get(), AllBlockItem.GOLDEN_BRICK_TILE_WALL.get());

        registerInterchangeableBrickFamily(recipeOutput, "netherite", Items.NETHERITE_INGOT, AllBlockItem.NETHERITE_BRICKS.get(), AllBlockItem.NETHERITE_BRICK_SLAB.get(), AllBlockItem.NETHERITE_BRICK_STAIR.get(), AllBlockItem.NETHERITE_BRICK_TILE.get(), AllBlockItem.NETHERITE_BRICK_TILE_SLAB.get(), AllBlockItem.NETHERITE_BRICK_TILE_STAIR.get(), AllBlockItem.NETHERITE_BRICK_TILE_WALL.get());

        registerInterchangeableBrickFamily(recipeOutput, "scorched", AllBlockItem.SCORCHED_BRIQUETTES.get(), AllBlockItem.SCORCHED_BRICKS.get(), AllBlockItem.SCORCHED_BRICK_SLAB.get(), AllBlockItem.SCORCHED_BRICK_STAIR.get(), AllBlockItem.SCORCHED_BRICK_TILE.get(), AllBlockItem.SCORCHED_BRICK_TILE_SLAB.get(), AllBlockItem.SCORCHED_BRICK_TILE_STAIR.get(), AllBlockItem.SCORCHED_BRICK_TILE_WALL.get());

        registerInterchangeableBrickFamily(recipeOutput, "sturdy", AllBlockItem.STURDY_BRICK, AllBlockItem.STURDY_BRICKS.get(), AllBlockItem.STURDY_BRICK_SLAB.get(), AllBlockItem.STURDY_BRICK_STAIR.get(), AllBlockItem.STURDY_BRICK_TILE.get(), AllBlockItem.STURDY_BRICK_TILE_SLAB.get(), AllBlockItem.STURDY_BRICK_TILE_STAIR.get(), AllBlockItem.STURDY_BRICK_TILE_WALL.get());

        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, AllBlockItem.BURNT_PLANKS.get(), 4)
                .requires(Alltags.Items.BURNT_LOG) // Assuming you have a tag for all burnt logs/woods
                .unlockedBy("has_burnt_log", has(AllBlockItem.BURNT_LOG.get()))
                .group("planks")
                .save(recipeOutput, "burnt_planks_from_logs");

        // Planks to Stairs
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, AllBlockItem.BURNT_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(recipeOutput);

        // Planks to Slabs
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, AllBlockItem.BURNT_SLAB.get(), 6)
                .pattern("###")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, AllBlockItem.BURNABLE_CAMPFIRE).define('L', ItemTags.LOGS).define('S', Items.STICK).define('C', ItemTags.COALS).pattern(" S ").pattern("SCS").pattern("LLL").unlockedBy("has_stick", has(Items.STICK)).unlockedBy("has_coal", has(ItemTags.COALS)).save(recipeOutput);
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, AllBlockItem.BURNABLE_SOUL_CAMPFIRE).define('L', ItemTags.LOGS).define('S', Items.STICK).define('#', AllBlockItem.SCORCHED_COAL.asItem()).pattern(" S ").pattern("S#S").pattern("LLL").unlockedBy("has_soul_sand", has(ItemTags.SOUL_FIRE_BASE_BLOCKS)).save(recipeOutput);

        // Planks to Fences
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, AllBlockItem.BURNT_FENCE.get(), 3)
                .pattern("#W#")
                .pattern("#W#")
                .define('#', Items.STICK)
                .define('W', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(recipeOutput);

        // Planks to Fence Gates
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AllBlockItem.BURNT_FENCE_GATE.get(), 1)
                .pattern("W#W")
                .pattern("W#W")
                .define('#', Items.STICK)
                .define('W', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(recipeOutput);

        // Planks to Trapdoors
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AllBlockItem.BURNT_TRAPDOOR.get(), 2)
                .pattern("###")
                .pattern("###")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(recipeOutput);

        // Planks to Buttons
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, AllBlockItem.BURNT_BUTTON.get(), 1)
                .requires(AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(recipeOutput);

        // Planks to Pressure Plates
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AllBlockItem.BURNT_PRESSURE_PLATE.get(), 1)
                .pattern("##")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(recipeOutput);

        // Planks to Doors
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AllBlockItem.BURNT_DOOR.get(), 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(recipeOutput);

        // Planks to Boat
        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, AllBlockItem.BURNT_BOAT.get(), 1)
                .pattern("# #")
                .pattern("###")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(recipeOutput);

        // Boat to Chest Boat
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, AllBlockItem.BURNT_CHEST_BOAT.get(), 1)
                .requires(AllBlockItem.BURNT_BOAT.get())
                .requires(Items.CHEST)
                .unlockedBy("has_burnt_boat", has(AllBlockItem.BURNT_BOAT.get()))
                .save(recipeOutput);

        // --- Copper Stove Recipes ---
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.COPPER_STOVE, AllBlockItem.STURDY_BRICK, Items.REDSTONE, Items.COPPER_INGOT);

        // Copper Stove Waxing (Shapeless)
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.WAXED_COPPER_STOVE.get())
                .requires(AllBlockItem.COPPER_STOVE.get())
                .requires(Items.HONEYCOMB)
                .unlockedBy("has_copper_stove", has(AllBlockItem.COPPER_STOVE.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.WAXED_EXPOSED_COPPER_STOVE.get())
                .requires(AllBlockItem.EXPOSED_COPPER_STOVE.get())
                .requires(Items.HONEYCOMB)
                .unlockedBy("has_exposed_copper_stove", has(AllBlockItem.EXPOSED_COPPER_STOVE.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.WAXED_WEATHERED_COPPER_STOVE.get())
                .requires(AllBlockItem.WEATHERED_COPPER_STOVE.get())
                .requires(Items.HONEYCOMB)
                .unlockedBy("has_weathered_copper_stove", has(AllBlockItem.WEATHERED_COPPER_STOVE.get()))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.WAXED_OXIDIZED_COPPER_STOVE.get())
                .requires(AllBlockItem.OXIDIZED_COPPER_STOVE.get())
                .requires(Items.HONEYCOMB)
                .unlockedBy("has_oxidized_copper_stove", has(AllBlockItem.OXIDIZED_COPPER_STOVE.get()))
                .save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHARCOAL, 1)
                .pattern("##")
                .pattern("##")
                .define('#', AllBlockItem.CHARCOAL_BIT.get())
                .unlockedBy("has_charcoal_bit", has(AllBlockItem.CHARCOAL_BIT.get()))
                .save(recipeOutput, "charcoal_from_bits");

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, AllBlockItem.ASHTRAY.get())
                .pattern("# #")
                .pattern("#$#")
                .pattern("###")
                .define('#', AllBlockItem.STURDY_BRICK.get())
                .define('$', Items.GOLD_NUGGET)
                .unlockedBy("has_sturdy_brick", has(AllBlockItem.STURDY_BRICK.get()))
                .save(recipeOutput);

        // --- Coal & Charcoal Bits Interchange ---
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COAL, 1)
                .pattern("##")
                .pattern("##")
                .define('#', AllBlockItem.COAL_BIT.get())
                .unlockedBy("has_coal_bit", has(AllBlockItem.COAL_BIT.get()))
                .save(recipeOutput, "coal_from_bits");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.COAL_BIT.get(), 4)
                .requires(Items.COAL)
                .unlockedBy("has_coal", has(Items.COAL))
                .save(recipeOutput, "coal_bits_from_coal");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.CHARCOAL_BIT.get(), 4)
                .requires(Items.CHARCOAL)
                .unlockedBy("has_charcoal", has(Items.CHARCOAL))
                .save(recipeOutput, "charcoal_bits_from_charcoal");

        for (AllBlockItem.BurntWoodSet set : AllBlockItem.BURNT_WOOD_SETS) {
            ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, set.wood().get(), 3)
                    .pattern("##")
                    .pattern("##")
                    .define('#', set.log().get())
                    .unlockedBy("has_" + set.prefix() + "_log", has(set.log().get()))
                    .save(recipeOutput);
        }
    }

    private static AllBlockItem.BurntWoodSet burntWoodSet(String material) {
        return AllBlockItem.BURNT_WOOD_SETS.stream()
                .filter(set -> set.material().equals(material))
                .findFirst()
                .orElseThrow();
    }

    private static List<VanillaWoodSet> vanillaWoodSets() {
        return List.of(
                new VanillaWoodSet("oak", Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG, Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD, Blocks.OAK_PLANKS, Blocks.OAK_STAIRS, Blocks.OAK_SLAB, Blocks.OAK_FENCE, Blocks.OAK_FENCE_GATE, Blocks.OAK_TRAPDOOR, Blocks.OAK_BUTTON, Blocks.OAK_PRESSURE_PLATE, Blocks.OAK_DOOR),
                new VanillaWoodSet("spruce", Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG, Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD, Blocks.SPRUCE_PLANKS, Blocks.SPRUCE_STAIRS, Blocks.SPRUCE_SLAB, Blocks.SPRUCE_FENCE, Blocks.SPRUCE_FENCE_GATE, Blocks.SPRUCE_TRAPDOOR, Blocks.SPRUCE_BUTTON, Blocks.SPRUCE_PRESSURE_PLATE, Blocks.SPRUCE_DOOR),
                new VanillaWoodSet("birch", Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG, Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD, Blocks.BIRCH_PLANKS, Blocks.BIRCH_STAIRS, Blocks.BIRCH_SLAB, Blocks.BIRCH_FENCE, Blocks.BIRCH_FENCE_GATE, Blocks.BIRCH_TRAPDOOR, Blocks.BIRCH_BUTTON, Blocks.BIRCH_PRESSURE_PLATE, Blocks.BIRCH_DOOR),
                new VanillaWoodSet("jungle", Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG, Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD, Blocks.JUNGLE_PLANKS, Blocks.JUNGLE_STAIRS, Blocks.JUNGLE_SLAB, Blocks.JUNGLE_FENCE, Blocks.JUNGLE_FENCE_GATE, Blocks.JUNGLE_TRAPDOOR, Blocks.JUNGLE_BUTTON, Blocks.JUNGLE_PRESSURE_PLATE, Blocks.JUNGLE_DOOR),
                new VanillaWoodSet("acacia", Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG, Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD, Blocks.ACACIA_PLANKS, Blocks.ACACIA_STAIRS, Blocks.ACACIA_SLAB, Blocks.ACACIA_FENCE, Blocks.ACACIA_FENCE_GATE, Blocks.ACACIA_TRAPDOOR, Blocks.ACACIA_BUTTON, Blocks.ACACIA_PRESSURE_PLATE, Blocks.ACACIA_DOOR),
                new VanillaWoodSet("dark_oak", Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG, Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD, Blocks.DARK_OAK_PLANKS, Blocks.DARK_OAK_STAIRS, Blocks.DARK_OAK_SLAB, Blocks.DARK_OAK_FENCE, Blocks.DARK_OAK_FENCE_GATE, Blocks.DARK_OAK_TRAPDOOR, Blocks.DARK_OAK_BUTTON, Blocks.DARK_OAK_PRESSURE_PLATE, Blocks.DARK_OAK_DOOR),
                new VanillaWoodSet("mangrove", Blocks.MANGROVE_LOG, Blocks.STRIPPED_MANGROVE_LOG, Blocks.MANGROVE_WOOD, Blocks.STRIPPED_MANGROVE_WOOD, Blocks.MANGROVE_PLANKS, Blocks.MANGROVE_STAIRS, Blocks.MANGROVE_SLAB, Blocks.MANGROVE_FENCE, Blocks.MANGROVE_FENCE_GATE, Blocks.MANGROVE_TRAPDOOR, Blocks.MANGROVE_BUTTON, Blocks.MANGROVE_PRESSURE_PLATE, Blocks.MANGROVE_DOOR),
                new VanillaWoodSet("cherry", Blocks.CHERRY_LOG, Blocks.STRIPPED_CHERRY_LOG, Blocks.CHERRY_WOOD, Blocks.STRIPPED_CHERRY_WOOD, Blocks.CHERRY_PLANKS, Blocks.CHERRY_STAIRS, Blocks.CHERRY_SLAB, Blocks.CHERRY_FENCE, Blocks.CHERRY_FENCE_GATE, Blocks.CHERRY_TRAPDOOR, Blocks.CHERRY_BUTTON, Blocks.CHERRY_PRESSURE_PLATE, Blocks.CHERRY_DOOR)
        );
    }

    private record VanillaWoodSet(String material, Block log, Block strippedLog, Block wood, Block strippedWood,
                                  Block planks, Block stairs, Block slab, Block fence, Block fenceGate,
                                  Block trapdoor, Block button, Block pressurePlate, Block door) {
    }
}
