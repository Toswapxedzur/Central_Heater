package com.minecart.central_heater.jei_compat.category;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.jei_compat.misc.AshDropChanceRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;

public class ScorchedDustDropChanceCategory extends AbstractRecipeCategory<AshDropChanceRecipe> {
    // Unique Recipe Type for Scorched Dust
    public static final RecipeType<AshDropChanceRecipe> RECIPE_TYPE = RecipeType.create(CentralHeater.MODID, "scorched_dust_drop_chance", AshDropChanceRecipe.class);

    private final IDrawable arrow;

    public ScorchedDustDropChanceCategory(IGuiHelper helper) {
        super(
                RECIPE_TYPE,
                // Title: "Scorched Dust Recycling" or similar
                Component.translatable("jei.central_heater.category.scorched_dust_drop"),
                // Icon: Scorched Dust
                helper.createDrawableItemStack(new ItemStack(AllBlockItem.SCORCHED_DUST.get())),
                100,
                40
        );
        arrow = helper.getRecipeArrow();
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AshDropChanceRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(17, 5)
                .addItemStacks(recipe.getInput());

        // Output is always Scorched Dust
        builder.addOutputSlot(64, 5)
                .addItemStack(new ItemStack(AllBlockItem.SCORCHED_DUST.get()));
    }

    @Override
    public void draw(AshDropChanceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 38, 5);
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, AshDropChanceRecipe recipe, IFocusGroup focuses) {
        Component chanceText = createChanceText(recipe.getValue());

        builder.addText(chanceText, getWidth(), 20)
                .setPosition(0, 20)
                .setTextAlignment(HorizontalAlignment.CENTER)
                .setTextAlignment(VerticalAlignment.CENTER)
                .setColor(0xFF808080);
    }

    private Component createChanceText(float chance) {
        NumberFormat percentFormat = NumberFormat.getPercentInstance();
        percentFormat.setMaximumFractionDigits(1);
        String percentage = percentFormat.format(chance);

        return Component.translatable("jei.central_heater.scorched_dust_drop_chance", percentage);
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(AshDropChanceRecipe recipe) {
        return null;
    }
}
