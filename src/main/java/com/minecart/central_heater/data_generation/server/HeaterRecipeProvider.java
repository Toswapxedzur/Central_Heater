package com.minecart.central_heater.data_generation.server;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.builder.BlockCleaningRecipeBuilder;
import com.minecart.central_heater.recipe.builder.BlockSmolderingRecipeBuilder;
import com.minecart.central_heater.recipe.builder.SmolderingRecipeBuilder;
import com.minecart.central_heater.recipe.recipe_types.EmptyRecipe;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.FalseCondition;
import net.minecraftforge.fluids.FluidStack;

import java.util.List;
import java.util.function.Consumer;

public abstract class HeaterRecipeProvider extends RecipeProvider {

    public HeaterRecipeProvider(PackOutput output) {
        super(output);
    }

    protected static void brickTileRecipe(Consumer<FinishedRecipe> consumer, Item result, Item ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result, 2)
                .pattern("###").pattern("###").pattern("###")
                .define('#', ingredient).group("building")
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(consumer);
    }

    protected static void brickRecipe(Consumer<FinishedRecipe> consumer, Item result, Item ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, result)
                .pattern("##").pattern("##")
                .define('#', ingredient).group("building")
                .unlockedBy(getHasName(ingredient), has(ingredient))
                .save(consumer);
    }

    protected static void stairSlabWallCraftingStoneCuttingRecipe(Consumer<FinishedRecipe> consumer, Item ingredient, Item stair, Item slab, Item wall){
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, stair, ingredient);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, slab, ingredient, 2);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, wall, ingredient);

        stairBuilder(stair, Ingredient.of(ingredient)).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
        slabBuilder(RecipeCategory.BUILDING_BLOCKS, slab, Ingredient.of(ingredient)).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
        wallBuilder(RecipeCategory.BUILDING_BLOCKS, wall, Ingredient.of(ingredient)).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    protected static void stoveCraftingRecipeBuilder(Consumer<FinishedRecipe> consumer, ItemLike stove, ItemLike baseBrick, ItemLike ingot, ItemLike bars){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, stove).pattern("*&*").pattern("# #").pattern("###")
                .define('#', baseBrick).define('*', ingot).define('&', bars)
                .unlockedBy(getHasName(stove), has(stove)).group("misc").save(consumer);
    }

    protected static void potRecipe(Consumer<FinishedRecipe> consumer, ItemLike pot, ItemLike item){
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, pot).pattern("# #").pattern("###").pattern("* *")
                .define('#', item).define('*', AllBlockItem.MUD_BRICK.get())
                .unlockedBy(getHasName(item), has(item)).save(consumer);
    }

    protected static void burntRecipe(Consumer<FinishedRecipe> consumer, ItemLike raw, ItemLike cooked, ItemLike burnt){
        emptyRecipe(consumer, new ResourceLocation(getItemName(cooked.asItem())));
        oreSmelting(consumer, List.of(raw, cooked), RecipeCategory.FOOD, burnt.asItem(), 0.1f, 200, "burnt");
        oreSmoking(consumer, List.of(cooked), RecipeCategory.FOOD, burnt.asItem(), 0.1f, 100, "burnt");
        oreCampfiring(consumer, List.of(cooked), RecipeCategory.FOOD, burnt.asItem(), 0.1f, 600, "burnt");
    }

    // Tools bundles helper methods
    protected static void pickaxeItem(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result).pattern("###").pattern(" * ").pattern(" * ")
                .define('#', ingredient).define('*', Items.STICK).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    protected static void axeItem(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result).pattern("##").pattern("#*").pattern(" *")
                .define('#', ingredient).define('*', Items.STICK).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    protected static void shovelItem(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result).pattern("#").pattern("*").pattern("*")
                .define('#', ingredient).define('*', Items.STICK).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    protected static void hoeItem(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result).pattern("##").pattern(" *").pattern(" *")
                .define('#', ingredient).define('*', Items.STICK).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    protected static void swordItem(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, result).pattern("#").pattern("#").pattern("*")
                .define('#', ingredient).define('*', Items.STICK).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    protected static void helmetItem(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result).pattern("###").pattern("# #")
                .define('#', ingredient).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    protected static void chestplateItem(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result).pattern("# #").pattern("###").pattern("###")
                .define('#', ingredient).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    protected static void leggingsItem(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result).pattern("###").pattern("# #").pattern("# #")
                .define('#', ingredient).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    protected static void bootsItem(Consumer<FinishedRecipe> consumer, ItemLike result, ItemLike ingredient){
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, result).pattern("# #").pattern("# #")
                .define('#', ingredient).unlockedBy(getHasName(ingredient), has(ingredient)).save(consumer);
    }

    protected static void toolsBundle(Consumer<FinishedRecipe> consumer, ItemLike ingredient, ItemLike pickaxe, ItemLike axe, ItemLike shovel, ItemLike hoe, ItemLike sword){
        pickaxeItem(consumer, pickaxe, ingredient);
        axeItem(consumer, axe, ingredient);
        shovelItem(consumer, shovel, ingredient);
        hoeItem(consumer, hoe, ingredient);
        swordItem(consumer, sword, ingredient);
    }

    protected static void armorsBundle(Consumer<FinishedRecipe> consumer, ItemLike ingredient, ItemLike helmet, ItemLike chestplate, ItemLike leggings, ItemLike boots){
        helmetItem(consumer, helmet, ingredient);
        chestplateItem(consumer, chestplate, ingredient);
        leggingsItem(consumer, leggings, ingredient);
        bootsItem(consumer, boots, ingredient);
    }

    protected void stonecutterTag(Consumer<FinishedRecipe> consumer, RecipeCategory category, ItemLike result, Ingredient input, int count, String nameSuffix) {
        SingleItemRecipeBuilder.stonecutting(input, category, result, count)
                .unlockedBy("has_" + nameSuffix, has(result))
                .save(consumer, getItemName(result.asItem()) + "_from_" + nameSuffix);
    }

    protected static void emptyRecipe(Consumer<FinishedRecipe> consumer, String id){
        emptyRecipe(consumer, new ResourceLocation(id));
    }

    protected static void emptyRecipe(Consumer<FinishedRecipe> consumer, ResourceLocation id) {
        // Mirrors 1.21.1: emit both a conditional recipe AND a conditional advancement at <id>
        // so that vanilla counterparts get overridden by an always-false entry.
        ConditionalRecipe.builder()
                .addCondition(FalseCondition.INSTANCE)
                .addRecipe(finishedRecipeConsumer ->
                        EmptyRecipe.Builder.empty().build(finishedRecipeConsumer, id)
                )
                .generateAdvancement(id)
                .build(consumer, id);
    }

    protected static void oreSeething(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group){
        oreCooking(consumer, AllRecipe.HAUNTING_RECIPE_SERIALIZER.get(), ingredients, category, result, experience, cookingTime, group, "_from_seething");
    }

    protected static void oreCooking(Consumer<FinishedRecipe> consumer, RecipeSerializer<? extends AbstractCookingRecipe> serializer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float xp, int time, String group, String suffix) {
        // Match 1.21.1 vanilla behavior: save with bare id -> resolves to minecraft:<name>
        for(ItemLike itemlike : ingredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), category, result, xp, time, serializer)
                    .group(group).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(consumer, getItemName(result.asItem()) + suffix + "_" + getItemName(itemlike.asItem()));
        }
    }

    protected static void oreSmoking(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(consumer, RecipeSerializer.SMOKING_RECIPE, ingredients, category, result, experience, cookingTime, group, "_from_smoking");
    }

    protected static void oreCampfiring(Consumer<FinishedRecipe> consumer, List<ItemLike> ingredients, RecipeCategory category, ItemLike result, float experience, int cookingTime, String group) {
        oreCooking(consumer, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, ingredients, category, result, experience, cookingTime, group, "_from_campfire_cooking");
    }

    // Smoldering Recipe Overloads
    protected static void smoldering(Consumer<FinishedRecipe> consumer, Ingredient ingredients, FluidStack fluidIngredient, ItemStack result, FluidStack fluidResult, int time, int tier, int fireLevel){
        smoldering(consumer, NonNullList.of(Ingredient.EMPTY, ingredients), fluidIngredient, NonNullList.of(ItemStack.EMPTY, result), fluidResult, time, tier, fireLevel);
    }

    protected static void smoldering(Consumer<FinishedRecipe> consumer, NonNullList<Ingredient> ingredients, FluidStack fluidIngredient, NonNullList<ItemStack> result, FluidStack fluidResult, int time, int tier, int fireLevel){
        SmolderingRecipeBuilder builder = SmolderingRecipeBuilder.create(ingredients, fluidIngredient, result, fluidResult, time, tier, fireLevel)
                .unlockedBy(getHasName(Items.CAULDRON), has(Items.CAULDRON));

        StringBuilder name = new StringBuilder();
        name.append("recipe_with_");
        for(ItemStack stack : result) if(!stack.isEmpty()) name.append(getItemName(stack.getItem())).append("_");
        if(!fluidResult.isEmpty()) name.append(getFluidName(fluidResult.getFluid())).append("_");
        name.append("time_").append(time).append("_tier_").append(tier).append("_with_flame_level_").append(fireLevel);

        // Match 1.21.1: save with bare id (no namespace) -> resolves to minecraft:<name>
        builder.save(consumer, new ResourceLocation(name.toString()));
    }

    protected static void blockSmoldering(Consumer<FinishedRecipe> consumer, Block input, Block result, int time, int fireLevel, boolean surround) {
        blockSmoldering(consumer, input, result, NonNullList.create(), time, fireLevel, surround);
    }

    protected static void blockSmoldering(Consumer<FinishedRecipe> consumer, Block input, Block result, NonNullList<ItemStack> itemOutputs, int time, int fireLevel, boolean surround) {
        BlockSmolderingRecipeBuilder builder = BlockSmolderingRecipeBuilder.create(input, result, itemOutputs, time, fireLevel, surround);
        builder.unlockedBy(getHasName(input), has(input));

        String inputName = BuiltInRegistries.BLOCK.getKey(input).getPath();
        String resultName = BuiltInRegistries.BLOCK.getKey(result).getPath();
        String recipeName = inputName + "_to_" + resultName + "_fire_" + fireLevel;

        builder.save(consumer, new ResourceLocation(CentralHeater.MODID, recipeName));
    }

    protected static String getFluidName(Fluid fluid) {
        return BuiltInRegistries.FLUID.getKey(fluid).getPath();
    }

    /**
     * Interchangeable brick family: produces brick, tile, and stair/slab/wall
     * variants for both bricks and tiles, plus stonecutter recipes between every
     * pair. Mirrors 1.21.1's helper exactly.
     */
    protected void registerInterchangeableBrickFamily(Consumer<FinishedRecipe> consumer, String name, ItemLike baseItem, ItemLike brick, ItemLike brickSlab, ItemLike brickStair, ItemLike tile, ItemLike tileSlab, ItemLike tileStair, ItemLike tileWall) {

        // 4 Base Blocks -> 1 Bricks (the 1.21.1 version saves 1 brick despite 2x2 pattern; preserve parity)
        // Match 1.21.1: save with bare String id -> resolves to minecraft:<name>
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, brick, 1)
                .pattern("BB").pattern("BB").define('B', baseItem)
                .unlockedBy(getHasName(baseItem), has(baseItem))
                .save(consumer, name + "_bricks_from_base");

        // 4 Bricks -> 4 Tiles
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, tile, 4)
                .pattern("BB").pattern("BB").define('B', brick)
                .unlockedBy(getHasName(brick), has(brick))
                .save(consumer, name + "_tiles_from_bricks");

        // Brick variants
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, brickSlab, 6)
                .pattern("BBB").define('B', brick)
                .unlockedBy(getHasName(brick), has(brick)).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, brickStair, 4)
                .pattern("B  ").pattern("BB ").pattern("BBB").define('B', brick)
                .unlockedBy(getHasName(brick), has(brick)).save(consumer);

        // Tile variants
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, tileSlab, 6)
                .pattern("TTT").define('T', tile)
                .unlockedBy(getHasName(tile), has(tile)).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, tileStair, 4)
                .pattern("T  ").pattern("TT ").pattern("TTT").define('T', tile)
                .unlockedBy(getHasName(tile), has(tile)).save(consumer);
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, tileWall, 6)
                .pattern("TTT").pattern("TTT").define('T', tile)
                .unlockedBy(getHasName(tile), has(tile)).save(consumer);

        // Stonecutter interchange between brick and tile
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, tile, brick, 1);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, brick, tile, 1);

        // Stonecutter from Bricks
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, brickSlab, brick, 2);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, brickStair, brick, 1);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, tileSlab, brick, 2);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, tileStair, brick, 1);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, tileWall, brick, 1);

        // Stonecutter from Tiles
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, tileSlab, tile, 2);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, tileStair, tile, 1);
        stonecutterResultFromBase(consumer, RecipeCategory.BUILDING_BLOCKS, tileWall, tile, 1);
    }

    /**
     * Mirrors 1.21.1's naming exactly: the recipe file is named
     * {@code <inputBlock>_from_<outputBlock>_cleaning} and lives in the
     * {@code minecraft} namespace (because 1.21.1 passed a bare String to
     * RecipeOutput#save, which parses to {@code minecraft:<id>}).
     */
    protected void createCleaningRecipe(Consumer<FinishedRecipe> consumer, Block inputBlock, Block outputBlock, Item dyeItem, int count, float dyeDropChance) {
        String safeName = getConversionRecipeName(inputBlock, outputBlock) + "_cleaning";
        BlockCleaningRecipeBuilder.cleaning(inputBlock, outputBlock, dyeItem, count, dyeDropChance)
                .unlockedBy("has_input", has(inputBlock))
                .save(consumer, new ResourceLocation(safeName));
    }

    protected void createCleaningRecipe(Consumer<FinishedRecipe> consumer, Block inputBlock, Block outputBlock, Item dyeItem, float dyeDropChance) {
        this.createCleaningRecipe(consumer, inputBlock, outputBlock, dyeItem, 1, dyeDropChance);
    }

    protected void createCleaningRecipe(Consumer<FinishedRecipe> consumer, String customId, Block inputBlock, Block outputBlock, Item dyeItem, int count, float dyeDropChance) {
        BlockCleaningRecipeBuilder.cleaning(inputBlock, outputBlock, dyeItem, count, dyeDropChance)
                .unlockedBy("has_input", has(inputBlock))
                .save(consumer, new ResourceLocation(customId));
    }
}