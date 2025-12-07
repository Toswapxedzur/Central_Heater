package com.minecart.central_heater.jei_compat;

import com.minecart.central_heater.Central_heater;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;

public class NetherFuelCategory extends AbstractRecipeCategory<IJeiFuelingRecipe> {
    public static final RecipeType<IJeiFuelingRecipe> RECIPE_TYPE = RecipeType.create(Central_heater.MODID, "nether_fueling", IJeiFuelingRecipe.class);

    public final IGuiHelper helper;

    public NetherFuelCategory(IGuiHelper helper) {
        super(RECIPE_TYPE, Component.translatable("jei.central_heater.category.netherFuel"),
                helper.drawableBuilder(Central_heater.modLoc("textures/gui/soul_flame.png"), 0, 0, 14, 14).setTextureSize(14, 14).build(),
                getMaxWidth(), 34);
        this.helper = helper;
    }

    private static int getMaxWidth() {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        Component maxSmeltCountText = createSmeltCountText(10000000 * 200);
        int maxStringWidth = fontRenderer.width(maxSmeltCountText.getString());
        int textPadding = 20;
        return 18 + textPadding + maxStringWidth;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, IJeiFuelingRecipe recipe, IFocusGroup focuses) {
        builder.addInputSlot(1, 17)
                .setStandardSlotBackground()
                .addItemStacks(recipe.getInputs());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, IJeiFuelingRecipe recipe, IFocusGroup focuses) {
        int burnTime = recipe.getBurnTime();

        JEIUtil.addAnimatedRecipeSoulFlame(builder, 1, 0, burnTime, helper);

        Component smeltCountText = createSmeltCountText(burnTime);
        builder.addText(smeltCountText, getWidth() - 20, getHeight())
                .setPosition(20, 0)
                .setTextAlignment(HorizontalAlignment.CENTER)
                .setTextAlignment(VerticalAlignment.CENTER)
                .setColor(0xFF808080);
    }

    public static Component createSmeltCountText(int burnTime) {
        if (burnTime == 200) {
            return Component.translatable("gui.jei.category.fuel.smeltCount.single");
        } else {
            NumberFormat numberInstance = NumberFormat.getNumberInstance();
            numberInstance.setMaximumFractionDigits(2);
            String smeltCount = numberInstance.format(burnTime / 200f);
            return Component.translatable("gui.jei.category.fuel.smeltCount", smeltCount);
        }
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(IJeiFuelingRecipe recipe) {
        return null;
    }
}
