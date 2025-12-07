package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.AllRecipe;
import com.minecart.central_heater.recipe.FireBrewingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
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
        brickTileRecipe(recipeOutput, AllBlockItem.stone_brick_tile.asItem(), AllBlockItem.stone_brick.asItem());
        brickTileRecipe(recipeOutput, AllBlockItem.deepslate_brick_tile.asItem(), AllBlockItem.deepslate_brick.asItem());
        brickTileRecipe(recipeOutput, AllBlockItem.mud_brick_tile.asItem(), AllBlockItem.mud_brick.asItem());
        brickTileRecipe(recipeOutput, AllBlockItem.sturdy_brick_tile.asItem(), AllBlockItem.sturdy_brick.asItem());
        brickTileRecipe(recipeOutput, Items.NETHER_BRICKS, Items.NETHER_BRICK);
        brickTileRecipe(recipeOutput, Items.RED_NETHER_BRICKS, AllBlockItem.red_nether_brick.asItem());
        brickTileRecipe(recipeOutput, Items.BRICKS, Items.BRICK);
        brickTileRecipe(recipeOutput, AllBlockItem.blackstone_brick_tile.asItem(), AllBlockItem.blackstone_brick.asItem());

        //deal with bricks recipe in crafting
        brickRecipe(recipeOutput, Items.STONE_BRICKS, AllBlockItem.stone_brick.asItem());
        brickRecipe(recipeOutput, Items.DEEPSLATE_BRICKS, AllBlockItem.deepslate_brick.asItem());
        brickRecipe(recipeOutput, Items.MUD_BRICKS, AllBlockItem.mud_brick.asItem());
        brickRecipe(recipeOutput, Items.POLISHED_BLACKSTONE_BRICKS, AllBlockItem.blackstone_brick.asItem());

        //other special blocks
        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.stone_brick_tile.asItem(), AllBlockItem.stone_brick_tile_stair.asItem(),
                AllBlockItem.stone_brick_tile_slab.asItem(), AllBlockItem.stone_brick_tile_wall.asItem());
        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.deepslate_brick_tile.asItem(), AllBlockItem.deepslate_brick_tile_stair.asItem(),
                AllBlockItem.deepslate_brick_tile_slab.asItem(), AllBlockItem.deepslate_brick_tile_wall.asItem());
        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.mud_brick_tile.asItem(), AllBlockItem.mud_brick_tile_stair.asItem(),
                AllBlockItem.mud_brick_tile_slab.asItem(), AllBlockItem.mud_brick_tile_wall.asItem());
        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.blackstone_brick_tile.asItem(), AllBlockItem.blackstone_brick_tile_stair.asItem(),
                AllBlockItem.blackstone_brick_tile_slab.asItem(), AllBlockItem.blackstone_brick_tile_wall.asItem());
        stairSlabWallCraftingStoneCuttingRecipe(recipeOutput, AllBlockItem.sturdy_brick_tile.asItem(), AllBlockItem.sturdy_brick_tile_stair.asItem(),
                AllBlockItem.sturdy_brick_tile_slab.asItem(), AllBlockItem.sturdy_brick_tile_wall.asItem());

        //other things
        oreBlasting(recipeOutput, List.of(Items.PACKED_MUD), RecipeCategory.MISC, AllBlockItem.mud_brick, 0.1f, 200, "brick");
        oreBlasting(recipeOutput, List.of(Items.POLISHED_BLACKSTONE), RecipeCategory.MISC, AllBlockItem.blackstone_brick, 0.1f, 200, "brick");

        oreSeething(recipeOutput, List.of(Items.COAL_BLOCK), RecipeCategory.MISC, AllBlockItem.diamond_shard, 1f, 1000, "misc");
        oreSeething(recipeOutput, List.of(Items.SAND), RecipeCategory.MISC, Items.SOUL_SAND, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.DIRT), RecipeCategory.MISC, Items.SOUL_SOIL, 1f, 300, "misc");
        oreSeething(recipeOutput, List.of(Items.INK_SAC), RecipeCategory.MISC, Items.GLOW_INK_SAC, 1f, 1000, "misc");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.diamond_shard, 4).requires(Items.DIAMOND)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.DIAMOND).requires(AllBlockItem.diamond_shard, 4)
                .unlockedBy(getHasName(AllBlockItem.diamond_shard), has(AllBlockItem.diamond_shard)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.sturdy_nugget, 9).requires(AllBlockItem.sturdy_brick)
                .unlockedBy(getHasName(AllBlockItem.sturdy_brick), has(AllBlockItem.sturdy_brick)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.sturdy_brick).requires(AllBlockItem.sturdy_nugget, 9)
                .unlockedBy(getHasName(AllBlockItem.sturdy_nugget), has(AllBlockItem.sturdy_nugget)).group("misc").save(recipeOutput);

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
        stoveCraftingRecipeBuilder(recipeOutput, AllBlockItem.blackstone_stove, AllBlockItem.blackstone_brick, Items.GOLD_INGOT, AllBlockItem.gold_bars);

        potRecipe(recipeOutput, AllBlockItem.mud_brick_pot, AllBlockItem.mud_brick);
        potRecipe(recipeOutput, AllBlockItem.stone_pot, AllBlockItem.stone_brick);
        potRecipe(recipeOutput, AllBlockItem.deepslate_pot, AllBlockItem.deepslate_brick);

        burntRecipe(recipeOutput, Items.BEEF, Items.COOKED_BEEF, AllBlockItem.burnt_beef);
        burntRecipe(recipeOutput, Items.CHICKEN, Items.COOKED_CHICKEN, AllBlockItem.burnt_chicken);
        burntRecipe(recipeOutput, Items.COD, Items.COOKED_COD, AllBlockItem.burnt_cod);
        burntRecipe(recipeOutput, Items.MUTTON, Items.COOKED_MUTTON, AllBlockItem.burnt_mutton);
        burntRecipe(recipeOutput, Items.PORKCHOP, Items.COOKED_PORKCHOP, AllBlockItem.burnt_porkchop);
        burntRecipe(recipeOutput, Items.POTATO, Items.BAKED_POTATO, AllBlockItem.burnt_potato);
        burntRecipe(recipeOutput, Items.RABBIT, Items.COOKED_RABBIT, AllBlockItem.burnt_rabbit);
        burntRecipe(recipeOutput, Items.SALMON, Items.COOKED_SALMON, AllBlockItem.burnt_salmon);

        toolsBundle(recipeOutput, AllBlockItem.sturdy_brick, AllBlockItem.sturdy_pickaxe, AllBlockItem.sturdy_axe, AllBlockItem.sturdy_shovel, AllBlockItem.sturdy_hoe, AllBlockItem.sturdy_sword);
        armorsBundle(recipeOutput, AllBlockItem.sturdy_brick, AllBlockItem.sturdy_helmet, AllBlockItem.sturdy_chestplate, AllBlockItem.sturdy_leggings, AllBlockItem.sturdy_boots);

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

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.ICE)), FluidStack.EMPTY, NonNullList.of(ItemStack.EMPTY), new FluidStack(Fluids.WATER, 750),200, 1,1);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.MAGMA_CREAM)), new FluidStack(Fluids.LAVA, 250), NonNullList.of(ItemStack.EMPTY), new FluidStack(Fluids.LAVA, 500),500, 3,2);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.CHARCOAL)), new FluidStack(Fluids.LAVA, 200), NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.COAL)), new FluidStack(Fluids.LAVA, 200),400, 2,2);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.BONE)), FluidStack.EMPTY, NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.BONE_MEAL, 4)), FluidStack.EMPTY,800, 1,1);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.NETHERITE_SCRAP),
                Ingredient.of(Items.NETHERITE_SCRAP)), new FluidStack(Fluids.LAVA, 500), NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.NETHERITE_INGOT)), FluidStack.EMPTY,2400, 3,2);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.OBSIDIAN), Ingredient.of(AllBlockItem.diamond_shard)), new FluidStack(Fluids.WATER, 250), NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.CRYING_OBSIDIAN)), FluidStack.EMPTY,2000, 3,0);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.EGG), Ingredient.of(Items.CLAY_BALL), Ingredient.of(Items.MILK_BUCKET)), FluidStack.EMPTY, NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.SLIME_BALL), new ItemStack(Items.BUCKET)), FluidStack.EMPTY,400, 1,1);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.deepslate_cobble), Ingredient.of(Items.GOLD_NUGGET)), new FluidStack(Fluids.LAVA, 300), NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.deepslate_brick.asItem())), new FluidStack(Fluids.LAVA, 250),200, 2,2);

        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.cobble), Ingredient.of(Items.IRON_NUGGET)), new FluidStack(Fluids.LAVA, 150), NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.stone_brick.asItem())), new FluidStack(Fluids.LAVA, 100),150, 1,2);

        SpecialRecipeBuilder.special(category -> new FireBrewingRecipe()).save(recipeOutput, "cauldron_potion_brewing");
    }
}
