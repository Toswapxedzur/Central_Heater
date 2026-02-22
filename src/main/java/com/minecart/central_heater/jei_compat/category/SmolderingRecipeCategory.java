package com.minecart.central_heater.jei_compat.category;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.block.stove.GoldenStoveBlock;
import com.minecart.central_heater.jei_compat.misc.ExtendedFluidRenderer;
import com.minecart.central_heater.misc.VirtualLevel;
import com.minecart.central_heater.misc.enumeration.NetherFireState;
import com.minecart.central_heater.recipe.recipe_types.FireBrewingRecipe;
import com.minecart.central_heater.recipe.recipe_types.SmolderingRecipe;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
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
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class SmolderingRecipeCategory implements IRecipeCategory<SmolderingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CentralHeater.MODID, "smoldering");
    public static final RecipeType<SmolderingRecipe> RECIPE_TYPE = RecipeType.create(CentralHeater.MODID, "smoldering", SmolderingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final IDrawable slotBackground;

    public SmolderingRecipeCategory(IGuiHelper helper) {
        this.title = Component.translatable("jei.central_heater.category.smoldering");
        this.background = helper.createBlankDrawable(160, 88);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(AllBlockItem.MUD_BRICK_POT.get()));
        this.slotBackground = helper.getSlotDrawable();
    }

    @Override
    public RecipeType<SmolderingRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, SmolderingRecipe recipe, IFocusGroup focuses) {
        // Inputs
        for (int i = 0; i < recipe.getIngredients().size(); i++) {
            builder.addSlot(RecipeIngredientRole.INPUT, 10, 6 + 20 * i)
                    .setBackground(slotBackground, -1, -1)
                    .addIngredients(recipe.getIngredients().get(i));
        }

        if (!recipe.getFluidIngredient().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.INPUT, 32, 6)
                    .setBackground(slotBackground, -1, -1)
                    .setCustomRenderer(ForgeTypes.FLUID_STACK, new ExtendedFluidRenderer(1000, true, 16, 16))
                    .addIngredient(ForgeTypes.FLUID_STACK, recipe.getFluidIngredient());
        }

        // Outputs
        for (int i = 0; i < recipe.getResults().size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 6 + 20 * i)
                    .setBackground(slotBackground, -1, -1)
                    .addItemStack(recipe.getResults().get(i));
        }

        if (!recipe.getFluidResult().isEmpty()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 142, 6)
                    .setBackground(slotBackground, -1, -1)
                    .setCustomRenderer(ForgeTypes.FLUID_STACK, new ExtendedFluidRenderer(1000, true, 16, 16))
                    .addIngredient(ForgeTypes.FLUID_STACK, recipe.getFluidResult());
        }
    }

    @Override
    public void draw(SmolderingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int ticks = recipe.getTime();
        int seconds = ticks / 20;
        Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", seconds);

        Font font = Minecraft.getInstance().font;
        int stringWidth = font.width(timeString);
        guiGraphics.drawString(font, timeString, getWidth() - stringWidth, 88 - font.lineHeight, 0xFF808080, false);

        // 2. Render Blocks (Cleaned up: No more unnecessary casting)
        BlockState state = switch (recipe.getTier()) {
            case 1 -> AllBlockItem.MUD_BRICK_POT.get().defaultBlockState();
            case 2 -> AllBlockItem.BRICK_CAULDRON.get().defaultBlockState();
            case 3 -> AllBlockItem.IRON_CAULDRON.get().defaultBlockState();
            case 4 -> AllBlockItem.GOLDEN_CAULDRON.get().defaultBlockState();
            default -> AllBlockItem.MUD_BRICK_POT.get().defaultBlockState();
        };

        BlockState heaterState = switch (recipe.getFireLevel()) {
            case 0 -> Blocks.GRASS_BLOCK.defaultBlockState();
            case 1 -> AllBlockItem.BRICK_STOVE.get().defaultBlockState()
                    .setValue(BlockStateProperties.LIT, true)
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
            default -> AllBlockItem.BLACKSTONE_STOVE.get().defaultBlockState()
                    .setValue(GoldenStoveBlock.LIT_SOUL, NetherFireState.SOUL)
                    .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
        };

        BlockRenderDispatcher blockRender = VirtualLevel.getBlockRenderer();
        PoseStack pose = guiGraphics.pose();
        Lighting.setupLevel(pose.last().pose());
        RenderSystem.enableDepthTest();
        MultiBufferSource.BufferSource buffer = guiGraphics.bufferSource();

        pose.pushPose();
        pose.mulPose(Axis.XN.rotationDegrees(25f));
        pose.mulPose(Axis.YN.rotationDegrees(30f));
        pose.translate(92f, 48f, 20f);
        pose.scale(25f, -25f, 25f);
        blockRender.renderSingleBlock(heaterState, pose, buffer, 15728880, OverlayTexture.NO_OVERLAY);
        pose.popPose();

        // Render Pot/Cauldron
        pose.pushPose();
        pose.mulPose(Axis.XN.rotationDegrees(25f));
        pose.mulPose(Axis.YN.rotationDegrees(30f));
        pose.translate(92f, 23f, 20f);
        pose.scale(25f, -25f, 25f);
        blockRender.renderSingleBlock(state, pose, buffer, 15728880, OverlayTexture.NO_OVERLAY);
        pose.popPose();

        buffer.endBatch();
        RenderSystem.disableDepthTest();
        Lighting.setupFor3DItems();
    }

    @Override
    public boolean isHandled(SmolderingRecipe recipe) {
        return !(recipe instanceof FireBrewingRecipe);
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(SmolderingRecipe recipe) {
        return recipe.getId();
    }
}