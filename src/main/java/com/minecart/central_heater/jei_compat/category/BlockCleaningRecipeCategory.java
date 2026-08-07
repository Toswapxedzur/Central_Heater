package com.minecart.central_heater.jei_compat.category;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.jei_compat.misc.JEIUtil;
import com.minecart.central_heater.recipe.recipe_types.BlockCleaningRecipe;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockCleaningRecipeCategory implements IRecipeCategory<BlockCleaningRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CentralHeater.MODID, "block_cleaning");
    public static final RecipeType<BlockCleaningRecipe> RECIPE_TYPE = RecipeType.create(CentralHeater.MODID, "block_cleaning", BlockCleaningRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final IDrawableAnimated arrow;
    private final IDrawable slotBackground;

    public BlockCleaningRecipeCategory(IGuiHelper helper) {
        this.title = Component.translatable("jei.central_heater.category.block_cleaning");
        this.background = helper.createBlankDrawable(140, 70);
        this.icon = helper.createDrawableItemStack(new ItemStack(AllBlockItem.SOAP.get()));
        ResourceLocation vanillaGui = new ResourceLocation("jei", "textures/jei/gui/gui_vanilla.png");
        this.arrow = helper.createAnimatedDrawable(helper.createDrawable(vanillaGui, 82, 128, 24, 17), 60, IDrawableAnimated.StartDirection.LEFT, false);
        this.slotBackground = helper.getSlotDrawable();
    }

    @Override
    public RecipeType<BlockCleaningRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, BlockCleaningRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 5)
                .setBackground(slotBackground, -1, -1)
                .addItemStack(recipe.getItemOutput())
                .addTooltipCallback((slotView, tooltip) -> {
                    float chance = recipe.getDropChance() * 100f;
                    if (chance < 100f) {
                        tooltip.add(Component.translatable("jei.central_heater.drop_chance",
                                String.format("%.1f%%", chance)).withStyle(ChatFormatting.GRAY));
                    }
                });

        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                .addItemStack(new ItemStack(recipe.getInputBlock()));

        builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT)
                .addItemStack(new ItemStack(recipe.getOutputBlock()));
    }

    @Override
    public void draw(BlockCleaningRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        arrow.draw(guiGraphics, 55, 25);

        guiGraphics.renderFakeItem(new ItemStack(AllBlockItem.SOAP.get()), 42, 4);

        float chance = recipe.getDropChance();
        String chanceText = String.format("%.1f%%", chance * 100);
        guiGraphics.drawString(Minecraft.getInstance().font, chanceText, 60, 8, 0xFF808080, false);

        BlockState inputState = recipe.getInputBlock().defaultBlockState();
        BlockState outputState = recipe.getOutputBlock().defaultBlockState();

        PoseStack poseStack = guiGraphics.pose();
        Lighting.setupLevel(poseStack.last().pose());
        RenderSystem.enableDepthTest();
        MultiBufferSource.BufferSource buffer = guiGraphics.bufferSource();

        poseStack.pushPose();
        poseStack.translate(35f, 55f, 100f);
        poseStack.mulPose(Axis.XN.rotationDegrees(25f));
        poseStack.mulPose(Axis.YN.rotationDegrees(210f));
        poseStack.scale(25f, -25f, 25f);
        JEIUtil.render3DBlock(inputState, poseStack, buffer);
        poseStack.popPose();

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

    @Override
    public @Nullable ResourceLocation getRegistryName(BlockCleaningRecipe recipe) {
        return recipe.getId();
    }
}
