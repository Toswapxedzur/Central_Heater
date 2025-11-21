package com.minecart.central_heater.compat;

import com.minecart.central_heater.AllRegistry;
import com.minecart.central_heater.Central_heater;
import com.minecart.central_heater.recipe.SeethingRecipe;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.Tags;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Stream;

public class SeethingRecipeCategory extends AbstractCookingCategory<SeethingRecipe>{
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(Central_heater.MODID, "seething");
    public static final RecipeType<RecipeHolder<SeethingRecipe>> RECIPE_TYPE = RecipeType.createRecipeHolderType(UID);

    public SeethingRecipeCategory(IGuiHelper guiHelper) {
        super(guiHelper, RECIPE_TYPE, Blocks.SOUL_CAMPFIRE, "gui.jei.category.seething", 200);
    }
}
