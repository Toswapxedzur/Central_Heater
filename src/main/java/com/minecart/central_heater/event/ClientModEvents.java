package com.minecart.central_heater.event;

import com.minecart.central_heater.CentralHeater;
import com.minecart.central_heater.block_entity.AllBlockEntity;
import com.minecart.central_heater.block_entity_renderer.BurnableCampfireBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.pot.BrickCauldronBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.pot.CauldronBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.pot.GoldenCauldronBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.pot.MudBrickPotBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.stove.BrickStoveBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.stove.GoldenStoveBlockEntityRenderer;
import com.minecart.central_heater.block_entity_renderer.stove.StoneStoveBlockEntityRenderer;
import com.minecart.central_heater.entity.AllEntity;
import com.minecart.central_heater.entity_renderer.BlazingFurnaceMinecartRenderer;
import com.minecart.central_heater.recipe.AllRecipe;
import com.minecart.central_heater.recipe.AllRecipeBooks;
import com.minecart.central_heater.user_interface.AllMenu;
import com.minecart.central_heater.user_interface.screen.BlazingFurnaceScreen;
import net.minecraft.client.model.MinecartModel;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RegisterRecipeBookCategoriesEvent;

import java.util.List;

@EventBusSubscriber(modid = CentralHeater.MODID, value = Dist.CLIENT)
public class ClientModEvents {
    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
    }

    @SubscribeEvent
    public static void onRegisterRenderer(EntityRenderersEvent.RegisterRenderers event){
        event.registerBlockEntityRenderer(AllBlockEntity.stone_stove.get(), StoneStoveBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.red_nether_brick_stove.get(), GoldenStoveBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.brick_stove.get(), BrickStoveBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.burnable_campfire.get(), BurnableCampfireBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.pot.get(), MudBrickPotBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.brick_cauldron.get(), BrickCauldronBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.iron_cauldron.get(), CauldronBlockEntityRenderer::new);
        event.registerBlockEntityRenderer(AllBlockEntity.golden_cauldron.get(), GoldenCauldronBlockEntityRenderer::new);

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
