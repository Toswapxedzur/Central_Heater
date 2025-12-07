package com.minecart.central_heater.jei_compat;

import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.util.VirtualLevel;
import com.mojang.serialization.Codec;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableBuilder;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.IPlaceable;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.ICodecHelper;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;

public abstract class AbstractCookingCategory<T extends AbstractCookingRecipe> extends AbstractRecipeCategory<RecipeHolder<T>> {
    private IGuiHelper guiHelper;

    protected final int regularCookTime;

    public AbstractCookingCategory(IGuiHelper guiHelper, RecipeType<RecipeHolder<T>> recipeType, Block icon, String translationKey, int regularCookTime) {
        this(guiHelper, recipeType, icon, translationKey, regularCookTime, 82, 54);
    }

    public AbstractCookingCategory(IGuiHelper guiHelper, RecipeType<RecipeHolder<T>> recipeType, Block icon, String translationKey, int regularCookTime, int width, int height) {
        super(
                recipeType,
                Component.translatable(translationKey),
                guiHelper.createDrawableItemLike(icon),
                width,
                height
        );
        this.guiHelper = guiHelper;
        this.regularCookTime = regularCookTime;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<T> recipeHolder, IFocusGroup focuses) {
        T recipe = recipeHolder.value();

        builder.addInputSlot(1, 1)
                .setStandardSlotBackground()
                .addIngredients(recipe.getIngredients().getFirst());

        builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 1, 37)
                .setStandardSlotBackground();

        builder.addOutputSlot(61, 19)
                .setOutputSlotBackground()
                .addItemStack(recipe.getResultItem(VirtualLevel.getRegistryAccess()));
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<T> recipeHolder, IFocusGroup focuses) {
        T recipe = recipeHolder.value();
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        builder.addAnimatedRecipeArrow(cookTime)
                .setPosition(26, 17);
        JEIUtil.addAnimatedRecipeSoulFlame(builder, 1, 20, cookTime, guiHelper);

        addExperience(builder, recipeHolder);
        addCookTime(builder, recipeHolder);
    }

    protected void addExperience(IRecipeExtrasBuilder builder, RecipeHolder<T> recipeHolder) {
        T recipe = recipeHolder.value();
        float experience = recipe.getExperience();
        if (experience > 0) {
            Component experienceString = Component.translatable("gui.jei.category.smelting.experience", experience);
            builder.addText(experienceString, getWidth() - 20, 10)
                    .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.TOP)
                    .setTextAlignment(HorizontalAlignment.RIGHT)
                    .setColor(0xFF808080);
        }
    }

    protected void addCookTime(IRecipeExtrasBuilder builder, RecipeHolder<T> recipeHolder) {
        T recipe = recipeHolder.value();
        int cookTime = recipe.getCookingTime();
        if (cookTime <= 0) {
            cookTime = regularCookTime;
        }
        if (cookTime > 0) {
            int cookTimeSeconds = cookTime / 20;
            Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", cookTimeSeconds);
            builder.addText(timeString, getWidth() - 20, 10)
                    .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
                    .setTextAlignment(HorizontalAlignment.RIGHT)
                    .setTextAlignment(VerticalAlignment.BOTTOM)
                    .setColor(0xFF808080);
        }
    }

    @Override
    public boolean isHandled(RecipeHolder<T> recipeHolder) {
        T recipe = recipeHolder.value();
        return !recipe.isSpecial();
    }

    @Override
    public ResourceLocation getRegistryName(RecipeHolder<T> recipe) {
        return recipe.id();
    }

    @Override
    public Codec<RecipeHolder<T>> getCodec(ICodecHelper codecHelper, IRecipeManager recipeManager) {
        return codecHelper.getRecipeHolderCodec();
    }

}
