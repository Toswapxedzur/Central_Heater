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
import net.minecraft.world.level.block.Block;
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
        emptyRecipe(writer, "minecraft:campfire");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.CLAY_BALL)
                .requires(AllBlockItem.CLAY_BIT.get(), 4)
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

        stonecutterResultFromBase(writer, RecipeCategory.BUILDING_BLOCKS, Blocks.CHISELED_STONE_BRICKS, AllBlockItem.STONE_BRICK_TILE.get());
        stonecutterResultFromBase(writer, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILES, AllBlockItem.DEEPSLATE_BRICK_TILE.get());
        stonecutterResultFromBase(writer, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_STAIRS, AllBlockItem.DEEPSLATE_BRICK_TILE.get());
        stonecutterResultFromBase(writer, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_SLAB, AllBlockItem.DEEPSLATE_BRICK_TILE.get(), 2);
        stonecutterResultFromBase(writer, RecipeCategory.BUILDING_BLOCKS, Blocks.DEEPSLATE_TILE_WALL, AllBlockItem.DEEPSLATE_BRICK_TILE.get());

        brickTileRecipe(writer, AllBlockItem.STONE_BRICK_TILE.get().asItem(), AllBlockItem.STONE_BRICK.get().asItem());
        brickTileRecipe(writer, AllBlockItem.DEEPSLATE_BRICK_TILE.get().asItem(), AllBlockItem.DEEPSLATE_BRICK.get().asItem());
        brickTileRecipe(writer, AllBlockItem.MUD_BRICK_TILE.get().asItem(), AllBlockItem.MUD_BRICK.get().asItem());
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
                .pattern("NFN")
                .pattern("BBB")
                .define('N', Items.NETHERRACK)
                .define('B', AllBlockItem.GOLDEN_BRICKS.get())
                .define('F', Items.BLAST_FURNACE)
                .unlockedBy("has_golden_bricks", has(AllBlockItem.GOLDEN_BRICKS.get()))
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
                new ItemStack(AllBlockItem.WHEAT_DOUGH.get().asItem()), FluidStack.EMPTY, 100, 1, 0);

        oreCampfiring(writer, List.of(AllBlockItem.WHEAT_DOUGH.get()), RecipeCategory.FOOD, Items.BREAD, 0.35f, 600, "bread");

        smoldering(writer, Ingredient.of(AllBlockItem.SCORCHED_DUST.get()), new FluidStack(Fluids.LAVA, 125),
                new ItemStack(AllBlockItem.SOUL_MIXTURE.get().asItem()), FluidStack.EMPTY, 100, 2, 1);

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

        // Melting ice (100 ticks)
        smoldering(writer, Ingredient.of(Items.ICE), FluidStack.EMPTY, ItemStack.EMPTY, new FluidStack(Fluids.WATER, 1000), 100, 1, 1);

        // Separation/infusion reactions (200 ticks)
        smoldering(writer, Ingredient.of(Items.MAGMA_CREAM), FluidStack.EMPTY, new ItemStack(Items.SLIME_BALL), new FluidStack(Fluids.LAVA, 250), 200, 3, 2);
        smoldering(writer, Ingredient.of(Items.CHARCOAL), new FluidStack(Fluids.LAVA, 250), new ItemStack(Items.COAL), FluidStack.EMPTY, 200, 2, 2);

        // Burning bone to ash (100 ticks)
        smoldering(writer, Ingredient.of(Items.BONE), FluidStack.EMPTY, new ItemStack(Items.BONE_MEAL, 4), FluidStack.EMPTY, 100, 1, 1);

        // Complex gooey slime mixture (200 ticks)
        smoldering(writer, NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.EGG), Ingredient.of(Items.CLAY_BALL), Ingredient.of(AllBlockItem.WHEAT_DOUGH.get()), Ingredient.of(Items.LIME_DYE)),
                new FluidStack(Fluids.WATER, 250),
                NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.SLIME_BALL, 2)),
                FluidStack.EMPTY, 200, 1, 1);

        // Basic building materials - Blast Furnace speed (100 ticks)
        smoldering(writer, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.DEEPSLATE_COBBLE.get()), Ingredient.of(Items.GOLD_NUGGET)),
                new FluidStack(Fluids.LAVA, 125),
                NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.DEEPSLATE_BRICK.get().asItem())),
                FluidStack.EMPTY, 100, 2, 1);
        smoldering(writer, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.COBBLE.get()), Ingredient.of(Items.IRON_NUGGET)),
                new FluidStack(Fluids.LAVA, 125),
                NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.STONE_BRICK.get().asItem())),
                FluidStack.EMPTY, 100, 2, 1);

        // Higher-tier building material (200 ticks)
        smoldering(writer, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.COBBLE.get()), Ingredient.of(Items.IRON_INGOT), Ingredient.of(Items.KELP)),
                new FluidStack(Fluids.LAVA, 125),
                NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.STURDY_BRICK.get().asItem())),
                FluidStack.EMPTY, 200, 3, 1);

        // Gunpowder chemical mix (200 ticks)
        smoldering(writer, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.FIRE_ASH.get().asItem()), Ingredient.of(Items.SUGAR), Ingredient.of(Items.CHARCOAL)),
                FluidStack.EMPTY,
                NonNullList.of(ItemStack.EMPTY, new ItemStack(Items.GUNPOWDER), new ItemStack(Items.GUNPOWDER)),
                FluidStack.EMPTY, 200, 2, 1);

        // ================================================================
        // SPECIFIC BURNT LOGS & WOODS (200 Ticks)
        // ================================================================

        // Birch
        blockSmoldering(writer, Blocks.BIRCH_LOG, AllBlockItem.BURNT_BIRCH_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_BIRCH_LOG, AllBlockItem.BURNT_BIRCH_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.BIRCH_WOOD, AllBlockItem.BURNT_BIRCH_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_BIRCH_WOOD, AllBlockItem.BURNT_BIRCH_WOOD.get(), 200, 1, true);

        // Jungle
        blockSmoldering(writer, Blocks.JUNGLE_LOG, AllBlockItem.BURNT_JUNGLE_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_JUNGLE_LOG, AllBlockItem.BURNT_JUNGLE_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.JUNGLE_WOOD, AllBlockItem.BURNT_JUNGLE_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_JUNGLE_WOOD, AllBlockItem.BURNT_JUNGLE_WOOD.get(), 200, 1, true);

        // Cherry
        blockSmoldering(writer, Blocks.CHERRY_LOG, AllBlockItem.BURNT_CHERRY_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_CHERRY_LOG, AllBlockItem.BURNT_CHERRY_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.CHERRY_WOOD, AllBlockItem.BURNT_CHERRY_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_CHERRY_WOOD, AllBlockItem.BURNT_CHERRY_WOOD.get(), 200, 1, true);

        // Mangrove
        blockSmoldering(writer, Blocks.MANGROVE_LOG, AllBlockItem.BURNT_MANGROVE_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_MANGROVE_LOG, AllBlockItem.BURNT_MANGROVE_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.MANGROVE_WOOD, AllBlockItem.BURNT_MANGROVE_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_MANGROVE_WOOD, AllBlockItem.BURNT_MANGROVE_WOOD.get(), 200, 1, true);

        // ================================================================
        // GENERIC BURNT LOGS & WOODS (200 Ticks)
        // ================================================================

        // Oak
        blockSmoldering(writer, Blocks.OAK_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_OAK_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.OAK_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_OAK_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);

        // Spruce
        blockSmoldering(writer, Blocks.SPRUCE_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_SPRUCE_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.SPRUCE_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_SPRUCE_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);

        // Acacia
        blockSmoldering(writer, Blocks.ACACIA_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_ACACIA_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.ACACIA_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_ACACIA_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);

        // Dark Oak
        blockSmoldering(writer, Blocks.DARK_OAK_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_DARK_OAK_LOG, AllBlockItem.BURNT_LOG.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.DARK_OAK_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);
        blockSmoldering(writer, Blocks.STRIPPED_DARK_OAK_WOOD, AllBlockItem.BURNT_WOOD.get(), 200, 1, true);

        Block[] allPlanks = {Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.BIRCH_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.ACACIA_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.MANGROVE_PLANKS, Blocks.CHERRY_PLANKS};
        for (Block plank : allPlanks) {
            blockSmoldering(writer, plank, AllBlockItem.BURNT_PLANKS.get(), 50, 1, true);
        }

        // Stairs (38 ticks)
        Block[] allStairs = {Blocks.OAK_STAIRS, Blocks.SPRUCE_STAIRS, Blocks.BIRCH_STAIRS, Blocks.JUNGLE_STAIRS, Blocks.ACACIA_STAIRS, Blocks.DARK_OAK_STAIRS, Blocks.MANGROVE_STAIRS, Blocks.CHERRY_STAIRS};
        for (Block stair : allStairs) {
            blockSmoldering(writer, stair, AllBlockItem.BURNT_STAIRS.get(), 38, 1, true);
        }

        // Slabs (25 ticks)
        Block[] allSlabs = {Blocks.OAK_SLAB, Blocks.SPRUCE_SLAB, Blocks.BIRCH_SLAB, Blocks.JUNGLE_SLAB, Blocks.ACACIA_SLAB, Blocks.DARK_OAK_SLAB, Blocks.MANGROVE_SLAB, Blocks.CHERRY_SLAB};
        for (Block slab : allSlabs) {
            blockSmoldering(writer, slab, AllBlockItem.BURNT_SLAB.get(), 25, 1, true);
        }

        // Fences (50 ticks)
        Block[] allFences = {Blocks.OAK_FENCE, Blocks.SPRUCE_FENCE, Blocks.BIRCH_FENCE, Blocks.JUNGLE_FENCE, Blocks.ACACIA_FENCE, Blocks.DARK_OAK_FENCE, Blocks.MANGROVE_FENCE, Blocks.CHERRY_FENCE};
        for (Block fence : allFences) {
            blockSmoldering(writer, fence, AllBlockItem.BURNT_FENCE.get(), 50, 1, true);
        }

        // Fence Gates (50 ticks)
        Block[] allGates = {Blocks.OAK_FENCE_GATE, Blocks.SPRUCE_FENCE_GATE, Blocks.BIRCH_FENCE_GATE, Blocks.JUNGLE_FENCE_GATE, Blocks.ACACIA_FENCE_GATE, Blocks.DARK_OAK_FENCE_GATE, Blocks.MANGROVE_FENCE_GATE, Blocks.CHERRY_FENCE_GATE};
        for (Block gate : allGates) {
            blockSmoldering(writer, gate, AllBlockItem.BURNT_FENCE_GATE.get(), 50, 1, true);
        }

        // Burnt Planks to Sticks
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.STICK, 4)
                .pattern("#")
                .pattern("#")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(writer, CentralHeater.modLoc("sticks_from_burnt_planks"));

        blockSmoldering(writer, Blocks.BRICKS, Blocks.NETHER_BRICKS, 200, 2, false);
        blockSmoldering(writer, Blocks.BRICK_STAIRS, Blocks.NETHER_BRICK_STAIRS, 200, 2, false);
        blockSmoldering(writer, Blocks.BRICK_SLAB, Blocks.NETHER_BRICK_SLAB, 200, 2, false);
        blockSmoldering(writer, Blocks.BRICK_WALL, Blocks.NETHER_BRICK_WALL, 200, 2, false);

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

        SmolderingSpecialRecipeBuilder.smolderingSpecial(AllRecipe.FIRE_BREWING_SERIALIZER.get()).save(writer, "cauldron_potion_brewing");

        oreSeething(writer, List.of(Items.ROTTEN_FLESH), RecipeCategory.MISC, Items.LEATHER, 0.1f, 400, "leather_curing");

        List<ItemLike> allSaplings = List.of(
                Items.OAK_SAPLING, Items.SPRUCE_SAPLING, Items.BIRCH_SAPLING, Items.JUNGLE_SAPLING,
                Items.ACACIA_SAPLING, Items.DARK_OAK_SAPLING, Items.CHERRY_SAPLING,
                Items.AZALEA, Items.FLOWERING_AZALEA, Items.BAMBOO
        );

        oreSmelting(writer, allSaplings, RecipeCategory.MISC, Items.DEAD_BUSH, 0.1f, 200, "dead_bush_from_sapling");

        smoldering(writer, NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.FIRE_ASH.get()), Ingredient.of(Items.SAND)),
                new FluidStack(Fluids.WATER, 250),
                NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.CLAY_BIT.get().asItem())),
                FluidStack.EMPTY, 200, 1, 0);

        // Concrete colors (20 ticks each)
        smoldering(writer, Ingredient.of(Items.WHITE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.WHITE_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.ORANGE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.ORANGE_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.MAGENTA_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.MAGENTA_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.LIGHT_BLUE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.LIGHT_BLUE_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.YELLOW_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.YELLOW_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.LIME_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.LIME_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.PINK_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.PINK_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.GRAY_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.GRAY_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.LIGHT_GRAY_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.LIGHT_GRAY_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.CYAN_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.CYAN_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.PURPLE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.PURPLE_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.BLUE_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.BLUE_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.BROWN_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.BROWN_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.GREEN_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.GREEN_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.RED_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.RED_CONCRETE), FluidStack.EMPTY, 20, 0, 0);
        smoldering(writer, Ingredient.of(Items.BLACK_CONCRETE_POWDER), new FluidStack(Fluids.WATER, 1000),
                new ItemStack(Items.BLACK_CONCRETE), FluidStack.EMPTY, 20, 0, 0);

        // --- WOOL (100% Drop Rate, Base: White) ---
        createCleaningRecipe(writer, Blocks.ORANGE_WOOL, Blocks.WHITE_WOOL, Items.ORANGE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.MAGENTA_WOOL, Blocks.WHITE_WOOL, Items.MAGENTA_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.LIGHT_BLUE_WOOL, Blocks.WHITE_WOOL, Items.LIGHT_BLUE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.YELLOW_WOOL, Blocks.WHITE_WOOL, Items.YELLOW_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.LIME_WOOL, Blocks.WHITE_WOOL, Items.LIME_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.PINK_WOOL, Blocks.WHITE_WOOL, Items.PINK_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.GRAY_WOOL, Blocks.WHITE_WOOL, Items.GRAY_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.LIGHT_GRAY_WOOL, Blocks.WHITE_WOOL, Items.LIGHT_GRAY_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.CYAN_WOOL, Blocks.WHITE_WOOL, Items.CYAN_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.PURPLE_WOOL, Blocks.WHITE_WOOL, Items.PURPLE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.BLUE_WOOL, Blocks.WHITE_WOOL, Items.BLUE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.BROWN_WOOL, Blocks.WHITE_WOOL, Items.BROWN_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.GREEN_WOOL, Blocks.WHITE_WOOL, Items.GREEN_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.RED_WOOL, Blocks.WHITE_WOOL, Items.RED_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.BLACK_WOOL, Blocks.WHITE_WOOL, Items.BLACK_DYE, 1.0f);

        // --- CARPET (60% Drop Rate, Base: White) ---
        createCleaningRecipe(writer, Blocks.ORANGE_CARPET, Blocks.WHITE_CARPET, Items.ORANGE_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.MAGENTA_CARPET, Blocks.WHITE_CARPET, Items.MAGENTA_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.LIGHT_BLUE_CARPET, Blocks.WHITE_CARPET, Items.LIGHT_BLUE_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.YELLOW_CARPET, Blocks.WHITE_CARPET, Items.YELLOW_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.LIME_CARPET, Blocks.WHITE_CARPET, Items.LIME_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.PINK_CARPET, Blocks.WHITE_CARPET, Items.PINK_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.GRAY_CARPET, Blocks.WHITE_CARPET, Items.GRAY_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.LIGHT_GRAY_CARPET, Blocks.WHITE_CARPET, Items.LIGHT_GRAY_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.CYAN_CARPET, Blocks.WHITE_CARPET, Items.CYAN_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.PURPLE_CARPET, Blocks.WHITE_CARPET, Items.PURPLE_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.BLUE_CARPET, Blocks.WHITE_CARPET, Items.BLUE_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.BROWN_CARPET, Blocks.WHITE_CARPET, Items.BROWN_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.GREEN_CARPET, Blocks.WHITE_CARPET, Items.GREEN_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.RED_CARPET, Blocks.WHITE_CARPET, Items.RED_DYE, 0.60f);
        createCleaningRecipe(writer, Blocks.BLACK_CARPET, Blocks.WHITE_CARPET, Items.BLACK_DYE, 0.60f);

        // --- GLASS PANE (4.5% Drop Rate, Base: Glass Pane) ---
        createCleaningRecipe(writer, Blocks.WHITE_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.WHITE_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.ORANGE_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.ORANGE_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.MAGENTA_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.MAGENTA_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.LIGHT_BLUE_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.LIGHT_BLUE_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.YELLOW_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.YELLOW_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.LIME_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.LIME_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.PINK_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.PINK_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.GRAY_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.GRAY_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.LIGHT_GRAY_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.LIGHT_GRAY_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.CYAN_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.CYAN_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.PURPLE_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.PURPLE_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.BLUE_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.BLUE_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.BROWN_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.BROWN_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.GREEN_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.GREEN_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.RED_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.RED_DYE, 0.045f);
        createCleaningRecipe(writer, Blocks.BLACK_STAINED_GLASS_PANE, Blocks.GLASS_PANE, Items.BLACK_DYE, 0.045f);

        // --- GLASS (12% Drop Rate, Base: Glass) ---
        createCleaningRecipe(writer, Blocks.WHITE_STAINED_GLASS, Blocks.GLASS, Items.WHITE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.ORANGE_STAINED_GLASS, Blocks.GLASS, Items.ORANGE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.MAGENTA_STAINED_GLASS, Blocks.GLASS, Items.MAGENTA_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.LIGHT_BLUE_STAINED_GLASS, Blocks.GLASS, Items.LIGHT_BLUE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.YELLOW_STAINED_GLASS, Blocks.GLASS, Items.YELLOW_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.LIME_STAINED_GLASS, Blocks.GLASS, Items.LIME_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.PINK_STAINED_GLASS, Blocks.GLASS, Items.PINK_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.GRAY_STAINED_GLASS, Blocks.GLASS, Items.GRAY_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.LIGHT_GRAY_STAINED_GLASS, Blocks.GLASS, Items.LIGHT_GRAY_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.CYAN_STAINED_GLASS, Blocks.GLASS, Items.CYAN_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.PURPLE_STAINED_GLASS, Blocks.GLASS, Items.PURPLE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.BLUE_STAINED_GLASS, Blocks.GLASS, Items.BLUE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.BROWN_STAINED_GLASS, Blocks.GLASS, Items.BROWN_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.GREEN_STAINED_GLASS, Blocks.GLASS, Items.GREEN_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.RED_STAINED_GLASS, Blocks.GLASS, Items.RED_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.BLACK_STAINED_GLASS, Blocks.GLASS, Items.BLACK_DYE, 0.12f);

        // --- TERRACOTTA (12% Drop Rate, Base: Terracotta) ---
        createCleaningRecipe(writer, Blocks.WHITE_TERRACOTTA, Blocks.TERRACOTTA, Items.WHITE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.ORANGE_TERRACOTTA, Blocks.TERRACOTTA, Items.ORANGE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.MAGENTA_TERRACOTTA, Blocks.TERRACOTTA, Items.MAGENTA_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.LIGHT_BLUE_TERRACOTTA, Blocks.TERRACOTTA, Items.LIGHT_BLUE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.YELLOW_TERRACOTTA, Blocks.TERRACOTTA, Items.YELLOW_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.LIME_TERRACOTTA, Blocks.TERRACOTTA, Items.LIME_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.PINK_TERRACOTTA, Blocks.TERRACOTTA, Items.PINK_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.GRAY_TERRACOTTA, Blocks.TERRACOTTA, Items.GRAY_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.LIGHT_GRAY_TERRACOTTA, Blocks.TERRACOTTA, Items.LIGHT_GRAY_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.CYAN_TERRACOTTA, Blocks.TERRACOTTA, Items.CYAN_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.PURPLE_TERRACOTTA, Blocks.TERRACOTTA, Items.PURPLE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.BLUE_TERRACOTTA, Blocks.TERRACOTTA, Items.BLUE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.BROWN_TERRACOTTA, Blocks.TERRACOTTA, Items.BROWN_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.GREEN_TERRACOTTA, Blocks.TERRACOTTA, Items.GREEN_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.RED_TERRACOTTA, Blocks.TERRACOTTA, Items.RED_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.BLACK_TERRACOTTA, Blocks.TERRACOTTA, Items.BLACK_DYE, 0.12f);

        // --- CONCRETE POWDER (12% Drop Rate, Base: White) ---
        createCleaningRecipe(writer, Blocks.ORANGE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.ORANGE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.MAGENTA_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.MAGENTA_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.LIGHT_BLUE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.LIGHT_BLUE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.YELLOW_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.YELLOW_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.LIME_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.LIME_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.PINK_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.PINK_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.GRAY_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.GRAY_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.LIGHT_GRAY_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.LIGHT_GRAY_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.CYAN_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.CYAN_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.PURPLE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.PURPLE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.BLUE_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.BLUE_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.BROWN_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.BROWN_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.GREEN_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.GREEN_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.RED_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.RED_DYE, 0.12f);
        createCleaningRecipe(writer, Blocks.BLACK_CONCRETE_POWDER, Blocks.WHITE_CONCRETE_POWDER, Items.BLACK_DYE, 0.12f);

        // --- SHULKER BOXES (100% Drop Rate, Base: Shulker Box) ---
        createCleaningRecipe(writer, Blocks.WHITE_SHULKER_BOX, Blocks.SHULKER_BOX, Items.WHITE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.ORANGE_SHULKER_BOX, Blocks.SHULKER_BOX, Items.ORANGE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.MAGENTA_SHULKER_BOX, Blocks.SHULKER_BOX, Items.MAGENTA_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.LIGHT_BLUE_SHULKER_BOX, Blocks.SHULKER_BOX, Items.LIGHT_BLUE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.YELLOW_SHULKER_BOX, Blocks.SHULKER_BOX, Items.YELLOW_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.LIME_SHULKER_BOX, Blocks.SHULKER_BOX, Items.LIME_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.PINK_SHULKER_BOX, Blocks.SHULKER_BOX, Items.PINK_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.GRAY_SHULKER_BOX, Blocks.SHULKER_BOX, Items.GRAY_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.LIGHT_GRAY_SHULKER_BOX, Blocks.SHULKER_BOX, Items.LIGHT_GRAY_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.CYAN_SHULKER_BOX, Blocks.SHULKER_BOX, Items.CYAN_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.PURPLE_SHULKER_BOX, Blocks.SHULKER_BOX, Items.PURPLE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.BLUE_SHULKER_BOX, Blocks.SHULKER_BOX, Items.BLUE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.BROWN_SHULKER_BOX, Blocks.SHULKER_BOX, Items.BROWN_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.GREEN_SHULKER_BOX, Blocks.SHULKER_BOX, Items.GREEN_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.RED_SHULKER_BOX, Blocks.SHULKER_BOX, Items.RED_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.BLACK_SHULKER_BOX, Blocks.SHULKER_BOX, Items.BLACK_DYE, 1.0f);

        // --- BEDS (100% Drop Rate, Base: White) ---
        createCleaningRecipe(writer, Blocks.ORANGE_BED, Blocks.WHITE_BED, Items.ORANGE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.MAGENTA_BED, Blocks.WHITE_BED, Items.MAGENTA_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.LIGHT_BLUE_BED, Blocks.WHITE_BED, Items.LIGHT_BLUE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.YELLOW_BED, Blocks.WHITE_BED, Items.YELLOW_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.LIME_BED, Blocks.WHITE_BED, Items.LIME_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.PINK_BED, Blocks.WHITE_BED, Items.PINK_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.GRAY_BED, Blocks.WHITE_BED, Items.GRAY_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.LIGHT_GRAY_BED, Blocks.WHITE_BED, Items.LIGHT_GRAY_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.CYAN_BED, Blocks.WHITE_BED, Items.CYAN_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.PURPLE_BED, Blocks.WHITE_BED, Items.PURPLE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.BLUE_BED, Blocks.WHITE_BED, Items.BLUE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.BROWN_BED, Blocks.WHITE_BED, Items.BROWN_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.GREEN_BED, Blocks.WHITE_BED, Items.GREEN_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.RED_BED, Blocks.WHITE_BED, Items.RED_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.BLACK_BED, Blocks.WHITE_BED, Items.BLACK_DYE, 1.0f);

        // --- CANDLES (100% Drop Rate, Base: Uncolored Candle) ---
        createCleaningRecipe(writer, Blocks.WHITE_CANDLE, Blocks.CANDLE, Items.WHITE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.ORANGE_CANDLE, Blocks.CANDLE, Items.ORANGE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.MAGENTA_CANDLE, Blocks.CANDLE, Items.MAGENTA_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.LIGHT_BLUE_CANDLE, Blocks.CANDLE, Items.LIGHT_BLUE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.YELLOW_CANDLE, Blocks.CANDLE, Items.YELLOW_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.LIME_CANDLE, Blocks.CANDLE, Items.LIME_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.PINK_CANDLE, Blocks.CANDLE, Items.PINK_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.GRAY_CANDLE, Blocks.CANDLE, Items.GRAY_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.LIGHT_GRAY_CANDLE, Blocks.CANDLE, Items.LIGHT_GRAY_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.CYAN_CANDLE, Blocks.CANDLE, Items.CYAN_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.PURPLE_CANDLE, Blocks.CANDLE, Items.PURPLE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.BLUE_CANDLE, Blocks.CANDLE, Items.BLUE_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.BROWN_CANDLE, Blocks.CANDLE, Items.BROWN_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.GREEN_CANDLE, Blocks.CANDLE, Items.GREEN_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.RED_CANDLE, Blocks.CANDLE, Items.RED_DYE, 1.0f);
        createCleaningRecipe(writer, Blocks.BLACK_CANDLE, Blocks.CANDLE, Items.BLACK_DYE, 1.0f);

        // --- FLOOR BANNERS (100% Drop Rate, 6 Dyes, Base: White) ---
        createCleaningRecipe(writer, Blocks.ORANGE_BANNER, Blocks.WHITE_BANNER, Items.ORANGE_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.MAGENTA_BANNER, Blocks.WHITE_BANNER, Items.MAGENTA_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.LIGHT_BLUE_BANNER, Blocks.WHITE_BANNER, Items.LIGHT_BLUE_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.YELLOW_BANNER, Blocks.WHITE_BANNER, Items.YELLOW_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.LIME_BANNER, Blocks.WHITE_BANNER, Items.LIME_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.PINK_BANNER, Blocks.WHITE_BANNER, Items.PINK_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.GRAY_BANNER, Blocks.WHITE_BANNER, Items.GRAY_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.LIGHT_GRAY_BANNER, Blocks.WHITE_BANNER, Items.LIGHT_GRAY_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.CYAN_BANNER, Blocks.WHITE_BANNER, Items.CYAN_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.PURPLE_BANNER, Blocks.WHITE_BANNER, Items.PURPLE_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.BLUE_BANNER, Blocks.WHITE_BANNER, Items.BLUE_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.BROWN_BANNER, Blocks.WHITE_BANNER, Items.BROWN_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.GREEN_BANNER, Blocks.WHITE_BANNER, Items.GREEN_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.RED_BANNER, Blocks.WHITE_BANNER, Items.RED_DYE, 6, 1.0f);
        createCleaningRecipe(writer, Blocks.BLACK_BANNER, Blocks.WHITE_BANNER, Items.BLACK_DYE, 6, 1.0f);

        // --- WALL BANNERS (100% Drop Rate, 6 Dyes, Base: White, Explicitly Named) ---
        createCleaningRecipe(writer, "orange_wall_banner_cleaning", Blocks.ORANGE_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.ORANGE_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "magenta_wall_banner_cleaning", Blocks.MAGENTA_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.MAGENTA_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "light_blue_wall_banner_cleaning", Blocks.LIGHT_BLUE_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.LIGHT_BLUE_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "yellow_wall_banner_cleaning", Blocks.YELLOW_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.YELLOW_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "lime_wall_banner_cleaning", Blocks.LIME_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.LIME_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "pink_wall_banner_cleaning", Blocks.PINK_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.PINK_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "gray_wall_banner_cleaning", Blocks.GRAY_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.GRAY_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "light_gray_wall_banner_cleaning", Blocks.LIGHT_GRAY_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.LIGHT_GRAY_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "cyan_wall_banner_cleaning", Blocks.CYAN_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.CYAN_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "purple_wall_banner_cleaning", Blocks.PURPLE_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.PURPLE_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "blue_wall_banner_cleaning", Blocks.BLUE_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.BLUE_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "brown_wall_banner_cleaning", Blocks.BROWN_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.BROWN_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "green_wall_banner_cleaning", Blocks.GREEN_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.GREEN_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "red_wall_banner_cleaning", Blocks.RED_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.RED_DYE, 6, 1.0f);
        createCleaningRecipe(writer, "black_wall_banner_cleaning", Blocks.BLACK_WALL_BANNER, Blocks.WHITE_WALL_BANNER, Items.BLACK_DYE, 6, 1.0f);

        // --- Briquettes / Scorched Briquettes / Soap ---
        smoldering(writer,
                NonNullList.of(Ingredient.EMPTY, Ingredient.of(Items.CHARCOAL, Items.COAL), Ingredient.of(Items.CLAY_BALL)),
                new FluidStack(Fluids.WATER, 250),
                NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.BRIQUETTES.get())),
                FluidStack.EMPTY,
                100, 2, 1);

        smoldering(writer,
                NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.SCORCHED_COAL.get()), Ingredient.of(Items.MAGMA_CREAM)),
                new FluidStack(Fluids.LAVA, 250),
                NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.SCORCHED_BRIQUETTES.get())),
                FluidStack.EMPTY,
                100, 2, 2);

        smoldering(writer,
                NonNullList.of(Ingredient.EMPTY, Ingredient.of(AllBlockItem.FIRE_ASH.get()), Ingredient.of(Items.SLIME_BALL), Ingredient.of(Items.PINK_DYE)),
                new FluidStack(Fluids.WATER, 250),
                NonNullList.of(ItemStack.EMPTY, new ItemStack(AllBlockItem.SOAP.get())),
                FluidStack.EMPTY,
                400, 2, 1);

        // --- Sturdy Shears ---
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, AllBlockItem.STURDY_SHEARS.get())
                .pattern(" I")
                .pattern("I ")
                .define('I', AllBlockItem.STURDY_BRICK.get())
                .unlockedBy("has_sturdy_brick", has(AllBlockItem.STURDY_BRICK.get()))
                .save(writer);

        // --- Sturdy Anvil ---
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, AllBlockItem.STURDY_ANVIL.get())
                .pattern("BBB")
                .pattern(" i ")
                .pattern("iii")
                .define('B', AllBlockItem.STURDY_BRICKS.get())
                .define('i', AllBlockItem.STURDY_BRICK.get())
                .unlockedBy("has_sturdy_bricks", has(AllBlockItem.STURDY_BRICKS.get()))
                .save(writer);

        // --- Interchangeable brick families ---
        registerInterchangeableBrickFamily(writer, "coal", AllBlockItem.BRIQUETTES.get(),
                AllBlockItem.COAL_BRICKS.get(), AllBlockItem.COAL_BRICK_SLAB.get(), AllBlockItem.COAL_BRICK_STAIR.get(),
                AllBlockItem.COAL_BRICK_TILE.get(), AllBlockItem.COAL_BRICK_TILE_SLAB.get(), AllBlockItem.COAL_BRICK_TILE_STAIR.get(), AllBlockItem.COAL_BRICK_TILE_WALL.get());

        registerInterchangeableBrickFamily(writer, "gold", Items.GOLD_INGOT,
                AllBlockItem.GOLDEN_BRICKS.get(), AllBlockItem.GOLDEN_BRICK_SLAB.get(), AllBlockItem.GOLDEN_BRICK_STAIR.get(),
                AllBlockItem.GOLDEN_BRICK_TILE.get(), AllBlockItem.GOLDEN_BRICK_TILE_SLAB.get(), AllBlockItem.GOLDEN_BRICK_TILE_STAIR.get(), AllBlockItem.GOLDEN_BRICK_TILE_WALL.get());

        registerInterchangeableBrickFamily(writer, "netherite", Items.NETHERITE_INGOT,
                AllBlockItem.NETHERITE_BRICKS.get(), AllBlockItem.NETHERITE_BRICK_SLAB.get(), AllBlockItem.NETHERITE_BRICK_STAIR.get(),
                AllBlockItem.NETHERITE_BRICK_TILE.get(), AllBlockItem.NETHERITE_BRICK_TILE_SLAB.get(), AllBlockItem.NETHERITE_BRICK_TILE_STAIR.get(), AllBlockItem.NETHERITE_BRICK_TILE_WALL.get());

        registerInterchangeableBrickFamily(writer, "scorched", AllBlockItem.SCORCHED_BRIQUETTES.get(),
                AllBlockItem.SCORCHED_BRICKS.get(), AllBlockItem.SCORCHED_BRICK_SLAB.get(), AllBlockItem.SCORCHED_BRICK_STAIR.get(),
                AllBlockItem.SCORCHED_BRICK_TILE.get(), AllBlockItem.SCORCHED_BRICK_TILE_SLAB.get(), AllBlockItem.SCORCHED_BRICK_TILE_STAIR.get(), AllBlockItem.SCORCHED_BRICK_TILE_WALL.get());

        registerInterchangeableBrickFamily(writer, "sturdy", AllBlockItem.STURDY_BRICK.get(),
                AllBlockItem.STURDY_BRICKS.get(), AllBlockItem.STURDY_BRICK_SLAB.get(), AllBlockItem.STURDY_BRICK_STAIR.get(),
                AllBlockItem.STURDY_BRICK_TILE.get(), AllBlockItem.STURDY_BRICK_TILE_SLAB.get(), AllBlockItem.STURDY_BRICK_TILE_STAIR.get(), AllBlockItem.STURDY_BRICK_TILE_WALL.get());

        // --- Burnt Planks (from burnt logs) ---
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, AllBlockItem.BURNT_PLANKS.get(), 4)
                .requires(Alltags.Items.BURNT_LOG)
                .unlockedBy("has_burnt_log", has(AllBlockItem.BURNT_LOG.get()))
                .group("planks")
                .save(writer, "burnt_planks_from_logs");

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, AllBlockItem.BURNT_STAIRS.get(), 4)
                .pattern("#  ")
                .pattern("## ")
                .pattern("###")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, AllBlockItem.BURNT_SLAB.get(), 6)
                .pattern("###")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, AllBlockItem.BURNABLE_CAMPFIRE.get())
                .define('L', ItemTags.LOGS).define('S', Items.STICK).define('C', ItemTags.COALS)
                .pattern(" S ").pattern("SCS").pattern("LLL")
                .unlockedBy("has_stick", has(Items.STICK))
                .unlockedBy("has_coal", has(ItemTags.COALS))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, AllBlockItem.BURNABLE_SOUL_CAMPFIRE.get())
                .define('L', ItemTags.LOGS).define('S', Items.STICK).define('#', AllBlockItem.SCORCHED_COAL.get().asItem())
                .pattern(" S ").pattern("S#S").pattern("LLL")
                .unlockedBy("has_soul_sand", has(ItemTags.SOUL_FIRE_BASE_BLOCKS))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, AllBlockItem.BURNT_FENCE.get(), 3)
                .pattern("#W#")
                .pattern("#W#")
                .define('#', Items.STICK)
                .define('W', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AllBlockItem.BURNT_FENCE_GATE.get(), 1)
                .pattern("W#W")
                .pattern("W#W")
                .define('#', Items.STICK)
                .define('W', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AllBlockItem.BURNT_TRAPDOOR.get(), 2)
                .pattern("###")
                .pattern("###")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, AllBlockItem.BURNT_BUTTON.get(), 1)
                .requires(AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AllBlockItem.BURNT_PRESSURE_PLATE.get(), 1)
                .pattern("##")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, AllBlockItem.BURNT_DOOR.get(), 3)
                .pattern("##")
                .pattern("##")
                .pattern("##")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.TRANSPORTATION, AllBlockItem.BURNT_BOAT.get(), 1)
                .pattern("# #")
                .pattern("###")
                .define('#', AllBlockItem.BURNT_PLANKS.get())
                .unlockedBy("has_burnt_planks", has(AllBlockItem.BURNT_PLANKS.get()))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TRANSPORTATION, AllBlockItem.BURNT_CHEST_BOAT.get(), 1)
                .requires(AllBlockItem.BURNT_BOAT.get())
                .requires(Items.CHEST)
                .unlockedBy("has_burnt_boat", has(AllBlockItem.BURNT_BOAT.get()))
                .save(writer);

        // --- Copper Stove Recipes ---
        stoveCraftingRecipeBuilder(writer, AllBlockItem.COPPER_STOVE.get(), AllBlockItem.STURDY_BRICK.get(), Items.REDSTONE, Items.COPPER_INGOT);

        // Copper Stove Waxing
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.WAXED_COPPER_STOVE.get())
                .requires(AllBlockItem.COPPER_STOVE.get())
                .requires(Items.HONEYCOMB)
                .unlockedBy("has_copper_stove", has(AllBlockItem.COPPER_STOVE.get()))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.WAXED_EXPOSED_COPPER_STOVE.get())
                .requires(AllBlockItem.EXPOSED_COPPER_STOVE.get())
                .requires(Items.HONEYCOMB)
                .unlockedBy("has_exposed_copper_stove", has(AllBlockItem.EXPOSED_COPPER_STOVE.get()))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.WAXED_WEATHERED_COPPER_STOVE.get())
                .requires(AllBlockItem.WEATHERED_COPPER_STOVE.get())
                .requires(Items.HONEYCOMB)
                .unlockedBy("has_weathered_copper_stove", has(AllBlockItem.WEATHERED_COPPER_STOVE.get()))
                .save(writer);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.WAXED_OXIDIZED_COPPER_STOVE.get())
                .requires(AllBlockItem.OXIDIZED_COPPER_STOVE.get())
                .requires(Items.HONEYCOMB)
                .unlockedBy("has_oxidized_copper_stove", has(AllBlockItem.OXIDIZED_COPPER_STOVE.get()))
                .save(writer);

        // --- Charcoal Bits to Charcoal ---
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.CHARCOAL, 1)
                .pattern("##")
                .pattern("##")
                .define('#', AllBlockItem.CHARCOAL_BIT.get())
                .unlockedBy("has_charcoal_bit", has(AllBlockItem.CHARCOAL_BIT.get()))
                .save(writer, "charcoal_from_bits");

        // --- Ashtray ---
        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, AllBlockItem.ASHTRAY.get())
                .pattern("# #")
                .pattern("#$#")
                .pattern("###")
                .define('#', AllBlockItem.STURDY_BRICK.get())
                .define('$', Items.GOLD_NUGGET)
                .unlockedBy("has_sturdy_brick", has(AllBlockItem.STURDY_BRICK.get()))
                .save(writer);

        // --- Coal & Charcoal Bits Interchange ---
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, Items.COAL, 1)
                .pattern("##")
                .pattern("##")
                .define('#', AllBlockItem.COAL_BIT.get())
                .unlockedBy("has_coal_bit", has(AllBlockItem.COAL_BIT.get()))
                .save(writer, "coal_from_bits");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.COAL_BIT.get(), 4)
                .requires(Items.COAL)
                .unlockedBy("has_coal", has(Items.COAL))
                .save(writer, "coal_bits_from_coal");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, AllBlockItem.CHARCOAL_BIT.get(), 4)
                .requires(Items.CHARCOAL)
                .unlockedBy("has_charcoal", has(Items.CHARCOAL))
                .save(writer, "charcoal_bits_from_charcoal");

        // --- Logs to Woods (Vanilla Ratio: 4 Logs = 3 Wood) ---
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, AllBlockItem.BURNT_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', AllBlockItem.BURNT_LOG.get())
                .unlockedBy("has_burnt_log", has(AllBlockItem.BURNT_LOG.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, AllBlockItem.BURNT_BIRCH_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', AllBlockItem.BURNT_BIRCH_LOG.get())
                .unlockedBy("has_burnt_birch_log", has(AllBlockItem.BURNT_BIRCH_LOG.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, AllBlockItem.BURNT_JUNGLE_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', AllBlockItem.BURNT_JUNGLE_LOG.get())
                .unlockedBy("has_burnt_jungle_log", has(AllBlockItem.BURNT_JUNGLE_LOG.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, AllBlockItem.BURNT_CHERRY_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', AllBlockItem.BURNT_CHERRY_LOG.get())
                .unlockedBy("has_burnt_cherry_log", has(AllBlockItem.BURNT_CHERRY_LOG.get()))
                .save(writer);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, AllBlockItem.BURNT_MANGROVE_WOOD.get(), 3)
                .pattern("##")
                .pattern("##")
                .define('#', AllBlockItem.BURNT_MANGROVE_LOG.get())
                .unlockedBy("has_burnt_mangrove_log", has(AllBlockItem.BURNT_MANGROVE_LOG.get()))
                .save(writer);
    }
}
