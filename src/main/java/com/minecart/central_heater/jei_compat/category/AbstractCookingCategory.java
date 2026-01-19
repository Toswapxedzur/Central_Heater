package com.minecart.central_heater.jei_compat.category;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.minecart.central_heater.jei_compat.misc.VariantCategory;
import com.minecart.central_heater.jei_compat.misc.JEIUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.level.block.Block;

import static mezz.jei.api.recipe.RecipeIngredientRole.INPUT;
import static mezz.jei.api.recipe.RecipeIngredientRole.OUTPUT;

public abstract class AbstractCookingCategory<T extends AbstractCookingRecipe> extends VariantCategory<T> {
    private final IDrawable background;
    private final int regularCookTime;
    private final IDrawable icon;
    private final Component localizedName;

    public AbstractCookingCategory(IGuiHelper guiHelper, Block icon, String translationKey, int regularCookTime) {
        super(guiHelper);
        this.background = guiHelper.createDrawable(RECIPE_GUI_VANILLA, 0, 114, 82, 54);
        this.regularCookTime = regularCookTime;
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(icon));
        this.localizedName = Component.translatable(translationKey);
    }

    protected IDrawableAnimated getArrow(T recipe) {
        int cookTime = recipe.getCookingTime();
        return super.getArrow(cookTime);
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
    public void draw(T recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        animatedFlame.draw(guiGraphics, 1, 20);

        IDrawableAnimated arrow = getArrow(recipe);
        arrow.draw(guiGraphics, 24, 18);

        drawExperience(recipe, guiGraphics, 0);
        drawCookTime(recipe, guiGraphics, 45);
    }

    protected void drawExperience(T recipe, GuiGraphics guiGraphics, int y) {
        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(experienceString);
            guiGraphics.drawString(fontRenderer, experienceString, getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    protected void drawCookTime(T recipe, GuiGraphics guiGraphics, int y) {
        int cookTime = recipe.getCookingTime();
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            Minecraft minecraft = Minecraft.getInstance();
            Font fontRenderer = minecraft.font;
            int stringWidth = fontRenderer.width(timeString);
            guiGraphics.drawString(fontRenderer, timeString, getWidth() - stringWidth, y, 0xFF808080, false);
        }
    }

    @Override
    public Component getTitle() {
        return localizedName;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, T recipe, IFocusGroup focuses) {
        builder.addSlot(INPUT, 1, 1)
                .addIngredients(recipe.getIngredients().get(0));

        builder.addSlot(OUTPUT, 61, 19)
                .addItemStack(JEIUtil.getResultItem(recipe));
    }

    @Override
    public boolean isHandled(T recipe) {
        return !recipe.isSpecial();
    }

    @Override
    public ResourceLocation getRegistryName(T recipe) {
        return recipe.getId();
    }
}