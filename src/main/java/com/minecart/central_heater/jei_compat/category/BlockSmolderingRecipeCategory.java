package com.minecart.central_heater.jei_compat.category;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.block.stove.BrickStoveBlock;
import com.minecart.central_heater.block.stove.GoldenStoveBlock;
import com.minecart.central_heater.recipe.recipe_types.BlockSmolderingRecipe;
import com.minecart.central_heater.misc.VirtualLevel;
import com.minecart.central_heater.misc.enumeration.NetherFireState;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;

public class BlockSmolderingRecipeCategory extends AbstractRecipeCategory<RecipeHolder<BlockSmolderingRecipe>> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(CentralHeater.MODID, "block_smoldering");
    public static final RecipeType<RecipeHolder<BlockSmolderingRecipe>> RECIPE_TYPE = RecipeType.createRecipeHolderType(UID);

    private final IGuiHelper helper;

    public BlockSmolderingRecipeCategory(IGuiHelper helper){
        super(RECIPE_TYPE, Component.translatable("jei.central_heater.category.block_smoldering"), helper.createDrawableItemLike(Items.CHARCOAL), 140, 70);
        this.helper = helper;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<BlockSmolderingRecipe> recipeHolder, IFocusGroup focuses) {
        BlockSmolderingRecipe recipe = recipeHolder.value();
        NonNullList<ItemStack> result = recipe.getItemOutputs();
        for(int i=0;i<result.size();i++){
            builder.addOutputSlot(120, 6+20*i).setStandardSlotBackground().addItemStack(result.get(i));
        }
        ItemStack inputStack = new ItemStack(recipe.getInputBlock().getBlock());
        builder.addInvisibleIngredients(RecipeIngredientRole.INPUT)
                .addItemStack(inputStack);

        if (!recipe.getResultBlock().isAir()) {
            ItemStack blockResultStack = new ItemStack(recipe.getResultBlock().getBlock());

            boolean alreadyInOutput = result.stream()
                    .anyMatch(stack -> ItemStack.isSameItem(stack, blockResultStack));

            if (!alreadyInOutput) {
                builder.addInvisibleIngredients(RecipeIngredientRole.OUTPUT)
                        .addItemStack(blockResultStack);
            }
        }
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<BlockSmolderingRecipe> recipeHolder, IFocusGroup focuses) {
        BlockSmolderingRecipe recipe = recipeHolder.value();
        int ticks = recipe.getTime();
        int seconds = ticks/20;
        Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", seconds);
        builder.addText(timeString, getWidth() - 20, 10)
                .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
                .setTextAlignment(HorizontalAlignment.RIGHT)
                .setTextAlignment(VerticalAlignment.BOTTOM)
                .setColor(0xFF808080);
        builder.addAnimatedRecipeArrow(ticks, 45, 25);
    }

    @Override
    public void draw(RecipeHolder<BlockSmolderingRecipe> recipeHolder, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        BlockSmolderingRecipe recipe = recipeHolder.value();
        BlockState stove = switch (recipe.getFireLevel()){
            default -> Blocks.GRASS_BLOCK.defaultBlockState();
            case 1 -> AllBlockItem.BRICK_STOVE.get().defaultBlockState().setValue(BrickStoveBlock.LIT, Boolean.valueOf(true)).setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
            case 2 -> AllBlockItem.BLACKSTONE_STOVE.get().defaultBlockState().setValue(GoldenStoveBlock.LIT_SOUL, NetherFireState.SOUL).setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH);
        };
        BlockState input = recipe.getInputBlock();
        BlockState blockResult = recipe.getResultBlock();
        ItemRenderer itemRenderer = VirtualLevel.getItemRenderer();
        BlockRenderDispatcher blockRenderer = VirtualLevel.getBlockRenderer();
        Lighting.setupLevel();
        RenderSystem.enableDepthTest();
        MultiBufferSource.BufferSource buffer = guiGraphics.bufferSource();
        PoseStack poseStack = guiGraphics.pose();

        poseStack.pushPose();
        poseStack.translate(25f, 65f, 100f);
        poseStack.mulPose(Axis.XN.rotationDegrees(25f));
        poseStack.mulPose(Axis.YN.rotationDegrees(210f));
        poseStack.scale(25f, -25f, 25f);

        blockRenderer.renderSingleBlock(stove, poseStack, buffer, 15728880, OverlayTexture.NO_OVERLAY);
        poseStack.translate(0.0f, 1.0f, 0.0f);
        blockRenderer.renderSingleBlock(input, poseStack, buffer, 15728880, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();

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
}
