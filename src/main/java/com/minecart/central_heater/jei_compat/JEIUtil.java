package com.minecart.central_heater.jei_compat;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.fuel.FuelMapHook;
import com.minecart.central_heater.recipe.SmolderingRecipe;
import com.minecart.central_heater.util.VirtualLevel;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;

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
                    int burnTime = FuelMapHook.getBurnTime(stack);
                    if(burnTime > 0)
                        consumer.accept(new NetherFuelingRecipe(List.of(stack), burnTime));
                }).sorted(Comparator.comparingInt(IJeiFuelingRecipe::getBurnTime))
                .toList();
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

            ItemStack itemFrom = new ItemStack(Items.POTION);
            ItemStack itemTo = new ItemStack(Items.POTION);
            itemFrom.set(DataComponents.POTION_CONTENTS, new PotionContents(from));
            itemTo.set(DataComponents.POTION_CONTENTS, new PotionContents(to));

            SmolderingRecipe recipe = new SmolderingRecipe(NonNullList.of(Ingredient.EMPTY, ingredient, Ingredient.of(itemFrom)), FluidStack.EMPTY, NonNullList.of(ItemStack.EMPTY, itemTo), FluidStack.EMPTY, 400, 4, 2);
            ResourceLocation id = Central_heater.modLoc(ResourceLocation.tryParse(to.getRegisteredName()).getPath());
            RecipeHolder<SmolderingRecipe> recipeHolder = new RecipeHolder<>(id, recipe);
            recipeMixes.add(recipeHolder);
        }
        return recipeMixes;
    }

    public static void addAnimatedRecipeSoulFlame(IRecipeExtrasBuilder builder, int x, int y, int tick, IGuiHelper helper){
        ResourceLocation texture = Central_heater.modLoc("textures/gui/soul_flame.png");
        IDrawableStatic staticDraw = helper.drawableBuilder(texture, 0, 0, 14, 14).setTextureSize(14, 14).build();
        IDrawableAnimated animatedDraw = helper.createAnimatedDrawable(staticDraw, tick, IDrawableAnimated.StartDirection.TOP, true);
        builder.addDrawable(helper.getRecipeFlameEmpty(), x, y);
        builder.addDrawable(animatedDraw, x, y);
    }

    private static List<RepairData> getRepairData() {
        return List.of(
                new RepairData(AllBlockItem.STURDY.getRepairIngredient(),
                        new ItemStack(AllBlockItem.sturdy_chestplate.asItem()),
                        new ItemStack(AllBlockItem.sturdy_helmet.asItem()),
                        new ItemStack(AllBlockItem.sturdy_leggings.asItem()),
                        new ItemStack(AllBlockItem.sturdy_boots.asItem())
                ),
                new RepairData(AllBlockItem.STURDY.getRepairIngredient(),
                        new ItemStack(AllBlockItem.sturdy_pickaxe.asItem()),
                        new ItemStack(AllBlockItem.sturdy_axe.asItem()),
                        new ItemStack(AllBlockItem.sturdy_shovel.asItem()),
                        new ItemStack(AllBlockItem.sturdy_hoe.asItem()),
                        new ItemStack(AllBlockItem.sturdy_sword.asItem())
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
