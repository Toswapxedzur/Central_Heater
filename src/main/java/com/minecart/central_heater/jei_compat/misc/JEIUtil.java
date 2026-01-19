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
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class JEIUtil {
    private JEIUtil() {}

    public static List<IJeiFuelingRecipe> getNetherFuelRecipes(IIngredientManager manager) {
        return manager.getAllItemStacks().stream()
                .<IJeiFuelingRecipe>mapMulti((stack, consumer) -> {
                    int burnTime = DataMapHook.getNetherFuelBurnTime(stack);
                    if (burnTime > 0)
                        consumer.accept(new NetherFuelingRecipe(List.of(stack), burnTime));
                }).sorted(Comparator.comparingInt(IJeiFuelingRecipe::getBurnTime))
                .toList();
    }

    public static List<BlockSmolderingRecipe> getBlocksSmolderingRecipes() {
        Level virtualLevel = VirtualLevel.getLevel();
        RecipeManager manager = virtualLevel.getRecipeManager();
        List<BlockSmolderingRecipe> original = manager.getAllRecipesFor(AllRecipe.BLOCK_SMOLDERING_RECIPE.get());

        List<AbstractCookingRecipe> allCookingRecipes = new ArrayList<>();
        allCookingRecipes.addAll(manager.getAllRecipesFor(RecipeType.SMELTING));
//        allCookingRecipes.addAll(manager.getAllRecipesFor(RecipeType.BLASTING));
//        allCookingRecipes.addAll(manager.getAllRecipesFor(RecipeType.SMOKING));
//        allCookingRecipes.addAll(manager.getAllRecipesFor(RecipeType.CAMPFIRE_COOKING));
        allCookingRecipes.addAll(manager.getAllRecipesFor(AllRecipe.HAUNTING.get()));

        Stream<BlockSmolderingRecipe> cooking = allCookingRecipes.stream().mapMulti((recipe, mapper) -> {
            ItemStack result = recipe.getResultItem(virtualLevel.registryAccess());
            if (recipe.getIngredients().isEmpty()) return;

            List<ItemStack> allIngredient = new ArrayList<>();
            for(Ingredient ingredient : recipe.getIngredients()) {
                allIngredient.addAll(Arrays.asList(ingredient.getItems()));
            }

            for (ItemStack stack : allIngredient) {
                if (stack.getItem() instanceof BlockItem input) {
                    boolean isBlockResult = result.getItem() instanceof BlockItem && !result.getItem().getDefaultInstance().is(Alltags.Items.SHOULD_DISPLAY_ITEM);
                    BlockState blockResult = isBlockResult ? ((BlockItem) result.getItem()).getBlock().defaultBlockState() : Blocks.AIR.defaultBlockState();
                    int fireLevel = recipe instanceof HauntingRecipe ? 2 : 1;

                    boolean repetitive = manager.getRecipeFor(AllRecipe.BLOCK_SMOLDERING_RECIPE.get(),
                            new BlockSmolderingRecipeInput(input.getBlock().defaultBlockState(), fireLevel, false), virtualLevel).isPresent();

                    if (!repetitive) {
                        ResourceLocation newId = new ResourceLocation(recipe.getId().getNamespace(), recipe.getId().getPath() + "_with_item_" + ForgeRegistries.ITEMS.getKey(stack.getItem()).getPath());

                        mapper.accept(new BlockSmolderingRecipe(
                                newId,
                                input.getBlock().defaultBlockState(),
                                blockResult,
                                isBlockResult ? NonNullList.create() : NonNullList.of(ItemStack.EMPTY, result),
                                recipe.getCookingTime(),
                                fireLevel,
                                false
                        ));
                    }
                }
            }
        });
        return Stream.concat(original.stream(), cooking).toList();
    }

    public static List<AshDropChanceRecipe> getFireAshDropChanceRecipes(IIngredientManager manager) {
        return manager.getAllItemStacks().stream()
                .<AshDropChanceRecipe>mapMulti((stack, consumer) -> {
                    if(DataMapHook.getFireAshDropChance(stack) > 0)
                        consumer.accept(new AshDropChanceRecipe(List.of(stack), DataMapHook.getFireAshDropChance(stack)));
                }).sorted().toList();
    }

    public static List<AshDropChanceRecipe> getScorchedDustDropChanceRecipes(IIngredientManager manager) {
        return manager.getAllItemStacks().stream()
                .filter(stack -> DataMapHook.getNetherFuelBurnTime(stack) > 0)
                .<AshDropChanceRecipe>mapMulti((stack, consumer) -> {
                    consumer.accept(new AshDropChanceRecipe(List.of(stack), DataMapHook.getScorchedDustDropChance(stack)));
                }).sorted().toList();
    }

    public static List<IJeiAnvilRecipe> getAllAnvilRecipes(IVanillaRecipeFactory factory, IIngredientManager manager) {
        List<IJeiAnvilRecipe> recipes = new ArrayList<>();
        for (RepairData repairData : getRepairData()) {
            List<ItemStack> repairIngredient = List.of(repairData.repairIngredient.getItems());
            for (ItemStack singleRepair : repairData.repairables) {
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

    public static List<SmolderingRecipe> fireBrewingSmolderingRecipe() {
        // Access static POTION_MIXES directly (Access Transformer enabled)
        List<PotionBrewing.Mix<Potion>> potionMixes = PotionBrewing.POTION_MIXES;
        List<SmolderingRecipe> recipeMixes = new ArrayList<>();

        if (potionMixes == null) return recipeMixes;

        for (PotionBrewing.Mix<Potion> potionMix : potionMixes) {
            Holder<Potion> from = potionMix.from;
            Ingredient ingredient = potionMix.ingredient;
            Holder<Potion> to = potionMix.to;

            FluidStack fluidFrom = new FluidStack(Fluids.WATER, 1000);
            FluidStack fluidTo = new FluidStack(Fluids.WATER, 1000);

            setPotionNBT(fluidFrom, from.get());
            setPotionNBT(fluidTo, to.get());

            ResourceLocation id = new ResourceLocation(CentralHeater.MODID, ForgeRegistries.POTIONS.getKey(to.get()).getPath());
            SmolderingRecipe recipe = new SmolderingRecipe(id, NonNullList.of(Ingredient.EMPTY, ingredient), fluidFrom, NonNullList.of(ItemStack.EMPTY), fluidTo, 400, 4, 2);
            recipeMixes.add(recipe);
        }
        return recipeMixes;
    }

    private static void setPotionNBT(FluidStack stack, Potion potion) {
        if (!stack.hasTag()) stack.setTag(new CompoundTag());
        ResourceLocation potionName = ForgeRegistries.POTIONS.getKey(potion);
        if (potionName != null) {
            stack.getTag().putString("Potion", potionName.toString());
        }
    }

    public static IDrawableAnimated getAnimatedSoulFlame(IGuiHelper helper, int tick) {
        ResourceLocation texture = new ResourceLocation(CentralHeater.MODID, "textures/gui/soul_flame.png");
        IDrawableStatic staticDraw = helper.drawableBuilder(texture, 0, 0, 14, 14).setTextureSize(14, 14).build();
        return helper.createAnimatedDrawable(staticDraw, tick, IDrawableAnimated.StartDirection.TOP, true);
    }

    public static ItemStack getResultItem(Recipe<?> recipe) {
        Minecraft minecraft = Minecraft.getInstance();
        ClientLevel level = minecraft.level;
        if (level == null) {
            throw new NullPointerException("level must not be null.");
        }
        RegistryAccess registryAccess = level.registryAccess();
        return recipe.getResultItem(registryAccess);
    }

    private static List<RepairData> getRepairData() {
        return List.of(
                new RepairData(AllBlockItem.STURDY.getRepairIngredient(),
                        new ItemStack(AllBlockItem.STURDY_CHESTPLATE.get()),
                        new ItemStack(AllBlockItem.STURDY_HELMET.get()),
                        new ItemStack(AllBlockItem.STURDY_LEGGINGS.get()),
                        new ItemStack(AllBlockItem.STURDY_BOOTS.get())
                ),
                new RepairData(AllBlockItem.STURDY.getRepairIngredient(),
                        new ItemStack(AllBlockItem.STURDY_PICKAXE.get()),
                        new ItemStack(AllBlockItem.STURDY_AXE.get()),
                        new ItemStack(AllBlockItem.STURDY_SHOVEL.get()),
                        new ItemStack(AllBlockItem.STURDY_HOE.get()),
                        new ItemStack(AllBlockItem.STURDY_SWORD.get())
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
    }
}