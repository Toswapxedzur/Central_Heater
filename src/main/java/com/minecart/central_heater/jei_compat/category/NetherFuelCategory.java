package com.minecart.central_heater.jei_compat.category;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.jei_compat.misc.JEIUtil;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.vanilla.IJeiFuelingRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.text.NumberFormat;

public class NetherFuelCategory implements IRecipeCategory<IJeiFuelingRecipe> {
    public static final RecipeType<IJeiFuelingRecipe> RECIPE_TYPE = RecipeType.create(CentralHeater.MODID, "nether_fueling", IJeiFuelingRecipe.class);

    private final IGuiHelper helper;
    private final Component title;
    private final IDrawable background;
    private final IDrawable icon;
    private final IDrawable slotBackground;
    private final int width;

    public NetherFuelCategory(IGuiHelper helper) {
        this.helper = helper;
        this.title = Component.translatable("jei.central_heater.category.netherFuel");
        this.width = calculateWidth();
        this.background = helper.createBlankDrawable(width, 34);

        // Icon: Soul Flame texture
        this.icon = helper.drawableBuilder(new ResourceLocation(CentralHeater.MODID, "textures/gui/soul_flame.png"), 0, 0, 14, 14)
                .setTextureSize(14, 14)
                .build();

        this.slotBackground = helper.getSlotDrawable();
    }

    private static int calculateWidth() {
        Minecraft minecraft = Minecraft.getInstance();
        Font fontRenderer = minecraft.font;
        Component maxSmeltCountText = createSmeltCountText(10000000 * 200);
        int maxStringWidth = fontRenderer.width(maxSmeltCountText.getString());
        int textPadding = 20;
        return 18 + textPadding + maxStringWidth;
    }

    @Override
    public RecipeType<IJeiFuelingRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, IJeiFuelingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 17)
                .setBackground(slotBackground, -1, -1)
                .addItemStacks(recipe.getInputs());
    }

    @Override
    public void draw(IJeiFuelingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int burnTime = recipe.getBurnTime();

        // 1. Draw Animated Flame
        // Note: Creating a drawable every frame in 'draw' is slightly expensive but necessary
        // if you want the speed to match the specific burn time of this item.
        IDrawableAnimated flame = JEIUtil.getAnimatedSoulFlame(helper, burnTime);
        flame.draw(guiGraphics, 1, 0);

        // 2. Draw Text
        Component smeltCountText = createSmeltCountText(burnTime);
        Font font = Minecraft.getInstance().font;
        int stringWidth = font.width(smeltCountText);

        // Center text in the area right of the flame/slot
        // Area start: 20, Area width: (width - 20)
        int xPos = 20 + ((width - 20 - stringWidth) / 2);
        int yPos = (34 - font.lineHeight) / 2; // Center Vertically

        guiGraphics.drawString(font, smeltCountText, xPos, yPos, 0xFF808080, false);
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