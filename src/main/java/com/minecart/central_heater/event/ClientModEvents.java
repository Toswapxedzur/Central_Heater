package com.minecart.central_heater.event;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.block_entity_renderer.BurnableCampfireBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.cauldron.CauldronBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.stove.BrickStoveBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.stove.CopperStoveBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.stove.GoldenStoveBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.stove.StoneStoveBlockEntityRenderer;
import com.minecart.central_heater.client.ClientSimilarStackTooltip;
import com.minecart.central_heater.entity.AllEntity;
import com.minecart.central_heater.entity_renderer.BlazingFurnaceMinecartRenderer;
import com.minecart.central_heater.item.similar_stack.SimilarStackTooltip;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.AllRecipeBooks;
import com.minecart.central_heater.user_interface.AllMenu;
import com.minecart.central_heater.user_interface.screen.BlazingFurnaceScreen;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;

import java.util.List;

@EventBusSubscriber(modid = CentralHeater.MODID, value = Dist.CLIENT, bus = EventBusSubscriber.Bus.MOD)
public class ClientModEvents {
    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(AllBlockEntity.STONE_STOVE.get(), StoneStoveBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.RED_NETHER_BRICK_STOVE.get(), GoldenStoveBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.BRICK_STOVE.get(), BrickStoveBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.COPPER_STOVE.get(), CopperStoveBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.BURNABLE_CAMPFIRE.get(), BurnableCampfireBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.MUD_BRICK_POT.get(), CauldronBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.BRICK_CAULDRON.get(), CauldronBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.IRON_CAULDRON.get(), CauldronBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.GOLDEN_CAULDRON.get(), CauldronBlockEntityRenderer::new);

        event.registerEntityRenderer(AllEntity.PEBBLE.get(), ThrownItemRenderer::new);
        event.registerEntityRenderer(AllEntity.BLAZING_FURNACE_MINECART.get(), BlazingFurnaceMinecartRenderer::new);
    }

    @SubscribeEvent
    public static void registerLayerDefinition(EntityRenderersEvent.RegisterLayerDefinitions event){
        event.registerLayerDefinition(BlazingFurnaceMinecartRenderer.LAYER, MinecartModel::createBodyLayer);
    }

    @SubscribeEvent
    public static void onRegisterMenuScreens(RegisterMenuScreensEvent event){
        event.register(AllMenu.BLAZING_FURNACE.get(), BlazingFurnaceScreen::new);
    }

    @SubscribeEvent
    public static void registerTooltipComponents(RegisterClientTooltipComponentFactoriesEvent event) {
        event.register(SimilarStackTooltip.class, ClientSimilarStackTooltip::new);
    }

    @SubscribeEvent
    public static void registerRecipeBookCategories(RegisterRecipeBookCategoriesEvent event) {
        event.registerBookCategories(
                AllRecipeBooks.getBlazingType(),
                List.of(AllRecipeBooks.getBlazingSearch(), AllRecipeBooks.getBlazingMisc())
        );

        event.registerRecipeCategoryFinder(AllRecipe.HAUNTING.get(), recipeHolder -> {
            return AllRecipeBooks.getBlazingMisc();
        });

        event.registerRecipeCategoryFinder(AllRecipe.HAUNTING.get(), recipeHolder -> {
            return AllRecipeBooks.getBlazingSearch();
        });
    }
}
