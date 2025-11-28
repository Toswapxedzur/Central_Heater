package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.AllRecipe;
import com.minecart.central_heater.recipe.SeethingRecipe;
import com.minecart.central_heater.recipe.SmolderingRecipeBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
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

public class Recipe extends ModRecipeProvider implements IConditionBuilder {
    public Recipe(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        //remove recipe from stonecutter
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_stairs_from_cobbled_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:nether_brick_slab_from_nether_bricks_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_stairs_from_polished_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_slab_from_polished_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_slab_from_cobbled_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:stone_bricks_from_stone_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_bricks_from_cobbled_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_wall_from_cobbled_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:stone_brick_slab_from_stone_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:stone_brick_stairs_from_stone_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_bricks_from_polished_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:deepslate_brick_wall_from_polished_deepslate_stonecutting");
        emptyRecipe(recipeOutput, "minecraft:stone_brick_walls_from_stone_stonecutting");

        //add recipe from stone cutter
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_STONE_BRICKS, AllBlockItem.stone_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILES, AllBlockItem.deepslate_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_STAIRS, AllBlockItem.deepslate_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_SLAB, AllBlockItem.deepslate_brick_tile, 2);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_WALL, AllBlockItem.deepslate_brick_tile);

        //deal with tile bricks recipes in crafting
        tileBrickRecipe(recipeOutput, AllBlockItem.stone_brick_tile.asItem(), AllBlockItem.stone_brick.asItem());
        tileBrickRecipe(recipeOutput, AllBlockItem.deepslate_brick_tile.asItem(), AllBlockItem.deepslate_brick.asItem());
        tileBrickRecipe(recipeOutput, AllBlockItem.mud_brick_tile.asItem(), AllBlockItem.mud_brick.asItem());
        tileBrickRecipe(recipeOutput, Items.NETHER_BRICKS, Items.NETHER_BRICK);
        tileBrickRecipe(recipeOutput, Items.RED_NETHER_BRICKS, AllBlockItem.red_nether_brick.asItem());
        tileBrickRecipe(recipeOutput, Items.BRICKS, Items.BRICK);

        //deal with bricks recipe in crafting
        brickRecipe(recipeOutput, Items.STONE_BRICKS, AllBlockItem.stone_brick.asItem());
        brickRecipe(recipeOutput, Items.DEEPSLATE_BRICKS, AllBlockItem.deepslate_brick.asItem());
        brickRecipe(recipeOutput, Items.MUD_BRICKS, AllBlockItem.mud_brick.asItem());

        //other special blocks
        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.stone_brick_tile.asItem(), AllBlockItem.stone_brick_tile_stair.asItem(),
                AllBlockItem.stone_brick_tile_slab.asItem(), AllBlockItem.stone_brick_tile_wall.asItem());
        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.deepslate_brick_tile.asItem(), AllBlockItem.deepslate_brick_tile_stair.asItem(),
                AllBlockItem.deepslate_brick_tile_slab.asItem(), AllBlockItem.deepslate_brick_tile_wall.asItem());
        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.mud_brick_tile.asItem(), AllBlockItem.mud_brick_tile_stair.asItem(),
                AllBlockItem.mud_brick_tile_slab.asItem(), AllBlockItem.mud_brick_tile_wall.asItem());

        //other things
        oreBlasting(recipeOutput, List.of(Items.PACKED_MUD), RecipeCategory.MISC, AllBlockItem.mud_brick, 0.1f, 200, "brick");

        oreSeething(recipeOutput, List.of(Items.COAL_BLOCK), RecipeCategory.MISC, AllBlockItem.diamond_shard, 1f, 1000, "misc");
        oreSeething(recipeOutput, List.of(Items.SAND), RecipeCategory.MISC, Items.SOUL_SAND, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.DIRT), RecipeCategory.MISC, Items.SOUL_SOIL, 1f, 300, "misc");
        oreSeething(recipeOutput, List.of(Items.INK_SAC), RecipeCategory.MISC, Items.GLOW_INK_SAC, 1f, 1000, "misc");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.diamond_shard, 4).requires(Items.DIAMOND)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.DIAMOND).requires(AllBlockItem.diamond_shard, 4)
                .unlockedBy(getHasName(AllBlockItem.diamond_shard), has(AllBlockItem.diamond_shard)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.stone_brick)
                .requires(AllBlockItem.cobble).requires(Items.CLAY_BALL).requires(Items.FLINT).requires(Items.IRON_NUGGET)
                .unlockedBy(getHasName(AllBlockItem.cobble), has(AllBlockItem.cobble)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.deepslate_brick)
                .requires(AllBlockItem.deepslate_cobble).requires(Items.CLAY_BALL).requires(Items.FLINT).requires(Items.GOLD_NUGGET)
                .unlockedBy(getHasName(AllBlockItem.deepslate_cobble), has(AllBlockItem.deepslate_cobble)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.red_nether_brick).requires(Items.NETHER_BRICK).requires(Ingredient.of(Items.NETHER_WART, Items.RED_DYE))
                .unlockedBy(getHasName(Items.NETHER_BRICK), has(Items.NETHER_BRICK)).group("misc").save(recipeOutput);

        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.brick_stove, Items.BRICK, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.mud_brick_stove, AllBlockItem.mud_brick, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.stone_stove, AllBlockItem.stone_brick, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.deepslate_stove, AllBlockItem.deepslate_brick, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.nether_brick_stove, Items.NETHER_BRICK, Items.GOLD_INGOT, AllBlockItem.gold_bars);
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.red_nether_brick_stove, AllBlockItem.red_nether_brick, Items.GOLD_INGOT, AllBlockItem.gold_bars);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.gold_bars, 16).pattern("###").pattern("###").define('#', Items.GOLD_INGOT)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.cobble, 4).requires(Items.COBBLESTONE)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(Items.COBBLESTONE)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COBBLESTONE).requires(AllBlockItem.cobble, 4)
                .unlockedBy(getHasName(AllBlockItem.cobble), has(AllBlockItem.cobble)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.deepslate_cobble, 4).requires(Items.COBBLED_DEEPSLATE)
                .unlockedBy(getHasName(Items.COBBLED_DEEPSLATE), has(Items.COBBLED_DEEPSLATE)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COBBLED_DEEPSLATE).requires(AllBlockItem.deepslate_cobble, 4)
                .unlockedBy(getHasName(AllBlockItem.deepslate_cobble), has(AllBlockItem.deepslate_cobble)).group("misc").save(recipeOutput);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.ICE)), FluidStack.EMPTY, ItemStack.EMPTY, new FluidStack(Fluids.WATER, 750),200, 1,1);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.MAGMA_CREAM)), new FluidStack(Fluids.LAVA, 250), ItemStack.EMPTY, new FluidStack(Fluids.LAVA, 500),500, 3,2);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.CHARCOAL)), new FluidStack(Fluids.LAVA, 200), new ItemStack(Items.COAL), new FluidStack(Fluids.LAVA, 200),400, 2,2);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.BONE)), FluidStack.EMPTY, new ItemStack(Items.BONE_MEAL, 4), FluidStack.EMPTY,800, 1,1);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.NETHERITE_SCRAP),
                Ingredient.of(Items.NETHERITE_SCRAP), Ingredient.of(Items.NETHERITE_SCRAP)), new FluidStack(Fluids.LAVA, 500), new ItemStack(Items.NETHERITE_INGOT), FluidStack.EMPTY,2400, 3,2);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.OBSIDIAN), Ingredient.of(AllBlockItem.diamond_shard)), new FluidStack(Fluids.WATER, 250), new ItemStack(Items.CRYING_OBSIDIAN), FluidStack.EMPTY,2000, 3,0);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.EGG), Ingredient.of(Items.CLAY_BALL), Ingredient.of(Items.MILK_BUCKET)), FluidStack.EMPTY, new ItemStack(Items.BUCKET), FluidStack.EMPTY,400, 1,1);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.deepslate_cobble), Ingredient.of(Items.GOLD_NUGGET)), new FluidStack(Fluids.LAVA, 300), new ItemStack(AllBlockItem.deepslate_brick.asItem()), new FluidStack(Fluids.LAVA, 250),200, 2,2);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.cobble), Ingredient.of(Items.IRON_NUGGET)), new FluidStack(Fluids.LAVA, 150), new ItemStack(AllBlockItem.stone_brick.asItem()), new FluidStack(Fluids.LAVA, 100),150, 1,2);
    }

    protected static void tileBrickRecipe(RecipeOutput output, Item result, Item ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 2).pattern("###").pattern("###").pattern("###")
                .define('#', Ingredient.of(ingredient)).group("building").unlockedBy(getHasName(ingredient), has(ingredient))
                .showNotification(true).save(output);
    }

    protected static void brickRecipe(RecipeOutput output, Item result, Item ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result).pattern("##").pattern("##")
                .define('#', Ingredient.of(ingredient)).group("building").unlockedBy(getHasName(ingredient), has(ingredient))
                .showNotification(true).save(output);
    }

    protected static void stairSlabWallCraftingStoneCuttingRecipe(RecipeOutput output, Item ingredient, Item stair, Item slab, Item wall){
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, stair, ingredient);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, slab, ingredient, 2);
        stonecutterResultFromBase(output, RecipeCategory.BUILDING_BLOCKS, wall, ingredient);
        stairBuilder(stair, Ingredient.of(ingredient)).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab, Ingredient.of(ingredient)).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
        wallBuilder(RecipeCategory.BUILDING_BLOCKS, wall, Ingredient.of(ingredient)).unlockedBy(getHasName(ingredient), has(ingredient)).save(output);
    }

    protected static void stoveCraftingRecipeBuilder(RecipeOutput output, ItemLike stove, ItemLike baseBrick, ItemLike ingot, ItemLike bars){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, stove).pattern("*&*").pattern("# #").pattern("###")
                .define('#', baseBrick).define('*', ingot).define('&', bars).unlockedBy(getHasName(stove), has(stove)).group("misc").save(output);
    }
}
