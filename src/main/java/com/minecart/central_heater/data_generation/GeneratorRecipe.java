package com.minecart.central_heater.data_generation;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.recipe.FireBrewingRecipe;
import com.minecart.central_heater.util.Alltags;
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


        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CLAY_BALL)
                .requires(AllBlockItem.clay_bit, 4)
                .unlockedBy(getHasName(AllBlockItem.clay_bit), has(AllBlockItem.clay_bit))
                .group("clay_processing")
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.clay_bit, 4)
                .requires(Items.CLAY_BALL)
                .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .group("clay_processing")
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLACK_DYE)
                .requires(Alltags.Items.OVERBURNT)
                .unlockedBy("has_burnt_food", has(Alltags.Items.OVERBURNT))
                .save(recipeOutput, "black_dye_from_burnt_food");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.clay_brick)
                .pattern("##")
                .define('#', Items.CLAY_BALL)
                .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .group("clay_processing")
                .save(recipeOutput);

        oreSmelting(recipeOutput, List.of(AllBlockItem.clay_brick), RecipeCategory.MISC, Items.CLAY_BALL, 0.1f, 100, "clay_processing");

        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(ItemTags.PLANKS), 4, "planks");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(ItemTags.WOODEN_STAIRS), 3, "stairs");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(ItemTags.WOODEN_SLABS), 2, "slabs");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(ItemTags.WOODEN_PRESSURE_PLATES), 1, "pressure_plates");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(ItemTags.WOODEN_BUTTONS), 1, "buttons");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(ItemTags.WOODEN_DOORS), 8, "doors");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(ItemTags.WOODEN_TRAPDOORS), 6, "trapdoors");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(ItemTags.BOATS), 12, "boats");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(ItemTags.WOODEN_FENCES), 2, "fences");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(ItemTags.FENCE_GATES), 3, "fence_gates");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(ItemTags.SIGNS), 1, "signs");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(Items.STICK), 2, "sticks");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(ItemTags.LOGS), 16, "logs");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(Items.WOODEN_SHOVEL), 3, "wooden_shovel");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(Items.WOODEN_SWORD), 5, "wooden_sword");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(Items.WOODEN_HOE), 6, "wooden_hoe");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(Items.WOODEN_PICKAXE), 8, "wooden_pickaxe");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(Items.WOODEN_AXE), 8, "wooden_axe");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(Items.BOWL), 2, "bowls");
        stonecutterTag(recipeOutput, RecipeCategory.MISC, AllBlockItem.wood_chips, Ingredient.of(Items.LADDER), 4, "ladders");

        stonecutterResultFromBase(recipeOutput, RecipeCategory.FOOD, AllBlockItem.wheat_flour, Items.WHEAT);

        stonecutterResultFromBase(recipeOutput, RecipeCategory.MISC, AllBlockItem.scorched_dust, Items.SOUL_SOIL);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.MISC, AllBlockItem.scorched_dust, Items.SOUL_SAND);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.diamond_shard, 4).requires(Items.DIAMOND)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.DIAMOND).requires(AllBlockItem.diamond_shard, 4)
                .unlockedBy(getHasName(AllBlockItem.diamond_shard), has(AllBlockItem.diamond_shard)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.sturdy_nugget, 9).requires(AllBlockItem.sturdy_brick)
                .unlockedBy(getHasName(AllBlockItem.sturdy_brick), has(AllBlockItem.sturdy_brick)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.sturdy_brick).requires(AllBlockItem.sturdy_nugget, 9)
                .unlockedBy(getHasName(AllBlockItem.sturdy_nugget), has(AllBlockItem.sturdy_nugget)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.cobble, 4).requires(Items.COBBLESTONE)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(Items.COBBLESTONE)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COBBLESTONE).requires(AllBlockItem.cobble, 4)
                .unlockedBy(getHasName(AllBlockItem.cobble), has(AllBlockItem.cobble)).group("misc").save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.deepslate_cobble, 4).requires(Items.COBBLED_DEEPSLATE)
                .unlockedBy(getHasName(Items.COBBLED_DEEPSLATE), has(Items.COBBLED_DEEPSLATE)).group("misc").save(recipeOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COBBLED_DEEPSLATE).requires(AllBlockItem.deepslate_cobble, 4)
                .unlockedBy(getHasName(AllBlockItem.deepslate_cobble), has(AllBlockItem.deepslate_cobble)).group("misc").save(recipeOutput);


        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_STONE_BRICKS, AllBlockItem.stone_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILES, AllBlockItem.deepslate_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_STAIRS, AllBlockItem.deepslate_brick_tile);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_SLAB, AllBlockItem.deepslate_brick_tile, 2);
        stonecutterResultFromBase(recipeOutput, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_WALL, AllBlockItem.deepslate_brick_tile);

        brickTileRecipe(recipeOutput, AllBlockItem.stone_brick_tile.asItem(), AllBlockItem.stone_brick.asItem());
        brickTileRecipe(recipeOutput, AllBlockItem.deepslate_brick_tile.asItem(), AllBlockItem.deepslate_brick.asItem());
        brickTileRecipe(recipeOutput, AllBlockItem.mud_brick_tile.asItem(), AllBlockItem.mud_brick.asItem());
        brickTileRecipe(recipeOutput, AllBlockItem.sturdy_brick_tile.asItem(), AllBlockItem.sturdy_brick.asItem());
        brickTileRecipe(recipeOutput, Items.NETHER_BRICKS, Items.NETHER_BRICK);
        brickTileRecipe(recipeOutput, Items.RED_NETHER_BRICKS, AllBlockItem.red_nether_brick.asItem());
        brickTileRecipe(recipeOutput, Items.BRICKS, Items.BRICK);
        brickTileRecipe(recipeOutput, AllBlockItem.blackstone_brick_tile.asItem(), AllBlockItem.blackstone_brick.asItem());

        brickRecipe(recipeOutput, Items.STONE_BRICKS, AllBlockItem.stone_brick.asItem());
        brickRecipe(recipeOutput, Items.DEEPSLATE_BRICKS, AllBlockItem.deepslate_brick.asItem());
        brickRecipe(recipeOutput, Items.MUD_BRICKS, AllBlockItem.mud_brick.asItem());
        brickRecipe(recipeOutput, Items.POLISHED_BLACKSTONE_BRICKS, AllBlockItem.blackstone_brick.asItem());

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

        oreBlasting(recipeOutput, List.of(Items.PACKED_MUD), RecipeCategory.MISC, AllBlockItem.mud_brick, 0.1f, 200, "brick");
        oreBlasting(recipeOutput, List.of(Items.POLISHED_BLACKSTONE), RecipeCategory.MISC, AllBlockItem.blackstone_brick, 0.1f, 200, "brick");

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

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.sturdy_tank_item)
                .pattern("# #").pattern(" # ").define('#', AllBlockItem.sturdy_brick)
                .unlockedBy(getHasName(AllBlockItem.sturdy_brick), has(AllBlockItem.sturdy_brick))
                .group("misc").save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.clay_cauldron)
                .pattern("# #").pattern("# #").pattern("###")
                .define('#', AllBlockItem.clay_brick)
                .unlockedBy(getHasName(AllBlockItem.clay_brick), has(AllBlockItem.clay_brick))
                .save(recipeOutput);

        oreSmelting(recipeOutput, List.of(AllBlockItem.clay_cauldron), RecipeCategory.MISC, AllBlockItem.brick_cauldron, 0.3f, 200, "cauldron");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.iron_cauldron).pattern("# #").pattern("# #").pattern("###")
                .define('#', Items.IRON_INGOT).unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT)).save(recipeOutput, ResourceLocation.withDefaultNamespace("cauldron"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.golden_cauldron).pattern("# #").pattern("# #").pattern("###")
                .define('#', Items.GOLD_INGOT).unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.gold_bars, 16).pattern("###").pattern("###").define('#', Items.GOLD_INGOT)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT)).group("misc").save(recipeOutput);


        List<ItemLike> sturdyGear = List.of(
                AllBlockItem.sturdy_pickaxe, AllBlockItem.sturdy_axe, AllBlockItem.sturdy_shovel, AllBlockItem.sturdy_hoe, AllBlockItem.sturdy_sword,
                AllBlockItem.sturdy_helmet, AllBlockItem.sturdy_chestplate, AllBlockItem.sturdy_leggings, AllBlockItem.sturdy_boots
        );

        toolsBundle(recipeOutput, AllBlockItem.sturdy_brick, AllBlockItem.sturdy_pickaxe, AllBlockItem.sturdy_axe, AllBlockItem.sturdy_shovel, AllBlockItem.sturdy_hoe, AllBlockItem.sturdy_sword);
        armorsBundle(recipeOutput, AllBlockItem.sturdy_brick, AllBlockItem.sturdy_helmet, AllBlockItem.sturdy_chestplate, AllBlockItem.sturdy_leggings, AllBlockItem.sturdy_boots);

        oreSmelting(recipeOutput, sturdyGear, RecipeCategory.MISC, AllBlockItem.sturdy_nugget, 0.1f, 200, "sturdy_nugget");
        oreBlasting(recipeOutput, sturdyGear, RecipeCategory.MISC, AllBlockItem.sturdy_nugget, 0.1f, 100, "sturdy_nugget");


        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.SOUL_TORCH, 4).pattern("C").pattern("S")
                .define('C', AllBlockItem.scorched_coal.asItem()).define('S', Items.STICK)
                .unlockedBy("has_scorched_coal", has(AllBlockItem.scorched_coal.asItem())).save(recipeOutput, ResourceLocation.parse("minecraft:soul_torch"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.SOUL_CAMPFIRE).pattern(" S ").pattern("SCS").pattern("LLL")
                .define('S', Items.STICK).define('C', AllBlockItem.scorched_coal.asItem()).define('L', ItemTags.LOGS_THAT_BURN)
                .unlockedBy("has_scorched_coal", has(AllBlockItem.scorched_coal.asItem())).save(recipeOutput, ResourceLocation.parse("minecraft:soul_campfire"));

        burntRecipe(recipeOutput, Items.BEEF, Items.COOKED_BEEF, AllBlockItem.burnt_beef);
        burntRecipe(recipeOutput, Items.CHICKEN, Items.COOKED_CHICKEN, AllBlockItem.burnt_chicken);
        burntRecipe(recipeOutput, Items.COD, Items.COOKED_COD, AllBlockItem.burnt_cod);
        burntRecipe(recipeOutput, Items.MUTTON, Items.COOKED_MUTTON, AllBlockItem.burnt_mutton);
        burntRecipe(recipeOutput, Items.PORKCHOP, Items.COOKED_PORKCHOP, AllBlockItem.burnt_porkchop);
        burntRecipe(recipeOutput, Items.RABBIT, Items.COOKED_RABBIT, AllBlockItem.burnt_rabbit);
        burntRecipe(recipeOutput, Items.SALMON, Items.COOKED_SALMON, AllBlockItem.burnt_salmon);

        smoldering(recipeOutput, Ingredient.of(AllBlockItem.wheat_flour), new FluidStack(Fluids.WATER, 50),
                new ItemStack(AllBlockItem.wheat_dough.asItem()), FluidStack.EMPTY, 200, 1, 0);

        oreCampfiring(recipeOutput, List.of(AllBlockItem.wheat_dough), RecipeCategory.FOOD, Items.BREAD, 0.35f, 600, "bread");


        smoldering(recipeOutput, Ingredient.of(AllBlockItem.scorched_dust), new FluidStack(Fluids.LAVA, 50),
                new ItemStack(AllBlockItem.soul_mixture.asItem()), FluidStack.EMPTY, 200, 2, 1);

        oreSeething(recipeOutput, List.of(AllBlockItem.soul_mixture), RecipeCategory.MISC, AllBlockItem.scorched_coal, 1f, 600, "scorched");

        oreSeething(recipeOutput, List.of(Items.SAND), RecipeCategory.MISC, Items.SOUL_SAND, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.DIRT), RecipeCategory.MISC, Items.SOUL_SOIL, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.COAL_BLOCK), RecipeCategory.MISC, AllBlockItem.diamond_shard, 1f, 2000, "misc");
        oreSeething(recipeOutput, List.of(Items.INK_SAC), RecipeCategory.MISC, Items.GLOW_INK_SAC, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.GLASS), RecipeCategory.MISC, Items.QUARTZ, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.BRICK), RecipeCategory.MISC, Items.NETHER_BRICK, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.BRICKS), RecipeCategory.MISC, Items.NETHER_BRICKS, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.BRICK_STAIRS), RecipeCategory.MISC, Items.NETHER_BRICK_STAIRS, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.BRICK_SLAB), RecipeCategory.MISC, Items.NETHER_BRICK_SLAB, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.BRICK_WALL), RecipeCategory.MISC, Items.NETHER_BRICK_WALL, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.REDSTONE), RecipeCategory.MISC, Items.GLOWSTONE_DUST, 1f, 800, "misc");
        oreSeething(recipeOutput, List.of(Items.SWEET_BERRIES), RecipeCategory.MISC, Items.GLOW_BERRIES, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.VINE), RecipeCategory.MISC, Items.GLOW_LICHEN, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.POTATO), RecipeCategory.MISC, Items.POISONOUS_POTATO, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.OBSIDIAN), RecipeCategory.MISC, Items.MAGMA_BLOCK, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.RED_MUSHROOM), RecipeCategory.MISC, Items.CRIMSON_FUNGUS, 1f, 400, "misc");
        oreSeething(recipeOutput, List.of(Items.BROWN_MUSHROOM), RecipeCategory.MISC, Items.WARPED_FUNGUS, 1f, 400, "misc");

        smoldering(recipeOutput, Ingredient.of(Items.ICE), FluidStack.EMPTY, NonNullList.of(ItemStack.EMPTY), new FluidStack(Fluids.WATER, 750),200, 1,1);
        smoldering(recipeOutput, Ingredient.of(Items.MAGMA_CREAM), FluidStack.EMPTY, new ItemStack(Items.SLIME_BALL), new FluidStack(Fluids.LAVA, 250),400, 3,2);
        smoldering(recipeOutput, Ingredient.of(Items.CHARCOAL), new FluidStack(Fluids.LAVA, 50), new ItemStack(Items.COAL), new FluidStack(Fluids.LAVA, 200),400, 2,2);
        smoldering(recipeOutput, Ingredient.of(Items.BONE), FluidStack.EMPTY, new ItemStack(Items.BONE_MEAL, 4), FluidStack.EMPTY,400, 1,1);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.GOLD_INGOT), Ingredient.of(Items.NETHERITE_SCRAP),
                Ingredient.of(Items.NETHERITE_SCRAP)), new FluidStack(Fluids.LAVA, 500), new ItemStack(Items.NETHERITE_INGOT), FluidStack.EMPTY,2400, 3,2);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.OBSIDIAN), Ingredient.of(AllBlockItem.diamond_shard)), new FluidStack(Fluids.WATER, 250), new ItemStack(Items.CRYING_OBSIDIAN), FluidStack.EMPTY,2000, 3,0);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.EGG), Ingredient.of(Items.CLAY_BALL), Ingredient.of(AllBlockItem.wheat_dough), Ingredient.of(Items.LIME_DYE)), new FluidStack(Fluids.WATER, 50), NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.SLIME_BALL, 2), new ItemStack(Items.BUCKET)), FluidStack.EMPTY,400, 1,1);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.deepslate_cobble), Ingredient.of(Items.GOLD_NUGGET)), new FluidStack(Fluids.LAVA, 300), new ItemStack(AllBlockItem.deepslate_brick.asItem()), new FluidStack(Fluids.LAVA, 250),400, 2,1);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.cobble), Ingredient.of(Items.IRON_NUGGET)), new FluidStack(Fluids.LAVA, 150), new ItemStack(AllBlockItem.stone_brick.asItem()), new FluidStack(Fluids.LAVA, 100),400, 2,1);
        smoldering(recipeOutput, Ingredient.of(Items.COAL_BLOCK), new FluidStack(Fluids.LAVA, 1000), new ItemStack(Items.ANCIENT_DEBRIS), new FluidStack(Fluids.LAVA, 400),12000, 3,2);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.stone_brick), Ingredient.of(Items.IRON_INGOT), Ingredient.of(Items.KELP), Ingredient.of(Items.KELP)), new FluidStack(Fluids.LAVA, 100), new ItemStack(AllBlockItem.sturdy_brick.asItem()), FluidStack.EMPTY,1000, 3,1);
        smoldering(recipeOutput, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.fire_ash.asItem()), Ingredient.of(Items.SUGAR), Ingredient.of(Items.CHARCOAL)), FluidStack.EMPTY, NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.GUNPOWDER)), FluidStack.EMPTY, 400, 2, 1);

        SpecialRecipeBuilder.special(category -> new FireBrewingRecipe()).save(recipeOutput, "cauldron_potion_brewing");
    }
}
