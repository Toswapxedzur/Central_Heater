package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.misc.Alltags;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.builder.SmolderingSpecialRecipeBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.function.Consumer;

public class GeneratorRecipe extends HeaterRecipeProvider implements IConditionBuilder {
    public GeneratorRecipe(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> writer) {
        emptyRecipe(writer, "minecraft:brick");
        emptyRecipe(writer, "minecraft:stone_bricks_from_stone_stonecutting");
        emptyRecipe(writer, "minecraft:stone_brick_slab_from_stone_stonecutting");
        emptyRecipe(writer, "minecraft:stone_brick_stairs_from_stone_stonecutting");
        emptyRecipe(writer, "minecraft:stone_brick_walls_from_stone_stonecutting");
        emptyRecipe(writer, "minecraft:deepslate_bricks_from_cobbled_deepslate_stonecutting");
        emptyRecipe(writer, "minecraft:deepslate_brick_stairs_from_cobbled_deepslate_stonecutting");
        emptyRecipe(writer, "minecraft:deepslate_brick_slab_from_cobbled_deepslate_stonecutting");
        emptyRecipe(writer, "minecraft:deepslate_brick_wall_from_cobbled_deepslate_stonecutting");
        emptyRecipe(writer, "minecraft:deepslate_bricks_from_polished_deepslate_stonecutting");
        emptyRecipe(writer, "minecraft:deepslate_brick_stairs_from_polished_deepslate_stonecutting");
        emptyRecipe(writer, "minecraft:deepslate_brick_slab_from_polished_deepslate_stonecutting");
        emptyRecipe(writer, "minecraft:deepslate_brick_wall_from_polished_deepslate_stonecutting");
        emptyRecipe(writer, "minecraft:nether_brick_slab_from_nether_bricks_stonecutting");
        emptyRecipe(writer, "minecraft:charcoal");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CLAY_BALL)
                .requires(AllBlockItem.CLAY_BIT.get(), 4) // [Port] Added .get() if these are RegistryObjects
                .unlockedBy(getHasName(AllBlockItem.CLAY_BIT.get()), has(AllBlockItem.CLAY_BIT.get()))
                .group("clay_processing")
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.CLAY_BIT.get(), 4)
                .requires(Items.CLAY_BALL)
                .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .group("clay_processing")
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.CLAY_BRICK.get())
                .pattern("##")
                .define('#', Items.CLAY_BALL)
                .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .group("clay_processing")
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CLAY_BALL, 2)
                .requires(AllBlockItem.CLAY_BRICK.get())
                .unlockedBy("has_clay_brick", has(AllBlockItem.CLAY_BRICK.get()))
                .save(writer, "clay_balls_from_clay_brick");

        oreSmelting(writer, List.of(AllBlockItem.CLAY_BRICK.get()), RecipeCategory.MISC, Items.BRICK, 0.1f, 200, "clay_processing");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BLACK_DYE)
                .requires(Alltags.Items.OVERBURNT)
                .unlockedBy("has_burnt_food", has(Alltags.Items.OVERBURNT))
                .save(writer, "black_dye_from_burnt_food");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.DIAMOND_SHARD.get(), 4).requires(Items.DIAMOND)
                .unlockedBy(getHasName(Items.DIAMOND), has(Items.DIAMOND)).group("misc").save(writer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.DIAMOND).requires(AllBlockItem.DIAMOND_SHARD.get(), 4)
                .unlockedBy(getHasName(AllBlockItem.DIAMOND_SHARD.get()), has(AllBlockItem.DIAMOND_SHARD.get())).group("misc").save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.STURDY_NUGGET.get(), 9).requires(AllBlockItem.STURDY_BRICK.get())
                .unlockedBy(getHasName(AllBlockItem.STURDY_BRICK.get()), has(AllBlockItem.STURDY_BRICK.get())).group("misc").save(writer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.STURDY_BRICK.get()).requires(AllBlockItem.STURDY_NUGGET.get(), 9)
                .unlockedBy(getHasName(AllBlockItem.STURDY_NUGGET.get()), has(AllBlockItem.STURDY_NUGGET.get())).group("misc").save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.COBBLE.get(), 4).requires(Items.COBBLESTONE)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(Items.COBBLESTONE)).group("misc").save(writer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COBBLESTONE).requires(AllBlockItem.COBBLE.get(), 4)
                .unlockedBy(getHasName(AllBlockItem.COBBLE.get()), has(AllBlockItem.COBBLE.get())).group("misc").save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.DEEPSLATE_COBBLE.get(), 4).requires(Items.COBBLED_DEEPSLATE)
                .unlockedBy(getHasName(Items.COBBLED_DEEPSLATE), has(Items.COBBLED_DEEPSLATE)).group("misc").save(writer);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.COBBLED_DEEPSLATE).requires(AllBlockItem.DEEPSLATE_COBBLE.get(), 4)
                .unlockedBy(getHasName(AllBlockItem.DEEPSLATE_COBBLE.get()), has(AllBlockItem.DEEPSLATE_COBBLE.get())).group("misc").save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CHARCOAL)
                .requires(AllBlockItem.BURNT_LOG.get())
                .unlockedBy("has_burnt_log", has(AllBlockItem.BURNT_LOG.get()))
                .save(writer, "charcoal_from_burnt_log");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CHARCOAL)
                .requires(AllBlockItem.BURNT_WOOD.get())
                .unlockedBy("has_burnt_wood", has(AllBlockItem.BURNT_WOOD.get()))
                .save(writer, "charcoal_from_burnt_wood");

        // [Port] Helpers in RecipeProvider take 'writer' as first arg in 1.20
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(ItemTags.PLANKS), 4, "planks");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(ItemTags.WOODEN_STAIRS), 3, "stairs");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(ItemTags.WOODEN_SLABS), 2, "slabs");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(ItemTags.WOODEN_PRESSURE_PLATES), 1, "pressure_plates");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(ItemTags.WOODEN_BUTTONS), 1, "buttons");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(ItemTags.WOODEN_DOORS), 8, "doors");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(ItemTags.WOODEN_TRAPDOORS), 6, "trapdoors");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(ItemTags.BOATS), 12, "boats");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(ItemTags.WOODEN_FENCES), 2, "fences");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(ItemTags.FENCE_GATES), 3, "fence_gates");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(ItemTags.SIGNS), 1, "signs");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(Items.STICK), 2, "sticks");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(ItemTags.LOGS), 16, "logs");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(Items.WOODEN_SHOVEL), 3, "wooden_shovel");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(Items.WOODEN_SWORD), 5, "wooden_sword");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(Items.WOODEN_HOE), 6, "wooden_hoe");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(Items.WOODEN_PICKAXE), 8, "wooden_pickaxe");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(Items.WOODEN_AXE), 8, "wooden_axe");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(Items.BOWL), 2, "bowls");
        stonecutterTag(writer, RecipeCategory.MISC, AllBlockItem.WOOD_CHIPS.get(), Ingredient.of(Items.LADDER), 4, "ladders");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, AllBlockItem.WHEAT_FLOUR.get())
                .requires(Items.WHEAT)
                .unlockedBy(getHasName(Items.WHEAT), has(Items.WHEAT))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.SCORCHED_DUST.get())
                .requires(Items.SOUL_SOIL)
                .unlockedBy(getHasName(Items.SOUL_SOIL), has(Items.SOUL_SOIL))
                .save(writer, new ResourceLocation(CentralHeater.MODID, "scorched_dust_from_soul_soil"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.SCORCHED_DUST.get())
                .requires(Items.SOUL_SAND)
                .unlockedBy(getHasName(Items.SOUL_SAND), has(Items.SOUL_SAND))
                .save(writer, new ResourceLocation(CentralHeater.MODID, "scorched_dust_from_soul_sand"));

        stonecutterResultFromBase(writer, RecipeCategory.BUILDING_BLOCKS, AllBlockItem.STONE_BRICK_TILE.get(), Blocks.CHISELED_STONE_BRICKS);
        stonecutterResultFromBase(writer, RecipeCategory.BUILDING_BLOCKS, AllBlockItem.DEEPSLATE_BRICK_TILE.get(), Blocks.DEEPSLATE_TILES);
        stonecutterResultFromBase(writer, RecipeCategory.BUILDING_BLOCKS, AllBlockItem.DEEPSLATE_BRICK_TILE.get(), Blocks.DEEPSLATE_TILE_STAIRS);
        stonecutterResultFromBase(writer, RecipeCategory.BUILDING_BLOCKS, AllBlockItem.DEEPSLATE_BRICK_TILE.get(), Blocks.DEEPSLATE_TILE_SLAB, 2);
        stonecutterResultFromBase(writer, RecipeCategory.BUILDING_BLOCKS, AllBlockItem.DEEPSLATE_BRICK_TILE.get(), Blocks.DEEPSLATE_TILE_WALL);

        brickTileRecipe(writer, AllBlockItem.STONE_BRICK_TILE.get().asItem(), AllBlockItem.STONE_BRICK.get().asItem());
        brickTileRecipe(writer, AllBlockItem.DEEPSLATE_BRICK_TILE.get().asItem(), AllBlockItem.DEEPSLATE_BRICK.get().asItem());
        brickTileRecipe(writer, AllBlockItem.MUD_BRICK_TILE.get().asItem(), AllBlockItem.MUD_BRICK.get().asItem());
        brickTileRecipe(writer, AllBlockItem.STURDY_BRICK_TILE.get().asItem(), AllBlockItem.STURDY_BRICK.get().asItem());
        brickTileRecipe(writer, Items.NETHER_BRICKS, Items.NETHER_BRICK);
        brickTileRecipe(writer, Items.RED_NETHER_BRICKS, AllBlockItem.RED_NETHER_BRICK.get().asItem());
        brickTileRecipe(writer, Items.BRICKS, Items.BRICK);
        brickTileRecipe(writer, AllBlockItem.BLACKSTONE_BRICK_TILE.get().asItem(), AllBlockItem.BLACKSTONE_BRICK.get().asItem());

        brickRecipe(writer, Items.STONE_BRICKS, AllBlockItem.STONE_BRICK.get().asItem());
        brickRecipe(writer, Items.DEEPSLATE_BRICKS, AllBlockItem.DEEPSLATE_BRICK.get().asItem());
        brickRecipe(writer, Items.MUD_BRICKS, AllBlockItem.MUD_BRICK.get().asItem());
        brickRecipe(writer, Items.POLISHED_BLACKSTONE_BRICKS, AllBlockItem.BLACKSTONE_BRICK.get().asItem());

        stairSlabWallCraftingStoneCuttingRecipe(writer, AllBlockItem.STONE_BRICK_TILE.get().asItem(), AllBlockItem.STONE_BRICK_TILE_STAIR.get().asItem(),
                AllBlockItem.STONE_BRICK_TILE_SLAB.get().asItem(), AllBlockItem.STONE_BRICK_TILE_WALL.get().asItem());
        stairSlabWallCraftingStoneCuttingRecipe(writer, AllBlockItem.DEEPSLATE_BRICK_TILE.get().asItem(), AllBlockItem.DEEPSLATE_BRICK_TILE_STAIR.get().asItem(),
                AllBlockItem.DEEPSLATE_BRICK_TILE_SLAB.get().asItem(), AllBlockItem.DEEPSLATE_BRICK_TILE_WALL.get().asItem());
        stairSlabWallCraftingStoneCuttingRecipe(writer, AllBlockItem.MUD_BRICK_TILE.get().asItem(), AllBlockItem.MUD_BRICK_TILE_STAIR.get().asItem(),
                AllBlockItem.MUD_BRICK_TILE_SLAB.get().asItem(), AllBlockItem.MUD_BRICK_TILE_WALL.get().asItem());
        stairSlabWallCraftingStoneCuttingRecipe(writer, AllBlockItem.BLACKSTONE_BRICK_TILE.get().asItem(), AllBlockItem.BLACKSTONE_BRICK_TILE_STAIR.get().asItem(),
                AllBlockItem.BLACKSTONE_BRICK_TILE_SLAB.get().asItem(), AllBlockItem.BLACKSTONE_BRICK_TILE_WALL.get().asItem());
        stairSlabWallCraftingStoneCuttingRecipe(writer, AllBlockItem.STURDY_BRICK_TILE.get().asItem(), AllBlockItem.STURDY_BRICK_TILE_STAIR.get().asItem(),
                AllBlockItem.STURDY_BRICK_TILE_SLAB.get().asItem(), AllBlockItem.STURDY_BRICK_TILE_WALL.get().asItem());

        oreBlasting(writer, List.of(Items.PACKED_MUD), RecipeCategory.MISC, AllBlockItem.MUD_BRICK.get(), 0.1f, 200, "brick");
        oreBlasting(writer, List.of(Items.POLISHED_BLACKSTONE), RecipeCategory.MISC, AllBlockItem.BLACKSTONE_BRICK.get(), 0.1f, 200, "brick");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.STONE_BRICK.get())
                .requires(AllBlockItem.COBBLE.get()).requires(Items.CLAY_BALL).requires(Items.FLINT).requires(Items.IRON_NUGGET)
                .unlockedBy(getHasName(AllBlockItem.COBBLE.get()), has(AllBlockItem.COBBLE.get())).group("misc").save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.DEEPSLATE_BRICK.get())
                .requires(AllBlockItem.DEEPSLATE_COBBLE.get()).requires(Items.CLAY_BALL).requires(Items.FLINT).requires(Items.GOLD_NUGGET)
                .unlockedBy(getHasName(AllBlockItem.DEEPSLATE_COBBLE.get()), has(AllBlockItem.DEEPSLATE_COBBLE.get())).group("misc").save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.RED_NETHER_BRICK.get()).requires(Items.NETHER_BRICK).requires(Ingredient.of(Items.NETHER_WART, Items.RED_DYE))
                .unlockedBy(getHasName(Items.NETHER_BRICK), has(Items.NETHER_BRICK)).group("misc").save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.GOLD_BARS.get(), 16).pattern("###").pattern("###").define('#', Items.GOLD_INGOT)
                .unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT)).group("misc").save(writer);

        stoveCraftingRecipeBuilder(writer, AllBlockItem.BRICK_STOVE.get(), Items.BRICK, Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(writer, AllBlockItem.MUD_BRICK_STOVE.get(), AllBlockItem.MUD_BRICK.get(), Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(writer, AllBlockItem.STONE_STOVE.get(), AllBlockItem.STONE_BRICK.get(), Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(writer, AllBlockItem.DEEPSLATE_STOVE.get(), AllBlockItem.DEEPSLATE_BRICK.get(), Items.IRON_INGOT, Items.IRON_BARS);
        stoveCraftingRecipeBuilder(writer, AllBlockItem.NETHER_BRICK_STOVE.get(), Items.NETHER_BRICK, Items.GOLD_INGOT, AllBlockItem.GOLD_BARS.get());
        stoveCraftingRecipeBuilder(writer, AllBlockItem.RED_NETHER_BRICK_STOVE.get(), AllBlockItem.RED_NETHER_BRICK.get(), Items.GOLD_INGOT, AllBlockItem.GOLD_BARS.get());
        stoveCraftingRecipeBuilder(writer, AllBlockItem.BLACKSTONE_STOVE.get(), AllBlockItem.BLACKSTONE_BRICK.get(), Items.GOLD_INGOT, AllBlockItem.GOLD_BARS.get());

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.MUD_BRICK_POT.get())
                .pattern("# #").pattern("# #").pattern("###")
                .define('#', AllBlockItem.MUD_BRICK.get())
                .unlockedBy(getHasName(AllBlockItem.MUD_BRICK.get()), has(AllBlockItem.MUD_BRICK.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.STURDY_TANK_ITEM.get())
                .pattern("# #").pattern(" # ").define('#', AllBlockItem.STURDY_BRICK.get())
                .unlockedBy(getHasName(AllBlockItem.STURDY_BRICK.get()), has(AllBlockItem.STURDY_BRICK.get()))
                .group("misc").save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.CLAY_CAULDRON.get())
                .pattern("# #").pattern("# #").pattern("###")
                .define('#', AllBlockItem.CLAY_BRICK.get())
                .unlockedBy(getHasName(AllBlockItem.CLAY_BRICK.get()), has(AllBlockItem.CLAY_BRICK.get()))
                .save(writer);

        oreSmelting(writer, List.of(AllBlockItem.CLAY_CAULDRON.get()), RecipeCategory.MISC, AllBlockItem.BRICK_CAULDRON.get(), 0.3f, 200, "cauldron");

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.IRON_CAULDRON.get()).pattern("# #").pattern("# #").pattern("###")
                .define('#', Items.IRON_INGOT).unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT)).save(writer, new ResourceLocation("cauldron"));

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.GOLDEN_CAULDRON.get()).pattern("# #").pattern("# #").pattern("###")
                .define('#', Items.GOLD_INGOT).unlockedBy(getHasName(Items.GOLD_INGOT), has(Items.GOLD_INGOT)).save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, AllBlockItem.BLAZING_FURNACE.get())
                .pattern("NNN")
                .pattern("N N")
                .pattern("BBB")
                .define('N', Items.NETHER_BRICK)
                .define('B', AllBlockItem.BLACKSTONE_BRICK_TILE.get())
                .unlockedBy("has_netherite", has(AllBlockItem.BLACKSTONE_BRICK_TILE.get()))
                .save(writer);

        List<ItemLike> sturdyGear = List.of(
                AllBlockItem.STURDY_PICKAXE.get(), AllBlockItem.STURDY_AXE.get(), AllBlockItem.STURDY_SHOVEL.get(), AllBlockItem.STURDY_HOE.get(), AllBlockItem.STURDY_SWORD.get(),
                AllBlockItem.STURDY_HELMET.get(), AllBlockItem.STURDY_CHESTPLATE.get(), AllBlockItem.STURDY_LEGGINGS.get(), AllBlockItem.STURDY_BOOTS.get()
        );

        toolsBundle(writer, AllBlockItem.STURDY_BRICK.get(), AllBlockItem.STURDY_PICKAXE.get(), AllBlockItem.STURDY_AXE.get(), AllBlockItem.STURDY_SHOVEL.get(), AllBlockItem.STURDY_HOE.get(), AllBlockItem.STURDY_SWORD.get());
        armorsBundle(writer, AllBlockItem.STURDY_BRICK.get(), AllBlockItem.STURDY_HELMET.get(), AllBlockItem.STURDY_CHESTPLATE.get(), AllBlockItem.STURDY_LEGGINGS.get(), AllBlockItem.STURDY_BOOTS.get());

        oreSmelting(writer, sturdyGear, RecipeCategory.MISC, AllBlockItem.STURDY_NUGGET.get(), 0.1f, 200, "sturdy_nugget");
        oreBlasting(writer, sturdyGear, RecipeCategory.MISC, AllBlockItem.STURDY_NUGGET.get(), 0.1f, 100, "sturdy_nugget");

        burntRecipe(writer, Items.BEEF, Items.COOKED_BEEF, AllBlockItem.BURNT_BEEF.get());
        burntRecipe(writer, Items.CHICKEN, Items.COOKED_CHICKEN, AllBlockItem.BURNT_CHICKEN.get());
        burntRecipe(writer, Items.COD, Items.COOKED_COD, AllBlockItem.BURNT_COD.get());
        burntRecipe(writer, Items.MUTTON, Items.COOKED_MUTTON, AllBlockItem.BURNT_MUTTON.get());
        burntRecipe(writer, Items.PORKCHOP, Items.COOKED_PORKCHOP, AllBlockItem.BURNT_PORKCHOP.get());
        burntRecipe(writer, Items.RABBIT, Items.COOKED_RABBIT, AllBlockItem.BURNT_RABBIT.get());
        burntRecipe(writer, Items.SALMON, Items.COOKED_SALMON, AllBlockItem.BURNT_SALMON.get());

        smoldering(writer, Ingredient.of(AllBlockItem.WHEAT_FLOUR.get()), new FluidStack(Fluids.WATER, 250),
                new ItemStack(AllBlockItem.WHEAT_DOUGH.get().asItem()), FluidStack.EMPTY, 200, 1, 0);

        oreCampfiring(writer, List.of(AllBlockItem.WHEAT_DOUGH.get()), RecipeCategory.FOOD, Items.BREAD, 0.35f, 600, "bread");

        smoldering(writer, Ingredient.of(AllBlockItem.SCORCHED_DUST.get()), new FluidStack(Fluids.LAVA, 125),
                new ItemStack(AllBlockItem.SOUL_MIXTURE.get().asItem()), FluidStack.EMPTY, 200, 2, 1);

        oreSeething(writer, List.of(AllBlockItem.SOUL_MIXTURE.get()), RecipeCategory.MISC, AllBlockItem.SCORCHED_COAL.get(), 1f, 600, "scorched");
        oreSeething(writer, List.of(Items.SAND), RecipeCategory.MISC, Items.SOUL_SAND, 1f, 400, "misc");
        oreSeething(writer, List.of(Items.DIRT), RecipeCategory.MISC, Items.SOUL_SOIL, 1f, 400, "misc");
        oreSeething(writer, List.of(Items.INK_SAC), RecipeCategory.MISC, Items.GLOW_INK_SAC, 1f, 400, "misc");
        oreSeething(writer, List.of(Items.BRICK), RecipeCategory.MISC, Items.NETHER_BRICK, 1f, 400, "misc");
        oreSeething(writer, List.of(Items.REDSTONE), RecipeCategory.MISC, Items.GLOWSTONE_DUST, 1f, 800, "misc");
        oreSeething(writer, List.of(Items.SWEET_BERRIES), RecipeCategory.MISC, Items.GLOW_BERRIES, 1f, 400, "misc");
        oreSeething(writer, List.of(Items.VINE), RecipeCategory.MISC, Items.GLOW_LICHEN, 1f, 400, "misc");
        oreSeething(writer, List.of(Items.POTATO), RecipeCategory.MISC, Items.POISONOUS_POTATO, 1f, 400, "misc");
        oreSeething(writer, List.of(Items.RED_MUSHROOM), RecipeCategory.MISC, Items.CRIMSON_FUNGUS, 1f, 400, "misc");
        oreSeething(writer, List.of(Items.BROWN_MUSHROOM), RecipeCategory.MISC, Items.WARPED_FUNGUS, 1f, 400, "misc");

        smoldering(writer, Ingredient.of(Items.ICE), FluidStack.EMPTY, ItemStack.EMPTY, new FluidStack(Fluids.WATER, 1000), 200, 1, 1);
        smoldering(writer, Ingredient.of(Items.MAGMA_CREAM), FluidStack.EMPTY, new ItemStack(Items.SLIME_BALL), new FluidStack(Fluids.LAVA, 250), 400, 3, 2);
        smoldering(writer, Ingredient.of(Items.CHARCOAL), new FluidStack(Fluids.LAVA, 250), new ItemStack(Items.COAL), FluidStack.EMPTY, 400, 2, 2);
        smoldering(writer, Ingredient.of(Items.BONE), FluidStack.EMPTY, new ItemStack(Items.BONE_MEAL, 4), FluidStack.EMPTY, 400, 1, 1);
        smoldering(writer, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.EGG), Ingredient.of(Items.CLAY_BALL), Ingredient.of(AllBlockItem.WHEAT_DOUGH.get()), Ingredient.of(Items.LIME_DYE)), new FluidStack(Fluids.WATER, 250), NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.SLIME_BALL, 2)), FluidStack.EMPTY, 400, 1, 1);
        smoldering(writer, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.DEEPSLATE_COBBLE.get()), Ingredient.of(Items.GOLD_NUGGET)), new FluidStack(Fluids.LAVA, 125), NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.DEEPSLATE_BRICK.get())), FluidStack.EMPTY, 400, 2, 1);
        smoldering(writer, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.COBBLE.get()), Ingredient.of(Items.IRON_NUGGET)), new FluidStack(Fluids.LAVA, 125), NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.STONE_BRICK.get())), FluidStack.EMPTY, 400, 2, 1);
        smoldering(writer, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.COBBLE.get()), Ingredient.of(Items.IRON_INGOT), Ingredient.of(Items.KELP)), new FluidStack(Fluids.LAVA, 125), NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.STURDY_BRICK.get())), FluidStack.EMPTY, 1000, 3, 1);
        smoldering(writer, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.FIRE_ASH.get()), Ingredient.of(Items.SUGAR), Ingredient.of(Items.CHARCOAL)), FluidStack.EMPTY, NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.GUNPOWDER)), FluidStack.EMPTY, 400, 2, 1);

        blockSmoldering(writer, Blocks.ACACIA_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.BIRCH_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.CHERRY_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.OAK_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.JUNGLE_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.DARK_OAK_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.MANGROVE_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.SPRUCE_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.ACACIA_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.BIRCH_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.CHERRY_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.OAK_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.JUNGLE_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.DARK_OAK_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.MANGROVE_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.SPRUCE_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.BRICKS, Blocks.NETHER_BRICKS, 200, 2, false);
        blockSmoldering(writer, Blocks.BRICK_STAIRS, Blocks.NETHER_BRICK_STAIRS, 200, 2, false);
        blockSmoldering(writer, Blocks.BRICK_SLAB, Blocks.NETHER_BRICK_SLAB, 200, 2, false);
        blockSmoldering(writer, Blocks.BRICK_WALL, Blocks.NETHER_BRICK_WALL, 200, 2, false);

//        blockSmoldering(writer, Blocks.BONE_BLOCK, Blocks.ANCIENT_DEBRIS, 12000, 2, false);
//        blockSmoldering(writer, Blocks.COAL_BLOCK, Blocks.AIR, NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.DIAMOND_SHARD.get())), 1200, 2, false);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.SOUL_TORCH, 4).pattern("C").pattern("S")
                .define('C', AllBlockItem.SCORCHED_COAL.get().asItem()).define('S', Items.STICK)
                .unlockedBy("has_scorched_coal", has(AllBlockItem.SCORCHED_COAL.get().asItem())).save(writer, new ResourceLocation("minecraft:soul_torch"));

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, Items.SOUL_CAMPFIRE).pattern(" S ").pattern("SCS").pattern("LLL")
                .define('S', Items.STICK).define('C', AllBlockItem.SCORCHED_COAL.get().asItem()).define('L', ItemTags.LOGS_THAT_BURN)
                .unlockedBy("has_scorched_coal", has(AllBlockItem.SCORCHED_COAL.get().asItem())).save(writer, new ResourceLocation("minecraft:soul_campfire"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, AllBlockItem.BLAZING_FURNACE_MINECART.get())
                .requires(AllBlockItem.BLAZING_FURNACE.get())
                .requires(Items.MINECART)
                .unlockedBy("has_blazing_furnace", has(AllBlockItem.BLAZING_FURNACE.get()))
                .save(writer);

        oreSeething(writer, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER, 0.1f, 400, "leather_curing");

        List<ItemLike> allSaplings = List.of(
                Items.OAK_SAPLING, Items.SPRUCE_SAPLING, Items.BIRCH_SAPLING, Items.JUNGLE_SAPLING,
                Items.ACACIA_SAPLING, Items.DARK_OAK_SAPLING, Items.CHERRY_SAPLING,
                Items.AZALEA, Items.FLOWERING_AZALEA, Items.BAMBOO
        );

        oreSmelting(writer, allSaplings, RecipeCategory.MISC, Items.DEAD_BUSH, 0.1f, 200, "dead_bush_from_sapling");

        smoldering(writer, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.FIRE_ASH.get()), Ingredient.of(Items.SAND)),
                new FluidStack(Fluids.WATER, 250),
                NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.CLAY_BIT.get().asItem())), FluidStack.EMPTY, 200, 1, 0);

        smoldering(writer, Ingredient.of(Items.WHITE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.WHITE_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.ORANGE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.ORANGE_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.MAGENTA_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.MAGENTA_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.LIGHT_BLUE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.LIGHT_BLUE_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.YELLOW_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.YELLOW_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.LIME_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.LIME_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.PINK_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.PINK_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.GRAY_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.GRAY_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.LIGHT_GRAY_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.LIGHT_GRAY_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.CYAN_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.CYAN_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.PURPLE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.PURPLE_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.BLUE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.BLUE_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.BROWN_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.BROWN_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.GREEN_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.GREEN_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.RED_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.RED_CONCRETE), FluidStack.EMPTY, 100, 0, 0);
        smoldering(writer, Ingredient.of(Items.BLACK_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.BLACK_CONCRETE), FluidStack.EMPTY, 100, 0, 0);

        SmolderingSpecialRecipeBuilder.smolderingSpecial(AllRecipe.FIRE_BREWING_SERIALIZER.get()).save(writer, "fire_brewing");
    }
}