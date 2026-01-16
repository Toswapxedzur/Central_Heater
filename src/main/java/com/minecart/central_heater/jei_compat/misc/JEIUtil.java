package com.minecart.central_heater.jei_compat.misc;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.misc.Alltags;
import com.minecart.central_heater.misc.DataMapHook;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.recipe_input.BlockSmolderingRecipeInput;
import com.minecart.central_heater.recipe.recipe_types.BlockSmolderingRecipe;
import com.minecart.central_heater.recipe.recipe_types.HauntingRecipe;
import com.minecart.central_heater.recipe.recipe_types.SmolderingRecipe;
import com.minecart.central_heater.misc.VirtualLevel;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class JEIUtil {
    private JEIUtil(){

    }

    public static List<IJeiFuelingRecipe> getNetherFuelRecipes(IIngredientManager manager){
        return manager.getAllItemStacks().stream()
                .<IJeiFuelingRecipe>mapMulti((stack, consumer) -> {
                    int burnTime = DataMapHook.getNetherFuelBurnTime(stack);
                    if(burnTime > 0)
                        consumer.accept(new NetherFuelingRecipe(List.of(stack), burnTime));
                }).sorted(Comparator.comparingInt(IJeiFuelingRecipe::getBurnTime))
                .toList();
    }

    public static List<RecipeHolder<BlockSmolderingRecipe>> getBlocksSmolderingRecipes(){
        Level virtualLevel = VirtualLevel.getLevel();
        RecipeManager manager = virtualLevel.getRecipeManager();
        Stream<RecipeHolder<BlockSmolderingRecipe>> original = manager.getAllRecipesFor(AllRecipe.BLOCK_SMOLDERING_RECIPE.get()).stream();
        List<RecipeHolder<AbstractCookingRecipe>> allCookingRecipes = new ArrayList<>();
        allCookingRecipes.addAll((List) manager.getAllRecipesFor(RecipeType.SMELTING));
        allCookingRecipes.addAll((List) manager.getAllRecipesFor(RecipeType.BLASTING));
        allCookingRecipes.addAll((List) manager.getAllRecipesFor(RecipeType.SMOKING));
        allCookingRecipes.addAll((List) manager.getAllRecipesFor(RecipeType.CAMPFIRE_COOKING));
        allCookingRecipes.addAll((List) manager.getAllRecipesFor(AllRecipe.HAUNTING.get()));
        Stream<RecipeHolder<BlockSmolderingRecipe>> cooking = allCookingRecipes.stream().mapMulti((holder, mapper) -> {
            AbstractCookingRecipe recipe = holder.value();
            ItemStack result = recipe.getResultItem(virtualLevel.registryAccess());
            if (recipe.getIngredients().isEmpty()) return;
            List<ItemStack> allIngredient = recipe.getIngredients().stream().<ItemStack>mapMulti((
                    (ingredient, consumer) -> Arrays.stream(ingredient.getItems()).toList().forEach(itemStack -> consumer.accept(itemStack)))).toList();
            for (ItemStack stack : allIngredient) {
                if (stack.getItem() instanceof BlockItem input) {
                    boolean isBlockResult = result.getItem() instanceof BlockItem && !result.getItem().getDefaultInstance().is(Alltags.Items.SHOULD_DISPLAY_ITEM);
                    BlockState blockResult = isBlockResult ? ((BlockItem) result.getItem()).getBlock().defaultBlockState() : Blocks.AIR.defaultBlockState();
                    int fireLevel = recipe instanceof HauntingRecipe ? 2 : 1;
                    boolean repetitive = !manager.getRecipeFor(AllRecipe.BLOCK_SMOLDERING_RECIPE.get(),
                            new BlockSmolderingRecipeInput(input.getBlock().defaultBlockState(), fireLevel, false), virtualLevel).isEmpty();
                    if (!repetitive)
                        mapper.accept(new RecipeHolder<>(holder.id().withPath(str -> str.concat("_with_item_" + BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath())), new BlockSmolderingRecipe(
                                input.getBlock().defaultBlockState(),
                                blockResult,
                                isBlockResult ? NonNullList.create() : NonNullList.of(ItemStack.EMPTY, result),
                                recipe.getCookingTime(),
                                fireLevel,
                                false
                        )));
                }
            }
        });
        return Stream.concat(original, cooking).toList();
    }

    public static List<AshDropChanceRecipe> getFireAshDropChanceRecipes(IIngredientManager manager){
        return manager.getAllItemStacks().stream().filter(stack -> stack.getBurnTime(RecipeType.SMELTING) > 0)
                .<AshDropChanceRecipe>mapMulti((stack, consumer) -> {
                    consumer.accept(new AshDropChanceRecipe(List.of(stack), DataMapHook.getFireAshDropChance(stack)));
                }).sorted().toList();
    }

    public static List<AshDropChanceRecipe> getScorchedDustDropChanceRecipes(IIngredientManager manager){
        return manager.getAllItemStacks().stream().filter(stack -> DataMapHook.getNetherFuelBurnTime(stack) > 0)
                .<AshDropChanceRecipe>mapMulti((stack, consumer) -> {
                    consumer.accept(new AshDropChanceRecipe(List.of(stack), DataMapHook.getScorchedDustDropChance(stack)));
                }).sorted().toList();
    }

    public static List<IJeiAnvilRecipe> getAllAnvilRecipes(IVanillaRecipeFactory factory, IIngredientManager manager){
        List<IJeiAnvilRecipe> recipes = new ArrayList<>();
        for(RepairData repairData : getRepairData()){
            List<ItemStack> repairIngredient = List.of(repairData.repairIngredient.getItems());
            for(ItemStack singleRepair : repairData.repairables){
                ItemStack damagedFully = singleRepair.copy();
                ItemStack damagedThreeQuarterly = singleRepair.copy();
                ItemStack damagedHalfly = singleRepair.copy();
                damagedThreeQuarterly.setDamageValue(damagedThreeQuarterly.getMaxDamage() * 3 / 4);
                damagedHalfly.setDamageValue(damagedHalfly.getMaxDamage() / 2);
                damagedFully.setDamageValue(damagedHalfly.getMaxDamage());
                recipes.add(factory.createAnvilRecipe(List.of(damagedFully), repairIngredient, List.of(damagedThreeQuarterly)));
                recipes.add(factory.createAnvilRecipe(List.of(damagedThreeQuarterly), List.of(damagedThreeQuarterly), List.of(damagedHalfly)));
            }
        }
        return recipes;
    }

    public static List<RecipeHolder<SmolderingRecipe>> fireBrewingSmolderingRecipe(){
        PotionBrewing brewing = VirtualLevel.getPotionBrewing();
        List<PotionBrewing.Mix<Potion>> potionMixes = brewing.potionMixes;
        List<RecipeHolder<SmolderingRecipe>> recipeMixes = new ArrayList<>();
        for(PotionBrewing.Mix<Potion> potion : potionMixes){
            Holder<Potion> from = potion.from();
            Ingredient ingredient = potion.ingredient();
            Holder<Potion> to = potion.to();

            FluidStack fluidFrom = new FluidStack(Fluids.WATER, 1000);
            FluidStack fluidTo = new FluidStack(Fluids.WATER, 1000);
            fluidFrom.set(DataComponents.POTION_CONTENTS, new PotionContents(from));
            fluidTo.set(DataComponents.POTION_CONTENTS, new PotionContents(to));

            SmolderingRecipe recipe = new SmolderingRecipe(NonNullList.of(Ingredient.EMPTY, ingredient), fluidFrom, NonNullList.of(ItemStack.EMPTY), fluidTo, 400, 4, 2);
            ResourceLocation id = CentralHeater.modLoc(ResourceLocation.tryParse(to.getRegisteredName()).getPath());
            RecipeHolder<SmolderingRecipe> recipeHolder = new RecipeHolder<>(id, recipe);
            recipeMixes.add(recipeHolder);
        }
        return recipeMixes;
    }

    public static void addAnimatedRecipeSoulFlame(IRecipeExtrasBuilder builder, int x, int y, int tick, IGuiHelper helper){
        ResourceLocation texture = CentralHeater.modLoc("textures/gui/soul_flame.png");
        IDrawableStatic staticDraw = helper.drawableBuilder(texture, 0, 0, 14, 14).setTextureSize(14, 14).build();
        IDrawableAnimated animatedDraw = helper.createAnimatedDrawable(staticDraw, tick, IDrawableAnimated.StartDirection.TOP, true);
        builder.addDrawable(helper.getRecipeFlameEmpty(), x, y);
        builder.addDrawable(animatedDraw, x, y);
    }

    private static List<RepairData> getRepairData() {
        return List.of(
                new RepairData(AllBlockItem.STURDY.getRepairIngredient(),
                        new ItemStack(AllBlockItem.STURDY_CHESTPLATE.asItem()),
                        new ItemStack(AllBlockItem.STURDY_HELMET.asItem()),
                        new ItemStack(AllBlockItem.STURDY_LEGGINGS.asItem()),
                        new ItemStack(AllBlockItem.STURDY_BOOTS.asItem())
                ),
                new RepairData(AllBlockItem.STURDY.getRepairIngredient(),
                        new ItemStack(AllBlockItem.STURDY_PICKAXE.asItem()),
                        new ItemStack(AllBlockItem.STURDY_AXE.asItem()),
                        new ItemStack(AllBlockItem.STURDY_SHOVEL.asItem()),
                        new ItemStack(AllBlockItem.STURDY_HOE.asItem()),
                        new ItemStack(AllBlockItem.STURDY_SWORD.asItem())
                )
        );
    }

    private static class RepairData {
        private final Ingredient repairIngredient;
        private final List<ItemStack> repairables;

        public RepairData(Ingredient repairIngredient, ItemStack... repairables) {
            this.repairIngredient = repairIngredient;
            this.repairables = List.of(repairables);
        }

        public Ingredient getRepairIngredient() {
            return repairIngredient;
        }

        public List<ItemStack> getRepairables() {
            return repairables;
        }
    }

}
