package com.minecart.central_heater.jei_compat.category;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.jei_compat.misc.JEIUtil;
import com.minecart.central_heater.recipe.recipe_types.BlockCleaningRecipe;
import com.minecart.central_heater.misc.VirtualLevel;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.state.BlockState;

public class BlockCleaningRecipeCategory extends AbstractRecipeCategory<RecipeHolder<BlockCleaningRecipe>> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(CentralHeater.MODID, "block_cleaning");
    public static final RecipeType<RecipeHolder<BlockCleaningRecipe>> RECIPE_TYPE = RecipeType.createRecipeHolderType(UID);

    private final IDrawableAnimated arrow;

    public BlockCleaningRecipeCategory(IGuiHelper helper) {
        super(RECIPE_TYPE,
                Component.translatable("jei.central_heater.category.block_cleaning"),
                helper.createDrawableItemLike(AllBlockItem.SOAP.get()),
                140, 70);

        // Cache the standard static recipe arrow
        this.arrow = helper.createAnimatedRecipeArrow(60);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<BlockCleaningRecipe> recipeHolder, IFocusGroup focuses) {
        BlockCleaningRecipe recipe = recipeHolder.value();

        // Visible Dye Output Slot (positioned above the right block)
        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 5)
                .setStandardSlotBackground()
                .addItemStack(recipe.getItemOutput())
                .addTooltipCallback((recipeSlotView, tooltip) -> {
                    float chance = recipe.getDropChance() * 100f;
                    if (chance < 100f) {
                        // Adds the chance to the tooltip if it's not guaranteed
                        tooltip.add(Component.translatable("jei.central_heater.drop_chance", String.format("%.1f%%", chance)).withStyle(net.minecraft.ChatFormatting.GRAY));
                    }
                });

        // Invisible Ingredients (so players can click blocks in JEI to find this recipe)
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                .addItemStack(new ItemStack(recipe.getInputBlock()));

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT)
                .addItemStack(new ItemStack(recipe.getOutputBlock()));
    }

    @Override
    public void draw(RecipeHolder<BlockCleaningRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        BlockCleaningRecipe recipe = recipeHolder.value();

        // 1. Draw Arrow centrally
        arrow.draw(guiGraphics, 55, 25);

        // 2. Draw Soap Icon moved slightly left
        guiGraphics.renderFakeItem(new ItemStack(AllBlockItem.SOAP.get()), 42, 4);

        // 3. Draw drop chance text immediately to the right of the Soap icon
        float chance = recipe.getDropChance();
        String chanceText = String.format("%.1f%%", chance * 100);
        // Y=8 aligns the text vertically with the center of the 16x16 soap icon (rendered at Y=4)
        guiGraphics.drawString(Minecraft.getInstance().font, chanceText, 60, 8, 0xFF808080, false);

        BlockState inputState = recipe.getInputBlock().defaultBlockState();
        BlockState outputState = recipe.getOutputBlock().defaultBlockState();

        Lighting.setupLevel();
        RenderSystem.enableDepthTest();
        MultiBufferSource.BufferSource buffer = guiGraphics.bufferSource();
        PoseStack poseStack = guiGraphics.pose();

        // Render Input Block (Moved right / closer to center)
        poseStack.pushPose();
        poseStack.translate(35f, 55f, 100f);
        poseStack.mulPose(Axis.XN.rotationDegrees(25f));
        poseStack.mulPose(Axis.YN.rotationDegrees(210f));
        poseStack.scale(25f, -25f, 25f);
        JEIUtil.render3DBlock(inputState, poseStack, buffer);
        poseStack.popPose();

        // Render Cleaned Output Block (Moved left / closer to center)
        poseStack.pushPose();
        poseStack.translate(105f, 55f, 100f);
        poseStack.mulPose(Axis.XN.rotationDegrees(25f));
        poseStack.mulPose(Axis.YN.rotationDegrees(210f));
        poseStack.scale(25f, -25f, 25f);
        JEIUtil.render3DBlock(outputState, poseStack, buffer);
        poseStack.popPose();

        buffer.endBatch();
        RenderSystem.disableDepthTest();
        Lighting.setupFor3DItems();
    }
}
