package com.minecart.central_heater.jei_compat.category;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.jei_compat.misc.AshDropChanceRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;

public class ScorchedDustDropChanceCategory implements IRecipeCategory<AshDropChanceRecipe> {
    public static final RecipeType<AshDropChanceRecipe> RECIPE_TYPE = RecipeType.create(CentralHeater.MODID, "scorched_dust_drop_chance", AshDropChanceRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final IDrawable arrow;
    private final IDrawable slotBackground;

    public ScorchedDustDropChanceCategory(IGuiHelper helper) {
        this.title = Component.translatable("jei.central_heater.category.scorched_dust_drop");
        this.background = helper.createBlankDrawable(100, 40);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(AllBlockItem.SCORCHED_DUST.get()));

        // Cache slot background
        this.slotBackground = helper.getSlotDrawable();

        // Manual Arrow (Vanilla Furnace Texture)
        this.arrow = helper.drawableBuilder(new ResourceLocation("minecraft", "textures/gui/container/furnace.png"), 176, 14, 24, 17)
                .build();
    }

    @Override
    public RecipeType<AshDropChanceRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AshDropChanceRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 17, 5)
                .setBackground(slotBackground, -1, -1)
                .addItemStacks(recipe.getInput());

        builder.addSlot(RecipeIngredientRole.OUTPUT, 64, 5)
                .setBackground(slotBackground, -1, -1)
                .addItemStack(new ItemStack(AllBlockItem.SCORCHED_DUST.get()));
    }

    @Override
    public void draw(AshDropChanceRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        // 1. Draw Arrow
        arrow.draw(guiGraphics, 38, 5);

        // 2. Draw Text (Manually centered)
        Component chanceText = createChanceText(recipe.getValue());
        Font font = Minecraft.getInstance().font;
        int stringWidth = font.width(chanceText);

        // Center text horizontally in the 100px width, at Y=26
        int xPos = (100 - stringWidth) / 2;
        guiGraphics.drawString(font, chanceText, xPos, 26, 0xFF808080, false);
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