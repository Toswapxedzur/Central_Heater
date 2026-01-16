package com.minecart.central_heater.jei_compat.category;

import com.minecart.central_heater.AllBlockItem;
import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.block.stove.GoldenStoveBlock;
import com.minecart.central_heater.jei_compat.misc.ExtendedFluidRenderer;
import com.minecart.central_heater.recipe.recipe_types.FireBrewingRecipe;
import com.minecart.central_heater.recipe.recipe_types.SmolderingRecipe;
import com.minecart.central_heater.misc.enumeration.NetherFireState;
import com.minecart.central_heater.misc.VirtualLevel;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.gui.placement.HorizontalAlignment;
import mezz.jei.api.gui.placement.VerticalAlignment;
import mezz.jei.api.gui.widgets.IRecipeExtrasBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.neoforge.NeoForgeTypes;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.AbstractRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class SmolderingRecipeCategory extends AbstractRecipeCategory<RecipeHolder<SmolderingRecipe>> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(CentralHeater.MODID, "smoldering");
    public static final RecipeType<RecipeHolder<SmolderingRecipe>> RECIPE_TYPE = RecipeType.createRecipeHolderType(UID);

    public SmolderingRecipeCategory(IGuiHelper helper) {
        super(RECIPE_TYPE, Component.translatable("jei.central_heater.category.smoldering"), helper.createDrawableItemLike(AllBlockItem.MUD_BRICK_POT), 160, 88);
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RecipeHolder<SmolderingRecipe> holder, IFocusGroup focuses) {
        SmolderingRecipe recipe = holder.value();

        for (int i = 0; i < recipe.getIngredients().size(); i++)
            builder.addInputSlot(10, 6 + 20 * i).setStandardSlotBackground().addIngredients(recipe.getIngredients().get(i));
        if (!recipe.getFluidIngredient().isEmpty())
            builder.addInputSlot(32, 6).setStandardSlotBackground()
                    .setCustomRenderer(NeoForgeTypes.FLUID_STACK, new ExtendedFluidRenderer(1000, true, 16, 16))
                    .addFluidStack(recipe.getFluidIngredient().getFluid(), recipe.getFluidIngredient().getAmount(), recipe.getFluidIngredient().getComponents().asPatch());

        for(int i = 0; i < recipe.getResults().size() ; i++)
            builder.addOutputSlot(120, 6 + 20 * i).setStandardSlotBackground().addItemStack(recipe.getResults().get(i));
        if (!recipe.getFluidResult().isEmpty())
            builder.addOutputSlot(142, 6).setStandardSlotBackground()
                    .setCustomRenderer(NeoForgeTypes.FLUID_STACK, new ExtendedFluidRenderer(1000, true, 16, 16))
                    .addFluidStack(recipe.getFluidResult().getFluid(), recipe.getFluidResult().getAmount(), recipe.getFluidResult().getComponents().asPatch());
    }

    @Override
    public void createRecipeExtras(IRecipeExtrasBuilder builder, RecipeHolder<SmolderingRecipe> holder, IFocusGroup focuses) {
        SmolderingRecipe recipe = holder.value();
        int ticks = recipe.getTime();
        int seconds = ticks/20;
        Component timeString = Component.translatable("gui.jei.category.smelting.time.seconds", seconds);
        builder.addText(timeString, getWidth() - 20, 10)
                .setPosition(0, 0, getWidth(), getHeight(), HorizontalAlignment.RIGHT, VerticalAlignment.BOTTOM)
                .setTextAlignment(HorizontalAlignment.RIGHT)
                .setTextAlignment(VerticalAlignment.BOTTOM)
                .setColor(0xFF808080);
    }

    @Override
    public void draw(RecipeHolder<SmolderingRecipe> recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        BlockState state = switch (recipe.value().getTier()){
            case 1 ->
                    AllBlockItem.MUD_BRICK_POT.get().defaultBlockState();
            case 2 ->
                    AllBlockItem.BRICK_CAULDRON.get().defaultBlockState();
            case 3 ->
                    AllBlockItem.IRON_CAULDRON.get().defaultBlockState();
            case 4 ->
                    AllBlockItem.GOLDEN_CAULDRON.get().defaultBlockState();
            default ->
                    AllBlockItem.MUD_BRICK_POT.get().defaultBlockState();
        };
        BlockState heaterState = switch (recipe.value().getFireLevel()){
            case 0 ->
                Blocks.GRASS_BLOCK.defaultBlockState();
            case 1 ->
                AllBlockItem.BRICK_STOVE.get().defaultBlockState().setValue(BlockStateProperties.LIT, Boolean.valueOf(true)).setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
            default ->
                    AllBlockItem.BLACKSTONE_STOVE.get().defaultBlockState().setValue(GoldenStoveBlock.LIT_SOUL, NetherFireState.SOUL).setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.SOUTH);
        };
        ItemRenderer itemRender = VirtualLevel.getItemRenderer();
        BlockRenderDispatcher blockRender = VirtualLevel.getBlockRenderer();
        Lighting.setupLevel();
        RenderSystem.enableDepthTest();
        MultiBufferSource.BufferSource buffer = guiGraphics.bufferSource();
        PoseStack pose = guiGraphics.pose();
        pose.pushPose();
        pose.mulPose(Axis.XN.rotationDegrees(25f));
        pose.mulPose(Axis.YN.rotationDegrees(30f));
        pose.translate(92f, 48f, 20f);
        pose.scale(25f, -25f, 25f);
        blockRender.renderSingleBlock(heaterState, pose, buffer, 15728880, OverlayTexture.NO_OVERLAY);
        pose.popPose();
        pose.pushPose();
        pose.mulPose(Axis.XN.rotationDegrees(25f));
        pose.mulPose(Axis.YN.rotationDegrees(30f));
        pose.translate(92f, 23f, 20f);
        pose.scale(25f, -25f, 25f);
        blockRender.renderSingleBlock(state, pose, buffer, 15728880, OverlayTexture.NO_OVERLAY);
        pose.popPose();
        pose.pushPose();
        pose.mulPose(Axis.XN.rotationDegrees(25f));
        pose.mulPose(Axis.YN.rotationDegrees(30f));
        pose.translate(84f, -10f, 30f);
        pose.scale(15f, -15f, 15f);
        pose.popPose();
        buffer.endBatch();
        RenderSystem.disableDepthTest();
        Lighting.setupFor3DItems();
    }

    @Override
    public boolean isHandled(RecipeHolder<SmolderingRecipe> recipe) {
        if(recipe.value() instanceof FireBrewingRecipe)
            return false;
        return true;
    }
}
