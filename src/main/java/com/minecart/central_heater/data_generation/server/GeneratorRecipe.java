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
        brickTileRecipe(recipeOutput, AllBlockItem.STURDY_BRICK_TILE.asItem(), AllBlockItem.STURDY_BRICK.asItem());
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
        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.STURDY_BRICK_TILE.asItem(), AllBlockItem.STURDY_BRICK_TILE_STAIR.asItem(),
                AllBlockItem.STURDY_BRICK_TILE_SLAB.asItem(), AllBlockItem.STURDY_BRICK_TILE_WALL.asItem());

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
                .pattern("N N")
                .pattern("BBB")
                .define('N', Items.NETHER_BRICK)
                .define('B', AllBlockItem.BLACKSTONE_BRICK_TILE)
                .unlockedBy("has_netherite", has(AllBlockItem.BLACKSTONE_BRICK_TILE))
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
                new ItemStack(AllBlockItem.WHEAT_DOUGH.asItem()), FluidStack.EMPTY, 200, 1, 0);

        oreCampfiring(recipeOutput, List.of(AllBlockItem.WHEAT_DOUGH), RecipeCategory.FOOD, Items.BREAD, 0.35f, 600, "bread");

        smoldering(recipeOutput, Ingredient.of(AllBlockItem.SCORCHED_DUST), new FluidStack(Fluids.LAVA, 125),
                new ItemStack(AllBlockItem.SOUL_MIXTURE.asItem()), FluidStack.EMPTY, 200, 2, 1);

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

        smoldering(recipeOutput, Ingredient.of(Items.ICE), FluidStack.EMPTY, NonNullList.of(ItemStack.EMPTY), new FluidStack(Fluids.WATER, 1000),200, 1,1);
        smoldering(recipeOutput, Ingredient.of(Items.MAGMA_CREAM), FluidStack.EMPTY, new ItemStack(Items.SLIME_BALL), new FluidStack(Fluids.LAVA, 250),400, 3,2);
        smoldering(recipeOutput, Ingredient.of(Items.CHARCOAL), new FluidStack(Fluids.LAVA, 250), new ItemStack(Items.COAL), FluidStack.EMPTY,400, 2,2);
        smoldering(recipeOutput, Ingredient.of(Items.BONE), FluidStack.EMPTY, new ItemStack(Items.BONE_MEAL, 4), FluidStack.EMPTY,400, 1,1);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.EGG), Ingredient.of(Items.CLAY_BALL), Ingredient.of(AllBlockItem.WHEAT_DOUGH), Ingredient.of(Items.LIME_DYE)), new FluidStack(Fluids.WATER, 250), new ItemStack(Items.SLIME_BALL, 2), FluidStack.EMPTY,400, 1,1);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.DEEPSLATE_COBBLE), Ingredient.of(Items.GOLD_NUGGET)), new FluidStack(Fluids.LAVA, 125), new ItemStack(AllBlockItem.DEEPSLATE_BRICK.asItem()), FluidStack.EMPTY,400, 2,1);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.COBBLE), Ingredient.of(Items.IRON_NUGGET)), new FluidStack(Fluids.LAVA, 125), new ItemStack(AllBlockItem.STONE_BRICK.asItem()), FluidStack.EMPTY,400, 2,1);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.COBBLE), Ingredient.of(Items.IRON_INGOT), Ingredient.of(Items.KELP)), new FluidStack(Fluids.LAVA, 125), new ItemStack(AllBlockItem.STURDY_BRICK.asItem()), FluidStack.EMPTY,1000, 3,1);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.FIRE_ASH.asItem()), Ingredient.of(Items.SUGAR), Ingredient.of(Items.CHARCOAL)), FluidStack.EMPTY, NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.GUNPOWDER)), FluidStack.EMPTY, 400, 2, 1);

        blockSmoldering(recipeOutput, Blocks.ACACIA_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.BIRCH_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.CHERRY_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.OAK_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.JUNGLE_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.DARK_OAK_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.MANGROVE_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.SPRUCE_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);

        blockSmoldering(recipeOutput, Blocks.ACACIA_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.BIRCH_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.CHERRY_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.OAK_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.JUNGLE_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.DARK_OAK_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.MANGROVE_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(recipeOutput, Blocks.SPRUCE_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);

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
                new ItemStack(Items.WHITE_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.ORANGE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.ORANGE_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.MAGENTA_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.MAGENTA_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.LIGHT_BLUE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.LIGHT_BLUE_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.YELLOW_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.YELLOW_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.LIME_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.LIME_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.PINK_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.PINK_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.GRAY_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.GRAY_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.LIGHT_GRAY_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.LIGHT_GRAY_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.CYAN_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.CYAN_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.PURPLE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.PURPLE_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.BLUE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.BLUE_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.BROWN_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.BROWN_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.GREEN_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.GREEN_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.RED_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.RED_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(recipeOutput, Ingredient.of(Items.BLACK_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.BLACK_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
    }
}
