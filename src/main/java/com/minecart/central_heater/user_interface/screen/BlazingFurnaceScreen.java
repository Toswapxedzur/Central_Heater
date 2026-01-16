package com.minecart.central_heater.user_interface.screen;

import com.minecart.central_heater.recipe.recipe_book.HauntingRecipeBookComponent;
import com.minecart.central_heater.user_interface.menu.BlazingFurnaceMenu;
import net.minecraft.client.gui.screens.inventory.AbstractFurnaceScreen;
import net.minecraft.client.gui.screens.recipebook.SmeltingRecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlazingFurnaceScreen extends AbstractFurnaceScreen<BlazingFurnaceMenu> {
    private static final ResourceLocation LIT_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/blazing_furnace/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace("container/blazing_furnace/burn_progress");
    private static final ResourceLocation TEXTURE = ResourceLocation.withDefaultNamespace("textures/gui/container/blazing_furnace.png");

    public BlazingFurnaceScreen(BlazingFurnaceMenu menu, Inventory playerInventory, Component title) {
        super(menu, new HauntingRecipeBookComponent(), playerInventory, title, TEXTURE, LIT_PROGRESS_SPRITE, BURN_PROGRESS_SPRITE);
    }
}
