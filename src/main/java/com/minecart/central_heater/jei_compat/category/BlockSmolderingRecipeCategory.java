package com.minecart.central_heater.jei_compat.category;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.block.stove.BrickStoveBlock;
import com.minecart.central_heater.block.stove.GoldenStoveBlock;
import com.minecart.central_heater.jei_compat.misc.VariantCategory;
import com.minecart.central_heater.recipe.recipe_types.BlockSmolderingRecipe;
import com.minecart.central_heater.misc.VirtualLevel;
import com.minecart.central_heater.misc.enumeration.NetherFireState;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

public class BlockSmolderingRecipeCategory extends VariantCategory<BlockSmolderingRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(CentralHeater.MODID, "block_smoldering");
    public static final RecipeType<BlockSmolderingRecipe> RECIPE_TYPE = RecipeType.create(CentralHeater.MODID, "block_smoldering", BlockSmolderingRecipe.class);

    private final IDrawable background;
    private final IDrawable icon;
    private final Component title;
    private final IDrawable slotBackground;

    public BlockSmolderingRecipeCategory(IGuiHelper helper) {
        super(helper);
        this.title = Component.translatable("jei.central_heater.category.block_smoldering");
        this.background = helper.createBlankDrawable(140, 70);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(Items.CHARCOAL));
        this.slotBackground = helper.getSlotDrawable();
    }

    @Override
    public RecipeType<BlockSmolderingRecipe> getRecipeType() {
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
    public void setRecipe(IRecipeLayoutBuilder builder, BlockSmolderingRecipe recipe, IFocusGroup focuses) {
        NonNullList<ItemStack> result = recipe.getItemOutputs();
        for (int i = 0; i < result.size(); i++) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 120, 6 + 20 * i)
                    .setBackground(slotBackground, -1, -1)
                    .addItemStack(result.get(i));
        }

        ItemStack inputStack = new ItemStack(recipe.getInputBlock().getBlock());
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT).addItemStack(inputStack);

        if (!recipe.getResultBlock().isAir()) {
            ItemStack blockResultStack = new ItemStack(recipe.getResultBlock().getBlock());

            boolean alreadyInOutput = result.stream()
                    .anyMatch(stack -> ItemStack.isSameItem(stack, blockResultStack));

            if (!alreadyInOutput) {
                builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT).addItemStack(blockResultStack);
            }
        }
    }

    @Override
    public void draw(BlockSmolderingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        int ticks = recipe.getTime();
        int seconds = ticks / 20;
        Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", seconds);

        staticArrow.draw(guiGraphics, 45, 25);
        getArrow(ticks).draw(guiGraphics, 45, 25);

        Font font = Minecraft.getInstance().font;
        int stringWidth = font.width(timeString);
        guiGraphics.drawString(font, timeString, getWidth() - stringWidth, 70 - font.lineHeight, 0xFF808080, false);

        BlockState stove = switch (recipe.getFireLevel()) {
            default -> Blocks.GRASS_BLOCK.defaultBlockState();
            case 1 -> AllBlockItem.BRICK_STOVE.get().defaultBlockState().setValue(BrickStoveBlock.LIT, true).setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
            case 2 -> AllBlockItem.BLACKSTONE_STOVE.get().defaultBlockState().setValue(GoldenStoveBlock.LIT_SOUL, NetherFireState.SOUL).setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
        };

        BlockState input = recipe.getInputBlock();
        BlockState blockResult = recipe.getResultBlock();
        BlockRenderDispatcher blockRenderer = VirtualLevel.getBlockRenderer();
        PoseStack poseStack = guiGraphics.pose();
        Lighting.setupLevel(poseStack.last().pose());
        RenderSystem.enableDepthTest();

        MultiBufferSource.BufferSource buffer = guiGraphics.bufferSource();

        poseStack.pushPose();
        poseStack.translate(25f, 65f, 100f);
        poseStack.mulPose(Axis.XN.rotationDegrees(25f));
        poseStack.mulPose(Axis.YN.rotationDegrees(210f));
        poseStack.scale(25f, -25f, 25f);

        blockRenderer.renderSingleBlock(stove, poseStack, buffer, 15728880, OverlayTexture.NO_OVERLAY);
        poseStack.translate(0.0f, 1.0f, 0.0f);
        blockRenderer.renderSingleBlock(input, poseStack, buffer, 15728880, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();

        // Render Output + Stove (Right)
        poseStack.pushPose();
        poseStack.translate(95f, 65f, 100f);
        poseStack.mulPose(Axis.XN.rotationDegrees(25f));
        poseStack.mulPose(Axis.YN.rotationDegrees(210f));
        poseStack.scale(25f, -25f, 25f);

        blockRenderer.renderSingleBlock(stove, poseStack, buffer, 15728880, OverlayTexture.NO_OVERLAY);
        if (!blockResult.isAir()) {
            poseStack.translate(0.0f, 1.0f, 0.0f);
            blockRenderer.renderSingleBlock(blockResult, poseStack, buffer, 15728880, OverlayTexture.NO_OVERLAY);
        }
        poseStack.popPose();

        buffer.endBatch();
        RenderSystem.disableDepthTest();
        Lighting.setupFor3DItems();
    }

    @Override
    public @Nullable ResourceLocation getRegistryName(BlockSmolderingRecipe recipe) {
        return recipe.getId();
    }
}